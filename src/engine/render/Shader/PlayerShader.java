/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.render.Shader;

import org.lwjgl.opengl.GL20;

/**
 *
 * @author Nick
 */
public class PlayerShader extends AbstractShader{

    public PlayerShader(String VertexShaderfilePath,String fragmentShaderFilePath) {
        super(VertexShaderfilePath ,fragmentShaderFilePath);
        bindAttributeLocations();
    }
    

    @Override
    public void bindAttributeLocations() {
        GL20.glBindAttribLocation(programID, 0, "in_Position");
        // Color information will be attribute 1
        GL20.glBindAttribLocation(programID, 1, "in_Color");
        // Textute information will be attribute 2
        GL20.glBindAttribLocation(programID, 2, "in_TextureCoord");
        
        linkAndAttach(vsid);
        linkAndAttach(fsid);
    }

 
    
}
