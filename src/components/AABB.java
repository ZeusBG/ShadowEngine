/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.util.ArrayList;
import math.Line2f;
import math.Vector2f;

/**
 *
 * @author Zeus
 */
public class AABB {
    
    private float minX, minY;
    private float maxX, maxY;
    private Vector2f center;
    private Line2f[] lines;
    public AABB(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        center = new Vector2f(minX+(maxX-minX)/2.0f,minY+(maxY-minY)/2.0f);
        lines = new Line2f[4];
        lines[0] = new Line2f(minX,minY,maxX,minY);
        lines[1] = new Line2f(maxX,minY,maxX,maxY);
        lines[2] = new Line2f(maxX,maxY,minX,maxY);
        lines[3] = new Line2f(minX,maxY,minX,minY);
    }
    
    public AABB() {
        minX = 100000;
        minY = 100000;
        maxX = -100000;
        maxY = -100000;
        lines = new Line2f[4];
        for(int i=0;i<4;i++){
            lines[i] = new Line2f();
        }
        center = new Vector2f();
        
    }
    
    public AABB(AABB aabb) {
        minX = aabb.minX;
        maxX = aabb.maxX;
        minY = aabb.minY;
        maxY = aabb.maxY;
        center = new Vector2f(aabb.center.x, aabb.center.y);
        lines = new Line2f[4];
        lines[0] = new Line2f(minX,minY,maxX,minY);
        lines[1] = new Line2f(maxX,minY,maxX,maxY);
        lines[2] = new Line2f(maxX,maxY,minX,maxY);
        lines[3] = new Line2f(minX,maxY,minX,minY);
    }
    
    
    public AABB(ArrayList<Vector2f> points){
        if (points.isEmpty()) {
            return;
        }
        minX = points.get(0).x;
        minY = points.get(0).y;
        maxX = points.get(0).x;
        maxY = points.get(0).y;

        for (Vector2f p : points) {
            if (minX > p.x) {
                minX = p.x;
            }
            if (maxX < p.x) {
                maxX = p.x;
            }
            if (minY > p.y) {
                minY = p.y;
            }
            if (maxY < p.y) {
                maxY = p.y;
            }
        }

        center = new Vector2f(minX+(maxX-minX)/2,minY+(maxY-minY)/2);
        lines = new Line2f[4];
        lines[0] = new Line2f(minX,minY,maxX,minY);
        lines[1] = new Line2f(maxX,minY,maxX,maxY);
        lines[2] = new Line2f(maxX,maxY,minX,maxY);
        lines[3] = new Line2f(minX,maxY,minX,minY);
        
    }

    public boolean intersect(AABB b) {
        return !(maxX < b.getMinX() || b.getMaxX() < minX || maxY < b.getMinY() || b.getMaxY() < minY);
    }
    
    public boolean contains(AABB b) {
        
        return (minX<b.getMinX() && minY<b.getMinY() && maxY>b.getMaxY() && maxX>b.getMaxX());
    }

    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public Vector2f getCenter() {
        return center;
    }

    public Line2f[] getLines() {
        return lines;
    }

    
    public void update(Vector2f p){
        boolean change = false;
        if (minX > p.x) {
            minX = p.x;
            change = true;
        }
        if (maxX < p.x) {
            maxX = p.x;
            change = true;
        }
        if (minY > p.y) {
            minY = p.y;
            change = true;
        }
        if (maxY < p.y) {
            maxY = p.y;
            change = true;
        }
        
        if(change){
            center.x =minX+(maxX-minX)/2.0f;
            center.y = minY+(maxY-minY)/2.0f;
            
        lines[0].setX1(minX); lines[0].setY1(minY); lines[0].setX2(maxX); lines[0].setY2(minY);
        lines[1].setX1(maxX); lines[1].setY1(minY); lines[1].setX2(maxX); lines[1].setY2(maxY);
        lines[2].setX1(maxX); lines[2].setY1(maxY); lines[2].setX2(minX); lines[2].setY2(maxY);
        lines[3].setX1(minX); lines[3].setY1(maxY); lines[3].setX2(minX); lines[3].setY2(minY);
        }
        
    }
    
    @Override
    public String toString() {
        return String.format("AABB: min(%.2f,%.2f),max(%.2f,%.2f)", minX, minY, maxX, maxY);
    }

    public void createAABB(ArrayList<Vector2f> points) {
        float minX, minY, maxX, maxY;
        if (points.isEmpty()) {
            return;
        }
        minX = points.get(0).x;
        minY = points.get(0).y;
        maxX = points.get(0).x;
        maxY = points.get(0).y;

        for (Vector2f p : points) {
            if (minX > p.x) {
                minX = p.x;
            }
            if (maxX < p.x) {
                maxX = p.x;
            }
            if (minY > p.y) {
                minY = p.y;
            }
            if (maxY < p.y) {
                maxY = p.y;
            }
        }
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;

    }
    
    public void reset(float minX,float minY,float maxX,float maxY){
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        center.x =minX+(maxX-minX)/2.0f;
        center.y = minY+(maxY-minY)/2.0f;
        lines[0].setX1(minX); lines[0].setY1(minY); lines[0].setX2(maxX); lines[0].setY2(minY);
        lines[1].setX1(maxX); lines[1].setY1(minY); lines[1].setX2(maxX); lines[1].setY2(maxY);
        lines[2].setX1(maxX); lines[2].setY1(maxY); lines[2].setX2(minX); lines[2].setY2(maxY);
        lines[3].setX1(minX); lines[3].setY1(maxY); lines[3].setX2(minX); lines[3].setY2(minY);
    }
    
    public boolean contains(Vector2f p){
        return p.x>=minX-0.1 && p.y>=minY-0.1 && p.x<=maxX+0.1 && p.y<=maxY+0.1;
    }
    
}
