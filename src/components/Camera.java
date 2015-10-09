/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import engine.Core;
import gameObjects.DynamicGameObject;
import java.awt.geom.Point2D;

/**
 *
 * @author Zeus
 */
public class Camera {
    private DynamicGameObject target;
    private Point2D.Float offset;
    private Point2D.Float cameraCenter;
    private float cameraSpeed;
    private float width,height;
    private Core core;
    private Point2D.Float dynamicLock;
    private boolean dynamic = false;
    private float widthScale = 1.0f;
    private float heightScale = 1.0f;

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

    public Camera(Core core){
        offset = new Point2D.Float(0,0);
        width=0;
        height=0;
        cameraCenter = new Point2D.Float(0,0);
        cameraSpeed = 10;
        this.core = core;
        dynamicLock = new Point2D.Float(0,0);
        widthScale = core.getWidth()/width;
        heightScale = core.getHeight()/height;
    }
    
    public Camera(int x, int y, int width, int height) {
        offset = new Point2D.Float(x,y);
        cameraCenter = new Point2D.Float(x+width/2,y+height/2);
        this.width = width;
        this.height = height;
        cameraSpeed = 10;
        dynamicLock = new Point2D.Float(0,0);
        
    }

    public void addOffSetX(int offSet){
            offset.x-=offSet;
    }
    
    public void addOffSetY(int offSet){
            offset.y-=offSet;
    }
    
    public void update(){
        cameraSpeed =5;
        float targetX = target.getCurrentPosition().x;
        float targetY = target.getCurrentPosition().y;
        
        //cameraCenter.x += (targetX - cameraCenter.x)*cameraSpeed/100.0;
        //cameraCenter.y += (targetY - cameraCenter.y)*cameraSpeed/100.0;
        //cameraCenter.x = targetX;
        //cameraCenter.y = targetY;
        
        //position.x = (targetX+core.getInput().getMouseX())/2;
        //position.y = (targetY+core.getInput().getMouseY())/2;
        //position.x = target.getX()-(core.getInput().getMouseXNonScale()+target.getX())/4;
        //position.y = target.getY()-(core.getInput().getMouseYNonScale()+target.getY())/4;
        if(!dynamic){
            cameraCenter.x += (targetX-cameraCenter.x)*cameraSpeed/100.0;
            cameraCenter.y += (targetY-cameraCenter.y)*cameraSpeed/100.0;
        }
        else{
            cameraCenter.x += targetX-cameraCenter.x;
            cameraCenter.y += targetY-cameraCenter.y;
        }
 
        offset.x = -(cameraCenter.x-width/2);
        offset.y = -(cameraCenter.y-height/2);
        
        
        
        dynamicLock.x = -(targetX+core.getInput().getMouseX())/2+width/2;
        dynamicLock.y = -(targetY+core.getInput().getMouseY())/2+height/2;
        
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

    public Point2D.Float getPosition() {
        return offset;
    }

    public void setPosition(Point2D.Float position) {
        this.offset = position;
    }
    
 
    public float getWidth() {
        return width;
    }

    public void setCore(Core core) {
        this.core = core;
        widthScale = core.getWidth()/width;
        heightScale = core.getHeight()/height;
        
        if(widthScale>heightScale){
            widthScale = heightScale;
        }
        else if(heightScale>widthScale){
            heightScale = widthScale;
        }
        
    }
    
    public float getX(){
        return offset.x;
    }
    
    public float getY(){
        return offset.y;
    }
    
    public float getDynamicX(){
        return dynamicLock.x;
    }
    
    public float getDynamicY(){
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
    
    public void zoomIn(){
        width*=0.99;
        height*=0.99;
        refreshScale();
    }
    
    public void zoomOut(){
        width/=0.99;
        height/=0.99;
        refreshScale();
    }
    
    public void refreshScale(){
        widthScale = core.getWidth()/width;
        heightScale = core.getHeight()/height;
        
        if(widthScale>heightScale){
            widthScale = heightScale;
        }
        else if(heightScale>widthScale){
            heightScale = widthScale;
        }
    }
    
    public void setCameraSpeed(float cameraSpeed) {
        if(cameraSpeed<=100 && cameraSpeed>=0)
            this.cameraSpeed = cameraSpeed;
    }
    
    public boolean isDynamic(){
        return dynamic;
    }
}
