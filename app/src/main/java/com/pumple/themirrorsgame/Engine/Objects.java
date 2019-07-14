package com.pumple.themirrorsgame.Engine;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Илья on 30.03.2018.
 */

public class Objects {
    private int x,y;
    protected Context context;
    Bitmap bitmap;

    /**
     * @param k0 - angle rate of part of laser
     * @param b0 - free rate of part of laser
     * @param x0 - x coordinate of entry of laser in square
     * @param y0 - y coordinate of entry of laser in square
     * @return Array of int[], if crossed then returned {0, xCross, yCross}, if bumped return {1, xBumped, yBumped} else
     * return {-1}
     */
    public int[] isCrossed(double k0, double b0, double x0, double y0){
        return null;
    }
    public Integer[][] performAction(double k, double b0, int x, int y, int count, Laser laser, Engine engine){
        return null;
    }
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
