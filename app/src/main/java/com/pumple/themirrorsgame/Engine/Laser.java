package com.pumple.themirrorsgame.Engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class Laser {
    private ArrayList<Integer[]> list = new ArrayList<>();

    private double k;

    private double z;

    private boolean isReturn = false;

    public void setReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    private Objects[][] objects;

    private Canvas canvas;

    private int colorOfLaser = Color.RED;

    public int getColorOfLaser() {
        return colorOfLaser;
    }

    private int count;

    void setCount(int count) {
        this.count = count;
    }

    double getK() {
        return k;
    }

    double getZ() {
        return z;
    }

    private boolean painterFlag = true;

    void setPainterFlag(boolean painterFlag) {
        this.painterFlag = painterFlag;
    }

    void setColorOfLaser(int colorOfLaser) {
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

    void laser(int firstRecountablePoint, Engine engine) {
        ArrayList<Integer[]> list1 = new ArrayList<>();
        for (int i = 0; i < firstRecountablePoint; i++) {
            list1.add(list.get(i));
        }
        list = list1;
        Integer[] b, b1;
        b = list.get(firstRecountablePoint - 2);
        b1 = list.get(firstRecountablePoint - 1);
        int x = b1[0], y = b1[1];
        if (!java.util.Objects.equals(b[0], b1[0])) {
            Matrix delta = new Matrix(b[0], 1, b1[0], 1);
            Matrix deltaK = new Matrix(b[1], 1, b1[1], 1);
            Matrix deltaZ = new Matrix(b[0], b[1], b1[0], b1[1]);
            k = deltaK.count() / delta.count();
            z = deltaZ.count() / delta.count();
            if (b[0] < b1[0]) {
                count = 1;
            } else {
                count = -1;
            }
        } else {
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
            int widthInFields = canvas.getWidth() / Engine.OBJECTS_WIDTH;
            int heightInFields = canvas.getHeight() / Engine.OBJECTS_HEIGHT;
            int xCoordinate = x / widthInFields;
            int yCoordinate = y / heightInFields;
            Objects processedObject = objects[xCoordinate][yCoordinate];
            if ((x >= canvas.getWidth()) || (y >= canvas.getHeight())
                    || (x <= 0) || (y <= 0)) {
                Integer[] integers = {x, y};
                list.add(integers);
                Log.i("Laser", "Bad end" + x + " " + y);
                return;
            } else if ((processedObject == null || processedObject.getClass() == Painter.class &&
                    ((Painter) processedObject).isCrossed(k, z, count)[0] == -1) && xCoordinate <= 19 && yCoordinate <= 9) {

                if (k != -32000) {
                    if (count > 0) {
                        if (k <= 0) {
                            if ((((xCoordinate) + 1) * widthInFields - 1) * k + z >=
                                    (yCoordinate) * heightInFields) {
                                x = ((xCoordinate) + count) * widthInFields;
                            } else {
                                x = (int) ((yCoordinate * heightInFields - z - 1) / k);
                            }
                        } else {
                            if ((((xCoordinate) + 1) * widthInFields - 1) * k + z <=
                                    (yCoordinate + 1) * heightInFields - 1) {
                                x = ((xCoordinate) + count) * widthInFields;
                            } else {
                                x = (int) (((yCoordinate + 1) * heightInFields - z) / k);
                            }
                        }
                    } else {
                        if (k <= 0) {
                            if (((xCoordinate) * widthInFields * k + z) <=
                                    (yCoordinate + 1) * heightInFields - 1) {
                                x = xCoordinate * widthInFields - 1;
                            } else {
                                x = (int) (((yCoordinate + 1) * heightInFields - z) / k);
                            }
                        } else {
                            if ((((xCoordinate)) * widthInFields * k + z) >=
                                    (yCoordinate) * heightInFields) {
                                x = ((xCoordinate)) * widthInFields - 1;
                            } else {
                                x = (int) (((yCoordinate) * heightInFields - z - 1) / k);
                            }
                        }
                    }
                } else {
                    if (count > 0) {
                        y = (yCoordinate + 1) * heightInFields;
                    } else {
                        y = (yCoordinate) * heightInFields - 1;
                    }
                }
                flag = true;
                mirrorFlag = true;
                prismCostilFlag = true;
                painterFlag = true;
            } else if ((processedObject != null) &&
                    (processedObject.getClass() == Mirror.class) &&
                    mirrorFlag && ((processedObject).isCrossed(k, z, x, y)[0] == 0)/*Костылина, нужно переделать*/) {
                int[] ints = (processedObject).isCrossed(k, z, x, y);
                list.add(new Integer[]{ints[1], ints[2]});
                Integer[][] result = processedObject.performAction(k, z, ints[1], ints[2], count, this, engine);
                Collections.addAll(list, result);
                x = list.get(list.size() - 1)[0];
                y = list.get(list.size() - 1)[1];
                if (isReturn) {
                    return;
                }
                mirrorFlag = false;
            } else if ((processedObject != null) &&
                    (processedObject.getClass() == Prism.class) && prismCostilFlag &&
                    ((processedObject).isCrossed(k, z, x, y)[0] == 1)) {
                int[] ints = (processedObject).isCrossed(k, z, x, y);
                list.add(new Integer[]{ints[1], ints[2]});
                Integer[][] result = processedObject.performAction(k, z, ints[1], ints[2], count, this, engine);
                Collections.addAll(list, result);
                x = list.get(list.size() - 1)[0];
                y = list.get(list.size() - 1)[1];
                if (isReturn) {
                    return;
                }
                prismCostilFlag = false;
            } else if ((processedObject != null) &&
                    (processedObject.getClass() == Painter.class) &&
                    (((Painter) processedObject).isCrossed(k, z, count)[0] == 0) && painterFlag) {
//                Painter painter = ((Painter) processedObject);
//                int[] coordinatesOfCross = painter.isCrossed(k, z, count);
//                Laser laserOfOtherColor = new Laser(engine.getObjects(), engine.getCanvas(),
//                        new Integer[]{coordinatesOfCross[1], coordinatesOfCross[2]}, new Integer[]{coordinatesOfCross[1] + count * 10, (int) ((coordinatesOfCross[1] + count * 10) * k + z)});
//                laserOfOtherColor.setColorOfLaser(painter.getColor());
//                laserOfOtherColor.setPainterFlag(false);
//                engine.addLaser(laserOfOtherColor);
//                list.add(new Integer[]{coordinatesOfCross[3], coordinatesOfCross[4]});
                int[] ints = (processedObject).isCrossed(k, z, x, y);
                list.add(new Integer[]{ints[1], ints[2]});
                Integer[][] result = processedObject.performAction(k, z, ints[1], ints[2], count, this, engine);
                Collections.addAll(list, result);
                x = list.get(list.size() - 1)[0];
                y = list.get(list.size() - 1)[1];
                if (isReturn) {
                    return;
                }
            } else if ((processedObject != null) &&
                    (processedObject.getClass() == Painter.class) &&
                    (((Painter) processedObject).isCrossed(k, z, count)[0] == 1)) {
                int[] coordinatesOfCross = ((Painter) processedObject).isCrossed(k, z, count);
                list.add(new Integer[]{coordinatesOfCross[1], coordinatesOfCross[2]});
                return;
            } else if ((processedObject != null)
                    && (processedObject.getClass() == Walls.class)) {
                Integer[] integers = {x, y};
                list.add(integers);
                return;
            } else if ((processedObject != null)
                    && (processedObject.getClass() == Container.class)) {
                int[] ints = ((Container) (processedObject)).isComplete(k, z, count, colorOfLaser);
                Log.i("Container", "Coordinates x = " + ints[1] + "; y = " + ints[2]);
                list.add(new Integer[]{ints[1], ints[2], ints[0]});
                return;
            } else if ((processedObject != null) &&
                    (processedObject.getClass() == Mirror.class) &&
                    mirrorFlag && ((processedObject).isCrossed(k, z, x, y)[0] == 1)) {
                list.add(new Integer[]{(processedObject).isCrossed(k, z, x, y)[1],
                        (processedObject).isCrossed(k, z, x, y)[2]});
                return;
            } else if ((processedObject != null)
                    && (processedObject.getClass() == Sourse.class) && flag) {
                Integer[] integers = {x, y};
                list.add(integers);
                return;
            }
        }
    }
}
