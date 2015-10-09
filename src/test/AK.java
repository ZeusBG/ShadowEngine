/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import components.AABB;
import engine.Core;
import render.Renderer;
import gameObjects.LivingObject;
import gameObjects.Projectile;
import gameObjects.Weapon;
import java.awt.geom.Point2D;
import java.util.ArrayList;

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
        fireRate = 600;
        timePerProjectile = 60000 / (double) fireRate;
        currentPosition = new Point2D.Float();
        aabb = new AABB(currentPosition.x-5,currentPosition.y-5,currentPosition.x+5,currentPosition.y+5);
        for (int i = 0; i < clipSize; i++) {
            ammonition.add(new Bullet(0, 0));
        }
        

    }

    @Override
    public void update(float dt) {
        if (owner != null) {
            orientation = owner.getOrientation();
            currentPosition.x = owner.getCurrentPosition().x;
            currentPosition.y = owner.getCurrentPosition().y;

            nextPosition.x = owner.getNextPosition().x;
            nextPosition.y = owner.getNextPosition().y;
            aabb = new AABB(currentPosition.x-5,currentPosition.y-5,currentPosition.x+5,currentPosition.y+5);
        }
    }

    @Override
    public void render(Renderer r) {
        //orientation.normalize();
        Point2D.Float p1 = new Point2D.Float(currentPosition.x + 10 * orientation.x,
                currentPosition.y + 10 * orientation.y);
        Point2D.Float p2 = new Point2D.Float(currentPosition.x - 5 * orientation.y,
                currentPosition.y + 5 * orientation.x);
        Point2D.Float p3 = new Point2D.Float(currentPosition.x + 5 * orientation.y,
                currentPosition.y - 5 * orientation.x);
        r.drawTriangle(p1,p2,p3);
    }

}
