/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

/**
 *
 * @author Nick
 */
public class Timer {
    
    private long timeStarted;
    private long currentTime;
    private long previousTime;
    private long currentTimeLong;
    private long timeSpentPaused;
    private long timePaused;
    private float delta;
    private float deltaInSeconds;
    public Timer(){
        timeStarted = System.currentTimeMillis();
        currentTime = timeStarted;
        previousTime = timeStarted;
        timeSpentPaused = 0;
        delta = 0;
        
    }
    
    public void update(){
        previousTime = currentTime;
        currentTime = System.nanoTime() - timeSpentPaused;
        
        delta = (currentTime - previousTime) / 1000000.0f;
        deltaInSeconds = (currentTime - previousTime) / 1000000000.0f;
        currentTimeLong = System.currentTimeMillis() - timeSpentPaused/1000000;
        
    }

    public float getTimeStarted() {
        return timeStarted;
    }

    public float getCurrentTime() {
        return currentTime;
    }

    public float getPreviousTime() {
        return previousTime;
    }
    
    public float getDelta(){
        return delta;
    }
    
    public float getDeltaInSeconds(){
        return deltaInSeconds;
    }
    
    public void start(){
        currentTime = System.nanoTime();
        previousTime = currentTime;
        currentTimeLong = System.currentTimeMillis();
    }
    
    public long getCurrentTimeMillis(){
        return currentTimeLong;
    }
    
    public void pause(){
        timePaused = System.nanoTime();
    }
    
    public void unPause(){
        timeSpentPaused += System.nanoTime()- timePaused;

    }
    
    public void reset(){
        currentTime = System.nanoTime() - timeSpentPaused;
        previousTime = currentTime;
    }
    
    
}
