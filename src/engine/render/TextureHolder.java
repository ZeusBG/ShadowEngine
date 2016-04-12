/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.render;

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
    private static HashMap<String,Texture> textures = new HashMap();
    
    
    public static Texture get(String name,String type, String path){
        if(textures.get(name)!=null){
            System.out.println();
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
