/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 *
 * @author Zeus
 */
public class Input{
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
        
        System.out.println("Mouse: "+Mouse.isCreated());
    }
    
    public void update(){
        previousKeys = keys.clone();
        previousMouseButtons = mouseButtons.clone();
        mouseX = Mouse.getX();
        mouseY = Display.getHeight()-Mouse.getY();
        
    }

    
    
    
    
    public boolean isKeyJustPressed(int code){
        return Keyboard.isKeyDown(code);
    }
    
    public boolean isKeyJustReleased(int code){
        return !Keyboard.isKeyDown(code);
    }
    
    public boolean isKeyPressed(int code){
        
        return Keyboard.isKeyDown(code);
    }
    
    public boolean isKeyReleased(int code){
        return !Keyboard.isKeyDown(code);
    }
    
    public boolean isMouseButtonPressed(int button){
        return Mouse.isButtonDown(button);
    }
    
    public boolean isMouseButtonReleased(int button){
        return !Mouse.isButtonDown(button);
    }
    
     public int getMouseX(){
        return (int)(mouseX/core.getObjectManager().getCamera().getWidthScale() - core.getObjectManager().getCamera().getX());
    }
    
    public int getMouseY(){
        return (int)(mouseY/core.getObjectManager().getCamera().getHeightScale() - core.getObjectManager().getCamera().getY());
    }
    
}
