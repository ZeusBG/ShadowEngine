/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import components.ObjectManager;
import gameObjects.DynamicGameObject;
import gameObjects.Projectile;
import gameObjects.StaticGameObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collections;
import utils.GeometryUtil;
import utils.SortPointsClockWise;

/**
 *
 * @author Zeus
 */
public class Renderer {
    private Core core;
    //private Graphics2D g2d;
    private Graphics2D g2d;
    private BufferStrategy bs;
    
    
    public Renderer(Core core){
        this.core = core;
        g2d = (Graphics2D) core.getWindow().getBufferStrategy().getDrawGraphics();
        bs =core.getWindow().getBufferStrategy();
    }
    
    public void render(){
        ObjectManager objectsToRender = core.getObjectManager();
        //if(dynamicLights) - castvam senki kum vsi4ko(kur6umi,hora);
        //castvam visibilityto
        
        //setvam clip
        //render objects
        
        clear();
        castShadows(objectsToRender.getPlayer().getCurrentPosition());
        
        //setClip
        if(core.getObjectManager().getPlayer()!=null)
            core.getObjectManager().getPlayer().render(core, this);
        
        for(DynamicGameObject obj : core.getObjectManager().getDynamicObjects()){
            obj.render(core,this);
        }
        for(Projectile obj : core.getObjectManager().getProjectiles()){
            obj.render(core,this);
        }
        
        for(StaticGameObject obj : core.getObjectManager().getStaticObjects()){
            
            obj.render(core,this);
        }
        
        
        bs.show();
    }
    
    public void drawCircle(int x, int y,int r){
        g2d.draw(new Ellipse2D.Double(x-r,y-r,r*2,r*2));
    }
    
    public void clear(){
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, core.getWidth(), core.getHeight());
        g2d.setColor(Color.BLACK);
    }
    
    public void castShadows(Point2D.Double lightSource){
        
        ArrayList<Point2D.Double> points = core.getObjectManager().getPoints();
        ArrayList<Line2D.Double> walls = core.getObjectManager().getLines();
        drawSight(lightSource,points,walls);
        
    }
    
    private void drawSight(Point2D.Double lightSource,ArrayList<Point2D.Double> points,ArrayList<Line2D.Double> segments){
        
        Area light1 = new Area(findIntersectionPoints(lightSource,points,segments));
        //g2d.setColor(Color.white);
        
        float radius = 400;
        float[] dist = {0.0f, 0.4f};
        Color[] colors = {new Color(1.0f, 1.0f, 1.0f, 1.0f), new Color(0.0f, 0.0f, 0.0f, 1.0f)};
        float[] dist2 = {0.1f, 0.3f};
        
        Polygon screen = new Polygon();
        
        screen.addPoint(0,0);
        screen.addPoint(0,core.getHeight());
        screen.addPoint(core.getWidth(),core.getHeight());
        screen.addPoint(core.getWidth(),0);
        Area mask = new Area(screen);
        //if(light1.npoints>0){
            g2d.setColor(Color.BLACK);
            /*Path2D.Double sight = new Path2D.Double();
            sight.moveTo(intersectionPoints.get(0).x, intersectionPoints.get(0).y);
            for(int i = 1;i<=intersectionPoints.size();i++){
                sight.lineTo(intersectionPoints.get(i%intersectionPoints.size()).x, intersectionPoints.get(i%intersectionPoints.size()).y);
            }
            */
            mask.subtract(new Area(light1));
            g2d.setColor(Color.black);
            g2d.fill(mask);
            
            g2d.setColor(Color.WHITE);
            //g2d.setPaint(new RadialGradientPaint(lightSource, radius, dist2, colors));
            
            Area lightRadius = new Area(new Ellipse2D.Double(lightSource.x-radius,lightSource.y-radius,2*radius,2*radius));
            light1.intersect(lightRadius);
            //g2d.setColor(Color.black);
            g2d.fill(light1);
            g2d.setPaint(null);
            //g2d.setClip(light1);

    }
    
    
    
    public Polygon findIntersectionPoints(Point2D.Double lightSource,ArrayList<Point2D.Double> points,ArrayList<Line2D.Double> segments){
        
        
        ArrayList<Point2D.Double> intersectionPoints = new ArrayList<>();
        ArrayList<Point2D.Double> sPoints = new ArrayList<>();
        //Point2D.Double playerPoint = lightSource;
        for(Point2D.Double cPoint : points){

            Line2D.Double tmp = new Line2D.Double(lightSource,cPoint);
            double currentDistance = 10000;
            Point2D.Double tmpPoint = null;
            
            for(Line2D.Double line : segments){
                Point2D.Double currentIntersection = GeometryUtil.getIntersection2(tmp,line);
                if(currentIntersection!=null && GeometryUtil.getDistance(currentIntersection, lightSource)<currentDistance){     
                    tmpPoint = currentIntersection;
                    currentDistance = GeometryUtil.getDistance(currentIntersection, lightSource);
                }
            }
            
            //if(tmpPoint!=null && tmpPoint.equals(cPoint)){
                addSecondaryPoints(cPoint,lightSource,sPoints);
            //}
            
        }
        
        Line2D.Double currentLine=null;
        Line2D.Double last = null;
        boolean newWall = false;
        Point2D.Double lastPoint = null;
        
        Collections.sort(sPoints,new SortPointsClockWise(lightSource));
        
        for(Point2D.Double cPoint : sPoints){
            
            Line2D.Double tmp = new Line2D.Double(lightSource,cPoint);
            double currentDistance = 10000;
            Point2D.Double tmpPoint = null;
            for(Line2D.Double line : segments){
                
                    //System.out.println("\n\nwtf !?!?!?!?!??!?!\n\n");
                Point2D.Double currentIntersection = GeometryUtil.getIntersection2(tmp,line);
                if(currentIntersection!=null && GeometryUtil.getDistance(currentIntersection, lightSource)<currentDistance){       
                    tmpPoint = currentIntersection;
                    last = line;
                    currentDistance = GeometryUtil.getDistance(currentIntersection, lightSource);
                }
            }
            if(tmpPoint!=null){
                intersectionPoints.add(tmpPoint);
            }
            /*if(last!=null && !last.equals(currentLine)){
                currentLine = last;
                newWall = true;
            }
            
            else if (tmpPoint!=null)
                //lastPoint = new Point2D.Double(tmpPoint.x,tmpPoint.y);
                lastPoint = tmpPoint;
            if(tmpPoint!=null && newWall){
                intersectionPoints.add(tmpPoint);
                if(lastPoint!=null){
                    intersectionPoints.add(lastPoint);
                }
                
            }
            newWall = false;*/
            
        }
        //System.out.println("Intersection Points size: " + intersectionPoints.size());
        Collections.sort(intersectionPoints,new SortPointsClockWise(lightSource));
        
        Polygon light = new Polygon();
        //System.out.println("intersectionPoints size: "+intersectionPoints.size());
        for(int i=0;i<intersectionPoints.size();i++){
            light.addPoint((int)intersectionPoints.get(i).x,(int)intersectionPoints.get(i).y);
            
        }
        return light;
    }
    
    public void addSecondaryPoints(Point2D.Double p,Point2D.Double lightSource,ArrayList<Point2D.Double> sPoints){
        double x,y;
            
            y = p.x-lightSource.x;
            x = p.x-lightSource.y;
            double newX,newY;
            newX = p.x - x*0.0001;
            newY = p.y + y*0.0001;
            sPoints.add(new Point2D.Double(newX,newY));
            
            newX = p.x + x*0.0001;
            newY = p.y - y*0.0001;
            sPoints.add(new Point2D.Double(newX,newY));
            
    }
    
    public void drawLine(int x1,int y1,int x2, int y2){
        
        g2d.drawLine(x1, y1, x2, y2);
        
    }
    
    public void requestLine(int x1,int y1,int x2, int y2){
        Line2D.Double lineToBeDrawn = new Line2D.Double(x1,y1,x2,y2);
        Point2D.Double startPoint = new Point2D.Double(x1,y1);
        ArrayList<Line2D.Double> segments = core.getObjectManager().getLines();
        
        double currentDistance = 100000;
        Point2D.Double tmpPoint = null;
        for(Line2D.Double line : segments){
                Point2D.Double currentIntersection = GeometryUtil.getIntersection2(lineToBeDrawn,line);
                if(currentIntersection!=null && GeometryUtil.getDistance(currentIntersection, startPoint)<currentDistance){     
                    tmpPoint = currentIntersection;
                    currentDistance = GeometryUtil.getDistance(currentIntersection, startPoint);
                }
            }
        if(tmpPoint!=null){
            g2d.drawLine(x1, y1, (int)tmpPoint.x, (int)tmpPoint.y);
            //System.out.println("testing github");
        }
        else g2d.drawLine(x1, y1, x2, y2);
    }
    
    public void setColor(Color c){
        g2d.setColor(c);
    }
    
    public void drawTriangle(Point2D.Double a,Point2D.Double b,Point2D.Double c){
        Polygon p = new Polygon();
        p.addPoint((int)a.x, (int)a.y);
        p.addPoint((int)b.x, (int)b.y);
        p.addPoint((int)c.x, (int)c.y);
        g2d.fill(p);
    }
}
