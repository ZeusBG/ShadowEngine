/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import components.ObjectManager;
import gameObjects.DynamicGameObject;
import gameObjects.Projectile;
import gameObjects.StaticGameObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collections;
import math.Ray;
import math.Vector;
import utils.SortPointsClockWise;

/**
 *
 * @author Zeus
 */
public class Renderer {

    private Core core;
    //private Graphics2D g2d;
    private Graphics2D g2d;
    private BufferStrategy bs;
    private double cameraOffSetX;
    private double cameraOffSetY;
    private double scaleX;
    private double scaleY;
    private boolean dynamicCamera = false;

    public Renderer(Core core) {
        this.core = core;
        g2d = (Graphics2D) core.getWindow().getBufferStrategy().getDrawGraphics();
        bs = core.getWindow().getBufferStrategy();
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //  g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
        //          RenderingHints.VALUE_RENDER_QUALITY);
    }

    public void render() {
        ObjectManager objectsToRender = core.getObjectManager();
        //if(dynamicLights) - castvam senki kum vsi4ko(kur6umi,hora);
        //castvam visibilityto

        //setvam clip
        //render objects
        if(dynamicCamera){
            cameraOffSetX = core.getObjectManager().getCamera().getDynamicX();
            cameraOffSetY = core.getObjectManager().getCamera().getDynamicY();
        }
        else{
            cameraOffSetX = core.getObjectManager().getCamera().getX();
            cameraOffSetY = core.getObjectManager().getCamera().getY();
        }
        //cameraOffSetX = 0;
        //cameraOffSetX = 0;

        scaleX = core.getWidthScale();
        scaleY = core.getHeightScale();

        clear();
        castShadows(objectsToRender.getPlayer().getCurrentPosition());

        //setClip
        if (core.getObjectManager().getPlayer() != null) {
            core.getObjectManager().getPlayer().render(core, this);
        }

        for (DynamicGameObject obj : core.getObjectManager().getDynamicObjects()) {
            obj.render(core, this);
        }
        for (Projectile obj : core.getObjectManager().getProjectiles()) {
            obj.render(core, this);
        }

        for (StaticGameObject obj : core.getObjectManager().getStaticObjects()) {

            obj.render(core, this);
        }

        bs.show();
    }

    public void drawCircle(int x, int y, int r) {
        g2d.draw(new Ellipse2D.Double(scaleX * x - r + cameraOffSetX, scaleY * y - r + cameraOffSetY, scaleX * r * 2, scaleY * r * 2));
    }

    public Graphics2D getG2d() {
        return g2d;
    }

    public void clear() {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, core.getWidth(), core.getHeight());
        g2d.setColor(Color.BLACK);
    }

    public void castShadows(Point2D.Double lightSource) {
        ArrayList<Point2D.Double> points = core.getObjectManager().getPoints();
        ArrayList<Line2D.Double> walls = core.getObjectManager().getLines();
        drawSight(lightSource, points, walls);

    }

    private void drawSight(Point2D.Double lightSource, ArrayList<Point2D.Double> points, ArrayList<Line2D.Double> segments) {
        g2d.setClip(null);
        Area light1 = new Area(findIntersectionPoints(lightSource, points, segments));

        float radius = 640;
        float[] dist = {0.0f, 0.4f};
        Color[] colors = {new Color(1.0f, 1.0f, 1.0f, 1.0f), new Color(0.0f, 0.0f, 0.0f, 1.0f)};
        float[] dist2 = {0.1f, 0.3f};

        Polygon screen = new Polygon();

        screen.addPoint(0, 0);
        screen.addPoint(0, core.getHeight());
        screen.addPoint(core.getWidth(), core.getHeight());
        screen.addPoint(core.getWidth(), 0);
        Area mask = new Area(screen);
        //if(light1.npoints>0){
        g2d.setColor(Color.BLACK);
        /*Path2D.Double sight = new Path2D.Double();
         sight.moveTo(intersectionPoints.get(0).x, intersectionPoints.get(0).y);
         for(int i = 1;i<=intersectionPoints.size();i++){
         sight.lineTo(intersectionPoints.get(i%intersectionPoints.size()).x, intersectionPoints.get(i%intersectionPoints.size()).y);
         }
         */

        g2d.setColor(Color.WHITE);

        Area lightRadius = new Area(new Ellipse2D.Double(scaleX * (lightSource.x - radius) + cameraOffSetX, scaleY * (lightSource.y - radius) + cameraOffSetY, scaleX * 2 * radius + cameraOffSetX, scaleY * 2 * radius + cameraOffSetY));
        light1.intersect(lightRadius);

        Polygon triangleSight = new Polygon();
        Point2D.Double playerPoint = core.getObjectManager().getPlayer().getCurrentPosition();
        Vector playerOrientation = new Vector(core.getObjectManager().getPlayer().getOrientation());
        playerOrientation.normalize();

        Point2D.Double p1 = new Point2D.Double(playerPoint.x, playerPoint.y);
        Point2D.Double p2 = new Point2D.Double(playerPoint.x + 180 * playerOrientation.x - 150 * playerOrientation.y,
                playerPoint.y + 180 * playerOrientation.y + 150 * playerOrientation.x);
        Point2D.Double p3 = new Point2D.Double(playerPoint.x + 180 * playerOrientation.x + 150 * playerOrientation.y,
                playerPoint.y + 180 * playerOrientation.y - 150 * playerOrientation.x);
        p1.x = scaleX * p1.x + cameraOffSetX;
        p1.y = scaleY * p1.y + cameraOffSetY;
        p2.x = scaleX * p2.x + cameraOffSetX;
        p2.y = scaleY * p2.y + cameraOffSetY;
        p3.x = scaleX * p3.x + cameraOffSetX;
        p3.y = scaleY * p3.y + cameraOffSetY;
        triangleSight.addPoint((int) p1.x, (int) p1.y);
        triangleSight.addPoint((int) p2.x, (int) p2.y);
        triangleSight.addPoint((int) p3.x, (int) p3.y);

        int arcRadius = 380;
        int extent = 90;
        double angle = Math.atan2(playerOrientation.x, playerOrientation.y) * 180 / Math.PI;
        //if(angle<0)
        //angle = 360+angle;

        Arc2D.Double arc = new Arc2D.Double(p1.x - arcRadius / 2, p1.y - arcRadius / 2, arcRadius, arcRadius, angle-extent/2 , -extent, Arc2D.PIE);

        light1.intersect(new Area(arc));
        mask.subtract(new Area(light1));

        g2d.setColor(Color.black);
        g2d.fill(mask);

        //g2d.setColor(Color.black);
        g2d.setPaint(new RadialGradientPaint(new Point2D.Double(lightSource.x + cameraOffSetX, lightSource.y + cameraOffSetY), radius, dist2, colors));
        g2d.fill(light1);
        g2d.setClip(light1);

        g2d.setPaint(null);
        //core.getObjectManager().getRayCollisionTree().drawTree(g2d, (int) cameraOffSetX, (int) cameraOffSetY);
    }

    public Polygon findIntersectionPoints(Point2D.Double lightSource, ArrayList<Point2D.Double> points, ArrayList<Line2D.Double> segments) {

        ArrayList<Point2D.Double> intersectionPoints = new ArrayList<>();
        ArrayList<Point2D.Double> sPoints = new ArrayList<>();
        //Point2D.Double playerPoint = lightSource;

        for (StaticGameObject s : core.getObjectManager().getStaticObjects()) {
            for (Point2D.Double cPoint : s.getPoints()) {
                Ray tmpRay = new Ray(lightSource, cPoint);
                if (cPoint.equals(core.getObjectManager().getRayCollisionTree().intersect(tmpRay))) {
                    addSecondaryPoints(cPoint, lightSource, sPoints);
                }
            }
        }

        for (Point2D.Double cPoint : sPoints) {

            Ray tmpRay = new Ray(lightSource, cPoint);
            Point2D.Double intersection = core.getObjectManager().getRayCollisionTree().intersect(tmpRay);
            if (intersection != null) {
                intersectionPoints.add(intersection);
            }
        }

        Collections.sort(intersectionPoints, new SortPointsClockWise(lightSource));

        Polygon light = new Polygon();
        for (int i = 0; i < intersectionPoints.size(); i++) {
            light.addPoint((int) (scaleX * intersectionPoints.get(i).x + cameraOffSetX), (int) (scaleY * intersectionPoints.get(i).y + cameraOffSetY));

        }
        return light;

    }

    public void addSecondaryPoints(Point2D.Double p, Point2D.Double lightSource, ArrayList<Point2D.Double> sPoints) {
        double x, y;

        y = p.x - lightSource.x;
        x = p.y - lightSource.y;

        double newX, newY;
        newX = p.x - x * 0.001;
        newY = p.y + y * 0.001;
        sPoints.add(new Point2D.Double(newX, newY));

        newX = p.x + x * 0.001;
        newY = p.y - y * 0.001;
        sPoints.add(new Point2D.Double(newX, newY));

    }

    public void drawLine(int x1, int y1, int x2, int y2) {

        g2d.drawLine((int) (scaleX * x1 + cameraOffSetX), (int) (scaleY * y1 + cameraOffSetY), (int) (scaleX * x2 + cameraOffSetX), (int) (scaleY * y2 + cameraOffSetY));

    }

    public void drawRay(int x1, int y1, int x2, int y2) {

        Ray r = new Ray(x1, y1, x2, y2);
        Point2D.Double endPoint = core.getObjectManager().getRayCollisionTree().intersect(r);

        if (endPoint != null) {
            drawLine(x1, y1, (int) endPoint.x, (int) endPoint.y);
            //System.out.println("testing github");
        } else {
            drawLine(x1, y1, x2, y2);
        }
    }

    public void setColor(Color c) {
        g2d.setColor(c);
    }

    public void drawTriangle(Point2D.Double a, Point2D.Double b, Point2D.Double c) {
        Polygon p = new Polygon();
        p.addPoint((int) (scaleX * a.x + cameraOffSetX), (int) (scaleY * a.y + cameraOffSetY));
        p.addPoint((int) (scaleX * b.x + cameraOffSetX), (int) (scaleY * b.y + cameraOffSetY));
        p.addPoint((int) (scaleX * c.x + cameraOffSetX), (int) (scaleY * c.y + cameraOffSetY));
        g2d.fill(p);
    }
}
