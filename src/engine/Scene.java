/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import engine.space.QuadTree;
import gameObjects.util.AABB;
import engine.Core;
import gameObjects.DynamicGameObject;
import gameObjects.GameObject;
import gameObjects.LivingObject;
import gameObjects.Projectile;
import gameObjects.StaticGameObject;
import java.util.ArrayList;
import math.Vector2f;
import gameObjects.util.ObjectType;

/**
 *
 * @author Zeus
 *
 */
public class Scene {

    /*other managers here later */
    private Core core;
    private LivingObject player;
    private ArrayList<GameObject> allObjects;
    private ArrayList<StaticGameObject> staticObjects;
    private ArrayList<DynamicGameObject> dynamicObjects;
    private ArrayList<DynamicGameObject> bodies;
    private ArrayList<Projectile> projectiles;

    private int numObjects;
    private Vector2f size;
    private QuadTree rayCollisionTree;

    public Scene(Core core, float width, float height) {
        this.core = core;
        staticObjects = new ArrayList<>();
        dynamicObjects = new ArrayList<>();
        bodies = new ArrayList<>();
        projectiles = new ArrayList<>();
        size = new Vector2f(width, height);
        //should be map.getWidth() and map.getHeight() nut there isnt a map for now
        rayCollisionTree = new QuadTree<StaticGameObject>(3, 8, new AABB(0, 0, width, height));//should be map.getWidth() and map.getHeight() nut there isnt a map for now
        allObjects = new ArrayList<>();
        numObjects = 0;

    }

    public ArrayList<StaticGameObject> getStaticObjects() {
        return staticObjects;
    }

    public ArrayList<DynamicGameObject> getDynamicObjects() {

        return dynamicObjects;
    }

    public ArrayList<DynamicGameObject> getBodies() {
        return bodies;
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public LivingObject getPlayer() {
        return player;
    }

    public void addStaticObject(GameObject obj) {

        staticObjects.add((StaticGameObject) obj);

        rayCollisionTree.insert(obj);

    }

    public void update(float delta) {

        rayCollisionTree = new QuadTree<StaticGameObject>(3, 8, new AABB(-1000, -1000, 2600, 2200));
        for (StaticGameObject s : staticObjects) {
            rayCollisionTree.insert(s);
        }

    }

    public void addObject(GameObject obj) {
        //System.out.println("ADDING: "+obj.getType()+" ID: "+obj.getID());

        allObjects.add(obj);

        obj.setCore(core);
        if (obj.getType() == ObjectType.PLAYER) {
            player = (LivingObject) obj;
            numObjects++;
        } else if (obj.getType() == ObjectType.PROJECTILE) {
            projectiles.add((Projectile) obj);
            dynamicObjects.add((Projectile) obj);
            numObjects += 2;
        } else if (obj.getType() == ObjectType.NPC || obj.getType() == ObjectType.ITEM) {
            dynamicObjects.add((DynamicGameObject) obj);
            numObjects++;
        } else if (obj.getType() == ObjectType.ENVIRONMENT) {
            addStaticObject(obj);
            numObjects++;
        }
    }

    public QuadTree getRayCollisionTree() {
        return rayCollisionTree;
    }

    public ArrayList<GameObject> getAllObjects() {
        return allObjects;
    }

    public void removeObject(GameObject obj) {
        allObjects.remove(obj);

        if (obj.getType() == ObjectType.PROJECTILE) {
            projectiles.remove(obj);
            dynamicObjects.remove(obj);
            numObjects -= 2;
        } else if (obj.getType() == ObjectType.ENVIRONMENT) {
            staticObjects.remove(obj);
            numObjects--;
        } else if (obj.getType() == ObjectType.ITEM || obj.getType() == ObjectType.NPC) {
            dynamicObjects.remove(obj);
            numObjects--;
        }
    }

    public Vector2f getSize() {
        return size;
    }

    public void printNumObjects() {
        System.err.println("Objects in scene: " + (dynamicObjects.size() + projectiles.size()));
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }
}
