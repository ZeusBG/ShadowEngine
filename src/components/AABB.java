/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author Zeus
 */
public class AABB {
    
    private double minX, minY;
    private double maxX, maxY;
    private Point2D.Double center;
    private Line2D.Double[] lines;

    public AABB(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        center = new Point2D.Double(minX+(maxX-minX)/2,minY+(maxY-minY)/2);
        lines = new Line2D.Double[4];
        lines[0] = new Line2D.Double(minX,minY,maxX,minY);
        lines[1] = new Line2D.Double(maxX,minY,maxX,maxY);
        lines[2] = new Line2D.Double(maxX,maxY,minX,maxY);
        lines[3] = new Line2D.Double(minX,maxY,minX,minY);
    }
    
    public AABB() {
        minX = 100000;
        minY = 100000;
        maxX = -100000;
        maxY = -100000;
        lines = new Line2D.Double[4];
        for(int i=0;i<4;i++){
            lines[i] = new Line2D.Double();
        }
        center = new Point2D.Double();
        
    }
    
    public AABB(AABB aabb) {
        minX = aabb.minX;
        maxX = aabb.maxX;
        minY = aabb.minY;
        maxY = aabb.maxY;
        center = new Point2D.Double(aabb.center.x, aabb.center.y);
        lines = new Line2D.Double[4];
        lines[0] = new Line2D.Double(minX,minY,maxX,minY);
        lines[1] = new Line2D.Double(maxX,minY,maxX,maxY);
        lines[2] = new Line2D.Double(maxX,maxY,minX,maxY);
        lines[3] = new Line2D.Double(minX,maxY,minX,minY);
    }
    
    
    public AABB(ArrayList<Point2D.Double> points){
        if (points.isEmpty()) {
            return;
        }
        minX = points.get(0).x;
        minY = points.get(0).y;
        maxX = points.get(0).x;
        maxY = points.get(0).y;

        for (Point2D.Double p : points) {
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

        center = new Point2D.Double(minX+(maxX-minX)/2,minY+(maxY-minY)/2);
        lines = new Line2D.Double[4];
        lines[0] = new Line2D.Double(minX,minY,maxX,minY);
        lines[1] = new Line2D.Double(maxX,minY,maxX,maxY);
        lines[2] = new Line2D.Double(maxX,maxY,minX,maxY);
        lines[3] = new Line2D.Double(minX,maxY,minX,minY);
        
    }

    

    boolean intersect(AABB b) {
        return !(maxX < b.getMinX() || b.getMaxX() < minX || maxY < b.getMinY() || b.getMaxY() < minY);
    }
    
    boolean contains(AABB b) {
        
        return (minX<b.getMinX() && minY<b.getMinY() && maxY>b.getMaxY() && maxX>b.getMaxX());
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public Point2D.Double getCenter() {
        return center;
    }

    public Line2D.Double[] getLines() {
        return lines;
    }

    
    public void update(Point2D.Double p){
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
            center.x =minX+(maxX-minX)/2;
            center.y = minY+(maxY-minY)/2;
            
            lines[0].x1=minX; lines[0].y1=minY; lines[0].x2=maxX; lines[0].y2=minY;
            
            lines[1].x1=maxX; lines[1].y1=minY; lines[1].x2=maxX; lines[1].y2=maxY;
            lines[2].x1=maxX; lines[2].y1=maxY; lines[2].x2=minX; lines[2].y2=maxY;
            lines[3].x1=minX; lines[3].y1=maxY; lines[3].x2=minX; lines[3].y2=minY;
        }
        
    }
    
    @Override
    public String toString() {
        return String.format("AABB: %.2f,%.2f,%.2f,%.2f", minX, minY, maxX, maxY);
    }

    public void createAABB(ArrayList<Point2D.Double> points) {
        double minX, minY, maxX, maxY;
        if (points.isEmpty()) {
            return;
        }
        minX = points.get(0).x;
        minY = points.get(0).y;
        maxX = points.get(0).x;
        maxY = points.get(0).y;

        for (Point2D.Double p : points) {
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
    
    public void reset(double minX,double minY,double maxX,double maxY){
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        center.x =minX+(maxX-minX)/2;
        center.y = minY+(maxY-minY)/2;
        lines[0].x1=minX; lines[0].y1=minY; lines[0].x2=maxX; lines[0].y2=minY;
        lines[1].x1=maxX; lines[1].y1=minY; lines[1].x2=maxX; lines[1].y2=maxY;
        lines[2].x1=maxX; lines[2].y1=maxY; lines[2].x2=minX; lines[2].y2=maxY;
        lines[3].x1=minX; lines[3].y1=maxY; lines[3].x2=minX; lines[3].y2=minY;
    }
    
    public boolean contains(Point2D.Double p){
        return p.x>=minX-0.1 && p.y>=minY-0.1 && p.x<=maxX+0.1 && p.y<=maxY+0.1;
    }
    
    public boolean contains(Point2D p){
        return p.getX()>=minX-0.1 && p.getY()>=minY-0.1 && p.getX()<=maxX+0.1 && p.getY()<=maxY+0.1;
    }
    
}
