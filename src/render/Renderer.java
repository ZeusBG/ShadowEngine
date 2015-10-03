/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import components.AABB;
import components.ObjectManager;
import engine.Core;
import gameObjects.GameObject;
import gameObjects.LivingObject;
import gameObjects.StaticGameObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import math.Ray;
import math.Vector;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;

import utils.SortPointsClockWise;
import static org.lwjgl.opengl.GL20.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import render.Material.MaterialBuilder;

/**
 *
 * @author Zeus
 */
public class Renderer {

    private Core core;
    public static final int CIRCLE_ACCURACY = 50;
    //private Graphics2D g2d;
    private Graphics2D g2d;
    private BufferStrategy bs;
    private float cameraOffSetX;
    private float cameraOffSetY;
    private float scaleX;
    private float scaleY;
    private Shader lightShader;
    private LightMap lightMap;
    private Texture tex;//for test until Z-index is added 
    private Texture floor;

    public Renderer(Core core) {
        this.core = core;
        initialize();
        lightMap = new LightMap(core.getWidth(), core.getHeight(), this, core);
        lightMap.init();
        glEnable(GL_TEXTURE_2D);
        tex = null;
        try {
            tex = TextureLoader.getTexture("JPEG", new FileInputStream(new File("res/textures/car.jpg")));
            floor = TextureLoader.getTexture("JPEG", new FileInputStream(new File("res/textures/plates6.jpg")));
        } catch (IOException ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void render() {

        ObjectManager objectsToRender = core.getObjectManager();

        //get offsets and scales from the camera
        if (core.getObjectManager().getCamera().isDynamic()) {
            cameraOffSetX = (float) core.getObjectManager().getCamera().getDynamicX();
            cameraOffSetY = (float) core.getObjectManager().getCamera().getDynamicY();
        } else {
            cameraOffSetX = (float) core.getObjectManager().getCamera().getX();
            cameraOffSetY = (float) core.getObjectManager().getCamera().getY();
        }
        scaleX = (float) core.getObjectManager().getCamera().getWidthScale();
        scaleY = (float) core.getObjectManager().getCamera().getHeightScale();

        cameraOffSetX *= scaleX;
        cameraOffSetY *= scaleY;

        //just for test because there isnt a z-index
        MaterialBuilder matBuilder = new MaterialBuilder(floor, new Point2D.Float(0, 0)).color(org.newdawn.slick.Color.white).scale(45, 45).dimension(2000, 2000);

        Material mat = new Material(matBuilder);
        drawMaterial(mat);

        //later will remove this random glEnables :D
        glEnable(GL_STENCIL_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);

        //draws light visibility polygons in normal buffer and in lightMap buffer
        castShadows(objectsToRender.getPlayer());

        glDisable(GL_BLEND);
        glDisable(GL_STENCIL_TEST);
        //glDisable(GL_BLEND);
        glColor3f(1.0f, 0.0f, 0.0f);
        glBegin(GL_QUADS);
        {
            glVertex2f(300, 300);
            glVertex2f(350, 300);
            glVertex2f(350, 350);
            glVertex2f(300, 350);
        }
        glEnd();

        glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        glEnable(GL_STENCIL_TEST);
        glDisable(GL_STENCIL_TEST);

        lightMap.blurShadows();
        lightMap.blurShadows();
        lightMap.blurShadows();
        lightMap.blurShadows();

        drawLightMap();

        if (core.getObjectManager().getPlayer() != null) {
            core.getObjectManager().getPlayer().render(this);
        }

        glColor3f(1.0f, 1.0f, 1.0f);
        for (GameObject obj : core.getObjectManager().getAllObjects()) {
            if (obj.getMaterial() != null) {
                drawMaterial(obj.getMaterial());
            } else {
                obj.render(this);
            }
        }

        glDisable(GL_BLEND);

        lightMap.clear();
        //draws the visibility polygon for the player
        //so that he cant see behind walls and behind his back
        lightMap.drawVisibilityTriangle(objectsToRender.getPlayer());
        lightMap.drawVisibilityPolygon(core.getObjectManager().getPlayer().getCurrentPosition());
        //drawLightMap();
        lightMap.blurShadows();
        lightMap.blurShadows();
        lightMap.blurShadows();
        lightMap.blurShadows();

        drawLightMap();
        glClear(GL_STENCIL_BUFFER_BIT);
        glDisable(GL_STENCIL_TEST);
        glDisable(GL_BLEND);
        //core.getPhysics().getCollisionTree().drawTree(this);
        //core.getPhysics().getCollisionTree().drawTree(this);

        Display.update();
        Display.sync(60);

        clear();
    }

    public float getScale() {
        return scaleX;
    }

    private void initialize() {
        initLightShader("src/render/shaders/lightShader.frag");

    }

    public void drawCircle(double cx, double cy, double r) {
        cx = scaleX * cx + cameraOffSetX;
        cy = scaleY * cy + cameraOffSetY;
        r = scaleX * r;

        float step = 2.0f * 3.1415926f / (float) CIRCLE_ACCURACY;
        float theta = 0;
        glBegin(GL_LINE_LOOP);
        {
            for (int i = 0; i < CIRCLE_ACCURACY; i++) {
                theta += step;//get the current angle 

                double x = r * Math.cos(theta);//calculate the x component 
                double y = r * Math.sin(theta);//calculate the y component 

                glVertex2f((float) (x + cx), (float) (y + cy));//output vertex 
            }
        }
        glEnd();
    }

    public void drawDirectedPartCircle(float cx, float cy, float r, Vector direction, float spanAngle) {
        float spanAngleRadians = (float) (spanAngle * Math.PI / 180);
        float startAngle = (float) (Math.atan2(direction.y, direction.x) - spanAngleRadians / 2);

        cx = scaleX * cx + cameraOffSetX;
        cy = scaleY * cy + cameraOffSetY;
        r = scaleX * r;
        float step = (float) (spanAngle * Math.PI / 180 / CIRCLE_ACCURACY);
        float x=  (float)(r * Math.cos(startAngle));
        float y = (float)(r * Math.sin(startAngle));
        float prevX,prevY;
        for (int i = 0; i < CIRCLE_ACCURACY; i++) {
            startAngle += step;//get the current angle 
            prevX=x;
            prevY=y;
            x = (float)(r * Math.cos(startAngle));//calculate the x component 
            y = (float)(r * Math.sin(startAngle));//calculate the y component 

            //output vertex 
            glBegin(GL_TRIANGLES);
            {
                glVertex2f(cx,cy);
                glVertex2f(x + cx,y + cy);
                glVertex2f(prevX + cx,prevY + cy);
            }
            glEnd();

        }

    }

    public void clear() {

        glClear(GL_COLOR_BUFFER_BIT);
        glClear(GL_STENCIL_BUFFER_BIT);
        glDisable(GL_STENCIL_TEST);
        lightMap.clear();
    }

    public void castShadows(LivingObject pointOfView) {

        drawSight(pointOfView.getCurrentPosition());
        glDisable(GL_BLEND);
        glClear(GL_STENCIL_BUFFER_BIT);

    }

    private void drawVisibilityTriangle(LivingObject pointOfView) {
        glColor3f(0.0f, 0.0f, 0.0f);
        Vector p = pointOfView.getOrientation();
        Point2D.Double p1 = new Point2D.Double();
        p1.x = pointOfView.getCurrentPosition().x + p.x * 10;
        p1.y = pointOfView.getCurrentPosition().y + p.y * 10;
        Point2D.Double p2 = new Point2D.Double();
        Point2D.Double p3 = new Point2D.Double();
        p2.x = p1.x + p.getPerpendicular().x * 20;
        p2.y = p1.y + p.getPerpendicular().y * 20;
        p3.x = p1.x + p.getPerpendicular2().x * 20;
        p3.y = p1.y + p.getPerpendicular2().y * 20;
        p2.x += (p2.x - pointOfView.getCurrentPosition().x) * 1000;
        p2.y += (p2.y - pointOfView.getCurrentPosition().y) * 1000;

        p3.x += (p3.x - pointOfView.getCurrentPosition().x) * 1000;
        p3.y += (p3.y - pointOfView.getCurrentPosition().y) * 1000;

        ArrayList<Point2D.Double> visibility = new ArrayList<>();
        visibility.add(pointOfView.getCurrentPosition());
        visibility.add(p2);
        visibility.add(p3);
        drawPolygon(visibility);
    }

    private void drawSight(Point2D.Double lightSource) {

        for (GameObject obj : core.getObjectManager().getAllObjects()) {

            Light light = obj.getLight();
            if (light == null) {
                continue;
            }
            Point2D.Double pos = getScaledPoint(light.getLocation());

            //gets the points for the visibility polygon from the rayCollisionTree in object manager
            ArrayList<Point2D.Double> points = findIntersectionPoints(light);

            //uses these points to draw the visibility polygon in the lightmap and in the gmae buffer
            lightMap.addLight(light, pos, points);
            drawLight(points, light);

        }

    }

    public Point2D.Double getScaledPoint(Point2D.Double p) {
        Point2D.Double scaled = new Point2D.Double();
        scaled.x = scaleX * p.x + cameraOffSetX;
        scaled.y = scaleY * p.y + cameraOffSetY;
        return scaled;
    }

    public Point2D.Float getScaledPoint(Point2D.Float p) {
        Point2D.Float scaled = new Point2D.Float();
        scaled.x = (float) (scaleX * p.x + cameraOffSetX);
        scaled.y = (float) (scaleY * p.y + cameraOffSetY);
        return scaled;
    }

    public ArrayList<Point2D.Double> findIntersectionPoints(Light lightSrc) {

        Point2D.Double lightSource = lightSrc.getOwner().getCurrentPosition();
        ArrayList<Point2D.Double> intersectionPoints = new ArrayList<>(500);
        ArrayList<Point2D.Double> sPoints = new ArrayList<>(500);
        HashSet<GameObject> objects = core.getObjectManager().getRayCollisionTree().getObjectsInRange(lightSrc.getAABB());
        ArrayList<StaticGameObject> allObjects = core.getObjectManager().getStaticObjects();

        for (GameObject s : objects) {
            if (!s.getObjState().isRenderable()) {
                continue;
            }
            for (Point2D.Double cPoint : s.getPoints()) {
                Ray tmpRay = new Ray(lightSource, cPoint);
                if (cPoint.equals(core.getObjectManager().getRayCollisionTree().intersect(tmpRay, objects))) {
                    addSecondaryPoints(cPoint, lightSource, sPoints);
                }
            }
        }

        for (Point2D.Double cPoint : sPoints) {
            Ray tmpRay = new Ray(lightSource, cPoint);
            Point2D.Double intersection = core.getObjectManager().getRayCollisionTree().intersect(tmpRay, objects);
            if (intersection != null) {
                intersectionPoints.add(intersection);
            }
        }

        Collections.sort(intersectionPoints, new SortPointsClockWise(lightSource));

        return intersectionPoints;

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

        glBegin(GL_LINE_LOOP);
        {
            glVertex2f((float) (scaleX * x1 + cameraOffSetX), (float) (scaleY * y1 + cameraOffSetY));
            glVertex2f((float) (scaleX * x2 + cameraOffSetX), (float) (scaleY * y2 + cameraOffSetY));
        }
        glEnd();

    }

    public void drawRay(int x1, int y1, int x2, int y2) {

        Ray r = new Ray(x1, y1, x2, y2);
        Point2D.Double endPoint = core.getObjectManager().getRayCollisionTree().intersect(r, null);

        if (endPoint != null) {
            drawLine(x1, y1, (int) endPoint.x, (int) endPoint.y);
        } else {
            drawLine(x1, y1, x2, y2);
        }
    }

    public void setColor(Color c) {
        if (c == null) {
            return;
        }
        glColor4f(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    public void drawTriangle(Point2D.Double a, Point2D.Double b, Point2D.Double c) {
        glBegin(GL_QUADS);
        {
            glVertex2f((float) (scaleX * a.x + cameraOffSetX), (float) (scaleY * a.y + cameraOffSetY));
            glVertex2f((float) (scaleX * b.x + cameraOffSetX), (float) (scaleY * b.y + cameraOffSetY));
            glVertex2f((float) (scaleX * c.x + cameraOffSetX), (float) (scaleY * c.y + cameraOffSetY));
        }
        glEnd();
    }

    public void drawPolygon(ArrayList<Point2D.Double> points) {
        glEnable(GL_STENCIL_TEST);
        glClear(GL_STENCIL_BUFFER_BIT);
        glColorMask(false, false, false, false);
        glStencilFunc(GL_EQUAL, 1, 1);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_ZERO);

        glBegin(GL_POLYGON);
        {
            for (Point2D.Double p : points) {
                glVertex2f((float) (scaleX * p.x + cameraOffSetX), (float) (scaleY * p.y + cameraOffSetY));
            }
        }
        glEnd();

        glColorMask(true, true, true, true);
        glStencilFunc(GL_EQUAL, 0, 1);
        glStencilOp(GL_ZERO, GL_ZERO, GL_ZERO);

        glBegin(GL_QUADS);
        {
            glVertex2f(0, 0);
            glVertex2f(core.getWidth(), 0);
            glVertex2f(core.getWidth(), core.getHeight());
            glVertex2f(0, core.getHeight());
        }
        glEnd();
        //glUseProgram(0);
    }

    public void drawReversedPolygon(ArrayList<Point2D.Double> points) {

        glColorMask(false, false, false, false);
        glStencilFunc(GL_EQUAL, 1, 1);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_ZERO);

        glBegin(GL_POLYGON);
        for (Point2D.Double p : points) {
            glVertex2f((float) (scaleX * p.x + cameraOffSetX), (float) (scaleY * p.y + cameraOffSetY));
        }
        glEnd();

        glColorMask(true, true, true, true);
        glStencilFunc(GL_EQUAL, 1, 1);
        glStencilOp(GL_ZERO, GL_ZERO, GL_ZERO);

        glBegin(GL_QUADS);
        {
            glVertex2f(0, 0);
            glVertex2f(core.getWidth(), 0);
            glVertex2f(core.getWidth(), core.getHeight());
            glVertex2f(0, core.getHeight());
        }
        glEnd();
        glUseProgram(0);
    }

    public void drawLight(ArrayList<Point2D.Double> points, Light light) {
        glEnable(GL_STENCIL_TEST);
        glClear(GL_STENCIL_BUFFER_BIT);
        glColorMask(false, false, false, false);
        glStencilFunc(GL_EQUAL, 0, 1);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_INCR);

        glBegin(GL_POLYGON);
        {
            for (Point2D.Double p : points) {
                glVertex2f((float) (scaleX * p.x + cameraOffSetX), (float) (scaleY * p.y + cameraOffSetY));
            }
        }
        glEnd();
        
       
        glStencilFunc(GL_EQUAL, 1,1);
        glStencilOp(GL_ZERO, GL_ZERO, GL_INCR);
        drawDirectedPartCircle((float)light.getLocation().x, (float)light.getLocation().y, light.getRadius(), light.getDirection(), light.getSpanAngle());
        
        
        
        glColorMask(true, true, true, true);
        glStencilFunc(GL_EQUAL, 2, 2);
        glStencilOp(GL_ZERO, GL_ZERO, GL_ZERO);

        Point2D.Double lightLocation = getScaledPoint(light.getLocation());
        Color lightColor = light.getColor();

        lightShader.bind();
        glUniform1f(glGetUniformLocation(lightShader.getProgramID(), "power"), (float) light.getPower());
        glUniform2f(glGetUniformLocation(lightShader.getProgramID(), "lightLocation"), (float) lightLocation.x, Display.getHeight() - (float) lightLocation.y + 15);
        glUniform3f(glGetUniformLocation(lightShader.getProgramID(), "lightColor"), lightColor.getRed(), lightColor.getGreen(), lightColor.getBlue());
        glUniform1f(glGetUniformLocation(lightShader.getProgramID(), "scale"), scaleX);
        AABB aabb = light.getAABB();
        glBegin(GL_QUADS);
        {
            glVertex2f(scaleX*(float)aabb.getMinX()+cameraOffSetX, scaleY*(float)aabb.getMinY()+cameraOffSetY);
            glVertex2f(scaleX*(float)aabb.getMaxX()+cameraOffSetX, scaleY*(float)aabb.getMinY()+cameraOffSetY);
            glVertex2f(scaleX*(float)aabb.getMaxX()+cameraOffSetX, scaleY*(float)aabb.getMaxY()+cameraOffSetY);
            glVertex2f(scaleX*(float)aabb.getMinX()+cameraOffSetX, scaleY*(float)aabb.getMaxY()+cameraOffSetY);
        }
        glEnd();
        lightShader.unbind();

    }

    private void initLightShader(String path) {
        lightShader = new Shader(path);
    }

    public void setTextures(boolean enable) {
        if (enable) {
            glEnable(GL_TEXTURE_2D);
        } else {
            glDisable(GL_TEXTURE_2D);
        }
    }

    public float getCameraOffSetX() {
        return cameraOffSetX;
    }

    public float getCameraOffSetY() {
        return cameraOffSetY;
    }

    public void unbindTexture() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void bindTexture(Texture tex) {
        glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
    }

    public void drawLightMap() {
        int textureBuffer = lightMap.getFrameBuffer();
        glEnable(GL_BLEND);
        glBlendFunc(GL_DST_COLOR, GL_ZERO);

        glColor3f(1.0f, 1.0f, 1.0f);
        setTextures(true);
        glBindTexture(GL_TEXTURE_2D, textureBuffer);
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0f, 0f);
            glVertex2f(0, 0);
            glTexCoord2f(1f, 0f);
            glVertex2f(core.getWidth(), 0);
            glTexCoord2f(1f, 1f);
            glVertex2f(core.getWidth(), core.getHeight());
            glTexCoord2f(0f, 1f);
            glVertex2f(0, core.getHeight());
        }
        glEnd();
        setTextures(false);
        unbindTexture();
        glDisable(GL_BLEND);

    }

    public void drawMaterial(Material mat) {
        setTextures(true);
        bindTexture(mat.getTexture());
        glColor3f(1.0f, 1.0f, 1.0f);
        float matScaleX = mat.getScaleX();
        float matScaleY = mat.getScaleY();
        float width = mat.getWidth();
        float height = mat.getHeight();

        Point2D.Float location = mat.getLocation();

        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 0);
            glVertex2f(location.x * scaleX + cameraOffSetX, location.y * scaleY + cameraOffSetY);
            glTexCoord2f(matScaleX, 0);
            glVertex2f((location.x + width) * scaleX + cameraOffSetX, location.y * scaleY + cameraOffSetY);
            glTexCoord2f(matScaleX, matScaleY);
            glVertex2f((location.x + width) * scaleX + cameraOffSetX, (location.y + height) * scaleY + cameraOffSetY);
            glTexCoord2f(0, matScaleY);
            glVertex2f(location.x * scaleX + cameraOffSetX, (location.y + height) * scaleY + cameraOffSetY);
        }
        glEnd();

        unbindTexture();
        setTextures(false);
    }

    public void drawRect(int x, int y, int width, int height) {

        glBegin(GL_LINE_LOOP);
        {
            glVertex2f(x * scaleX + cameraOffSetX, y * scaleY + cameraOffSetY);
            glVertex2f((x + width) * scaleX + cameraOffSetX, y * scaleY + cameraOffSetY);
            glVertex2f((x + width) * scaleX + cameraOffSetX, (y + height) * scaleY + cameraOffSetY);
            glVertex2f(x * scaleX + cameraOffSetX, (y + height) * scaleY + cameraOffSetY);
            glVertex2f(x * scaleX + cameraOffSetX, y * scaleY + cameraOffSetY);
        }
        glEnd();

    }

    public void drawRotatedMaterial(Material mat, int angle) {
        glPushMatrix();
        glTranslatef((float) mat.getLocation().x * scaleX + cameraOffSetX, (float) mat.getLocation().y * scaleY + cameraOffSetY, 0);
        glRotatef(angle, 0, 0, 1);
        setTextures(true);
        bindTexture(mat.getTexture());
        glColor3f(1.0f, 1.0f, 1.0f);
        float matScaleX = mat.getScaleX();
        float matScaleY = mat.getScaleY();
        float halfWidth = mat.getWidth() / 2 * scaleX;
        float halfHeight = mat.getHeight() / 2 * scaleY;

        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 0);
            glVertex2f(-halfWidth, -halfHeight);
            glTexCoord2f(matScaleX, 0);
            glVertex2f(halfWidth, -halfHeight);
            glTexCoord2f(matScaleX, matScaleY);
            glVertex2f(halfWidth, halfHeight);
            glTexCoord2f(0, matScaleY);
            glVertex2f(-halfWidth, halfHeight);
        }
        glEnd();

        unbindTexture();
        setTextures(false);
        glPopMatrix();
    }
}
