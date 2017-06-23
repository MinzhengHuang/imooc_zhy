package com.zhy.changeskin;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;
import com.zhy.R;
import com.zhy.changeskin.base.BaseSkinActivity;
import com.zhy.changeskin.utils.L;

import java.io.File;
import java.lang.reflect.Method;

/**
 * http://blog.csdn.net/lmj623565791/article/details/41531475
 *
 * @author zhy
 */
public class ChangeSkinActivity extends BaseSkinActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private String mSkinPkgPath = Environment.getExternalStorageDirectory() + File.separator + "night_plugin.apk";
    private String[] mDatas = new String[]{"Activity", "Service", "Activity", "Service", "Activity", "Service", "Activity", "Service"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeskin);
        initView();
        initEvents();

    }

    private void initEvents() {

        mListView = (ListView) findViewById(R.id.id_listview);
        mListView.setAdapter(new ArrayAdapter<String>(this, -1, mDatas) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(ChangeSkinActivity.this).inflate(R.layout.item_changeskin, parent
                            , false);
                }

                TextView tv = (TextView) convertView.findViewById(R.id.id_tv_title);
                tv.setText(getItem(position));
                return convertView;
            }
        });


        mDrawerLayout.setDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;

                if (drawerView.getTag().equals("LEFT")) {
                    float leftScale = 1 - 0.3f * scale;
                    ViewHelper.setScaleX(mMenu, leftScale);
                    ViewHelper.setScaleY(mMenu, leftScale);
                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                    ViewHelper.setTranslationX(mContent, mMenu.getMeasuredWidth() * (1 - scale));
                    ViewHelper.setPivotX(mContent, 0);
                    ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }
        });
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.id_left_menu_container);
        if (fragment == null) {
            fm.beginTransaction().add(R.id.id_left_menu_container, new MenuLeftFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_changeskin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.id_action_plugin_skinchange:
                com.zhy.changeskin.SkinManager.getInstance().changeSkin(mSkinPkgPath, "com.zhy.plugin", new com.zhy.changeskin.callback.ISkinChangingCallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(ChangeSkinActivity.this, "换肤失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(ChangeSkinActivity.this, "换肤成功", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.id_action_remove_any_skin:
                com.zhy.changeskin.SkinManager.getInstance().removeAnySkin();
                break;
            case R.id.id_action_test_res:
                AssetManager assetManager = null;
                try {
                    assetManager = AssetManager.class.newInstance();
                    Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                    addAssetPath.invoke(assetManager, mSkinPkgPath);

                    File file = new File(mSkinPkgPath);
                    L.e(file.exists() + "");
                    Resources superRes = getResources();
                    Resources mResources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());

                    int mainBgId = mResources.getIdentifier("skin_main_bg", "drawable", "com.zhy.plugin");
                    findViewById(R.id.id_drawerLayout).setBackgroundDrawable(mResources.getDrawable(mainBgId));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }


        return super.onOptionsItemSelected(item);
    }

}
