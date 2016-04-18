/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import engine.render.Renderer;
import gameObjects.LivingObject;
import gameObjects.Weapon;
import java.awt.Color;
import math.Vector2f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL20;
import engine.render.Light;
import engine.render.Shader.PlayerShader;
import engine.render.TexturedQuad;
import gameObjects.util.ObjectType;
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
        texQuad = new TexturedQuad(core.getWindow().getResolution(),new Vector2f(50,50));
        texQuad.loadTexture("../res/textures/char.png", "PNG");
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
        texQuad = new TexturedQuad(new Vector2f(400,225) ,new Vector2f(50,50));
        texQuad.loadTexture("res/textures/ship2.png", "png");
        texQuad.setScale(new Vector2f(1,1));
        
        String vShader = "src/engine/render/shaders/defaultQuadVShader.vert";
        String fShader = "src/engine/render/shaders/defaultQuadFShader.frag";
        PlayerShader shader = new PlayerShader(vShader,fShader);
        shader.bindAttributeLocations();
        texQuad.setShader(shader);
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
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        if(core.getInput().isKeyPressed(Keyboard.KEY_S)){
            direction.y += 1;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        if(core.getInput().isKeyPressed(Keyboard.KEY_A)){
            direction.x -= 1;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        if(core.getInput().isKeyPressed(Keyboard.KEY_D)){
            direction.x += 1;
            core.getSoundManager().play(Sounds.MOVEMENT_PLAYER);
        }
        direction.normalize();
        nextPosition.x = position.x+direction.x*speed*dt;
        nextPosition.y = position.y+direction.y*speed*dt;
        
        if(core.getInput().isKeyPressed(Keyboard.KEY_O)){
            //core.getSoundManager().changeVolume(Sounds.MOVEMENT_PLAYER, 0.8f);
            core.getCamera().zoomIn();
        }
        
        if(core.getInput().isKeyPressed(Keyboard.KEY_P)){
            core.getCamera().zoomOut();
            //core.getSoundManager().changeVolume(Sounds.MOVEMENT_PLAYER, 1.0f);
        }
        
        if(core.getInput().isKeyReleased(Keyboard.KEY_A) &&
           core.getInput().isKeyReleased(Keyboard.KEY_D) &&
           core.getInput().isKeyReleased(Keyboard.KEY_W) &&
           core.getInput().isKeyReleased(Keyboard.KEY_S)){
            core.getSoundManager().stop(Sounds.MOVEMENT_PLAYER);
        }
        
        if(core.getInput().isKeyPressed(Keyboard.KEY_F)){
            core.pauseGame();
        }
        
        geometry.getAabb().reset(position.x-5,position.y-5,position.x+5,position.y+5);

        texQuad.setSRT(new Vector2f(1,1),-orientation.getAngle() , position);
        texQuad.setCameraOffset(core.getCamera().getPosition());
        weapon.getGeometry().rotateAround(position, -orientation.getAngle());
        texQuad.setResolution(core.getCamera().getSize());
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
        texQuad.renderQuad();
    }
    
}
