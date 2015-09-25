/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import components.ObjectManager;
import engine.Core;
import gameObjects.GameObject;
import gameObjects.LivingObject;
import gameObjects.StaticGameObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import utils.GeometryUtil;

/**
 *
 * @author Zeus
 */
public class Renderer {

    private Core core;
    //private Graphics2D g2d;
    private Graphics2D g2d;
    private BufferStrategy bs;
    private float cameraOffSetX;
    private float cameraOffSetY;
    private float scaleX;
    private float scaleY;
    private int lightShader;
    private int lightFragmentShader;
    private int textureShader;
    private LightMap lightMap;
    private Texture tex;
    private Texture floor;

    public Renderer(Core core) {
        this.core = core;
        initialize();
        lightMap = new LightMap(core.getWidth(), core.getHeight(),this,core);
        lightMap.init();
        glEnable(GL_TEXTURE_2D);
        tex = null;
        try {
             tex = TextureLoader.getTexture("JPEG",new FileInputStream(new File("res/textures/car.jpg")));
             floor = TextureLoader.getTexture("JPEG",new FileInputStream(new File("res/textures/plates6.jpg")));
        } catch (IOException ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void render() {
        float xPos = core.getInput().getMouseX();
        float ypos = core.getInput().getMouseY();
        
        
        
        
        
        
        ObjectManager objectsToRender = core.getObjectManager();

        if (core.getObjectManager().getCamera().isDynamic()) {
            cameraOffSetX = (float)core.getObjectManager().getCamera().getDynamicX();
            cameraOffSetY = (float)core.getObjectManager().getCamera().getDynamicY();
        } else {
            cameraOffSetX = (float)core.getObjectManager().getCamera().getX();
            cameraOffSetY = (float)core.getObjectManager().getCamera().getY();
        }
        scaleX = (float)core.getObjectManager().getCamera().getWidthScale();
        scaleY = (float)core.getObjectManager().getCamera().getHeightScale();

        cameraOffSetX *= scaleX;
        cameraOffSetY *= scaleY;
        
        
        MaterialBuilder matBuilder = new MaterialBuilder(floor,new Point2D.Float(0,0)).color(org.newdawn.slick.Color.white).scale(45, 45).dimension(2000, 2000);
        
        Material mat = new Material(matBuilder);
        drawMaterial(mat);
        
        glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
        glEnable(GL_TEXTURE_2D);
        glBegin(GL_QUADS);
           glTexCoord2f(0,0);
           glVertex2f((int)(200*scaleX+cameraOffSetX),(int)(200*scaleY+cameraOffSetY));
           glTexCoord2f(0,0.65f);
           glVertex2f((int)(225*scaleX+cameraOffSetX),(int)(200*scaleY+cameraOffSetY));
           glTexCoord2f(0.65f,0.65f);
           glVertex2f((int)(225*scaleX+cameraOffSetX),(int)(250*scaleY+cameraOffSetY));
           glTexCoord2f(0.65f,0);
           glVertex2f((int)(200*scaleX+cameraOffSetX),(int)(250*scaleY+cameraOffSetY));
        glEnd();
        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D);
          
          
          
          
        glEnable(GL_STENCIL_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        
        castShadows(objectsToRender.getPlayer());
        
        glDisable(GL_BLEND);
        glDisable(GL_STENCIL_TEST);
        //glDisable(GL_BLEND);
        glColor3f(1.0f, 0.0f, 0.0f);
        glBegin(GL_QUADS);
        glVertex2f(300, 300);
        glVertex2f(350, 300);
        glVertex2f(350, 350);
        glVertex2f(300, 350);
        glEnd();
        

       
        
        glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        glEnable(GL_STENCIL_TEST);
        //drawPolygon(findIntersectionPoints(objectsToRender.getPlayer().getLight()));
        glDisable(GL_STENCIL_TEST);
        
        //lightMap.blurShadows();
        lightMap.blurShadows();
        lightMap.blurShadows();
        lightMap.blurShadows();
        lightMap.blurShadows();

        drawLightMap();
        
         if (core.getObjectManager().getPlayer() != null) {
            core.getObjectManager().getPlayer().render(core, this);
        }

        
        glColor3f(1.0f,1.0f,1.0f);
        for(GameObject obj : core.getObjectManager().getAllObjects()){
            if(obj.getMaterial()!=null){
                drawMaterial(obj.getMaterial());
            }
            else{
                obj.render(core, this);
            }
        }

        glDisable(GL_BLEND);
        
        lightMap.clear();
        lightMap.drawVisibilityTriangle(objectsToRender.getPlayer());
        lightMap.drawVisibilityPolygon(core.getObjectManager().getPlayer().getCurrentPosition());
        //drawLightMap();
        lightMap.blurShadows();
        lightMap.blurShadows();
        lightMap.blurShadows();
        lightMap.blurShadows();
    
        
        drawLightMap();
        
        
        
        Display.update();
        Display.sync(60);
        
        
        clear();
    }

    public float getScale(){
        return scaleX;
    }
    
    private void initialize() {
        initLightShader();

    }

    public void drawCircle(double cx, double cy, double r) {
        glBegin(GL_LINE_LOOP);
        int num_segments = 100;
        cx = scaleX * cx + cameraOffSetX;
        cy = scaleY * cy + cameraOffSetY;
        r = scaleX * r;
        for (int ii = 0; ii < num_segments; ii++) {
            float theta = ii * 2.0f * 3.1415926f / (float) num_segments;//get the current angle 

            double x = r * Math.cos(theta);//calculate the x component 
            double y = r * Math.sin(theta);//calculate the y component 

            glVertex2f((float) (x + cx), (float) (y + cy));//output vertex 

        }
        glEnd();
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
        glColor3f(0.0f,0.0f,0.0f);
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
            
            ArrayList<Point2D.Double> points = findIntersectionPoints(light);
            lightMap.addLight(light,pos,points);
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
        scaled.x = (float)(scaleX * p.x + cameraOffSetX);
        scaled.y = (float)(scaleY * p.y + cameraOffSetY);
        return scaled;
    }

    public ArrayList<Point2D.Double> findIntersectionPoints(Light lightSrc) {

        Point2D.Double lightSource = lightSrc.getOwner().getCurrentPosition();
        ArrayList<Point2D.Double> intersectionPoints = new ArrayList<>();
        ArrayList<Point2D.Double> sPoints = new ArrayList<>();
        HashSet<GameObject> objects = new HashSet();
        ArrayList<StaticGameObject> allObjects = core.getObjectManager().getStaticObjects();

        for (GameObject s : allObjects) {
            if (!s.getObjState().isRenderable()) {
                continue;
            }
            for (Point2D.Double cPoint : s.getPoints()) {
                Ray tmpRay = new Ray(lightSource, cPoint);
                if (cPoint.equals(core.getObjectManager().getRayCollisionTree().intersect(tmpRay, null))) {
                    addSecondaryPoints(cPoint, lightSource, sPoints);
                }
            }
        }

        for (Point2D.Double cPoint : sPoints) {
            Ray tmpRay = new Ray(lightSource, cPoint);
            Point2D.Double intersection = core.getObjectManager().getRayCollisionTree().intersect(tmpRay, null);
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
        glVertex2f((float) (scaleX * x1 + cameraOffSetX), (float) (scaleY * y1 + cameraOffSetY));
        glVertex2f((float) (scaleX * x2 + cameraOffSetX), (float) (scaleY * y2 + cameraOffSetY));;
        glEnd();

    }

    public void drawRay(int x1, int y1, int x2, int y2) {

        Ray r = new Ray(x1, y1, x2, y2);
        Point2D.Double endPoint = core.getObjectManager().getRayCollisionTree().intersect(r, null);

        if (endPoint != null) {
            drawLine(x1, y1, (int) endPoint.x, (int) endPoint.y);
            //System.out.println("testing github");
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
        glVertex2f((float) (scaleX * a.x + cameraOffSetX), (float) (scaleY * a.y + cameraOffSetY));
        glVertex2f((float) (scaleX * b.x + cameraOffSetX), (float) (scaleY * b.y + cameraOffSetY));
        glVertex2f((float) (scaleX * c.x + cameraOffSetX), (float) (scaleY * c.y + cameraOffSetY));
        glEnd();
    }

    public void drawPolygon(ArrayList<Point2D.Double> points) {
        glEnable(GL_STENCIL_TEST);
        glClear(GL_STENCIL_BUFFER_BIT);
        glColorMask(false, false, false, false);
        glStencilFunc(GL_EQUAL, 1, 1);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_ZERO);

        glBegin(GL_POLYGON);
        for (Point2D.Double p : points) {
            glVertex2f((float) (scaleX * p.x + cameraOffSetX), (float) (scaleY * p.y + cameraOffSetY));
        }
        glEnd();

        glColorMask(true, true, true, true);
        glStencilFunc(GL_EQUAL, 0, 1);
        glStencilOp(GL_ZERO, GL_ZERO, GL_ZERO);

        glBegin(GL_QUADS);
        glVertex2f(0, 0);
        glVertex2f(core.getWidth(), 0);
        glVertex2f(core.getWidth(), core.getHeight());
        glVertex2f(0, core.getHeight());
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
        glVertex2f(0, 0);
        glVertex2f(core.getWidth(), 0);
        glVertex2f(core.getWidth(), core.getHeight());
        glVertex2f(0, core.getHeight());
        glEnd();
        glUseProgram(0);
    }

    public void drawLight(ArrayList<Point2D.Double> points, Light light) {
        glEnable(GL_STENCIL_TEST);
        glClear(GL_STENCIL_BUFFER_BIT);
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

        Point2D.Double lightLocation = getScaledPoint(light.getLocation());
        Color lightColor = light.getColor();

        glUseProgram(lightShader);
        glUniform1f(glGetUniformLocation(lightShader, "power"), (float) light.power);
        glUniform2f(glGetUniformLocation(lightShader, "lightLocation"), (float) lightLocation.x, Display.getHeight() - (float) lightLocation.y+15);
        glUniform3f(glGetUniformLocation(lightShader, "lightColor"), lightColor.getRed(), lightColor.getGreen(), lightColor.getBlue());
        glUniform1f(glGetUniformLocation(lightShader, "scale"), scaleX);

        glBegin(GL_QUADS);
        glVertex2f(0, 0);
        glVertex2f(core.getWidth(), 0);
        glVertex2f(core.getWidth(), core.getHeight());
        glVertex2f(0, core.getHeight());
        glEnd();
        glUseProgram(0);

    }

    private void initLightShader() {
        lightShader = glCreateProgram();
        lightFragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        StringBuilder fragmentShaderSource = new StringBuilder();

        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Zeus\\Desktop\\OPENGL test\\ShadowEngine\\src\\render\\lightShader.frag"));
            while ((line = reader.readLine()) != null) {
                fragmentShaderSource.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        glShaderSource(lightFragmentShader, fragmentShaderSource);
        glCompileShader(lightFragmentShader);
        if (glGetShaderi(lightFragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("light fragment shader shader not compiled!");
        } else {
            System.out.println("shader loaded successfuly");
        }

        glAttachShader(lightShader, lightFragmentShader);
        glLinkProgram(lightShader);
        glValidateProgram(lightShader);
    }

    
    public void setTextures(boolean enable){
        if(enable){
            glEnable(GL_TEXTURE_2D);
        }
        else
            glDisable(GL_TEXTURE_2D);
    }

    public float getCameraOffSetX() {
        return cameraOffSetX;
    }

    public float getCameraOffSetY() {
        return cameraOffSetY;
    }
    
    public void unbindTexture(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }
    
    public void bindTexture(Texture tex){
        glBindTexture(GL_TEXTURE_2D,tex.getTextureID());
    }
    
    public void drawLightMap() {
        int textureBuffer = lightMap.getFrameBuffer();
        glEnable(GL_BLEND);
        glBlendFunc(GL_DST_COLOR, GL_ZERO);

        glColor3f(1.0f, 1.0f, 1.0f);
        setTextures(true);
        glBindTexture(GL_TEXTURE_2D, textureBuffer);
            glBegin(GL_QUADS);
                glTexCoord2f(0f, 0f);
                glVertex2f(0, 0);
                glTexCoord2f(1f, 0f);
                glVertex2f(core.getWidth(), 0);
                glTexCoord2f(1f, 1f);
                glVertex2f(core.getWidth(), core.getHeight());
                glTexCoord2f(0f, 1f);
                glVertex2f(0, core.getHeight());
            glEnd();
        setTextures(false);
        unbindTexture();
        glDisable(GL_BLEND);
        
 
    }

    public void drawMaterial(Material mat){
        setTextures(true);
        bindTexture(mat.getTexture());
        glColor3f(1.0f,1.0f,1.0f);
        float matScaleX = mat.getScaleX();
        float matScaleY = mat.getScaleY();
        float width = mat.getWidth();
        float height = mat.getHeight();
        
        Point2D.Float location = mat.getLocation();
        
        glBegin(GL_QUADS);
            glTexCoord2f(0,0);
            glVertex2f(location.x * scaleX+cameraOffSetX,location.y * scaleY + cameraOffSetY);
            glTexCoord2f(matScaleX,0);
            glVertex2f((location.x + width) * scaleX + cameraOffSetX,location.y * scaleY + cameraOffSetY);
            glTexCoord2f(matScaleX,matScaleY);
            glVertex2f((location.x + width) * scaleX + cameraOffSetX,(location.y + height) * scaleY + cameraOffSetY);
            glTexCoord2f(0,matScaleY);
            glVertex2f(location.x * scaleX + cameraOffSetX,(location.y + height) * scaleY + cameraOffSetY);
        glEnd();
        
        unbindTexture();
        setTextures(false);

    }
    
    public void drawRotatedMaterial(Material mat){
        
    }
}
