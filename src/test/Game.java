/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import components.emitter.Emitter;
import engine.AbstractGame;
import engine.Core;
import render.Renderer;
import gameObjects.Weapon;
import math.Vector2f;
import org.newdawn.slick.opengl.Texture;
import render.Material;

/**
 *
 * @author Zeus
 */
public class Game extends AbstractGame{
        

    
    public static void main(String[] args) {
        Game game = new Game();
        Core core = new Core(game);
        core.init();
        //core.setSize(1920, 1080);
        game.test(core);
        core.getScene().getRayCollisionTree().printTree();
        core.start();
    }
    
    public Game(){
        super();
    }
    
    private void test(Core core){
        Player player = new Player();
        player.setPosition(new Vector2f(170,150));
        player.setNextPosition(new Vector2f(170,150));
        core.addObject(player);
        Weapon wep = new AK(0,0,null);
        core.addObject(wep);
        player.addWeapon(wep);
        //player.setSpeed(50);
        
        
        RandomBot bot = new RandomBot();
        bot.setPosition(new Vector2f(160,160));
        bot.setNextPosition(new Vector2f(160,160));
        //core.addObject(bot);
        wep = new AK(0,0,null);
        //core.addObject(wep);
        bot.addWeapon(wep);
        
        bot = new RandomBot();
        bot.setPosition(new Vector2f(360,160));
        bot.setNextPosition(new Vector2f(360,160));
        //core.addObject(bot);
        wep = new AK(0,0,null);
        //core.addObject(wep);
        bot.addWeapon(wep);
        
        
        
        
        Emitter em = new Emitter();
        core.addObject(em);
        for(int i=1;i<10;i++){
            for(int j=1;j<10;j++){
                
                String path = "res/textures/box2.png";
                String type = "PNG";
                Texture texture = textures.get("box2.png", type, path);
                Material.MaterialBuilder matBuilder = new Material.MaterialBuilder(texture,new Vector2f(i*100,j*100)).color(org.newdawn.slick.Color.white).scale(/*80/128f*/1, /*80/128f*/1).dimension(50, 50);
                Material mat = new Material(matBuilder);
                
                StaticObject w = new StaticObject(0,0);
                w.addPoint(new Vector2f(i*100,j*100));
                w.addPoint(new Vector2f(i*100,j*100+50));
                w.addPoint(new Vector2f(i*100+50,j*100+50));
                w.addPoint(new Vector2f(i*100+50,j*100));
                w.addPoint(new Vector2f(i*100,j*100));
                w.setMaterial(mat);
                //w.getGeometry().rotate(30.0f);
                core.addObject(w);
                w.addEmitter(em);
            }
        }
        
         StaticObject w = new StaticObject(0,0);
                w.addPoint(new Vector2f(1200,300));
                w.addPoint(new Vector2f(1100,400));
                w.addPoint(new Vector2f(1300,400));
                w.addPoint(new Vector2f(1200,300));
                core.addObject(w);
        w.addEmitter(em);

        
        w = new StaticObject(0,0);
        //w.removeCollidableType("all");
        w.addPoint(new Vector2f(0,0));
        w.addPoint(new Vector2f(1600,0));
        w.addPoint(new Vector2f(1600,1200));
        w.addPoint(new Vector2f(0,1200)); 
        w.addPoint(new Vector2f(0,0));
        core.addObject(w);
        w.addEmitter(em);
        w = new StaticObject(0,0);
        
        
        core.getScene().getCamera().setTarget(player);
    }
    
    @Override
    public void update( float dt) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render(Renderer r) {
        r.render();
    }

    @Override
    public void init(Core gc) {
        
    }
}
