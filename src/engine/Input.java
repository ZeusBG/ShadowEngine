/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;


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
    
    private float mouseX,mouseY;
    
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
    
     public float getMouseX(){
        return (float)(mouseX/core.getScene().getCamera().getWidthScale() - core.getScene().getCamera().getX());
    }
    
    public float getMouseY(){
        return (float)(mouseY/core.getScene().getCamera().getHeightScale() - core.getScene().getCamera().getY());
    }
    
}
