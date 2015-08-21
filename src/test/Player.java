/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import engine.Core;
import engine.Renderer;
import gameObjects.LivingObject;
import gameObjects.Projectile;
import gameObjects.Weapon;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import utils.ObjectType;
import utils.Sounds;

/**
 *
 * @author Zeus
 */
public class Player extends LivingObject{

    private Point crosshair;
    private Weapon weapon;
    
    public Player(int x, int y, ObjectType type) {
        super(x, y, type);
        speed = 500;
    }
    
    public Player(){
        super(50,50,ObjectType.PLAYER);
        crosshair = new Point();
        weapon = null;
    }
    
    @Override
    public void update(Core core, float dt) {
        crosshair.x = core.getInput().getMouseX();
        crosshair.y = core.getInput().getMouseY();
        
        orientation.x = crosshair.x - currentPosition.x;
        orientation.y = crosshair.y - currentPosition.y;
        orientation.normalize();
        
        if(core.getInput().isMouseButtonPressed(MouseEvent.BUTTON1)){
            Projectile p = weapon.fire();
            if(p!=null){
                core.addObject(p);
                core.getSoundManager().play(Sounds.AK47_SHOT);
            }
        }
        
        if(core.getInput().isKeyPressed(KeyEvent.VK_W)){
            nextPosition.y = currentPosition.y-2;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        if(core.getInput().isKeyPressed(KeyEvent.VK_S)){
            nextPosition.y = currentPosition.y+2;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        if(core.getInput().isKeyPressed(KeyEvent.VK_A)){
            nextPosition.x = currentPosition.x-2;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        if(core.getInput().isKeyPressed(KeyEvent.VK_D)){
            nextPosition.x = currentPosition.x+2;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        
        
        
        if(core.getInput().isKeyPressed(KeyEvent.VK_O)){
            core.getSoundManager().changeVolume(Sounds.MOVEMENT_PLAYER, 0.8f);
        }
        
        if(core.getInput().isKeyPressed(KeyEvent.VK_P)){
            core.getSoundManager().changeVolume(Sounds.MOVEMENT_PLAYER, 1.0f);
        }
        
        if(core.getInput().isKeyReleased(KeyEvent.VK_A) &&
           core.getInput().isKeyReleased(KeyEvent.VK_D) &&
           core.getInput().isKeyReleased(KeyEvent.VK_W) &&
           core.getInput().isKeyReleased(KeyEvent.VK_S)){
            core.getSoundManager().stop(Sounds.MOVEMENT_PLAYER);
        }
        
        
    }
    
    public void addWeapon(Weapon w){
        if(w!=null){
            weapon = w;
            weapon.setOwner(this);
        }
    }
    
    @Override
    public void render(Core gc, Renderer r) {
        r.setColor(Color.black);
        r.drawCircle((int)currentPosition.x,(int)currentPosition.y,20);
        r.setColor(Color.red);
        r.requestLine((int)currentPosition.x, (int)currentPosition.y,crosshair.x, crosshair.y);
        //r.drawLine(x, y,crosshair.x, crosshair.y);
    }
    
}
