/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import render.Renderer;
import components.ObjectManager;
import components.Physics;
import components.sound.SoundManager;
import gameObjects.GameObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;


/**
 *
 * @author Zeus
 */
public class Core implements Runnable {

    private Thread thread;
    private AbstractGame game;
    private Window window;
    private Input input;
    private Physics physics;
    
    private String title = "My engine v1.0";
    private ObjectManager objManager;
    private Renderer renderer;
    private SoundManager soundManager;
    private long timeStarted;

    private float frameCap = 1.0f / 30.0f;
    private boolean isRunning = false;

    public Core(AbstractGame game) {
        this.game = game;
    }

    public void init() {
        
        int width = 1920;
        int height = 1080;
        window = new Window(width,height);
        thread = new Thread(this);
        objManager = new ObjectManager(this);
        
        
        input = new Input(this);
        renderer = new Renderer(this);
        physics = new Physics(this);
        soundManager = new SoundManager();

    }

    public void start() {
        if (isRunning) {
            return;
        }

        timeStarted = System.currentTimeMillis();
        thread.run();
    }

    public void stop() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
    }

    @Override
    public void run() {
        isRunning = true;

        double FPSCounter;

        double newCycleTime = 0;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime = 0;
        double unprocessedTime = 0;
        long testTime;
        int count = 0;
        while (isRunning) {
            count=0;
            //count=0;
            if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Display.isCloseRequested())
                System.exit(0);
            FPSCounter = System.nanoTime();
            boolean render = false;

            newCycleTime = System.nanoTime() / 1000000000.0;
            passedTime = newCycleTime - lastTime;
            lastTime = newCycleTime;
            unprocessedTime += passedTime;
            input.update();
            while (unprocessedTime >= frameCap) {
                count++;
                testTime = System.currentTimeMillis();
                
                physics.update( frameCap);
                game.update(frameCap);
                objManager.update();

                unprocessedTime -= frameCap;
                render = true;
                //if(count>1)//for debuggin only
                 //   break;
                //System.out.println("Test time per cycle: "+(System.currentTimeMillis()-testTime));
            }
            //System.out.println("count is "+count);
            if (render) {
                game.render(renderer);
                System.out.println("FPS: " + (int)(1000/((System.nanoTime() - FPSCounter) / 1000000)));
            } else {
                try {
                    System.out.println("sleeping for: "+ (frameCap-passedTime)*1000);
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            

        }

    }

    public Physics getPhysics() {
        return physics;
    }

    public void handleInput() {

    }




    public void setTitle(String title) {
        this.title = title;
    }



    public String getTitle() {
        return title;
    }

    public Window getWindow() {
        return window;
    }

    public ObjectManager getObjectManager() {
        return objManager;
    }

    public Input getInput() {
        return input;
    }

    public void addObject(GameObject obj) {
        objManager.addObject(obj);
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }
    
}
