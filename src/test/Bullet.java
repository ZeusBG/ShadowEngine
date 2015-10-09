/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

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
        speed=60;
        light = new Light(this);
        light.setSpanAngle(360);
        Color c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
        light.setColor(c);
        explosive = new Explosion1();
        explosive.getLight().setColor(c);
    }

    @Override
    public void update(float dt) {
        aabb.reset(currentPosition.x,currentPosition.y,currentPosition.x,currentPosition.y);
        nextPosition.x = currentPosition.x + speed * dt * direction.x;
        nextPosition.y = currentPosition.y + speed * dt * direction.y;
    }

    @Override
    public void render(Renderer r) {
        r.setColor(Color.WHITE);
        r.drawCircle(currentPosition.x, currentPosition.y, 1);
    }

}
