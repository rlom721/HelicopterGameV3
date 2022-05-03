package org.csc133.a3.gameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.interfaces.Steerable;

import java.util.ArrayList;

import static com.codename1.ui.CN.*;

public class Helicopter extends Movable implements Steerable {
    private Point center;
    final private int size;
    private int fuel, water;
    private final int headingRadius;
    private double angle;
    final private int MAX_SPEED = 10;
    final private int MAX_WATER = 1000;
    private static final int BUBBLE_RADIUS = 125;
    private ArrayList<GameObject> heloParts;

    public Helicopter(Point helipadCenter, int initFuel, Dimension worldSize){
        setWorldSize(worldSize);
        fuel = initFuel;
        water = 0;
        setSpeed(0);
        setHeading(0);
        angle = Math.toRadians(heading());
        size = worldSize.getHeight() / 40;
        headingRadius = size * 2;
        heloParts = new ArrayList<>();

        center = new Point(helipadCenter.getX(), helipadCenter.getY() + size);
        setLocation(new Point(  center.getX() - size / 2,
                                    center.getY() - size / 2));
        setColor(ColorUtil.YELLOW);
        setDimension(new Dimension(size, size));

        // add parts to hierarchical helicopter
        heloParts = new ArrayList<>();
        heloParts.add(new HeloBubble());
    }

//    public Helicopter(Point lz){
//        heloParts = new ArrayList<>();
//        heloParts.add(new HeloBubble());
//    }

    @Override
    public void move() {
        center = new Point( center.getX() + (int)(speed() * Math.cos(angle)),
                            center.getY() - (int)(speed() * Math.sin(angle)));
        setLocation(new Point(  center.getX() - size/2,
                                center.getY() - size/2));
    }

    public void increaseSpeed() {
        if (speed() != MAX_SPEED) setSpeed(speed()+1);
    }

    public void decreaseSpeed() {
        if (speed() != 0) setSpeed(speed()-1);
    }

    public void drink() {
        if (speed() < 3 && water < MAX_WATER)
            water += 100;
    }

    public void fight(Fire fire) {
        fire.shrink(water / 4);
    }

    public void dumpWater() {
        water = 0;
    }

    public void reduceFuel() {
        fuel -= Math.sqrt(speed()) + 5;
        if (fuel < 0) fuel = 0;
    }

    public boolean isAboveRiver(River river) {
        int rX1 = river.getLocation().getX();
        int rX2 = river.getLocation().getX() + river.width();
        int rY1 = river.getLocation().getY();
        int rY2 = river.getLocation().getY() + river.height();

        return hasCollided(rX1, rX2, rY1, rY2);
    }

    public boolean isWithinRangeOfFire(Fire fire) {
        int fX1 = fire.getLocation().getX() - 10;
        int fX2 = fire.getLocation().getX() + fire.diameter() + 10;
        int fY1 = fire.getLocation().getY() - 10;
        int fY2 = fire.getLocation().getY() + fire.diameter() + 10;

        // helicopter is within bounding box of fire
        //
        if (hasCollided(fX1, fX2, fY1, fY2)) {
            // small fire sizes only require heli to be within range of fire
            //
            if (fire.diameter() < 100)
                return true;
            else
                return isWithinCircle(fire.getCenter(), fire.diameter());
        }

        return false;
    }

    public boolean hasLandedOnHelipad(Helipad helipad) {
        return isWithinCircle(helipad.getCenter(), helipad.circleDiameter());
    }

    @Override
    public void steerLeft() {
        if (heading() < 0 || heading() > 360)
            setHeading(0);
        setHeading(heading()+15);
    }

    @Override
    public void steerRight() {
        if (heading() < 0 || heading() > 360)
            setHeading(360);
        setHeading(heading()-15);
    }

    public int fuel() {
        return fuel;
    }

    // checks for collision with other object (passes in bounding box values)
    // x1: left bound, x2: right bound, y1: upper bound, y2 lower bound
    //
    private boolean hasCollided(int x1, int x2, int y1, int y2) {
        int hX1 = getLocation().getX();
        int hX2 = getLocation().getX() + size;
        int hY1 = getLocation().getY();
        int hY2 = getLocation().getY() + size;

        // check bounding box conditions (these are true if NO collision)
        //
        return !(hX1 > x2) && !(hX2 < x1) && !(hY1 > y2) && !(hY2 < y1);
    }

    private boolean isWithinCircle(Point cCenter, int diameter) {
        int x = center.getX() - cCenter.getX();
        int y = center.getY() - cCenter.getY();

        // applies distance formula to see if helicopter is near a fire:
        // d = sqrt(a^2 + b^2)
        //
        return Math.sqrt(x * x + y * y) <= (double) (diameter / 2);
    }


    public void updateLocalTransforms() {

    }

    //-------------------------------------------------------------------------
    private static class HeloBubble extends GameObject{
        public HeloBubble(){
            setColor(ColorUtil.YELLOW);
            setDimension(new Dimension(2*Helicopter.BUBBLE_RADIUS,
                    2*Helicopter.BUBBLE_RADIUS));
            //translate();
        }

        @Override
        public void localDraw(Graphics g, Point parentOrigin,
                              Point screenOrigin){
            g.setColor(getColor());
            containerTranslate(g, parentOrigin);
            cn1ForwardPrimitiveTranslate(g, getDimension());
            g.drawArc(0, 0,
                    getDimension().getWidth(), getDimension().getHeight(),
                    135, 270);
        }
    }
    //-------------------------------------------------------------------------

    @Override
    public void localDraw(Graphics g, Point parentOrigin, Point screenOrigin){
        for (GameObject go : heloParts)
            go.draw(g, parentOrigin, screenOrigin);
    }

//    @Override
//    public void draw(Graphics g, Point containerOrigin) {
//        angle = Math.toRadians(heading()) + Math.PI / 2;
//
//        g.setColor(getColor());
//        g.setFont(Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_MEDIUM));
//
//        // draw helicopter body and show stats
//        //
//        g.fillArc(containerOrigin.getX() + getLocation().getX(),
//                containerOrigin.getY() + getLocation().getY(), size, size,
//                0, 360);
//        g.drawString("F:  " + fuel,
//                containerOrigin.getX() + getLocation().getX() - size/2,
//                containerOrigin.getY() + getLocation().getY() + size*3);
//        g.drawString("W: " + water,
//                containerOrigin.getX() + getLocation().getX() - size / 2,
//                containerOrigin.getY() + getLocation().getY() + size * 4);
//
//        // use polar to coordinate conversion for heading line position
//        //
//        int heX = containerOrigin.getX() + center.getX()
//                + (int) (headingRadius * Math.cos(angle));
//        int heY = containerOrigin.getY() +
//                center.getY() - (int) (headingRadius * Math.sin(angle));
//
//        g.drawLine(containerOrigin.getX() + center.getX(),
//                containerOrigin.getY() + center.getY(), heX, heY);
//    }
}
