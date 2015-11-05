/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import gameObjects.GameObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import math.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 *
 * @author Zeus
 */
public class Material {
    private Texture texture;
    private Vector2f location;
    private float offSetX,offSetY;
    private Vector2f direction;
    private Color color;
    private GameObject owner;
    private float scaleX,scaleY;
    private float width,height;
    
    
    
    public Material(Texture texture, Vector2f location, Color color) {
        this.texture = texture;
        this.location = location;
        this.color = color;
        offSetX = 0;
        offSetY = 0;
        owner = null;
    }
    
    public Material(Texture texture, float x,float y, Color color) {
        this.texture = texture;
        location = new Vector2f(x,y);
        this.color = color;
        offSetX = 0;
        offSetY = 0;
        owner = null;
    }
    
    public Material(Texture texture, float x,float y) {
        this.texture = texture;
        location = new Vector2f(x,y);
        this.color = new Color(1.0f,1.0f,1.0f);
        offSetX = 0;
        offSetY = 0;
        owner = null;
    }
    
    public Vector2f getLocation(){
        if(owner==null)
            return location;
        else
            return location;
    }

    public float getOffsetx() {
        return offSetX;
    }

    public void setOffsetx(float offsetx) {
        this.offSetX = offsetx;
    }

    public float getOffsety() {
        return offSetY;
    }

    public void setOffsety(float offsety) {
        this.offSetY = offsety;
    }

    public Vector2f getDirection() {
        return direction;
    }

    public void setDirection(Vector2f direction) {
        this.direction = direction;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public GameObject getOwner() {
        return owner;
    }

    public void setOwner(GameObject owner) {
        this.owner = owner;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
    
    public Material(MaterialBuilder builder){
        this.texture = builder.tex;
        this.location = builder.location;
        this.color = builder.color;
        this.offSetX = builder.offSetX;
        this.offSetY = builder.offSetY;
        this.owner = builder.owner;
        this.direction = builder.direction;
        this.width = builder.width;
        this.height = builder.height;
        this.scaleX = builder.scaleX;
        this.scaleY = builder.scaleY;
    }

    public float getOffSetX() {
        return offSetX;
    }

    public void setOffSetX(float offSetX) {
        this.offSetX = offSetX;
    }

    public float getOffSetY() {
        return offSetY;
    }

    public void setOffSetY(float offSetY) {
        this.offSetY = offSetY;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
    
    public static class MaterialBuilder {
        // Required parameters
                private Texture tex = null;
		private Vector2f location = null;

		// Optional parameters - initialize with default values
		private Color color = Color.white;
                private float offSetX = 0;
                private float offSetY = 0;
                private Vector2f direction = null;
                private GameObject owner = null;
                private float scaleX = 1;
                private float scaleY = 1;
                private float width = 0;
                private float height = 0;
                

		public MaterialBuilder(Texture tex, Vector2f location) {
                    this.tex = tex;
                    this.location = location;
		}
                
                public MaterialBuilder(String type,String path, Vector2f location) {
                    
                    try {
                        tex = TextureLoader.getTexture(type,new FileInputStream(new File(path)));
                    } catch (IOException ex) {
                        Logger.getLogger(Material.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    this.location = location;
		}

		public MaterialBuilder color(Color color) {
			this.color = color;
			return this;
		}

		public MaterialBuilder offset(float offSetX,float offSetY) {
			this.offSetX = offSetX;
                        this.offSetY = offSetY;
			return this;
		}

		public MaterialBuilder owner(GameObject owner) {
			this.owner = owner;
			return this;
		}
                
                public MaterialBuilder scale(float scaleX, float scaleY) {
			this.scaleX = scaleX;
                        this.scaleY = scaleY;
			return this;
		}
                public MaterialBuilder dimension(float width, float height) {
			this.width = width;
                        this.height = height;
			return this;
		}
                
                
		public Material build() {
			return new Material(this);
		}

	}
    
}
