/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import components.emitter.BulletEmitter;
import gameObjects.GameObject;
import render.Renderer;
import gameObjects.StaticGameObject;
import java.awt.Color;
import java.util.ArrayList;
import math.Line2f;
import math.Vector2f;

/**
 *
 * @author Zeus
 */
public class StaticObject extends StaticGameObject {
    public static int firstTime = 0;
    public StaticObject(float x, float y) {
        super(x, y);

    }

    @Override
    public void update(float dt) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //geometry.rotate(1.1f);
    }

    @Override
    public void render(Renderer r) {
        
        ArrayList<Vector2f> points = geometry.getPoints();
        
        r.setColor(Color.BLUE);
        for (int i = 0; i < points.size() - 1; i++) {
            r.drawLine((int) points.get(i).x, (int) points.get(i).y, (int) points.get(i + 1).x, (int) points.get(i + 1).y);
        }
        
        r.setColor(Color.WHITE);
    }

    @Override
    public void reactionOnHit(GameObject other, Vector2f hitPoint, Line2f lineHit) {
        Vector2f v = new Vector2f(hitPoint);
        v.add(lineHit.asVector().getPerpendicularCloserTo(other.getPosition(), hitPoint).normalize());
        
        BulletEmitter apocalipse = new BulletEmitter(core, v);
        apocalipse.setInterval(lineHit.asVector().getPerpendicularCloserTo(other.getPosition(), hitPoint), 160);
        //if(firstTime<2)
            //apocalipse.spawnParticles(1000);
        //firstTime++;
        if (emitter != null) {
            
            emitter.setPosition(hitPoint);
            emitter.setNextPosition(hitPoint);
            emitter.setInterval(lineHit.getNormal(), 160);
            emitter.spawnParticles(10);
            
        }
        
    }

}
