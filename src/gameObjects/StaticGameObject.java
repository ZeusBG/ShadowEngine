/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import utils.ObjectType;

/**
 *
 * @author Zeus
 */

/*This is the class that all static object should extend - walls,lamps etc. */

public abstract class StaticGameObject extends GameObject{
    
    protected boolean visible;
    protected boolean collidable;
    
    protected ArrayList<Point2D.Double> points;
    
    
    public StaticGameObject(int x, int y) {
        super(x, y, ObjectType.ENVIRONMENT);
        points = new ArrayList<>();
    }

    public ArrayList<Point2D.Double> getPoints(){
        return points;
    }
    
    
    
}
