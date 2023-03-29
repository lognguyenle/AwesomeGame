package com.lolop.awesomegame;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TextureLoader{
    
    static private Map<String, File> keysToTextureFile;
    File directory = new File("AwesomeGame/assets");
    File[] directoryArray = directory.listFiles();
    
    public TextureLoader(){
        keysToTextureFile = new HashMap<>();
        for(File child : directoryArray){
            if(child.getName().contains("png") || child.getName().contains("jpg")){
                System.out.println(child.getName().substring(0, child.getName().lastIndexOf(".")));
                keysToTextureFile.put(child.getName().substring(0, child.getName().lastIndexOf(".")), child);
            }
        }
    }

    public static void load(){
        new TextureLoader();
    }

    // public static void main(String[] args) {
    //     TextureLoader test = new TextureLoader();
    //     System.out.println(keysToTextureFile.toString());
    //     System.out.println(keysToTextureFile.get("Asset 1").getName());
    //     generateDrawable("Asset 1");
    // }

    public static TextureRegionDrawable generateDrawable(String id){
        return new TextureRegionDrawable(
            new TextureRegion(
                new Texture(
                    new Pixmap(new FileHandle(keysToTextureFile.get(id))))));
    }

}