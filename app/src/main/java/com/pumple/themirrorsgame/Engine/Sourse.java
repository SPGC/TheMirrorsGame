package com.pumple.themirrorsgame.Engine;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.themirrorsgame.R;
import com.pumple.themirrorsgame.*;
/**
 * Created by Илья on 30.03.2018.
 */

class Sourse extends Objects{
    //rot - поле отвечающее за ориентацию источника, принимает значения 0;90;180;270

    Sourse(int x, int y, int rot, Context context) {
        super(x,y,context);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.source);
        this.rot = rot;
    }

    public int getRot() {
        return rot;
    }
    @Override
    public boolean isHard(){
        return true;
    }
}
