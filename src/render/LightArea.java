/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import math.Vector2f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.ARBVertexArrayObject.*;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import org.lwjgl.opengl.GL15;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

/**
 *
 * @author Zeus
 */
public class LightArea {
    private int vao;
    private ArrayList<Integer> vbos;
    private Vector2f lightCenter;
    private int numVertices;
    private int positionsVboId;
    private int indicesVboId;
    //TODO make static flaot array with indices
    
    public LightArea(){
        vao = createVao();
        vbos = new ArrayList<>();
        positionsVboId = glGenBuffers();
        indicesVboId = glGenBuffers();
        numVertices = 0;
    }
    
    
    
    public void generateTriangles(ArrayList<Vector2f> points,Vector2f lightCenter){
        prepare();
        if(points.size()<2){
            numVertices = 0;
            return;
        }
        this.lightCenter = lightCenter;
        vao = createVao();
        vbos = new ArrayList<>();
        IntBuffer indicesBuffer = generateIndices(points);
        bindIndices(indicesBuffer);
        
        FloatBuffer positionsBuffer = generatePositions(points);
        bindPositions(positionsBuffer, 0);
        unBindVao();
        numVertices = points.size()*3;
    }
    
    private void bindIndices(IntBuffer indicesBuffer){
        vbos.add(indicesVboId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,indicesVboId);
        GL15.glBufferData(GL_ELEMENT_ARRAY_BUFFER,indicesBuffer,GL_DYNAMIC_DRAW);
    }
    
    private void bindPositions(FloatBuffer positions,int attributeNumber){
        vbos.add(positionsVboId);
        glBindBuffer(GL_ARRAY_BUFFER,positionsVboId);
        GL15.glBufferData(GL_ARRAY_BUFFER,positions,GL_DYNAMIC_DRAW);
        glVertexAttribPointer(attributeNumber,2,GL_FLOAT,false,0,0);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    
    private void prepare(){
        glDeleteVertexArrays(vao);
        for(Integer vbo : vbos)
            glDeleteBuffers(vbo);
        //vao = createVao();
        //vbos = new ArrayList<>();
    }
    
    private FloatBuffer generatePositions(ArrayList<Vector2f> points){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(2*points.size()+2);
        buffer.put(lightCenter.x);
        buffer.put(lightCenter.y);
        
        for(int i=0;i<points.size();i++){
            buffer.put(points.get(i).x);
            buffer.put(points.get(i).y);
        }
        buffer.flip();
        return buffer;
    }
    
    private IntBuffer generateIndices(ArrayList<Vector2f> points){

        IntBuffer indices = BufferUtils.createIntBuffer(3*points.size());
        for(int i=1;i<points.size()-1;i++){
            indices.put(0);
            indices.put(i);
            indices.put(i+1);
            
        }
        indices.put(0);
        indices.put(points.size()-1);
        indices.put(1);
        
        indices.flip();
        return indices;
        
    }
    
    private void unBindVao(){
        glBindVertexArray(0);
    }
    
    private int createVao(){
        int vao = glGenVertexArrays();
        glBindVertexArray(vao);
        return vao;
    }

    public int getVao() {
        return vao;
    }
    
    public int getNumVertices(){
        return numVertices;
    }
    
}
