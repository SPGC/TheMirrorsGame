package com.pumple.themirrorsgame.Engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class Laser {
    private ArrayList<Integer[]> list = new ArrayList<>();

    private Engine engine;

    private double k;

    private double z;

    private Objects[][] objects;

    private Canvas canvas;

    private int colorOfLaser = Color.RED;

    public int getColorOfLaser(){
        return colorOfLaser;
    }

    private int count;

    public void setCount(int count) {
        this.count = count;
    }

    double getK() {
        return k;
    }

    double getZ() {
        return z;
    }

    private boolean painterflag = true;

    public void setPainterflag(boolean painterflag) {
        this.painterflag = painterflag;
    }

    public void setColorOfLaser(int colorOfLaser) {
        this.colorOfLaser = colorOfLaser;
    }

    public ArrayList<Integer[]> getList() {
        return list;
    }

    Laser(Objects[][] objects, Canvas canvas, Integer[] coordinates1, Integer[] coordinates2) {
        this.objects = objects;
        this.canvas = canvas;
        list.add(coordinates1);
        list.add(coordinates2);
    }

    void setK(double k) {
        this.k = k;
    }

    void setZ(double z) {
        this.z = z;
    }

    void laser(int a, Engine engine){
        ArrayList<Integer[]> list1 = new ArrayList<>();
        for (int i = 0; i < a; i++) {
            list1.add(list.get(i));
        }
        list = list1;
        Integer[] b, b1;
        b = list.get(a - 2);
        b1 = list.get(a - 1);
        int x = b1[0], y = b1[1];
        if (!java.util.Objects.equals(b[0], b1[0])){
            Matrix delt = new Matrix(b[0], 1, b1[0], 1);
            Matrix deltk = new Matrix(b[1], 1, b1[1], 1);
            Matrix deltz = new Matrix(b[0], b[1], b1[0], b1[1]);
            k = deltk.count() / delt.count();
            z = deltz.count() / delt.count();
            if (b[0] < b1[0]){
                count = 1;
            } else {
                count = -1;
            }
        }
        else{
            k = -32000;
            z = x;
            if (b[1] < b1[1]) {
                count = 1;
            } else {
                count = -1;
            }
        }
        boolean flag = false;
        boolean prismCostilFlag = true;
        int bufX = -1, bufY = -1;
        boolean mirrorFlag = true;
        while (true) {
            if ((x == bufX) && (y == bufY)) {
                return;
            }
            bufX = x;
            bufY = y;
            if ((int) k != -32000) {
                x += count;
                y = (int) ((k * x) + z);
            } else {
                y += count;
            }
            int xCoordinate = x / (canvas.getWidth() / Engine.OBJECTS_WIDTH);
            int yCoordinate = y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT);
            if ((x  >= canvas.getWidth()) || (y  >= canvas.getHeight())
                    || (x <= 0) || (y <= 0)) {
                Integer[] integers = {x, y};
                list.add(integers);
                Log.i("Lazer","Bad end" + x + " " + y);
                return;
            } else if ((objects[xCoordinate][yCoordinate] == null || objects[xCoordinate][yCoordinate].getClass() == Painter.class &&
                    ((Painter) objects[xCoordinate][yCoordinate]).isCrossed(k, z, count)[0] == -1) && xCoordinate <= 19 && yCoordinate <= 9) {

                if (k != -32000) {
                    if (count > 0) {
                        if (k <= 0) {
                            if ((((xCoordinate) + 1) * (canvas.getWidth() / Engine.OBJECTS_WIDTH) - 1) * k + z >=
                                    (yCoordinate) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT)) {
                                x = ((xCoordinate) + count) * (canvas.getWidth() / Engine.OBJECTS_WIDTH);
                            } else {
                                x = (int) ((yCoordinate * (canvas.getHeight() / Engine.OBJECTS_HEIGHT) - z - 1) / k);
                            }
                        } else {
                            if ((((xCoordinate) + 1) * (canvas.getWidth() / Engine.OBJECTS_WIDTH) - 1) * k + z <=
                                    (yCoordinate + 1) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT) - 1) {
                                x = ((xCoordinate) + count) * (canvas.getWidth() / Engine.OBJECTS_WIDTH);
                            } else {
                                x = (int) (((yCoordinate + 1) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT) - z) / k);
                            }
                        }
                    } else {
                        if (k <= 0) {
                            if (((xCoordinate) * (canvas.getWidth() / Engine.OBJECTS_WIDTH) * k + z) <=
                                    (yCoordinate + 1) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT) - 1) {
                                x = xCoordinate * (canvas.getWidth() / Engine.OBJECTS_WIDTH) - 1;
                            } else {
                                x = (int) (((yCoordinate + 1) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT) - z) / k);
                            }
                        } else {
                            if ((((xCoordinate)) * (canvas.getWidth() / Engine.OBJECTS_WIDTH) * k + z) >=
                                    (yCoordinate) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT)) {
                                x = ((xCoordinate)) * (canvas.getWidth() / Engine.OBJECTS_WIDTH) - 1;
                            } else {
                                x = (int) (((yCoordinate) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT) - z - 1) / k);
                            }
                        }
                    }
                } else {
                    if (count > 0) {
                        y = (yCoordinate + 1) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT);
                    } else {
                        y = (yCoordinate) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT) - 1;
                    }
                }
                flag = true;
                mirrorFlag = true;
                prismCostilFlag = true;
                painterflag = true;
            } else if ((objects[xCoordinate][yCoordinate] != null) &&
                    (objects[xCoordinate][yCoordinate].getClass() == Mirror.class) &&
                    mirrorFlag && ((objects[xCoordinate][yCoordinate]).isCrossed(k, z, x, y)[0] == 0)/*Костылина, нужно переделать*/) {
                Integer[][] result = objects[xCoordinate][yCoordinate].performAction(k, z, x, y, count, this, engine);
                Collections.addAll(list, result);
                x = list.get(list.size() - 1)[0];
                y = list.get(list.size() - 1)[1];
                mirrorFlag = false;
            } else if ((objects[xCoordinate][yCoordinate] != null) &&
                    (objects[xCoordinate][yCoordinate].getClass() == Prism.class) && prismCostilFlag &&
                    ((objects[xCoordinate][yCoordinate]).isCrossed(k, z, x, y)[0] == 1)) {
                int[] ints = (objects[xCoordinate][yCoordinate]).isCrossed(k, z, x, y);
                list.add(new Integer[]{ints[1], ints[2]});
                Integer[][] result = objects[xCoordinate][yCoordinate].performAction(k, z, ints[1], ints[2], count, this, engine);
                Collections.addAll(list, result);
                x = list.get(list.size() - 1)[0];
                y = list.get(list.size() - 1)[1];
                prismCostilFlag = false;
            } else if ((objects[xCoordinate][yCoordinate] != null) &&
                    (objects[xCoordinate][yCoordinate].getClass() == Painter.class) &&
                    (((Painter) objects[xCoordinate][yCoordinate]).isCrossed(k, z, count)[0] == 0) && painterflag) {
                Painter painter = ((Painter) objects[xCoordinate][yCoordinate]);
                int[] coordinatesOfCross = painter.isCrossed(k, z, count);
                Laser laserOfOtherColor = new Laser(engine.getObjects(), engine.getCanvas(),
                        new Integer[]{coordinatesOfCross[1], coordinatesOfCross[2]}, new Integer[]{coordinatesOfCross[1] + count * 10, (int) ((coordinatesOfCross[1] + count * 10) * k + z)});
                laserOfOtherColor.setColorOfLaser(painter.getColor());
                laserOfOtherColor.setPainterflag(false);
                engine.addLaser(laserOfOtherColor);
                list.add(new Integer[]{coordinatesOfCross[3], coordinatesOfCross[4]});
                return;
            } else {
                if ((objects[xCoordinate][yCoordinate] != null)
                        && (objects[xCoordinate][yCoordinate].getClass() == Walls.class)) {
                    Integer[] integers = {x, y};
                    list.add(integers);
                    return;
                } else {
                    if ((objects[xCoordinate][yCoordinate] != null)
                            && (objects[xCoordinate][yCoordinate].getClass() == Container.class)) {
                        int[] ints = ((Container) (objects[xCoordinate][yCoordinate])).isComplete(k, z, count, colorOfLaser);
                        Log.i("Container", "Coordinates x = " + ints[1] + "; y = " + ints[2]);
                        list.add(new Integer[]{ints[1], ints[2], ints[0]});
                        return;
                    } else if ((objects[xCoordinate][yCoordinate] != null) &&
                            (objects[xCoordinate][yCoordinate].getClass() == Mirror.class) &&
                            mirrorFlag && ((objects[xCoordinate][yCoordinate]).isCrossed(k, z, x, y)[0] == 1)) {
                        list.add(new Integer[]{(objects[xCoordinate][yCoordinate]).isCrossed(k, z, x, y)[1],
                                (objects[xCoordinate][yCoordinate]).isCrossed(k, z, x, y)[2]});
                        return;
                    } else if ((objects[xCoordinate][yCoordinate] != null)
                            && (objects[xCoordinate][yCoordinate].getClass() == Sourse.class) && flag) {
                        Integer[] integers = {x, y};
                        list.add(integers);
                        return;
                    } else if ((objects[xCoordinate][yCoordinate] != null) &&
                            (objects[xCoordinate][yCoordinate].getClass() == Painter.class) &&
                            (((Painter) objects[xCoordinate][yCoordinate]).isCrossed(k, z, count)[0] == 1)) {
                        int[] coordinatesOfCross = ((Painter) objects[xCoordinate][yCoordinate]).isCrossed(k, z, count);
                        list.add(new Integer[]{coordinatesOfCross[1], coordinatesOfCross[2]});
                        return;
                    }
                }
            }
        }
    }
}
