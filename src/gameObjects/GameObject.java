/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects;

import components.AABB;
import components.emitter.Emitter;
import engine.Core;
import render.Renderer;
import java.util.ArrayList;
import math.Line2f;
import math.Vector2f;
import render.Light;
import render.Material;
import utils.ObjectState;
import utils.ObjectType;

/**
 *
 * @author Zeus
 */
public abstract class GameObject {
    protected Core core;
    protected Vector2f position;
    protected ObjectType type;
    protected ObjectState ObjState;
    protected AABB aabb;
    protected ArrayList<Vector2f> points;
    protected ArrayList<Line2f> lines;
    private static long IDGEN;
    private long id;
    protected Light light;
    protected Material material;//later it will have arrayList of components
    protected Emitter emitter;
    protected int zIndex;
    
    
    public GameObject(float x, float y,ObjectType type){
        id = IDGEN++;
        this.type = type;
        position = new Vector2f(x,y);
        ObjState = new ObjectState();
        points = new ArrayList<>();
        lines = new ArrayList<>();
        aabb = new AABB();
        light = null;
        zIndex = 0;
    }
    
    public void addEmitter(Emitter em){
        emitter = em;
    }
    
    public void setType(ObjectType newType){
        type = newType;
    }
    
    public ObjectType getType(){
        return type;
    }

    public float getX() {
        return position.x;
    }

    public void setX(float x) {
        position.x = x;
    }

    public float getY() {
        return position.y;
    }

    public void setY(float y) {
        position.y = y;
    }

    public ObjectState getObjState() {
        return ObjState;
    }

    public void setObjState(ObjectState ObjState) {
        this.ObjState = ObjState;
    }

    public void setCore(Core core) {
        this.core = core;
    }

    public AABB getAabb() {
        return aabb;
    }

    public void setAabb(AABB aabb) {
        this.aabb = aabb;
    }

    public int getzIndex() {
        return zIndex;
    }

    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }
    
    public ArrayList<Vector2f> getPoints(){
        return points;
    }

    public ArrayList<Line2f> getLines() {
        return lines;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    
    public void addPoint(Vector2f point){
        if(!points.isEmpty()){
            lines.add(new Line2f(points.get(points.size()-1),point));
        }
        points.add(point);
        aabb.update(point);
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameObject other = (GameObject) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    public long getID(){
        return id;
    }

    public Light getLight() {
        return light;
    }
    
    protected void dispose(){
        core.getObjectManager().removeObject(this);
    }
    
    public boolean isCollidableWith(GameObject object){
        return ObjState.isCollidableWith(object.getObjState());
    }
    
    public void addCollidableWithType(String type){
        ObjState.addCollidableWithType(type);
    }
    
    public void addCollidableType(String type){
        ObjState.addCollidableType(type);
    }
    
    public void removeCollidableWithType(String type){
        ObjState.removeCollidableWithType(type);
    }
    
    public void removeCollidableType(String type){
        ObjState.removeCollidableType(type);
    }
    
    public void setRenderable(boolean renderable){
        ObjState.setRenderable(renderable);
    }
    public boolean isRenderable(){
        return ObjState.isRenderable();
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = new Vector2f(position);
    }
    
    public boolean isDead(){
        return false;
    }
    
    
    
    public abstract void update(float dt);
    public abstract void render(Renderer r);
}

