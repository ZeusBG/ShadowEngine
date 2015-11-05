/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.emitter;

import engine.Core;
import math.Vector2f;
import test.Bullet;

/**
 *
 * @author Zeus
 */
public class BulletEmitter {
    private Core core;
    private Vector2f position;

    private float startAngle;
    private float endAngle;
    public BulletEmitter(Core core,Vector2f position) {
        this.core = core;
        this.position = new Vector2f(position);
    }

    public void spawnParticles(int numOfParticles){
        for(int i=0;i<numOfParticles;i++){
            float angle = (float)(startAngle+Math.random()*(endAngle-startAngle));
            Vector2f dir = new Vector2f();
            dir.x = (float)Math.sin(angle);
            dir.y = (float)Math.cos(angle);
            float speed = (float)Math.random()*20+50;
            
            Bullet b = new Bullet(position.x,position.y);
            //b.setPosition(position);
            b.setDirection(dir);
            b.setOrientation(dir);
            b.setNextPosition(new Vector2f(position.x,position.y));
            b.setSpeed((int)speed);
            
            core.addObject(b);
        }
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setInterval(float startAngle,float endAngle){
        this.startAngle = (float)(startAngle*Math.PI/180);
        this.endAngle = (float)(endAngle*Math.PI/180);
    }
    
    public void setInterval(Vector2f dir,float spanAngle){
        startAngle = dir.getAngleInRadians()-(float)(spanAngle*Math.PI/360);
        endAngle = dir.getAngleInRadians()+(float)(spanAngle*Math.PI/360);
    }
    
    public float getEndAngle() {
        return endAngle;
    }
}
