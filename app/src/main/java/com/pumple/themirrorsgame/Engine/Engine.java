package com.pumple.themirrorsgame.Engine;

import android.content.Context;
import android.graphics.Canvas;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import static java.lang.Integer.parseInt;

/**
 * Created by Илья on 30.03.2018.
 */

public class Engine {
    private ArrayList<Integer[]> list;
    private ArrayList<Laser> laserList;
    static final int CANVAS_WIDTH = 1920;
    static final int CANVAS_HEIGHT = 1080;
    public static final int OBJECTS_WIDTH = 16/*20*/;
    public static final int OBJECTS_HEIGHT = 9/*10*/;
    private Objects[][] objects = new Objects[OBJECTS_WIDTH][OBJECTS_HEIGHT];
    private Canvas canvas;
    private int height, width;
    private Context context;
    private ArrayList<int[]> changeable = new ArrayList<>();

    public Objects[][] getObjects() {
        return objects;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    Canvas getCanvas(){
        return canvas;
    }

    public ArrayList<Laser> getLaserList() {
        return laserList;
    }

    void addLaser(Laser laser){
        laserList.add(laser);
    }

    public Engine (Canvas canvas, Context context){
        this.canvas = canvas;
        width = canvas.getWidth();
        height = canvas.getHeight();
        this.context = context;
    }
    public Engine (Canvas canvas, Context context, Objects[][] objectses, ArrayList<Integer[]> list){
        this.canvas = canvas;
        this.context = context;
        this.objects = objectses;
        this.list = list;
        width = canvas.getWidth();
        height = canvas.getHeight();
    }
    public void initLevel (InputStream file) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file));
        String s;
        list = new ArrayList<>(); //Массив для глобальных координат (координат устройства)
        laserList = new ArrayList<>(1);
        StringTokenizer stringTokenizer;
        for (int i = 0; i < OBJECTS_WIDTH; i++) {
            for (int j = 0; j < OBJECTS_HEIGHT; j++) {
                objects[i][j] = null;
            }
        }
            while (true) {
                try {
                    s = reader.readLine();
                    stringTokenizer = new StringTokenizer(s);
                } catch (Exception e) {
                    break;
                }
                    int x = parseInt(stringTokenizer.nextToken());
                    int y = parseInt(stringTokenizer.nextToken());
                    Objects objects1;
                    switch (parseInt(stringTokenizer.nextToken())) {
                        case 1:
                            objects1 = new Mirror(x, y, parseInt(stringTokenizer.nextToken()), this, context);
                            changeable.add(new int[]{x,y});
                            break;
                        case 2:
                            objects1 = new Sourse(x, y, parseInt(stringTokenizer.nextToken()), context);
                            Integer[] a = {(width / OBJECTS_WIDTH) * x + width / (OBJECTS_WIDTH * 2), (height / OBJECTS_HEIGHT) * y + height / (OBJECTS_HEIGHT * 2)};//Задание первой координаты для лазера
                            list.add(a);
                            if (90 == (objects1).getRot()) {//Задание второй координаты для лазера
                                Integer[] b = {(width / OBJECTS_WIDTH) * x + width / (OBJECTS_WIDTH * 2), (height / OBJECTS_HEIGHT) * y + height / (OBJECTS_HEIGHT * 2) + 1};
                                list.add(b);
                            } else {
                                Integer[] b = {(width / OBJECTS_WIDTH) * x + width / (OBJECTS_WIDTH * 2) + (int) Math.cos(Math.PI * objects1.getRot() / 180),
                                        (height / OBJECTS_HEIGHT) * y + height / (OBJECTS_HEIGHT * 2) + (int) Math.sin(Math.PI * objects1.getRot() / 180)};
                                list.add(b);
                            }
                            break;
                        case 3:
                            objects1 = new Walls(x,y,context);
                            break;
                        case 4:
                            objects1 = new Container(x, y, parseInt(stringTokenizer.nextToken()), context, this, parseInt(stringTokenizer.nextToken()));
                            break;
                        case 5:
                            objects1 = new Prism(x,y,context,this);
                            break;
                        case 6:
                            objects1 = new Painter(x, y, context, parseInt(stringTokenizer.nextToken()), parseInt(stringTokenizer.nextToken()), this);
                            break;
                        default:
                            objects1 = null;
                    }
                    objects[x][y] = objects1;
            }
        laser(2);
    }
    synchronized public void laser(int a){
        ArrayList<Laser> buffer = new ArrayList<>(1);
        buffer.add(new Laser(objects, canvas, list.get(0), list.get(1)));
        laserList = buffer;
        int counter = 0;
        while (counter < laserList.size()){
            laserList.get(counter).laser(2, this);
            counter++;
        }
    }
}


