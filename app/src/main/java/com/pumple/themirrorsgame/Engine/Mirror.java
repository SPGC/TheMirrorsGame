package com.pumple.themirrorsgame.Engine;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;


import com.example.themirrorsgame.R;




/**
 * Created by Pumple on 30.03.2018.
 */

public class Mirror extends Objects {
    private double k, b;
    private Engine engine;
    private int centerOfSectorX, centerOfSectorY, radius;

    @Override
    public boolean isShining() {
        return true;
    }

    @Override
    public boolean isHard() {
        return true;
    }

    Mirror(int x, int y, int rot, Engine engine, Context context) {
        super(x, y, context);
        setRot(rot);
        this.engine = engine;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mirror);
        centerOfSectorX = (int) ((x + 0.5) * engine.getWidth() / Engine.OBJECTS_WIDTH);
        centerOfSectorY = (int) ((y + 0.5) * engine.getHeight() / Engine.OBJECTS_HEIGHT);
        radius = (int) (0.5 * engine.getHeight() / Engine.OBJECTS_HEIGHT);
        b = centerOfSectorY - centerOfSectorX * k;
        radius = engine.getHeight() / Engine.OBJECTS_HEIGHT / 2;
        /*if (k <= -32000) {
            b = centerOfSectorX;
        }*/
    }

    public int countRotation(int x, int y) {
        if (y == centerOfSectorY) {
            if (centerOfSectorX > x) {
                return 270;
            } else {
                return 90;
            }
        } else {
            Matrix matrix_determinant = new Matrix(x, 1, this.centerOfSectorX, 1);
            Matrix matrix_determinantK = new Matrix(y, 1, this.centerOfSectorY, 1);
            //Matrix matrix_determinantB = new Matrix(x, y, this.centerOfSectorX, this.centerOfSectorY);
            double k = matrix_determinantK.count() / matrix_determinant.count();
            if (Math.abs(k) < 0.0027624309392265192) {
                if (centerOfSectorX > x) {
                    return 270;
                } else {
                    return 90;
                }
            }

            //double b = matrix_determinantB.count() / matrix_determinant.count();
            int coef = x > centerOfSectorX ? 1 : -1;
            int rotation = (int) Math.toDegrees(Math.atan(-1 / k)) * coef;
            if (centerOfSectorX < x) {
                if (y < centerOfSectorY) {
                    return rotation;
                } else {
                    return 180 + rotation;
                }
            } else {
                if (y > centerOfSectorY) {
                    return 180 - rotation;
                } else {
                    return -rotation;
                }
            }

            /*Log.i("Mirror_Tang", "" + rotation);
            Log.i("Mirror_Tangens", "" + k);
            return rotation;*/
        }
    }

    public int getRot() {
        return rot;
    }

    @Override
    public int[] isCrossed(double k0, double b0, double x0, double y0) {
        if ((k0 != -32000) && (k != -32000)) {
            double XCross1, XCross2 = 0;
            double YCross1, YCross2 = 0;
            double XCrossMirror = 0;
            double YCrossMirror = 0;
            boolean flagIsCrossed;
            Matrix delta = new Matrix(k0, -1, k, -1);
            Matrix deltaX = new Matrix(-b0, -1, -b, -1);
            Matrix deltaY = new Matrix(k0, -b0, k, -b);
            if (delta.count() == 0) {
                flagIsCrossed = false;
            } else if ((centerOfSectorX - 0.5 * engine.getWidth() / Engine.OBJECTS_WIDTH <= deltaX.count() / delta.count()) &&
                    (centerOfSectorX + 0.5 * engine.getWidth() / Engine.OBJECTS_WIDTH >= deltaX.count() / delta.count()) &&
                    (centerOfSectorY - 0.5 * engine.getHeight() / Engine.OBJECTS_HEIGHT <= deltaY.count() / delta.count()) &&
                    (centerOfSectorY + 0.5 * engine.getHeight() / Engine.OBJECTS_HEIGHT >= deltaY.count() / delta.count())) {
                flagIsCrossed = true;
                XCrossMirror = deltaX.count() / delta.count();
                YCrossMirror = deltaY.count() / delta.count();
            } else {
                flagIsCrossed = false;
            }
            boolean isOneRoot;
            double discriminant = 4 * Math.pow((centerOfSectorX - k0 * b0 + k0 * centerOfSectorY), 2) - 4 * ((k0 * k0 + 1) * (centerOfSectorX * centerOfSectorX + Math.pow((b0 - centerOfSectorY), 2) - radius * radius));
            if (discriminant < 0) {
                return new int[]{-1};
            } else if (discriminant == 0) {
                isOneRoot = true;
                XCross1 = (centerOfSectorX - k0 * b0 + k0 * centerOfSectorY) / (k0 * k0 + 1); //2 сократилась
                YCross1 = XCross1 * k0 + b0;
            } else {
                isOneRoot = false;
                XCross1 = (2 * (centerOfSectorX - k0 * b0 + k0 * centerOfSectorY) + Math.sqrt(discriminant)) / (2 * (k0 * k0 + 1));
                YCross1 = XCross1 * k0 + b0;
                XCross2 = (2 * (centerOfSectorX - k0 * b0 + k0 * centerOfSectorY) - Math.sqrt(discriminant)) / (2 * (k0 * k0 + 1));
                YCross2 = XCross2 * k0 + b0;
            }
            if (isOneRoot) {
                if ((rot < 90) || (rot > 270)) {
                    if (XCross1 * k + b <= YCross1) {
                        Log.i("Bumped", "Onroot" + k + " " + b + " " + XCross1 + " " + YCross1);
                        return new int[]{1, (int) XCross1, (int) YCross1};
                    } else {
                        if (flagIsCrossed) {
                            return new int[]{0, (int) XCrossMirror, (int) YCrossMirror};
                        } else {
                            return new int[]{-1};
                        }
                    }
                } else {
                    if (XCross1 * k + b >= YCross1) {
                        return new int[]{1, (int) XCross1, (int) YCross1};
                    } else {
                        if (flagIsCrossed) {
                            return new int[]{0, (int) XCrossMirror, (int) YCrossMirror};
                        } else {
                            return new int[]{-1};
                        }
                    }
                }
            } else {
                double nearestX, nearestY;
                if (Math.pow(XCross1 - x0, 2) + Math.pow(YCross1 - y0, 2) > (Math.pow(XCross2 - x0, 2) + Math.pow(YCross2 - y0, 2))) {
                    nearestX = XCross2;
                    nearestY = YCross2;
                } else {
                    nearestX = XCross1;
                    nearestY = YCross1;
                }
                if ((rot < 90) || (rot > 270)) {
                    if (nearestX * k + b <= nearestY) {
                        Log.i("Bumped", "" + k + " " + b + " " + nearestX + " " + nearestY);
                        return new int[]{1, (int) nearestX, (int) nearestY};
                    } else {
                        if (flagIsCrossed) {
                            return new int[]{0, (int) XCrossMirror, (int) YCrossMirror};
                        } else {
                            return new int[]{-1};
                        }
                    }
                } else {
                    if (nearestX * k + b >= nearestY) {
                        return new int[]{1, (int) nearestX, (int) nearestY};
                    } else {
                        if (flagIsCrossed) {
                            return new int[]{0, (int) XCrossMirror, (int) YCrossMirror};
                        } else
                            return new int[]{-1};
                    }
                }
            }
        } else if (k == -32000) {
            if (k0 != -32000) {
                double xCrossMirror = centerOfSectorX, yCrossMirror = xCrossMirror * k0 + b0;
                boolean flagIsCrossed = (yCrossMirror > centerOfSectorY - 0.5 * engine.getHeight() / Engine.OBJECTS_HEIGHT) && (yCrossMirror < centerOfSectorY + 0.5 * engine.getHeight() / Engine.OBJECTS_HEIGHT);
                double XCross1, YCross1;
                double XCross2 = 0, YCross2 = 0;
                boolean isOneRoot;
                double discriminant = 4 * Math.pow((centerOfSectorX - k0 * b0 + k0 * centerOfSectorY), 2) - 4 * ((k0 * k0 + 1) * (centerOfSectorX * centerOfSectorX + Math.pow((b0 - centerOfSectorY), 2) - radius * radius));
                if (discriminant < 0) {
                    return new int[]{-1};
                } else if (discriminant == 0) {
                    isOneRoot = true;
                    XCross1 = (centerOfSectorX - k0 * b0 + k0 * centerOfSectorY) / (k0 * k0 + 1); //2 сократилась
                    YCross1 = XCross1 * k0 + b0;
                } else {
                    isOneRoot = false;
                    XCross1 = (2 * (centerOfSectorX - k0 * b0 + k0 * centerOfSectorY) + Math.sqrt(discriminant)) / (2 * (k0 * k0 + 1));
                    YCross1 = XCross1 * k0 + b0;
                    XCross2 = (2 * (centerOfSectorX - k0 * b0 + k0 * centerOfSectorY) - Math.sqrt(discriminant)) / (2 * (k0 * k0 + 1));
                    YCross2 = XCross2 * k0 + b0;
                }
                if (isOneRoot) {
                    if (rot == 90) {
                        if (XCross1 < centerOfSectorX) {
                            return new int[]{1, (int) XCross1, (int) YCross1};
                        } else {
                            if (flagIsCrossed) {
                                return new int[]{0, (int) xCrossMirror, (int) yCrossMirror};
                            } else {
                                return new int[]{-1};
                            }
                        }
                    } else {
                        if (XCross1 > centerOfSectorX) {
                            return new int[]{1, (int) XCross1, (int) YCross1};
                        } else {
                            if (flagIsCrossed) {
                                return new int[]{0, (int) xCrossMirror, (int) yCrossMirror};
                            } else {
                                return new int[]{-1};
                            }
                        }
                    }
                } else {
                    double nearestX, nearestY;
                    if (Math.pow(XCross1 - x0, 2) + Math.pow(YCross1 - y0, 2) > (Math.pow(XCross2 - x0, 2) + Math.pow(YCross2 - y0, 2))) {
                        nearestX = XCross2;
                        nearestY = YCross2;
                    } else {
                        nearestX = XCross1;
                        nearestY = YCross1;
                    }
                    if ((rot == 90)) {
                        if (nearestX < centerOfSectorX) {
                            Log.i("Bumped", "" + k + " " + b + " " + nearestX + " " + nearestY);
                            return new int[]{1, (int) nearestX, (int) nearestY};
                        } else {
                            if (flagIsCrossed) {
                                return new int[]{0, (int) xCrossMirror, (int) yCrossMirror};
                            } else {
                                return new int[]{-1};
                            }
                        }
                    } else {
                        if (nearestX > centerOfSectorX) {
                            return new int[]{1, (int) nearestX, (int) nearestY};
                        } else {
                            if (flagIsCrossed) {
                                return new int[]{0, (int) xCrossMirror, (int) yCrossMirror};
                            } else
                                return new int[]{-1};
                        }
                    }
                }
            } else {
                if (rot == 90) {
                    if (b0 < centerOfSectorX) {
                        return new int[]{1, (int) b0, centerOfSectorY};
                    } else {
                        return new int[]{-1};
                    }
                } else {
                    if (b0 > centerOfSectorX) {
                        return new int[]{1, (int) b0, centerOfSectorY};
                    } else {
                        return new int[]{-1};
                    }

                }
            }
        } else {
            double xCrossMirror = b0, yCrossMirror = xCrossMirror * k + b;
            double xCross1 = b0, yCross1;
            double xCross2 = b0, yCross2 = 0;
            boolean flagIsCrossed = (yCrossMirror > centerOfSectorY - 0.5 * engine.getHeight() / Engine.OBJECTS_HEIGHT) &&
                    (yCrossMirror < centerOfSectorY + 0.5 * engine.getHeight() / Engine.OBJECTS_HEIGHT);
            double discriminant = 4 * radius * radius - 4 * Math.pow(b0 - centerOfSectorX, 2);
            boolean isOneRoot;
            if (discriminant < 0) {
                return new int[]{-1};
            } else if (discriminant == 0) {
                isOneRoot = true;
                yCross1 = centerOfSectorY;
            } else {
                isOneRoot = false;
                yCross1 = (2 * centerOfSectorY - Math.sqrt(discriminant)) / 2;
                yCross2 = (2 * centerOfSectorY + Math.sqrt(discriminant)) / 2;
            }
            if (isOneRoot) {
                if ((rot < 90) || (rot > 270)) {
                    if (xCross1 * k + b <= yCross1) {
                        Log.i("Bumped", "Onroot" + k + " " + b + " " + xCross1 + " " + xCross1);
                        return new int[]{1, (int) xCross1, (int) yCross1};
                    } else {
                        if (flagIsCrossed) {
                            return new int[]{0, (int) xCrossMirror, (int) yCrossMirror};
                        } else {
                            return new int[]{-1};
                        }
                    }
                } else {
                    if (xCross1 * k + b >= yCross1) {
                        return new int[]{1, (int) xCross1, (int) yCross1};
                    } else {
                        if (flagIsCrossed) {
                            return new int[]{0, (int) xCrossMirror, (int) yCrossMirror};
                        } else {
                            return new int[]{-1};
                        }
                    }
                }
            } else {
                double nearestX, nearestY;
                if (Math.pow(xCross1 - x0, 2) + Math.pow(yCross1 - y0, 2) > (Math.pow(xCross2 - x0, 2) + Math.pow(yCross2 - y0, 2))) {
                    nearestX = xCross2;
                    nearestY = yCross2;
                } else {
                    nearestX = xCross1;
                    nearestY = yCross1;
                }
                if ((rot < 90) || (rot > 270)) {
                    if (nearestX * k + b <= nearestY) {
                        Log.i("Bumped", "" + k + " " + b + " " + nearestX + " " + nearestY);
                        return new int[]{1, (int) nearestX, (int) nearestY};
                    } else {
                        if (flagIsCrossed) {
                            return new int[]{0, (int) xCrossMirror, (int) yCrossMirror};
                        } else {
                            return new int[]{-1};
                        }
                    }
                } else {
                    if (nearestX * k + b >= nearestY) {
                        return new int[]{1, (int) nearestX, (int) nearestY};
                    } else {
                        if (flagIsCrossed) {
                            return new int[]{0, (int) xCrossMirror, (int) yCrossMirror};
                        } else {
                            return new int[]{-1};
                        }
                    }
                }
            }
        }
    }

    int[][] getCoordinates() {
        int[][] coordinates = new int[2][2];
        coordinates[0][0] = (int) (centerOfSectorX + Math.cos(Math.PI * rot / 180) * engine.getWidth() / (Engine.OBJECTS_WIDTH * 2));
        coordinates[0][1] = (int) (centerOfSectorY + Math.sin(Math.PI * rot / 180) * engine.getHeight() / (Engine.OBJECTS_HEIGHT * 2));
        coordinates[1][0] = (int) (centerOfSectorX - Math.cos(Math.PI * rot / 180) * engine.getWidth() / (Engine.OBJECTS_WIDTH * 2));
        coordinates[1][1] = (int) (centerOfSectorY - Math.sin(Math.PI * rot / 180) * engine.getHeight() / (Engine.OBJECTS_HEIGHT * 2));
        return coordinates;
    }

    public void setRot(int rot) {
        if ((rot >= 0) && (rot < 360)) {
            this.rot = rot;
            if ((this.rot != 90) && (this.rot != 270)) {
                k = Math.tan(Math.PI * this.rot / 180);
            } else {
                k = -32000;
            }
        } else if (rot >= 360) {
            this.rot = rot % 360;
            if ((this.rot != 90) && (this.rot != 270)) {
                k = Math.tan(Math.PI * this.rot / 180);
            } else {
                k = -32000;
            }
        } else {
            while (rot < 0) {
                rot += 360;
            }
            this.rot = rot;
            if ((this.rot != 90) && (this.rot != 270)) {
                k = Math.tan(Math.PI * this.rot / 180);
            } else {
                k = -32000;
            }
        }
        b = centerOfSectorY - centerOfSectorX * k;
    }

    @Override
    public Integer[][] performAction(double k, double b0, int x, int y, int count, Laser laser, Engine engine){
        Integer[] ints = reflect(k, b0, x, y, count, laser);
        if (laser.getK() != -32000) {
            count = ints[0] > ints[2] ? -1 : 1;
        } else {
            count = ints[1] > ints[3] ? -1 : 1;
        }
        laser.setCount(count);
        return new Integer[][]{new Integer[]{ints[0], ints[1]}, new Integer[]{ints[2], ints[3]}};
    }
    private Integer[] reflect(double k, double b, int x, int y, int count, Laser laser) {
        double XCross, YCross;
        if ((k != -32000) && (this.k != -32000)) {
            Matrix delta1 = new Matrix(this.k, -1, k, -1);
            Matrix deltaX1 = new Matrix(-this.b, -1, -b, -1);
            Matrix deltaY1 = new Matrix(this.k, -this.b, k, -b);
            XCross = deltaX1.count() / delta1.count();
            YCross = deltaY1.count() / delta1.count(); // Координаты точки пересечения лазера и зеркала
        } else if (k == -32000) {
            XCross = b;
            YCross = XCross * this.k + this.b;
        } else {
            XCross = centerOfSectorX;
            YCross = XCross * k + b;
        }
        double bOfPerpendic, kOfPerpendic; // Коэфициенты перепендикуляра к зеркалу, из точки (x1; y1)
        double xImaginary, yImaginary;
        double xCrossPerpedic;
        int finalX, finalY, finalB;
        double finalK;
        double x1, y1; // Отойдем от точки пересечения
        if (k == -32000) {
            y1 = y - count * 10;
            x1 = x;
        } else {
            x1 = x - count * 10;
            y1 = x1 * k + b;
        }
        if ((rot != 180) && (rot != 0)) {
            if ((this.k != -32000)) {
                kOfPerpendic = -1 / this.k;
                bOfPerpendic = y1 - kOfPerpendic * x1;
                xCrossPerpedic = MyMath.solveASystemOfEquations(kOfPerpendic, -1, -bOfPerpendic,
                        this.k, -1, -this.b)[1]; // Ищем абсцису точки пересечения перпендикуляра и зеркала
                xImaginary = xCrossPerpedic + xCrossPerpedic - x1;
                yImaginary = xImaginary * kOfPerpendic + bOfPerpendic;

            } else {
                kOfPerpendic = 0;
                bOfPerpendic = y1;
                xCrossPerpedic = XCross;
                xImaginary = xCrossPerpedic + xCrossPerpedic - x1;
                yImaginary = (xImaginary * kOfPerpendic + bOfPerpendic);
            }
            if ((int) XCross != (int) xImaginary) {
                Matrix delta = new Matrix(xImaginary, 1, XCross, 1);
                Matrix deltaK = new Matrix(yImaginary, 1, YCross, 1);
                Matrix deltaB = new Matrix(xImaginary, yImaginary, XCross, YCross);
                finalK = deltaK.count() / delta.count();
                finalB = (int) (deltaB.count() / delta.count());
                if (count < 0) {
                    if ((this.rot <= 180) && (this.rot >= 0)) {
                        finalX = (int) (XCross + 1 * (Math.abs(XCross - xImaginary) / (XCross - xImaginary)));
                    } else {
                        finalX = (int) (XCross + 1 * (Math.abs(XCross - xImaginary) / (XCross - xImaginary)));
                    }
                } else {
                    if ((this.rot <= 180) && (this.rot >= 0)) {
                        finalX = (int) (XCross + 1 * (Math.abs(XCross - xImaginary) / (XCross - xImaginary)));
                    } else {
                        finalX = (int) (XCross + 1 * (Math.abs(XCross - xImaginary) / (XCross - xImaginary)));
                    }
                }
                finalY = (int) (finalX * finalK + finalB);
            } else {
                finalK = -32000;
                finalB = (int) xImaginary;
                finalX = (int) xImaginary;
                finalY = (int) (YCross - 1 * Math.cos(rot * Math.PI / 180) / Math.abs(Math.cos(rot * Math.PI / 180)));
                Log.i("YCross", "" + YCross);
            }
            laser.setK(finalK);
            laser.setZ(finalB);
            return new Integer[]{(int) XCross, (int) YCross, finalX, finalY};
        } else {
            if (k != -32000) {
                finalK = -k;
                finalB = (int) (YCross - XCross * finalK);
                finalX = (int) XCross + count * 10;
                finalY = (int) (finalX * finalK + finalB);
                laser.setK(finalK);
                laser.setZ(finalB);
                return new Integer[]{(int) XCross, (int) YCross, finalX, finalY};
            } else {
                laser.setK(-32000);
                laser.setZ(XCross);
                return new Integer[]{(int) XCross, (int) YCross, (int) XCross, (int) YCross - 10 * count};
            }
        }
    }
}
