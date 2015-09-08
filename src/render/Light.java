/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import components.AABB;
import gameObjects.DynamicGameObject;
import java.awt.Color;

/**
 *
 * @author Zeus
 */
public class Light {
    float radius= 100;
    float[] dist = {0.001f,0.01f, 1.0f};
    Color[] colors = {new Color(255/255f, 255/255f, 128/255f, 0.5f), new Color(255/255f, 255/255f, 204/255f,0.2f),new Color(1.0f, 1.0f, 1.0f, 0.0f)};
    DynamicGameObject owner;
    AABB aabb;
    
    
    public Light(float radius, float[] dist, Color[] colors, DynamicGameObject owner) {
        this.radius = radius;
        this.dist = dist;
        this.colors = colors;
        this.owner = owner;
        aabb = new AABB();
    }

    public Light(DynamicGameObject owner) {
        this.owner = owner;
        aabb = new AABB();
    }

    public float getRadius() {
        return radius;
    }

    public float[] getDist() {
        return dist;
    }

    public Color[] getColors() {
        return colors;
    }

    public DynamicGameObject getOwner() {
        return owner;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setDist(float[] dist) {
        this.dist = dist;
    }

    public void setColors(Color[] colors) {
        this.colors = colors;
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
    
    


    
}
