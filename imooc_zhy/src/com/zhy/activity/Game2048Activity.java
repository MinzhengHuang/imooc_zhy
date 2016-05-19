package com.zhy.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.TextView;

import com.zhy.R;
import com.zhy.game.Game2048Layout;
import com.zhy.game.Game2048Layout.OnGame2048Listener;

public class Game2048Activity extends Activity implements OnGame2048Listener{

	private Game2048Layout mGame2048Layout;

	private TextView mScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048);
        mScore = (TextView) findViewById(R.id.id_score);
		mGame2048Layout = (Game2048Layout) findViewById(R.id.id_game2048);
		mGame2048Layout.setOnGame2048Listener(this);
    }

    @Override
	public void onScoreChange(int score) {
		mScore.setText("SCORE: " + score);
	}

	@Override
	public void onGameOver() {
		new AlertDialog.Builder(this).setTitle("GAME OVER")
				.setMessage("YOU HAVE GOT " + mScore.getText())
				.setPositiveButton("RESTART", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mGame2048Layout.restart();
					}
				}).setNegativeButton("EXIT", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).show();
	}
}
