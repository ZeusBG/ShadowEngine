/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import components.AABB;
import engine.Core;
import render.Renderer;
import gameObjects.Projectile;
import java.awt.Color;
import render.Light;

/**
 *
 * @author Zeus
 */
public class Bullet extends Projectile {

    public Bullet(int x, int y) {
        super(x, y);
        speed=1000;
        light = new Light(this);
        explosive = new Explosion1();
    }

    @Override
    public void update(Core gc, float dt) {
        aabb = new AABB(currentPosition.x-1,currentPosition.y-1,currentPosition.x+1,currentPosition.y+1);
        nextPosition.x = currentPosition.x + speed * dt * direction.x;
        nextPosition.y = currentPosition.y + speed * dt * direction.y;
    }

    @Override
    public void render(Core gc, Renderer r) {
        r.setColor(Color.BLACK);
        r.drawCircle((int) currentPosition.x, (int) currentPosition.y, 2);
    }

}
