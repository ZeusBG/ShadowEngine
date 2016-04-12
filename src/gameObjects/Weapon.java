/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects;

import java.util.ArrayList;
import math.Vector2f;
import test.Bullet;
import utils.ObjectType;


/**
 *
 * @author Zeus
 */
public abstract class Weapon extends DynamicGameObject{
    protected LivingObject owner;
    protected ArrayList<Projectile> ammonition;
    protected Vector2f orientation;
    
    protected int clipSize;
    protected int maxAmmo;
    protected double reloadTime;
    protected double timeStartedReloading;
    protected double timeLastFired;
    protected double blockedTime;
    protected int currentAmmo;
    protected int fireRate;
    protected double timePerProjectile;
    
    public Weapon(int x, int y,LivingObject owner) {
        super(x, y, ObjectType.ITEM);
        orientation = new Vector2f(1,0);
        this.owner = owner;
        
        
    }

    public DynamicGameObject getOwner() {
        return owner;
    }

    public void setOwner(LivingObject owner) {
        this.owner = owner;
    }
    

    public ArrayList<Projectile> getAmmonition() {
        return ammonition;
    }

    public void setAmmonition(ArrayList<Projectile> ammonition) {
        this.ammonition = ammonition;
    }

    public Vector2f getOrientation() {
        return orientation;
    }

    public void setOrientation(Vector2f orientation) {
        this.orientation = orientation;
    }

    public double getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime(double reloadTime) {
        this.reloadTime = reloadTime;
    }

    public int getFireRate() {
        return fireRate;
    }

    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
        timePerProjectile = 60/fireRate;
    }

    public double getBlockedTime() {
        return blockedTime;
    }

    public void setBlockedTime(double blockedTime) {
        this.blockedTime = blockedTime;
    }
    
    @Override
    public void moveToNextPoint(){
        position = owner.getPosition();
    }
    
    public boolean canFire(){
        //System.out.printf("%f vs %f\n",System.currentTimeMillis()-timeLastFired,timePerProjectile);
        return core.getCurrentTimeMillis()-timeLastFired>timePerProjectile && core.getCurrentTimeMillis()-timeStartedReloading>reloadTime*1000;
    }
    
    public void fireSound(){
        
    }
    
    public void fire() {
        
        if(canFire()){
            
            if(ammonition.size()>0){
                Projectile p = ammonition.get(ammonition.size()-1);
                p.setPosition(position);
                orientation.normalize();
                p.setDirection(orientation);
                core.addObject(p);
                ammonition.remove(ammonition.size()-1);
                timeLastFired = core.getCurrentTimeMillis();
                //core.getSoundManager().play(Sounds.AK47_SHOT);
 
            }
            else{
                reload();
            }
        }
        
        
    }
    public void reload() {
        timeStartedReloading = core.getCurrentTimeMillis();
        if(currentAmmo<=0){
            return;
        }
        
        int bullets;
        if(currentAmmo<clipSize){
            bullets=currentAmmo;
            currentAmmo = 0;
        }
        else{ 
            bullets=clipSize;
            currentAmmo-=clipSize;
        }
        
        for(int i=0;i<bullets;i++){
            ammonition.add(new Bullet(0,0));
            
        }
    }
    
    public boolean isClipEmpty(){
        return currentAmmo==0;
    }
}
