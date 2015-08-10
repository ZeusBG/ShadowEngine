/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects;

import utils.ObjectType;

/**
 *
 * @author Zeus
 */
public abstract class LivingObject extends DynamicGameObject{

    public LivingObject(int x, int y, ObjectType type) {
        super(x, y, type);
    }

    
}
