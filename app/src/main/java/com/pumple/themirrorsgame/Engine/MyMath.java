package com.pumple.themirrorsgame.Engine;

import android.util.Log;

/**
 * Created by Admin on 18.02.2019.
 */

class MyMath {
    static double atan(double tan){
        if (tan < 0) {
            return Math.atan(tan) + Math.PI;
        }
        else {
            return Math.atan(tan);
        }
    }

    static double asin (double sin){
        return 0;
    }

    static double[] isCrossedLines(double k, double z, double xLeft, double yLeft, double xRight, double yRight){
        double[] equationResult = solveASystemOfEquations(xLeft, 1, yLeft, xRight, 1, yRight);
        if (xLeft == xRight) {
            return isCrossedLines(k, z, -32000, xLeft, xLeft, yLeft, xRight, yRight);
        } else {
            return isCrossedLines(k, z, equationResult[1], equationResult[2], xLeft, yLeft, xRight, yRight);
        }
    }

    static double[] isCrossedLines(double k, double z, double k1, double z1, double xLeft, double yLeft, double xRight, double yRight) {
        if(k!=-32000) {
            if (k1 != -32000) {
                final int TOLERANCE = 1;
                double[] equationResult = solveASystemOfEquations(k, -1, -z, k1, -1, -z1);
                if (equationResult[0] == -1) {
                    return new double[]{-1};
                }
                double xCross = equationResult[1];
                double yCross = equationResult[2];
                double yMax = yLeft >= yRight ? yLeft : yRight;
                double yMin = yLeft < yRight ? yLeft : yRight;
                if (((int) xCross >= (int) xLeft - TOLERANCE) && ((int) xCross <= (int) xRight + TOLERANCE) && ((int) yCross >= (int) yMin - TOLERANCE)
                        && ((int) yCross <= (int) yMax + TOLERANCE)) {
                    return new double[]{1, xCross, yCross};
                } else {
                    return new double[]{-1};
                }
            } else {
                double yMax = yLeft >= yRight ? yLeft : yRight;
                double yMin = yLeft < yRight ? yLeft : yRight;
                if ((xLeft * k + z >= yMin) && (xLeft * k + z <= yMax)) {
                    return new double[]{1, xLeft, xLeft * k + z};
                } else {
                    return new double[]{-1};
                }
            }
        } else {
            if (k1 != -32000){
                final int TOLERANCE = 1;
                if ((z > xLeft - TOLERANCE) && (z < xRight + TOLERANCE)) {
                    return new double[]{1, z, k1 * z + z1};
                } else {
                    return new double[]{-1};
                }
            } else {
                return new double[]{-1};
            }
        }
    }
    static double[] solveASystemOfEquations(double a1, double b1, double c1, double a2, double b2, double c2) {
        Matrix delta = new Matrix(a1, b1, a2, b1);
        Matrix deltaX = new Matrix(c1, b1, c2, b2);
        Matrix deltaY = new Matrix(a1, c1, a2, c2);
        if (delta.count() == 0) {
            return new double[]{-1};
        } else{
            return new double[]{1, deltaX.count() / delta.count(), deltaY.count() / delta.count()};
        }
    }
    static boolean isDotOnSegment(double x, double y, double xLeft, double yLeft, double xRight, double yRight){
        if(1 == solveASystemOfEquations(xLeft, 1, yLeft, xRight, 1, yRight)[0]) {
            double k = solveASystemOfEquations(xLeft, 1, yLeft, xRight, 1, yRight)[1];
            double z = solveASystemOfEquations(xLeft, 1, yLeft, xRight, 1, yRight)[2];
            return (int) (x * k + z) == (int) y && (x >= xLeft) && (x <= yRight);
        } else {
            return (int) x == (int) xLeft && (((y >= yLeft) && (y <= yRight)) ||
                    ((y <= yLeft) && (y >= yRight)));
        }
    }

    static double countDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
