package com.pumple.themirrorsgame.GraphicEngine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

import com.pumple.themirrorsgame.Engine.Engine;
import com.pumple.themirrorsgame.GameActivity;
import com.pumple.themirrorsgame.GraphicEngine.Level.DrawLevel;
import com.pumple.themirrorsgame.GraphicEngine.Level.Level;
import com.pumple.themirrorsgame.GraphicEngine.MenuSettings.Buttons;
import com.example.themirrorsgame.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Илья on 12.04.2018.
 */

public class GraphicEngine extends SurfaceView implements SurfaceHolder.Callback {

    public int x, y, x1, y1;
    boolean flag = false;
    Level level = null;
    int chosenLevel;
    Context context;
    GameActivity activity;


    public GraphicEngine(Context context, int chosenLevel, GameActivity activity) {
        super(context);
        getHolder().addCallback(this);
        this.chosenLevel = chosenLevel;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        DrawLevel thread = new DrawLevel(getHolder(), getContext(), chosenLevel);
        ExecutorService exe = Executors.newSingleThreadExecutor();
        Future<Object[]> f = exe.submit(thread);
        level = new Level(getHolder(), getContext());
        while(!f.isDone()){
        }
        Log.i("thread", "finished");
        try {
            level.setChangeable((Bitmap) (f.get()[0]));
            level.setStaticBitmap((Bitmap) (f.get()[1]));
            level.setEngine((Engine)f.get()[2]);
        } catch (Exception e) {
            Log.i("ERRRRRRRRRRRRRRROOOOOOR", e.toString());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!flag){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                x1 = (int) event.getX();
                y1 = (int) event.getY();
                level.setGe(this);
                ExecutorService exe = Executors.newSingleThreadExecutor();
                Future<Boolean> f = exe.submit(level);
                /*Выполняется, если лазер достиг цели**/
                try {
                    if (f.get()) {
                        flag = true;
                        level.draw();
                    }
                }
                catch (Exception e){
                    Log.i("Error",e.toString());
                }
                break;
        }
        return true;
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (level.getMainMenuButton().isPressed((int)event.getX(), (int)event.getY())) {
                Intent result = new Intent();
                result.putExtra("Result", 1);
                activity.setResult(Activity.RESULT_OK, result);
                activity.finish();
            }
            return true;
        } else {
            return true;
        }
    }
}
