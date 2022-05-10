package org.csc133.a3.gameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameObjects.parts.Rectangle;
import org.csc133.a3.gameObjects.parts.Text;

import java.util.Random;

public class Building extends Fixed {
    private int value;
    final private int MAX_FIRE_DIAMETER = 26;
    final private int MIN_FIRE_DIAMETER = MAX_FIRE_DIAMETER-5;
    private int fireAreaBudget;
    private Fires fires;
    final private int area;
    final private Rectangle bRect;
    final private Text valueText;
    final private Text damageText;

    public Building(Point position, Dimension dimension, Dimension worldSize) {
        setWorldSize(worldSize);
        fires = new Fires();
        position = getLocation();
        setLocation(position);
        setDimension(dimension);
        bRect = new Rectangle(ColorUtil.rgb(255, 0, 0),
                                width(), height(),
                                position.getX(), position.getY(),
                                1, 1, 0, 5);
        value = (width() % 10) * 100;
        area = dimension.getHeight()*dimension.getWidth();
        fireAreaBudget = 1000;
        valueText = new Text(ColorUtil.rgb(255, 0 , 0),
                position.getX() + (float)(width()/2) + 20,
                position.getY() + (float)(height()/2),
                1, -1, "V:  " + value);
        damageText = new Text(ColorUtil.rgb(255, 0 , 0),
                position.getX() + (float)(width()/2) + 20,
                position.getY() + (float)(height()/2) - 35,
                1, -1, "D: " + damage() + "%");
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
        bRect.draw(g, parentOrigin, originScreen);
        valueText.draw(g, parentOrigin, originScreen);
        valueText.updateText("V:  " + value);
        damageText.draw(g, parentOrigin, originScreen);
        damageText.updateText("D: " + damage() + "%");
    }
}
