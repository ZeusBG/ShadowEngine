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
 * @author Nick
 */
public class Geometry {
    private AABB aabb;
    private ArrayList<Vector2f> points;
    private ArrayList<Line2f> lines;
    private Vector2f center;
    private Vector2f scale;
    private Vector2f offset;

    
    public Geometry(){
        
        aabb = new AABB();
        points = new ArrayList<>();
        lines = new ArrayList<>();
        center = new Vector2f();
        scale = new Vector2f(1,1);
        offset = new Vector2f(0,0);
    }

    public void addPoint(Vector2f point){
        if(!points.isEmpty()){
            lines.add(new Line2f(points.get(points.size()-1),point));
        }
        points.add(point);
        aabb.update(point);
        updateCenter();
    }
    
    public void rotate(float angle){
        
        
        for(Vector2f p : points){
            rotateSinglePoint(p, angle, center);
        }
        updateLines();
        updateAABB();
        
    }
    
    public void rotateAround(Vector2f pivot,float angle){
        
        for(Vector2f p : points){
            rotateSinglePoint(p, angle,pivot);
        }
        updateLines();
        updateCenter();
        updateAABB();
        
    }
    
    public void translate(Vector2f dir){
        
       for(Vector2f p : points){
            p.x += dir.x;
            p.y += dir.y;
        }
       center.x += dir.x;
       center.y += dir.y;
       updateLines();
       updateAABB();
        
    }
    
    public void scale(float scaleX, float scaleY){
        
        for(Vector2f p : points){
            scaleSinglePoint(p,scaleX,scaleY);
        }
        updateLines();
        updateAABB();
    }
    
    private void updateCenter(){
        center.x = 0;
        center.y = 0;
        for(Vector2f p : points){
            center.x += p.x;
            center.y += p.y;
        }
        
        center.x /= points.size();
        center.y /= points.size();
        
    }
    
    private void scaleSinglePoint(Vector2f p,float scaleX, float scaleY){
        p.x = p.x - center.x;
        p.y = p.y - center.y;

        p.x = center.x + p.x*scaleX;
        p.y = center.y + p.y*scaleY;
    }
    
    private void updateLines(){
        for(int i=0;i<points.size()-1;i++){
            lines.get(i).reset(points.get(i), points.get(i+1));
        }
    }
    
    private void rotateSinglePoint(Vector2f point,float angle, Vector2f pivot){
        
        if(point == null)
            return;
        
        float angleRadians = (float)Math.toRadians(angle);
        
        float sin = (float)Math.sin(angleRadians);
        float cos = (float)Math.cos(angleRadians);

        point.x -= pivot.x;
        point.y -= pivot.y;

        float xnew = point.x * cos - point.y * sin;
        float ynew = point.x * sin + point.y * cos;

        point.x = xnew + pivot.x;
        point.y = ynew + pivot.y;
    }
    
    private void updateAABB(){
        if(points.size()<1)
            return;
        float minX,minY,maxX,maxY;
        
        minX = points.get(0).x;
        minY = minX;
        maxX = minX;
        maxY = minX;
        
        for(int i=1;i<points.size();i++){
            if(minX>points.get(i).x)
                minX = points.get(i).x;
            if(maxX<points.get(i).x)
                maxX = points.get(i).x;
            if(minY>points.get(i).y)
                minY = points.get(i).y;
            if(maxY<points.get(i).y)
                maxY = points.get(i).y;
        }
        
        aabb.reset(minX, minY, maxX, maxY);
            
    }

    public AABB getAabb() {
        return aabb;
    }

    public ArrayList<Vector2f> getPoints() {
        return points;
    }

    public ArrayList<Line2f> getLines() {
        return lines;
    }

    public Vector2f getCenter() {
        return center;
    }

    public void setAabb(AABB aabb) {
        this.aabb = aabb;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

    public Vector2f getOffset() {
        return offset;
    }

    public void setOffset(Vector2f offset) {
        this.offset = offset;
    }
    
    
    
}
