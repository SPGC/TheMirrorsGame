package com.pumple.themirrorsgame.GraphicEngine.Level;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.pumple.themirrorsgame.Engine.Engine;
import com.pumple.themirrorsgame.Engine.Laser;
import com.pumple.themirrorsgame.Engine.Mirror;
import com.pumple.themirrorsgame.Engine.Objects;
import com.pumple.themirrorsgame.GraphicEngine.GraphicEngine;
import com.pumple.themirrorsgame.GraphicEngine.MenuSettings.Buttons;
import com.example.themirrorsgame.R;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.pumple.themirrorsgame.Engine.Engine.OBJECTS_HEIGHT;
import static com.pumple.themirrorsgame.Engine.Engine.OBJECTS_WIDTH;

/**
 * Created by Pumple on 16.05.2018.
 */

public class Level implements Callable<Boolean> {
    private Bitmap changeable, staticBitmap;
    private SurfaceHolder surfaceHolder;
    private GraphicEngine ge;
    private Engine engine;
    private Context context;
    private Buttons chooseLevel = null;
    private Buttons mainmenu = null;
    public Engine getEngine() {
        return engine;
    }

    public Buttons getMainMenuButton(){
        return mainmenu;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
        mainmenu = new Buttons(new int[]{getEngine().getWidth() / 3, getEngine().getHeight() / 2 + 10,
                getEngine().getWidth() / 3 + (getEngine().getHeight() / 6 + getEngine().getWidth() / 6) / 2,
                getEngine().getHeight() / 2 + (getEngine().getHeight() / 6 + getEngine().getWidth() / 6) / 2 + 10,},
                "mainmenu", BitmapFactory.decodeResource(context.getResources(), R.drawable.win));
        chooseLevel = new Buttons(new int[]{getEngine().getWidth() / 3, getEngine().getHeight() / 4,
                getEngine().getWidth() * 2 / 3, getEngine().getHeight() / 2,},
                "chooseLevel", BitmapFactory.decodeResource(context.getResources(), R.drawable.chooselevelwin));
    }

    public void setGe(GraphicEngine ge) {
        this.ge = ge;
    }

    public Level(SurfaceHolder surfaceHolder, Context context) {
        this.surfaceHolder = surfaceHolder;
        this.context = context;
    }

    public void setChangeable(Bitmap changeable) {
        this.changeable = changeable;
    }

    public void setStaticBitmap(Bitmap staticBitmap) {
        this.staticBitmap = staticBitmap;
    }

    public Boolean call() {
        boolean b = false;
        Objects[][] objectses = engine.getObjects();
        if (objectses[ge.x / (engine.getWidth() / OBJECTS_WIDTH)][ge.y / (engine.getHeight() / OBJECTS_HEIGHT)] != null) {
            if (Mirror.class == objectses[ge.x / (engine.getWidth() / OBJECTS_WIDTH)][ge.y / (engine.getHeight() / OBJECTS_HEIGHT)].getClass()) {
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    ((Mirror) (objectses[ge.x / (canvas.getWidth() / OBJECTS_WIDTH)][ge.y / (canvas.getHeight() / OBJECTS_HEIGHT)])).setRot(
                            ((Mirror)(objectses[ge.x / (canvas.getWidth() / OBJECTS_WIDTH)][ge.y / (canvas.getHeight() / OBJECTS_HEIGHT)])).countRotation(ge.x1,ge.y1)/*getRot() + (ge.y - ge.y1) / 10*/);

                    ExecutorService exe1 = Executors.newSingleThreadExecutor();
                    Future<ArrayList<Laser>> f1 = exe1.submit(new CountLazer(engine, 2));
                    /*while (!f1.isDone()){}
                    Log.i("Laser","Finished");*/
                    ExecutorService exe = Executors.newSingleThreadExecutor();
                    Future<Bitmap> f = exe.submit(new DrawChangeable(objectses, engine));
                    canvas.drawBitmap(staticBitmap, 1, 1, new Paint());
                    try {
                        b = DrawLevel.DrawLaser(f1.get(), canvas);
                        canvas.drawBitmap(f.get(), 1, 1, new Paint());
                    } catch (Exception e) {
                        canvas.drawText(e.toString(), 200, 300, new Paint());
                    }
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
                return b;
            }
        }
        return false;
    }
    public void draw (){
        Objects[][] objectses = engine.getObjects();
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            ExecutorService exe1 = Executors.newSingleThreadExecutor();
            Future<ArrayList<Laser>> f1 = exe1.submit(new CountLazer(engine, 2));
            ExecutorService exe = Executors.newSingleThreadExecutor();
            Future<Bitmap> f = exe.submit(new DrawChangeable(objectses, engine));
            canvas.drawBitmap(staticBitmap, 1, 1, new Paint());
            try {
                DrawLevel.DrawLaser(f1.get(), canvas);
                canvas.drawBitmap(f.get(), 1, 1, new Paint());
            } catch (Exception e) {
                canvas.drawText(e.toString(), 200, 300, new Paint());
            }
            //canvas.rotate(180, canvas.getWidth() / 2, canvas.getHeight() / 2);
            canvas.drawBitmap(Bitmap.createScaledBitmap(chooseLevel.getBitmap(), chooseLevel.getCoordinates()[0] - chooseLevel.getCoordinates()[2],
                    chooseLevel.getCoordinates()[1] - chooseLevel.getCoordinates()[3], false), chooseLevel.getCoordinates()[0], chooseLevel.getCoordinates()[1], new Paint());
            canvas.drawBitmap(Bitmap.createScaledBitmap(mainmenu.getBitmap(), mainmenu.getCoordinates()[0] - mainmenu.getCoordinates()[2],
                    mainmenu.getCoordinates()[1] - mainmenu.getCoordinates()[3], false), mainmenu.getCoordinates()[0], mainmenu.getCoordinates()[1], new Paint());
            //canvas.restore();
        }
        surfaceHolder.unlockCanvasAndPost(canvas);

    }
}

