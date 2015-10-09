/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.geom.Point2D;
import java.util.Comparator;

/**
 *
 * @author Zeus
 */
public class SortPointsClockWise implements Comparator<Point2D.Float>{
        
        private Point2D.Float center;
        public SortPointsClockWise(Point2D.Float _center){
            center = _center;
        }
        @Override
        public int compare(Point2D.Float o1, Point2D.Float o2) {
            if(o1.equals(o2)){
                return 0;
            }
            if (o1.x - center.x >= 0 && o2.x - center.x < 0)
                return 1;
            if (o1.x - center.x < 0 && o2.x - center.x >= 0)
                return -1;
            if (o1.x - center.x == 0 && o2.x - center.x == 0) {
                if (o1.y - center.y >= 0 || o2.y - center.y >= 0)
                    if (o1.y > o2.y)
                        return 1;
                    else return -1;
                if (o2.y > o1.y)
                    return 1;
                else return -1;
            }

            // compute the cross product of vectors (center -> a) x (center -> b)
            double det = (o1.x - center.x) * (o2.y - center.y) - (o2.x - center.x) * (o1.y - center.y);
            if (det < 0)
                return 1;
            if (det > 0)
                return -1;

            // points a and b are on the same line from the center
            // check which point is closer to the center
            double d1 = (o1.x - center.x) * (o1.x - center.x) + (o1.y - center.y) * (o1.y - center.y);
            double d2 = (o2.x - center.x) * (o2.x - center.x) + (o2.y - center.y) * (o2.y - center.y);
            if(d1 > d2)
                return 1;
            else return -1;
        }
    }