package com.pumple.themirrorsgame.GraphicEngine.Level;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import com.pumple.themirrorsgame.Engine.Engine;
import com.pumple.themirrorsgame.Engine.Laser;
import com.pumple.themirrorsgame.Engine.Objects;
import com.example.themirrorsgame.R;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Илья on 12.04.2018.
 */

public class DrawLevel implements Callable<Object[]>{
    private SurfaceHolder surfaceHolder;
    private Context context;
    private volatile Bitmap changeableBitmap, staticBitmap;
    private Engine engine;
    private int chosenLevel;
    public DrawLevel(SurfaceHolder surfaceHolder, Context context, int chosenLevel) {
        this.surfaceHolder = surfaceHolder;
        this.context = context;
        this.chosenLevel = chosenLevel;
    }
    /** Отрисовка лазера**/
    static boolean DrawLaser(ArrayList<Laser> list, Canvas canvas){
        for (Laser laser : list) {
            Paint paint = new Paint();
            {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setColor(laser.getColorOfLaser());
            }
            for (int i = 1; i < laser.getList().size(); i++) {
                Integer[] integers = laser.getList().get(i - 1);
                Integer[] integers1 = laser.getList().get(i);
                canvas.drawLine(integers[0], integers[1], integers1[0], integers1[1], paint);
                if ((integers1.length == 3) && (integers1[2] == 1)) {
                    Log.i("Final", "Laser is in the container");
                    return true;
                }
            }
        }
        return false;
    }

    public Object[] call(){
         //long time = SystemClock.currentThreadTimeMillis(); // Нужно для отладки
         Objects[][] objectses;
         Canvas canvas = surfaceHolder.lockCanvas();
         if (canvas != null) {
             try {
                 /**Первоначальная задание уровня**/

                    engine = new Engine(canvas, context);
                    engine.initLevel(context.getResources().openRawResource(chosenLevel));

                 /**Отрисовка задника**/

                     Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.field);
                     Bitmap bmp = Bitmap.createScaledBitmap(bm, canvas.getWidth(), canvas.getHeight(), false);
                     canvas.drawBitmap(bmp, 0, 0, new Paint());
                     objectses = engine.getObjects();

                 /** Создание изменяемого и статичного изображений, для ускорения будущих вычислений **/
                     DrawChangeable dc = new DrawChangeable(objectses, engine);
                     ExecutorService exe1 = Executors.newSingleThreadExecutor();
                     Future<Bitmap> result1 = exe1.submit(dc);
                     DrawStatic ds = new DrawStatic(objectses, engine, this, context);
                     ExecutorService exe2 = Executors.newSingleThreadExecutor();
                     Future<Bitmap> result2 = exe2.submit(ds);
                 /** Отрисовка изменяемого изображения **/
                 for (int i = 0; i < Engine.OBJECTS_WIDTH; i++) {
                     for (int j = 0; j < Engine.OBJECTS_HEIGHT; j++) {
                         if (objectses[i][j] != null) {
                             Bitmap bitmap = Bitmap.createScaledBitmap(objectses[i][j].getBitmap(),
                                     (canvas.getWidth() / Engine.OBJECTS_WIDTH),
                                     (canvas.getHeight() / Engine.OBJECTS_HEIGHT), false);
                             Bitmap bitmp = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                             Canvas can = new Canvas(bitmp);
                             can.save(Canvas.MATRIX_SAVE_FLAG);
                             can.rotate(objectses[i][j].getRot(), bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                             can.drawBitmap(bitmap,0,0,new Paint());
                             can.restore();
                             canvas.drawBitmap(bitmp,(objectses[i][j].getX() * canvas.getWidth() / Engine.OBJECTS_WIDTH),
                                     (objectses[i][j].getY() * canvas.getHeight() / Engine.OBJECTS_HEIGHT),
                                     new Paint());
                         }
                     }
                 }

                 ArrayList<Laser> list = engine.getLaserList();
                    DrawLaser(list, canvas);
                 while (!result1.isDone()){ // Ждем пока не отрисуется изменяемая часть картинки

                 }
                 changeableBitmap = result1.get();
                 while (!result2.isDone()){ // Ждем пока не отрисуется неизменяемая часть картинки

                 }
                 staticBitmap = result2.get();
             }
             catch (Exception e){
                 Paint paint = new Paint();
                 {
                     paint.setStyle(Paint.Style.FILL_AND_STROKE);
                     paint.setColor(0xFFFF0000);
                 }
                 Paint paintText = new Paint();
                 {
                     paintText.setStyle(Paint.Style.STROKE);
                     paintText.setColor(0xFFFFFFFF);
                 }
                 canvas.drawText(e.toString(),50,50,paintText);
                 Log.i("Error",e.toString());

             }
             finally {
                 surfaceHolder.unlockCanvasAndPost(canvas);
             }
         }
        return new Object[]{changeableBitmap,staticBitmap,engine};
     }

}
