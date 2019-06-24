package com.pumple.themirrorsgame.GraphicEngine.MenuSettings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.themirrorsgame.R;

/**
 * Created by Pumple on 22.05.2018.
 */

public class DrawLevels {
    private Context context;

    public DrawLevels(Context context) {
        this.context = context;
    }

    public Buttons[] getButtons() {
        return buttons;
    }

    public Buttons getBack() {
        return back;
    }

    private Buttons[] buttons; // Кнопки выбора уровней
    private Buttons back; // Кнопка возврата в главное меню
    /** Отрисовка меню выбора уровней**/
    public Bitmap drawLevels(Canvas c){
        final int UP_SHIFT = 100;
        Bitmap bmp = Bitmap.createBitmap(c.getWidth(), c.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        buttons = new Buttons[12];
        back = new Buttons(new int[]{((canvas.getWidth() - 60) * 5 / 12) + 10, UP_SHIFT + 2 * (canvas.getWidth() - 60) / 6, ((canvas.getWidth() - 60) * 7 / 12), UP_SHIFT + 3 * (canvas.getWidth() - 60) / 6 - 10},
                "back", BitmapFactory.decodeResource(context.getResources(), R.drawable.back));
        for (int i = 0; i < 12; i++) {
            int a;
            int b;
            switch (i){
                case 0:
                    a = R.drawable.level1;
                    b = R.raw.level1;
                    break;
                case 1:
                    a = R.drawable.level2;
                    b = R.raw.level2;
                    break;
                case 2:
                    a = R.drawable.level3;
                    b = R.raw.level3;
                    break;
                case 3:
                    a = R.drawable.level4;
                    b = R.raw.level4;
                    break;
                case 4:
                    a = R.drawable.level5;
                    b = R.raw.level5;
                    break;
                case 5:
                    a = R.drawable.level6;
                    b = R.raw.level6;
                    break;
                case 6:
                    a = R.drawable.level7;
                    b = R.raw.level7;
                    break;
                case 7:
                    a = R.drawable.level8;
                    b = R.raw.level8;
                    break;
                case 8:
                    a = R.drawable.level9;
                    b = R.raw.level9;
                    break;
                case 9:
                    a = R.drawable.level10;
                    b = R.raw.level10;
                    break;
                case 10:
                    a = R.drawable.level11;
                    b = R.raw.level11;
                    break;
                case 11:
                    a = R.drawable.level12;
                    b = R.raw.level12;
                    break;
                default:
                    a = 0;
                    b = 0;
            }
            if (i < 6) {
                buttons[i] = new Buttons(new int[]{((canvas.getWidth() - 60) * i / 6) + 10, UP_SHIFT, ((canvas.getWidth() - 60) * (i + 1)) / 6, UP_SHIFT + (canvas.getWidth() - 60) / 6 - 10}, "" + (i + 1),
                        BitmapFactory.decodeResource(context.getResources(), a));
            } else {
                buttons[i] = new Buttons(new int[]{((canvas.getWidth() - 60) * (i - 6) / 6) + 10, UP_SHIFT + (canvas.getWidth() - 60) / 6, ((canvas.getWidth() - 60) * (i - 6 + 1)) / 6, UP_SHIFT + 2 * (canvas.getWidth() - 60) / 6 - 10}, "" + (i + 1),
                        BitmapFactory.decodeResource(context.getResources(), a));
            }
            buttons[i].setSetLevel(b);
        }

        canvas.rotate(180, canvas.getWidth() / 2, canvas.getHeight() / 2);
        canvas.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.title2), canvas.getWidth()*2 / 3, canvas.getHeight() / 4, false),
                canvas.getWidth() / 6, 20, new Paint());
        canvas.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.field), canvas.getWidth(), canvas.getHeight(), false), 0, 0, new Paint());
        for (Buttons b:buttons) {
            canvas.drawBitmap(Bitmap.createScaledBitmap(b.getBitmap(), canvas.getWidth() / 5 - 10, canvas.getHeight() / 3, false), b.getCoordinates()[0], b.getCoordinates()[1], new Paint());
        }
        canvas.drawBitmap(Bitmap.createScaledBitmap(back.getBitmap(), canvas.getWidth() / 5, canvas.getHeight() / 3, false), back.getCoordinates()[0], back.getCoordinates()[1], new Paint());
        return bmp;
    }
}
