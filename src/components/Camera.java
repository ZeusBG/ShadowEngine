/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import engine.Core;
import gameObjects.DynamicGameObject;
import java.awt.geom.Point2D;
import utils.GeometryUtil;

/**
 *
 * @author Zeus
 */
public class Camera {
    private DynamicGameObject target;
    private Point2D.Double offset;
    private Point2D.Double cameraCenter;
    private double cameraSpeed;
    private int width,height;
    private Core core;
    private Point2D.Double dynamicLock;
    private boolean dynamic = false;

    public Camera(Core core){
        offset = new Point2D.Double(0,0);
        width=0;
        height=0;
        cameraCenter = new Point2D.Double(0,0);
        cameraSpeed = 10;
        this.core = core;
        dynamicLock = new Point2D.Double(0,0);
    }
    
    public Camera(int x, int y, int width, int height) {
        offset = new Point2D.Double(x,y);
        cameraCenter = new Point2D.Double(x+width/2,y+height/2);
        this.width = width;
        this.height = height;
        cameraSpeed = 10;
        dynamicLock = new Point2D.Double(0,0);
    }

    public void addOffSetX(int offSet){
        //if(x-offSet>=0){
            offset.x-=offSet;
        //}
    }
    
    public void addOffSetY(int offSet){
        //if(y-offSet>=0){
            offset.y-=offSet;
        //}
    }
    
    public void update(){
        cameraSpeed =5;
        double targetX = target.getCurrentPosition().x;
        double targetY = target.getCurrentPosition().y;
        
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
        dynamicLock.y = -(targetY+core.getInput().getMouseY())/2+width/2;
        
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

    public Point2D.Double getPosition() {
        return offset;
    }

    public void setPosition(Point2D.Double position) {
        this.offset = position;
    }
    
 
    public int getWidth() {
        return width;
    }

    public void setCore(Core core) {
        this.core = core;
    }
    
    public double getX(){
        return offset.x;
    }
    
    public double getY(){
        return offset.y;
    }
    
    public double getDynamicX(){
        return dynamicLock.x;
    }
    
    public double getDynamicY(){
        return dynamicLock.y;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getCameraSpeed() {
        return cameraSpeed;
    }

    public void setCameraSpeed(double cameraSpeed) {
        if(cameraSpeed<=100 && cameraSpeed>=0)
            this.cameraSpeed = cameraSpeed;
    }
}
