/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import engine.Core;
import gameObjects.DynamicGameObject;
import math.Vector2f;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glTranslatef;

/**
 *
 * @author Zeus
 */
public class Camera {

    private DynamicGameObject target;
    private Vector2f offset;
    private Vector2f cameraCenter;
    private float cameraSpeed;
    private Vector2f size;
    private Core core;
    private boolean dynamic = false;
    private Vector2f scale;
    private AABB visibleArea;
    
 

    public Camera(Core core) {
        offset = new Vector2f(0, 0);
        size = new Vector2f();
        size.x = 0;
        size.y = 0;
        cameraCenter = new Vector2f(0, 0);
        cameraSpeed = 10;
        this.core = core;
        scale = new Vector2f();
        scale.x = core.getWindow().getWidth()/ size.x;
        scale.y = core.getWindow().getHeight() / size.y;
        visibleArea = new AABB();
        initCamera();
    }

    public Camera(int x, int y, int width, int height) {
        offset = new Vector2f(x, y);
        cameraCenter = new Vector2f(x + width / 2, y + height / 2);
        scale = new Vector2f();
        size = new Vector2f();
        size.x = width;
        size.y = height;
        cameraSpeed = 10;
        visibleArea = new AABB(x, y, x + width, y + height);
        initCamera();
    }

    private void initCamera(){
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, size.x, size.y, 0, -10, 10);
        glMatrixMode(GL_MODELVIEW);
    }

    public void update() {
        cameraSpeed = 5;
        float targetX = target.getPosition().x;
        float targetY = target.getPosition().y;

        if (dynamic) {

            offset.x = -(targetX + core.getInput().getMouseX()) / 2 + size.x / 2;
            offset.y =  -(targetY + core.getInput().getMouseY()) / 2 + size.y / 2;
            
            cameraCenter.x = -offset.x + size.x/2;
            cameraCenter.y = offset.y + size.y/2;
            
            visibleArea.reset(-offset.x, -offset.y, -offset.x  + size.x , -offset.y  + size.y );

        } else {
            
            cameraCenter.x += (targetX - cameraCenter.x) * cameraSpeed / 100.0;
            cameraCenter.y += (targetY - cameraCenter.y) * cameraSpeed / 100.0;
            
            offset.x = -(cameraCenter.x - size.x / 2);
            offset.y = -(cameraCenter.y - size.y / 2);
            
            visibleArea.reset(-offset.x, -offset.y, -offset.x  + size.x , -offset.y  + size.y);
        }
        restore();
        
    }
    
    public void zoomIn() {
        size.x *= 0.99;
        size.y *= 0.99;
        refreshScale();
    }
    
    public void zoomOut() {
        size.x /= 0.99;
        size.y /= 0.99;
        refreshScale();
    }

    public void refreshScale() {
        scale.x = core.getWindow().getWidth() / size.x;
        scale.y = core.getWindow().getHeight() / size.y;

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, size.x, size.y, 0, -100, 100);
        glMatrixMode(GL_MODELVIEW);
    }
    
    public void restore(){
        glLoadIdentity();
        glTranslatef(offset.x, offset.y, 1);
    }
    
    public DynamicGameObject getTarget() {
        return target;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public void setTarget(DynamicGameObject target) {
        this.target = target;
    }

    public Vector2f getPosition() {
        return offset;
    }

    public void setPosition(Vector2f position) {
        this.offset = position;
    }

    public AABB getVisibleArea() {
        return visibleArea;
    }

    public void setVisibleArea(AABB visibleArea) {
        this.visibleArea = visibleArea;
    }

    public float getWidth() {
        return size.x;
    }

    public void setCore(Core core) {
        this.core = core;
        scale.x = core.getWindow().getWidth()/ size.x;
        scale.y = core.getWindow().getHeight() / size.y;
    }

    public float getX() {
        return offset.x;
    }

    public float getY() {
        return offset.y;
    }

    public void setWidth(int width) {
        this.size.x = width;
    }

    public float getHeight() {
        return size.y;
    }

    public void setHeight(int height) {
        this.size.y = height;
    }

    public float getCameraSpeed() {
        return cameraSpeed;
    }
    
    public float getWidthScale() {
        return scale.x;
    }

    public void setWidthScale(float widthScale) {
        this.scale.x = widthScale;
    }

    public float getHeightScale() {
        return scale.y;
    }

    public void setHeightScale(float heightScale) {
        this.scale.y = heightScale;
    }
    
    public void setCameraSpeed(float cameraSpeed) {
        if (cameraSpeed <= 100 && cameraSpeed >= 0) {
            this.cameraSpeed = cameraSpeed;
        }
    }

    public Vector2f getScale() {
        return scale;
    }
    
    public Vector2f getSize(){
        return size;
    }
    
    public boolean isDynamic() {
        return dynamic;
    }

    public Vector2f getCameraCenter() {
        return cameraCenter;
    }
    
    
}
