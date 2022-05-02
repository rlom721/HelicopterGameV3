package org.csc133.a3.gameObjects;

import com.codename1.charts.util.ColorUtil;

public class UnStarted extends FireState {
    private static UnStarted unStarted;

    private UnStarted() { }

    public static FireState getInstance(){
        if (unStarted == null)
            unStarted = new UnStarted();
        return unStarted;
    }

    @Override
    void setNextState(Fire fire) {
        fire.setColor(ColorUtil.MAGENTA);
        fire.setFireState(Burning.getInstance());
    }
}
