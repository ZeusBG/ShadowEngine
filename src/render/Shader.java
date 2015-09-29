/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

/**
 *
 * @author Zeus
 */
public class Shader {
    private int programID;
    
    public Shader(String file){
        loadShaderFromFile(file);
    }
    
    public void loadShaderFromFile(String path){
        int fragmentShader;
        programID = glCreateProgram();
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
            GL20.glGetShader(programID, GL20.GL_INFO_LOG_LENGTH, infoLogLength);
            ByteBuffer infoLog = BufferUtils.createByteBuffer(infoLogLength.get(0));
            infoLogLength.clear();
            
            GL20.glGetShaderInfoLog(programID, infoLogLength, infoLog);
            byte[] infoLogBytes = new byte[infoLogLength.get(0)];
            infoLog.get(infoLogBytes, 0, infoLogLength.get(0));
            String v = new String(infoLogBytes, Charset.forName("UTF-8") );
            System.out.println(infoLogBytes.length);
            System.out.println(v);
            System.err.println(path+" not compiled");
            
            System.out.println("asdasd");
            
        }

        glAttachShader(programID, fragmentShader);
        glLinkProgram(programID);
        glValidateProgram(programID);
    }
    
    public void bind(){
        glUseProgram(programID);
    }
    public void unbind(){
        glUseProgram(0);
    }

    public int getProgramID() {
        return programID;
    }
    
    
}
