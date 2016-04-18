/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.render;

import gameObjects.util.AABB;
import gameObjects.DynamicGameObject;
import java.awt.Color;
import math.Vector2f;

/**
 *
 * @author Zeus
 */
public class Light {
    private float radius= 180;
    private float power = 1;
    private Color color;
    private Vector2f location;
    private DynamicGameObject owner;
    private AABB aabb;
    private float spanAngle;
    private Vector2f direction;
    private Vector2f offset;
    
    
    public Light(float radius, Color color, DynamicGameObject owner) {
        this.radius = radius;
        this.color = color;
        this.owner = owner;
        aabb = new AABB();
        spanAngle = 360;
        direction = new Vector2f(0,0);
    }

    public Light(DynamicGameObject owner) {
        this.owner = owner;
        radius = 200;
        color = new Color(1.0f,1.0f,1.0f);
        aabb = new AABB();
        spanAngle = 360;
        direction = new Vector2f(0,0);
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
        if(owner==null){
            return location;
        }
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

    public float getSpanAngle() {
        return spanAngle;
    }

    public void setSpanAngle(float spanAngle) {
        this.spanAngle = spanAngle;
    }

    public Vector2f getDirection() {
        if(owner==null)
            return direction;
        direction.x = -owner.getDirection().x;
        direction.y = -owner.getDirection().y;
        return direction;
    }

    public void setDirection(Vector2f direction) {
        this.direction = direction;
    }


    
}
