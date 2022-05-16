package org.csc133.a3.gameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameObjects.parts.Rectangle;

public class River extends Fixed {
    final private Rectangle riverBody;

    public River(Dimension worldSize) {
        setWorldSize(worldSize);
        setDimension(new Dimension( worldSize.getWidth(),
                              worldSize.getHeight()/8));
        setLocation( new Point((width()/2),
                    (int)(0.67*worldSize.getHeight())));
        riverBody = new Rectangle(ColorUtil.BLUE, width(), height(),
                                    getLocation().getX(), getLocation().getY(),
                                    1, 1, 0);
//        riverBody = new Rectangle(ColorUtil.BLUE, width(), height(),
//                (float)(width()/2), (int)(0.67*worldSize.getHeight()),
//                1, 1, 0);
    }

    public int width() { return getDimension().getWidth(); }

    public int height() { return getDimension().getHeight(); }

    @Override
    public void localDraw(Graphics g, Point parentOrigin, Point originScreen) {
        riverBody.draw(g, parentOrigin, originScreen);
    }
}
