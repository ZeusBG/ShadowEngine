/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
        
        Dimension s = new Dimension((int)(core.getWidth()),(int)(core.getHeight()));
        gameArea = new JPanel();
        gameArea.setPreferredSize(s);
        gameArea.setMaximumSize(s);
        
        gameArea.setBackground(Color.WHITE);
        gameArea.setFocusable(true);
        gameArea.requestFocusInWindow();
        
        frame = new JFrame(core.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setLayout(new BorderLayout());
        frame.add(gameArea,BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.createBufferStrategy(2);
        g = gameArea.getGraphics();
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
    
    
}
