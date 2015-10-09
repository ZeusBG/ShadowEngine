/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import components.AABB;
import gameObjects.GameObject;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import math.Ray;

/**
 *
 * @author Zeus
 */
public class GeometryUtil {

   
    
    public static Point2D.Float getIntersection2(Line2D.Float ray, Line2D.Float line){
        float s1_x, s1_y, s2_x, s2_y;
        s1_x = ray.x2 - ray.x1;     s1_y = ray.y2 - ray.y1;
        s2_x = line.x2 - line.x1;     s2_y = line.y2 - line.y1;

        float s, t;
        s = (-s1_y * (ray.x1 - line.x1) + s1_x * (ray.y1 - line.y1)) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (ray.y1 - line.y1) - s2_y * (ray.x1 - line.x1)) / (-s2_x * s1_y + s1_x * s2_y);

        
        if (s >= 0 && s<=1 && t >= 0)
        {
            // Collision detected
            Point2D.Float intersection = new Point2D.Float();
            intersection.x = ray.x1 + (t * s1_x);
            intersection.y = ray.y1 + (t * s1_y);
            return intersection;
        }
        return null;
    }
    
    public static boolean checkIntersectionRayLine(Ray ray, Line2D.Float line){
        
        float s1_x, s1_y, s2_x, s2_y;
        s1_x = ray.x2 - ray.x1;     s1_y = ray.y2 - ray.y1;
        s2_x = line.x2 - line.x1;     s2_y = line.y2 - line.y1;

        float s, t;
        s = (-s1_y * (ray.x1 - line.x1) + s1_x * (ray.y1 - line.y1)) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (ray.y1 - line.y1) - s2_y * (ray.x1 - line.x1)) / (-s2_x * s1_y + s1_x * s2_y);

        
        return s >= 0 && s<=1 && t >= 0;
        
    }
    
    public static Point2D.Float getIntersectionRayLine(Ray ray, Line2D.Float line){
        
        float s1_x, s1_y, s2_x, s2_y;
        s1_x = ray.x2 - ray.x1;     s1_y = ray.y2 - ray.y1;
        s2_x = line.x2 - line.x1;     s2_y = line.y2 - line.y1;

        float s, t;
        s = (-s1_y * (ray.x1 - line.x1) + s1_x * (ray.y1 - line.y1)) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (ray.y1 - line.y1) - s2_y * (ray.x1 - line.x1)) / (-s2_x * s1_y + s1_x * s2_y);

        
        if (s >= 0 && s<=1 && t >= 0)
        {
            Point2D.Float intersection = new Point2D.Float();
            intersection.x = ray.x1 + (t * s1_x);
            intersection.y = ray.y1 + (t * s1_y);
            return intersection;
        }
        
        return null;
        
    }
    
    public static boolean checkIntersectionLineAABB(Line2D.Float line,AABB aabb){
        
        Line2D.Float lines[] = aabb.getLines();
        for(int i=0;i<4;i++){
            if(line.intersectsLine(lines[i]))
                return true;
        }

        boolean containsP1 = aabb.contains(line.getP1());
        boolean containsP2 = aabb.contains(line.getP2());
        
        return (containsP1 && containsP2);
    }
    
    public static Point2D.Float getIntersectionLines(Line2D.Float line1, Line2D.Float line2){
        
        float s1_x, s1_y, s2_x, s2_y;
        s1_x = line1.x2 - line1.x1;     s1_y = line1.y2 - line1.y1;
        s2_x = line2.x2 - line2.x1;     s2_y = line2.y2 - line2.y1;

        float s, t;
        s = (-s1_y * (line1.x1 - line2.x1) + s1_x * (line1.y1 - line2.y1)) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (line1.y1 - line2.y1) - s2_y * (line1.x1 - line2.x1)) / (-s2_x * s1_y + s1_x * s2_y);

        
        if (s >= 0 && s<=1 && t >= 0 && t<=1)
        {
            // Collision detected
            Point2D.Float intersection = new Point2D.Float();
            intersection.x = line1.x1 + (t * s1_x);
            intersection.y = line1.y1 + (t * s1_y);
            return intersection;
        }
        
        return null;
    }
    
    public static Point2D.Float getIntersectionLines(Line2D.Float line1, Line2D line2){
        
        float s1_x, s1_y, s2_x, s2_y;
        s1_x = line1.x2 - line1.x1;     s1_y = line1.y2 - line1.y1;
        s2_x = (float)(line2.getX2() - (line2.getX1())); 
        s2_y = (float)(line2.getY2() - line2.getY1());

        float s, t;
        s = (-s1_y * (line1.x1 - (float)line2.getX1()) + s1_x * (line1.y1 - (float)line2.getY1())) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (line1.y1 - (float)line2.getY1()) - s2_y * (line1.x1 - (float)line2.getX1())) / (-s2_x * s1_y + s1_x * s2_y);

        
        if (s >= 0 && s<=1 && t >= 0 && t<=1)
        {
            // Collision detected
            Point2D.Float intersection = new Point2D.Float();
            intersection.x = line1.x1 + (t * s1_x);
            intersection.y = line1.y1 + (t * s1_y);
            return intersection;
        }
        
        return null;
    }
    

    public static float getDistance(Point2D p1, Point2D p2){
        
        return (float)Math.sqrt((p2.getX()-p1.getX())*(p2.getX()-p1.getX())+(p2.getY()-p1.getY())*(p2.getY()-p1.getY()));
    }
    
    public static Point2D.Float getClosestIntersection(Line2D.Float path,ArrayList<GameObject> objects){
        Point2D.Float intersection = null;
        float currentDistance = 100000;
        
        
        for(GameObject go: objects){
            
                for(Line2D.Float line: go.getLines()){
                    Point2D.Float currentIntersection = GeometryUtil.getIntersectionLines(path, line);
                    if(currentIntersection!=null && GeometryUtil.getDistance(currentIntersection,path.getP1())<currentDistance){
                        currentDistance = GeometryUtil.getDistance(currentIntersection,path.getP1());
                        intersection = currentIntersection;
                    }
                }
            
        }
        return intersection;
    }
    
    public static boolean pointsEqual(Point2D.Float p1,Point2D.Float p2){
        return  p1.x==p2.x && p1.y == p2.y;
    }
    
    
    /*
    private static int CohenSutherland(Line2D.Double l,AABB aabb){
        //0-top,1-right,2-bottom,3-left,4-inside
        int code1[],code2[];        // initialised as being inside of clip window
        
        code1 = getCode(l.x1,l.y1,aabb);
        code2 = getCode(l.x1,l.y1,aabb);
        
	if((code1[0]==0 && code2[0]==0)  || (code1[0]==1 && code2[0]==1) || (code1[0]==2 && code2[0]==2) ||(code1[0]==3 && code2[0]==3)){
            return 1;
        }
        else if()
            return
        
    }
    
    private static int[] getCode(double x,double y,AABB aabb){
        int code[] = new int[2];
        code[0] = 4;
        code[0] = 4;
        
        if (x < aabb.getMinX())           // to the left of clip window
		code[0] = 3;
	else if (x > aabb.getMaxX())      // to the right of clip window
		code[0] = 1;
	if (y < aabb.getMinY())           // below the clip window
		code[1] = 2;
	else if (y > aabb.getMaxY())      // above the clip window
		code[1] = 0;
        
        return code;
    }
    
    */
}
