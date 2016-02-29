/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

import java.awt.geom.Line2D;

/**
 *
 * @author Zeus
 */
public class Line2f{
    //Wrapper class for Line2D.Float so that i can add some custom constructors with Vector2f and some setters and getters 
    //for easier work
    private Line2D.Float line;
    private Vector2f p1;
    private Vector2f p2;
    private Vector2f normal;
    private float x1,x2,y1,y2;
    public Line2f(float x1,float y1,float x2,float y2){
        line = new Line2D.Float(x1,y1,x2,y2);
        p1 = new Vector2f(x1,y1);
        p2 = new Vector2f(x2,y2);
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        normal = new Vector2f(y1-y2,-(x1-x2));
        normal.normalize();
    }
    
    public Line2f(Vector2f p1, Vector2f p2){
        line = new Line2D.Float(p1.x,p1.y,p2.x,p2.y);
        this.p1 = new Vector2f(p1);
        this.p2 = new Vector2f(p2);
        this.x1 = p1.x;
        this.x2 = p2.x;
        this.y1 = p1.y;
        this.y2 = p2.y;
        normal = new Vector2f(y1-y2,-(x1-x2));
        normal.normalize();
    }
    
    public Line2f(){
        p1 = new Vector2f();
        p2 = new Vector2f();
        line = new Line2D.Float();
        x1=0;y1=0;x2=0;y2=0;
        normal = new Vector2f();
    }
    
    public Line2D.Float getLine(){
        return line;
    }
    
    public Vector2f getP1(){
        return p1;
    }
    
    public Vector2f getP2(){
        return p2;
    }
    
    public boolean intersectsLine(Line2f line){
        return this.line.intersectsLine(line.getLine());
    }

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        line.x1 = x1;
        p1.x = x1;
        this.x1 = x1;
        resetNormal();
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        line.x2 = x2;
        p2.x = x2;
        this.x2 = x2;
        resetNormal();
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        line.y1 = y1;
        p1.y = y1;
        this.y1 = y1;
        resetNormal();
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        line.y2 = y2;
        p2.y = y2;
        this.y2 = y2;
        resetNormal();
    }
    
    public Vector2f asVector(){ 
        return new Vector2f(p1,p2);
    }
    
    private void resetNormal(){
        normal.x = y1-y2;
        normal.y = -(x1-x2);
        normal.normalize();
    }

    public Vector2f getNormal() {
        return normal;
    }
    
    public void reset(Vector2f p1,Vector2f p2){
        
        line.x1 = p1.x;
        line.x2 = p2.x;
        line.y1 = p1.y;
        line.y2 = p2.y;
        this.p1.x = p1.x;
        this.p2.x = p2.x;
        this.p1.y = p1.y;
        this.p2.y = p2.y;
        this.x1 = p1.x;
        this.x2 = p2.x;
        this.y1 = p1.y;
        this.y2 = p2.y;
        resetNormal();
        
    }    
   
}
