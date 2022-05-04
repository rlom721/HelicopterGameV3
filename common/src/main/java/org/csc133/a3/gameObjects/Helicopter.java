package org.csc133.a3.gameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.Game;
import org.csc133.a3.gameObjects.parts.Arc;
import org.csc133.a3.gameObjects.parts.Rectangle;
import org.csc133.a3.interfaces.Steerable;

import java.awt.*;
import java.util.ArrayList;

import static com.codename1.ui.CN.*;

public class Helicopter extends Movable implements Steerable {
    private static final int BUBBLE_RADIUS = 125;
    private static final int ENGINE_BLOCK_WIDTH = (int) (BUBBLE_RADIUS * 1.8);
    private static final int ENGINE_BLOCK_HEIGHT = ENGINE_BLOCK_WIDTH / 3;
    private static final int BLADE_LENGTH = BUBBLE_RADIUS * 5;
    private static final int BLADE_WIDTH = 25;
    private static final int RAIL_WIDTH = BLADE_WIDTH;
    private static final int RAIL_HEIGHT = ENGINE_BLOCK_HEIGHT*5;
    private static final int RAIL_CONNECTOR_WIDTH = (int) (BLADE_WIDTH * 1.75);
    private static final int RAIL_CONNECTOR_LENGTH = BLADE_WIDTH / 2;
    private static final int TAIL_WIDTH = 5;
    private static final int TAIL_LENGTH = ENGINE_BLOCK_HEIGHT*4;
    private static final int TAIL_END_WIDTH = TAIL_WIDTH*7;
    private static final int TAIL_END_LENGTH = 20;
    private Point center;
    final private int size;
    private int fuel, water;
    private final int headingRadius;
    private double angle;
    final private int MAX_SPEED = 10;
    final private int MAX_WATER = 1000;
    private ArrayList<GameObject> heloParts;
    private HeloBlade heloBlade;

    public Helicopter(Point helipadCenter, int initFuel, Dimension worldSize) {
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
        setLocation(new Point(center.getX() - size / 2,
                center.getY() - size / 2));
        setColor(ColorUtil.YELLOW);
        setDimension(new Dimension(size, size));

        // add parts to hierarchical helicopter
        heloParts = new ArrayList<>();
        heloParts.add(new HeloRailConnectorBottom(-1));
        heloParts.add(new HeloRailConnectorBottom(1));
        heloParts.add(new HeloRailConnectorTop(-1));
        heloParts.add(new HeloRailConnectorTop(1));
        heloParts.add(new HeloBubble());
        heloParts.add(new HeloEngineBlock());
        heloBlade = new HeloBlade();
        heloParts.add(heloBlade);
        heloParts.add(new HeloBladeShaft());
        heloParts.add(new HeloRail(-1));
        heloParts.add(new HeloRail(1));
        heloParts.add(new HeloTailRod());
        heloParts.add(new HeloTailEnd());
        heloParts.add(new HeloTailSide(-1));
        heloParts.add(new HeloTailSide(1));
        scale(0.5, 0.5);
        translate(getLocation().getX(), getLocation().getY());
    }

    @Override
    public void move() {
        center = new Point(center.getX() + (int) (speed() * Math.cos(angle)),
                center.getY() - (int) (speed() * Math.sin(angle)));
        setLocation(new Point(center.getX() - size / 2,
                center.getY() - size / 2));
    }

    public void increaseSpeed() {
        if (speed() != MAX_SPEED) setSpeed(speed() + 1);
    }

    public void decreaseSpeed() {
        if (speed() != 0) setSpeed(speed() - 1);
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
        setHeading(heading() + 15);
    }

    @Override
    public void steerRight() {
        if (heading() < 0 || heading() > 360)
            setHeading(360);
        setHeading(heading() - 15);
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
        heloBlade.updateLocalTransforms(-30d);
    }

    //-------------------------------------------------------------------------
    private static class HeloBubble extends Arc {
        public HeloBubble() {
            super(ColorUtil.YELLOW,
                    2 * Helicopter.BUBBLE_RADIUS,
                    2 * Helicopter.BUBBLE_RADIUS,
                    0, (float) (Helicopter.BUBBLE_RADIUS * 0.80),
                    1, 1,
                    0,
                    135, 270);
        }
    }

    //-------------------------------------------------------------------------
    private static class HeloEngineBlock extends Rectangle {
        public HeloEngineBlock() {
            super(ColorUtil.YELLOW,
                    ENGINE_BLOCK_WIDTH,
                    ENGINE_BLOCK_HEIGHT,
                    0, (float) (-ENGINE_BLOCK_HEIGHT / 2),
                    1, 1, 0);
        }
    }

    //-------------------------------------------------------------------------
    private static class HeloBlade extends Rectangle {
        public HeloBlade() {
            super(ColorUtil.LTGRAY,
                    Helicopter.BLADE_LENGTH,
                    Helicopter.BLADE_WIDTH,
                    0, -Helicopter.ENGINE_BLOCK_HEIGHT / 2,
                    1, 1,
                    45);
        }

        public void updateLocalTransforms(double rotationSpeed) {
            this.rotate(rotationSpeed);
        }
    }

    //-------------------------------------------------------------------------
    private static class HeloBladeShaft extends Arc {
        public HeloBladeShaft() {
            super(ColorUtil.GRAY,
                    2 * Helicopter.BLADE_WIDTH / 3,
                    2 * Helicopter.BLADE_WIDTH / 3,
                    0, -Helicopter.ENGINE_BLOCK_HEIGHT / 2,
                    1, 1,
                    0, 0,
                    360);
        }
    }

    //-------------------------------------------------------------------------
    private static class HeloRail extends Rectangle {
        public HeloRail(int heliSide) {
            super(ColorUtil.MAGENTA,
                    Helicopter.RAIL_WIDTH,
                    Helicopter.RAIL_HEIGHT,
                    (float) (0.75 * Helicopter.ENGINE_BLOCK_WIDTH) * heliSide,
                    (int)(ENGINE_BLOCK_HEIGHT/2),
                    1, 1, 0);
        }
    }

    //-------------------------------------------------------------------------
    private static class HeloRailConnectorBottom extends Rectangle {
        public HeloRailConnectorBottom(int heliSide) {
            super(ColorUtil.GRAY,
                    Helicopter.RAIL_CONNECTOR_WIDTH,
                    Helicopter.RAIL_CONNECTOR_LENGTH,
                    (float) (Helicopter.ENGINE_BLOCK_WIDTH / 1.7) * heliSide,
//                        - (float)(Helicopter.RAIL_CONNECTOR_WIDTH/2)*heliSide,
                    (float) (-ENGINE_BLOCK_HEIGHT / 2),
                    1, 1, 0);
        }

        @Override
        public void localDraw(Graphics g, Point parentOrigin,
                              Point originScreen) {
            super.localDraw(g, parentOrigin, originScreen);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    //-------------------------------------------------------------------------
    private static class HeloRailConnectorTop extends Rectangle {
        public HeloRailConnectorTop(int heliSide) {
            super(ColorUtil.GRAY,
                    Helicopter.RAIL_CONNECTOR_WIDTH,
                    Helicopter.RAIL_CONNECTOR_LENGTH,
                    (float) (Helicopter.ENGINE_BLOCK_WIDTH / 1.7) * heliSide,
                    (float) (ENGINE_BLOCK_HEIGHT * 2),
                    1, 1, 0);
        }

        @Override
        public void localDraw(Graphics g, Point parentOrigin,
                              Point originScreen) {
            super.localDraw(g, parentOrigin, originScreen);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    //-------------------------------------------------------------------------
    private static class HeloTailRod extends Rectangle {
        public HeloTailRod() {
            super(ColorUtil.YELLOW,
                    Helicopter.TAIL_WIDTH,
                    Helicopter.TAIL_LENGTH,
                    0, (float) (-ENGINE_BLOCK_HEIGHT*3),
                    1, 1, 0);
        }

        @Override
        public void localDraw(Graphics g, Point parentOrigin,
                              Point originScreen) {
            super.localDraw(g, parentOrigin, originScreen);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    //-------------------------------------------------------------------------
    private static class HeloTailSide extends GameObject{
        int x1, x2, y1, y2;
        int slope;

        public HeloTailSide(int heliSide){
            x1 = Helicopter.TAIL_END_WIDTH/2 *heliSide;
            y1 = -ENGINE_BLOCK_HEIGHT*3 - TAIL_END_LENGTH;
            x2 = ENGINE_BLOCK_WIDTH/8 *heliSide;
            y2 = ENGINE_BLOCK_HEIGHT/2 + TAIL_WIDTH;
            slope = (y1-y2)/(x1-x2);
        }

        @Override
        public void localDraw(Graphics g, Point parentOrigin,
                              Point originScreen){
            g.drawLine(x1, y1, x2, y2);

//            int midX = x2/2;
//            int midY = -slope*midX;
//            g.drawLine(x1, y1, -x2/2, y2/40);
//            g.drawLine(x1, y1, -midX, -midY);
        }
    }
    //-------------------------------------------------------------------------
    private static class HeloTailEnd extends Rectangle{
        public HeloTailEnd(){
            super(ColorUtil.GRAY,
                    Helicopter.TAIL_END_WIDTH,
                    Helicopter.TAIL_END_LENGTH,
                    0, (float) (-ENGINE_BLOCK_HEIGHT*5),
                    1, 1, 0);
        }

        @Override
        public void localDraw(Graphics g, Point parentOrigin,
                              Point originScreen) {
            super.localDraw(g, parentOrigin, originScreen);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    //-------------------------------------------------------------------------
    @Override
    public void localDraw(Graphics g, Point parentOrigin, Point screenOrigin) {
//        Point newLocation = new Point(  parentOrigin.getX()
//                                            + getLocation().getX(),
//                                        parentOrigin.getY()
//                                            + getLocation().getY());
        for (GameObject go : heloParts)
            go.draw(g, parentOrigin, screenOrigin);
//        go.draw(g, newLocation, screenOrigin);
    }
}
