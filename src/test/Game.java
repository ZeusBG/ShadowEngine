/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import engine.AbstractGame;
import engine.Core;
import render.Renderer;
import gameObjects.Weapon;
import java.awt.geom.Point2D;
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
        core.getObjectManager().getRayCollisionTree().printTree();
        core.start();
    }
    
    public Game(){
        super();
    }
    
    private void test(Core core){
        Player player = new Player();
        player.setCurrentPosition(new Point2D.Float(150,150));
        player.setNextPosition(new Point2D.Float(150,150));
        core.addObject(player);
        Weapon wep = new AK(0,0,null);
        core.addObject(wep);
        player.addWeapon(wep);
        //player.setSpeed(50);
        
        
        RandomBot bot = new RandomBot();
        bot.setCurrentPosition(new Point2D.Float(160,160));
        bot.setNextPosition(new Point2D.Float(160,160));
        //core.addObject(bot);
        wep = new AK(0,0,null);
        //core.addObject(wep);
        bot.addWeapon(wep);
        
        bot = new RandomBot();
        bot.setCurrentPosition(new Point2D.Float(360,160));
        bot.setNextPosition(new Point2D.Float(360,160));
        //core.addObject(bot);
        wep = new AK(0,0,null);
        //core.addObject(wep);
        bot.addWeapon(wep);
        
        
        
        
        
        for(int i=1;i<10;i++){
            for(int j=1;j<10;j++){
                
                String path = "res/textures/box2.png";
                String type = "PNG";
                Texture texture = textures.get("box2.png", type, path);
                Material.MaterialBuilder matBuilder = new Material.MaterialBuilder(texture,new Point2D.Float(i*100,j*100)).color(org.newdawn.slick.Color.white).scale(/*80/128f*/1, /*80/128f*/1).dimension(50, 50);
                Material mat = new Material(matBuilder);
        
                Wall w = new Wall(0,0);
                w.addPoint(new Point2D.Float(i*100,j*100));
                w.addPoint(new Point2D.Float(i*100+50,j*100));
                w.addPoint(new Point2D.Float(i*100+50,j*100+50));
                w.addPoint(new Point2D.Float(i*100,j*100+50));
                w.addPoint(new Point2D.Float(i*100,j*100));
                w.setMaterial(mat);
                core.addObject(w);
            }
        }
        
         Wall w = new Wall(0,0);
                w.addPoint(new Point2D.Float(1200,300));
                w.addPoint(new Point2D.Float(1100,400));
                w.addPoint(new Point2D.Float(1300,400));
                w.addPoint(new Point2D.Float(1200,300));
                core.addObject(w);
        
        /*Wall w = new Wall(0,0);
        
        w.addPoint(new Point2D.Float(460,330));
        w.addPoint(new Point2D.Float(660,330));
        w.addPoint(new Point2D.Float(660,430));
        w.addPoint(new Point2D.Float(460,330));

        core.addObject(w);
         */
        
        w = new Wall(0,0);
        //w.removeCollidableType("all");
        w.addPoint(new Point2D.Float(0,0));
        w.addPoint(new Point2D.Float(1600,0));
        w.addPoint(new Point2D.Float(1600,1200));
        w.addPoint(new Point2D.Float(0,1200)); 
        w.addPoint(new Point2D.Float(0,0));
        core.addObject(w);
        
        w = new Wall(0,0);
        /*
        String path = "res/textures/boxy.jpg";
        String type = "JPEG";
        Material.MaterialBuilder matBuilder = new Material.MaterialBuilder(type,path,new Point2D.Float(300,300)).color(org.newdawn.slick.Color.white).scale(91/127f, 91/127f).dimension(100, 100);
        Material mat = new Material(matBuilder);
        w.setMaterial(mat);
        w.addPoint(new Point2D.Float(300,300));
        w.addPoint(new Point2D.Float(400,300));
        w.addPoint(new Point2D.Float(400,400));
        w.addPoint(new Point2D.Float(300,400));
        w.addPoint(new Point2D.Float(300,300));
        core.addObject(w);
        
        w = new Wall(0,0);
        

        matBuilder = new Material.MaterialBuilder(type,path,new Point2D.Float(500,100)).color(org.newdawn.slick.Color.white).scale(91/127f, 91/127f).dimension(100, 100);
        mat = new Material(matBuilder);
        w.setMaterial(mat);
        
        w.addPoint(new Point2D.Float(500, 100));
        w.addPoint(new Point2D.Float(600,100));
        w.addPoint(new Point2D.Float(600,200));
        w.addPoint(new Point2D.Float(500,200));
        w.addPoint(new Point2D.Float(500,100));
        core.addObject(w);
        
        w = new Wall(0,0);
        matBuilder = new Material.MaterialBuilder(type,path,new Point2D.Float(10,10)).color(org.newdawn.slick.Color.white).scale(91/127f, 91/127f).dimension(30, 30);
        mat = new Material(matBuilder);
        w.setMaterial(mat);
        w.addPoint(new Point2D.Float(10, 10));
        w.addPoint(new Point2D.Float(40,10));
        w.addPoint(new Point2D.Float(40,40));
        w.addPoint(new Point2D.Float(10,40));
        w.addPoint(new Point2D.Float(10,10));
        
        core.addObject(w);
        w = new Wall(0,0);
        matBuilder = new Material.MaterialBuilder(type,path,new Point2D.Float(110,110)).color(org.newdawn.slick.Color.white).scale(91/127f, 91/127f).dimension(30, 30);
        mat = new Material(matBuilder);
        w.setMaterial(mat);
        w.addPoint(new Point2D.Float(110, 110));
        w.addPoint(new Point2D.Float(140,110));
        w.addPoint(new Point2D.Float(140,140));
        w.addPoint(new Point2D.Float(110,140));
        w.addPoint(new Point2D.Float(110,110));
        core.addObject(w);
        
        w = new Wall(0,0);
        matBuilder = new Material.MaterialBuilder(type,path,new Point2D.Float(210,110)).color(org.newdawn.slick.Color.white).scale(91/127f, 91/127f).dimension(30, 30);
        mat = new Material(matBuilder);
        w.setMaterial(mat);
        w.addPoint(new Point2D.Float(210, 110));
        w.addPoint(new Point2D.Float(240,110));
        w.addPoint(new Point2D.Float(240,140));
        w.addPoint(new Point2D.Float(210,140));
        w.addPoint(new Point2D.Float(210,110));
        core.addObject(w);
        
        */
        
        core.getObjectManager().getCamera().setTarget(player);
    }
    
    @Override
    public void update(Core gc, float dt) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render(Core gc, Renderer r) {
        r.render();
    }

    @Override
    public void init(Core gc) {
        
    }
}
