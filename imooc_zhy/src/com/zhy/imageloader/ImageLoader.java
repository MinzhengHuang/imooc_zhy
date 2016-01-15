package com.zhy.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ImageLoader {
	/** 图片缓存的核心类 */
	private LruCache<String, Bitmap> mLruCache;

	/** 线程池 */
	private ExecutorService mThreadPool;

	/** 线程池的线程数量，默认为1 */
	private int mThreadCount = 1;

	/** 队列的调度方式 */
	private Type mType = Type.LIFO;

	/** 任务队列 */
	private LinkedList<Runnable> mTasks;

	/** 轮询的线程 */
	private Thread mPoolThread;

	private Handler mPoolThreadHander;

	/** 运行在UI线程的handler，用于给ImageView设置图片 */
	private Handler mUIHandler;

	/** 引入一个值为1的信号量，防止mPoolThreadHander未初始化完成 */
	private volatile Semaphore mSemaphore = new Semaphore(0);

	/** 引入一个值为1的信号量，由于线程池内部也有一个阻塞线程，防止加入任务的速度过快，使LIFO效果不明显 */
	private volatile Semaphore mPoolSemaphore;

	private static ImageLoader mInstance;

	/**
	 * 队列的调度方式
	 */
	public enum Type {
		FIFO, LIFO
	}

	/**
	 * 单例获得该实例对象
	 */
	public static ImageLoader getInstance() {

		if (mInstance == null) {
			synchronized (ImageLoader.class) {
				if (mInstance == null) {
					mInstance = new ImageLoader(1, Type.LIFO);
				}
			}
		}
		return mInstance;
	}

	private ImageLoader(int threadCount, Type type) {
		init(threadCount, type);
	}

	private void init(int threadCount, Type type) {
		// loop thread
		mPoolThread = new Thread() {

			@Override
			public void run() {
				Looper.prepare();
				mPoolThreadHander = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						//线程池取出一个任务执行
						mThreadPool.execute(getTask());
						try {
							mPoolSemaphore.acquire();
						} catch (InterruptedException e) {

						}
					}
				};
				// 释放一个信号量
				mSemaphore.release();
				Looper.loop();
			}
		};
		mPoolThread.start();

		// 获取应用程序最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		mLruCache = new LruCache<String, Bitmap>(cacheSize) {

			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			};
		};

		//创建线程池
		mThreadPool = Executors.newFixedThreadPool(threadCount);
		mPoolSemaphore = new Semaphore(threadCount);
		mTasks = new LinkedList<Runnable>();
		mType = type == null ? Type.LIFO : type;

	}

	/**
	 * 根据path加载图片
	 * 
	 * @param path
	 * @param imageView
	 */
	public void loadImage(final String path, final ImageView imageView) {
		// set tag
		imageView.setTag(path);
		// UI线程
		if (mUIHandler == null) {
			mUIHandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					//获取得到的图片，为imageView回调设置图片
					ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
					ImageView imageView = holder.imageView;
					Bitmap bm = holder.bitmap;
					String path = holder.path;
					if (imageView.getTag().toString().equals(path)) {
						imageView.setImageBitmap(bm);
					}
				}
			};
		}

		//根据path在缓存中获取bitmap
		Bitmap bm = getBitmapFromLruCache(path);
		if (bm != null) {
			ImgBeanHolder holder = new ImgBeanHolder();
			holder.bitmap = bm;
			holder.imageView = imageView;
			holder.path = path;
			Message message = Message.obtain();
			message.obj = holder;
			mUIHandler.sendMessage(message);
		} else {
			addTask(new Runnable() {

				@Override
				public void run() {
					ImageSize imageSize = getImageViewSize(imageView);
					int reqWidth = imageSize.width;
					int reqHeight = imageSize.height;
					Bitmap bm = decodeSampledBitmapFromResource(path, reqWidth,
							reqHeight);
					addBitmapToLruCache(path, bm);
					ImgBeanHolder holder = new ImgBeanHolder();
					holder.bitmap = getBitmapFromLruCache(path);
					holder.imageView = imageView;
					holder.path = path;
					Message message = Message.obtain();
					message.obj = holder;
					// Log.e("TAG", "mUIHandler.sendMessage(message);");
					mUIHandler.sendMessage(message);
					mPoolSemaphore.release();
				}
			});
		}

	}

	/**
	 * 添加一个任务
	 * 
	 * @param runnable
	 */
	private synchronized void addTask(Runnable runnable) {
		try {
			// 请求信号量，防止mPoolThreadHander为null
			if (mPoolThreadHander == null){
				mSemaphore.acquire();
			}
		} catch (InterruptedException e) {

		}
		mTasks.add(runnable);
		mPoolThreadHander.sendEmptyMessage(0x110);
	}

	/**
	 * 取出一个任务
	 * 
	 * @return
	 */
	private synchronized Runnable getTask() {
		if (mType == Type.FIFO) {
			return mTasks.removeFirst();
		} else if (mType == Type.LIFO) {
			return mTasks.removeLast();
		}
		return null;
	}

	/**
	 * 单例获得该实例对象
	 * 
	 * @return
	 */
	public static ImageLoader getInstance(int threadCount, Type type) {

		if (mInstance == null) {
			synchronized (ImageLoader.class) {
				if (mInstance == null) {
					mInstance = new ImageLoader(threadCount, type);
				}
			}
		}
		return mInstance;
	}

	/**
	 * 根据ImageView获得适当的压缩的宽和高
	 * 
	 * @param imageView
	 * @return
	 */
	private ImageSize getImageViewSize(ImageView imageView) {
		ImageSize imageSize = new ImageSize();
		final DisplayMetrics displayMetrics = imageView.getContext()
				.getResources().getDisplayMetrics();
		final LayoutParams params = imageView.getLayoutParams();

		int width = params.width == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getWidth(); // 获得imageView的实际宽度
		if (width <= 0){
			width = params.width; // 获得imageView在layout中声明的宽度
		}
		if (width <= 0){
			width = getImageViewFieldValue(imageView, "mMaxWidth"); // 检查最大值
		}

		if (width <= 0){
			width = displayMetrics.widthPixels;
		}
		int height = params.height == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getHeight(); // 获得imageView的实际高度
		if (height <= 0){
			height = params.height; // 获得imageView在layout中声明的高度
		}

		if (height <= 0){
			height = getImageViewFieldValue(imageView, "mMaxHeight"); // 检查最大值
		}
		if (height <= 0){
			height = displayMetrics.heightPixels;
		}

		imageSize.width = width;
		imageSize.height = height;
		return imageSize;

	}

	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 */
	private Bitmap getBitmapFromLruCache(String key) {
		return mLruCache.get(key);
	}

	/**
	 * 往LruCache中添加一张图片
	 * 
	 * @param key
	 * @param bitmap
	 */
	private void addBitmapToLruCache(String key, Bitmap bitmap) {
		if (getBitmapFromLruCache(key) == null) {
			if (bitmap != null){
				mLruCache.put(key, bitmap);
			}

		}
	}

	/**
	 * 计算inSampleSize，用于压缩图片
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 源图片的宽度
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;

		if (width > reqWidth && height > reqHeight) {
			// 计算出实际宽度和目标宽度的比率
			int widthRatio = Math.round((float) width / (float) reqWidth);
			int heightRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = Math.max(widthRatio, heightRatio);
		}
		return inSampleSize;
	}

	/**
	 * 根据计算的inSampleSize，得到压缩后图片
	 * 
	 * @param pathName
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private Bitmap decodeSampledBitmapFromResource(String pathName,
			int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小，不把图片加载到内存中
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth,reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);

		return bitmap;
	}

	private class ImgBeanHolder {
		Bitmap bitmap;
		ImageView imageView;
		String path;
	}

	private class ImageSize {
		int width;
		int height;
	}

	/**
	 * 反射获得ImageView设置的最大宽度和高度
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 */
	private static int getImageViewFieldValue(Object object, String fieldName) {
		int value = 0;
		try {
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
				value = fieldValue;
				Log.e("TAG", value + "");
			}
		} catch (Exception e) {
		}
		return value;
	}

}
