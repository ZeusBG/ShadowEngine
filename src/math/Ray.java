/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

import components.AABB;
import java.awt.geom.Point2D;
import utils.GeometryUtil;

/**
 *
 * @author Zeus
 */
public class Ray {

    public float x1,y1,x2,y2;
    public Point2D.Float source;
    public Ray(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        source = new Point2D.Float(x1,y1);
    }
    
    
    public Ray(Point2D.Float from, Point2D.Float to){
        x1 = from.x;
        y1 = from.y;
        x2 = to.x;
        y2 = to.y;
        source = new Point2D.Float(x1,y1);
    }
    
    
    
    public boolean intersect(AABB aabb) {
        
        
        return GeometryUtil.checkIntersectionRayLine(this, aabb.getLines()[0]) || 
                GeometryUtil.checkIntersectionRayLine(this, aabb.getLines()[1]) ||
                GeometryUtil.checkIntersectionRayLine(this, aabb.getLines()[2]) || 
                GeometryUtil.checkIntersectionRayLine(this, aabb.getLines()[3]);

    }
}
