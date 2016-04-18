/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import gameObjects.util.AABB;
import java.awt.Color;
import math.Vector2f;
import engine.render.Renderer;

/**
 *
 * @author Zeus
 */
public class RectangleButton extends AbstractButton{
    float x,y;
    float width;
    float height;
    private AABB aabb;

    public RectangleButton(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        aabb = new AABB(x,y,x+width,y+width);
    }
    
    
    public boolean isHovered(Vector2f mouseCursor){
        return aabb.contains(mouseCursor);
    }
    
    
    
    @Override
    public void render(Renderer r) {
        r.setColor(Color.red);
        r.drawRect(x, y, width, height);
    }

    @Override
    public void update() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onClick() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hover() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
