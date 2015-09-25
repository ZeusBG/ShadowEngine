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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import math.Vector;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL20.*;
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
    
    private int lightShader;
    private int blurShader;
    private Core core;
    
    
    public LightMap(int width,int height,Renderer renderer,Core core){
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

    
    public int compileFragmentShader(String path){
        int program;
        int fragmentShader;
        program = glCreateProgram();
        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        StringBuilder fragmentShaderSource = new StringBuilder();

        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(path));
            while ((line = reader.readLine()) != null) {
                fragmentShaderSource.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            
            IntBuffer infoLogLength = BufferUtils.createIntBuffer(1);
            GL20.glGetShader(program, GL20.GL_INFO_LOG_LENGTH, infoLogLength);
            ByteBuffer infoLog = BufferUtils.createByteBuffer(infoLogLength.get(0));
            infoLogLength.clear();
            
            GL20.glGetShaderInfoLog(program, infoLogLength, infoLog);
            byte[] infoLogBytes = new byte[infoLogLength.get(0)];
            infoLog.get(infoLogBytes, 0, infoLogLength.get(0));
            String v = new String(infoLogBytes, Charset.forName("UTF-8") );
            System.out.println(infoLogBytes.length);
            System.out.println(v);
            System.err.println(path+" not compiled");
            
            System.out.println("asdasd");
            
        }

        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        glValidateProgram(program);
        return program;
    }
    
    public int createFrameBuffer(){
        int frameBuffer = glGenFramebuffersEXT();
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, frameBuffer);

        int depthBuffer = glGenRenderbuffersEXT();
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthBuffer);
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_STENCIL_INDEX, width, height);

        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_STENCIL_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, depthBuffer);
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, depthBuffer);
        int frameBufferTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, frameBufferTexture);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, frameBufferTexture, 0);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        return frameBuffer;
    }
    
    public void init() {
        
        lightShader = compileFragmentShader("src/render/lightMapShader.frag");
        blurShader = compileFragmentShader("src/render/lightBlur.frag");

        frameBuffer = createFrameBuffer();
        tmpFrameBuffer = createFrameBuffer();

    }

    public void addLight(Light light,Point2D.Double pos,ArrayList<Point2D.Double> points) {
        glBindTexture(GL_TEXTURE_2D, 0);
        switchToFrameBuffer(frameBuffer);

        
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        
        glEnable(GL_STENCIL_TEST);
        glClear(GL_STENCIL_BUFFER_BIT);
        glColorMask(false, false, false, false);
        glStencilFunc(GL_EQUAL, 1, 1);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_ZERO);

        glBegin(GL_POLYGON);
        for (Point2D.Double p : points) {
            Point2D.Double p1 = renderer.getScaledPoint(p);
            glVertex2f((float) p1.x, (float) (Display.getHeight()-p1.y));
        }
        glEnd();

        glColorMask(true, true, true, true);
        glStencilFunc(GL_EQUAL, 1, 1);
        glStencilOp(GL_ZERO, GL_ZERO, GL_ZERO);

        Point2D.Double lightLocation = renderer.getScaledPoint(light.getLocation());
        Color lightColor = light.getColor();
        
        glUseProgram(lightShader);
        
        glUniform2f(glGetUniformLocation(lightShader, "lightLocation"), (float) pos.x, (float) pos.y);
        glUniform1f(glGetUniformLocation(lightShader, "power"), light.power);
        glUniform1f(glGetUniformLocation(lightShader, "scale"), renderer.getScale());
        
        glBegin(GL_QUADS);
        glVertex2f(0, 0);
        glVertex2f(width, 0);
        glVertex2f(width, height);
        glVertex2f(0, height);
        glEnd();
        glUseProgram(0);
        
        
        glClear(GL_STENCIL_BUFFER_BIT);
        glDisable(GL_STENCIL_TEST);
        removeFrameBuffer();
    }
    
    public void switchToFrameBuffer(int buffer){
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, buffer);
        glPushAttrib(GL_VIEWPORT_BIT);
        glViewport(0, 0, width, height);
    }
    
    public void removeFrameBuffer(){
        glUseProgram(0);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }
    
    public void drawVisibilityTriangle(LivingObject pointOfView){
        switchToFrameBuffer(frameBuffer);
        glColor3f(1.0f,1.0f,1.0f);
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
        for (Point2D.Double point : visibility) {
            glVertex2f((float)(scaleX * point.x + cameraOffSetX), (float)(height - (scaleY * point.y + cameraOffSetY)));
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
        glDisable(GL_STENCIL_TEST);
        removeFrameBuffer();
    }
    
    public void drawVisibilityPolygon(Point2D.Double center){
        switchToFrameBuffer(frameBuffer);
        renderer.unbindTexture();
        renderer.setTextures(false);
        
        float scaleX = renderer.getScale();
        float scaleY = renderer.getScale();
        float cameraOffSetX = renderer.getCameraOffSetX();
        float cameraOffSetY = -renderer.getCameraOffSetY();

        glColor3f(0.0f,0.0f,0.0f);
        ArrayList<StaticGameObject> staticObjects = core.getObjectManager().getStaticObjects();
        Line2D.Double lineOfSight = new Line2D.Double();
        lineOfSight.x1 = center.x;
        lineOfSight.y1 = center.y;
        for(StaticGameObject so:staticObjects){
            
            int size = so.getPoints().size();
            
            for(Line2D.Double line1 : so.getLines()){
                lineOfSight.x2 = line1.x1;
                lineOfSight.y2 = line1.y1;
                
                for(Line2D.Double line : so.getLines()){
                    Point2D.Double intersection = GeometryUtil.getIntersectionLines(lineOfSight, line);
                    if(intersection!=null && !GeometryUtil.pointsEqual(intersection,new Point2D.Double(lineOfSight.x2,lineOfSight.y2))){
                        Vector v = new Vector(center,new Point2D.Double(line1.x1,line1.y1));
                        Vector v2 = new Vector(center,new Point2D.Double(line1.x2,line1.y2));
                        v.normalize();
                        v.multiply(1500);
                        v2.multiply(1500);
                        
                        glDisable(GL_STENCIL_TEST);
                        glBegin(GL_QUADS);
                            glVertex2f((float)(line1.x1+v.x)*scaleX+cameraOffSetX,(float)(height-(line1.y1+v.y)*scaleY+cameraOffSetY));
                            glVertex2f((float)(line1.x2+v2.x)*scaleX+cameraOffSetX,(float)(height-(line1.y2+v2.y)*scaleY+cameraOffSetY));
                            glVertex2f((float)(line1.x2)*scaleX+cameraOffSetX,(float)(height-(line1.y2)*scaleY+cameraOffSetY));
                            glVertex2f((float)(line1.x1)*scaleX+cameraOffSetX,(float)(height-(line1.y1)*scaleY+cameraOffSetY));
                        glEnd();
                    }
                }
                
                lineOfSight.x2 = line1.x2;
                lineOfSight.y2 = line1.y2;
                
                for(Line2D.Double line : so.getLines()){
                    Point2D.Double intersection = GeometryUtil.getIntersectionLines(lineOfSight, line);
                    if(intersection!=null && !GeometryUtil.pointsEqual(intersection,new Point2D.Double(lineOfSight.x2,lineOfSight.y2))){
                        Vector v = new Vector(center,new Point2D.Double(line1.x1,line1.y1));
                        Vector v2 = new Vector(center,new Point2D.Double(line1.x2,line1.y2));
                        v.normalize();
                        v.multiply(1500);
                        v2.multiply(1500);
                        

                        glBegin(GL_QUADS);
                            glVertex2f((float)(line1.x1+v.x)*scaleX+cameraOffSetX,(float)(height-(line1.y1+v.y)*scaleY+cameraOffSetY));
                            glVertex2f((float)(line1.x2+v2.x)*scaleX+cameraOffSetX,(float)(height-(line1.y2+v2.y)*scaleY+cameraOffSetY));
                            glVertex2f((float)(line1.x2)*scaleX+cameraOffSetX,(float)(height-(line1.y2)*scaleY+cameraOffSetY));
                            glVertex2f((float)(line1.x1)*scaleX+cameraOffSetX,(float)(height-(line1.y1)*scaleY+cameraOffSetY));
                        glEnd();
                    }
                }
            }
        }
        glColor3f(1.0f,1.0f,1.0f);
        removeFrameBuffer();
    }
    
    public void blurShadows(){
        glUseProgram(blurShader);
        glUniform2f(glGetUniformLocation(blurShader, "dir"), 0f,1f);
        Point2D.Double pos = core.getObjectManager().getPlayer().getCurrentPosition();
        glUniform2f(glGetUniformLocation(blurShader, "playerView"), (float)pos.x,(float)pos.y);
        renderer.setTextures(true);
        glBindTexture(GL_TEXTURE_2D, frameBuffer);
        switchToFrameBuffer(tmpFrameBuffer);
            glBegin(GL_QUADS);
                glTexCoord2f(0,0);
                glVertex2f(0,0);
                glTexCoord2f(1,0);
                glVertex2f(width,0);
                glTexCoord2f(1,1);
                glVertex2f(width,height);
                glTexCoord2f(0,1);
                glVertex2f(0,height);
            glEnd();
        renderer.setTextures(false);
        renderer.unbindTexture();
        
        glUseProgram(blurShader);
        glUniform2f(glGetUniformLocation(blurShader, "dir"), 1f,0f);
        renderer.setTextures(true);
        glBindTexture(GL_TEXTURE_2D,tmpFrameBuffer);
        switchToFrameBuffer(frameBuffer);
            glBegin(GL_QUADS);
                glTexCoord2f(0,0);
                glVertex2f(0,0);
                glTexCoord2f(1,0);
                glVertex2f(width,0);
                glTexCoord2f(1,1);
                glVertex2f(width,height);
                glTexCoord2f(0,1);
                glVertex2f(0,height);
            glEnd();
        renderer.unbindTexture();
        renderer.setTextures(false);
        removeFrameBuffer();
        glUseProgram(0);
    }
    
    public void clear() {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, frameBuffer);
        glPushAttrib(GL_VIEWPORT_BIT);
        glViewport(0, 0, width, height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }
}