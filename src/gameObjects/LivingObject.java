/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameObjects;

import gameObjects.util.ObjectType;

/**
 *
 * @author Zeus
 */
public abstract class LivingObject extends DynamicGameObject{
    
    private float hp = 100;
    private float maxHP = 100;
    private float maxArmor = 100;
    private float armor = 0;
    
    public LivingObject(ObjectType type) {
        super(0, 0, type);
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public float getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(float maxHP) {
        this.maxHP = maxHP;
    }

    public float getMaxArmor() {
        return maxArmor;
    }

    public void setMaxArmor(float maxArmor) {
        this.maxArmor = maxArmor;
    }

    public float getArmor() {
        return armor;
    }

    public void setArmor(float armor) {
        this.armor = armor;
    }
    
    public void damage(float damage){
        
    } 
}
