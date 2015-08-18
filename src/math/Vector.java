/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

import java.awt.geom.Point2D;

/**
 *
 * @author Zeus
 */
public class Vector {
    public double x,y;
    
    
    
    public Vector(double _x, double _y){
        x = _x;
        y = _y;
    }
    
    public Vector(int _x, int _y){
        x = _x;
        y = _y;
    }
    
    public Vector(Vector v){
        x = v.x;
        y = v.y;
    }
    
    public Vector(Point2D.Double a, Point2D.Double b){
        x = b.x - a.x;
        y = b.y - a.y;
    }
    
    
    
    public void normalize(){
        double length = Math.sqrt(x*x + y*y);
        x = x/length;
        y = y/length;   
    }
    
    public void multiply(double scalar){
        x*=scalar;
        y*=scalar;
    }
    public String toString(){
        return String.format("Vector: %f %f\n",x,y);
    }
}
