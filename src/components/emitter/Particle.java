/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.emitter;

import engine.Core;
import java.awt.Color;
import math.Vector2f;
import engine.render.Renderer;

/**
 *
 * @author Zeus
 */
public class Particle implements IParticle{
    
    private Vector2f position;
    private Vector2f startPosition;
    private float lifeTime;
    private Vector2f nextPosition;
    private Vector2f direction;
    private Vector2f acceleration;
    private float speed;
    private long timeSpawned;
    

    public Particle(Vector2f position, Vector2f direction, float speed,float lifeTime) {
        this.position = position;
        this.direction = direction;
        this.speed = speed;
        this.lifeTime = lifeTime;
        startPosition = new Vector2f(position);
        timeSpawned = System.currentTimeMillis();
    }
    
    
    
    @Override
    public void update(Core core, float dt) {
        if(!isDead()){
            position.x = position.x+direction.x*speed*dt;
            position.y = position.y+direction.y*speed*dt;
        }
    }

    @Override
    public void render(Core core, Renderer r) {
        if(!isDead()){
            r.setColor(Color.white);
            r.drawLine(startPosition.x,startPosition.y,position.x,position.y);
            //r.drawCircle(position.x, position.y,1);
       
        }
    }
    
    public boolean isDead(){
        return (System.currentTimeMillis()-timeSpawned-lifeTime*1000)>0;
    }
        
    
}
