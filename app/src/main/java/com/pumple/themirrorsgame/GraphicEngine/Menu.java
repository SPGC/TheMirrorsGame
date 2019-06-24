package com.pumple.themirrorsgame.GraphicEngine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.pumple.themirrorsgame.GameActivity;
import com.pumple.themirrorsgame.GraphicEngine.MenuSettings.Buttons;
import com.pumple.themirrorsgame.GraphicEngine.MenuSettings.DrawLevels;
import com.pumple.themirrorsgame.GraphicEngine.MenuSettings.MenuStart;
import com.pumple.themirrorsgame.MainActivity;

/**
 * Created by Pumple on 22.05.2018.
 */
/**Меню**/
public class Menu extends View {
    Context context;
    int flag = 0;//Отвечает за состояние меню, 0 - главное меню, 1 - выбор уровней
    public Menu(Context context, MainActivity activity) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    MainActivity activity;
    DrawLevels dl;
    boolean isFirst = true;
    MenuStart menuStart;
    Canvas levelsCan;
    Bitmap levelsBitmap;
    Buttons[] chooseLevel;
    /**Отрисовка меню**/
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //Подготовка к отрисовке выбора уровней
        if (isFirst){
            levelsBitmap = Bitmap.createBitmap(canvas.getWidth(),canvas.getHeight(), Bitmap.Config.ARGB_8888);
            levelsCan = new Canvas(levelsBitmap);
            dl = new DrawLevels(context);
            levelsBitmap = dl.drawLevels(canvas);//Отрисовка кнопок уровней
            chooseLevel = dl.getButtons();//Получение кнопок уровней
            isFirst = false;
        }
        //Отрисовка выбора уровней
        if (flag == 1) {
            canvas.rotate(180, canvas.getWidth() / 2, canvas.getHeight() / 2);
            canvas.drawBitmap(levelsBitmap, 0, 0, new Paint());
            canvas.rotate(-180, canvas.getWidth() / 2, canvas.getHeight() / 2);
        } else {
            // Отрисовка главного меню
            menuStart = new MenuStart(canvas, context);
        }
    }
    /**Обработчик нажатий**/
    @Override
    public boolean onTouchEvent(MotionEvent event){
        try{
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Обработчик нажатий в главном меню
                if (flag == 0) {
                    if ((event.getX() < menuStart.getButtons1().getCoordinates()[2]) && (event.getX() > menuStart.getButtons1().getCoordinates()[0]) &&
                            (event.getY() > menuStart.getButtons1().getCoordinates()[1]) && (event.getY() < menuStart.getButtons1().getCoordinates()[3])) {
                        flag = 1;
                        invalidate();
                    }
                }
                // Обработчик нажатий при выборе уровня
                else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (dl.getBack().isPressed((int) event.getX(), (int) event.getY())) {
                        flag = 0;
                        invalidate();
                    } else {
                        Log.i("Pressed on", "" + event.getX() + " " + event.getY());
                        for (Buttons button : chooseLevel) {
                            Log.i(button.getName(), "" + button.getCoordinates()[0] + " " + button.getCoordinates()[1] + " " + button.getCoordinates()[2] + " " + button.getCoordinates()[3]);
                            if (button.isPressed((int) event.getX(), (int) event.getY())) {
                                Log.d("button_pressed", button.getName());
                                Intent intent = new Intent(context, GameActivity.class);
                                intent.putExtra("level", button.getSetLevel());
                                activity.startActivityForResult(intent, 1);// Запуск игровой активности
                                break;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e){
            Log.i("ERROR",e.toString());//Логирование
        }
        return true;
    }

}
