package com.zhy.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.zhy.R;
import com.zhy.game.PintuLayout;

/**
 * http://www.imooc.com/learn/224
 */
public class GamePintuActivity extends Activity {
	PintuLayout mGameView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_pintu);
        mGameView = (PintuLayout) findViewById(R.id.id_gameview);
        mGameView.setOnGamePintuListener(new PintuLayout.GamePintuListener() {
            @Override
            public void nextLevel(int level) {
                new AlertDialog.Builder(GamePintuActivity.this).setTitle("Game Iofo").setMessage("LEVEL UP!")
                .setPositiveButton("next level", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGameView.nextLevel();
                    }
                }).show();
            }

            @Override
            public void timeChanged(int currentTime) {

            }

            @Override
            public void gameOver() {

            }
        });

    }

}
