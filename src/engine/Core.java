/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import static engine.GameState.MENU;
import static engine.GameState.PAUSED;
import static engine.GameState.RUNNING;
import engine.render.Renderer;
import engine.space.Physics;
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
    private Camera camera;
    private Timer timer;
    private Scene scene;
    private Renderer renderer;
    private SoundManager soundManager;
    private long timeStarted;
    private GameState gameState;
    private long timeLastPaused = 0;
    private final long minPausePeriod = 1000;
    
    
    private float FPSCap = 60;
    private float timeStep = 1.0f / FPSCap ;
    private boolean isRunning = false;

    public Core(AbstractGame game) {
        this.game = game;
    }

    public void init() {

        thread = new Thread(this);

        int width = 1920;
        int height = 1080;
        window = new Window(width, height);

        scene = new Scene(this, 1600, 1200);
        camera = new Camera(0, 0, 400, 225);
        camera.setCore(this);
        camera.setDynamic(true);

        input = new Input(this);
        renderer = new Renderer(this);
        physics = new Physics(this);
        soundManager = new SoundManager();
        timer = new Timer();

    }

    public void start() {
        if (isRunning) {
            return;
        }
        
        timeStarted = System.currentTimeMillis();
        thread.run();//this is wrong, should use thread.start() will fix later
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
        gameState = RUNNING;
        timer.start();
        
        while (isRunning) {
            
            if(gameState == RUNNING){
                gameLoop();
            }
            else if(gameState == PAUSED){
                pauseLoop();
            }
            else{
                mainMenuLoop();
            }
        }
    }
    
    private void gameLoop(){
        isRunning = true;

        double FPSCounter;
        double unprocessedTime = 0;
        
        boolean render = false;
        timer.reset();

        while (gameState == RUNNING) {
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Display.isCloseRequested()) {
                System.exit(0);
            }
            FPSCounter = System.nanoTime();
            render = false;

            timer.update();
            unprocessedTime += timer.getDeltaInSeconds();
            input.update();
            
            while (unprocessedTime >= timeStep) {
                
                scene.update(timeStep);
                physics.update(timeStep);
                game.update(timeStep);
                camera.update();

                unprocessedTime -= timeStep;
                render = true;
            }
            if (render) {
                game.render(renderer);
                System.out.println("FPS: " + (int) (1000 / ((System.nanoTime() - FPSCounter) / 1000000)));
            } else {
                sleepThread(1);
            }
            
        }
    }
    
    private void pauseLoop(){
        boolean paused = true;
        timer.pause();
        sleepThread(200);
        while(paused){
            input.update();
            if(input.isKeyPressed(Keyboard.KEY_F)){
                paused = false;
                
                gameState = RUNNING;
                System.err.println("game unpaused !");

            }
            sleepThread(32);
        }
        timeLastPaused = System.currentTimeMillis();
        timer.unPause();
    }
    
    private void mainMenuLoop(){
        
    }
    
    public Physics getPhysics() {
        return physics;
    }

    public Window getWindow() {
        return window;
    }

    public Scene getScene() {
        return scene;
    }

    public Input getInput() {
        return input;
    }

    public void addObject(GameObject obj) {
        scene.addObject(obj);
        obj.setCore(this);
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public Camera getCamera() {
        return camera;
    }
    
    public float getTime(){
        return timer.getCurrentTime();
    }
    
    public long getCurrentTimeMillis(){
        return timer.getCurrentTimeMillis();
    }
    
    public void pauseGame(){
        if(System.currentTimeMillis() - timeLastPaused < minPausePeriod)
            return;
        gameState = PAUSED;
    }
    
    public void resumeGame(){
        gameState = RUNNING;
    }
    
    public void goToMainMenu(){
        gameState = MENU;
    }
    
    private void sleepThread(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
