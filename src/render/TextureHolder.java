/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 *
 * @author Zeus
 */
public class TextureHolder {
    private HashMap<String,Texture> textures;
    
    public TextureHolder(){
        textures = new HashMap<>();
    }
    
    public Texture get(String name,String type, String path){
        if(textures.get(name)!=null){
            return textures.get(name);
        }
        else{
            Texture tmp = null;
            try {
                tmp = TextureLoader.getTexture(type,new FileInputStream(new File(path)));
            } catch (IOException ex) {
                Logger.getLogger(TextureHolder.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(tmp!=null){
                textures.put(name, tmp);
                
            }
            return tmp;
        }
    }
}
