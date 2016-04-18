/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects.util;

import java.util.ArrayList;

/**
 *
 * @author Zeus
 */
public class Collidable {
    private ArrayList<Integer> collidableWith;
    private ArrayList<Integer> collideTypes;
    
    public Collidable(){
        collidableWith = new ArrayList<>();
        collideTypes = new ArrayList<>();
    }

    public void addCollideType(String type){
        
        Integer hash = type.hashCode();
        if(!collideTypes.contains(hash))
            collideTypes.add(hash);
    }
    
    public void removeCollideType(String type){
        
        Integer hash = type.hashCode();
            collideTypes.remove(hash);
    }
    
    public void addCollidableWithType(String type){
        Integer hash = type.hashCode();
        if(!collidableWith.contains(hash))
            collidableWith.add(hash);
    }
    
    public void removeCollidableWithType(String type){
        Integer hash = type.hashCode();
            collidableWith.remove(hash);
    }
    
    public boolean collidesWith(int hash){ 
        return collidableWith.contains(hash);
    }

    public ArrayList<Integer> getCollidableWith() {
        return collidableWith;
    }

    public ArrayList<Integer> getCollideTypes() {
        return collideTypes;
    }
    public void addDefaultCollision(){
        addCollidableWithType("all");
        addCollideType("all");
    }
}
