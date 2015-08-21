/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import components.ObjectManager;
import components.Physics;
import components.SoundManager;
import gameObjects.GameObject;
import gameObjects.Weapon;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.AK;
import test.Player;
import test.Wall;

/**
 *
 * @author Zeus
 */
public class Core implements Runnable{
    private Thread thread;
    private AbstractGame game;
    private Window window;
    private Input input;
    private Physics physics;
    private int width = 640,height = 480;
    private float scale = 2.0f;
    private String title = "My engine v1.0";
    private ObjectManager objManager;
    private Renderer renderer;
    private SoundManager soundManager;
    private long timeStarted;
    
    private double frameCap = 1.0/60.0;
    private boolean isRunning = false;
    
    
    public Core(AbstractGame game) {
        this.game = game;
    }

   
    public void init(){
        window = new Window(this);
        thread = new Thread(this);
        input = new Input(this);
        physics = new Physics(this);
        objManager = new ObjectManager();
        renderer = new Renderer(this);
        soundManager = new SoundManager();
    }
    
    public void start(){
        if(isRunning)
            return;
        
        timeStarted = System.currentTimeMillis();
        test();
        thread.run();
    }
    
    private void test(){
        Player player = new Player();
        player.setCurrentPosition(new Point2D.Double(50,50));
        player.setNextPosition(new Point2D.Double(50,50));
        addObject(player);
        
        Weapon wep = new AK(0,0,null);
        addObject(wep);
        player.addWeapon(wep);
        
        
        Wall w = new Wall(0,0);
        w.addPoint(new Point2D.Double(60,30));
        w.addPoint(new Point2D.Double(100,160));
        w.addPoint(new Point2D.Double(100,200));
        w.addPoint(new Point2D.Double(200,200));
        
        addObject(w);
        w = new Wall(0,0);
        w.addPoint(new Point2D.Double(0,0));
        w.addPoint(new Point2D.Double(width+100,0));
        w.addPoint(new Point2D.Double(width+100,height+100));
        w.addPoint(new Point2D.Double(0,height+100)); 
        w.addPoint(new Point2D.Double(0,0));
        addObject(w);
    }
    
    public void stop(){
        if(!isRunning)
            return;
        isRunning = false;
    }

    @Override
    public void run() 
    {
        isRunning = true;
        
        double FPSCounter;
        
        double firstTime = 0;
        double lastTime = System.nanoTime()/1000000000.0;
        double passedTime = 0;
        double unprocessedTime = 0;
        
       
        while(isRunning)
        {
            FPSCounter = System.currentTimeMillis();
            //System.out.println("asdasd");
            boolean render = false;
           
            firstTime = System.nanoTime()/1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;
            unprocessedTime +=passedTime;
            input.update();
            while(unprocessedTime >= frameCap)
            {
                int count = 0;
                //System.out.printf("inner While - %d\n",count++);
                
                //game.update(this,(float) frameCap);
                game.update(this, (float)frameCap);
                physics.update((float)frameCap);
                
                
                unprocessedTime-=frameCap;
                render = true;
            }
            
            if(render)
            {
                //clear screen
                //game.render(this,r)
                //window.update();
                
                
                game.render(this,renderer);
                renderer.clear();
                
            }
            else
            {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //System.out.println("Time for a frame : " + (System.currentTimeMillis()-FPSCounter));
            
            
        }
        cleanUp();
        
    }
    
    public void handleInput(){
        
    }
    
    public void cleanUp(){
        window.cleanUp();
    }
    
     public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getScale() {
        return scale;
    }

    public String getTitle() {
        return title;
    }

    public Window getWindow() {
        return window;
    }
    
    public ObjectManager getObjectManager(){
        return objManager;
    }

    public Input getInput() {
        return input;
    }
    
    public void addObject(GameObject obj){
        System.out.println("adding "+obj.getType());
        objManager.addObject(obj);
    }
    
    public SoundManager getSoundManager(){
        return soundManager;
    }
}
