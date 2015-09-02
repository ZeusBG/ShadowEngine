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
import gameObjects.StaticGameObject;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import utils.ObjectType;

/**
 *
 * @author Zeus
 */
public class ObjectManager {
    
    /*other managares here later */
    private Core core;
    private LivingObject player;
    private ArrayList<GameObject> allObjects;
    private ArrayList<StaticGameObject> staticObjects;
    private ArrayList<DynamicGameObject> dynamicObjects;
    private ArrayList<DynamicGameObject> bodies;
    private ArrayList<Projectile> projectiles;
    private ArrayList<Line2D.Double> lines;
    private ArrayList<Point2D.Double> staticObjPoints;
    private QuadTree collisionTree;
    private QuadTree rayCollisionTree;
    
    private Camera camera;
    
    public ObjectManager(Core core){
        this.core = core;
        staticObjects = new ArrayList<>();
        dynamicObjects = new ArrayList<>();
        bodies = new ArrayList<>();
        projectiles = new ArrayList<>();
        lines = new ArrayList<>();
        staticObjPoints = new ArrayList<>();
        collisionTree = new QuadTree<GameObject>(4,6,new AABB(0,0,900,700));//should be map.getWidth() and map.getHeight() nut there isnt a map for now
        rayCollisionTree = new QuadTree<StaticGameObject>(4,6,new AABB(0,0,900,700));//should be map.getWidth() and map.getHeight() nut there isnt a map for now
        allObjects = new ArrayList<>();
        camera = new Camera(0,0,600,500);
        camera.setCore(core);
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
    
    public LivingObject getPlayer(){
        return player;
    }
    
    public void addStaticObject(GameObject obj){
        
        System.out.println("adding static object");
        
        staticObjects.add((StaticGameObject)obj);
        StaticGameObject tmp = (StaticGameObject) obj;
        Line2D.Double tmpLine = null;
        for(int i=0;i<tmp.getPoints().size()-1;i++){
            tmpLine = new Line2D.Double(tmp.getPoints().get(i),tmp.getPoints().get(i+1));
            lines.add(tmpLine);
            staticObjPoints.add(tmp.getPoints().get(i));
        }
        staticObjPoints.add(tmp.getPoints().get(tmp.getPoints().size()-1));
        
        rayCollisionTree.insert(obj);
    }
    
    public ArrayList<Point2D.Double> getPoints(){
        return staticObjPoints;
    }
    
    public ArrayList<Line2D.Double> getLines(){
        return lines;
    }
    
    public void addObject(GameObject obj){
        System.out.println("OBJECT ID: "+obj.getID());
        allObjects.add(obj);
        obj.setCore(core);
        //System.out.println("asdad");
        if(obj.getType()==ObjectType.PLAYER)
            player = (LivingObject) obj;
        else if(obj.getType()==ObjectType.PROJECTILE)
            projectiles.add((Projectile)obj);
        else if(obj.getType()==ObjectType.NPC)
            dynamicObjects.add((LivingObject)obj);
        else if(obj.getType()==ObjectType.ITEM)
            dynamicObjects.add((DynamicGameObject)obj);
        else if(obj.getType()==ObjectType.ENVIRONMENT)
            addStaticObject(obj);
    }

    public Camera getCamera() {
        return camera;
    }
    
    public void update(){
        camera.update();
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public QuadTree getRayCollisionTree() {
        return rayCollisionTree;
    }

    public ArrayList<GameObject> getAllObjects() {
        return allObjects;
    }
    
    
    
    
    
}
