/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import engine.AbstractGame;
import engine.Core;
import java.util.Stack;
import engine.render.Renderer;

/**
 *
 * @author Zeus
 */
public class MainMenu extends AbstractGame{
    private Stack<MenuContainer> currentMenu;
    
    
    public MainMenu(){
        currentMenu = new Stack();
    }

    @Override
    public void update( float dt) {
        
    }

    @Override
    public void render(Renderer r) {
        currentMenu.lastElement().render(r);
    }

    @Override
    public void init(Core gc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
