package org.csc133.a3.gameObjects.parts;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameObjects.GameObject;

public class Arc extends GameObject {
    private final int startAngle;
    private final int arcAngle;

    public Arc() {
        this.startAngle = 0;
        this.arcAngle = 0;
    }

    public Arc(int color, int w, int h, int startAngle, int arcAngle){
        this(color, w, h, 0, 0, 0, 0, 0,
                startAngle, arcAngle);
    }

    public Arc(   int color,
                  int w, int h,
                  float tx, float ty,
                  float sx, float sy,
                  float degreesRotation,
                  int startAngle, int arcAngle){

        setColor(color);
        setDimension(new Dimension(w, h));
        this.startAngle = startAngle;
        this.arcAngle = arcAngle;

        translate(tx, ty);
        scale(sx, sy);
        rotate(degreesRotation);
    }

    public void setDiameter(int diameter) {
        setDimension(new Dimension(diameter, diameter));
    }

    @Override
    public void localDraw(Graphics g, Point parentOrigin, Point originScreen) {
        g.setColor(getColor());
        containerTranslate(g, parentOrigin);
        cn1ForwardPrimitiveTranslate(g, getDimension());
        g.drawArc(0, 0, getWidth(), getHeight(), startAngle, arcAngle);
    }
}
