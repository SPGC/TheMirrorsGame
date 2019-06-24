package com.pumple.themirrorsgame.Engine;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Илья on 30.03.2018.
 */

public class Objects {
    private int x,y;
    Context context;
    Bitmap bitmap;
    protected boolean isHard(){
        return false;
    }
    public boolean isShining(){
        return false;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getRot() {
        return rot;
    }

    int rot; //Угол наклона объекта

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    Objects(int x, int y, Context context) {
        this.x = x;
        this.y = y;
        this.context = context;
    }

}
