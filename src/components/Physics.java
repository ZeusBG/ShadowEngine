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
    //private long timeResolvedCollision;
    //private long timeInsertedObjects;
    private boolean firstTime = true;

    public Physics(Core core) {
        this.core = core;
        mapSizeX = (int) core.getScene().getSize().x;
        mapSizeY = (int) core.getScene().getSize().y;

        treeMaxDepth = 7;
        treeMaxCapacity = 3;
        collisionTree = new QuadTree<>(treeMaxCapacity, treeMaxDepth, new AABB(0, 0, mapSizeX, mapSizeY));
    }

    public void moveObjects(float dt) {
        for (int i = 0; i < core.getScene().getProjectiles().size(); i++) {
            Projectile p = core.getScene().getProjectiles().get(i);

            if (p.getPosition().x > mapSizeX || p.getPosition().y > mapSizeY
                    || p.getPosition().x < 0 - 50 || p.getPosition().y < 0 - 50 || p.isDead()) {
                core.getScene().removeObject(p);
            } else {

                p.moveToNextPoint();
            }

        }
        core.getScene().getPlayer().moveToNextPoint();
    }

    public void update(float _dt) {
        //timeResolvedCollision = System.currentTimeMillis();
        //timeInsertedObjects = System.currentTimeMillis();
        collisionChecks = 0;
        //collisionTree.clean();

        //System.out.println("Time spent inserting objects: "+(System.currentTimeMillis()-timeInsertedObjects));
        //System.out.println("objects in tree: "+collisionTree.getNumOfObjects());
        //System.out.println("Nodes in the tree: "+collisionTree.getNumOfNodes());
        for (int i = core.getScene().getAllObjects().size() - 1; i >= 0; i--) {
            if (core.getScene().getAllObjects().get(i).isDead()) {
                core.getScene().removeObject(core.getScene().getAllObjects().get(i));
            }
        }

        //System.out.println("Number of objects in the game: " + core.getScene().getAllObjects().size());

        this.dt = _dt;
        updateObjects();
        collisionTree = new QuadTree<>(treeMaxCapacity, treeMaxDepth, new AABB(0, 0, mapSizeX, mapSizeY));
        insertObjects(core.getScene().getAllObjects());

        checkAllProjectilesCollision();
        collisionTree.checkCollision(this);

        moveObjects(dt);

        //System.out.println("Time spent for physics update objects: "+(System.currentTimeMillis()-timeResolvedCollision));
        //System.out.println("collisions resolved: "+collisionChecks);
    }

    public void updateObjects() {
        for (int i = 0; i < core.getScene().getAllObjects().size(); i++) {
            core.getScene().getAllObjects().get(i).update(dt);
        }
    }

    public QuadTree<GameObject> getCollisionTree() {
        return collisionTree;
    }

    public void insertObjects(ArrayList<GameObject> gameObjects) {
        for (GameObject go : gameObjects) {
            if (go.getType() != ObjectType.PROJECTILE) {
                collisionTree.insert(go);
            }
        }
    }

    public void checkAllProjectilesCollision() {
        for (int i = 0; i < core.getScene().getProjectiles().size(); i++) {
            checkProjectileCollision(core.getScene().getProjectiles().get(i));
        }
    }

    public void checkProjectileCollision(Projectile p) {

        collisionChecks++;

        Projectile projectile = (Projectile) p;

        Line2f projectilePath = new Line2f(projectile.getPosition().x, projectile.getPosition().y, projectile.getNextPosition().x, projectile.getNextPosition().y);
        ArrayList<GameObject> objects = new ArrayList<>();

        objects.addAll(collisionTree.getObjectsLineIntersect(projectilePath));

        Vector2f intersection = getClosestIntersection(p, projectilePath, objects);
        if (intersection != null) {
            

            if (projectile.getExplosive() != null) {
                Vector2f explosionSpawnPoint = new Vector2f();
                explosionSpawnPoint.x = intersection.x - projectile.getDirection().x;
                explosionSpawnPoint.y = intersection.y - projectile.getDirection().y;
                core.getScene().addObject(new Explosion1(explosionSpawnPoint.x, explosionSpawnPoint.y));
            }
            p.setToBeRemoved(true);

            firstTime = false;
        }

    }

    public static Vector2f getClosestIntersection(GameObject hitter, Line2f path, ArrayList<GameObject> objects) {
        Vector2f intersection = null;
        float currentDistance = 100000;
        GameObject gameObjectHit = null;
        Line2f lineHit = null;

        for (GameObject go : objects) {

            for (Line2f line : go.getLines()) {
                Vector2f currentIntersection = GeometryUtil.getIntersectionLines(path, line);
                if (currentIntersection != null && GeometryUtil.getDistance(currentIntersection, path.getP1()) < currentDistance) {
                    currentDistance = GeometryUtil.getDistance(currentIntersection, path.getP1());
                    intersection = currentIntersection;
                    gameObjectHit = go;
                    lineHit = line;
                }
            }
        }

        if (intersection != null) {

            if (gameObjectHit instanceof StaticGameObject) {
                StaticGameObject sgo = (StaticGameObject) gameObjectHit;
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

        if (go1 instanceof ExplodingGameObject) {
            //System.out.println(go1.getAabb());
            ExplodingGameObject ego = (ExplodingGameObject) go1;
            if (!ego.isExploded()) {
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
        } else if (go1 instanceof LivingObject) {
            LivingObject lObj = (LivingObject) (go1);
            Line2f objPath = new Line2f(lObj.getPosition().x, lObj.getPosition().y, lObj.getNextPosition().x, lObj.getNextPosition().y);

            if (go2 instanceof StaticGameObject) {
                if (GeometryUtil.checkIntersectionLineAABB(objPath, go2.getAabb())) {
                    for (Line2f line : go2.getLines()) {
                        if (GeometryUtil.getIntersectionLines(objPath, line) != null) {
                            //System.out.println("intersection !");
                            Vector2f nextPoint = GeometryUtil.getIntersectionLines(objPath, line);

                            Vector2f normal = line.getNormal();

                            nextPoint.x += normal.x;
                            nextPoint.y += normal.y;

                            lObj.setNextPosition(nextPoint);
                            return;
                        }
                    }
                }
            }
        }
    }

}
