package org.csc133.a3.gameObjects;

import java.util.ArrayList;

public abstract class GameObjectCollection<T> extends GameObject {
    ArrayList<T> gameObjects;

    public GameObjectCollection(){
        gameObjects = new ArrayList<>();
    }

    public ArrayList<T> getGameObjects(){
        return gameObjects;
    }

    public void add(T gameObject){
        gameObjects.add(gameObject);
    }

    public void remove(T gameObject){
        gameObjects.remove(gameObject);
    }

    public int size(){
        return gameObjects.size();
    }
}
