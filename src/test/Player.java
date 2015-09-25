/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import components.AABB;
import engine.Core;
import render.Renderer;
import gameObjects.LivingObject;
import gameObjects.Weapon;
import java.awt.Color;
import java.awt.Point;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import render.Light;
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
        aabb = new AABB(currentPosition.x-5,currentPosition.y-5,currentPosition.x+5,currentPosition.y+5);
        
    }
    
    public Player(){
        super(200,210,ObjectType.PLAYER);
        crosshair = new Point();
        weapon = null;
        aabb = new AABB(currentPosition.x-5,currentPosition.y-5,currentPosition.x+5,currentPosition.y+5);
        light = new Light(this);
        light.setRadius(300);
        
    }
    
    
    public void update(Core core, float dt) {
        
        crosshair.x = core.getInput().getMouseX();
        crosshair.y = core.getInput().getMouseY();
        
        orientation.x = crosshair.x - currentPosition.x;
        orientation.y = crosshair.y - currentPosition.y;
        orientation.normalize();
        
        if(core.getInput().isMouseButtonPressed(0)){
            weapon.fire();
        }
        
       if(core.getInput().isKeyPressed(Keyboard.KEY_R)){
           weapon.reload();
        }
        
        if(core.getInput().isKeyPressed(Keyboard.KEY_W)){
            nextPosition.y = currentPosition.y-2;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        if(core.getInput().isKeyPressed(Keyboard.KEY_S)){
            nextPosition.y = currentPosition.y+2;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        if(core.getInput().isKeyPressed(Keyboard.KEY_A)){
            nextPosition.x = currentPosition.x-2;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        if(core.getInput().isKeyPressed(Keyboard.KEY_D)){
            nextPosition.x = currentPosition.x+2;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        
        
        
        if(core.getInput().isKeyPressed(Keyboard.KEY_O)){
            //core.getSoundManager().changeVolume(Sounds.MOVEMENT_PLAYER, 0.8f);
            core.getObjectManager().getCamera().zoomIn();
        }
        
        if(core.getInput().isKeyPressed(Keyboard.KEY_P)){
            core.getObjectManager().getCamera().zoomOut();
            //core.getSoundManager().changeVolume(Sounds.MOVEMENT_PLAYER, 1.0f);
        }
        
        if(core.getInput().isKeyReleased(Keyboard.KEY_A) &&
           core.getInput().isKeyReleased(Keyboard.KEY_D) &&
           core.getInput().isKeyReleased(Keyboard.KEY_W) &&
           core.getInput().isKeyReleased(Keyboard.KEY_S)){
            core.getSoundManager().stop(Sounds.MOVEMENT_PLAYER);
        }
        
        aabb = new AABB(currentPosition.x-5,currentPosition.y-5,currentPosition.x+5,currentPosition.y+5);
    }
    
    public void addWeapon(Weapon w){
        if(w!=null){
            weapon = w;
            weapon.setOwner(this);
        }
    }
    
    @Override
    public void render(Core core, Renderer r) {
        r.setColor(Color.black);
        r.drawCircle((int)currentPosition.x,(int)currentPosition.y,5);
        r.setColor(Color.red);
        r.drawRay((int)currentPosition.x, (int)currentPosition.y,crosshair.x, crosshair.y);
        //r.drawLine(x, y,crosshair.x, crosshair.y);
    }
    
}
