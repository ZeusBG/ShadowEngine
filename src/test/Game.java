/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import engine.AbstractGame;
import engine.Core;
import engine.Renderer;

/**
 *
 * @author Zeus
 */
public class Game extends AbstractGame{
    

    
    public static void main(String[] args) {
        
        Core core = new Core(new Game());
        core.init();
        Player p = new Player();
 
        
        
        core.start();
        core.addObject(p);
    }

    @Override
    public void update(Core gc, float dt) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render(Core gc, Renderer r) {
        r.render();
    }

    @Override
    public void init(Core gc) {
        
    }
}
