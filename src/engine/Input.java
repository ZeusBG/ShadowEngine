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
public class Input {

    private Core core;
    private boolean keys[];
    private boolean previousKeys[];

    private boolean mouseButtons[];
    private boolean previousMouseButtons[];
    
    private float transformedMouseX,transformedMouseY;
    private float mouseX, mouseY;

    public Input(Core core) {
        this.core = core;
        keys = new boolean[256];
        previousKeys = new boolean[256];
        mouseButtons = new boolean[256];
        previousMouseButtons = new boolean[256];
    }

    public void update() {
        previousKeys = keys.clone();
        previousMouseButtons = mouseButtons.clone();
        
        Display.processMessages();
        updateKeyboard();
        updateMouse();

        
    }
    
    private void updateKeyboard(){
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                int key = Keyboard.getEventKey();
                keys[key] = true;
            } else {
                int key = Keyboard.getEventKey();
                keys[key] = false;
            }
        }
    }
    
    private void updateMouse(){
        while (Mouse.next()) {
            if (Mouse.getEventButtonState()) {
                int button = Mouse.getEventButton();

                if (button >= 0) {
                    mouseButtons[button] = true;
                }
            } else {
                int button = Mouse.getEventButton();

                if (button >= 0) {
                    mouseButtons[button] = false;
                }
            }
        }
        mouseX = Mouse.getX();
        mouseY = Display.getHeight() - Mouse.getY();
        
        transformedMouseX = (mouseX / core.getCamera().getWidthScale() - core.getCamera().getX());
        transformedMouseY = (mouseY / core.getCamera().getHeightScale() - core.getCamera().getY());
    }
    
    public boolean isKeyJustPressed(int code) {
        return keys[code] && !previousKeys[code];
    }

    public boolean isKeyJustReleased(int code) {
        return !keys[code] && previousKeys[code];
    }

    public boolean isKeyPressed(int code) {
        return keys[code];
    }

    public boolean isKeyReleased(int code) {
        return !keys[code];
    }

    public boolean isMouseButtonPressed(int button) {
        return mouseButtons[button];
    }

    public boolean isMouseButtonReleased(int button) {
        return !mouseButtons[button];
    }

    public boolean isMouseButtonJustPressed(int button) {
        return mouseButtons[button] && !previousMouseButtons[button];
    }

    public boolean isMouseButtonJustReleased(int button) {
        return !mouseButtons[button] && previousMouseButtons[button];
    }

    public float getMouseX() {
        return transformedMouseX;
    }

    public float getMouseY() {
        return transformedMouseY;
    }

}
