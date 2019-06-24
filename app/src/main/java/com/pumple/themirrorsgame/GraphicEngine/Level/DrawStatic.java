package com.pumple.themirrorsgame.GraphicEngine.Level;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.pumple.themirrorsgame.Engine.Engine;
import com.pumple.themirrorsgame.Engine.Mirror;
import com.pumple.themirrorsgame.Engine.Objects;
import com.example.themirrorsgame.R;

import java.util.concurrent.Callable;

/**
 * Created by Pumple on 15.05.2018.
 */

class DrawStatic implements Callable<Bitmap> {
    private Bitmap bim;
    private Objects[][] objectses;
    private Context context;

    DrawStatic (Objects[][] objects, Engine engine, DrawLevel dl, Context context)  {
        objectses = objects;
        bim = Bitmap.createBitmap(engine.getWidth(),engine.getHeight(), Bitmap.Config.ARGB_8888);
        this.context = context;
    }

    @Override
    public Bitmap call() {
        Canvas canvas = new Canvas(bim);
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.field);
        Bitmap bmp = Bitmap.createScaledBitmap(bm,canvas.getWidth(),canvas.getHeight(),false);
        canvas.drawBitmap(bmp,0,0,new Paint());
        for (int i = 0; i < Engine.OBJECTS_WIDTH; i++) {
            for (int j = 0; j < Engine.OBJECTS_HEIGHT; j++) {
                if ((objectses[i][j] != null) && (objectses[i][j].getClass() != Mirror.class)) {
                    Bitmap bitmap = Bitmap.createScaledBitmap(objectses[i][j].getBitmap(),
                            (canvas.getWidth() / Engine.OBJECTS_WIDTH),
                            (canvas.getHeight() / Engine.OBJECTS_HEIGHT), false);
                    Bitmap bitmp = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas can = new Canvas(bitmp);
                    can.save(Canvas.MATRIX_SAVE_FLAG);
                    can.rotate(objectses[i][j].getRot(), bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                    can.drawBitmap(bitmap, 0, 0, new Paint());
                    can.restore();
                    canvas.drawBitmap(bitmp,(objectses[i][j].getX() * canvas.getWidth() / Engine.OBJECTS_WIDTH),
                            (objectses[i][j].getY() * canvas.getHeight() / Engine.OBJECTS_HEIGHT),
                            new Paint());
                }
            }
        }
        return bim;
    }
}
