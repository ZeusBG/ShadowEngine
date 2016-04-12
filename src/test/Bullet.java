/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import engine.render.Renderer;
import gameObjects.Projectile;
import java.awt.Color;
import math.Vector2f;
import org.lwjgl.opengl.GL20;
import engine.render.Light;
import engine.render.Shader.PlayerShader;
import engine.render.TexturedQuad;

/**
 *
 * @author Zeus
 */
public class Bullet extends Projectile {
    
    public Bullet(float x, float y) {
        super(x, y);
        speed=150;
        light = new Light(this);
        light.setSpanAngle(360);
        light.setRadius(130);
        Color c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
        light.setColor(c);
        explosive = new Explosion1();
        explosive.getLight().setColor(c);
        
        texQuad = new TexturedQuad(new Vector2f(400,225) ,new Vector2f(15,15));
        texQuad.loadTexture("res/textures/bullet.png", "png");
        texQuad.setScale(new Vector2f(1,1));
        
        String vShader = "src/engine/render/shaders/defaultQuadVShader.vert";
        String fShader = "src/engine/render/shaders/defaultQuadFShader.frag";
        PlayerShader shader = new PlayerShader(vShader,fShader);
        shader.bindAttributeLocations();
        //shader.createProgram();
        //int fsid = shader.loadShader("src/render/shaders/defaultQuadVShader.vert", GL20.GL_VERTEX_SHADER);
        //int vsid = shader.loadShader("src/render/shaders/defaultQuadFShader.frag", GL20.GL_FRAGMENT_SHADER);
        
        //shader.bindAttributeLocations(shader.getProgramID());
        
        //shader.linkAndAttach(vsid);
        //shader.linkAndAttach(fsid);
        texQuad.setShader(shader);
        
    }

    @Override
    public void update(float dt) {
        geometry.getAabb().reset(position.x,position.y,position.x,position.y);
        nextPosition.x = position.x + speed * dt * direction.x;
        nextPosition.y = position.y + speed * dt * direction.y;
        texQuad.setCameraOffset(core.getCamera().getPosition());
        texQuad.setTranslation(position);
        texQuad.setResolution(core.getCamera().getSize());
        texQuad.setRotation(-direction.getAngle() + 180);
        
    }

    @Override
    public void render(Renderer r) {
        r.setColor(Color.WHITE);
        r.drawCircle(position.x, position.y, 1);
        
        texQuad.renderQuad();
    }

}
