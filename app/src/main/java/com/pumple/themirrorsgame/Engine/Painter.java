package com.pumple.themirrorsgame.Engine;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.example.themirrorsgame.R;

/**
 * Created by Admin on 01.04.2019.
 */

public class Painter extends Objects {
    private int color;

    private Engine engine;

    public int getColor() {
        return color;
    }

    private int[][] coordinates = new int[4][2];

    private void countCoordinates(){
        try{
        if (rot == 0) {

            coordinates[0][0] = getX() * engine.getWidth()/ Engine.OBJECTS_WIDTH;
            coordinates[0][1] = (getY() + 1) * engine.getHeight() / Engine.OBJECTS_HEIGHT -
                    engine.getHeight() / Engine.OBJECTS_HEIGHT / 3 - 1;
            coordinates[1][0] = getX() * engine.getWidth() / Engine.OBJECTS_WIDTH;
            coordinates[1][1] = getY() * engine.getHeight() / Engine.OBJECTS_HEIGHT +
                    engine.getHeight() / Engine.OBJECTS_HEIGHT / 3;
            coordinates[2][0] = (getX() + 1) * engine.getWidth() / Engine.OBJECTS_WIDTH - 1;
            coordinates[2][1] = getY() * engine.getHeight() / Engine.OBJECTS_HEIGHT +
                    engine.getHeight() / Engine.OBJECTS_HEIGHT / 3;
            coordinates[3][0] = (getX() + 1) * engine.getWidth() / Engine.OBJECTS_WIDTH - 1;
            coordinates[3][1] = (getY() + 1) * engine.getHeight() / Engine.OBJECTS_HEIGHT -
                    engine.getHeight() / Engine.OBJECTS_HEIGHT / 3 - 1;
        } else {
            coordinates[0][0] = getX() * engine.getWidth() / Engine.OBJECTS_WIDTH +
                    engine.getWidth() / Engine.OBJECTS_WIDTH / 3;
            coordinates[0][1] = (getY() + 1) * engine.getHeight() / Engine.OBJECTS_HEIGHT - 1;
            coordinates[1][0] = getX() * engine.getWidth() / Engine.OBJECTS_WIDTH +
                    engine.getWidth() / Engine.OBJECTS_WIDTH / 3;
            coordinates[1][1] = getY() * engine.getHeight() / Engine.OBJECTS_HEIGHT;
            coordinates[2][0] = (getX() + 1) * engine.getWidth() / Engine.OBJECTS_WIDTH -
                    engine.getWidth() / Engine.OBJECTS_WIDTH / 3 - 1;
            coordinates[2][1] = getY() * engine.getHeight() / Engine.OBJECTS_HEIGHT;
            coordinates[3][0] = (getX() + 1) * engine.getWidth() / Engine.OBJECTS_WIDTH -
                    engine.getWidth() / Engine.OBJECTS_WIDTH / 3 - 1;
            coordinates[3][1] = (getY() + 1) * engine.getHeight() / Engine.OBJECTS_HEIGHT - 1;
        }
        } catch (Exception e){
            Log.e("Painter", e.toString());
        }
    }
    Painter(int x, int y, Context context, int color, int rotation, Engine engine) {
        super(x, y, context);
        rot = rotation;
        this.engine = engine;
        countCoordinates();
        switch (color){
            case 1:
                this.color = Color.RED;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.painterred);
                break;
            case 2:
                this.color = Color.GREEN;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.paintergreen);
                break;
            case 3:
                this.color = Color.BLUE;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.painterblue);
                break;
            case 4:
                this.color = Color.MAGENTA;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.paintermagenta);
                break;
        }

    }

    int[] isCrossed(double k, double z, int count) {
        double[] crossCoordinatesLeft = MyMath.isCrossedLines(k, z,
                coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1]);
        double[] crossCoordinatesRight = MyMath.isCrossedLines(k, z,
                coordinates[2][0], coordinates[2][1], coordinates[3][0], coordinates[3][1]);
        double[] crossCoordinatesDown = MyMath.isCrossedLines(k, z,
                coordinates[0][0], coordinates[0][1], coordinates[3][0], coordinates[3][1]);
        double[] crossCoordinatesUp = MyMath.isCrossedLines(k, z,
                coordinates[1][0], coordinates[1][1], coordinates[2][0], coordinates[2][1]);
        if (rot == 0) {
            if (((k < 0) && (count > 0)) || ((k > 0) && (count < 0))) {
                if (crossCoordinatesDown[0] == 1) {
                    if (crossCoordinatesLeft[0] == 1) {
                        return new int[]{1, (int) crossCoordinatesDown[1], (int) crossCoordinatesDown[2]};
                    } else if (crossCoordinatesRight[0] == 1) {
                        return new int[]{1, (int) crossCoordinatesDown[1], (int) crossCoordinatesDown[2]};
                    }
                    return new int[]{0, (int) crossCoordinatesUp[1], (int) crossCoordinatesUp[2], (int) crossCoordinatesDown[1], (int) crossCoordinatesDown[2]};
                } else {
                    if (crossCoordinatesLeft[0] == 1) {
                        return new int[]{1, (int) crossCoordinatesLeft[1], (int) crossCoordinatesLeft[2]};
                    } else if (crossCoordinatesRight[0] == 1) {
                        return new int[]{1, (int) crossCoordinatesRight[1], (int) crossCoordinatesRight[2]};
                    }
                    return new int[]{-1};
                }
            } else {
                if (crossCoordinatesUp[0] == 1) {
                    if (crossCoordinatesLeft[0] == 1) {
                        return new int[]{1, (int) crossCoordinatesUp[1], (int) crossCoordinatesUp[2]};
                    } else if (crossCoordinatesRight[0] == 1) {
                        return new int[]{1, (int) crossCoordinatesUp[1], (int) crossCoordinatesUp[2]};
                    }
                    return new int[]{0, (int) crossCoordinatesDown[1], (int) crossCoordinatesDown[2], (int) crossCoordinatesUp[1], (int) crossCoordinatesUp[2]};
                } else {
                    if (crossCoordinatesLeft[0] == 1) {
                        return new int[]{1, (int) crossCoordinatesLeft[1], (int) crossCoordinatesLeft[2]};
                    } else if (crossCoordinatesRight[0] == 1) {
                        return new int[]{1, (int) crossCoordinatesRight[1], (int) crossCoordinatesRight[2]};
                    }
                    return new int[]{-1};
                }
            }
        } else {
            if (count == 1) {
                if (crossCoordinatesLeft[0] == 1) {
                    if (crossCoordinatesDown[0] == 1) {
                        return new int[]{1, (int) crossCoordinatesLeft[1], (int) crossCoordinatesLeft[2]};
                    } else if (crossCoordinatesUp[0] == 1) {
                        return new int[]{1, (int) crossCoordinatesLeft[1], (int) crossCoordinatesLeft[2]};
                    }
                    return new int[]{0, (int) crossCoordinatesRight[1], (int) crossCoordinatesRight[2], (int) crossCoordinatesLeft[1], (int) crossCoordinatesLeft[2]};
                } else {
                    if (crossCoordinatesDown[0] == 1) {
                        return new int[]{1, (int) crossCoordinatesDown[1], (int) crossCoordinatesDown[2]};
                    } else if (crossCoordinatesUp[0] == 1) {
                        return new int[]{1, (int) crossCoordinatesUp[1], (int) crossCoordinatesUp[2]};
                    }
                    return new int[]{-1};
                }
            } else {
                if (crossCoordinatesRight[0] == 1) {
                    if (crossCoordinatesDown[0] == 1) {
                        return new int[]{1, (int) crossCoordinatesRight[1], (int) crossCoordinatesRight[2]};
                    } else if (crossCoordinatesUp[0] == 1) {
                        return new int[]{1, (int) crossCoordinatesRight[1], (int) crossCoordinatesRight[2]};
                    }
                    return new int[]{0, (int) crossCoordinatesLeft[1], (int) crossCoordinatesLeft[2], (int) crossCoordinatesRight[1], (int) crossCoordinatesRight[2]};
                } else {
                    if (crossCoordinatesDown[0] == 1) {
                        return new int[]{1, (int) crossCoordinatesDown[1], (int) crossCoordinatesDown[2]};
                    } else if (crossCoordinatesUp[0] == 1) {
                        return new int[]{1, (int) crossCoordinatesUp[1], (int) crossCoordinatesUp[2]};
                    }
                    return new int[]{-1};
                }
            }
        }
    }
}
