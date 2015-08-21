/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import engine.Core;
import engine.Renderer;
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
        maxAmmo = 120;
        currentAmmo = 90;
        reloadTime = 1;
        blockedTime = 0;
        fireRate = 600;
        timePerProjectile = 60000 / (double) fireRate;
        currentPosition = new Point2D.Double();

        for (int i = 0; i < clipSize; i++) {
            ammonition.add(new Bullet(0, 0));
        }

    }

    @Override
    public void update(Core gc, float dt) {
        if (owner != null) {
            orientation = owner.getOrientation();
            currentPosition.x = owner.getCurrentPosition().x;
            currentPosition.y = owner.getCurrentPosition().y;

            nextPosition.x = owner.getNextPosition().x;
            nextPosition.y = owner.getNextPosition().y;

        }
    }

    @Override
    public void render(Core gc, Renderer r) {
        
        Point2D.Double p1 = new Point2D.Double(currentPosition.x + 10 * orientation.x,
                currentPosition.y + 10 * orientation.y);
        Point2D.Double p2 = new Point2D.Double(currentPosition.x - 5 * orientation.y,
                currentPosition.y + 5 * orientation.x);
        Point2D.Double p3 = new Point2D.Double(currentPosition.x + 5 * orientation.y,
                currentPosition.y - 5 * orientation.x);
        r.drawTriangle(p1,p2,p3);
    }

}
