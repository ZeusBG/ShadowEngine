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

    public float x, y;

    public Vector(float _x, float _y) {
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

    public Vector(Point2D.Float a, Point2D.Float b) {
        x = b.x - a.x;
        y = b.y - a.y;
    }

    public Vector normalize() {
        if(x==0 && y==0){
            return this;
        }
        float length = (float)Math.sqrt(x * x + y * y);
        x /= length;
        y /= length;
        return this;
    }

    public Vector multiply(float scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public String toString() {
        return String.format("Vector: %f %f\n", x, y);
    }

    public float dotProduct(Vector v) {
        return x * v.x + y * v.y;
    }

    public Vector substract(Vector v) {
        x -= v.x;
        y -= v.y;
        return this;
    }

    public Vector getPerpendicular() {
        return new Vector(y, -x);
    }

    public Vector getPerpendicular2() {
        return new Vector(-y, x);
    }

    public void reset(Point2D.Float a, Point2D.Float b) {

        x = b.x - a.x;
        y = b.y - a.y;
    }

    public Vector getPerpendicularCloserTo(Point2D.Float p, Point2D.Float startPoint) {

        Point2D.Float tmp = new Point2D.Float(startPoint.x + y, startPoint.y - x);
        Point2D.Float tmp2 = new Point2D.Float(startPoint.x - y, startPoint.y + x);
        if (GeometryUtil.getDistance(p, tmp) < GeometryUtil.getDistance(p, tmp2)) {
            return new Vector(y, -x);
        } else {
            return new Vector(-y, x);
        }
    }

    public void reset() {
        x = 0;
        y = 0;
    }
}
