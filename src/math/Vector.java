/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

import java.awt.geom.Point2D;
import utils.GeometryUtil;

/**
 *
 * @author Zeus
 */
public class Vector {

    public double x, y;

    public Vector(double _x, double _y) {
        x = _x;
        y = _y;
    }

    public Vector(int _x, int _y) {
        x = _x;
        y = _y;
    }

    public Vector(Vector v) {
        x = v.x;
        y = v.y;
    }

    public Vector(Point2D.Double a, Point2D.Double b) {
        x = b.x - a.x;
        y = b.y - a.y;
    }

    public Vector normalize() {
        double length = Math.sqrt(x * x + y * y);
        x = x / length;
        y = y / length;
        return this;
    }

    public Vector multiply(double scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public String toString() {
        return String.format("Vector: %f %f\n", x, y);
    }

    public double dotProduct(Vector v) {
        return x * v.x + y * v.y;
    }

    public Vector substract(Vector v) {
        x -= v.x;
        y -= v.y;
        return this;
    }

    public Vector getPerpendicular(){
        return new Vector(y,-x);
    }
    
    public Vector getPerpendicular2(){
        return new Vector(-y,x);
    }
    
    public Vector getPerpendicularCloserTo(Point2D.Double p,Point2D.Double startPoint) {

        Point2D.Double tmp = new Point2D.Double(startPoint.x + y, startPoint.y - x);
        Point2D.Double tmp2 = new Point2D.Double(startPoint.x - y, startPoint.y + x);
        if (GeometryUtil.getDistance(p, tmp) < GeometryUtil.getDistance(p, tmp2)) {
            return new Vector(y, -x);
        } else {
            return new Vector(-y, x);
        }
    }
}
