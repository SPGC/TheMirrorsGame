package com.pumple.themirrorsgame.GraphicEngine.MenuSettings;

import android.graphics.Bitmap;

/**
 * Created by Pumple on 22.05.2018.
 */

public class Buttons {
    private boolean isActive = true;
    private int[] coordinates;
    private String name;
    private Bitmap bitmap;
    private int setLevel;
    public Buttons(int[] coordinates, String name, Bitmap bitmap) {
        this.coordinates = coordinates;
        this.name = name;
        this.bitmap = bitmap;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getSetLevel() {
        return setLevel;
    }

    void setSetLevel(int setLevel) {
        this.setLevel = setLevel;
    }

    public boolean isPressed(int x, int y){
        return (isActive && (x < getCoordinates()[2]) && (x > getCoordinates()[0]) &&
                (y > getCoordinates()[1]) && (y < getCoordinates()[3]));
    }

    public int[] getCoordinates() {
        return coordinates;
    }


    public String getName() {
        return name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
