package org.csc133.a3.gameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameObjects.parts.Arc;
import org.csc133.a3.gameObjects.parts.Rectangle;

public class Helipad extends Fixed{
    final private Point center;
    final private int size;
    final private Rectangle heliSquare;
    final private Arc heliArc;

    public Helipad(Dimension worldSize) {
        setWorldSize(worldSize);
        setDimension(new Dimension( worldSize.getHeight()/8,
                                    worldSize.getHeight()/8));
        size = getDimension().getWidth();
        heliSquare = new Rectangle( ColorUtil.GRAY, size, size,
                                    0, 0, 1, 1,
                                0, 5);
        heliArc = new Arc(  ColorUtil.GRAY, size, size, 0f, 0f,
                        0.75f, 0.75f,
                            0f, 0, 360);

//        setLocation(new Point(  worldSize.getWidth()/2 - size/2, size/2));
        setLocation(new Point((worldSize.getWidth()/2), (int)(size*1.5)));
        center = new Point( getLocation().getX() + size / 2,
                            getLocation().getY() + size / 2);
        translate(getLocation().getX(), getLocation().getY());

//        translate((float)(worldSize.getWidth()/2), size*1.5);
    }

    public Point getCenter() { return center; }

    int circleDiameter() { return size; }

    @Override
    public void localDraw(Graphics g, Point parentOrigin, Point originScreen) {
        heliSquare.draw(g, parentOrigin, originScreen);
        heliArc.draw(g, parentOrigin, originScreen);
    }
}
