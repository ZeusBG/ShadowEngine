/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import engine.Core;
import gameObjects.DynamicGameObject;
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
    boolean canMoveFlag;//absolutely only for testing !
    
    public Physics (Core core){
        this.core = core;
    }
    
    public void moveObjects(float dt){
        
        if(canMoveFlag)
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
        checkWallCollision(core.getObjectManager().getPlayer());
        /*core.getObjectManager().getPlayer().update(core, (float)1.0);
        ObjectManager OM = core.getObjectManager();
        Line2D.Double playerLine = new Line2D.Double(OM.getPlayer().getCurrentPosition(),OM.getPlayer().getNextPosition());
        //System.out.printf("%s -- %s\n",playerLine.getP1(),playerLine.getP2());
        if(!playerLine.getP1().equals(playerLine.getP2())){
            System.out.println("in if");
            for(Line2D.Double wall:OM.getLines()){
                if(GeometryUtil.getIntersectionLines(playerLine, wall)!=null){
                    Point2D.Double nextPoint= GeometryUtil.getIntersectionLines(playerLine, wall);
                    Vector v = new Vector((Point2D.Double)wall.getP1(),(Point2D.Double)wall.getP2());
                    
                    v.normalize();
                    v.makePerpendicular();
                    
                    
                    Vector p = new Vector(OM.getPlayer().getCurrentPosition(),OM.getPlayer().getNextPosition());
                    //p.normalize();
                    v.changeParalelQuadrant(p.getQuadrant());
                    
                    if(p.getQuadrant()!=-1){
                        nextPoint.x += v.x;
                        nextPoint.y += v.y;
                        OM.getPlayer().setNextPosition(nextPoint);
                    }
                    
                    System.out.println("collision");
                }
            }
        }*/
        
        
        
    }
    
    public void update(float dt){
        
        //uncomment for collision
        //(buggy intersection between lines !!)
        canMoveFlag = true;
        checkHits();
        collisionObjects();
        moveObjects(dt);
    }
    
    public void checkWallCollision(DynamicGameObject obj){
        obj.update(core, (float)1.0);
        ObjectManager OM = core.getObjectManager();
        Line2D.Double objLine = new Line2D.Double(obj.getCurrentPosition(),obj.getNextPosition());
        //System.out.printf("%s -- %s\n",playerLine.getP1(),playerLine.getP2());
        if(!objLine.getP1().equals(objLine.getP2())){
            for(Line2D.Double wall:OM.getLines()){
                if(GeometryUtil.getIntersectionLines(objLine, wall)!=null){
                    Point2D.Double nextPoint= GeometryUtil.getIntersectionLines(objLine, wall);
                    Vector v = new Vector((Point2D.Double)wall.getP2(),(Point2D.Double)wall.getP1());
                    
                    v.normalize();
                    //v.makePerpendicular();

                    //p.normalize();
                    Vector normal = v.getPerpendicularCloserTo(OM.getPlayer().getCurrentPosition(),nextPoint);
                    nextPoint.x += normal.x;
                    nextPoint.y += normal.y;
                    
                    obj.setNextPosition(nextPoint);
                    
                    
                    System.out.println("collision");
                }
            }
        }
    }
    
}
