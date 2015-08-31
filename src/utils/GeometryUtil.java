/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import math.Ray;

/**
 *
 * @author Zeus
 */
public class GeometryUtil {

   
    
    public static Point2D.Double getIntersection2(Line2D.Double ray, Line2D.Double line){
        double s1_x, s1_y, s2_x, s2_y;
        s1_x = ray.x2 - ray.x1;     s1_y = ray.y2 - ray.y1;
        s2_x = line.x2 - line.x1;     s2_y = line.y2 - line.y1;

        double s, t;
        s = (-s1_y * (ray.x1 - line.x1) + s1_x * (ray.y1 - line.y1)) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (ray.y1 - line.y1) - s2_y * (ray.x1 - line.x1)) / (-s2_x * s1_y + s1_x * s2_y);

        
        if (s >= 0 && s<=1 && t >= 0)
        {
            // Collision detected
            Point2D.Double intersection = new Point2D.Double();
            intersection.x = ray.x1 + (t * s1_x);
            intersection.y = ray.y1 + (t * s1_y);
            return intersection;
        }
        return null;
    }
    
    public static boolean checkIntersectionRayLine(Ray ray, Line2D.Double line){
        
        double s1_x, s1_y, s2_x, s2_y;
        s1_x = ray.x2 - ray.x1;     s1_y = ray.y2 - ray.y1;
        s2_x = line.x2 - line.x1;     s2_y = line.y2 - line.y1;

        double s, t;
        s = (-s1_y * (ray.x1 - line.x1) + s1_x * (ray.y1 - line.y1)) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (ray.y1 - line.y1) - s2_y * (ray.x1 - line.x1)) / (-s2_x * s1_y + s1_x * s2_y);

        
        return s >= 0 && s<=1 && t >= 0;
        
    }
    
    public static Point2D.Double getIntersectionRayLine(Ray ray, Line2D.Double line){
        
        double s1_x, s1_y, s2_x, s2_y;
        s1_x = ray.x2 - ray.x1;     s1_y = ray.y2 - ray.y1;
        s2_x = line.x2 - line.x1;     s2_y = line.y2 - line.y1;

        double s, t;
        s = (-s1_y * (ray.x1 - line.x1) + s1_x * (ray.y1 - line.y1)) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (ray.y1 - line.y1) - s2_y * (ray.x1 - line.x1)) / (-s2_x * s1_y + s1_x * s2_y);

        
        if (s >= 0 && s<=1 && t >= 0)
        {
            Point2D.Double intersection = new Point2D.Double();
            intersection.x = ray.x1 + (t * s1_x);
            intersection.y = ray.y1 + (t * s1_y);
            return intersection;
        }
        
        return null;
        
    }
    
    
    public static Point2D.Double getIntersectionLines(Line2D.Double ray, Line2D.Double line){
        
        double s1_x, s1_y, s2_x, s2_y;
        s1_x = ray.x2 - ray.x1;     s1_y = ray.y2 - ray.y1;
        s2_x = line.x2 - line.x1;     s2_y = line.y2 - line.y1;

        double s, t;
        s = (-s1_y * (ray.x1 - line.x1) + s1_x * (ray.y1 - line.y1)) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (ray.y1 - line.y1) - s2_y * (ray.x1 - line.x1)) / (-s2_x * s1_y + s1_x * s2_y);

        
        if (s >= 0 && s<=1 && t >= 0 && t<=1)
        {
            // Collision detected
            Point2D.Double intersection = new Point2D.Double();
            intersection.x = ray.x1 + (t * s1_x);
            intersection.y = ray.y1 + (t * s1_y);
            return intersection;
        }
        
        return null;
    }
    
    

    public static double getDistance(Point2D.Double p1, Point2D.Double p2){
        
        return Math.sqrt((p2.x-p1.x)*(p2.x-p1.x)+(p2.y-p1.y)*(p2.y-p1.y));
    }

}
