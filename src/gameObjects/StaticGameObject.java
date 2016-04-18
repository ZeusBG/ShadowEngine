/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects;

import math.Line2f;
import math.Vector2f;
import gameObjects.util.ObjectType;

/**
 *
 * @author Zeus
 */

/*This is the class that all static object should extend - walls,lamps etc. */

public abstract class StaticGameObject extends GameObject{
    
    protected boolean visible;
    protected boolean collidable;
    
    public abstract void reactionOnHit(GameObject other, Vector2f hitPoint,Line2f lineHit);
    
    public StaticGameObject(float x, float y) {
        super(x, y, ObjectType.ENVIRONMENT);
        zIndex = -1000;
        
    }
   
    
}
