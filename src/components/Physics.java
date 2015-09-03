/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import engine.Core;
import gameObjects.DynamicGameObject;
import gameObjects.GameObject;
import gameObjects.LivingObject;
import gameObjects.Projectile;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import math.Vector;
import utils.GeometryUtil;
import utils.ObjectType;

/**
 *
 * @author Zeus
 */
public class Physics {

    private Core core;
    private float dt;
    private QuadTree<GameObject> collisionTree;

    public Physics(Core core) {
        this.core = core;
        collisionTree = new QuadTree<>(4, 6, new AABB(0, 0, 900, 700));
    }

    public void moveObjects(float dt) {
        for (int i = 0; i < core.getObjectManager().getProjectiles().size(); i++) {
            Projectile p = core.getObjectManager().getProjectiles().get(i);

            if (p.getCurrentPosition().x > core.getWidth() || p.getCurrentPosition().y > core.getHeight()
                    || p.getCurrentPosition().x < 0 || p.getCurrentPosition().y < 0) {
                core.getObjectManager().getProjectiles().remove(i);
                core.getObjectManager().getAllObjects().remove(p);
            } else {
                p.moveToNextPoint();
            }

        }
        core.getObjectManager().getPlayer().moveToNextPoint();
    }

    public void update(float _dt) {
        collisionTree = new QuadTree<>(4, 6, new AABB(0, 0, 900, 700));
        for (GameObject go : core.getObjectManager().getAllObjects()) {
            collisionTree.insert(go);
        }
        
        this.dt = _dt;
        updateObjects();
        collisionTree.checkCollision(this);
        moveObjects(dt);
    }
    
    public void updateObjects(){
        for(int i=0;i<core.getObjectManager().getAllObjects().size();i++){
            core.getObjectManager().getAllObjects().get(i).update(core, dt);
        }
    }

    public QuadTree<GameObject> getCollisionTree() {
        return collisionTree;
    }
    
    public void checkCollision(GameObject go1, GameObject go2) {
        if (go1 == go2) {
            return;
        }

        if (go1.getType() == ObjectType.ENVIRONMENT) {
            return;
        }
        
        if(go1.getType() == ObjectType.ITEM){
            go1.update(core, dt);
            return;
        }
       
        if (go1.getType() == ObjectType.PROJECTILE) {
            //go1.update(core, dt);
            Projectile projectile = (Projectile) go1;
            Line2D.Double projectilePath = new Line2D.Double(projectile.getCurrentPosition(), projectile.getNextPosition());

            if (GeometryUtil.checkIntersectionLineAABB(projectilePath, go2.getAabb())) {
                if (go2.getType() == ObjectType.ENVIRONMENT) {

                    for (Line2D l : go2.getLines()) {
                        if (GeometryUtil.getIntersectionLines(projectilePath, l) != null) {
                            core.getObjectManager().getProjectiles().remove(projectile);
                            core.getObjectManager().getAllObjects().remove(go1);
                        }
                    }
                }
            }

        } else if (go1.getType() == ObjectType.PLAYER) {
            LivingObject player = (LivingObject) (go1);
            Line2D.Double playerPath = new Line2D.Double(player.getCurrentPosition(), player.getNextPosition());

            if (go2.getType() == ObjectType.ENVIRONMENT) {
                if (GeometryUtil.checkIntersectionLineAABB(playerPath, go2.getAabb())) {
                    for (Line2D.Double line : go2.getLines()) {
                        if (GeometryUtil.getIntersectionLines(playerPath, line) != null) {
                            System.out.println("intersection !");
                            Point2D.Double nextPoint = GeometryUtil.getIntersectionLines(playerPath, line);
                            Vector v = new Vector((Point2D.Double) line.getP2(), (Point2D.Double) line.getP1());

                            v.normalize();
                            Vector normal = v.getPerpendicularCloserTo(player.getCurrentPosition(), nextPoint);
                            nextPoint.x += 2 * normal.x;
                            nextPoint.y += 2 * normal.y;

                            player.setNextPosition(nextPoint);
                            
                            return;
                        }
                    }
                }
            }
        }

    }

}
