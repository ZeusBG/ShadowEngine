/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import engine.render.Renderer;

/**
 *
 * @author Zeus
 */
public abstract class AbstractButton {
    protected boolean active;
    protected String text;
   
    public abstract void render(Renderer r);
    public abstract void update();
    public abstract void hover();
    public abstract void onClick();
}
