/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects;

import math.Vector;
import utils.ObjectType;

/**
 *
 * @author Zeus
 */

/*This is the class for all the dynamic game objects - player, npc, etc.*/
public abstract class DynamicGameObject extends GameObject{
    
    protected Vector direction;
    protected int speed;
    
    public DynamicGameObject(int x, int y, ObjectType type) {
        super(x, y, type);
    }

 
    
}
