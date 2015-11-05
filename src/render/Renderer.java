/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import components.AABB;
import components.ObjectManager;
import engine.Core;
import gameObjects.DynamicGameObject;
import gameObjects.GameObject;
import gameObjects.StaticGameObject;
import java.awt.Color;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import math.GeometryUtil;
import math.Ray;
import math.Vector2f;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
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
    public static final int CIRCLE_ACCURACY = 30;

    private float cameraOffSetX;
    private float cameraOffSetY;
    private float scaleX;
    private float scaleY;
    private Shader lightShader;
    private LightMap lightMap;
    private Texture tex;//for test until Z-index is added 
    private Texture floor;
    private Material mat;

    public Renderer(Core core) {
        this.core = core;
        initialize();
        lightMap = new LightMap(this, core);
        lightMap.init();
        glEnable(GL_TEXTURE_2D);
        tex = null;
        try {
            tex = TextureLoader.getTexture("JPEG", new FileInputStream(new File("res/textures/car.jpg")));
            floor = TextureLoader.getTexture("JPEG", new FileInputStream(new File("res/textures/plates6_512x512.jpg")));
        } catch (IOException ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }
        MaterialBuilder matBuilder = new MaterialBuilder(floor, new Vector2f(0, 0)).color(org.newdawn.slick.Color.white).scale(45, 45).dimension(2000, 2000);

         mat = new Material(matBuilder);

    }

    public void render() {

        ObjectManager objectsToRender = core.getObjectManager();

        //get offsets and scales from the camera
        cameraOffSetX = core.getObjectManager().getCamera().getX();
        cameraOffSetY = core.getObjectManager().getCamera().getY();

        scaleX = core.getObjectManager().getCamera().getWidthScale();
        scaleY = core.getObjectManager().getCamera().getHeightScale();

        cameraOffSetX *= scaleX;
        cameraOffSetY *= scaleY;

        //just for test because there isnt a z-index
        

        drawMaterial(mat);
 
        //later will remove this random glEnables :D
        

        //draws light visibility polygons in normal buffer and in lightMap buffer
        castShadows();

        glColor3f(1.0f, 0.0f, 0.0f);
        glBegin(GL_QUADS);
        {
            glVertex2f(300, 300);
            glVertex2f(350, 300);
            glVertex2f(350, 350);
            glVertex2f(300, 350);
        }
        glEnd();

        lightMap.blurShadows();
       // lightMap.blurShadows();
        //lightMap.blurShadows();
        //lightMap.blurShadows();


        
        
        AABB visibleArea = core.getObjectManager().getCamera().getVisibleArea();
        int objectsRendered = 0;
        for (DynamicGameObject obj : core.getObjectManager().getDynamicObjects()) {
            if(!obj.getAabb().intersect(visibleArea))
                continue;
            if (obj.getMaterial() != null ) {
                drawMaterial(obj.getMaterial());
            } else {
                obj.render(this);
            }
            
            /*if(obj.getLight()!=null){
                glColor3f(1.0f,0,0);
                AABB lightAABB = obj.getLight().getAABB();
                drawRect(lightAABB.getMinX(),lightAABB.getMinY(),2*obj.getLight().getRadius(),2*obj.getLight().getRadius());
                System.out.println(lightAABB);
            }*/
            
            objectsRendered++;
        }
        
        if (core.getObjectManager().getPlayer() != null) {
            //core.getObjectManager().getPlayer().render(this);
        }
        
        drawLightMap();
        for (StaticGameObject obj : core.getObjectManager().getStaticObjects()) {
            if(!obj.getAabb().intersect(visibleArea))
                continue;
            if (obj.getMaterial() != null) {
                drawMaterial(obj.getMaterial());
            } else {
                obj.render(this);
            }
            objectsRendered++;
        }
        System.out.println("objectsRendered: "+objectsRendered);


        lightMap.clear();
        //draws the visibility polygon for the player
        //so that he cant see behind walls and behind his back
        lightMap.drawVisibilityTriangle(objectsToRender.getPlayer());
        lightMap.drawVisibilityPolygon(new Vector2f(core.getObjectManager().getPlayer().getPosition().x,core.getObjectManager().getPlayer().getPosition().y));
        lightMap.blurShadows();
        //lightMap.blurShadows();
        //lightMap.blurShadows();
        //lightMap.blurShadows();
                
        drawLightMap();
                
        //core.getPhysics().getCollisionTree().drawTree(this);
        //core.getObjectManager().getRayCollisionTree().drawTree(this);
        core.getObjectManager().getPlayer().render(this);
        glColor3f(1f,0f,0f);
        /*
        {
            glVertex2f(visibleArea.getMinX(),visibleArea.getMinY());
            glVertex2f(visibleArea.getMaxX(),visibleArea.getMinY());
            glVertex2f(visibleArea.getMaxX(),visibleArea.getMaxY());
            glVertex2f(visibleArea.getMinX(),visibleArea.getMaxY());
            glVertex2f(visibleArea.getMinX(),visibleArea.getMinY());
        }
        glEnd();//remove glEnd() for drun effect !
                */
        //glBegin(GL_LINE_LOOP);
        Display.update();
        Display.sync(30);

        clear();
    }

    public float getScale() {
        return scaleX;
    }

    private void initialize() {
        initLightShader("src/render/shaders/lightShader.frag");

    }

    public void drawCircle(float cx, float cy, float r) {
        cx = scaleX * cx + cameraOffSetX;
        cy = scaleY * cy + cameraOffSetY;
        r = scaleX * r;

        float step = 2.0f * 3.1415926f / CIRCLE_ACCURACY;
        float theta = 0;
        glBegin(GL_LINE_LOOP);
        {
            for (int i = 0; i < CIRCLE_ACCURACY; i++) {
                theta += step;//get the current angle 

                float x = r * (float) Math.cos(theta);//calculate the x component 
                float y = r * (float) Math.sin(theta);//calculate the y component 

                glVertex2f((x + cx), (y + cy));//output vertex 
            }
        }
        glEnd();
    }

    public void drawDirectedPartCircle(float cx, float cy, float r, Vector2f direction, float spanAngle) {
        float spanAngleRadians = (float) (spanAngle * Math.PI / 180);
        float startAngle = (float) (Math.atan2(direction.y, direction.x) - spanAngleRadians / 2);

        cx = scaleX * cx + cameraOffSetX;
        cy = scaleY * cy + cameraOffSetY;
        r = scaleX * r;
        float step = (float) (spanAngle * Math.PI / 180 / CIRCLE_ACCURACY);
        float x = (float) (r * Math.cos(startAngle));
        float y = (float) (r * Math.sin(startAngle));
        float prevX, prevY;
        for (int i = 0; i < CIRCLE_ACCURACY; i++) {
            startAngle += step;//get the current angle 
            prevX = x;
            prevY = y;
            x = (float) (r * Math.cos(startAngle));//calculate the x component 
            y = (float) (r * Math.sin(startAngle));//calculate the y component 

            //output vertex 
            glBegin(GL_TRIANGLES);
            {
                glVertex2f(cx, cy);
                glVertex2f(x + cx, y + cy);
                glVertex2f(prevX + cx, prevY + cy);
            }
            glEnd();

        }

    }

    public void clear() {

        glClear(GL_COLOR_BUFFER_BIT);
        glClear(GL_STENCIL_BUFFER_BIT);
        glDisable(GL_STENCIL_TEST);
        glDisable(GL_BLEND);
        lightMap.clear();
    }

    private void castShadows() {
        
        
        drawSight();
        
        

    }

    private void drawSight() {
        int lightCounter = 0;
        for (GameObject obj : core.getObjectManager().getAllObjects()) {
            
            Light light = obj.getLight();
            if (light == null) {
                continue;
            }
            if(!light.getAABB().intersect(core.getObjectManager().getCamera().getVisibleArea()))
                continue;
            LightArea area = new LightArea();
            Vector2f pos = getScaledPoint(light.getLocation());
            lightCounter++;
            //gets the points for the visibility polygon from the rayCollisionTree in object manager
            ArrayList<Vector2f> points = findIntersectionPoints(light);
            /*
            for(int i=0;i<points.size();i++){
                drawLine(light.getLocation().x, light.getLocation().y, points.get(i).x, points.get(i).y);
            }
            */
            shrunkLight(points, light.getLocation(),light.getRadius());//so that there wont be a lot of overdraw
            
            
            ArrayList<Vector2f> scaledPoints = new ArrayList<>();
            for(int i=0;i<points.size();i++){
                scaledPoints.add(getScaledPoint(points.get(i)));
            }
            //uses these points to draw the visibility polygon in the lightmap and in the gmae buffer
            area.generateTriangles(scaledPoints, pos);
            lightMap.addLight(area,light);
            //shrunkLight(scaledPoints, pos,light.getRadius());
            
            glColor3f(1.0f,1.0f,1.0f);
            drawLightProperly(area,light);

        }
        System.out.println("number of lights rendered: "+lightCounter);
    }

    public Vector2f getScaledPoint(Vector2f p) {
        Vector2f scaled = new Vector2f();
        scaled.x = scaleX * p.x + cameraOffSetX;
        scaled.y = scaleY * p.y + cameraOffSetY;
        return scaled;
    }

    public ArrayList<Vector2f> findIntersectionPoints(Light lightSrc) {

        Vector2f lightSource = new Vector2f(lightSrc.getOwner().getPosition().x,lightSrc.getOwner().getPosition().y);
        ArrayList<Vector2f> intersectionPoints = new ArrayList<>(500);
        ArrayList<Vector2f> sPoints = new ArrayList<>(500);
        HashSet<GameObject> objects = core.getObjectManager().getRayCollisionTree().getObjectsInRange(lightSrc.getAABB());
        ArrayList<StaticGameObject> allObjects = core.getObjectManager().getStaticObjects();
        

        for (GameObject s : objects) {
            if (!s.getObjState().isRenderable()) {
                continue;
            }
            for (Vector2f cPoint : s.getPoints()) {
                Ray tmpRay = new Ray(lightSource, cPoint);
                if (cPoint.equals(core.getObjectManager().getRayCollisionTree().intersect(tmpRay, objects))) {
                    addSecondaryPoints(cPoint, lightSource, sPoints);
                }
            }
        }
        
        Vector2f lightCenter = lightSrc.getLocation();
        float step = (float)(2*Math.PI/8.0f);
        float start = 0;
        Vector2f tmp;
        for(int i=0;i<8;i++){
            tmp = new Vector2f();
            tmp.x = lightCenter.x+(float)Math.cos(start);
            tmp.y = lightCenter.y+(float)Math.sin(start);
            
            Ray tmpRay = new Ray(lightCenter, tmp);
            Vector2f intersection = core.getObjectManager().getRayCollisionTree().intersect(tmpRay, null);
            if(intersection!=null){
                intersectionPoints.add(intersection);
            }
            start+=step;
        }
        
        
        for (Vector2f cPoint : sPoints) {
            Ray tmpRay = new Ray(lightSource, cPoint);
            Vector2f intersection = core.getObjectManager().getRayCollisionTree().intersect(tmpRay, null);
            if (intersection != null) {
                intersectionPoints.add(intersection);
            }
        }

        Collections.sort(intersectionPoints, new SortPointsClockWise(lightSource));
        
        return intersectionPoints;

    }

    public void addSecondaryPoints(Vector2f p, Vector2f lightSource, ArrayList<Vector2f> sPoints) {
        float x, y;

        y = p.x - lightSource.x;
        x = p.y - lightSource.y;

        float newX, newY;
        newX = p.x - x * 0.001f;
        newY = p.y + y * 0.001f;
        sPoints.add(new Vector2f(newX, newY));

        newX = p.x + x * 0.001f;
        newY = p.y - y * 0.001f;
        sPoints.add(new Vector2f(newX, newY));

    }

    public void drawLine(float x1, float y1, float x2, float y2) {

        glBegin(GL_LINE_LOOP);
        {
            glVertex2f(scaleX * x1 + cameraOffSetX, scaleY * y1 + cameraOffSetY);
            glVertex2f(scaleX * x2 + cameraOffSetX, scaleY * y2 + cameraOffSetY);
        }
        glEnd();

    }

    public void drawRay(float x1, float y1, float x2, float y2) {

        Ray r = new Ray(x1, y1, x2, y2);
        Vector2f endPoint = core.getObjectManager().getRayCollisionTree().intersect(r, null);

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

    public void drawTriangle(Vector2f a, Vector2f b, Vector2f c) {
        glBegin(GL_QUADS);
        {
            glVertex2f(scaleX * a.x + cameraOffSetX, scaleY * a.y + cameraOffSetY);
            glVertex2f(scaleX * b.x + cameraOffSetX, scaleY * b.y + cameraOffSetY);
            glVertex2f(scaleX * c.x + cameraOffSetX, scaleY * c.y + cameraOffSetY);
        }
        glEnd();
    }

    public void drawPolygon(ArrayList<Vector2f> points) {
        glEnable(GL_STENCIL_TEST);
        glClear(GL_STENCIL_BUFFER_BIT);
        glColorMask(false, false, false, false);
        glStencilFunc(GL_EQUAL, 1, 1);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_ZERO);

        glBegin(GL_POLYGON);
        {
            for (Vector2f p : points) {
                glVertex2f(scaleX * p.x + cameraOffSetX, scaleY * p.y + cameraOffSetY);
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

    public void drawReversedPolygon(ArrayList<Vector2f> points) {

        glColorMask(false, false, false, false);
        glStencilFunc(GL_EQUAL, 1, 1);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_ZERO);

        glBegin(GL_POLYGON);
        for (Vector2f p : points) {
            glVertex2f(scaleX * p.x + cameraOffSetX, scaleY * p.y + cameraOffSetY);
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

    public void drawLight(ArrayList<Vector2f> points, Light light) {
        
        glEnable(GL_STENCIL_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        
        
        glEnable(GL_STENCIL_TEST);
        glClear(GL_STENCIL_BUFFER_BIT);
        glColorMask(false, false, false, false);
        glStencilFunc(GL_EQUAL, 0, 1);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_INCR);

        glBegin(GL_POLYGON);
        {
            for (Vector2f p : points) {
                glVertex2f(scaleX * p.x + cameraOffSetX, scaleY * p.y + cameraOffSetY);
            }
        }
        glEnd();

        //the second drawing is because light can be less than 360 degrees
        glStencilFunc(GL_EQUAL, 1, 1);
        glStencilOp(GL_ZERO, GL_ZERO, GL_INCR);
        drawDirectedPartCircle(light.getLocation().x, light.getLocation().y, light.getRadius(), light.getDirection(), light.getSpanAngle());

        glColorMask(true, true, true, true);
        glStencilFunc(GL_EQUAL, 2, 2);
        glStencilOp(GL_ZERO, GL_ZERO, GL_ZERO);

        Vector2f lightLocation = getScaledPoint(light.getLocation());
        Color lightColor = light.getColor();

        lightShader.bind();
        glUniform1f(glGetUniformLocation(lightShader.getProgramID(), "power"), light.getPower());
        glUniform2f(glGetUniformLocation(lightShader.getProgramID(), "lightLocation"), lightLocation.x, Display.getHeight() - lightLocation.y + 15);
        glUniform3f(glGetUniformLocation(lightShader.getProgramID(), "lightColor"), lightColor.getRed(), lightColor.getGreen(), lightColor.getBlue());
        glUniform1f(glGetUniformLocation(lightShader.getProgramID(), "scale"), scaleX);
        AABB aabb = light.getAABB();
        glBegin(GL_QUADS);
        {
            glVertex2f(scaleX * aabb.getMinX() + cameraOffSetX, scaleY * aabb.getMinY() + cameraOffSetY);
            glVertex2f(scaleX * aabb.getMaxX() + cameraOffSetX, scaleY * aabb.getMinY() + cameraOffSetY);
            glVertex2f(scaleX * aabb.getMaxX() + cameraOffSetX, scaleY * aabb.getMaxY() + cameraOffSetY);
            glVertex2f(scaleX * aabb.getMinX() + cameraOffSetX, scaleY * aabb.getMaxY() + cameraOffSetY);
        }
        glEnd();
        glDisable(GL_BLEND);
        glDisable(GL_STENCIL_TEST);
        glClear(GL_STENCIL_BUFFER_BIT);
        lightShader.unbind();

    }
    
    public void drawLightProperly(LightArea area,Light light) {
        if(area.getNumVertices()==0){
            return;
        }
        Vector2f lightLocation = getScaledPoint(light.getLocation());
        Color lightColor = light.getColor();
        
        lightShader.bind();
        
        glUniform1f(glGetUniformLocation(lightShader.getProgramID(), "power"), light.getPower());
        glUniform2f(glGetUniformLocation(lightShader.getProgramID(), "lightLocation"), lightLocation.x, Display.getHeight() - lightLocation.y + 15);
        glUniform3f(glGetUniformLocation(lightShader.getProgramID(), "lightColor"), lightColor.getRed(), lightColor.getGreen(), lightColor.getBlue());
        glUniform1f(glGetUniformLocation(lightShader.getProgramID(), "scale"), scaleX);
        
        glColor3f(1.0f,0,0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glBindVertexArray(area.getVao());
        glEnableVertexAttribArray(0);
	glDrawElements(GL11.GL_TRIANGLES, area.getNumVertices(), GL11.GL_UNSIGNED_INT, 0);
	glDisableVertexAttribArray(0);
	glBindVertexArray(0);
        glDisable(GL_BLEND);
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
            glTexCoord2f(0f, 1f);
            glVertex2f(0, 0);
            glTexCoord2f(1f, 1f);
            glVertex2f(core.getWidth(), 0);
            glTexCoord2f(1f, 0f);
            glVertex2f(core.getWidth(), core.getHeight());
            glTexCoord2f(0f, 0f);
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

        Vector2f location = mat.getLocation();
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
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

    public void drawRect(float x, float y, float width, float height) {

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
        glTranslatef(mat.getLocation().x * scaleX + cameraOffSetX, mat.getLocation().y * scaleY + cameraOffSetY, 0);
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
    
    private void shrunkLight(ArrayList<Vector2f> points, Vector2f center,float radius){
        
        Vector2f shrunkDirection = new Vector2f();
        float distance;
        for(Vector2f point: points){
            distance = GeometryUtil.getDistance(point, center);
            if(distance>2*radius){
                shrunkDirection.x = center.x - point.x;
                shrunkDirection.y = center.y - point.y;
                shrunkDirection.normalize();
                shrunkDirection.multiply(distance-2*radius);
                
                point.x = point.x + shrunkDirection.x;
                point.y = point.y + shrunkDirection.y;
            }
        }
        
        
    }
}
