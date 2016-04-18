/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects.util;

/**
 *
 * @author Zeus
 */
public class Renderable {
    public boolean renderable;

    public Renderable(boolean renderable) {
        this.renderable = renderable;
    }

    public boolean isRenderable() {
        return renderable;
    }

    public void setRenderable(boolean renderable) {
        this.renderable = renderable;
    }
    
    
}
