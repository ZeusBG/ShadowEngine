/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import engine.Core;
import gameObjects.ExplodingGameObject;
import gameObjects.GameObject;
import gameObjects.LivingObject;
import gameObjects.Projectile;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import math.Vector;
import test.Explosion1;
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

    public void updateObjects() {
        for (int i = 0; i < core.getObjectManager().getAllObjects().size(); i++) {
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
        if(!go1.isCollidableWith(go2))
            return;
        if (go1.getType() == ObjectType.ITEM) {
            
            if (go1 instanceof ExplodingGameObject) {
                //System.out.println(go1.getAabb());
                ExplodingGameObject ego = (ExplodingGameObject) go1;
                if(!ego.isExploded()){
                    
                    //System.out.println("it hasnt exploded yet");   
                    return;
                }
                HashSet<GameObject> objects = collisionTree.getObjectsInRange(ego.getCurrentAABB());
                for (GameObject go : objects) {
                    if (go instanceof LivingObject) {
                        LivingObject lo = (LivingObject) go;

                        double distanceToExplosion = GeometryUtil.getDistance(lo.getCurrentPosition(), ego.getCurrentPosition());
                        //System.out.println("dealing damage to "+go1.getID());
                        if (ego.getDamageDealtTo().get(go) == null
                                && distanceToExplosion > ego.getPreviousRadius()
                                && distanceToExplosion < ego.getCurrentRadius()) {

                            System.out.println("dealing damage to "+go.getID());
                            //ego.getDamageDealtTo().put(go, Boolean.TRUE);

                        }
                    }
                }

            }
            return;
        }

        else if (go1.getType() == ObjectType.PROJECTILE) {
            //go1.update(core, dt);
            Projectile projectile = (Projectile) go1;
            AABB ProjectilePathAABB = new AABB();
            ProjectilePathAABB.update(projectile.getCurrentPosition());
            ProjectilePathAABB.update(projectile.getNextPosition());

            Line2D.Double projectilePath = new Line2D.Double(projectile.getCurrentPosition(), projectile.getNextPosition());
            ArrayList<GameObject> objects = collisionTree.getObjectsLineIntersect(projectilePath);

            Point2D.Double intersection = GeometryUtil.getClosestIntersection(projectilePath, objects);
            if (intersection != null) {
                core.getObjectManager().removeObject(go1);
                Point2D.Double explosionSpawnPoint = new Point2D.Double();
                explosionSpawnPoint.x = intersection.x - projectile.getDirection().x * 2;
                explosionSpawnPoint.y = intersection.y - projectile.getDirection().y * 2;
                core.getObjectManager().addObject(new Explosion1((int) explosionSpawnPoint.x, (int) explosionSpawnPoint.y));
                collisionTree.remove(go1);
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
