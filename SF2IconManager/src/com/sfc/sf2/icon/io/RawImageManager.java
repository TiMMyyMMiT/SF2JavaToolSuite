/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.icon.io;

import com.sfc.sf2.graphics.Tile;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class RawImageManager {
    
    private static final String CHARACTER_FILENAME = "iconXXX.fle";
    
    public static Tile[] importImage(String basepath, int fileFormat) {
        System.out.println("com.sfc.sf2.icon.io.ImageManager.importImage() - Importing Image files ...");
        int count = new File(basepath).list().length;
        List<Tile[]> icons = new ArrayList();
        try{
            for(int i=0;i<count;i++){
                String index = String.format("%03d", i);
                String filePath = basepath + CHARACTER_FILENAME.replace("XXX.fle", index+"."+com.sfc.sf2.graphics.io.RawImageManager.GetFileExtensionString(fileFormat));
                System.out.println("Parsing " + filePath);
                Tile[] iconTiles = com.sfc.sf2.graphics.io.RawImageManager.importImage(filePath);
                icons.add(iconTiles);
                System.out.println("Parsed " + filePath);
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.icon.io.ImageManager.importImage() - Error while parsing icon data : "+e);
        }        
        Tile[] iconsArray = new Tile[icons.size()*6];
        for(int i=0;i<icons.size();i++){
            System.arraycopy(icons.get(i), 0, iconsArray, i*6, 6);
        }
        System.out.println("com.sfc.sf2.icon.io.ImageManager.importImage() - Image files imported.");
        return iconsArray;                
    }
    
    public static void exportImage(Tile[] tiles, String basepath, int fileFormat) {
        try {
            System.out.println("com.sfc.sf2.icon.io.ImageManager.exportImage() - Exporting Image files ...");
            for(int i = 0;i<tiles.length;i+=6){
                String index = String.format("%03d", i/6);
                String filePath = basepath + System.getProperty("file.separator") + CHARACTER_FILENAME.replace("XXX.fle", index+"."+com.sfc.sf2.graphics.io.RawImageManager.GetFileExtensionString(fileFormat));
                com.sfc.sf2.graphics.io.RawImageManager.exportImage(tiles, filePath, 2, fileFormat);
            }
            System.out.println("com.sfc.sf2.icon.io.ImageManager.exportImage() - Image files exported.");
        } catch (Exception ex) {
            Logger.getLogger(RawImageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
