package com.pumple.themirrorsgame.GraphicEngine.Level;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.pumple.themirrorsgame.Engine.Engine;
import com.pumple.themirrorsgame.Engine.Mirror;
import com.pumple.themirrorsgame.Engine.Objects;

import java.util.concurrent.Callable;

/**
 * Created by Pumple on 23.05.2018.
 */

class DrawChangeable implements Callable<Bitmap> {
    private Engine engine;
    private Objects[][] objectses;

    DrawChangeable(Objects[][] objectses, Engine engine) {
        this.engine = engine;
        this.objectses = objectses;
    }

    @Override
    public Bitmap call() throws Exception {
        Bitmap bim = Bitmap.createBitmap(engine.getWidth(), engine.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bim);
        for (int i = 0; i < Engine.OBJECTS_WIDTH; i++) {
            for (int j = 0; j < Engine.OBJECTS_HEIGHT; j++) {
                if ((objectses[i][j] != null) && (objectses[i][j].getClass() == Mirror.class)) {
                    Bitmap bitmap = Bitmap.createScaledBitmap(objectses[i][j].getBitmap(),
                            (canvas.getWidth() / Engine.OBJECTS_WIDTH),
                            (canvas.getHeight() / Engine.OBJECTS_HEIGHT), false);
                    Bitmap bitmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas can = new Canvas(bitmp);
                    can.save(Canvas.MATRIX_SAVE_FLAG);
                    can.rotate(objectses[i][j].getRot(), bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                    can.drawBitmap(bitmap, 0, 0, new Paint());
                    can.restore();
                    canvas.drawBitmap(bitmp, (objectses[i][j].getX() * canvas.getWidth() / Engine.OBJECTS_WIDTH),
                            (objectses[i][j].getY() * canvas.getHeight() / Engine.OBJECTS_HEIGHT),
                            new Paint());
                }
            }
        }
        return bim;
    }
}
