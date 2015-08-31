/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import gameObjects.GameObject;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import math.Ray;
import utils.GeometryUtil;

/**
 *
 * @author Zeus
 */
public class CollisionControl {

    public static Point2D.Double getClosestIntersection(Ray r, ArrayList<GameObject> objects) {
        Point2D.Double intersection = null;
        
        double currentDistance = 100000;
        for (GameObject obj : objects) {

            if (r.intersect(obj.getAabb())) {
                
                for (Line2D.Double line : obj.getLines()) {
                    Point2D.Double tmpPoint = GeometryUtil.getIntersectionRayLine(r, line);
                    if(currentDistance > GeometryUtil.getDistance(tmpPoint, r.source)){
                        currentDistance = GeometryUtil.getDistance(tmpPoint, r.source);
                        intersection = tmpPoint;
                    }
                }

            }
        }
        return intersection;
    }

}
