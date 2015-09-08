/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects;

import components.AABB;
import engine.Core;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.HashMap;
import render.Light;
import render.Renderer;
import utils.ObjectType;

/**
 *
 * @author Zeus
 */
public abstract class ExplodingGameObject extends DynamicGameObject{
    
    
    protected int expansionSpeed;
    protected int damage;
    protected double lifeTime;
    protected double timer;
    protected int maxRadius;
    private long timeExploded;
    private long timeSpawned;
    protected boolean exploded;
    protected int currentRadius;
    private int previousRadius;
    private HashMap<GameObject,Boolean> damageDealtTo;
    
    public ExplodingGameObject(int x, int y) {
        
        super(x, y, ObjectType.ITEM);
        timeSpawned = System.currentTimeMillis();
        damageDealtTo = new HashMap<>();
        light = new Light(this);
        
        
        
        currentPosition = new Point2D.Double(x,y);
        currentRadius = 0;
        exploded = false;
        aabb = new AABB(x-1,y-1,x+1,y+1);
    }
    
    public void explode(){
        exploded = true;
        currentRadius = 0;
        timeExploded = System.currentTimeMillis();
        light = new Light(this);
        Color[] colors = {new Color(255/255f, 0/255f, 0/255f, 0.9f), 
                            new Color(153/255f, 204/255f, 255/255f,0.2f),
                            new Color(1.0f, 1.0f, 1.0f, 0.0f)};
        
        light.setColors(colors);
        
    }
    
    public void update(Core core,float dt){
        if(exploded){
            damageDealtTo = new HashMap<>();
            if(System.currentTimeMillis()-timeExploded>lifeTime*1000){
                dispose();
                light=null;
                return;
            }
            previousRadius = currentRadius;
            currentRadius +=(expansionSpeed*dt);
            light.setRadius(currentRadius*5);
            
        }
        
        else{
            if(System.currentTimeMillis()-timeSpawned>1000*timer){
                explode();
            }
        }
    }
    
    public void render(Core core,Renderer r){
        if(exploded)
            r.drawCircle((int)currentPosition.x,(int)currentPosition.y,currentRadius);
        
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }

    public int getCurrentRadius() {
        return currentRadius;
    }

    public void setCurrentRadius(int currentRadius) {
        this.currentRadius = currentRadius;
    }
    
    public AABB getCurrentAABB(){
        AABB currentAABB = new AABB(x-currentRadius,y-currentRadius,x+currentRadius,y+currentRadius);
        return currentAABB;
    }

    public int getPreviousRadius() {
        return previousRadius;
    }

    public HashMap<GameObject, Boolean> getDamageDealtTo() {
        return damageDealtTo;
    }

    public boolean isExploded() {
        return exploded;
    }
    
}
