/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import components.AABB;
import render.Renderer;
import gameObjects.LivingObject;
import gameObjects.Projectile;
import gameObjects.Weapon;
import java.util.ArrayList;
import math.Vector2f;

/**
 *
 * @author Zeus
 */
public class AK extends Weapon {

    public AK(int x, int y, LivingObject owner) {
        super(x, y, owner);
        fireRate = 100;
        reloadTime = 1;
        ammonition = new ArrayList<Projectile>();
        clipSize = 30;
        maxAmmo = 1000;
        currentAmmo = 1000;
        reloadTime = 1;
        blockedTime = 0;
        fireRate = 800;
        timePerProjectile = 60000 / (double) fireRate;
        position = new Vector2f();
        aabb = new AABB(position.x-5,position.y-5,position.x+5,position.y+5);
        for (int i = 0; i < clipSize; i++) {
            ammonition.add(new Bullet(0, 0));
        }
        

    }

    @Override
    public void update(float dt) {
        if (owner != null) {
            orientation = owner.getOrientation();
            position.x = owner.getPosition().x;
            position.y = owner.getPosition().y;

            nextPosition.x = owner.getNextPosition().x;
            nextPosition.y = owner.getNextPosition().y;
            aabb = new AABB(position.x-5,position.y-5,position.x+5,position.y+5);
        }
    }

    @Override
    public void render(Renderer r) {
        //orientation.normalize();
        Vector2f p1 = new Vector2f(position.x + 10 * orientation.x,
                position.y + 10 * orientation.y);
        Vector2f p2 = new Vector2f(position.x - 5 * orientation.y,
                position.y + 5 * orientation.x);
        Vector2f p3 = new Vector2f(position.x + 5 * orientation.y,
                position.y - 5 * orientation.x);
        r.drawTriangle(p1,p2,p3);
    }

}
