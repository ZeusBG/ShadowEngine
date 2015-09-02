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
public abstract class Projectile extends DynamicGameObject{
    
    

    public Projectile(int x, int y) {
        super(x, y, ObjectType.PROJECTILE);
        this.speed = speed;
    }


    
    
    
}
