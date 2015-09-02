/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author Zeus
 */
public class Input implements KeyListener,MouseListener,MouseMotionListener{
    private Core core;
    private boolean keys[];
    private boolean previousKeys[];
    
    private boolean mouseButtons[];
    private boolean previousMouseButtons[];
    
    private int mouseX,mouseY;
    
    public Input(Core core){
        this.core = core;
        keys = new boolean[256];
        previousKeys = new boolean[256];
        mouseButtons = new boolean[5];
        previousMouseButtons = new boolean[5];
        
        
        core.getWindow().getGameArea().addMouseListener(this);
        core.getWindow().getGameArea().addMouseMotionListener(this);
        core.getWindow().getGameArea().addKeyListener(this);
        
    }
    
    public void update(){
        previousKeys = keys.clone();
        previousMouseButtons = mouseButtons.clone();
    }

    
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseButtons[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseButtons[e.getButton()] = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX()+8;
        mouseY = e.getY()-25;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX()+8;
        mouseY = e.getY()-25;
    }
    
    public boolean isKeyJustPressed(int code){
        return keys[code] && !previousKeys[code];
    }
    
    public boolean isKeyJustReleased(int code){
        return !keys[code] && previousKeys[code];
    }
    
    public boolean isKeyPressed(int code){
        
        return keys[code];
    }
    
    public boolean isKeyReleased(int code){
        return !keys[code];
    }
    
    public boolean isMouseButtonPressed(int button){
        return mouseButtons[button];
    }
    
    public boolean isMouseButtonReleased(int button){
        return !mouseButtons[button];
    }
    
    public int getMouseX(){
        return (int)(mouseX/core.getWidthScale() - core.getObjectManager().getCamera().getX());
    }
    
    public int getMouseY(){
        return (int)(mouseY/core.getHeightScale() - core.getObjectManager().getCamera().getY());
    }
    
}
