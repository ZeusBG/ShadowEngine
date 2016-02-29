/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import components.AABB;
import render.Renderer;
import gameObjects.LivingObject;
import gameObjects.Weapon;
import java.awt.Color;
import math.Vector2f;
import org.lwjgl.input.Keyboard;
import render.Light;
import utils.ObjectType;
import utils.Sounds;

/**
 *
 * @author Zeus
 */
public class Player extends LivingObject{

    private Vector2f crosshair;
    private Weapon weapon;
    private boolean laserActivated;
    public Player(ObjectType type) {
        super(type);
        speed = 500;
        geometry.getAabb().reset(position.x-5,position.y-5,position.x+5,position.y+5);
        
    }
    
    public Player(){
        super(ObjectType.PLAYER);
        crosshair = new Vector2f();
        weapon = null;
        geometry.getAabb().reset(position.x-5,position.y-5,position.x+5,position.y+5);
        light = new Light(this);
        light.setRadius(150);
        light.setPower(1);
        direction = new Vector2f(0,0);
    }
    
    
    @Override
    public void update(float dt) {
        direction.reset();
        crosshair.x = core.getInput().getMouseX();
        crosshair.y = core.getInput().getMouseY();
        orientation.x = crosshair.x - position.x;
        orientation.y = crosshair.y - position.y;
        orientation.normalize();
        speed =150;
        if(core.getInput().isMouseButtonPressed(0)){
            weapon.fire();
        }
        
        if(core.getInput().isMouseButtonPressed(1)){
            laserActivated = true;
        }else{
            laserActivated = false;
        }
        
       if(core.getInput().isKeyPressed(Keyboard.KEY_R)){
           weapon.reload();
        }
        
        if(core.getInput().isKeyPressed(Keyboard.KEY_W)){
            direction.y -= 1;
            nextPosition.y = position.y-2;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        if(core.getInput().isKeyPressed(Keyboard.KEY_S)){
            direction.y += 1;
            nextPosition.y = position.y+2;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        if(core.getInput().isKeyPressed(Keyboard.KEY_A)){
            direction.x -=1;
            nextPosition.x = position.x-2;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        if(core.getInput().isKeyPressed(Keyboard.KEY_D)){
            direction.x+=1;
            nextPosition.x = position.x+2;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        direction.normalize();
        nextPosition.x = position.x+direction.x*speed*dt;
        nextPosition.y = position.y+direction.y*speed*dt;
        
        if(core.getInput().isKeyPressed(Keyboard.KEY_O)){
            //core.getSoundManager().changeVolume(Sounds.MOVEMENT_PLAYER, 0.8f);
            core.getScene().getCamera().zoomIn();
        }
        
        if(core.getInput().isKeyPressed(Keyboard.KEY_P)){
            core.getScene().getCamera().zoomOut();
            //core.getSoundManager().changeVolume(Sounds.MOVEMENT_PLAYER, 1.0f);
        }
        
        if(core.getInput().isKeyReleased(Keyboard.KEY_A) &&
           core.getInput().isKeyReleased(Keyboard.KEY_D) &&
           core.getInput().isKeyReleased(Keyboard.KEY_W) &&
           core.getInput().isKeyReleased(Keyboard.KEY_S)){
            core.getSoundManager().stop(Sounds.MOVEMENT_PLAYER);
        }
        
        geometry.getAabb().reset(position.x-5,position.y-5,position.x+5,position.y+5);
        System.out.println("Player pos: "+ position);
    }
    
    public void addWeapon(Weapon w){
        if(w!=null){
            weapon = w;
            weapon.setOwner(this);
        }
    }
    
    @Override
    public void render(Renderer r) {
        r.setColor(Color.WHITE);
        r.drawCircle(position.x,position.y,10);
        r.setColor(Color.red);
        if(laserActivated){
            r.drawRay(position.x, position.y,crosshair.x, crosshair.y);
        }
        else{
            
            r.drawLine(crosshair.x, crosshair.y-2, crosshair.x, crosshair.y-6);
            r.drawLine(crosshair.x-2, crosshair.y, crosshair.x-6, crosshair.y);
            r.drawLine(crosshair.x, crosshair.y+2, crosshair.x, crosshair.y+6);
            r.drawLine(crosshair.x+2, crosshair.y, crosshair.x+6, crosshair.y);
        }
    }
    
}
