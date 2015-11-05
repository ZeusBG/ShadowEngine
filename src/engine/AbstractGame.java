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
    protected Core core;
    protected TextureHolder textures;
    public AbstractGame(){
        textures = new TextureHolder();
        core = null;
    }
    
    public abstract void update(float dt);
    public abstract void render(Renderer r);
    public abstract void init(Core core);
    
}
