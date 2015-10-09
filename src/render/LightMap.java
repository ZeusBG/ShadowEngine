/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import engine.Core;
import gameObjects.LivingObject;
import gameObjects.StaticGameObject;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import java.util.ArrayList;
import math.Vector;

import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

import static org.lwjgl.opengl.GL11.*;


import static org.lwjgl.opengl.GL20.*;
import static render.Renderer.CIRCLE_ACCURACY;
import utils.GeometryUtil;

/**
 *
 * @author Zeus
 */
public class LightMap {

    private Renderer renderer;

    private int frameBuffer;
    private int tmpFrameBuffer;

    private int width;
    private int height;

    private Shader lightShader;
    private Shader blurShader;
    private Core core;

    public LightMap(int width, int height, Renderer renderer, Core core) {
        this.width = width;
        this.height = height;
        this.renderer = renderer;
        this.core = core;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getFrameBuffer() {
        return frameBuffer;
    }

    public void init() {
        lightShader = new Shader("src/render/shaders/lightMapShader.frag");
        blurShader = new Shader("src/render/shaders/lightBlur.frag");

        frameBuffer = FrameBuffer.getFrameBuffer(width, height);
        tmpFrameBuffer = FrameBuffer.getFrameBuffer(width, height);

    }

    public void addLight(Light light, Point2D.Float pos, ArrayList<Point2D.Float> points) {
        glBindTexture(GL_TEXTURE_2D, 0);
        switchToFrameBuffer(frameBuffer);

        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        glEnable(GL_STENCIL_TEST);
        glClear(GL_STENCIL_BUFFER_BIT);
        glColorMask(false, false, false, false);
        glStencilFunc(GL_EQUAL, 0, 1);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_INCR);

        glBegin(GL_POLYGON);
        for (Point2D.Float p : points) {
            Point2D.Float p1 = renderer.getScaledPoint(p);
            glVertex2f( p1.x,  (Display.getHeight() - p1.y));
        }
        glEnd();

        
        glStencilFunc(GL_EQUAL, 1,1);
        glStencilOp(GL_ZERO, GL_ZERO, GL_INCR);
        drawDirectedPartCircle(light.getLocation().x, light.getLocation().y, light.getRadius(), light.getDirection(), light.getSpanAngle());
        
        
        
        
        
        glColorMask(true, true, true, true);
        glStencilFunc(GL_EQUAL, 2, 2);
        glStencilOp(GL_ZERO, GL_ZERO, GL_ZERO);

        lightShader.bind();

        glUniform2f(glGetUniformLocation(lightShader.getProgramID(), "lightLocation"),  pos.x,  pos.y);
        glUniform1f(glGetUniformLocation(lightShader.getProgramID(), "power"), light.getPower());
        glUniform1f(glGetUniformLocation(lightShader.getProgramID(), "scale"), renderer.getScale());
        glUniform1f(glGetUniformLocation(lightShader.getProgramID(), "radius"), light.getRadius());

        glBegin(GL_QUADS);
        glVertex2f(0, 0);
        glVertex2f(width, 0);
        glVertex2f(width, height);
        glVertex2f(0, height);
        glEnd();
        lightShader.unbind();

        glClear(GL_STENCIL_BUFFER_BIT);
        glDisable(GL_STENCIL_TEST);
        removeFrameBuffer();
    }

    public void switchToFrameBuffer(int buffer) {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, buffer);
        glPushAttrib(GL_VIEWPORT_BIT);
        glViewport(0, 0, width, height);
    }
    
    public void drawDirectedPartCircle(float cx, float cy, float r, Vector direction, float spanAngle) {
        float spanAngleRadians = (float) (spanAngle * Math.PI / 180);
        float startAngle = (float) (Math.atan2(direction.y, direction.x) - spanAngleRadians / 2);
        
        float scaleX = renderer.getScale();
        float scaleY = renderer.getScale();
        float cameraOffSetX = renderer.getCameraOffSetX();
        float cameraOffSetY = renderer.getCameraOffSetY();
        
        cx = scaleX * cx + cameraOffSetX;
        cy = Display.getHeight()-scaleY * cy - cameraOffSetY+10;
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
                glVertex2f(x+cx,cy-y);
                glVertex2f(prevX+ cx, cy-prevY);
            }
            glEnd();

        }

    }

    public void removeFrameBuffer() {
        glUseProgram(0);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    public void drawVisibilityTriangle(LivingObject pointOfView) {
        switchToFrameBuffer(frameBuffer);
        glColor3f(1.0f, 1.0f, 1.0f);
        Vector p = pointOfView.getOrientation();
        Point2D.Float p1 = new Point2D.Float();
        p1.x = pointOfView.getCurrentPosition().x + p.x * 10;
        p1.y = pointOfView.getCurrentPosition().y + p.y * 10;
        Point2D.Float p2 = new Point2D.Float();
        Point2D.Float p3 = new Point2D.Float();
        p2.x = p1.x + p.getPerpendicular().x * 20;
        p2.y = p1.y + p.getPerpendicular().y * 20;
        p3.x = p1.x + p.getPerpendicular2().x * 20;
        p3.y = p1.y + p.getPerpendicular2().y * 20;
        p2.x += (p2.x - pointOfView.getCurrentPosition().x) * 1000;
        p2.y += (p2.y - pointOfView.getCurrentPosition().y) * 1000;

        p3.x += (p3.x - pointOfView.getCurrentPosition().x) * 1000;
        p3.y += (p3.y - pointOfView.getCurrentPosition().y) * 1000;

        ArrayList<Point2D.Float> visibility = new ArrayList<>();
        visibility.add(pointOfView.getCurrentPosition());
        visibility.add(p2);
        visibility.add(p3);

        glEnable(GL_STENCIL_TEST);
        glClear(GL_STENCIL_BUFFER_BIT);
        glColorMask(false, false, false, false);
        glStencilFunc(GL_EQUAL, 1, 1);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_ZERO);

        float scaleX = renderer.getScale();
        float scaleY = renderer.getScale();
        float cameraOffSetX = renderer.getCameraOffSetX();
        float cameraOffSetY = renderer.getCameraOffSetY();

        glColorMask(false, false, false, false);
        glStencilFunc(GL_EQUAL, 1, 1);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_ZERO);

        glBegin(GL_POLYGON);
        {
            for (Point2D.Float point : visibility) {
                glVertex2f( scaleX * point.x + cameraOffSetX, height - (scaleY * point.y + cameraOffSetY));
            }
        }
        glEnd();
        glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);
        drawDirectedPartCircle(pointOfView.getCurrentPosition().x, pointOfView.getCurrentPosition().y, 10, pointOfView.getOrientation(), 360);
        
        
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
        glDisable(GL_STENCIL_TEST);
        removeFrameBuffer();
    }

    public void drawVisibilityPolygon(Point2D.Float center) {
        switchToFrameBuffer(frameBuffer);
        renderer.unbindTexture();
        renderer.setTextures(false);

        float scaleX = renderer.getScale();
        float scaleY = renderer.getScale();
        float cameraOffSetX = renderer.getCameraOffSetX();
        float cameraOffSetY = -renderer.getCameraOffSetY();

        glColor3f(0.0f, 0.0f, 0.0f);
        ArrayList<StaticGameObject> staticObjects = core.getObjectManager().getStaticObjects();
        Line2D.Float lineOfSight = new Line2D.Float();
        lineOfSight.x1 = center.x;
        lineOfSight.y1 = center.y;
        for (StaticGameObject so : staticObjects) {


            for (Line2D.Float line1 : so.getLines()) {
                lineOfSight.x2 = line1.x1;
                lineOfSight.y2 = line1.y1;

                for (Line2D.Float line : so.getLines()) {
                    Point2D.Float intersection = GeometryUtil.getIntersectionLines(lineOfSight, line);
                    if (intersection != null && !GeometryUtil.pointsEqual(intersection, new Point2D.Float(lineOfSight.x2, lineOfSight.y2))) {
                        Vector v = new Vector(center, new Point2D.Float(line1.x1, line1.y1));
                        Vector v2 = new Vector(center, new Point2D.Float(line1.x2, line1.y2));
                        v.normalize();
                        v.multiply(1500);
                        v2.multiply(1500);

                        glDisable(GL_STENCIL_TEST);
                        glBegin(GL_QUADS);
                        {
                            glVertex2f( (line1.x1 + v.x) * scaleX + cameraOffSetX,  (height - (line1.y1 + v.y) * scaleY + cameraOffSetY));
                            glVertex2f( (line1.x2 + v2.x) * scaleX + cameraOffSetX,  (height - (line1.y2 + v2.y) * scaleY + cameraOffSetY));
                            glVertex2f( (line1.x2) * scaleX + cameraOffSetX,  (height - (line1.y2) * scaleY + cameraOffSetY));
                            glVertex2f( (line1.x1) * scaleX + cameraOffSetX,  (height - (line1.y1) * scaleY + cameraOffSetY));
                        }
                        glEnd();
                    }
                }

                lineOfSight.x2 = line1.x2;
                lineOfSight.y2 = line1.y2;

                for (Line2D.Float line : so.getLines()) {
                    Point2D.Float intersection = GeometryUtil.getIntersectionLines(lineOfSight, line);
                    if (intersection != null && !GeometryUtil.pointsEqual(intersection, new Point2D.Float(lineOfSight.x2, lineOfSight.y2))) {
                        Vector v = new Vector(center, new Point2D.Float(line1.x1, line1.y1));
                        Vector v2 = new Vector(center, new Point2D.Float(line1.x2, line1.y2));
                        v.normalize();
                        v.multiply(1500);
                        v2.multiply(1500);

                        glBegin(GL_QUADS);
                        {
                            glVertex2f( (line1.x1 + v.x) * scaleX + cameraOffSetX,  (height - (line1.y1 + v.y) * scaleY + cameraOffSetY));
                            glVertex2f( (line1.x2 + v2.x) * scaleX + cameraOffSetX,  (height - (line1.y2 + v2.y) * scaleY + cameraOffSetY));
                            glVertex2f( (line1.x2) * scaleX + cameraOffSetX,  (height - (line1.y2) * scaleY + cameraOffSetY));
                            glVertex2f( (line1.x1) * scaleX + cameraOffSetX,  (height - (line1.y1) * scaleY + cameraOffSetY));
                        }
                        glEnd();
                    }
                }
            }
        }
        glColor3f(1.0f, 1.0f, 1.0f);
        removeFrameBuffer();
    }

    public void blurShadows() {
        blurShader.bind();
        glUniform2f(glGetUniformLocation(blurShader.getProgramID(), "dir"), 0f, 1f);
        Point2D.Float pos = core.getObjectManager().getPlayer().getCurrentPosition();
        glUniform2f(glGetUniformLocation(blurShader.getProgramID(), "playerView"),  pos.x,  pos.y);
        renderer.setTextures(true);
        glBindTexture(GL_TEXTURE_2D, frameBuffer);
        switchToFrameBuffer(tmpFrameBuffer);
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 0);
            glVertex2f(0, 0);
            glTexCoord2f(1, 0);
            glVertex2f(width, 0);
            glTexCoord2f(1, 1);
            glVertex2f(width, height);
            glTexCoord2f(0, 1);
            glVertex2f(0, height);
        }
        glEnd();
        renderer.setTextures(false);
        renderer.unbindTexture();

        glUniform2f(glGetUniformLocation(blurShader.getProgramID(), "dir"), 1f, 0f);
        renderer.setTextures(true);
        glBindTexture(GL_TEXTURE_2D, tmpFrameBuffer);
        switchToFrameBuffer(frameBuffer);
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 0);
            glVertex2f(0, 0);
            glTexCoord2f(1, 0);
            glVertex2f(width, 0);
            glTexCoord2f(1, 1);
            glVertex2f(width, height);
            glTexCoord2f(0, 1);
            glVertex2f(0, height);
        }
        glEnd();
        renderer.unbindTexture();
        renderer.setTextures(false);
        removeFrameBuffer();
        blurShader.unbind();
    }

    public void clear() {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, frameBuffer);
        glPushAttrib(GL_VIEWPORT_BIT);
        glViewport(0, 0, width, height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }
}
