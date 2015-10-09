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
import java.awt.geom.Line2D;
import java.util.ArrayList;
import render.Renderer;
import utils.GeometryUtil;



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
        LivingObject player = core.getObjectManager().getPlayer();
        Line2D.Float line = new Line2D.Float(player.getCurrentPosition(),currentPosition);
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.addAll(core.getObjectManager().getRayCollisionTree().getObjectsLineIntersect(line));
        if(GeometryUtil.getClosestIntersection(line, objects)==null){
            
            
            orientation.reset(currentPosition,player.getCurrentPosition());
            if(weapon.isClipEmpty()){
                weapon.reload();
                
            }
            fire();
        }

    }

    @Override
    public void render(Renderer r) {
        
        r.setColor(Color.ORANGE);
        r.drawCircle((int)currentPosition.x,(int)currentPosition.y,5);

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
