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
import math.Vector;

/**
 *
 * @author Zeus
 */
public class Light {
    private float radius= 180;
    private float power = 1;
    private Color color;
    private Point2D.Float location;
    private DynamicGameObject owner;
    private AABB aabb;
    private float spanAngle;
    private Vector direction;
    private Vector offset;
    
    
    public Light(float radius, Color color, DynamicGameObject owner) {
        this.radius = radius;
        this.color = color;
        this.owner = owner;
        aabb = new AABB();
        spanAngle = 360;
        direction = new Vector(0,0);
    }

    public Light(DynamicGameObject owner) {
        this.owner = owner;
        radius = 200;
        color = new Color(1.0f,1.0f,1.0f);
        aabb = new AABB();
        spanAngle = 360;
        direction = new Vector(0,0);
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
        aabb.reset(owner.getCurrentPosition().x-radius,
                owner.getCurrentPosition().y-radius,
                owner.getCurrentPosition().x+radius,
                owner.getCurrentPosition().y+radius);
        return aabb;
    }
    
    public Point2D.Float getLocation(){
        if(owner==null){
            return location;
        }
        Point2D.Float loc = new Point2D.Float();
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

    public float getSpanAngle() {
        return spanAngle;
    }

    public void setSpanAngle(float spanAngle) {
        this.spanAngle = spanAngle;
    }

    public Vector getDirection() {
        if(owner==null)
            return direction;
        direction.x = -owner.getDirection().x;
        direction.y = -owner.getDirection().y;
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }


    
}
