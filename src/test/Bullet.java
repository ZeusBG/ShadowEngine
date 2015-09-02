/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

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
        speed =1200;
        light = new Light(this);
    }

    @Override
    public void update(Core gc, float dt) {
        nextPosition.x = currentPosition.x + speed * dt * direction.x;
        nextPosition.y = currentPosition.y + speed * dt * direction.y;
    }

    @Override
    public void render(Core gc, Renderer r) {
        r.setColor(Color.BLACK);
        r.drawCircle((int) currentPosition.x, (int) currentPosition.y, 2);
    }

}
