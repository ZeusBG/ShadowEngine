/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import components.AABB;
import components.Scene;
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

    private Shader lightShader;
    private LightMap lightMap;
    private Texture tex;//for test until Z-index is added 
    private Texture floor;//again for test
    private Material mat;
    private boolean shadows = true;
    private boolean shadowBlur = true;
    private boolean light = true;

    public Renderer(Core core) {
        this.core = core;
        initialize();
        lightMap = new LightMap(this, core);
        lightMap.init();
        glEnable(GL_TEXTURE_2D);
        tex = null;
        try {//only for testing stuff
            tex = TextureLoader.getTexture("JPEG", new FileInputStream(new File("res/textures/car.jpg")));
            floor = TextureLoader.getTexture("JPEG", new FileInputStream(new File("res/textures/plates6_512x512.jpg")));
        } catch (IOException ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }
        MaterialBuilder matBuilder = new MaterialBuilder(floor, new Vector2f(0, 0)).color(org.newdawn.slick.Color.white).scale(45, 45).dimension(2000, 2000);

         mat = new Material(matBuilder);

    }

    public void render() {

        Scene scene = core.getScene();

        drawMaterial(mat);

        //draws light visibility polygons in normal buffer and in lightMap buffer
        if(light)
            castShadows();

        glColor3f(1.0f, 0.0f, 0.0f);
        glLoadIdentity();
        glBegin(GL_QUADS);
        {
            glVertex3f(100, 100,1f);
            glVertex3f(100, 120,1f);
            glVertex3f(120, 120,1f);
            glVertex3f(120, 100,1f);
            
        }
        glEnd();
        scene.getCamera().restore();
        glColor3f(1.0f,1.0f,1.0f);
        if(shadowBlur){
            lightMap.blurShadows();
            lightMap.blurShadows();
            lightMap.blurShadows();
            lightMap.blurShadows();
        }

        
        
        AABB visibleArea = core.getScene().getCamera().getVisibleArea();
        int objectsRendered = 0;
        for (DynamicGameObject obj : core.getScene().getDynamicObjects()) {
            if(!obj.getAabb().intersect(visibleArea))
                continue;
            if (obj.getMaterial() != null ) {
                drawMaterial(obj.getMaterial());
            } else {
                obj.render(this);
            }     
            objectsRendered++;
        }
        
        if(light)
            drawLightMap();
        
        for (StaticGameObject obj : core.getScene().getStaticObjects()) {
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
        if(shadows){
            lightMap.clear();
            //draws the visibility polygon for the player
            //so that he cant see behind walls and behind his back
            
            lightMap.drawVisibilityTriangle(scene.getPlayer());
            lightMap.drawVisibilityPolygon(new Vector2f(core.getScene().getPlayer().getPosition().x,core.getScene().getPlayer().getPosition().y));
            if(shadowBlur){
                lightMap.blurShadows();
                lightMap.blurShadows();
                lightMap.blurShadows();
                lightMap.blurShadows();
            }

            drawLightMap();
        }
        //core.getPhysics().getCollisionTree().drawTree(this);
        //core.getObjectManager().getRayCollisionTree().drawTree(this);
        core.getScene().getPlayer().render(this);
        glColor3f(1f,0f,0f);

        Display.update();
        Display.sync(60);

        clear();
    }

    private void initialize() {
        initLightShader("src/render/shaders/lightShader.frag");

    }

    public void drawCircle(float cx, float cy, float r) {

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
        for (GameObject obj : core.getScene().getAllObjects()) {
            
            Light light = obj.getLight();
            //object doesnt emmit light
            if (light == null) {
                continue;
            }
            //if light is out of the camera's range
            if(!light.getAABB().intersect(core.getScene().getCamera().getVisibleArea()))
                continue;
            
            LightArea area = new LightArea();
            Vector2f pos = light.getLocation();
            lightCounter++;
            //gets the points for the visibility polygon from the rayCollisionTree in object manager
            ArrayList<Vector2f> points = findIntersectionPoints(light);
            /*
            for(int i=0;i<points.size();i++){
                drawLine(light.getLocation().x, light.getLocation().y, points.get(i).x, points.get(i).y);
            }
            */
            //shrunkLight(points, light.getLocation(),light.getRadius());//so that there wont be a lot of overdraw
            
            
            
            //uses these points to draw the visibility polygon in the lightmap and in the game buffer
            //for(int i=0;i<points.size();i++){
            //    drawLine(pos.x, pos.y, points.get(i).x, points.get(i).y);
            //}
            shrunkLight(points, pos,light.getRadius());
            
            area.generateTriangles(points, pos);
            
            lightMap.addLight(area,light);
            
            
            glColor3f(1.0f,1.0f,1.0f);
            drawLightProperly(area,light);

        }
        System.out.println("number of lights rendered: "+lightCounter);
    }


    public ArrayList<Vector2f> findIntersectionPoints(Light lightSrc) {

        Vector2f lightSource = new Vector2f(lightSrc.getOwner().getPosition().x,lightSrc.getOwner().getPosition().y);
        ArrayList<Vector2f> intersectionPoints = new ArrayList<>(500);
        ArrayList<Vector2f> sPoints = new ArrayList<>(500);
        HashSet<GameObject> objects = core.getScene().getRayCollisionTree().getObjectsInRange(lightSrc.getAABB());
        ArrayList<StaticGameObject> allObjects = core.getScene().getStaticObjects();
        

        for (GameObject s : objects) {
            if (!s.getObjState().isRenderable()) {
                continue;
            }
            for (Vector2f cPoint : s.getPoints()) {
                Ray tmpRay = new Ray(lightSource, cPoint);
                if (cPoint.equals(core.getScene().getRayCollisionTree().intersect(tmpRay, objects))) {
                    addSecondaryPoints(cPoint, lightSource, sPoints);
                }
            }
        }
        
        int acc = 15;
        Vector2f lightCenter = lightSrc.getLocation();
        float step = (float)(2*Math.PI/(float)acc);
        float start = 0;
        Vector2f tmp;
        for(int i=0;i<acc;i++){
            tmp = new Vector2f();
            tmp.x = lightCenter.x+(float)Math.cos(start);
            tmp.y = lightCenter.y+(float)Math.sin(start);
            
            Ray tmpRay = new Ray(lightCenter, tmp);
            Vector2f intersection = core.getScene().getRayCollisionTree().intersect(tmpRay, null);
            if(intersection!=null){
                intersectionPoints.add(intersection);
            }
            start+=step;
        }
        
        
        for (Vector2f cPoint : sPoints) {
            Ray tmpRay = new Ray(lightSource, cPoint);
            Vector2f intersection = core.getScene().getRayCollisionTree().intersect(tmpRay, null);
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
            glVertex3f(x1, y1,0f);
            glVertex3f(x2, y2,0f);
        }
        glEnd();

    }

    public void drawRay(float x1, float y1, float x2, float y2) {

        Ray r = new Ray(x1, y1, x2, y2);
        Vector2f endPoint = core.getScene().getRayCollisionTree().intersect(r, null);

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
        glBegin(GL_TRIANGLES);
        {
            glVertex2f( a.x,  a.y);
            glVertex2f( b.x,  b.y);
            glVertex2f( c.x,  c.y);
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
                glVertex2f(p.x,p.y);
            }
        }
        glEnd();

        glColorMask(true, true, true, true);
        glStencilFunc(GL_EQUAL, 0, 1);
        glStencilOp(GL_ZERO, GL_ZERO, GL_ZERO);

        glBegin(GL_QUADS);
        {
            glVertex2f(0, 0);
            glVertex2f(0, core.getWindow().getHeight());
            glVertex2f(core.getWindow().getWidth(), core.getWindow().getHeight());
            glVertex2f(core.getWindow().getWidth(), 0);
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
            glVertex2f( p.x,  p.y);
        }
        glEnd();

        glColorMask(true, true, true, true);
        glStencilFunc(GL_EQUAL, 1, 1);
        glStencilOp(GL_ZERO, GL_ZERO, GL_ZERO);

        glBegin(GL_QUADS);
        {
            glVertex2f(0, 0);
            glVertex2f(0, core.getWindow().getHeight());
            glVertex2f(core.getWindow().getWidth(), core.getWindow().getHeight());
            glVertex2f(core.getWindow().getWidth(), 0);
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
                glVertex2f( p.x, p.y );
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

        Vector2f lightLocation = light.getLocation();
        Color lightColor = light.getColor();

        lightShader.bind();
        glUniform1f(glGetUniformLocation(lightShader.getProgramID(), "power"), light.getPower());
        glUniform2f(glGetUniformLocation(lightShader.getProgramID(), "lightLocation"), lightLocation.x, lightLocation.y );
        glUniform3f(glGetUniformLocation(lightShader.getProgramID(), "lightColor"), lightColor.getRed(), lightColor.getGreen(), lightColor.getBlue());
        glUniform1f(glGetUniformLocation(lightShader.getProgramID(), "scale"), 1920/400);//SCALEX !!!
        AABB aabb = light.getAABB();
        glBegin(GL_QUADS);
        {
            glVertex2f(aabb.getMinX(), aabb.getMinY());
            glVertex2f(aabb.getMaxX(), aabb.getMinY());
            glVertex2f(aabb.getMaxX(), aabb.getMaxY());
            glVertex2f(aabb.getMinX(), aabb.getMaxY());
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
        Vector2f lightLocation = light.getLocation();
        Color lightColor = light.getColor();
        
        lightShader.bind();
        
        float screenSizeX = core.getScene().getCamera().getWidth();
        float screenSizeY = core.getScene().getCamera().getHeight();
        float scaleX = core.getScene().getCamera().getWidthScale();
        float scaleY = core.getScene().getCamera().getHeightScale();
        float cameraPosX = core.getScene().getCamera().getX();
        float cameraPosY = core.getScene().getCamera().getY();
        
        
        glUniform1f(glGetUniformLocation(lightShader.getProgramID(), "power"), light.getPower());
        glUniform2f(glGetUniformLocation(lightShader.getProgramID(), "lightLocation"), lightLocation.x, lightLocation.y );
        glUniform3f(glGetUniformLocation(lightShader.getProgramID(), "lightColor"), lightColor.getRed(), lightColor.getGreen(), lightColor.getBlue());
        glUniform2f(glGetUniformLocation(lightShader.getProgramID(), "scale"), scaleX, scaleY);
        glUniform2f(glGetUniformLocation(lightShader.getProgramID(), "screenSize"), screenSizeX, screenSizeY);
        glUniform2f(glGetUniformLocation(lightShader.getProgramID(), "cameraCenter"),cameraPosX,cameraPosY);
        glUniform1f(glGetUniformLocation(lightShader.getProgramID(), "halfRadius"),light.getRadius()*0.5f);
        
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
        lightShader = new Shader();
        lightShader.loadShaderFromFile(path, GL_FRAGMENT_SHADER);
    }

    public void setTextures(boolean enable) {
        if (enable) {
            glEnable(GL_TEXTURE_2D);
        } else {
            glDisable(GL_TEXTURE_2D);
        }
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
        
        float width = core.getScene().getCamera().getWidth();
        float height = core.getScene().getCamera().getHeight();
        
        glColor3f(1.0f, 1.0f, 1.0f);
        setTextures(true);
        glBindTexture(GL_TEXTURE_2D, textureBuffer);
        
        glLoadIdentity();

        glBegin(GL_QUADS);
        {
            glTexCoord2f(0f, 1f);
            glVertex2f(0, 0);
            glTexCoord2f(0f, 0f);
            glVertex2f(0, height);
            
            glTexCoord2f(1f, 0f);
            glVertex2f(width,height);
            glTexCoord2f(1f, 1f);
            glVertex2f(width, 0);
        }
        glEnd();
        
        core.getScene().getCamera().restore();
        
        setTextures(false);
        unbindTexture();
        glDisable(GL_BLEND);

    }

    public void drawMaterial(Material mat) {
        setTextures(true);
        bindTexture(mat.getTexture());
        glColor3f(1.0f, 1.0f, 1.0f);
        float matScaleX = mat.getScaleX();//if the texture is not 2^n x 2^n
        float matScaleY = mat.getScaleY();//if the texture is not 2^n x 2^n
        float width = mat.getWidth();
        float height = mat.getHeight();

        Vector2f location = mat.getLocation();
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 0);
            glVertex3f(location.x , location.y ,1);
            glTexCoord2f(0, matScaleY);
            glVertex3f((location.x) , location.y+height,1);
            glTexCoord2f(matScaleX, matScaleY);
            glVertex3f((location.x + width), (location.y + height),1);
            glTexCoord2f(matScaleX, 0);
            glVertex3f(location.x + width, (location.y ),1);
        }
        glEnd();

        unbindTexture();
        setTextures(false);
    }

    public void drawRect(float x, float y, float width, float height) {

        glBegin(GL_LINE_LOOP);
        {
            glVertex2f(x , y );
            glVertex2f((x + width), y );
            glVertex2f((x + width) , (y + height));
            glVertex2f(x , (y + height));
            glVertex2f(x , y );
        }
        glEnd();

    }

    public void drawRotatedMaterial(Material mat, int angle) {
        glPushMatrix();
        glTranslatef(mat.getLocation().x, mat.getLocation().y , 0);
        glRotatef(angle, 0, 0, 1);
        setTextures(true);
        bindTexture(mat.getTexture());
        glColor3f(1.0f, 1.0f, 1.0f);
        float matScaleX = mat.getScaleX();
        float matScaleY = mat.getScaleY();
        float halfWidth = mat.getWidth() / 2 ;
        float halfHeight = mat.getHeight() / 2 ;

        glBegin(GL_QUADS);
        {
            glTexCoord3f(0, 0,0);
            glVertex3f(-halfWidth, -halfHeight,0);
            glTexCoord3f(matScaleX, 0,0);
            glVertex3f(halfWidth, -halfHeight,0);
            glTexCoord3f(matScaleX, matScaleY,0);
            glVertex3f(halfWidth, halfHeight,0);
            glTexCoord3f(0, matScaleY,0);
            glVertex3f(-halfWidth, halfHeight,0);
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
            if(distance>radius*1.5f){
                shrunkDirection.x = center.x - point.x;
                shrunkDirection.y = center.y - point.y;
                shrunkDirection.normalize();
                shrunkDirection.multiply(distance-1.2f*radius);
                
                point.x = point.x + shrunkDirection.x;
                point.y = point.y + shrunkDirection.y;
            }
        }
        
        
    }
}
