/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects;

import java.awt.geom.Point2D;
import math.Vector;
import utils.ObjectType;

/**
 *
 * @author Zeus
 */

/*This is the class for all the dynamic game objects - player, npc, etc.*/
public abstract class DynamicGameObject extends GameObject{
    protected Vector orientation;
    protected Vector direction;
    protected int speed;
    protected Point2D.Double currentPosition;
    protected Point2D.Double nextPosition;
    
    public DynamicGameObject(int x, int y, ObjectType type) {
        super(x, y, type);
        currentPosition = new Point2D.Double();
        nextPosition = new Point2D.Double();
        direction = new Vector(0,0);
        orientation = new Vector(1,0);
    }
    public DynamicGameObject(int x, int y, ObjectType type,Vector _direction,int _speed) {
        super(x, y, type);
        direction = new Vector(_direction);
        speed = _speed;
    }
    
    public void setCurrentPosition(Point2D.Double p){
        currentPosition = new Point2D.Double();
        currentPosition.x = p.x;
        currentPosition.y = p.y;
    }

    public Point2D.Double getCurrentPosition() {
        return currentPosition;
    }

    public Vector getOrientation() {
        return orientation;
    }

    public void setOrientation(Vector orientation) {
        this.orientation = orientation;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    public Point2D.Double getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(Point2D.Double p) {
        nextPosition = new Point2D.Double();
        nextPosition.x = p.x;
        nextPosition.y = p.y;
    }
    
    //this moves the object using the vector 
    //based on the delta time and the speed of the object
    public void move(float dt){
        currentPosition.x = nextPosition.x;
        currentPosition.y = nextPosition.y;
        
        nextPosition.x += dt*speed*direction.x;
        nextPosition.y += dt*speed*direction.y;
    }
    
    //instantly moves the object
    public void moveToNextPoint(){
        currentPosition.x = nextPosition.x;
        currentPosition.y = nextPosition.y;
    }
    
    public void setDirection(Point2D.Double p){
        direction = new Vector(currentPosition,p);
        direction.normalize();
        
    }
    public void setDirection(Vector dir){
        direction = new Vector(dir);
        direction.normalize();
        
    }
    
    public Vector getDirection(){
        return direction;
    }
}
