/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects;

import components.AABB;
import engine.Core;
import render.Renderer;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
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
    protected int x,y;
    protected ObjectType type;
    protected ObjectState ObjState;
    protected AABB aabb;
    protected ArrayList<Point2D.Double> points;
    protected ArrayList<Line2D.Double> lines;
    private static long IDGEN;
    private long id;
    protected Light light;
    protected Material material;//later it will have arrayList of components
    
    
    
    public GameObject(int x, int y,ObjectType type){
        id = IDGEN++;
        this.type = type;
        this.x = x;
        this.y = y;
        ObjState = new ObjectState();
        points = new ArrayList<>();
        lines = new ArrayList<>();
        aabb = new AABB();
        light = null;
    }
    
    
    
    public void setType(ObjectType newType){
        type = newType;
    }
    
    public ObjectType getType(){
        return type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
    
    public ArrayList<Point2D.Double> getPoints(){
        return points;
    }

    public ArrayList<Line2D.Double> getLines() {
        return lines;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    
    public void addPoint(Point2D.Double point){
        if(!points.isEmpty()){
            lines.add(new Line2D.Double(points.get(points.size()-1),point));
        }
        points.add(point);
        aabb.update(point);
    }
    
    public void printPoints(){
        for(Point2D.Double p : points){
            System.out.println(p);
            
        }
        for(Line2D.Double l : lines){
            System.out.printf("Line2D.Double[%.1f,%,1f] ->[%.1f,%.1f]\n",l.x1,l.y1,l.x2,l.y2);
        }
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
    
    
    public abstract void update(float dt);
    public abstract void render(Renderer r);
}

