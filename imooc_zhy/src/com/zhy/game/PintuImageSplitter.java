package com.zhy.game;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.util.Log;

public class PintuImageSplitter {
	/**
	 * 将图片切成 , piece *piece
	 * 
	 * @param bitmap
	 * @param piece
	 * @return
	 */
	public static List<PintuImagePiece> split(Bitmap bitmap, int piece) {

		List<PintuImagePiece> pieces = new ArrayList<PintuImagePiece>(piece * piece);

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int pieceWidth = Math.min(width, height) / piece;

		for (int i = 0; i < piece; i++) {
			for (int j = 0; j < piece; j++) {
				PintuImagePiece imagePiece = new PintuImagePiece();
				imagePiece.index = j + i * piece;

				Log.e("TAG", "imagePiece.index" + (j + i * piece));

				int xValue = j * pieceWidth;
				int yValue = i * pieceWidth;

				imagePiece.bitmap = Bitmap.createBitmap(bitmap, xValue, yValue,
						pieceWidth, pieceWidth);
				pieces.add(imagePiece);
			}
		}
		return pieces;
	}
}
