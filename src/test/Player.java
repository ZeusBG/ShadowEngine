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
import java.awt.Point;
import java.awt.geom.Point2D;
import math.Vector;
import org.lwjgl.input.Keyboard;
import render.Light;
import utils.ObjectType;
import utils.Sounds;

/**
 *
 * @author Zeus
 */
public class Player extends LivingObject{

    private Point2D.Float crosshair;
    private Weapon weapon;
    private boolean laserActivated;
    public Player(ObjectType type) {
        super(type);
        speed = 500;
        aabb = new AABB(currentPosition.x-5,currentPosition.y-5,currentPosition.x+5,currentPosition.y+5);
        
    }
    
    public Player(){
        super(ObjectType.PLAYER);
        crosshair = new Point2D.Float();
        weapon = null;
        aabb = new AABB(currentPosition.x-5,currentPosition.y-5,currentPosition.x+5,currentPosition.y+5);
        light = new Light(this);
        light.setRadius(300);
        light.setPower(0.9f);
        direction = new Vector(0,0);
    }
    
    
    @Override
    public void update(float dt) {
        direction.reset();
        crosshair.x = core.getInput().getMouseX();
        crosshair.y = core.getInput().getMouseY();
        orientation.x = crosshair.x - currentPosition.x;
        orientation.y = crosshair.y - currentPosition.y;
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
            nextPosition.y = currentPosition.y-2;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        if(core.getInput().isKeyPressed(Keyboard.KEY_S)){
            direction.y += 1;
            nextPosition.y = currentPosition.y+2;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        if(core.getInput().isKeyPressed(Keyboard.KEY_A)){
            direction.x -=1;
            nextPosition.x = currentPosition.x-2;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        if(core.getInput().isKeyPressed(Keyboard.KEY_D)){
            direction.x+=1;
            nextPosition.x = currentPosition.x+2;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        direction.normalize();
        nextPosition.x = currentPosition.x+direction.x*speed*dt;
        nextPosition.y = currentPosition.y+direction.y*speed*dt;
        
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
        
        aabb.reset(currentPosition.x-5,currentPosition.y-5,currentPosition.x+5,currentPosition.y+5);
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
        r.drawCircle(currentPosition.x,currentPosition.y,10);
        r.setColor(Color.red);
        if(laserActivated){
            r.drawRay(currentPosition.x, currentPosition.y,crosshair.x, crosshair.y);
        }
    }
    
}
