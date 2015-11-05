/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

/**
 *
 * @author Zeus
 */
public class Vector2f {

    public float x, y;
    
    public Vector2f(){
        x=0;
        y=0;
    }
    
    public Vector2f(float _x, float _y) {
        x = _x;
        y = _y;
    }

    public Vector2f(int _x, int _y) {
        x = _x;
        y = _y;
    }

    public Vector2f(Vector2f v) {
        x = v.x;
        y = v.y;
    }

    public Vector2f(Vector2f a, Vector2f b) {
        x = b.x - a.x;
        y = b.y - a.y;
    }

    public Vector2f normalize() {
        if(x==0 && y==0){
            return this;
        }
        float length = (float)Math.sqrt(x * x + y * y);
        x /= length;
        y /= length;
        return this;
    }

    public Vector2f multiply(float scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public String toString() {
        return String.format("Vector: %f %f\n", x, y);
    }

    public float dotProduct(Vector2f v) {
        return x * v.x + y * v.y;
    }

    public Vector2f substract(Vector2f v) {
        x -= v.x;
        y -= v.y;
        return this;
    }

    public Vector2f getPerpendicular() {
        return new Vector2f(y, -x);
    }

    public Vector2f getPerpendicular2() {
        return new Vector2f(-y, x);
    }

    public void reset(Vector2f a, Vector2f b) {

        x = b.x - a.x;
        y = b.y - a.y;
    }
    


    public Vector2f getPerpendicularCloserTo(Vector2f p, Vector2f startPoint) {
        //to be removed
        Vector2f tmp = new Vector2f(startPoint.x + y, startPoint.y - x);
        Vector2f tmp2 = new Vector2f(startPoint.x - y, startPoint.y + x);
        if (GeometryUtil.getDistance(p, tmp) < GeometryUtil.getDistance(p, tmp2)) {
            return new Vector2f(y, -x);
        } else {
            return new Vector2f(-y, x);
        }
    }
    
    

    public void reset() {
        x = 0;
        y = 0;
    }
    
    public float getAngle(){
        return (float)((Math.atan2(x, y)*180/Math.PI)+360)%360;
    }
    
    public float getAngleInRadians(){
        return (float)((Math.atan2(x, y)+6.28f)%6.28f);
    }
    
    public Vector2f getOppositeVector(){
        return new Vector2f(-x,-y);
    }

    public void add(Vector2f v){
        x+=v.x;
        y+=v.y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vector2f other = (Vector2f) obj;
        return this.x == other.x && this.y == other.y;
    }
    
    
}
