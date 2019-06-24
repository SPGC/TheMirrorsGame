package com.pumple.themirrorsgame.Engine;

/**
 * Created by Илья on 06.04.2018.
 */

class Matrix {
    private double x1,x2,y1,y2;

    Matrix(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    double count(){
        return x1 * y2 - x2 * y1;
    }
}
