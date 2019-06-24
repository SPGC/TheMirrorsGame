package com.pumple.themirrorsgame.GraphicEngine.Level;

import com.pumple.themirrorsgame.Engine.Engine;
import com.pumple.themirrorsgame.Engine.Laser;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Created by Pumple on 21.05.2018.
 */

class CountLazer implements Callable<ArrayList<Laser>>{

    private Engine engine;
    private int a;
    CountLazer(Engine engine, int a){
        this.a = a;
        this.engine = engine;
    }

    @Override
    public ArrayList<Laser> call() throws Exception {
        engine.lazer(a);
        //    Log.i("CountLaser", "I've been called");
        return engine.getLaserList();
    }
}
