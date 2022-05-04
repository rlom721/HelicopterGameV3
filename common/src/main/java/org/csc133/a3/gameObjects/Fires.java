package org.csc133.a3.gameObjects;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

public class Fires extends GameObjectCollection<Fire> {

    public Fires(){
        super();
    }

    @Override
    public void localDraw(Graphics g, Point parentOrigin, Point originScreen) {
        for (Fire fire : getGameObjects())
            fire.draw(g, parentOrigin, originScreen);
    }

//    @Override
//    public void draw(Graphics g, Point containerOrigin) {
//        for (Fire fire : getGameObjects())
//            fire.draw(g, parentOrigin, containerOrigin);
//    }
}
