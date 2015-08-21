/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects;

import java.util.ArrayList;
import math.Vector;
import test.Bullet;
import utils.ObjectType;

/**
 *
 * @author Zeus
 */
public abstract class Weapon extends DynamicGameObject{
    protected LivingObject owner;
    protected ArrayList<Projectile> ammonition;
    protected Vector orientation;
    
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
        orientation = new Vector(1,0);
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

    public Vector getOrientation() {
        return orientation;
    }

    public void setOrientation(Vector orientation) {
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
        currentPosition = owner.getCurrentPosition();
    }
    
    public boolean canFire(){
        //System.out.printf("%f vs %f\n",System.currentTimeMillis()-timeLastFired,timePerProjectile);
        return System.currentTimeMillis()-timeLastFired>timePerProjectile && System.currentTimeMillis()-timeStartedReloading>reloadTime*1000;
    }
    public Projectile fire() {
        
        if(canFire()){
            
            if(ammonition.size()>0){
                Projectile p = ammonition.get(ammonition.size()-1);
                p.setCurrentPosition(currentPosition);
                orientation.normalize();
                p.setDirection(orientation);
                ammonition.remove(ammonition.size()-1);
                timeLastFired = System.currentTimeMillis();
                return p;
            }
            else{
                reload();
                return null;
            }
        }
        
        return null;
        
        
    }
    public void reload() {
        System.out.println("reloading");
        timeStartedReloading = System.currentTimeMillis();
        if(currentAmmo<=0){
            System.out.println("No Ammo !!");
            return;
        }
        
        int bullets;
        if(currentAmmo<30){
            bullets=currentAmmo;
            currentAmmo=0;
        }
        else{ 
            bullets=30;
            currentAmmo-=30;
        }
        for(int i=0;i<bullets;i++){
            System.out.println(i);
            ammonition.add(new Bullet(0,0));
        }
    }
}
