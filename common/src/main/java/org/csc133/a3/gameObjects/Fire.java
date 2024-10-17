package org.csc133.a3.gameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameObjects.parts.Arc;
import org.csc133.a3.gameObjects.parts.Rectangle;

import java.util.Random;
import static com.codename1.ui.CN.*;

public class Fire extends Fixed {
    private Point center;
    private int diameter;
    private FireState state;
    private Building building;
    private int maxSize;
//    final private Arc fireBody;

    public Fire(Building building, Dimension worldSize) {
        diameter = 0;
        setWorldSize(worldSize);
        state = UnStarted.getInstance();
        this.building = building;
        this.building.setFireInBuilding(this);
        setDimension(new Dimension(diameter, diameter));
        this.center = new Point(getLocation().getX() + diameter / 2,
                                getLocation().getY() + diameter / 2);
        maxSize = 0;
//        fireBody = new Arc( ColorUtil.MAGENTA,
//                            diameter, diameter,
//                            0f, 0f, 1f, 1f,
//                            0, 0, 360);
//        translate(getLocation().getX(), getLocation().getY());
    }

    public void grow() {
        Random rand = new Random();
        int increase = rand.nextInt(2);

        // increase diameter without changing the center of the fire
        //
        diameter += increase;
        setLocation(new Point(center.getX() - diameter / 2,
                center.getY() - diameter / 2));

        if (getArea() > maxSize) maxSize = getArea();
    }

    void shrink(int reduce) {
        diameter -= reduce;
        if (diameter < 0) diameter = 0;
        setLocation(new Point(center.getX() - diameter / 2,
                center.getY() - diameter / 2));
    }

    public int getArea() {
        int radius = diameter / 2;
        return (int) (Math.PI * radius * radius);
    }

    Point getCenter() {
        return center;
    }

    public int diameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
//        if (fireBody != null)
//            fireBody.setDiameter(diameter);
        this.diameter = diameter;
    }

    void setFireState(FireState fireState) {
        state = fireState;
    }

    public void start() {
        state.setNextState(this);
    }

    public void extinguish() {
        state.setNextState(this);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public FireState getState() {
        return state;
    }

    @Override
    public void localDraw(Graphics g, Point parentOrigin, Point originScreen) {
        g.setFont(Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_MEDIUM));
        g.setColor(getColor());
//        containerTranslate(g, parentOrigin);
//        cn1ForwardPrimitiveTranslate(g, getDimension());
//        fireBody.setDiameter(diameter);
        if (diameter > 0) {
            g.fillArc(parentOrigin.getX() + getLocation().getX(),
                    parentOrigin.getY() + getLocation().getY(),
                    diameter, diameter, 0, 360);
//            fireBody.draw(g, parentOrigin, originScreen);
            g.drawString(Integer.toString(diameter),
                    parentOrigin.getX() + getLocation().getX() + diameter,
                    parentOrigin.getY() + getLocation().getY() + diameter);
        }
    }
}