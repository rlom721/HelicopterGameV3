package org.csc133.a3.gameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import java.util.Random;

public class Building extends Fixed {
    private int value;
    final private int MAX_FIRE_DIAMETER = 26;
    final private int MIN_FIRE_DIAMETER = MAX_FIRE_DIAMETER-5;
    private int fireAreaBudget;
    private Fires fires;
    final private int area;

    public Building(Point position, Dimension dimension, Dimension worldSize) {
        setWorldSize(worldSize);
        fires = new Fires();
        setColor(ColorUtil.rgb(255, 0, 0));
        setLocation(position);
        setDimension(dimension);
        value = (width() % 10) * 100;
        area = dimension.getHeight()*dimension.getWidth();
        fireAreaBudget = 1000;
//        translate(location.getX(), location.getY());
    }

    public void setFireInBuilding(Fire fire){
        Random rand = new Random();
        fire.setDiameter(rand.nextInt(MAX_FIRE_DIAMETER-MIN_FIRE_DIAMETER)
                                        + MIN_FIRE_DIAMETER);
        fire.setLocation(new Point( this.getLocation().getX()
                                + rand.nextInt(width()) - fire.diameter()/2,
                                    this.getLocation().getY()
                                + rand.nextInt(height()) - fire.diameter()/2));
        fires.add(fire);
        fireAreaBudget -= fire.getArea();
        fire.start();
    }

    public double getTotalFireArea(){
        double totalFireArea = 0.0;
        for (Fire fire : fires.getGameObjects()) {
            if (fire.getState() instanceof Extinguished)
                totalFireArea += fire.getMaxSize();
            else
                totalFireArea += fire.getArea();
        }
        return totalFireArea;
    }

    public int getFireAreaBudget() { return fireAreaBudget; }

    public int width(){ return getDimension().getWidth(); }

    public int height(){ return getDimension().getHeight(); }

    public int getArea() { return area; }

    // damage is ratio of total fire area to building area
    //
    public int damage() { return (int)((getTotalFireArea()/area)*100); }

    public double financialLoss() {
        return damage()/100.0 * value;
    }

    public boolean isDestroyed() { return damage() >= 100; }

    @Override
    public void localDraw(Graphics g, Point parentOrigin, Point originScreen) {
        g.setColor(getColor());
        g.drawRect( parentOrigin.getX(),
                    parentOrigin.getY(),
                        width(), height(), 5);
        g.drawString("V:  " + value,
                parentOrigin.getX() + width(),
                    parentOrigin.getY());
        g.drawString("D: " + damage() + "%",
                parentOrigin.getX() + width(),
                parentOrigin.getY() + 30);
    }
}
