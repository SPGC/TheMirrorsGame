package com.pumple.themirrorsgame.Engine;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.example.themirrorsgame.R;


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Pumple on 13.05.2018.
 */

class Container extends Objects {
    private Engine engine;
    private int[][] coordinates = new int[4][2];
    private int color;
    Container(int x, int y, int rot, Context context, Engine engine, int color) {
        super(x, y, context);
        this.engine = engine;
        this.rot = rot;
        switch (color){
            case 1:
                this.color = Color.RED;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.containerred);
                break;
            case 2:
                this.color = Color.GREEN;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.containergreen);
                break;
            case 3:
                this.color = Color.BLUE;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.containerblue);
                break;
            case 4:
                this.color = Color.MAGENTA;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.containermagenta);
                break;
        }
        countCoordinates();
    }

    private void countCoordinates() {
        coordinates[0][0] = getX() * engine.getWidth() / Engine.OBJECTS_WIDTH;
        coordinates[0][1] = (getY() + 1) * engine.getHeight() / Engine.OBJECTS_HEIGHT - 1;
        coordinates[1][0] = getX() * engine.getWidth() / Engine.OBJECTS_WIDTH;
        coordinates[1][1] = getY() * engine.getHeight() / Engine.OBJECTS_HEIGHT;
        coordinates[2][0] = (getX() + 1) * engine.getWidth() / Engine.OBJECTS_WIDTH - 1;
        coordinates[2][1] = getY() * engine.getHeight() / Engine.OBJECTS_HEIGHT;
        coordinates[3][0] = (getX() + 1) * engine.getWidth() / Engine.OBJECTS_WIDTH - 1;
        coordinates[3][1] = (getY() + 1) * engine.getHeight() / Engine.OBJECTS_HEIGHT - 1;
    }
    int[] isComplete(double k, double z, int count, int color){
        double[] crossLeft = MyMath.isCrossedLines(k, z, coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1]);
        double[] crossUp = MyMath.isCrossedLines(k, z, coordinates[1][0], coordinates[1][1], coordinates[2][0], coordinates[2][1]);
        double[] crossRight = MyMath.isCrossedLines(k, z, coordinates[2][0], coordinates[2][1], coordinates[3][0], coordinates[3][1]);
        double[] crossDown = MyMath.isCrossedLines(k, z, coordinates[0][0], coordinates[0][1], coordinates[3][0], coordinates[3][1]);
        double[][] crosses = new double[4][3];
        int counter = 0;
        if (crossDown[0] == 1) {
            crosses[counter][0] = crossDown[1];
            crosses[counter][1] = crossDown[2];
            crosses[counter][2] = 2;
            counter++;
        }
        if (crossLeft[0] == 1){
            crosses[counter][0] = crossLeft[1];
            crosses[counter][1] = crossLeft[2];
            crosses[counter][2] = 3;
            counter++;
            Log.i("Container Left Side", "x = " + crosses[counter][0] + " y = " + crosses[counter][1]);
        }
        if (crossUp[0] == 1){
            crosses[counter][0] = crossUp[1];
            crosses[counter][1] = crossUp[2];
            crosses[counter][2] = 4;
            counter++;
        }
        if (crossRight[0] == 1){
            crosses[counter][0] = crossRight[1];
            crosses[counter][1] = crossRight[2];
            crosses[counter][2] = 1;
            counter++;
        }
        class MyComparator implements Comparator<double[]> {
            public int compare(double[] a, double[] b) {
                return (int)(a[0] - b[0]);
            }
        }
        Arrays.sort(crosses,new MyComparator());
        double[] cross;
        if (count > 0) {
            cross = crosses[4 - counter];
        } else {
            cross = crosses[3];
        }
        switch(rot){
            case 0:
                if ((cross[2] == 1) && (this.color == color)) {
                    return new int[]{1, (int) cross[0], (int) cross[1]};
                } else {
                    return new int[]{0, (int) cross[0], (int) cross[1]};
                }
            case 90:
                if ((cross[2] == 2) && (this.color == color)){
                    return new int[]{1, (int) cross[0], (int) cross[1]};
                } else {
                    return new int[]{0, (int) cross[0], (int) cross[1]};
                }
            case 180:
                if ((cross[2] == 3) && (this.color == color)) {
                    return new int[]{1, (int) cross[0], (int) cross[1]};
                } else {
                    return new int[]{0, (int) cross[0], (int) cross[1]};
                }
            case 270:
                if ((cross[2] == 4) && (this.color == color)) {
                    return new int[]{1, (int) cross[0], (int) cross[1]};
                } else {
                    return new int[]{0, (int) cross[0], (int) cross[1]};
                }
            default:
                return new int[]{-1};
        }
        }
    @Override
    public boolean isShining() {
        return false;
    }

    @Override
    public boolean isHard() {
        return true;
    }
}
