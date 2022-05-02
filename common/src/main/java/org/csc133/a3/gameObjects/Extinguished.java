package org.csc133.a3.gameObjects;

public class Extinguished extends FireState {
    private static Extinguished extinguished;

    private Extinguished() { }

    public static FireState getInstance(){
        if (extinguished == null)
            extinguished = new Extinguished();
        return extinguished;
    }

    @Override
    void setNextState(Fire fire) { }
}
