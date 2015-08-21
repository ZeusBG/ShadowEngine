/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import engine.Core;
import gameObjects.DynamicGameObject;
import gameObjects.Projectile;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import math.Vector;
import utils.GeometryUtil;

/**
 *
 * @author Zeus
 */
public class Physics {
    private Core core;
    //private ObjectManager OM;
    
    public Physics (Core core){
        this.core = core;
    }
    
    public void moveObjects(float dt){
        for(int i=0;i<core.getObjectManager().getProjectiles().size();i++){
            Projectile p = core.getObjectManager().getProjectiles().get(i);
            
            if(p.getCurrentPosition().x>core.getWidth() || p.getCurrentPosition().y>core.getHeight()||
               p.getCurrentPosition().x<0 || p.getCurrentPosition().y<0){
                core.getObjectManager().getProjectiles().remove(i);
                continue;
            }
            p.update(core,dt);
            p.moveToNextPoint();
            
            
        }
        
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
        checkWallCollision(core.getObjectManager().getPlayer());

    }
    
    public void update(float dt){
        
        //uncomment for collision
        //(buggy intersection between lines !!)
        checkHits();
        collisionObjects();
        moveObjects(dt);
    }
    
    public void checkWallCollision(DynamicGameObject obj){
        obj.update(core, (float)1.0);
        ObjectManager OM = core.getObjectManager();
        Line2D.Double objLine = new Line2D.Double(obj.getCurrentPosition(),obj.getNextPosition());
        if(!objLine.getP1().equals(objLine.getP2())){
            for(Line2D.Double wall:OM.getLines()){
                if(GeometryUtil.getIntersectionLines(objLine, wall)!=null){
                    Point2D.Double nextPoint= GeometryUtil.getIntersectionLines(objLine, wall);
                    Vector v = new Vector((Point2D.Double)wall.getP2(),(Point2D.Double)wall.getP1());
                    
                    v.normalize();
                    Vector normal = v.getPerpendicularCloserTo(OM.getPlayer().getCurrentPosition(),nextPoint);
                    nextPoint.x += normal.x;
                    nextPoint.y += normal.y;
                    
                    obj.setNextPosition(nextPoint);
                    obj.moveToNextPoint();
                    return;
                }
            }
            obj.moveToNextPoint();
        }
    }
    
}
