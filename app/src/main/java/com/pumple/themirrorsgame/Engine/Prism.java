package com.pumple.themirrorsgame.Engine;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.themirrorsgame.R;

/**
 * Created by Pumpapple on 22.01.2019.
 */

class Prism extends Objects {

    private final double COEFFICIENT_OF_DEFLECTION = 1.52;

    private final double LIMIT_ANGLE = Math.asin(1 / COEFFICIENT_OF_DEFLECTION);

    private Engine engine;

    private int[][] coordinates; /* Координаты вершин трапеции (призмы)*/

    /** Задание угловых и свободных коэфициентов прямых, содержащих стороны призмы**/
    private double kRightSide;

    private double kLeftSide;

    private double bRightSide;

    private double bLeftSide;

    /*private int bUp;

    private int bDown;*/

    private int[][] setCoordinates(){
        int[][] coordinates = new int[4][2];
        coordinates[0][0] = getX() * engine.getWidth() / Engine.OBJECTS_WIDTH;
        coordinates[0][1] = (getY() + 1) * engine.getHeight() / Engine.OBJECTS_HEIGHT - 1;
        coordinates[1][0] = getX() * engine.getWidth() / Engine.OBJECTS_WIDTH +
                 (engine.getHeight() / Engine.OBJECTS_HEIGHT) / 3; /*На самом деле нужно было умножить на 2/3 и  косинус 60 градусов */
        coordinates[1][1] = (int) ((getY() + 1.0 / 3.0) * engine.getHeight() / Engine.OBJECTS_HEIGHT);
        coordinates[2][0] = ((getX() + 1) * engine.getWidth() / Engine.OBJECTS_WIDTH - 1) -
                (engine.getHeight() / Engine.OBJECTS_HEIGHT) / 3; /*На самом деле нужно было умножить на 2/3 и косинус 60 градусов*/
        coordinates[2][1] = (int) ((getY() + 1.0 / 3.0) * engine.getHeight() / Engine.OBJECTS_HEIGHT);
        coordinates[3][0] = (getX() + 1) * engine.getWidth() / Engine.OBJECTS_WIDTH - 1;
        coordinates[3][1] = (getY() + 1) * engine.getHeight() / Engine.OBJECTS_HEIGHT - 1;
        return coordinates;
    }

    Prism(int x, int y, Context context, Engine engine) {
        super(x, y, context);
        this.engine = engine;
        coordinates = setCoordinates();
        double[] leftKAndB = MyMath.solveASystemOfEquations(coordinates[0][0], 1, coordinates[0][1],
                coordinates[1][0],1,coordinates[1][1]);
        kLeftSide = leftKAndB[1];
        bLeftSide = leftKAndB[2];
        double[] rightKAndB = MyMath.solveASystemOfEquations(coordinates[2][0], 1, coordinates[2][1],
                coordinates[3][0],1,coordinates[3][1]);
        kRightSide = rightKAndB[1];
        bRightSide = rightKAndB[2];
        /*bUp = coordinates[1][0];
        bDown = coordinates[0][0];*/
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.prism);
    }

    @Override
    public boolean isShining() {
        return true;
    }

    @Override
    public boolean isHard() {
        return true;
    }

    @Override
    public int[] isCrossed(double k, double z, double x, double y) {
            double[][] distanceOfCrossDots = new double[4][3];
            if (MyMath.isCrossedLines(k, z, coordinates[0][0], coordinates[0][1],
                    coordinates[1][0], coordinates[1][1])[0] == 1) {
                distanceOfCrossDots[0][0] = MyMath.isCrossedLines(k, z, coordinates[0][0], coordinates[0][1],
                        coordinates[1][0], coordinates[1][1])[1];
                distanceOfCrossDots[0][1] = MyMath.isCrossedLines(k, z, coordinates[0][0], coordinates[0][1],
                        coordinates[1][0], coordinates[1][1])[2];
                distanceOfCrossDots[0][2] = MyMath.countDistance(distanceOfCrossDots[0][0],
                        distanceOfCrossDots[0][1], x, y);
            } else { /* Задание заведомо большого значения, чтобы при нахождении минимального расстояния, данная сторона не стала ответом */
                distanceOfCrossDots[0][2] = 100000;
            }
            if (MyMath.isCrossedLines(k, z, coordinates[1][0], coordinates[1][1],
                    coordinates[2][0], coordinates[2][1])[0] == 1) {
                distanceOfCrossDots[1][0] = MyMath.isCrossedLines(k, z, coordinates[1][0], coordinates[1][1],
                        coordinates[2][0], coordinates[2][1])[1];
                distanceOfCrossDots[1][1] = coordinates[1][1];
                distanceOfCrossDots[1][2] = MyMath.countDistance(distanceOfCrossDots[1][0],
                        distanceOfCrossDots[1][1], x, y);
            } else { /* Задание заведомо большого значения, чтобы при нахождении минимального расстояния, данная сторона не стала ответом */
                distanceOfCrossDots[1][2] = 100000;
            }
            if (MyMath.isCrossedLines(k, z, coordinates[2][0], coordinates[2][1],
                    coordinates[3][0], coordinates[3][1])[0] == 1) {
                distanceOfCrossDots[2][0] = MyMath.isCrossedLines(k, z, coordinates[2][0], coordinates[2][1],
                        coordinates[3][0], coordinates[3][1])[1];
                distanceOfCrossDots[2][1] = MyMath.isCrossedLines(k, z, coordinates[2][0], coordinates[2][1],
                        coordinates[3][0], coordinates[3][1])[2];
                distanceOfCrossDots[2][2] = MyMath.countDistance(distanceOfCrossDots[2][0],
                        distanceOfCrossDots[2][1], x, y);
            } else { /* Задание заведомо большого значения, чтобы при нахождении минимального расстояния, данная сторона не стала ответом */
                distanceOfCrossDots[2][2] = 100000;
            }
            if (MyMath.isCrossedLines(k, z, coordinates[0][0], coordinates[0][1],
                    coordinates[3][0], coordinates[3][1])[0] == 1) {
                distanceOfCrossDots[3][0] = MyMath.isCrossedLines(k, z, coordinates[0][0], coordinates[0][1],
                        coordinates[3][0], coordinates[3][1])[1];
                distanceOfCrossDots[3][1] = coordinates[0][1];
                distanceOfCrossDots[3][2] = MyMath.countDistance(distanceOfCrossDots[3][0],
                        distanceOfCrossDots[3][1], x, y);
            } else { /* Задание заведомо большого значения, чтобы при нахождении минимального расстояния, данная сторона не стала ответом */
                distanceOfCrossDots[3][2] = 100000;
            }
            /* Сортируем массив по значению расстояния */
            try {
                for (int i = 0; i < distanceOfCrossDots.length; i++) {
                    for (int j = distanceOfCrossDots.length - 1; j > 0; j--) {
                        if (distanceOfCrossDots[j][2] < distanceOfCrossDots[j - 1][2]) {
                            double[] buffer = distanceOfCrossDots[j - 1];
                            distanceOfCrossDots[j - 1] = distanceOfCrossDots[j];
                            distanceOfCrossDots[j] = buffer;
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("Exception", e.toString());
            }
            if (distanceOfCrossDots[0][2] == 100000) {
                return new int[]{-1};
            } else {
                return new int[]{1, (int) distanceOfCrossDots[0][0], (int) distanceOfCrossDots[0][1]};
            }
    }

    @Override
    public Integer[][] performAction(double k, double b0, int x, int y, int count, Laser laser, Engine engine){
        Object[] objects = refract(x, y, k, b0, true);
        laser.setK((Double)objects[1]);
        laser.setZ((Double)objects[2]);
        laser.setCount((Integer)objects[0]);
        Integer[][] result = new Integer[objects.length-3][2];
        for (int i = 3; i < objects.length; i++) {
            result[i - 3] = (Integer[]) objects[i];
        }
        return result;
    }

    /**Вроде сделано - нет**/
    Object[] refract(int x, int y, double k, double z, boolean isFirstly) {
        boolean up = true;
        boolean down = true;
        boolean right = true;
        boolean left = true;
        double k2, z2;
        if (isFirstly) {
            Object[] buffer = refractionLawIn(x, y, k);
            k2 = (Double) buffer[0];
            z2 = (Double) buffer[1];
            switch ((String) buffer[2]) {
                case "up":
                    up = false;
                    break;
                case "down":
                    down = false;
                    break;
                case "right":
                    right = false;
                    break;
                case "left":
                    left = false;
                    break;
            }
        } else {
            k2 = k;
            z2 = z;
            if (y == coordinates[1][1]) {
                up = false;
            } else if (y == coordinates[0][1]) {
                down = false;
            } else if ((x >= coordinates[0][0]) && (x <= coordinates[1][0])) {
                left = false;
            } else{
                right = false;
            }
        }
        double xCross = -1, yCross = -1;
        try{
        if (up && (MyMath.isCrossedLines(k2, z2, coordinates[1][0], coordinates[1][1],
                coordinates[2][0], coordinates[2][1])[0] == 1)) {
            xCross = MyMath.isCrossedLines(k2, z2, coordinates[1][0], coordinates[1][1],
                    coordinates[2][0], coordinates[2][1])[1];
            yCross = coordinates[2][1];
            down = false;
            right = false;
            left = false;
        } else if (down && (MyMath.isCrossedLines(k2, z2, coordinates[0][0], coordinates[0][1],
                coordinates[3][0], coordinates[3][1])[0] == 1)) {
            xCross = MyMath.isCrossedLines(k2, z2, coordinates[0][0], coordinates[0][1],
                    coordinates[3][0], coordinates[3][1])[1];
            yCross = coordinates[0][1];
            up = false;
            right = false;
            left = false;
        } else if (left && (MyMath.isCrossedLines(k2, z2, coordinates[0][0], coordinates[0][1],
                coordinates[1][0], coordinates[1][1])[0] == 1)) {
            xCross = MyMath.isCrossedLines(k2, z2, coordinates[0][0], coordinates[0][1],
                    coordinates[1][0], coordinates[1][1])[1];
            yCross = MyMath.isCrossedLines(k2, z2, coordinates[0][0], coordinates[0][1],
                    coordinates[1][0], coordinates[1][1])[2];
            up = false;
            down = false;
            right = false;
        } else if (right && (MyMath.isCrossedLines(k2, z2, coordinates[2][0], coordinates[2][1],
                    coordinates[3][0], coordinates[3][1])[0] == 1)) {
            xCross = MyMath.isCrossedLines(k2, z2, coordinates[2][0], coordinates[2][1],
                    coordinates[3][0], coordinates[3][1])[1];
            yCross = MyMath.isCrossedLines(k2, z2, coordinates[2][0], coordinates[2][1],
                    coordinates[3][0], coordinates[3][1])[2];
            up = false;
            down = false;
            left = false;
            } else {
            return new Object[]{0, k, z, new Integer[]{x, y}};
            }

        } catch (Exception e){
            Log.e("Exception", e.toString());
        }
        Log.i("Prism","Out is coming");
        return refractionLawOut(up, down, right, left, xCross, yCross, k2, z2, x, y);
    }
    private Object[] refractionLawIn(int x, int y, double k){
        Log.i("Prism Y", "" + y);
        Log.i("Prism Coord", "" + coordinates[0][1]);
        if (k == -32000) {
            if (MyMath.isDotOnSegment(x, y,
                    coordinates[1][0], coordinates[1][1], coordinates[2][0], coordinates[2][1])) {
                return new Object[]{-32000, x, "up"};
            } else if (MyMath.isDotOnSegment(x, y,
                    coordinates[0][0], coordinates[0][1], coordinates[3][0], coordinates[3][1])) {
                return new Object[]{-32000, x, "down"};
            } else if (MyMath.isDotOnSegment(x, y,
                    coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1])) {
                double k2 = Math.tan(34.733 * Math.PI / 180 + Math.PI / 6);
                double z2 = y - k2 * x;
                return new Object[]{k2, z2, "left"};
            } else {
                double k2 = Math.tan(-34.733 * Math.PI / 180 + 5 * Math.PI / 6);
                double z2 = y - k2 * x;
                return new Object[]{k2, z2, "right"};
            }
        } else if ((k == 1 / Math.sqrt(3)) && MyMath.isDotOnSegment(x, y,
                coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1])) {
            return new Object[]{k, y - k * x, "left"};
        } else if ((k == -1 / Math.sqrt(3)) && MyMath.isDotOnSegment(x, y,
                coordinates[2][0], coordinates[2][1], coordinates[3][0], coordinates[3][1])) {
            return new Object[]{k, y - k * x, "right"};
        }
        double k2, z2;
        String s;
        if ((y == coordinates[1][1]) || (y == coordinates[0][1])){
            k2 = Math.tan(Math.acos(Math.cos(MyMath.atan(k)) / COEFFICIENT_OF_DEFLECTION));
            z2 = y - k2 * x;
            s = y == coordinates[1][1] ? "up" : "down";
            Log.i("Prism", s);
        } else if ((coordinates[0][0] < x) && (coordinates[0][1] > y) &&
                (coordinates[1][0] > x) && (coordinates[1][1] < y)) {
            double angleOfRefractedLaser =
                    Math.asin(Math.sin(MyMath.atan(k) - Math.PI / 6) / COEFFICIENT_OF_DEFLECTION);
            if (MyMath.atan(k) < Math.PI * 2 / 3) {
                k2 = Math.tan(Math.PI / 6 + angleOfRefractedLaser);
            } else if (MyMath.atan(k) < 160.535 * Math.PI / 180) {
                k2 = Math.tan(Math.PI * 7 / 6 - angleOfRefractedLaser);
            } else {
                k2 = Math.tan(Math.PI / 6 - angleOfRefractedLaser);
            }
            s = "left";
            Log.i("Prism", s);
            z2 = y - k2 * x;
        } else {
            double angleOfRefractedLaser =
                    Math.asin((Math.sin(MyMath.atan(k) + Math.PI / 6) / COEFFICIENT_OF_DEFLECTION));
            /*if ((MyMath.atan(k) < Math.PI / 3) && (MyMath.atan(k) > 19.464 * Math.PI / 180)) {
                k2 = Math.tan(Math.PI / 6 + angleOfRefractedLaser);
            } else if (MyMath.atan(k) < Math.PI / 3) {
                k2 = Math.tan(5 * Math.PI / 6 + angleOfRefractedLaser);
            } else {
                k2 = Math.tan(5 * Math.PI / 6 - angleOfRefractedLaser);
            }*/
            if ((MyMath.atan(k) > Math.PI / 3) && (MyMath.atan(k) < 5 * Math.PI / 6)) {
                k2 = Math.tan(5 * Math.PI / 6 - Math.asin(Math.sin(5 * Math.PI / 6 - MyMath.atan(k)) / COEFFICIENT_OF_DEFLECTION));
            } else if (MyMath.atan(k) < Math.PI / 3) {
                k2 = Math.tan(Math.asin(Math.sin(MyMath.atan(k) + Math.PI / 6) / COEFFICIENT_OF_DEFLECTION) - Math.PI / 6);
            } else {
                k2 = Math.tan(5 * Math.PI / 6 + Math.asin(Math.sin(MyMath.atan(k) - 5 * Math.PI / 6) / COEFFICIENT_OF_DEFLECTION));
            }
            z2 = y - k2 * x;
            s = "right";
            Log.i("Prism", s);
        }
        return new Object[]{k2, z2, s};
    }

    private Object[] refractionLawOut(boolean up, boolean down, boolean right, boolean left,
                                      double xCross, double yCross, double k2, double z2, double x, double y) {
        /**Перепроверить**/
        if (k2 == -32000) {
            if (MyMath.isDotOnSegment(xCross, yCross,
                    coordinates[1][0], coordinates[1][1], coordinates[2][0], coordinates[2][1])) {
                return new Object[]{-1, -32000, xCross, new Integer[]{(int) xCross, (int) yCross}};
            } else if (MyMath.isDotOnSegment(xCross, yCross,
                    coordinates[0][0], coordinates[0][1], coordinates[3][0], coordinates[3][1])) {
                return new Object[]{1, -32000, xCross, new Integer[]{(int) xCross, (int) yCross}};
            } else if (MyMath.isDotOnSegment(xCross, yCross,
                    coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1])) {
                double[] buffer = reflect("left", xCross, yCross, k2, z2);
                Object[] refractedLaser = refract((int) xCross, (int) yCross, buffer[0], buffer[1], false);
                Object[] result = new Object[refractedLaser.length + 1];
                result[0] = refractedLaser[0];
                result[1] = refractedLaser[1];
                result[2] = new Integer[]{(int) xCross, (int) yCross};
                System.arraycopy(refractedLaser, 2, result, 3, refractedLaser.length - 2);
                return result;
            } else {
                double[] buffer = reflect("right", xCross, yCross, k2, z2);
                Object[] refractedLaser = refract((int) xCross, (int) yCross, buffer[0], buffer[1], false);
                Object[] result = new Object[refractedLaser.length + 1];
                result[0] = refractedLaser[0];
                result[1] = refractedLaser[1];
                result[2] = new Integer[]{(int) xCross, (int) yCross};
                System.arraycopy(refractedLaser, 2, result, 3, refractedLaser.length - 2);
                return result;
            }
        } else if ((k2 == 1 / Math.sqrt(3)) && MyMath.isDotOnSegment(xCross, yCross,
                coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1])) {
            return new Object[]{-1, k2, yCross - k2 * xCross, new Integer[]{(int) xCross, (int) yCross}};
        } else if ((k2 == -1 / Math.sqrt(3)) && MyMath.isDotOnSegment(xCross, yCross,
                coordinates[2][0], coordinates[2][1], coordinates[3][0], coordinates[3][1])) {
            return new Object[]{1, k2, yCross - k2 * xCross, new Integer[]{(int) xCross, (int) yCross}};
        }
        if (xCross == -1) {
            Log.i("Prism" + this.toString(), "Something went wrong" + k2 + " " + xCross + " " + yCross);
            return new Integer[]{(int) xCross, (int) yCross};
        } else {
            if (up) {
                Log.i("Prism", "Up");
                if (Math.abs(MyMath.atan(k2) - Math.PI / 2) < LIMIT_ANGLE) {/**Это, вроде, готово**/
                    double resultK = Math.tan(Math.acos(COEFFICIENT_OF_DEFLECTION * Math.cos(MyMath.atan(k2))));
                    double resultZ = yCross - xCross * resultK;
                    return new Object[]{(int) (-1 * resultK / Math.abs(resultK)), resultK, resultZ, new Integer[]{(int) xCross, (int) yCross}};
                } else {
                    double[] buffer = reflect("up", xCross, yCross, k2, z2);
                    Object[] refractedLaser = refract((int) xCross, (int) yCross, buffer[0], buffer[1], false);
                    Object[] result = new Object[refractedLaser.length + 1];
                    result[0] = refractedLaser[0];
                    result[1] = refractedLaser[1];
                    result[2] = refractedLaser[2];
                    result[3] = new Integer[]{(int) xCross, (int) yCross};
                    System.arraycopy(refractedLaser, 3, result, 4, refractedLaser.length - 3);
                    return result;
                }
            } else if (down) {
                Log.i("Prism", "Down");
                if (Math.abs(MyMath.atan(k2) - Math.PI / 2) < LIMIT_ANGLE) {/**Это, вроде, готово**/
                    double resultK = Math.tan(Math.acos(COEFFICIENT_OF_DEFLECTION * Math.cos(MyMath.atan(k2))));
                    double resultZ = yCross - xCross * resultK;
                    return new Object[]{(int) (resultK / Math.abs(resultK)), resultK, resultZ, new Integer[]{(int) xCross, (int) yCross}};
                } else {
                    double[] buffer = reflect("down", xCross, yCross, k2, z2);
                    Object[] refractedLaser = refract((int) xCross, (int) yCross, buffer[0], buffer[1], false);
                    Object[] result = new Object[refractedLaser.length + 1];
                    result[0] = refractedLaser[0];
                    result[1] = refractedLaser[1];
                    result[2] = refractedLaser[2];
                    result[3] = new Integer[]{(int) xCross, (int) yCross};
                    System.arraycopy(refractedLaser, 3, result, 4, refractedLaser.length - 3);
                    return result;
                }
            } else if (right) {
                Log.i("Prism", "Right");
                if (!((MyMath.atan(k2) >= (LIMIT_ANGLE - Math.PI / 6)) &&
                        ((MyMath.atan(k2) <= (5 * Math.PI / 6 - LIMIT_ANGLE))))) {
                    if ((MyMath.atan(k2) > Math.PI / 3) && (MyMath.atan(k2) < 5 * Math.PI / 6)) {
                        double resultK = Math.tan(5 * Math.PI / 6 -
                                Math.asin(COEFFICIENT_OF_DEFLECTION * Math.sin(-MyMath.atan(k2) + 5 * Math.PI / 6)));
                        double resultZ = yCross - resultK * xCross;
                        return new Object[]{(int) (-1 * resultK / Math.abs(resultK)), resultK, resultZ, new Integer[]{(int) xCross, (int) yCross}};
                    } else if (MyMath.atan(k2) > 5 * Math.PI / 6) {
                        double resultK = Math.tan(5 * Math.PI / 6 +
                                Math.asin(COEFFICIENT_OF_DEFLECTION * Math.sin(MyMath.atan(k2) - 5 * Math.PI / 6)));
                        double resultZ = yCross - resultK * xCross;
                        return new Object[]{1, resultK, resultZ, new Integer[]{(int) xCross, (int) yCross}};
                    } else {
                        double resultK = Math.tan(-1 * Math.PI / 6 +
                                Math.asin(COEFFICIENT_OF_DEFLECTION * Math.sin(MyMath.atan(k2) + Math.PI / 6)));
                        double resultZ = yCross - resultK * xCross;
                        return new Object[]{1, resultK, resultZ, new Integer[]{(int) xCross, (int) yCross}};
                    }
                } else {
                    double[] buffer = reflect("right", xCross, yCross, k2, z2);
                    Object[] refractedLaser = refract((int) xCross, (int) yCross, buffer[0], buffer[1], false);
                    Object[] result = new Object[refractedLaser.length + 1];
                    result[0] = refractedLaser[0];
                    result[1] = refractedLaser[1];
                    result[2] = refractedLaser[2];
                    result[3] = new Integer[]{(int) xCross, (int) yCross};
                    System.arraycopy(refractedLaser, 3, result, 4, refractedLaser.length - 3);
                    return result;
                }
            } else if (left){
                Log.i("Prism", "Left");
                if (((MyMath.atan(k2) > Math.PI * 2 / 3) && (MyMath.atan(k2) > -LIMIT_ANGLE + 7 * Math.PI / 6)) ||
                        ((MyMath.atan(k2) > Math.PI / 6) && (MyMath.atan(k2) < Math.PI * 2 / 3) && (MyMath.atan(k2) < LIMIT_ANGLE + Math.PI / 6)) || (MyMath.atan(k2) < Math.PI / 6)) {
                    double resultK;
                    if (MyMath.atan(k2) < Math.PI / 6 - Math.asin(1 / (2 * LIMIT_ANGLE))) {
                        resultK = Math.tan(7 * Math.PI / 6 -
                                Math.asin(COEFFICIENT_OF_DEFLECTION * Math.sin(-MyMath.atan(k2) + Math.PI / 6)));
                    } else if (MyMath.atan(k2) < Math.PI / 6) {
                        resultK = Math.tan(Math.PI / 6 -
                                Math.asin(COEFFICIENT_OF_DEFLECTION * Math.sin(-MyMath.atan(k2) + Math.PI / 6)));
                    } else if (MyMath.atan(k2) < 2 * Math.PI / 3) {
                        resultK = Math.tan(Math.PI / 6 +
                                Math.asin(COEFFICIENT_OF_DEFLECTION * Math.sin(MyMath.atan(k2) - Math.PI / 6)));
                    } else {
                        resultK = Math.tan(7 * Math.PI / 6 -
                                Math.asin(COEFFICIENT_OF_DEFLECTION * Math.sin(-MyMath.atan(k2) + 7 * Math.PI / 6)));
                    }
                    double resultZ = yCross - resultK * xCross;
                    return new Object[]{(int) ((xCross - x) / (Math.abs(xCross - x))), resultK, resultZ, new Integer[]{(int) xCross, (int) yCross}};
                } else {
                    double[] buffer = reflect("left", xCross, yCross, k2, z2);
                    Object[] refractedLaser = refract((int) xCross, (int) yCross, buffer[0], buffer[1], false);
                    Object[] result = new Object[refractedLaser.length + 1];
                    result[0] = refractedLaser[0];
                    result[1] = refractedLaser[1];
                    result[2] = refractedLaser[2];
                    result[3] = new Integer[]{(int) xCross, (int) yCross};
                    System.arraycopy(refractedLaser, 3, result, 4, refractedLaser.length - 3);
                    return result;
                }
            } else {
                if (k2 != -32000) {
                    return new Object[]{(int) Math.abs(xCross - x) / (xCross - x), k2, z2, new Integer[]{(int) xCross, (int) yCross}};
                } else {
                    return new Object[]{(int) Math.abs(yCross - y) / (yCross - y), k2, z2, new Integer[]{(int) xCross, (int) yCross}};
                }
            }
        }
    }
    private double[] reflect(String side, double x, double y, double k, double z) {
        if (k!=-32000) {
            double resultK;
            double resultZ;
            double kOfSide = 0;
            double zOfSide = 0;
            double kOfPerpendicular = 0; /*Строим препендикуляр из точки (x1;y1)) на поверхность призмы*/
            double x1 = x - 10; /*Отходим от точки пересечния на небольшое расстояние*/
            double y1 = x1 * k + z;
            switch (side) {
                case "up":
                    resultK = -k;
                    resultZ = y - x * resultK;
                    return new double[]{resultK, resultZ};
                case "down":
                    resultK = -k;
                    resultZ = y - x * resultK;
                    return new double[]{resultK, resultZ};
                case "right":
                    kOfPerpendicular = -1 / Math.sqrt(3);
                    kOfSide = kRightSide;
                    zOfSide = bRightSide;
                    break;
                case "left":
                    kOfPerpendicular = 1 / Math.sqrt(3);
                    kOfSide = kLeftSide;
                    zOfSide = bLeftSide;
                    break;
            }
            double zOfPerpendicular = y1 - kOfPerpendicular * x1;
            double[] crossPerpendicularAndPrism = MyMath.solveASystemOfEquations(kOfSide, -1, -zOfSide,
                    kOfPerpendicular, -1, -zOfPerpendicular);
            double ghostX, ghostY;
            try {
                ghostX = x1 - 2 * (x1 - crossPerpendicularAndPrism[1]);
            } catch (Exception e) {
                Log.i("Reflect method", e.toString());
                ghostX = -1;
            }
            ghostY = ghostX * kOfPerpendicular + zOfPerpendicular;
            return new double[]{MyMath.solveASystemOfEquations(x, 1, y, ghostX, 1, ghostY)[1],
                    MyMath.solveASystemOfEquations(x, 1, y, ghostX, 1, ghostY)[2]};
        } else if (java.util.Objects.equals(side, "right")) {
            return new double[]{Math.tan(Math.PI / 6), y - x * Math.tan(Math.PI / 6)};
        } else {
            return new double[]{Math.tan(5 * Math.PI / 6), y - x * Math.tan(5 * Math.PI / 6)};
        }
    }
}

