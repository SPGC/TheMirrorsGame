package com.pumple.themirrorsgame.Engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;

public class Laser {
    private ArrayList<Integer[]> list = new ArrayList<>();

    private double k;

    private double z;

    private Objects[][] objects;

    private Canvas canvas;

    private int colorOfLaser = Color.RED;

    public int getColorOfLaser(){
        return colorOfLaser;
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

    void lazer(int a, Engine engine){
        ArrayList<Integer[]> list1 = new ArrayList<>();
        for (int i = 0; i < a; i++) {
            list1.add(list.get(i));
        }
        list = list1;
        Integer[] b, b1;
        b = list.get(a - 2);
        b1 = list.get(a - 1);
        int count, x = b1[0], y = b1[1];
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
            //Log.i("MessageCostil", "x = " + x + " y = " + y + " k = " + k + "count = " + count);
            if ((int) k != -32000) {
                x += count;
                y = (int) ((k * x) + z);
            } else {
                y += count;
            }
            if ((x  >= canvas.getWidth()) || (y  >= canvas.getHeight())
                    || (x <= 0) || (y <= 0)) {
                Integer[] integers = {x, y};
                list.add(integers);
                Log.i("Lazer","Bad end" + x + " " + y);
                return;
            } else if (((objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)] == null) ||
                    ((objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)].getClass() == Painter.class) &&
                            (((Painter) objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)]).isCrossed(k, z, count)[0] == -1))) &&
                            (x / (canvas.getWidth() / Engine.OBJECTS_WIDTH) >= 0) && (x / (canvas.getWidth() / Engine.OBJECTS_WIDTH) <= 19) &&
                            (y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT) >= 0) && (y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT) <= 9)) {
                if (k != -32000) {
                    if (count > 0) {
                        if (k <= 0) {
                            if ((((x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)) + 1) * (canvas.getWidth() / Engine.OBJECTS_WIDTH) - 1) * k + z >=
                                    (y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT)) {
                                x = ((x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)) + count) * (canvas.getWidth() / Engine.OBJECTS_WIDTH);
                            } else {
                                x = (int) (((y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT) - z - 1) / k);
                            }
                        } else {
                            if ((((x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)) + 1) * (canvas.getWidth() / Engine.OBJECTS_WIDTH) - 1) * k + z <=
                                    (y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT) + 1) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT) - 1) {
                                x = ((x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)) + count) * (canvas.getWidth() / Engine.OBJECTS_WIDTH);
                            } else {
                                x = (int) (((y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT) + 1) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT) - z) / k);
                            }
                        }
                    } else {
                        if (k <= 0) {
                            if (((x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)) * (canvas.getWidth() / Engine.OBJECTS_WIDTH) * k + z) <=
                                    (y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT) + 1) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT) - 1) {
                                x = x / (canvas.getWidth() / Engine.OBJECTS_WIDTH) * (canvas.getWidth() / Engine.OBJECTS_WIDTH) - 1;
                            } else {
                                x = (int) (((y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT) + 1) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT) - z) / k);
                            }
                        } else {
                            if ((((x / (canvas.getWidth() / Engine.OBJECTS_WIDTH))) * (canvas.getWidth() / Engine.OBJECTS_WIDTH) * k + z) >=
                                    (y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT)) {
                                x = ((x / (canvas.getWidth() / Engine.OBJECTS_WIDTH))) * (canvas.getWidth() / Engine.OBJECTS_WIDTH) - 1;
                            } else {
                                x = (int) (((y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT) - z - 1) / k);
                            }
                        }
                    }
                } else {
                    if (count > 0) {
                        y = (y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT) + 1) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT);
                    } else {
                        y = (y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)) * (canvas.getHeight() / Engine.OBJECTS_HEIGHT) - 1;
                    }
                }
                flag = true;
                mirrorFlag = true;
                prismCostilFlag = true;
                painterflag = true;
            } else if ((objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)] != null) &&
                    (objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)].getClass() == Mirror.class) &&
                    mirrorFlag && (((Mirror) objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)]).isCrossed(x, y, k, z)[0] == 0)/*Костылина, нужно переделать*/) {
                Integer[] integers = ((Mirror) (objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)]
                        [y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)])).reflect(k, z, x, y, count, this);
                list.add(new Integer[]{integers[0], integers[1]});
                list.add(new Integer[]{integers[2], integers[3]});
                if (k != -32000) {
                    count = integers[0] > integers[2] ? -1 : 1;
                    x = integers[2] + count;
                    y = (int) (x * k + z);
                } else {
                    count = integers[1] > integers[3] ? -1 : 1;
                    x = integers[2];
                    y = integers[3];
                }

                mirrorFlag = false;
                //Log.i("Mirror", "Reflected " + x + " " + y + " " + k + " " + count);
            } else if ((objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)] != null) &&
                    (objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)].getClass() == Prism.class) && prismCostilFlag &&
                    (((Prism) objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)]).isCrossed(k, z, x, y)[0] == 1)) {
                double[] doubles = ((Prism) objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)]).isCrossed(k, z, x, y);
                list.add(new Integer[]{(int) doubles[1], (int) doubles[2]});
                Object[] result = ((Prism) objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)]).refract((int) doubles[1], (int) doubles[2], k, z, true);
                k = ((Double) result[1]);
                z = ((Double) result[2]);
                for (int i = 3; i < result.length; i++) {
                    list.add((Integer[]) result[i]);
                }
                x = list.get(list.size() - 1)[0];
                y = list.get(list.size() - 1)[1];
                if ((Integer) result[0] != 0) {
                    count = (Integer) result[0];
                }
                //Log.i("Prism", "Refracted" + x + " " + y + " " + k + " " + z);
                prismCostilFlag = false;
            } else if ((objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)] != null) &&
                    (objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)].getClass() == Painter.class) &&
                    (((Painter) objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)]).isCrossed(k, z, count)[0] == 0) && painterflag) {
                Painter painter = ((Painter) objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)]);
                int[] coordinatesOfCross = painter.isCrossed(k, z, count);
                Laser laserOfOtherColor = new Laser(engine.getObjects(), engine.getCanvas(),
                        new Integer[]{coordinatesOfCross[1], coordinatesOfCross[2]}, new Integer[]{coordinatesOfCross[1] + count * 10, (int) ((coordinatesOfCross[1] + count * 10) * k + z)});
                laserOfOtherColor.setColorOfLaser(painter.getColor());
                laserOfOtherColor.setPainterflag(false);
                engine.addLaser(laserOfOtherColor);
                list.add(new Integer[]{coordinatesOfCross[3], coordinatesOfCross[4]});
                Log.i("Painter", "Painter has been reached");
                return;
            } else {
                if ((objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)] != null)
                        && (objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)].getClass() == Walls.class)) {
                    Integer[] integers = {x, y};
                    list.add(integers);
                    //Log.i("Wall", "Bumped");
                    return;
                } else {
                    if ((objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)] != null)
                            && (objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)].getClass() == Container.class)) {
                        int[] ints = ((Container) (objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)])).isComplete(k, z, count, colorOfLaser);
                        Log.i("Container", "Coordinates x = " + ints[1] + "; y = " + ints[2]);
                        list.add(new Integer[]{ints[1], ints[2], ints[0]});
                        return;
                    } else if ((objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)] != null) &&
                            (objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)].getClass() == Mirror.class) &&
                            mirrorFlag && (((Mirror) objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)]).isCrossed(x, y, k, z)[0] == 1)) {
                        list.add(new Integer[]{((Mirror) objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)]).isCrossed(x, y, k, z)[1],
                                ((Mirror) objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)]).isCrossed(x, y, k, z)[2]});
                        //Log.i("Mirror", "Bumped " + (objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)]).getRot());
                        return;
                    } else if ((objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)] != null)
                            && (objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)].getClass() == Sourse.class) && flag) {
                        Integer[] integers = {x, y};
                        list.add(integers);
                        //Log.i("Source", "Bumped");
                        return;
                    } else if ((objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)] != null) &&
                            (objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)].getClass() == Painter.class) &&
                            (((Painter) objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)]).isCrossed(k, z, count)[0] == 1)) {
                        int[] coordinatesOfCross = ((Painter) objects[x / (canvas.getWidth() / Engine.OBJECTS_WIDTH)][y / (canvas.getHeight() / Engine.OBJECTS_HEIGHT)]).isCrossed(k, z, count);
                        list.add(new Integer[]{coordinatesOfCross[1], coordinatesOfCross[2]});
                        return;
                    }
                }
            }
        }
    }
}
