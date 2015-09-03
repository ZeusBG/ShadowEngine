/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import render.Renderer;
import components.ObjectManager;
import components.Physics;
import components.SoundManager;
import gameObjects.GameObject;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private int width = 800,height = 600;
    private float widthScale = 1.0f;
    private float heightScale = 1.0f;
    private String title = "My engine v1.0";
    private ObjectManager objManager;
    private Renderer renderer;
    private SoundManager soundManager;
    private long timeStarted;
    private final int defaultWidthForScale = 800;
    private final int defaultHeightForScale = 600;
    
    private double frameCap = 1.0/60.0;
    private boolean isRunning = false;
    
    
    public Core(AbstractGame game) {
        this.game = game;
    }

   
    public void init(){
        
        thread = new Thread(this);
        
        
        objManager = new ObjectManager(this);
        
        
        window = new Window(this);
        input = new Input(this);
        renderer = new Renderer(this);
        physics = new Physics(this);
        soundManager = new SoundManager();
        widthScale=width/defaultWidthForScale;
        heightScale = height/defaultHeightForScale;
    }
    
    public void start(){
        if(isRunning)
            return;
        
        timeStarted = System.currentTimeMillis();
        thread.run();
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
                objManager.update();
                
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
            System.out.println("Time for a frame : " + (System.currentTimeMillis()-FPSCounter));
            
            
        }
        cleanUp();
        
    }

    public Physics getPhysics() {
        return physics;
    }
    
    public void handleInput(){
        
    }
    
    public void cleanUp(){
        window.cleanUp();
    }
    
    public void setSize(int width, int height){
        widthScale = width/defaultWidthForScale;
        heightScale = height/defaultHeightForScale;
        //window.setSize(width, height);
        
        this.width = width;
        this.height = height;
        //window = new Window(this);
        //input = new Input(this);
        //renderer = new Renderer(this);
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

    public float getWidthScale() {
        return widthScale;
    }

    public float getHeightScale() {
        return heightScale;
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
