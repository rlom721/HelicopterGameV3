package org.csc133.a3;

import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameObjects.*;
import java.util.ArrayList;
import java.util.Random;

// ----------------------------------------------------------------------------
// Holds state of game, determines win/lose conditions and links Game objects.
//
public class GameWorld{
    private static GameWorld gameWorld;
    private Dimension worldSize;
    private River river;
    private Helipad helipad;
    private static Helicopter helicopter;
//    private static Point helipadLocation;
    private ArrayList<GameObject> go;
    private int numberOfFires;
    public static final int INIT_FUEL = 25000;
    private enum Result {LOST, WON}

    private GameWorld() { }

    public static GameWorld getInstance(){
        if (gameWorld == null)
            gameWorld = new GameWorld();
        return gameWorld;
    }

    public void init(){
        numberOfFires = 0;
        river = new River(worldSize);
        helipad = new Helipad(worldSize);
        helicopter = new Helicopter(helipad.getCenter(), INIT_FUEL, worldSize);
//        helipadLocation = helipad.getLocation();
        go = new ArrayList<>();

        go.add(river);
        go.add(helipad);
        go.add(addBuildingAboveRiver());
        go.add(addBuildingBelowLeftRiver());
        go.add(addBuildingBelowRightRiver());
        placeFiresInBuilding();
        go.add(helicopter);
    }

    public void tick(){
        helicopter.move();
        helicopter.reduceFuel();
        randomlyGrowFires();
        endGame();
    }

    public void accelerate() {
        helicopter.increaseSpeed();
    }

    public void brake() {
        helicopter.decreaseSpeed();
    }

    public void drink() {
        if(helicopter.isAboveRiver(river))
            helicopter.drink();
    }

    public void fight() {
        fightFiresIfHeliIsNear();
    }

    public void turnLeft() {
        helicopter.steerLeft();
    }

    public void turnRight() {
        helicopter.steerRight();
    }

    public void exit(){ Display.getInstance().exitApplication(); }

//    public static Point getHelipadLocation(){ return helipadLocation; }

    private void endGame() {
        if(helicopter.fuel() <= 0)
            gameOver(Result.LOST);
        else if( helicopter.hasLandedOnHelipad(helipad)
                 && allFiresAreOut()
                 && helicopter.speed() == 0)
            gameOver(Result.WON);
    }

    private void gameOver(Result result){
        boolean replayGame = Dialog.show("Game Over", replayPrompt(result),
                "Heck Yeah!", "Some Other Time");

        if(replayGame)
            init();
        else
            exit();
    }

    private String replayPrompt(Result result) {
        String dialogMsg = "";

        if(result == Result.LOST && helicopter.fuel() <= 0)
            dialogMsg = "You ran out of fuel :(\nPlay Again?";
        else if (result == Result.LOST && allBuildingsDestroyed())
            dialogMsg = "All buildings were destroyed :(\nPlay Again?";
        else if(result == Result.WON)
            dialogMsg = "You won!" + "\nScore: " + score() + "\nPlay Again?";

        return dialogMsg;
    }

    public void setDimension(Dimension worldSize) {
        this.worldSize = worldSize;
    }

    public Dimension getDimension() {
        return worldSize;
    }

    public String getHeading() {
        return Integer.toString(helicopter.heading());
    }

    public String getSpeed() {
        return Integer.toString(helicopter.speed());
    }

    public String getFuel() {
        return Integer.toString(helicopter.fuel());
    }

    public String getNumberOfFires() {
        return Integer.toString(numberOfFires);
    }

    public String getTotalFireSize() {
        return Integer.toString(totalFireSize());
    }

    public String getFinancialLoss() {
        return Integer.toString(totalFinancialLoss());
    }

    public String getTotalDamage() {
        return percentDamageOfBuildings() + "%";
    }

    public ArrayList<GameObject> getGameObjectCollection() {
        return go;
    }

    private int score() { return 100 - percentDamageOfBuildings(); }

    private void randomlyGrowFires() {
        Random rand = new Random();
        for(GameObject go : getGameObjectCollection()) {
            if (go instanceof Fire) {
                if (rand.nextInt(20) == 0)
                    ((Fire)go).grow();
            }
        }
    }

    private void placeFiresInBuilding(){
        ArrayList<Fire> tempFires = new ArrayList<>();
        for (GameObject go : getGameObjectCollection()){
            if (go instanceof Building){
                Building currentBuilding = (Building)go;
                while (currentBuilding.getFireAreaBudget() > 0) {
                    Fire fire = new Fire(currentBuilding, worldSize);
                    tempFires.add(fire);
                    numberOfFires += 1;
                }
            }
        }

        for (Fire fire : tempFires) {
            getGameObjectCollection().add(fire);
        }
    }

    private int totalFireSize(){
        int sizeFires = 0;
        for (GameObject go : getGameObjectCollection()){
            if (go instanceof Fire)
                sizeFires += ((Fire)go).diameter();
        }
        return sizeFires;
    }

    private int totalFinancialLoss() {
        double financialLoss = 0.0;
        for (GameObject go : getGameObjectCollection()) {
            if (go instanceof Building) {
                Building currentBuilding = (Building)go;
                financialLoss += currentBuilding.financialLoss();
            }
        }
        return (int)financialLoss;
    }

    private int percentDamageOfBuildings() {
        double totalDamage = 0.0;
        double totalBuildingArea = 0.0;
        for (GameObject go : getGameObjectCollection()){
            if (go instanceof Building) {
                totalDamage += ((Building)go).getTotalFireArea();
                totalBuildingArea += ((Building)go).getArea();
            }
        }
        return (int)((totalDamage/totalBuildingArea)*100);
    }

    private void fightFiresIfHeliIsNear() {
        ArrayList<Fire> deadFires = new ArrayList<>();
        for(GameObject go : getGameObjectCollection()) {
            if (go instanceof Fire) {
                Fire fire = (Fire) go;
                if (helicopter.isWithinRangeOfFire(fire))
                    helicopter.fight(fire);
                if (fire.diameter() == 0) {
                    fire.extinguish();
                    deadFires.add(fire);
                    numberOfFires -= 1;
                }
            }
        }
        helicopter.dumpWater();
        go.removeAll(deadFires);
    }

    private boolean allFiresAreOut(){
        for (GameObject go : getGameObjectCollection()){
            if (go instanceof Fire)
                return false;
        }
        return true;
    }

    private boolean allBuildingsDestroyed() {
        for (GameObject go : getGameObjectCollection()){
            if (go instanceof Building) {
                Building currentBuilding = (Building) go;
                if (!currentBuilding.isDestroyed()) return false;
            }
        }
        return true;
    }

    private Building addBuildingAboveRiver(){
        Dimension dimension = new Dimension((int)(worldSize.getWidth()/1.5),
                                            worldSize.getHeight()/10);
        Point worldPosition = new Point((int)(0.75*dimension.getWidth()),
                river.getLocation().getY() + dimension.getHeight()*2);
        return new Building(worldPosition, dimension, worldSize);
    }

    private Building addBuildingBelowLeftRiver(){
        Point worldPosition = new Point(  worldSize.getWidth()/8,
                                          worldSize.getHeight()/3);
        Dimension dimension = new Dimension(worldSize.getWidth()/9,
                                            worldSize.getHeight()/3);
        return new Building(worldPosition, dimension, worldSize);
    }

    private Building addBuildingBelowRightRiver(){
        Point worldPosition = new Point( (int)(0.85*worldSize.getWidth()),
                                            worldSize.getHeight()/3);
        Dimension dimension = new Dimension(worldSize.getWidth()/10,
                                            worldSize.getHeight()/3);
        return new Building(worldPosition, dimension, worldSize);
    }
}