/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import components.emitter.IParticle;
import engine.Core;
import gameObjects.DynamicGameObject;
import gameObjects.GameObject;
import gameObjects.LivingObject;
import gameObjects.Projectile;
import gameObjects.StaticGameObject;
import java.util.ArrayList;
import math.Line2f;
import math.Vector2f;
import utils.ObjectType;

/**
 *
 * @author Zeus
 * 
 */
public class Scene {
    
    /*other managares here later */
    private Core core;
    private LivingObject player;
    private ArrayList<GameObject> allObjects;
    private ArrayList<StaticGameObject> staticObjects;
    private ArrayList<DynamicGameObject> dynamicObjects;
    private ArrayList<DynamicGameObject> bodies;
    private ArrayList<Projectile> projectiles;
    private ArrayList<Line2f> lines;
    private ArrayList<Vector2f> staticObjPoints;
    private Vector2f size;
    private QuadTree rayCollisionTree;
    
    private Camera camera;
    
    public Scene(Core core, float width, float height){
        this.core = core;
        staticObjects = new ArrayList<>();
        dynamicObjects = new ArrayList<>();
        bodies = new ArrayList<>();
        projectiles = new ArrayList<>();
        lines = new ArrayList<>();
        staticObjPoints = new ArrayList<>();
        size = new Vector2f(width,height);
        //should be map.getWidth() and map.getHeight() nut there isnt a map for now
        rayCollisionTree = new QuadTree<StaticGameObject>(3,8,new AABB(0,0,width,height));//should be map.getWidth() and map.getHeight() nut there isnt a map for now
        allObjects = new ArrayList<>();
        camera = new Camera(0,0,400,225);
        camera.setCore(core);
        camera.setDynamic(true);
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
        Line2f tmpLine = null;
        for(int i=0;i<tmp.getLines().size();i++){
            lines.add(tmp.getLines().get(i));
            //staticObjPoints.add(tmp.getPoints().get(i));
        }
        staticObjPoints.add(tmp.getPoints().get(tmp.getPoints().size()-1));
        
        rayCollisionTree.insert(obj);
        
    }
    
    public ArrayList<Vector2f> getPoints(){
       
        return staticObjPoints;
    }
    
    public ArrayList<Line2f> getLines(){
        return lines;
    }
    
    public void update(float delta){
        
        rayCollisionTree = new QuadTree<StaticGameObject>(3,8,new AABB(-1000,-1000,2600,2200));
        for(StaticGameObject s : staticObjects){
            rayCollisionTree.insert(s);
        }
        
    }
    
    public void addObject(GameObject obj){
        //System.out.println("ADDING: "+obj.getType()+" ID: "+obj.getID());
        
        allObjects.add(obj);
        obj.setCore(core);
        if(obj.getType()==ObjectType.PLAYER)
            player = (LivingObject) obj;
        else if(obj.getType()==ObjectType.PROJECTILE){
            projectiles.add((Projectile)obj);
            dynamicObjects.add((Projectile)obj);
        }
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
    
    public void removeObject(GameObject obj){
        allObjects.remove(obj);
        
        if(obj.getType()==ObjectType.PROJECTILE){
            projectiles.remove(obj);
            dynamicObjects.remove(obj);
        }
        else if(obj.getType()==ObjectType.ENVIRONMENT){
            staticObjects.remove(obj);
        }
        else if(obj.getType()==ObjectType.ITEM || obj.getType()==ObjectType.NPC){
            dynamicObjects.remove(obj);
        }
        
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }
    
    
    
    
    
}
