/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import engine.Core;
import gameObjects.DynamicGameObject;
import java.util.Random;
import math.Vector2f;
import org.lwjgl.opengl.GL11;
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
    private float width, height;
    private Core core;
    private boolean dynamic = false;
    private Vector2f scale;
    private AABB visibleArea;

 

    public Camera(Core core) {
        offset = new Vector2f(0, 0);
        width = 0;
        height = 0;
        cameraCenter = new Vector2f(0, 0);
        cameraSpeed = 10;
        this.core = core;
        scale = new Vector2f();
        scale.x = core.getWindow().getWidth()/ width;
        scale.y = core.getWindow().getHeight() / height;
        visibleArea = new AABB();
        initCamera();
    }

    public Camera(int x, int y, int width, int height) {
        offset = new Vector2f(x, y);
        cameraCenter = new Vector2f(x + width / 2, y + height / 2);
        scale = new Vector2f();
        this.width = width;
        this.height = height;
        cameraSpeed = 10;
        visibleArea = new AABB(x, y, x + width, y + height);
        initCamera();
    }

    private void initCamera(){
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, height, 0, -10, 10);
        glMatrixMode(GL_MODELVIEW);
    }

    public void update() {
        cameraSpeed = 5;
        float targetX = target.getPosition().x;
        float targetY = target.getPosition().y;

        if (dynamic) {

            offset.x = -(targetX + core.getInput().getMouseX()) / 2 + width / 2;
            offset.y =  -(targetY + core.getInput().getMouseY()) / 2 + height / 2;
            
            cameraCenter.x = -offset.x + width/2;
            cameraCenter.y = offset.y + height/2;
            
            visibleArea.reset(-offset.x, -offset.y, -offset.x  + width , -offset.y  + height );

        } else {
            
            cameraCenter.x += (targetX - cameraCenter.x) * cameraSpeed / 100.0;
            cameraCenter.y += (targetY - cameraCenter.y) * cameraSpeed / 100.0;
            
            offset.x = -(cameraCenter.x - width / 2);
            offset.y = -(cameraCenter.y - height / 2);
            
            visibleArea.reset(-offset.x, -offset.y, -offset.x  + width , -offset.y  + height);
        }
        
        restore();
        
    }
    
    public void zoomIn() {
        width *= 0.99;
        height *= 0.99;
        refreshScale();
    }

    public void zoomOut() {
        width /= 0.99;
        height /= 0.99;
        refreshScale();
    }

    public void refreshScale() {
        scale.x = core.getWindow().getWidth() / width;
        scale.y = core.getWindow().getHeight() / height;

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, height, 0, -100, 100);
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
        return width;
    }

    public void setCore(Core core) {
        this.core = core;
        scale.x = core.getWindow().getWidth()/ width;
        scale.y = core.getWindow().getHeight() / height;
    }

    public float getX() {
        return offset.x;
    }

    public float getY() {
        return offset.y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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
    
    public boolean isDynamic() {
        return dynamic;
    }
}
