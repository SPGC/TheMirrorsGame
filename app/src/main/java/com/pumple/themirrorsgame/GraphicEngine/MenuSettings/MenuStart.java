package com.pumple.themirrorsgame.GraphicEngine.MenuSettings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.themirrorsgame.R;
/**
 * Created by Pumple on 22.05.2018.
 */

public class MenuStart {
    private Buttons buttons1;
    private Buttons buttons2;

    public Buttons getButtons1() {
        return buttons1;
    }

    public Buttons getButtons2() {
        return buttons2;
    }

    public MenuStart(Canvas canvas, Context context){
        Bitmap b = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.field), canvas.getWidth(), canvas.getHeight(), false);
        canvas.drawBitmap(b, 0, 0, new Paint());
        buttons1 = new Buttons(
            new int[]{canvas.getWidth() / 3, canvas.getHeight() / 3, canvas.getWidth() * 2 / 3, canvas.getHeight() * 2 / 3 - 50}, "choose",
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.chooselevel)); // Кнопка выбора уровней
        buttons2 = new Buttons(
                new int[]{canvas.getWidth() / 3, canvas.getHeight() * 2 / 3, canvas.getWidth() * 2 / 3, canvas.getHeight() - 50}, "me",
                BitmapFactory.decodeResource(context.getResources(), R.drawable.chooselevel)); //Не задействованная кнопка
        canvas.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.title), canvas.getWidth()*2 / 3, canvas.getHeight() / 3, false),
                canvas.getWidth() / 6, 50, new Paint()); //Надпись названия игры
        Paint paint = new Paint();
        {
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }
        canvas.drawText("Version 0.7.1", 1, 900, paint);
        canvas.rotate(180, canvas.getWidth() / 2, canvas.getHeight() / 2);
        canvas.drawBitmap(Bitmap.createScaledBitmap(buttons1.getBitmap(), buttons1.getCoordinates()[0] - buttons1.getCoordinates()[2],
                buttons1.getCoordinates()[1] - buttons1.getCoordinates()[3], false), buttons1.getCoordinates()[0], buttons1.getCoordinates()[1], new Paint()); //Отрисовка на холсте

    }
}
