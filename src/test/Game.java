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

    private void test(Core core){
        Player player = new Player();
        player.addCollidableWithType("player");
        player.setCurrentPosition(new Point2D.Double(200,200));
        player.setNextPosition(new Point2D.Double(220,210));
        core.addObject(player);
        
        Weapon wep = new AK(0,0,null);
        core.addObject(wep);
        player.addWeapon(wep);
        
        
        Wall w = new Wall(0,0);
        w.addPoint(new Point2D.Double(60,30));
        w.addPoint(new Point2D.Double(100,160));
        w.addPoint(new Point2D.Double(100,200));
        w.addPoint(new Point2D.Double(200,200));
        
        core.addObject(w);
        w = new Wall(0,0);
        w.addPoint(new Point2D.Double(0,0));
        w.addPoint(new Point2D.Double(800,0));
        w.addPoint(new Point2D.Double(800,600));
        w.addPoint(new Point2D.Double(0,600)); 
        w.addPoint(new Point2D.Double(0,0));
        core.addObject(w);
        
        w = new Wall(0,0);
        w.addPoint(new Point2D.Double(300,300));
        w.addPoint(new Point2D.Double(400,300));
        w.addPoint(new Point2D.Double(400,400));
        w.addPoint(new Point2D.Double(300,400));
        w.addPoint(new Point2D.Double(300,300));
        core.addObject(w);
        
        w = new Wall(0,0);
        w.setRenderable(false);
        w.addPoint(new Point2D.Double(500, 100));
        w.addPoint(new Point2D.Double(600,100));
        w.addPoint(new Point2D.Double(600,200));
        w.addPoint(new Point2D.Double(500,200));
        w.addPoint(new Point2D.Double(500,100));
        core.addObject(w);
        
        w = new Wall(0,0);
        w.addPoint(new Point2D.Double(10, 10));
        w.addPoint(new Point2D.Double(40,10));
        w.addPoint(new Point2D.Double(40,40));
        w.addPoint(new Point2D.Double(10,40));
        w.addPoint(new Point2D.Double(10,10));
        
        core.addObject(w);
        w = new Wall(0,0);
        w.addPoint(new Point2D.Double(110, 110));
        w.addPoint(new Point2D.Double(140,110));
        w.addPoint(new Point2D.Double(140,140));
        w.addPoint(new Point2D.Double(110,140));
        w.addPoint(new Point2D.Double(110,110));
        core.addObject(w);
        
        w = new Wall(0,0);
        w.addPoint(new Point2D.Double(210, 110));
        w.addPoint(new Point2D.Double(240,110));
        w.addPoint(new Point2D.Double(240,140));
        w.addPoint(new Point2D.Double(210,140));
        w.addPoint(new Point2D.Double(210,110));
        core.addObject(w);
        
        
        
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
