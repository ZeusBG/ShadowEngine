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
import gameObjects.StaticGameObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import math.Line2f;
import math.Vector2f;
import test.Explosion1;
import math.GeometryUtil;
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
    private HashMap<GameObject, Boolean> checkedProjectiles;
    //private long timeResolvedCollision;
    //private long timeInsertedObjects;
    private boolean firstTime = true;

    public Physics(Core core) {
        this.core = core;
        mapSizeX = 1600;
        mapSizeY = 1200;
        treeMaxDepth = 7;
        treeMaxCapacity = 3;
        collisionTree = new QuadTree<>(treeMaxCapacity, treeMaxDepth, new AABB(0, 0, mapSizeX, mapSizeY));
    }

    public void moveObjects(float dt) {
        for (int i = 0; i < core.getObjectManager().getProjectiles().size(); i++) {
            Projectile p = core.getObjectManager().getProjectiles().get(i);

            if (p.getPosition().x > mapSizeX || p.getPosition().y > mapSizeY
                    || p.getPosition().x < 0 - 50 || p.getPosition().y < 0 - 50 || p.isDead()) {
                core.getObjectManager().removeObject(p);
            } else {

                p.moveToNextPoint();
            }

        }
        core.getObjectManager().getPlayer().moveToNextPoint();
    }

    public void update(float _dt) {
        checkedProjectiles = new HashMap<>();
        //timeResolvedCollision = System.currentTimeMillis();
        //timeInsertedObjects = System.currentTimeMillis();
        collisionChecks = 0;
        //collisionTree.clean();

        //System.out.println("Time spent inserting objects: "+(System.currentTimeMillis()-timeInsertedObjects));
        //System.out.println("objects in tree: "+collisionTree.getNumOfObjects());
        //System.out.println("Nodes in the tree: "+collisionTree.getNumOfNodes());
        
        
        
        for (int i = core.getObjectManager().getAllObjects().size() - 1; i >= 0; i--) {
            if (core.getObjectManager().getAllObjects().get(i).isDead()) {
                core.getObjectManager().removeObject(core.getObjectManager().getAllObjects().get(i));
            }
        }
        
        System.out.println("Number of objects in the game: "+core.getObjectManager().getAllObjects().size());
        
        this.dt = _dt;
        updateObjects();
        collisionTree = new QuadTree<>(treeMaxCapacity, treeMaxDepth, new AABB(0, 0, mapSizeX, mapSizeY));
        insertObjects(core.getObjectManager().getAllObjects());
        
        checkAllProjectilesCollision();
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
    
    public void insertObjects(ArrayList<GameObject> gameObjects){
        for (GameObject go : gameObjects) {
            if(go.getType()!=ObjectType.PROJECTILE)
                collisionTree.insert(go);
        }
    }
    
    public void checkAllProjectilesCollision(){
        for(int i=0;i<core.getObjectManager().getProjectiles().size();i++){
            checkProjectileCollision(core.getObjectManager().getProjectiles().get(i));
        }
    }
    
    public void checkProjectileCollision(Projectile p) {

        collisionChecks++;

        if (checkedProjectiles.get(p) != null) {
            return;
        }
        checkedProjectiles.put(p, Boolean.TRUE);
        Projectile projectile = (Projectile) p;

        Line2f projectilePath = new Line2f(projectile.getPosition().x, projectile.getPosition().y, projectile.getNextPosition().x, projectile.getNextPosition().y);
        ArrayList<GameObject> objects = new ArrayList<>();
        
        objects.addAll(collisionTree.getObjectsLineIntersect(projectilePath));
        
        Vector2f intersection = getClosestIntersection(p,projectilePath, objects);
        if (intersection != null) {
            Vector2f explosionSpawnPoint = new Vector2f();
            explosionSpawnPoint.x = intersection.x - projectile.getDirection().x * 3;
            explosionSpawnPoint.y = intersection.y - projectile.getDirection().y * 3;

            if (projectile.getExplosive() != null) {

                core.getObjectManager().addObject(new Explosion1(explosionSpawnPoint.x, explosionSpawnPoint.y));
            }
            p.setToBeRemoved(true);
            

            
            firstTime = false;
        }

    }
    
    public static Vector2f getClosestIntersection(GameObject hitter,Line2f path,ArrayList<GameObject> objects){
        Vector2f intersection = null;
        float currentDistance = 100000;
        GameObject gameObjectHit = null;
        Line2f lineHit = null;
        
        for(GameObject go: objects){
            
                for(Line2f line: go.getLines()){
                    Vector2f currentIntersection = GeometryUtil.getIntersectionLines(path, line);
                    if(currentIntersection!=null && GeometryUtil.getDistance(currentIntersection,path.getP1())<currentDistance){
                        currentDistance = GeometryUtil.getDistance(currentIntersection,path.getP1());
                        intersection = currentIntersection;
                        gameObjectHit = go;
                        lineHit = line;
                    }
                }
        }
        
        if(intersection !=null){
            
            if(gameObjectHit instanceof StaticGameObject){
                StaticGameObject sgo = (StaticGameObject)gameObjectHit;
                sgo.reactionOnHit(hitter, intersection, lineHit);
            }
            
        }
        
        return intersection;
    }
    

    public void checkCollision(GameObject go1, GameObject go2) {

        if (go1.getType() == ObjectType.ENVIRONMENT) {
            return;
        }

        if (!go1.isCollidableWith(go2)) {
            return;
        }

        collisionChecks++;
        if (go1.getType() == ObjectType.ITEM) {

            if (go1 instanceof ExplodingGameObject) {
                //System.out.println(go1.getAabb());
                ExplodingGameObject ego = (ExplodingGameObject) go1;
                if (!ego.isExploded()) {

                    //System.out.println("it hasnt exploded yet");   
                    return;
                }
                HashSet<GameObject> objects = collisionTree.getObjectsInRange(ego.getCurrentAABB());
                for (GameObject go : objects) {
                    if (go instanceof LivingObject) {
                        LivingObject lo = (LivingObject) go;

                        Vector2f p1 = new Vector2f(lo.getPosition().x, lo.getPosition().y);
                        Vector2f p2 = new Vector2f(ego.getPosition().x, ego.getPosition().y);
                        float distanceToExplosion = GeometryUtil.getDistance(p1, p2);
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
        } else if (go1.getType() == ObjectType.PLAYER) {
            LivingObject player = (LivingObject) (go1);
            Line2f playerPath = new Line2f(player.getPosition().x, player.getPosition().y, player.getNextPosition().x, player.getNextPosition().y);

            if (go2.getType() == ObjectType.ENVIRONMENT) {
                if (GeometryUtil.checkIntersectionLineAABB(playerPath, go2.getAabb())) {
                    for (Line2f line : go2.getLines()) {
                        if (GeometryUtil.getIntersectionLines(playerPath, line) != null) {
                            //System.out.println("intersection !");
                            Vector2f nextPoint = GeometryUtil.getIntersectionLines(playerPath, line);
                            Vector2f v = new Vector2f((Vector2f) line.getP2(), (Vector2f) line.getP1());

                            v.normalize();
                            Vector2f normal = v.getPerpendicularCloserTo(player.getPosition(), nextPoint);
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
