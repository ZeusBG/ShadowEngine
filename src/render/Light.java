/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import gameObjects.DynamicGameObject;
import java.awt.Color;

/**
 *
 * @author Zeus
 */
public class Light {
    float radius= 30;
    float[] dist = {0.0001f, 1.0f};
    Color[] colors = {new Color(255/255f, 255/255f, 128/255f, 1.0f), new Color(1.0f, 1.0f, 1.0f, 0.0f)};
    DynamicGameObject owner;
    
    
    
    //drawMAP
    //drawLights
    //setClipLights
    //intersect clip with visibilityPolygon
    //draw all objects
    
    public Light(float radius, float[] dist, Color[] colors, DynamicGameObject owner) {
        this.radius = radius;
        this.dist = dist;
        this.colors = colors;
        this.owner = owner;
    }

    public Light(DynamicGameObject owner) {
        this.owner = owner;
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


    
}
