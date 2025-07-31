/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.mapsprite.MapSprite;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class RawImageManager {
    
    private static final String BASE_FILENAME = "mapspriteXXX-Y-Z.ext";
    
    public static MapSprite[] importImage(String basepath, int fileFormat) {
        System.out.println("com.sfc.sf2.mapsprite.io.RawImageManager.importImage() - Importing Image files ...");
        String extension = com.sfc.sf2.graphics.io.RawImageManager.GetFileExtensionString(fileFormat);
        List<MapSprite> mapSprites = new ArrayList();
        try{
            for(int i=0;i<1000;i++){
                String index = String.format("%03d", i/3);
                int facing = i%3;
                String filePath0 = basepath + BASE_FILENAME.replace("XXX-Y-Z.ext", index+"-"+facing+"-0."+extension);
                String filePath1 = basepath + BASE_FILENAME.replace("XXX-Y-Z.ext", index+"-"+facing+"-1."+extension);
                Tile[] tiles0 = com.sfc.sf2.graphics.io.RawImageManager.importImage(filePath0);
                Tile[] tiles1 = com.sfc.sf2.graphics.io.RawImageManager.importImage(filePath1);
                if(tiles0!=null && tiles1!=null){
                    if(tiles0.length==9 && tiles1.length==9){
                       MapSprite mapSprite = new MapSprite();
                       mapSprite.setIndex(i);
                       Tile[] tiles = new Tile[tiles0.length + tiles1.length];
                       System.arraycopy(tiles0, 0, tiles, 0, tiles0.length);
                       System.arraycopy(tiles1, 0, tiles, tiles0.length, tiles1.length);                    
                       mapSprite.setTiles(tiles);
                       mapSprites.add(mapSprite);
                       System.out.println("Created MapSprite " + i + " with " + tiles.length + " tiles.");                       
                    }else{
                        System.out.println("Could not create MapSprite " + i + " because of wrong lengths : tiles0=" + tiles0.length + ", tiles1=" + tiles1.length);
                    }
                }
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.mapsprite.io.RawImageManager.importImage() - Error while parsing graphics data : "+e);
        }        
        System.out.println("com.sfc.sf2.mapsprite.io.RawImageManager.importImage() - Image files imported.");
        return mapSprites.toArray(new MapSprite[mapSprites.size()]);                
    }
    
    public static void exportImage(MapSprite[] mapSprites, String basepath, int fileFormat){
        try {
            System.out.println("com.sfc.sf2.mapsprite.io.RawImageManager.exportImage() - Exporting Image files ...");
            String extension = com.sfc.sf2.graphics.io.RawImageManager.GetFileExtensionString(fileFormat);
            for(MapSprite mapSprite : mapSprites){
                String index = String.format("%03d", mapSprite.getIndex()/3);
                int facing = mapSprite.getIndex()%3;
                String filePath0 = basepath + System.getProperty("file.separator") + BASE_FILENAME.replace("XXX-Y-Z.ext", index+"-"+facing+"-0."+extension);
                com.sfc.sf2.graphics.io.RawImageManager.exportImage(mapSprite.getTiles(), filePath0, 9, fileFormat);
                String filePath1 = basepath + System.getProperty("file.separator") + BASE_FILENAME.replace("XXX-Y-Z.ext", index+"-"+facing+"-1."+extension);
                com.sfc.sf2.graphics.io.RawImageManager.exportImage(mapSprite.getTiles(), filePath1, 9, fileFormat);
            }
            System.out.println("com.sfc.sf2.mapsprite.io.RawImageManager.exportImage() - Image files exported.");
        } catch (Exception ex) {
            Logger.getLogger(RawImageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
