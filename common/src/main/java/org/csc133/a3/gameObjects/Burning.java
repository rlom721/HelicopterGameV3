package org.csc133.a3.gameObjects;

import com.codename1.charts.util.ColorUtil;

public class Burning extends FireState {
    private static Burning burning;

    private Burning() { }

    public static FireState getInstance(){
        if (burning == null)
            burning = new Burning();
        return burning;
    }

    @Override
    void setNextState(Fire fire) {
        fire.setColor(ColorUtil.BLACK);
        fire.setFireState(Extinguished.getInstance());
    }
}
