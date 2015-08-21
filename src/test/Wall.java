/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import engine.Core;
import engine.Renderer;
import gameObjects.StaticGameObject;
import java.awt.Color;
import java.awt.geom.Point2D;

/**
 *
 * @author Zeus
 */
public class Wall extends StaticGameObject{
    
    
    
    public Wall(int x, int y) {
        super(x, y);
    }

    public Wall(int x1, int y1,int x2,int y2) {
        super(x1, y1);
        
    }
    
    @Override
    public void update(Core gc, float dt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render(Core gc, Renderer r) {
        r.setColor(Color.RED);
        for(int i=0;i<points.size()-1;i++){
            r.drawLine((int)points.get(i).x,(int) points.get(i).y, (int)points.get(i+1).x, (int)points.get(i+1).y);
        }
        
        r.setColor(null);
    }
    
    public void addPoint(Point2D.Double p){
        points.add(new Point2D.Double(p.x,p.y));
    }
    
}
