/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import gameObjects.ExplodingGameObject;

/**
 *
 * @author Zeus
 */
public class Explosion1 extends ExplodingGameObject{

    public Explosion1(float x, float y) {
        super(x, y);
        expansionSpeed = 150;
        damage = 200;
        lifeTime = 1;
        timer = 1;
        maxRadius = 600;
    }
    
    public Explosion1() {
        super(-10000,-10000);
        expansionSpeed = 150;
        damage = 200;
        lifeTime = 1;
        timer = 1;
        maxRadius = 600;
        
        
    }
    
    
    
    
    
}
