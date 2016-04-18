/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects;


import gameObjects.util.ObjectType;

/**
 *
 * @author Zeus
 */
public abstract class Projectile extends DynamicGameObject{
    
    protected ExplodingGameObject explosive;
    protected boolean toBeRemoved;
    
    public Projectile(float x, float y) {
        super(x, y, ObjectType.PROJECTILE);
        explosive = null;
        toBeRemoved = false;
    }

    public ExplodingGameObject getExplosive() {
        return explosive;
    }

    public void setExplosive(ExplodingGameObject explosive) {
        this.explosive = explosive;
    }
    
    public void setToBeRemoved(boolean cond){
        toBeRemoved = cond;
    }
    
    @Override
    public boolean isDead(){
        return toBeRemoved;
    }
}
