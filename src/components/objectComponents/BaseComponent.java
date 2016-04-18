/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.objectComponents;

import engine.render.Renderer;
import gameObjects.GameObject;

/**
 *
 * @author Nick
 */
public abstract class BaseComponent {
    private static int IDGEN;
    private GameObject obj;
    private int id;
    
    public BaseComponent(GameObject obj){
        this.obj = obj;
        id = IDGEN++;
    }

    public GameObject getObj() {
        return obj;
    }

    public int getId() {
        return id;
    }
    
    public abstract void update(float delta);
    
    public abstract void render(Renderer r);
}
