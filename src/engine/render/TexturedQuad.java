/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import math.Vector2f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.ARBVertexArrayObject.glDeleteVertexArrays;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;
import engine.render.Shader.AbstractShader;

/**
 *
 * @author Nick
 */
public class TexturedQuad {

    private Vector2f scale;
    private Vector2f translation;
    private float rotation;
    private Vector2f cameraOffset;
    private Vector2f resolution;

    private TexturedVertex[] vertices;

    private AbstractShader shader;
    private Texture tex;
    private int vaoId = 0;
    private int vboId = 0;
    private int vboiId = 0;
    private int indicesCount = 0;

    public TexturedQuad(Vector2f resolution, Vector2f size) {
        this.resolution = new Vector2f(resolution.x, resolution.y);
        scale = new Vector2f(1, 1);
        rotation = 0;
        translation = new Vector2f(0, 0);
        vertices = new TexturedVertex[4];
        cameraOffset = new Vector2f();
        setupQuad(size);
    }

    public void setSRT(Vector2f newScale, float newRotation, Vector2f newTranslation) {
        scale = newScale;
        rotation = newRotation;
        translation = newTranslation;
    }

    public void addSRT(Vector2f newScale, float newRotation, Vector2f newTranslation) {
        scale.x += newScale.x;
        scale.y += newScale.y;
        rotation += newRotation;
        translation.x += newTranslation.x;
        translation.y += newTranslation.y;
    }

    public void addRotation(float angle) {
        rotation += angle;
    }

    public void setShader(AbstractShader newShader) {
        shader = newShader;
    }

    private void setupQuad(Vector2f size) {
        TexturedVertex v0 = new TexturedVertex();
        v0.setXYZ(-0.5f * size.x, 0.5f * size.y, 0);
        v0.setRGB(1, 0, 0);
        v0.setST(0, 0);
        TexturedVertex v1 = new TexturedVertex();
        v1.setXYZ(-0.5f * size.x, -0.5f * size.x, 0);
        v1.setRGB(0, 1, 0);
        v1.setST(0, 1);
        TexturedVertex v2 = new TexturedVertex();
        v2.setXYZ(0.5f * size.y, -0.5f * size.x, 0);
        v2.setRGB(0, 0, 1);
        v2.setST(1, 1);
        TexturedVertex v3 = new TexturedVertex();
        v3.setXYZ(0.5f * size.x, 0.5f * size.y, 0);
        v3.setRGB(1, 1, 1);
        v3.setST(1, 0);

        TexturedVertex[] vertices = new TexturedVertex[]{v0, v1, v2, v3};

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length
                * TexturedVertex.elementCount);
        for (int i = 0; i < vertices.length; i++) {

            verticesBuffer.put(vertices[i].getElements());
        }
        verticesBuffer.flip();

        byte[] indices = {
            0, 1, 2,
            2, 3, 0
        };
        indicesCount = indices.length;
        ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(0, TexturedVertex.positionElementCount, GL11.GL_FLOAT,
                false, TexturedVertex.stride, TexturedVertex.positionByteOffset);

        GL20.glVertexAttribPointer(1, TexturedVertex.colorElementCount, GL11.GL_FLOAT,
                false, TexturedVertex.stride, TexturedVertex.colorByteOffset);

        GL20.glVertexAttribPointer(2, TexturedVertex.textureElementCount, GL11.GL_FLOAT,
                false, TexturedVertex.stride, TexturedVertex.textureByteOffset);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL30.glBindVertexArray(0);

        vboiId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        //this.exitOnGLError("setupQuad");
    }

    private void exitOnGLError(String errorMessage) {
        int errorValue = GL11.glGetError();
        System.err.println("GL error code: " + errorValue);
        if (errorValue != GL11.GL_NO_ERROR) {
            String errorString = GLU.gluErrorString(errorValue);
            System.err.println("ERROR - " + errorMessage + ": " + errorString);

            if (Display.isCreated()) {
                Display.destroy();
            }
            System.exit(-1);
        }
    }
    
    public void loadTexture(String filePath, String texType) {
        tex = null;
        tex = TextureHolder.get(filePath, texType, filePath);

        System.out.println("wtf Texture" + filePath + "successfuly loaded !");
    }
    
    public void renderQuad() {

        int shaderID = shader.getProgramID();
        GL20.glUseProgram(shaderID);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTextureID());

        GL30.glBindVertexArray(vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        glUniform2f(glGetUniformLocation(shaderID, "scale"), scale.x, scale.y);
        glUniform2f(glGetUniformLocation(shaderID, "translation"), translation.x, translation.y);
        glUniform2f(glGetUniformLocation(shaderID, "cameraOffset"), cameraOffset.x, cameraOffset.y);
        glUniform1f(glGetUniformLocation(shaderID, "rotation"), rotation);
        glUniform2f(glGetUniformLocation(shaderID, "resolution"), resolution.x, resolution.y);
        
        GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_BYTE, 0);

   
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
        glDisable(GL_BLEND);
        GL20.glUseProgram(0);
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

    public Vector2f getTranslation() {
        return translation;
    }

    public void setTranslation(Vector2f translation) {
        this.translation = translation;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Vector2f getCameraOffset() {
        return cameraOffset;
    }

    public void setCameraOffset(Vector2f cameraOffset) {
        this.cameraOffset = cameraOffset;
    }

    public void setResolution(Vector2f res) {
        resolution = res;
    }
    
    @Override
    public void finalize(){
        glDeleteVertexArrays(vaoId);
        glDeleteBuffers(vboId);
        glDeleteBuffers(vboiId);
    }

}
