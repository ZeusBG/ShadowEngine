/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import engine.Core;
import engine.Renderer;
import gameObjects.LivingObject;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import utils.ObjectType;

/**
 *
 * @author Zeus
 */
public class Player extends LivingObject{

    private Point crosshair;
    
    public Player(int x, int y, ObjectType type) {
        super(x, y, type);
    }
    
    public Player(){
        super(50,50,ObjectType.PLAYER);
        crosshair = new Point();
    }
    
    @Override
    public void update(Core core, float dt) {
        
        crosshair.x = core.getInput().getMouseX();
        crosshair.y = core.getInput().getMouseY();
        
        if(core.getInput().isKeyPressed(KeyEvent.VK_W)){
            
            this.setY(this.getY()-2);
        }
        if(core.getInput().isKeyPressed(KeyEvent.VK_S)){
            this.setY(this.getY()+2);
        }
        if(core.getInput().isKeyPressed(KeyEvent.VK_A)){
            this.setX(this.getX()-2);
        }
        if(core.getInput().isKeyPressed(KeyEvent.VK_D)){
            this.setX(this.getX()+2);
        }
        
        
    }

    @Override
    public void render(Core gc, Renderer r) {
        r.drawCircle(x,y,20);
        r.requestLine(x, y,crosshair.x, crosshair.y);
        //r.drawLine(x, y,crosshair.x, crosshair.y);
    }
    
}
