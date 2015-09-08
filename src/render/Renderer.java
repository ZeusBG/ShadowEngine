/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import components.ObjectManager;
import engine.Core;
import gameObjects.DynamicGameObject;
import gameObjects.GameObject;
import gameObjects.Projectile;
import gameObjects.StaticGameObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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

    public Renderer(Core core) {
        this.core = core;
        g2d = (Graphics2D) core.getWindow().getBufferStrategy().getDrawGraphics();
        bs = core.getWindow().getBufferStrategy();
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
        //RenderingHints.VALUE_RENDER_QUALITY);
    }

    public void render() {
        ObjectManager objectsToRender = core.getObjectManager();
        //if(dynamicLights) - castvam senki kum vsi4ko(kur6umi,hora);
        //castvam visibilityto

        //setvam clip
        //render objects
        if (core.getObjectManager().getCamera().isDynamic()) {
            cameraOffSetX = core.getObjectManager().getCamera().getDynamicX();
            cameraOffSetY = core.getObjectManager().getCamera().getDynamicY();
        } else {
            cameraOffSetX = core.getObjectManager().getCamera().getX();
            cameraOffSetY = core.getObjectManager().getCamera().getY();
        }
        //cameraOffSetX = 0;
        //cameraOffSetX = 0;

        scaleX = core.getObjectManager().getCamera().getWidthScale();
        scaleY = core.getObjectManager().getCamera().getHeightScale();

        cameraOffSetX *= scaleX;
        cameraOffSetY *= scaleY;
        //System.out.println("cameraOffsetX: "+cameraOffSetX);

        clear();

        castShadows(objectsToRender.getPlayer().getCurrentPosition());

        //setClip
        if (core.getObjectManager().getPlayer() != null) {
            core.getObjectManager().getPlayer().render(core, this);
        }

        for (DynamicGameObject obj : core.getObjectManager().getDynamicObjects()) {
            if(obj.getObjState().isRenderable())
                obj.render(core, this);
        }
        for (Projectile obj : core.getObjectManager().getProjectiles()) {
            if(obj.getObjState().isRenderable())
                obj.render(core, this);
        }
        for (StaticGameObject obj : core.getObjectManager().getStaticObjects()) {
            if(obj.getObjState().isRenderable())
                obj.render(core, this);
        }
        bs.show();

    }

    public void drawCircle(int x, int y, int r) {
        g2d.draw(new Ellipse2D.Double(scaleX * (x - r) + cameraOffSetX, scaleY * (y - r) + cameraOffSetY, scaleX * r * 2, scaleY * r * 2));
    }

    public Graphics2D getG2d() {
        return g2d;
    }

    public void clear() {
        g2d.setClip(null);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 5000, 5000);
        g2d.setColor(Color.BLACK);
        //g2d.dispose();
        //bs.dispose();
    }

    public void castShadows(Point2D.Double lightSource) {

        drawSight(lightSource);

    }

    private void drawSight(Point2D.Double lightSource) {

        g2d.setClip(null);

        Area light1 = new Area(findIntersectionPoints(core.getObjectManager().getPlayer().getLight()));
        ArrayList<Area> lights = new ArrayList<>();
        ArrayList<Light> lightDescription = new ArrayList<>();
        Area visibility = new Area();
        for (GameObject obj : core.getObjectManager().getAllObjects()) {
            Light light = obj.getLight();
            if (light == null || !obj.getObjState().isRenderable()) {
                continue;
            }
            Area lightRadius = new Area(findIntersectionPoints(light));
            lightRadius.intersect(new Area(new Ellipse2D.Double(scaleX * (light.getOwner().getCurrentPosition().x - light.getRadius()) + cameraOffSetX,
                    scaleY * (light.getOwner().getCurrentPosition().y - light.getRadius()) + cameraOffSetY,
                    scaleX * 2 * light.getRadius(),
                    scaleY * 2 * light.getRadius())));

            lights.add(lightRadius);
            visibility.add(lightRadius);
            lightDescription.add(light);

        }

        visibility.intersect(light1);

        float radius = 640;
        float[] dist = {0.0f, 0.4f};
        Color[] colors = {new Color(1.0f, 1.0f, 1.0f, 1.0f), new Color(0.0f, 0.0f, 0.0f, 1.0f)};
        float[] dist2 = {0.05f, 0.3f};

        Polygon screen = new Polygon();

        screen.addPoint(0, 0);
        screen.addPoint(0, 2000);
        screen.addPoint(2000, 2000);
        screen.addPoint(2000, 0);
        Area mask = new Area(screen);
        //if(light1.npoints>0){

        //Area lightRadius = new Area(new Ellipse2D.Double(scaleX * (lightSource.x - radius) + cameraOffSetX, scaleY * (lightSource.y - radius) + cameraOffSetY, scaleX * 2 * radius + cameraOffSetX, scaleY * 2 * radius + cameraOffSetY));
        //light1.intersect(lightRadius);
        Point2D.Double playerPoint = core.getObjectManager().getPlayer().getCurrentPosition();
        Vector playerOrientation = new Vector(core.getObjectManager().getPlayer().getOrientation());
        playerOrientation.normalize();

        Point2D.Double p1 = getScaledPoint(playerPoint);

        int arcRadius = 380;
        int extent = 130;
        double angle = Math.atan2(playerOrientation.x, playerOrientation.y) * 180 / Math.PI;
        //if(angle<0)
        //angle = 360+angle;

        Arc2D.Double arc = new Arc2D.Double(p1.x - scaleX * arcRadius / 2, p1.y - scaleY * arcRadius / 2, scaleX * arcRadius, scaleY * arcRadius, angle - extent / 2 + extent - 90, -extent, Arc2D.PIE);
        //Arc2D.Double arc = new Arc2D.Double(scaleX*(playerPoint.x - arcRadius / 2)+cameraOffSetX, scaleY*(playerPoint.y - arcRadius / 2)+cameraOffSetY, scaleX*arcRadius, scaleY*arcRadius, angle-extent/2+extent-90 , -extent, Arc2D.PIE);

        light1.intersect(new Area(arc));
        light1.add(visibility);

        //g2d.setColor(Color.black);
        Polygon visibilityTriangle = new Polygon();

        Point2D.Double scaledLightSource = getScaledPoint(lightSource);

        visibilityTriangle.addPoint((int) scaledLightSource.x, (int) scaledLightSource.y);
        visibilityTriangle.addPoint((int) (scaledLightSource.x + (arc.getStartPoint().getX() - scaledLightSource.x) * 10), (int) (scaledLightSource.y + (arc.getStartPoint().getY() - scaledLightSource.y) * 10));
        visibilityTriangle.addPoint((int) (scaledLightSource.x + (arc.getEndPoint().getX() - scaledLightSource.x) * 10), (int) (scaledLightSource.y + (arc.getEndPoint().getY() - scaledLightSource.y) * 10));
        //g2d.setColor(Color.red);
        //g2d.drawPolygon(visibilityTriangle);

        light1.intersect(new Area(visibilityTriangle));
        mask.subtract(new Area(light1));
        g2d.setColor(Color.BLACK);
        g2d.fill(mask);
        for (StaticGameObject obj : core.getObjectManager().getStaticObjects()) {
            if(obj.getObjState().isRenderable())
                obj.render(core, this);
        }

        g2d.setColor(new Color(0f, 0f, 0f, 0.8f));
        g2d.fill(mask);

        //g2d.setColor(null);
        g2d.setPaint(new RadialGradientPaint(new Point2D.Double(scaleX * lightSource.x + cameraOffSetX, scaleY * lightSource.y + cameraOffSetY), (int) (scaleX * radius), dist2, colors));
        g2d.fill(light1);

        g2d.setColor(Color.red);
        //g2d.draw(light1);
        g2d.setClip(light1);
        //g2d.draw(light1);
        //g2d.scale(scaleX, scaleY);
        for (int i = 0; i < lightDescription.size(); i++) {
            Point2D.Double pos = new Point2D.Double();
            pos.x = lightDescription.get(i).getOwner().getCurrentPosition().x;
            pos.y = lightDescription.get(i).getOwner().getCurrentPosition().y;

            g2d.setPaint(new RadialGradientPaint(new Point2D.Double(scaleX * pos.x + cameraOffSetX, scaleY * pos.y + cameraOffSetY), (int) (lightDescription.get(i).getRadius() * scaleX), lightDescription.get(i).getDist(), lightDescription.get(i).getColors()));

            g2d.fill(lights.get(i));
        }

        //g2d.scale(1, 1);
        //g2d.setClip(null);
        //g2d.setPaint(null);
        //core.getObjectManager().getRayCollisionTree().drawTree(g2d, (int) cameraOffSetX, (int) cameraOffSetY);
        core.getPhysics().getCollisionTree().drawTree(g2d, (int) cameraOffSetX, (int) cameraOffSetY, scaleX, scaleY);
    }

    public Point2D.Double getScaledPoint(Point2D.Double p) {
        Point2D.Double scaled = new Point2D.Double();
        scaled.x = scaleX * p.x + cameraOffSetX;
        scaled.y = scaleY * p.y + cameraOffSetY;
        return scaled;
    }

    public Polygon findIntersectionPoints(Light lightSrc) {
        Point2D.Double lightSource = lightSrc.getOwner().getCurrentPosition();
        ArrayList<Point2D.Double> intersectionPoints = new ArrayList<>();
        ArrayList<Point2D.Double> sPoints = new ArrayList<>();
        //Point2D.Double playerPoint = lightSource;
        HashSet<GameObject> objects = core.getObjectManager().getRayCollisionTree().getObjectsInRange(lightSrc.getAABB());
        
        for (GameObject s : objects) {
            if(!s.getObjState().isRenderable())
                continue;
            for (Point2D.Double cPoint : s.getPoints()) {
                Ray tmpRay = new Ray(lightSource, cPoint);
                if (cPoint.equals(core.getObjectManager().getRayCollisionTree().intersect(tmpRay,objects))) {
                    addSecondaryPoints(cPoint, lightSource, sPoints);
                }
            }
        }

        for (Point2D.Double cPoint : sPoints) {

            Ray tmpRay = new Ray(lightSource, cPoint);
            Point2D.Double intersection = core.getObjectManager().getRayCollisionTree().intersect(tmpRay,objects);
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
        Point2D.Double endPoint = core.getObjectManager().getRayCollisionTree().intersect(r,null);

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
