package org.csc133.a3.gameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;

public class Helipad extends Fixed{
    final private Point center;
    final private int sSize;
    final private int cSize;
    final private int cOffset;

    public Helipad(Dimension worldSize) {
        setWorldSize(worldSize);
        setDimension(new Dimension( worldSize.getHeight()/8,
                                    worldSize.getHeight()/8));
        setColor(ColorUtil.GRAY);

        sSize = getDimension().getWidth();
        cOffset = 40;
        cSize = sSize - cOffset;

        setLocation(new Point(  worldSize.getWidth()/2 - sSize/2, sSize/2));
        center = new Point( getLocation().getX() + sSize / 2,
                            getLocation().getY() + sSize / 2);
    }

    public Point getCenter() { return center; }

    int circleDiameter() { return cSize; }

    @Override
    public void localDraw(Graphics g, Point parentOrigin, Point originScreen) {
        g.setColor(getColor());
        g.drawRect( originScreen.getX() + getLocation().getX(),
                originScreen.getY() + getLocation().getY(),
                sSize, sSize, 5);
        g.drawArc(  originScreen.getX() + getLocation().getX() + cOffset/2,
                originScreen.getY() + getLocation().getY() + cOffset/2,
                cSize, cSize, 0, 360);
    }
}
