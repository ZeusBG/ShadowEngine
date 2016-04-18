/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.emitter;

import gameObjects.util.AABB;
import engine.Core;
import gameObjects.DynamicGameObject;
import java.util.ArrayList;
import math.Vector2f;
import engine.render.Renderer;
import gameObjects.util.ObjectType;

/**
 *
 * @author Zeus
 */
public class Emitter extends DynamicGameObject implements IParticle {
    private float lifeTime;
    private Vector2f nextPosition;
    private Vector2f direction;
    private Vector2f acceleration;
    private float speed;
    private long timeSpawned;
    private ArrayList<IParticle> particles;
    private float startAngle;
    private float endAngle;
    
    public Emitter(Vector2f position, Vector2f direction, float speed,float lifeTime) {
        super(position.x,position.y,ObjectType.ITEM);
        this.position = position;
        this.direction = direction;
        this.speed = speed;
        this.lifeTime = lifeTime;
        timeSpawned = core.getCurrentTimeMillis();
        particles = new ArrayList<>();
        AABB aabb = new AABB();
        aabb.reset(position.x, position.y, position.x, position.y);
        geometry.setAabb(aabb);
        
    }
    
    public Emitter(){
        super(0,0,ObjectType.ITEM);
        position = new Vector2f();
        direction = new Vector2f();
        direction = new Vector2f();
        particles = new ArrayList();
        lifeTime = 0.1f;
        speed = 0;
        geometry.setAabb(new AABB());
    }
    
    @Override
    public void update(float dt) {
        
        for(int i=0;i<particles.size();i++){
            if(particles.get(i).isDead())
                particles.remove(i);
        }
        
        if(!isDead()){
            position.x = position.x+direction.x*speed*dt;
            position.y = position.y+direction.y*speed*dt;
            geometry.getAabb().reset(position.x, position.y, position.x, position.y);
            for(int i=0;i<particles.size();i++){
                particles.get(i).update(core, dt);
            }
        }
        
    }

    @Override
    public void render(Renderer r) {
        if(!isDead()){
            //r.drawCircle(position.x, position.y, 5);
            for(int i=0;i<particles.size();i++){
                particles.get(i).render(core,r);
            }
        }
       
    }
    
    public boolean isDead(){
        //return particles.isEmpty();
        return false;
    }
    
    
    
    @Override
    public void update(Core core, float dt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render(Core core, Renderer r) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void spawnParticles(int numOfParticles){
        for(int i=0;i<numOfParticles;i++){
            float angle = (float)(startAngle+Math.random()*(endAngle-startAngle));
            Vector2f dir = new Vector2f();
            dir.x = (float)Math.sin(angle);
            dir.y = (float)Math.cos(angle);
            dir.normalize();
            float speed = (float)Math.random()*100+150;
            float lifeSpan = (float)Math.random()*0.1f+0.05f;
            Particle p = new Particle(new Vector2f(position),dir,speed,lifeSpan);
            particles.add(p);
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
