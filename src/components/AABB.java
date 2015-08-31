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

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
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
            lines[0] = new Line2D.Double(minX,minY,maxX,minY);
            lines[1] = new Line2D.Double(maxX,minY,maxX,maxY);
            lines[2] = new Line2D.Double(maxX,maxY,minX,maxY);
            lines[3] = new Line2D.Double(minX,maxY,minX,minY);
            center = new Point2D.Double(minX+(maxX-minX)/2,minY+(maxY-minY)/2);
        }
        
    }
    
    @Override
    public String toString() {
        return String.format("AABB: %.2f,%.2f,%.2f,%.2f", minX, minY, maxX, maxY);
    }

    public void draw(Graphics2D g2d) {
        Polygon p = new Polygon();
        p.addPoint((int) minX, (int) minY);
        p.addPoint((int) maxX, (int) minY);
        p.addPoint((int) maxX, (int) maxY);
        p.addPoint((int) minX, (int) maxY);
        g2d.drawPolygon(p);
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

    public boolean contains(Point2D.Double p){
        return p.x>=minX-0.1 && p.y>=minY-0.1 && p.x<=maxX+0.1 && p.y<=maxY+0.1;
    }
    
    
    
}
