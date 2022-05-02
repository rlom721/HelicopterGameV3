package org.csc133.a3.gameObjects;

public abstract class Movable extends GameObject{
    private int speed;
    private int heading;

    public int speed() { return speed; }

    public int heading() { return heading; }

    public void setSpeed(int speed) { this.speed = speed; }

    public void setHeading(int heading) { this.heading = heading; }

    public abstract void move();
}
