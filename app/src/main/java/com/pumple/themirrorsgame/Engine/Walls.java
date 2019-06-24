package com.pumple.themirrorsgame.Engine;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.themirrorsgame.R;

/**
 * Created by Илья on 30.03.2018.
 */

class Walls extends Objects{
    private static final boolean isShining = false;
    private static final int size = 1;

    Walls(int x, int y, Context context) {
        super(x,y, context);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.walls);
    }
    @Override
    public boolean isHard(){
        return true;
    }


}
