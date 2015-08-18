/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import engine.Core;
import gameObjects.DynamicGameObject;
import gameObjects.StaticGameObject;
import java.awt.geom.Line2D;
import utils.GeometryUtil;

/**
 *
 * @author Zeus
 */
public class Physics {
    private Core core;
    //private ObjectManager OM;
    boolean canMoveFlag;//absolutely only for testing !
    
    public Physics (Core core){
        this.core = core;
    }
    
    public void moveObjects(float dt){
        core.getObjectManager().getPlayer().update(core, dt);
        //if(canMoveFlag)
            core.getObjectManager().getPlayer().moveToNextPoint();
        
        for(DynamicGameObject obj : core.getObjectManager().getDynamicObjects()){
            obj.update(core, dt);
        }
        for(DynamicGameObject obj : core.getObjectManager().getDynamicObjects()){
            obj.move(dt);
        }
    }
    
    public void checkHits(){
        //shte chekva hitove na projectile-i sus objecti
    }
    
    public void collisionObjects(){
        ObjectManager OM = core.getObjectManager();
        Line2D.Double playerLine = new Line2D.Double(OM.getPlayer().getCurrentPosition(),OM.getPlayer().getNextPosition());
        System.out.printf("%s -- %s\n",playerLine.getP1(),playerLine.getP2());
        if(!playerLine.getP1().equals(playerLine.getP2())){
            System.out.println("in if");
            for(Line2D.Double wall:OM.getLines()){
                if(GeometryUtil.getIntersectionLines(playerLine, wall)!=null){
                    canMoveFlag = false;
                    System.out.println("collision");
                }
            }
        }
        
        
        
    }
    
    public void update(float dt){
        
        //uncomment for collision
        //(buggy intersection between lines !!)
        //canMoveFlag = true;
        checkHits();
        //collisionObjects();
        moveObjects(dt);
    }
    
}
