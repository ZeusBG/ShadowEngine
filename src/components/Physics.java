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
    private int mapSizeX;
    private int mapSizeY;
    private int treeMaxDepth;
    private int treeMaxCapacity;
    private int collisionChecks;
    //private long timeResolvedCollision;
    //private long timeInsertedObjects;
    public Physics(Core core) {
        this.core = core;
        mapSizeX = 1600;
        mapSizeY = 1200;
        treeMaxDepth = 8;
        treeMaxCapacity = 2;
        collisionTree = new QuadTree<>(treeMaxCapacity, treeMaxDepth, new AABB(0, 0, mapSizeX, mapSizeY));
    }

    public void moveObjects(float dt) {
        for (int i = 0; i < core.getObjectManager().getProjectiles().size(); i++) {
            Projectile p = core.getObjectManager().getProjectiles().get(i);

            if (p.getCurrentPosition().x > mapSizeX || p.getCurrentPosition().y > mapSizeY
                    || p.getCurrentPosition().x < 0-50 || p.getCurrentPosition().y < 0-50) {
                core.getObjectManager().getProjectiles().remove(i);
                core.getObjectManager().getAllObjects().remove(p);
            } else {
                
                p.moveToNextPoint();
            }

        }
        core.getObjectManager().getPlayer().moveToNextPoint();
    }

    public void update(float _dt) {
        //timeResolvedCollision = System.currentTimeMillis();
        //timeInsertedObjects = System.currentTimeMillis();
        //collisionChecks = 0;
        //collisionTree.clean();
        
        //System.out.println("Time spent inserting objects: "+(System.currentTimeMillis()-timeInsertedObjects));
        //System.out.println("objects in tree: "+collisionTree.getNumOfObjects());
        //System.out.println("Nodes in the tree: "+collisionTree.getNumOfNodes());
        this.dt = _dt;
        updateObjects();
        collisionTree = new QuadTree<>(treeMaxCapacity, treeMaxDepth, new AABB(0, 0, mapSizeX, mapSizeY));
        for (GameObject go : core.getObjectManager().getAllObjects()) {
            collisionTree.insert(go);
        }
        collisionTree.checkCollision(this);
        
        moveObjects(dt);
        //System.out.println("Time spent for physics update objects: "+(System.currentTimeMillis()-timeResolvedCollision));
        //System.out.println("collisions resolved: "+collisionChecks);
    }

    public void updateObjects() {
        for (int i = 0; i < core.getObjectManager().getAllObjects().size(); i++) {
            core.getObjectManager().getAllObjects().get(i).update(dt);
        }
    }

    public QuadTree<GameObject> getCollisionTree() {
        return collisionTree;
    }

    public void checkCollision(GameObject go1, GameObject go2) {
        //collisionChecks++;
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

                            //System.out.println("dealing damage to "+go.getID());
                            ego.getDamageDealtTo().put(go, Boolean.TRUE);

                        }
                    }
                }
            }
            return;
        }

        else if (go1.getType() == ObjectType.PROJECTILE) {
            //go1.update(core, dt);
            Projectile projectile = (Projectile) go1;

            Line2D.Double projectilePath = new Line2D.Double(projectile.getCurrentPosition(), projectile.getNextPosition());
            ArrayList<GameObject> objects = new ArrayList<>();
            objects.addAll(collisionTree.getObjectsLineIntersect(projectilePath));

            Point2D.Double intersection = GeometryUtil.getClosestIntersection(projectilePath, objects);
            if (intersection != null) {
                Point2D.Double explosionSpawnPoint = new Point2D.Double();
                explosionSpawnPoint.x = intersection.x - projectile.getDirection().x * 3;
                explosionSpawnPoint.y = intersection.y - projectile.getDirection().y * 3;
                core.getObjectManager().addObject(new Explosion1((int) explosionSpawnPoint.x, (int) explosionSpawnPoint.y));
                collisionTree.remove(go1);
                core.getObjectManager().removeObject(go1);
            }

        } else if (go1.getType() == ObjectType.PLAYER) {
            LivingObject player = (LivingObject) (go1);
            Line2D.Double playerPath = new Line2D.Double(player.getCurrentPosition(), player.getNextPosition());

            if (go2.getType() == ObjectType.ENVIRONMENT) {
                if (GeometryUtil.checkIntersectionLineAABB(playerPath, go2.getAabb())) {
                    for (Line2D.Double line : go2.getLines()) {
                        if (GeometryUtil.getIntersectionLines(playerPath, line) != null) {
                            //System.out.println("intersection !");
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
