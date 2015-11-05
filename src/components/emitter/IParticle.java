/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.emitter;

import engine.Core;
import render.Renderer;

/**
 *
 * @author Zeus
 */
public interface IParticle {
    
    public void update(Core core, float dt);
    public void render(Core core, Renderer r);
    public boolean isDead();
}
