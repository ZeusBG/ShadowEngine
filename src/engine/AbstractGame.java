/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import render.Renderer;
import render.TextureHolder;

/**
 *
 * @author Zeus
 */
public abstract class AbstractGame {
    
    protected TextureHolder textures;
    public AbstractGame(){
        textures = new TextureHolder();
    }
    
    public abstract void update(Core gc, float dt);
    public abstract void render(Core gc, Renderer r);
    public abstract void init(Core gc);
    
}
