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
public abstract class PlayerObject extends LivingObject {
    
    
    
    public PlayerObject(int x, int y) {
        super(ObjectType.PLAYER);
    }


    
}
