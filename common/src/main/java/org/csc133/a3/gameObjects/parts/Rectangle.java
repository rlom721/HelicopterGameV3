package org.csc133.a3.gameObjects.parts;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameObjects.GameObject;

public class Rectangle extends GameObject {
    private Rectangle() { }

    public Rectangle(int c,
                     int w, int h,
                     float tx, float ty,
                     float sx, float sy,
                     float degrees) {
        setColor(c);
        setDimension(new Dimension(w, h));
        translate(tx, ty);
        scale(sx, sy);
        rotate(degrees);
    }

    @Override
    public void localDraw(Graphics g, Point parentOrigin, Point originScreen) {
        g.setColor(getColor());
        containerTranslate(g, parentOrigin);
        cn1ForwardPrimitiveTranslate(g, getDimension());
        g.drawRect(0, 0, getWidth(), getHeight());
    };
}
