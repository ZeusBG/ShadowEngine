/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.render;

import components.AABB;
import gameObjects.DynamicGameObject;
import java.awt.Color;
import math.Vector2f;

/**
 *
 * @author Zeus
 */
public abstract class AbstractLight {
    public float radius= 180;
    public float power = 1;
    public Color color;
    public Vector2f location;
    public DynamicGameObject owner;
    public AABB aabb;
    
    
    public AbstractLight(float radius, Color color, DynamicGameObject owner) {
        this.radius = radius;
        this.color = color;
        this.owner = owner;
        aabb = new AABB();
    }

    public AbstractLight(DynamicGameObject owner) {
        this.owner = owner;
        color = new Color(1.0f,1.0f,1.0f);
        aabb = new AABB();
    }

    public float getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    public DynamicGameObject getOwner() {
        return owner;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setOwner(DynamicGameObject owner) {
        this.owner = owner;
    }
    
    public AABB getAABB(){
        aabb.reset(owner.getPosition().x-radius,
                owner.getPosition().y-radius,
                owner.getPosition().x+radius,
                owner.getPosition().y+radius);
        return aabb;
    }
    
    public Vector2f getLocation(){
        
        Vector2f loc = new Vector2f();
        loc.x = owner.getPosition().x;
        loc.y = owner.getPosition().y;
        return loc;
        
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }
}
