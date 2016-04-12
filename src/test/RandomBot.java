/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import gameObjects.GameObject;
import gameObjects.LivingObject;
import gameObjects.NPC;
import gameObjects.Weapon;
import java.awt.Color;
import java.util.ArrayList;
import math.Line2f;
import engine.render.Renderer;
import math.GeometryUtil;



/**
 *
 * @author Zeus
 */
public class RandomBot extends NPC{
    
    private Weapon weapon;
    
    public RandomBot() {
        
    }

    @Override
    public void update(float dt) {
        LivingObject player = core.getScene().getPlayer();
        Line2f line = new Line2f(player.getPosition().x,player.getPosition().y,position.x,position.y);
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.addAll(core.getScene().getRayCollisionTree().getObjectsLineIntersect(line));
        if(GeometryUtil.getClosestIntersection(line, objects)==null){
            
            
            orientation.reset(position,player.getPosition());
            orientation.normalize();
            if(weapon.isClipEmpty()){
                weapon.reload();
                
            }
            fire();
        }

    }

    @Override
    public void render(Renderer r) {
        
       // r.setColor(Color.ORANGE);
       // r.drawCircle(position.x,position.y,5);

    }
    
    public boolean hasVisibility(LivingObject target){
        return false;
    }
    
    public void addWeapon(Weapon w){
        if(w!=null){
            weapon = w;
            weapon.setOwner(this);
        }
    }
    
    public void fire(){
        if(weapon!=null){
            weapon.fire();
        }
    }
}
