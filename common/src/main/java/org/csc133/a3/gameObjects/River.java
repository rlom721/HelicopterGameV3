package org.csc133.a3.gameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.Game;
import org.csc133.a3.GameWorld;
import org.csc133.a3.gameObjects.parts.Rectangle;
import org.w3c.dom.css.Rect;

public class River extends Fixed {
    final private Rectangle riverBody;

    public River(Dimension worldSize) {
        setWorldSize(worldSize);
        setDimension(new Dimension( worldSize.getWidth(),
                              worldSize.getHeight()/8));
        riverBody = new Rectangle(ColorUtil.BLUE, width(), height(),
                                    0, 0,  1, 1, 0);
        translate((float)(width()/2), (int)(0.67*worldSize.getHeight()));
    }

    public int width() { return getDimension().getWidth(); }

    public int height() { return getDimension().getHeight(); }

    @Override
    public void localDraw(Graphics g, Point parentOrigin, Point originScreen) {
        riverBody.draw(g, parentOrigin, originScreen);
    }
}
