/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

import components.AABB;
import gameObjects.GameObject;
import java.util.ArrayList;
import math.Line2f;
import math.Ray;
import math.Vector2f;

/**
 *
 * @author Zeus
 */
public class GeometryUtil {

   
    
    
    
    public static boolean checkIntersectionRayLine(Ray ray, Line2f line){
        
        float s1_x, s1_y, s2_x, s2_y;
        s1_x = ray.x2 - ray.x1;     s1_y = ray.y2 - ray.y1;
        s2_x = line.getX2() - line.getX1();     s2_y = line.getY2() - line.getY1();

        float s, t;
        s = (-s1_y * (ray.x1 - line.getX1()) + s1_x * (ray.y1 - line.getY1())) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (ray.y1 - line.getY1()) - s2_y * (ray.x1 - line.getX1())) / (-s2_x * s1_y + s1_x * s2_y);

        
        return s >= 0 && s<=1 && t >= 0;
        
    }
    
    public static Vector2f getIntersectionRayLine(Ray ray, Line2f line){
        
        float s1_x, s1_y, s2_x, s2_y;
        s1_x = ray.x2 - ray.x1;     s1_y = ray.y2 - ray.y1;
        s2_x = line.getX2() - line.getX1();     s2_y = line.getY2() - line.getY1();

        float s, t;
        s = (-s1_y * (ray.x1 - line.getX1()) + s1_x * (ray.y1 - line.getY1())) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (ray.y1 - line.getY1()) - s2_y * (ray.x1 - line.getX1())) / (-s2_x * s1_y + s1_x * s2_y);

        
        if (s >= 0 && s<=1 && t >= 0)
        {
            Vector2f intersection = new Vector2f();
            intersection.x = ray.x1 + (t * s1_x);
            intersection.y = ray.y1 + (t * s1_y);
            return intersection;
        }
        
        return null;
        
    }
    
    public static boolean checkIntersectionLineAABB(Line2f line,AABB aabb){
        
        Line2f lines[] = aabb.getLines();
        for(int i=0;i<4;i++){
            if(line.intersectsLine(lines[i]))
                return true;
        }

        boolean containsP1 = aabb.contains(line.getP1());
        boolean containsP2 = aabb.contains(line.getP2());
        
        return (containsP1 && containsP2);
    }
    
    public static Vector2f getIntersectionLines(Line2f line1, Line2f line2){

        float s1_x, s1_y, s2_x, s2_y;
        s1_x = line1.getX2() - line1.getX1();     s1_y = line1.getY2() - line1.getY1();
        s2_x = line2.getX2() - line2.getX1();     s2_y = line2.getY2() - line2.getY1();

        float s, t;
        s = (-s1_y * (line1.getX1() - line2.getX1()) + s1_x * (line1.getY1() - line2.getY1())) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (line1.getY1() - line2.getY1()) - s2_y * (line1.getX1() - line2.getX1())) / (-s2_x * s1_y + s1_x * s2_y);

        
        if (s >= 0 && s<=1 && t >= 0 && t<=1)
        {
            // Collision detected
            Vector2f intersection = new Vector2f();
            intersection.x = line1.getX1() + (t * s1_x);
            intersection.y = line1.getY1() + (t * s1_y);

            return intersection;
        }
        
        return null;
    }

    public static float getDistance(Vector2f p1, Vector2f p2){
        
        return (float)Math.sqrt((p2.x-p1.x)*(p2.x-p1.x)+(p2.y-p1.y)*(p2.y-p1.y));
    }
    
    public static Vector2f getClosestIntersection(Line2f path,ArrayList<GameObject> objects){
        Vector2f intersection = null;
        float currentDistance = 100000;
        
        
        for(GameObject go: objects){
            
                for(Line2f line: go.getLines()){
                    Vector2f currentIntersection = GeometryUtil.getIntersectionLines(path, line);
                    if(currentIntersection!=null && GeometryUtil.getDistance(currentIntersection,path.getP1())<currentDistance){
                        currentDistance = GeometryUtil.getDistance(currentIntersection,path.getP1());
                        intersection = currentIntersection;
                    }
                }
            
        }
        return intersection;
    }
    
    public static Vector2f getClosestIntersection(Line2f path,ArrayList<GameObject> objects,Vector2f lineObjectHit,GameObject objectHit){
        Vector2f intersection = null;
        float currentDistance = 100000;
        
        
        for(GameObject go: objects){
            
                for(Line2f line: go.getLines()){
                    Vector2f currentIntersection = GeometryUtil.getIntersectionLines(path, line);
                    if(currentIntersection!=null && GeometryUtil.getDistance(currentIntersection,path.getP1())<currentDistance){
                        currentDistance = GeometryUtil.getDistance(currentIntersection,path.getP1());
                        intersection = currentIntersection;
                        objectHit = go;
                        lineObjectHit.x = line.getX2()-line.getX1();
                        lineObjectHit.y = line.getY2()-line.getY1();
                        
                    }
                }
            
        }
        return intersection;
    }
    
    public static boolean pointsEqual(Vector2f p1,Vector2f p2){
        return  p1.x==p2.x && p1.y == p2.y;
    }
    
    
    
}
