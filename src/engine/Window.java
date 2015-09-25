/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import org.lwjgl.opengl.PixelFormat;

/**
 *
 * @author Zeus
 */
public class Window {
    
    private JFrame frame;
    private JPanel gameArea;
    private Graphics g;
    private BufferStrategy bs;
    
    
    public Window(Core core)
    {
        
        try {
            Display.setDisplayMode(new DisplayMode(core.getWidth(), core.getHeight()));
            Display.setTitle("Shadow Engine");
            Display.create(new PixelFormat(0, 16, 1));

        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        try {
            Mouse.create();
        } catch (LWJGLException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Keyboard.create();
        } catch (LWJGLException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, core.getWidth(), core.getHeight(), 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        
        
    }
    public void update()
    {
        //nqkakuv render
        //bs.show();
    }

    public JPanel getGameArea() {
        return gameArea;
    }
    
    public Graphics getGraphics(){
        return g;
    }
    
    public void cleanUp()
    {
        g.dispose();
        bs.dispose();
        frame.dispose();
    }
    
    public BufferStrategy getBufferStrategy(){
        return frame.getBufferStrategy();
    }
    
    public void setSize(int width, int height){
        //frame.setSize(width, height);
        gameArea.setSize(width, height);
        Dimension s = new Dimension(width,height);
        gameArea.setPreferredSize(s);
    }
    
}
