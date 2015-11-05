/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import engine.Core;
import gameObjects.DynamicGameObject;
import math.Vector2f;

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
    private Vector2f dynamicLock;
    private boolean dynamic = false;
    private float widthScale = 1.0f;
    private float heightScale = 1.0f;
    private float backUpHeightScale = 1.0f; // temporary solution !
    private float backUpWidthScale = 1.0f;
    private AABB visibleArea;

    public float getWidthScale() {
        return widthScale;
    }

    public void setWidthScale(float widthScale) {
        this.widthScale = widthScale;
    }

    public float getHeightScale() {
        return heightScale;
    }

    public void setHeightScale(float heightScale) {
        this.heightScale = heightScale;
    }

    public Camera(Core core) {
        offset = new Vector2f(0, 0);
        width = 0;
        height = 0;
        cameraCenter = new Vector2f(0, 0);
        cameraSpeed = 10;
        this.core = core;
        dynamicLock = new Vector2f(0, 0);
        widthScale = core.getWidth() / width;
        heightScale = core.getHeight() / height;

        visibleArea = new AABB();
    }

    public Camera(int x, int y, int width, int height) {
        offset = new Vector2f(x, y);
        cameraCenter = new Vector2f(x + width / 2, y + height / 2);
        this.width = width;
        this.height = height;
        cameraSpeed = 10;
        dynamicLock = new Vector2f(0, 0);
        visibleArea = new AABB(x, y, x + width, y + height);
    }

    public void addOffSetX(int offSet) {
        offset.x -= offSet;
    }

    public void addOffSetY(int offSet) {
        offset.y -= offSet;
    }

    public void update() {
        cameraSpeed = 5;
        float targetX = target.getPosition().x;
        float targetY = target.getPosition().y;

        //cameraCenter.x += (targetX - cameraCenter.x)*cameraSpeed/100.0;
        //cameraCenter.y += (targetY - cameraCenter.y)*cameraSpeed/100.0;
        //cameraCenter.x = targetX;
        //cameraCenter.y = targetY;
        //position.x = (targetX+core.getInput().getMouseX())/2;
        //position.y = (targetY+core.getInput().getMouseY())/2;
        //position.x = target.getX()-(core.getInput().getMouseXNonScale()+target.getX())/4;
        //position.y = target.getY()-(core.getInput().getMouseYNonScale()+target.getY())/4;
        if (!dynamic) {
            cameraCenter.x += (targetX - cameraCenter.x) * cameraSpeed / 100.0;
            cameraCenter.y += (targetY - cameraCenter.y) * cameraSpeed / 100.0;
        } else {
            cameraCenter.x += targetX - cameraCenter.x;
            cameraCenter.y += targetY - cameraCenter.y;
        }

        offset.x = -(cameraCenter.x - width / 2);
        offset.y = -(cameraCenter.y - height / 2);

        

        
        dynamicLock.x = -(targetX + core.getInput().getMouseX()) / 2 + width / 2;
        dynamicLock.y = -(targetY + core.getInput().getMouseY()) / 2 + height / 2;
        if(dynamic){
            visibleArea.reset(-dynamicLock.x, -dynamicLock.y, -dynamicLock.x  + width+width/3 , -dynamicLock.y  + height );//width/3 is hardcoded for now will fix later
            //System.out.println(visibleArea.toString());
        }
        else{
            visibleArea.reset(-offset.x, -offset.y, -offset.x  + width+width/3 , -offset.y  + height);
            
           //System.out.println(visibleArea.toString());
        }
        //System.out.println("topLeft camera: "+dynamicLock.toString());
       // System.out.println("width: "+width);
       // System.out.println("height: "+height);

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
        widthScale = core.getWidth() / width;
        heightScale = core.getHeight() / height;
        backUpHeightScale = heightScale;
        backUpWidthScale = widthScale;
        if (widthScale > heightScale) {
            widthScale = heightScale;
        } else if (heightScale > widthScale) {
            heightScale = widthScale;
        }

    }

    public float getX() {
        if(dynamic)
            return dynamicLock.x;
        return offset.x;
    }

    public float getY() {
        if(dynamic)
            return dynamicLock.y;
        return offset.y;
    }

    public float getDynamicX() {
        return dynamicLock.x;
    }

    public float getDynamicY() {
        return dynamicLock.y;
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
        widthScale = core.getWidth() / width;
        heightScale = core.getHeight() / height;
        backUpHeightScale = heightScale;
        backUpWidthScale = widthScale;
        if (widthScale > heightScale) {
            widthScale = heightScale;
        } else if (heightScale > widthScale) {
            heightScale = widthScale;
        }
    }

    public void setCameraSpeed(float cameraSpeed) {
        if (cameraSpeed <= 100 && cameraSpeed >= 0) {
            this.cameraSpeed = cameraSpeed;
        }
    }

    public boolean isDynamic() {
        return dynamic;
    }
}
