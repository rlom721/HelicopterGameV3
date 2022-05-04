package org.csc133.a3.views;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.Game;
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
        helicopter = new Helicopter(new Point(0, 0), GameWorld.INIT_FUEL,
                                    GameWorld.getInstance().getDimension());
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

    @Override
    public void laidOut(){
        gw.setDimension(new Dimension(this.getWidth(), this.getHeight()));
        gw.init();
    }

    @Deprecated
    void containerTranslate(Graphics g, Point parentOrigin){
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(parentOrigin.getX(), parentOrigin.getY());
        g.setTransform(gxForm);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Point parentOrigin = new Point(this.getX(), this.getY());
        Point screenOrigin = new Point(getAbsoluteX(), getAbsoluteY());

        displayTransform(g);

        // move origin to center of display
        //
        Transform t = Transform.makeIdentity();
        g.getTransform(t);
        t.translate(getWidth()/2, getHeight()/2);
        t.scale(1.5f, 1.5f);
        g.setTransform(t);

        // draw axis
        //
        g.setColor(ColorUtil.LTGRAY);
        g.drawLine(-getWidth()/2, 0, getWidth()/2, 0);
        g.drawLine(0, -getHeight()/2, 0, getWidth()/2);

        helicopter.draw(g, parentOrigin, screenOrigin);

        g.resetAffine();

//        for (GameObject go: gw.getGameObjectCollection()) {
//            setupVTM(g);
//
//            go.draw(g, parentOrigin, screenOrigin);
//            g.resetAffine();
//        }
    }

    public void updateLocalTransforms() {
//        for (GameObject go: gw.getGameObjectCollection())
//            go.updateLocalTransforms();
        helicopter.updateLocalTransforms();
    }

    private Transform buildWorldToNDXform(float winWidth, float winHeight,
                                               float winLeft, float winBottom){
        Transform tmpXform = Transform.makeIdentity();
        tmpXform.scale((1/winWidth), (1/winHeight));
        tmpXform.translate(-winLeft, -winBottom);
        return tmpXform;
    }

    private Transform buildNDToDisplayXform(float displayWidth,
                                                float displayHeight){
        Transform tmpXform = Transform.makeIdentity();
        tmpXform.translate(0, displayHeight);
        tmpXform.scale(displayWidth, -displayHeight);
        return tmpXform;
    }

    private void setupVTM(Graphics g){
        Transform worldToND, ndToDisplay, theVTM;
        float winLeft, winRight, winTop, winBottom;

        winLeft = winBottom = 0;
        winRight = this.getWidth();
        winTop = this.getHeight();

        float winHeight = winTop - winBottom;
        float winWidth = winRight - winLeft;

        worldToND = buildWorldToNDXform(winWidth, winHeight,
                                        winLeft, winBottom);
        ndToDisplay = buildNDToDisplayXform(this.getWidth(), this.getHeight());
        theVTM = ndToDisplay.copy();
        theVTM.concatenate(worldToND);

        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(getAbsoluteX(), getAbsoluteY());
        gxForm.concatenate(theVTM);
        gxForm.translate(-getAbsoluteX(), -getAbsoluteY());
        g.setTransform(gxForm);
    }

    // empty methods for helicopter commands?
    // state pattern delegates methods to object of state
}
