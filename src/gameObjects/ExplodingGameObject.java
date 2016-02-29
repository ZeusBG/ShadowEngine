/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects;

import components.AABB;
import java.awt.Color;
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
    private AABB explosionRange;
        
    
    public ExplodingGameObject(float x, float y) {
        
        super(x, y, ObjectType.ITEM);
        timeSpawned = System.currentTimeMillis();
        damageDealtTo = new HashMap<>();
        light = new Light(this);
        Color c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
        light.setColor(c);
        light.setRadius(50);
        
        currentRadius = 0;
        exploded = false;
        geometry.setAabb(new AABB(x-1,y-1,x+1,y+1));
        explosionRange = new AABB(x-1,y-1,x+1,y+1);
    }
    
    public void explode(){
        exploded = true;
        currentRadius = 0;
        timeExploded = System.currentTimeMillis();
        light = new Light(this);
        light.setRadius(50);
        Color c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
        light.setColor(c);
        
    }
    
    public void update(float dt){
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
            light.setPower(light.getPower()+(float)(expansionSpeed/10000.0));
            
        }
        
        else{
            if(System.currentTimeMillis()-timeSpawned>1000*timer){
                explode();
            }
        }
    }
    
    public void render(Renderer r){
        if(exploded){
            r.setColor(Color.WHITE);
            r.drawCircle(position.x,position.y,currentRadius);
        }
        
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
        //AABB currentAABB = new AABB(x-currentRadius,y-currentRadius,x+currentRadius,y+currentRadius);
        explosionRange.reset(position.x-currentRadius,position.y-currentRadius,position.x+currentRadius,position.y+currentRadius);
        return explosionRange;
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
