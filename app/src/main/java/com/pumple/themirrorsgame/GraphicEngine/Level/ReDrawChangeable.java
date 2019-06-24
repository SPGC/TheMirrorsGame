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

class ReDrawChangeable implements Callable<Bitmap> {
    Bitmap bim;
    private Mirror objectses;
    private Context context;
    ReDrawChangeable(Mirror objects, Bitmap bitmap, Context context)
    {
        objectses = objects;
        this.context = context;
        bim = bitmap;
    }

    @Override
    public Bitmap call() {
        Canvas canvas = new Canvas(bim);
        canvas.drawBitmap(Bitmap.createBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.field), canvas.getWidth(), canvas.getHeight(), false),
                objectses.getX() * canvas.getWidth() / Engine.OBJECTS_WIDTH, objectses.getY() * canvas.getHeight() / Engine.OBJECTS_HEIGHT,
                canvas.getWidth() / Engine.OBJECTS_WIDTH, canvas.getHeight() / Engine.OBJECTS_HEIGHT), objectses.getX() * canvas.getWidth() / Engine.OBJECTS_WIDTH,
                objectses.getY() * canvas.getHeight() / Engine.OBJECTS_HEIGHT, new Paint());
        Bitmap bitmap = Bitmap.createScaledBitmap(objectses.getBitmap(),
                (canvas.getWidth() / Engine.OBJECTS_WIDTH),
                (canvas.getHeight() / Engine.OBJECTS_HEIGHT), false);
        Bitmap bitmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas can = new Canvas(bitmp);
        can.save(Canvas.MATRIX_SAVE_FLAG);
        can.rotate(objectses.getRot(), bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        can.drawBitmap(bitmap, 0, 0, new Paint());
        can.restore();
        canvas.drawBitmap(bitmp, (objectses.getX() * canvas.getWidth() / Engine.OBJECTS_WIDTH),
                (objectses.getY() * canvas.getHeight() / Engine.OBJECTS_HEIGHT),
                new Paint());
        return bim;
    }
}
