/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects;

import engine.Core;
import engine.Renderer;
import utils.ObjectState;
import utils.ObjectType;

/**
 *
 * @author Zeus
 */
public abstract class GameObject {
    protected int x,y;
    protected ObjectType type;
    protected ObjectState ObjState;
    public GameObject(int x, int y,ObjectType type){
        this.type = type;
        this.x = x;
        this.y = y;
        ObjState = new ObjectState();
    }
    
    public void setType(ObjectType newType){
        type = newType;
    }
    
    public ObjectType getType(){
        return type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ObjectState getObjState() {
        return ObjState;
    }

    public void setObjState(ObjectState ObjState) {
        this.ObjState = ObjState;
    }
    
    
    
    public abstract void update(Core gc, float dt);
    public abstract void render(Core gc,Renderer r);
}
