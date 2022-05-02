package org.csc133.a3.views;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.GameWorld;
import org.csc133.a3.gameObjects.GameObject;
import org.csc133.a3.gameObjects.Helicopter;

public class MapView extends Container {
    GameWorld gw;
    Helicopter helicopter;

    public MapView(GameWorld gw){
        this.gw = gw;

        this.getAllStyles().setBgColor(ColorUtil.BLACK);
        this.getAllStyles().setBgTransparency(255);
    }

    public void init(){
        helicopter = new Helicopter(new Point(0, 0));
    }

    public void displayTransform(Graphics g){
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        gXform.translate(getAbsoluteX(), getAbsoluteY());

        // apply display mapping
        //
        gXform.translate(0, getHeight());
        gXform.scale(1f, -1f);

        // move drawing coordinates as part of the local origin transformations
        //
        gXform.translate(-getAbsoluteX(), -getAbsoluteY());
        g.setTransform(gXform);
    }

//    public void updateLocalTransforms(){
//        helicopter.updateLocalTransforms();
//    }

    @Override
    public void laidOut(){
        gw.setDimension(new Dimension(this.getWidth(), this.getHeight()));
        gw.init();
    }

    @Override
    public void paint(Graphics g) {
        for (GameObject go: gw.getGameObjectCollection())
            go.draw(g, new Point(this.getX(), this.getY()));

        Point parentOrigin = new Point(this.getX(), this.getY());
        Point screenOrigin = new Point(getAbsoluteX(), getAbsoluteY());

        displayTransform(g);
        helicopter.draw(g, parentOrigin, screenOrigin);
        g.resetAffine();
    }

    public void updateLocalTransforms() {
        helicopter.updateLocalTransforms();
    }

    // empty methods for helicopter commands?
    // state pattern delegates methods to object of state
}
