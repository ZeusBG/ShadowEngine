/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import engine.Core;
import gameObjects.DynamicGameObject;

/**
 *
 * @author Zeus
 */
public class Physics {
    private Core core;
    //private ObjectManager OM;
    
    public Physics (Core core){
        this.core = core;
    }
    
    public void moveObjects(float dt){
        core.getObjectManager().getPlayer().update(core, dt);
        
        for(DynamicGameObject obj : core.getObjectManager().getDynamicObjects()){
            obj.update(core, dt);
            //System.out.printf("Moved obj of type %s to (%d,%d)",obj.getType(),obj.getX(),obj.getY());
        }
    }
    
    public void checkHits(){
        //shte chekva hitove na projectile-i sus objecti
    }
    
    public void collisionObjects(){
        //shte chekva collision mejdu objekti moje i da ne trqbva
    }
    
    public void update(float dt){
        checkHits();
        collisionObjects();
        moveObjects(dt);
    }
    
}
