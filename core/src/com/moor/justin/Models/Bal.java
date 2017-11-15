package com.moor.justin.Models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Bal extends Sprite {

    // X en Y van de bal
    private float x;
    private float y;

    // Direction X en Y van de bal
    private float verX;
    private float verY;

    private boolean spawned;

    // Een texture wordt meegegeven aan de superklasse (Dus sprite)
    public Bal(Texture texture) {
        super(texture);
    }
    
    //Getters en setters
    public void setX(float x) {
        this.x = x;
    }

    public float getX() {
        return this.x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return this.y;
    }

    public float getverX() {
        return this.verX;
    }

    public void setVerX(float verX) {
        this.verX = verX;
    }

    public float getverY() {
        return this.verY;
    }

    public void setVerY(float verY) {
        this.verY = verY;
    }

    public void setSpawned(boolean spawned){
        this.spawned = spawned;
    }

    public boolean getSpawned(){
        return this.spawned;
    }

}






