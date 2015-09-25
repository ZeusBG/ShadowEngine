/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import components.AABB;
import gameObjects.DynamicGameObject;
import java.awt.Color;
import java.awt.geom.Point2D;

/**
 *
 * @author Zeus
 */
public class Light {
    public float radius= 300;
    public float power = 1;
    public Color color;
    public Point2D.Double location;
    public DynamicGameObject owner;
    public AABB aabb;
    
    
    public Light(float radius, Color color, DynamicGameObject owner) {
        this.radius = radius;
        this.color = color;
        this.owner = owner;
        aabb = new AABB();
    }

    public Light(DynamicGameObject owner) {
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
        aabb.setMinX(owner.getCurrentPosition().x-radius);
        aabb.setMaxX(owner.getCurrentPosition().x+radius);
        aabb.setMinY(owner.getCurrentPosition().y-radius);
        aabb.setMaxY(owner.getCurrentPosition().y+radius);
        return aabb;
    }
    
    public Point2D.Double getLocation(){
        
        Point2D.Double loc = new Point2D.Double();
        loc.x = owner.getCurrentPosition().x;
        loc.y = owner.getCurrentPosition().y;
        return loc;
        
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }


    
}
