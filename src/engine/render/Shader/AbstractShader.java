/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.render.Shader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;

/**
 *
 * @author Nick
 */
public abstract class AbstractShader {
    protected Integer programID = null;
    protected int fsid;
    protected int vsid;
    
    private static HashMap<String,Integer> loadedShaders = new HashMap();
    
    public AbstractShader(String VertexShaderFilePath,String fragmenShaderFilePath){
        programID = loadedShaders.get(VertexShaderFilePath + fragmenShaderFilePath);
        if(programID == null){
            createProgram(VertexShaderFilePath,fragmenShaderFilePath);
            loadedShaders.put(VertexShaderFilePath + fragmenShaderFilePath, programID);
        }
    }
    
    private void createProgram(String VertexShaderFilePath,String fragmenShaderFilePath){
        programID = GL20.glCreateProgram();
        vsid = loadShader(VertexShaderFilePath, GL20.GL_VERTEX_SHADER);
        fsid = loadShader(fragmenShaderFilePath, GL20.GL_FRAGMENT_SHADER);
    }
    
    public void createProgram(){
        programID = GL20.glCreateProgram();
        
    }
    
    public int loadShader(String filePath,int type){
        Integer id = loadedShaders.get(filePath);
        if(id != null ){

            //return id;
            
        }

        int shader;
        shader = glCreateShader(type);
        StringBuilder fragmentShaderSource = new StringBuilder();

        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null) {
                fragmentShaderSource.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        glShaderSource(shader, fragmentShaderSource);
        glCompileShader(shader);
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            
            IntBuffer infoLogLength = BufferUtils.createIntBuffer(1);
            GL20.glGetShader(programID, GL20.GL_INFO_LOG_LENGTH, infoLogLength);
            ByteBuffer infoLog = BufferUtils.createByteBuffer(infoLogLength.get(0));
            infoLogLength.clear();
            
            GL20.glGetShaderInfoLog(programID, infoLogLength, infoLog);
            byte[] infoLogBytes = new byte[infoLogLength.get(0)];
            infoLog.get(infoLogBytes, 0, infoLogLength.get(0));
            String v = new String(infoLogBytes, Charset.forName("UTF-8") );
            System.out.println(infoLogBytes.length);
            System.out.println(v);
            System.err.println(filePath+" not compiled");
            
        }
        glAttachShader(programID, shader);
        loadedShaders.put(filePath, shader);
        return shader;
    }
    public abstract void bindAttributeLocations();
    public void linkAndAttach(int shaderID){
        
        glLinkProgram(programID);
        glValidateProgram(programID);
    }
    public void setupShader(String filePath, int type){
        if(programID == null){
            createProgram();
        }
        int shaderID = loadShader(filePath, type);
        bindAttributeLocations();
        linkAndAttach(shaderID);
    }
    
    public int getProgramID(){
        return programID;
    }
    
}
