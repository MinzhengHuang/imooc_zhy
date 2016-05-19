package com.zhy.activity;

import android.app.Activity;
import android.os.Bundle;

import com.zhy.R;
import com.zhy.game.PintuLayout;

public class GamePintuActivity extends Activity {
	PintuLayout mGameView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_pintu);
        mGameView = (PintuLayout) findViewById(R.id.id_gameview);

    }

}
