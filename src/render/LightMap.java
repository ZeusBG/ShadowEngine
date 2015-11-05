/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import engine.Core;
import gameObjects.LivingObject;
import gameObjects.StaticGameObject;
import java.awt.Color;

import java.util.ArrayList;
import math.Line2f;
import math.Vector2f;

import static org.lwjgl.opengl.EXTFramebufferObject.*;

import static org.lwjgl.opengl.GL11.*;


import static org.lwjgl.opengl.GL20.*;
import static render.Renderer.CIRCLE_ACCURACY;
import math.GeometryUtil;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

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

    public LightMap(Renderer renderer, Core core) {
        this.renderer = renderer;
        this.core = core;
        width = core.getWidth();
        height = core.getHeight();
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

    public void addLight(LightArea area, Light light) {
        glBindTexture(GL_TEXTURE_2D, 0);
        switchToFrameBuffer(frameBuffer);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        
        
        if(area.getNumVertices()==0){
            return;
        }
        Vector2f lightLocation = renderer.getScaledPoint(light.getLocation());
        
        lightShader.bind();
        
        glUniform1f(glGetUniformLocation(lightShader.getProgramID(), "power"), light.getPower());
        glUniform2f(glGetUniformLocation(lightShader.getProgramID(), "lightLocation"), lightLocation.x, Display.getHeight() - lightLocation.y);
        glUniform1f(glGetUniformLocation(lightShader.getProgramID(), "scale"), renderer.getScale());
        
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
        
        removeFrameBuffer();
    }

    public void switchToFrameBuffer(int buffer) {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, buffer);
        glPushAttrib(GL_VIEWPORT_BIT);
        glViewport(0, 0, width, height);
    }
    
    public void drawDirectedPartCircle(float cx, float cy, float r, Vector2f direction, float spanAngle) {
        float spanAngleRadians = (float) (spanAngle * Math.PI / 180);
        float startAngle = (float) (Math.atan2(direction.y, direction.x) - spanAngleRadians / 2);
        
        float scaleX = renderer.getScale();
        float scaleY = renderer.getScale();
        float cameraOffSetX = renderer.getCameraOffSetX();
        float cameraOffSetY = renderer.getCameraOffSetY();
        
        cx = scaleX * cx + cameraOffSetX;
        cy = scaleY * cy + cameraOffSetY;
        r = scaleX * r;
        float step = (float) (spanAngle * Math.PI / 180 / CIRCLE_ACCURACY);
        float x =  (float)(r * Math.cos(startAngle));
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
                glVertex2f(x+cx,cy+y);
                glVertex2f(prevX+ cx, cy+prevY);
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
        Vector2f p = pointOfView.getOrientation();
        
        Vector2f p1 = new Vector2f();
        p1.x = pointOfView.getPosition().x + p.x * 10;
        p1.y = pointOfView.getPosition().y + p.y * 10;
        
        Vector2f p2 = new Vector2f();
        Vector2f p3 = new Vector2f();
        p2.x = p1.x + p.getPerpendicular().x * 20;
        p2.y = p1.y + p.getPerpendicular().y * 20;
        p3.x = p1.x + p.getPerpendicular2().x * 20;
        p3.y = p1.y + p.getPerpendicular2().y * 20;
        p2.x += (p2.x - pointOfView.getPosition().x) * 100;
        p2.y += (p2.y - pointOfView.getPosition().y) * 100;

        p3.x += (p3.x - pointOfView.getPosition().x) * 100;
        p3.y += (p3.y - pointOfView.getPosition().y) * 100;

        ArrayList<Vector2f> visibility = new ArrayList<>();
        visibility.add(new Vector2f(pointOfView.getPosition().x,pointOfView.getPosition().y));
        visibility.add(p2);
        visibility.add(p3);

        glEnable(GL_STENCIL_TEST);
        glClear(GL_STENCIL_BUFFER_BIT);
        glColorMask(false, false, false, false);

        float scaleX = renderer.getScale();
        float scaleY = renderer.getScale();
        float cameraOffSetX = renderer.getCameraOffSetX();
        float cameraOffSetY = renderer.getCameraOffSetY();

        glColorMask(false, false, false, false);
        glStencilFunc(GL_EQUAL, 1, 1);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_ZERO);

        glBegin(GL_POLYGON);
        {
            for (Vector2f point : visibility) {
                glVertex2f( scaleX * point.x + cameraOffSetX, scaleY * point.y + cameraOffSetY);
            }
        }
        glEnd();
        glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);
        drawDirectedPartCircle(pointOfView.getPosition().x, pointOfView.getPosition().y, 10, pointOfView.getOrientation(), 360);
        
        
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
        glClear(GL_STENCIL_BUFFER_BIT);
        glDisable(GL_STENCIL_TEST);
        removeFrameBuffer();
    }

    public void drawVisibilityPolygon(Vector2f center) {
        switchToFrameBuffer(frameBuffer);
        renderer.unbindTexture();
        renderer.setTextures(false);

        float scaleX = renderer.getScale();
        float scaleY = renderer.getScale();
        float cameraOffSetX = renderer.getCameraOffSetX();
        float cameraOffSetY = renderer.getCameraOffSetY();

        glColor3f(0.0f, 0.0f, 0.0f);
        ArrayList<StaticGameObject> staticObjects = core.getObjectManager().getStaticObjects();
        Line2f lineOfSight = new Line2f();
        lineOfSight.setX1(center.x);
        lineOfSight.setY1(center.y);
        for (StaticGameObject so : staticObjects) {


            for (Line2f line1 : so.getLines()) {
                lineOfSight.setX2(line1.getX1());
                lineOfSight.setY2(line1.getY1());

                for (Line2f line : so.getLines()) {
                    Vector2f intersection = GeometryUtil.getIntersectionLines(lineOfSight, line);
                    if (intersection != null && !GeometryUtil.pointsEqual(intersection, new Vector2f(lineOfSight.getX2(), lineOfSight.getY2()))) {
                        Vector2f v = new Vector2f(center, new Vector2f(line1.getX1(), line1.getY1()));
                        Vector2f v2 = new Vector2f(center, new Vector2f(line1.getX2(), line1.getY2()));
                        v.normalize();
                        v.multiply(1500);
                        v2.multiply(1500);

                        glDisable(GL_STENCIL_TEST);
                        glBegin(GL_QUADS);
                        {
                            glVertex2f( (line1.getX1() + v.x) * scaleX + cameraOffSetX,  (line1.getY1() + v.y) * scaleY + cameraOffSetY);
                            glVertex2f( (line1.getX2()+ v2.x) * scaleX + cameraOffSetX, (line1.getY2() + v2.y) * scaleY + cameraOffSetY);
                            glVertex2f( (line1.getX2()) * scaleX + cameraOffSetX, (line1.getY2()) * scaleY + cameraOffSetY);
                            glVertex2f( (line1.getX1()) * scaleX + cameraOffSetX,  (line1.getY1()) * scaleY + cameraOffSetY);
                        }
                        glEnd();
                    }
                }

                lineOfSight.setX2(line1.getX2());
                lineOfSight.setY2(line1.getY2());

                for (Line2f line : so.getLines()) {
                    Vector2f intersection = GeometryUtil.getIntersectionLines(lineOfSight, line);
                    if (intersection != null && !GeometryUtil.pointsEqual(intersection, new Vector2f(lineOfSight.getX2(), lineOfSight.getY2()))) {
                        Vector2f v = new Vector2f(center, new Vector2f(line1.getX1(), line1.getY1()));
                        Vector2f v2 = new Vector2f(center, new Vector2f(line1.getX2(), line1.getY2()));
                        v.normalize();
                        v.multiply(1500);
                        v2.multiply(1500);

                        glBegin(GL_QUADS);
                        {
                            glVertex2f( (line1.getX1() + v.x) * scaleX + cameraOffSetX,  (line1.getY1() + v.y) * scaleY + cameraOffSetY);
                            glVertex2f( (line1.getX2()+ v2.x) * scaleX + cameraOffSetX,  (line1.getY2() + v2.y) * scaleY + cameraOffSetY);
                            glVertex2f( (line1.getX2()) * scaleX + cameraOffSetX,  (line1.getY2()) * scaleY + cameraOffSetY);
                            glVertex2f( (line1.getX1()) * scaleX + cameraOffSetX, (line1.getY1()) * scaleY + cameraOffSetY);
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
        //Vector2f pos = new Vector2f(core.getObjectManager().getPlayer().getPosition().x,core.getObjectManager().getPlayer().getPosition().y);
        //glUniform2f(glGetUniformLocation(blurShader.getProgramID(), "playerView"),  pos.x,  pos.y);
        glUniform2f(glGetUniformLocation(blurShader.getProgramID(), "resolution"), width,height);
        //Vector2f pos = new Vector2f(core.getObjectManager().getPlayer().getPosition
        renderer.setTextures(true);
        glBindTexture(GL_TEXTURE_2D, frameBuffer);
        switchToFrameBuffer(tmpFrameBuffer);
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 0);
            glVertex2f(0, 0);
            glTexCoord2f(1f, 0);
            glVertex2f(width, 0);
            glTexCoord2f(1f, 1f);
            glVertex2f(width, height);
            glTexCoord2f(0, 1f);
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
            glTexCoord2f(1f, 0);
            glVertex2f(width, 0);
            glTexCoord2f(1f, 1f);
            glVertex2f(width, height);
            glTexCoord2f(0, 1f);
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
