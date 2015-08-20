/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import math.Vector;

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
        
        /*double r_px = ray.x1;
	double r_py = ray.y1;
	double r_dx = ray.x2-ray.x1;
	double r_dy = ray.y2-ray.y1;
	// SEGMENT in parametric: Point + Delta*T2
	double s_px = segment.x1;
	double s_py = segment.y1;
	double s_dx = segment.x2-segment.x1;
	double s_dy = segment.y2-segment.y1;
	// Are they parallel? If so, no intersect
	double r_mag = Math.sqrt(r_dx*r_dx+r_dy*r_dy);
	double s_mag = Math.sqrt(s_dx*s_dx+s_dy*s_dy);
	if(r_dx/r_mag==s_dx/s_mag && r_dy/r_mag==s_dy/s_mag){
		// Unit vectors are the same.
		return null;
	}
	// SOLVE FOR T1 & T2
	// r_px+r_dx*T1 = s_px+s_dx*T2 && r_py+r_dy*T1 = s_py+s_dy*T2
	// ==> T1 = (s_px+s_dx*T2-r_px)/r_dx = (s_py+s_dy*T2-r_py)/r_dy
	// ==> s_px*r_dy + s_dx*T2*r_dy - r_px*r_dy = s_py*r_dx + s_dy*T2*r_dx - r_py*r_dx
	// ==> T2 = (r_dx*(s_py-r_py) + r_dy*(r_px-s_px))/(s_dx*r_dy - s_dy*r_dx)
        
	double T2 = (r_dx*(s_py-r_py) + r_dy*(r_px-s_px))/(s_dx*r_dy - s_dy*r_dx);
	double T1 = (s_px+s_dx*T2-r_px)/r_dx;
	// Must be within parametic whatevers for RAY/SEGMENT
	if(T1<0 || T1>1) return null;
	if(T2<0 || T2>1) return null;
        
        Point2D.Double intersection = new Point2D.Double();
        intersection.x = r_px+r_dx*T1;
	intersection.y = r_py+r_dy*T1;
        return intersection;*/
    }
    

    public static double getDistance(Point2D.Double p1, Point2D.Double p2){
        
        return Math.sqrt((p2.x-p1.x)*(p2.x-p1.x)+(p2.y-p1.y)*(p2.y-p1.y));
    }

}
