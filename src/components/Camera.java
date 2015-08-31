/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import gameObjects.DynamicGameObject;
import java.awt.geom.Point2D;

/**
 *
 * @author Zeus
 */
public class Camera {
    private DynamicGameObject target;
    private Point2D.Double position;
    private Point2D.Double cameraCenter;
    private double cameraSpeed;
    private int width,height;

    public Camera(){
        position = new Point2D.Double(0,0);
        width=0;
        height=0;
        cameraCenter = new Point2D.Double(0,0);
        cameraSpeed = 10;
    }
    
    public Camera(int x, int y, int width, int height) {
        position = new Point2D.Double(x,y);
        cameraCenter = new Point2D.Double(x+width/2,y+height/2);
        this.width = width;
        this.height = height;
        cameraSpeed = 10;
    }

    public void addOffSetX(int offSet){
        //if(x-offSet>=0){
            position.x-=offSet;
        //}
    }
    
    public void addOffSetY(int offSet){
        //if(y-offSet>=0){
            position.y-=offSet;
        //}
    }
    
    public void update(){
        double targetX = target.getCurrentPosition().x;
        double targetY = target.getCurrentPosition().y;
       
        cameraCenter.x += (targetX - cameraCenter.x)*cameraSpeed/100.0;
        cameraCenter.y += (targetY - cameraCenter.y)*cameraSpeed/100.0;
        position.x -= (targetX - cameraCenter.x)*cameraSpeed/100.0;
        position.y -= (targetY - cameraCenter.y)*cameraSpeed/100.0;
    }

    public DynamicGameObject getTarget() {
        return target;
    }

    public void setTarget(DynamicGameObject target) {
        this.target = target;
    }

    public Point2D.Double getPosition() {
        return position;
    }

    public void setPosition(Point2D.Double position) {
        this.position = position;
    }
    
 
    public int getWidth() {
        return width;
    }
    
    public double getX(){
        return position.x;
    }
    
    public double getY(){
        return position.y;
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