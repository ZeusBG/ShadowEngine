/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects;

import gameObjects.util.AABB;
import gameObjects.util.Geometry;
import components.emitter.Emitter;
import engine.Core;
import engine.render.Renderer;
import java.util.ArrayList;
import math.Line2f;
import math.Vector2f;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2f;
import engine.render.Light;
import engine.render.Material;
import engine.render.Shader.Shader;
import gameObjects.util.ObjectState;
import gameObjects.util.ObjectType;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import engine.render.TexturedQuad;

/**
 *
 * @author Zeus
 */
public abstract class GameObject {
    protected Core core;
    protected Vector2f position;
    protected Vector2f orientation;
    protected ObjectType type;
    protected ObjectState ObjState;
    protected Geometry geometry;
    protected TexturedQuad texQuad;
    private static long IDGEN;
    private long id;
    protected Light light;
    protected Material material;//later it will have arrayList of components
    protected Emitter emitter;
    protected int zIndex;
    protected Shader shader;
    
    
    public GameObject(float x, float y,ObjectType type){
        id = IDGEN++;
        this.type = type;
        position = new Vector2f(x,y);
        ObjState = new ObjectState();
        geometry = new Geometry();
        light = null;
        zIndex = 0;
        orientation = new Vector2f(1,0);
        texQuad = null;
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
        return geometry.getAabb();
    }

    public int getzIndex() {
        return zIndex;
    }

    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }
    
    public ArrayList<Vector2f> getPoints(){
        return geometry.getPoints();
    }

    public ArrayList<Line2f> getLines() {
        return geometry.getLines();
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    
    public void addPoint(Vector2f point){
        geometry.addPoint(point);
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
        core.getScene().removeObject(this);
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

    public Geometry getGeometry() {
        return geometry;
    }
    
    public void prepareShader(){
        
        if(shader ==null){
            return;
        }
        
        int programId = shader.getProgramID();
        
        shader.bind();
        
        Vector2f offset = position;
        Vector2f cameraOffset = core.getCamera().getPosition();
        Vector2f scale = geometry.getScale();
        float rotAngle = orientation.getAngleInRadians();
        Vector2f resolutionScale = core.getCamera().getScale();
        
        glUniform2f(glGetUniformLocation(programId, "objectOffset"), offset.x,offset.y);
        glUniform2f(glGetUniformLocation(programId, "cameraOffset"),cameraOffset.x,cameraOffset.y);
        glUniform2f(glGetUniformLocation(programId, "scale"),scale.x,scale.y);
        glUniform2f(glGetUniformLocation(programId, "resolutionScale"),resolutionScale.x,resolutionScale.y);
        glUniform1f(glGetUniformLocation(programId, "rotAngle"),rotAngle);
        
        
        
    }
    
    
    public abstract void update(float dt);
    public abstract void render(Renderer r);
}

