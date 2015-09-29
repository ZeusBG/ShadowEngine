/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;

/**
 *
 * @author Zeus
 */
public class ObjectState {
    Collidable objectsItCollides;
    Renderable renderable;
    private boolean hasMoved;
    private boolean hasShot;
    public ObjectState(){
        hasMoved = false;
        hasShot = false;
        objectsItCollides = new Collidable();
        objectsItCollides.addDefaultCollision();
        renderable = new Renderable(true);
    }
    
    public boolean isRenderable(){
        return renderable.isRenderable();
    }
    
    public boolean isCollidableWith(ObjectState state){
        ArrayList<Integer> stateCollisionTypes = state.getObjectsItCollides().getCollideTypes();
        for(int i=0;i<stateCollisionTypes.size();++i){
            if(objectsItCollides.collidesWith(stateCollisionTypes.get(i))){
                return true;
            }
        }
        return false;
        
    }

    public Collidable getObjectsItCollides() {
        return objectsItCollides;
    }

    public Renderable getRenderable() {
        return renderable;
    }
    public void addCollidableWithType(String type){
        objectsItCollides.addCollidableWithType(type);
    }
    
    public void addCollidableType(String type){
        objectsItCollides.addCollideType(type);
    }
    
    public void removeCollidableWithType(String type){
        objectsItCollides.removeCollidableWithType(type);
    }
    
    public void removeCollidableType(String type){
        objectsItCollides.removeCollideType(type);
    }
    
    public void setRenderable(boolean render){
        renderable.setRenderable(render);
    }
    
}
