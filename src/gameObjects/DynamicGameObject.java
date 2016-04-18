/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects;

import math.Vector2f;
import gameObjects.util.ObjectType;

/**
 *
 * @author Zeus
 */

/*This is the class for all the dynamic game objects - player, npc, etc.*/
public abstract class DynamicGameObject extends GameObject{
    
    protected Vector2f direction;
    protected float speed;
    protected Vector2f nextPosition;
    
    public DynamicGameObject(float x, float y, ObjectType type) {
        super(x, y, type);
        nextPosition = new Vector2f();
        direction = new Vector2f(0,0);
        orientation = new Vector2f(1,0);
        zIndex = 1000;
    }
    public DynamicGameObject(int x, int y, ObjectType type,Vector2f _direction,int _speed) {
        super(x, y, type);
        direction = new Vector2f(_direction);
        speed = _speed;
    }
  

    public Vector2f getOrientation() {
        return orientation;
    }

    public void setOrientation(Vector2f orientation) {
        this.orientation = orientation;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    public Vector2f getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(Vector2f p) {
        nextPosition = new Vector2f(p);
    }
    
    //this moves the object using the vector 
    //based on the delta time and the speed of the object
    public void move(float dt){
        position.x = nextPosition.x;
        position.y = nextPosition.y;
        
        nextPosition.x += dt*speed*direction.x;
        nextPosition.y += dt*speed*direction.y;
    }
    
    //instantly moves the object
    public void moveToNextPoint(){
        position.x = nextPosition.x;
        position.y = nextPosition.y;
    }
    
    public void setDirectionTo(Vector2f p){
        direction = new Vector2f(p.x-position.x,p.y-position.y);
        direction.normalize();
        
    }
    public void setDirection(Vector2f dir){
        direction = new Vector2f(dir);
        direction.normalize();
        
    }
    
    public Vector2f getDirection(){
        return direction;
    }
    
    
}
