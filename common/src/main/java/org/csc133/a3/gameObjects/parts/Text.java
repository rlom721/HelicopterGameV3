package org.csc133.a3.gameObjects.parts;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameObjects.GameObject;

public class Text extends GameObject {
    final private String textString;

    public Text(int c,
                float tx, float ty,
                float sx, float sy,
                String text) {
        setColor(c);
        translate(tx, ty);
        scale(sx, sy);
        textString = text;
    }

    @Override
    public void localDraw(Graphics g, Point parentOrigin, Point originScreen) {
        g.setColor(getColor());
        containerTranslate(g, parentOrigin);
        cn1ForwardPrimitiveTranslate(g, getDimension());
        g.drawString(textString, parentOrigin.getX(), parentOrigin.getY());
    }
}
