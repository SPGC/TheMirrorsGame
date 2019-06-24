package com.pumple.themirrorsgame;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.pumple.themirrorsgame.GraphicEngine.GraphicEngine;

/**
 * Created by Pumple on 23.05.2018.
 */

public class GameActivity extends Activity {
    GameActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        int level = getIntent().getExtras().getInt("level");
        GraphicEngine engine = new GraphicEngine(this, level, activity);
        setContentView(engine); // Запуск отрисовщика уровня
    }
}
