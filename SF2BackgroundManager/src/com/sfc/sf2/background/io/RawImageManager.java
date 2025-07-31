/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.background.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.background.Background;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TiMMy
 */
public class RawImageManager {
    
    private static final String BASE_FILENAME = "backgroundXX.file";
    
    public static Background[] importImages(String basepath, int fileFormat) {
        System.out.println("com.sfc.sf2.background.io.RawImageManager.importImage() - Importing Image files ...");
        String extension = "." + com.sfc.sf2.graphics.io.RawImageManager.GetFileExtensionString(fileFormat);
        List<Background> backgrounds = new ArrayList();
        try {
            int count = 0;
            int index = 0;
            File directory = new File(basepath);
            File[] files = directory.listFiles();
            for (File f : files) { 
                if (f.getName().startsWith("background") && f.getName().endsWith(extension)) {
                    Tile[] tiles = com.sfc.sf2.graphics.io.RawImageManager.importImage(f.getAbsolutePath());
                    if (tiles != null) {
                        if (tiles.length == 384) {
                            index = count;
                            String fileNumber = f.getName().replaceAll("[^0-9]", "");
                            if (fileNumber.length() > 0)
                                index = Integer.parseInt(fileNumber);
                            Background background = new Background();
                            background.setIndex(index);
                            System.arraycopy(tiles, 0, tiles, 0, tiles.length);                   
                            background.setTiles(tiles);
                            backgrounds.add(background);
                            count++;
                            System.out.println("Created Background " + index + " with " + tiles.length + " tiles.");                       
                        } else {
                            System.out.println("Could not create Background " + index + " because of wrong length : tiles=" + tiles.length);
                        }
                    }
                }
            }
        } catch (Exception e) {
             System.err.println("com.sfc.sf2.background.io.RawImageManager.importImage() - Error while parsing graphics data : "+e);
        }
        System.out.println("com.sfc.sf2.background.io.RawImageManager.importImage() - Image files imported.");
        return backgrounds.toArray(new Background[backgrounds.size()]);
    }
    
    public static void exportImages(Background[] backgrounds, String basepath, int fileFormat) {
        try {
            System.out.println("com.sfc.sf2.background.io.RawImageManager.exportImage() - Exporting Image files ...");
            String extension = "." + com.sfc.sf2.graphics.io.RawImageManager.GetFileExtensionString(fileFormat);
            for (int i = 0; i < backgrounds.length; i++) {
                String index = String.format("%02d", backgrounds[i].getIndex());
                String filePath = basepath + System.getProperty("file.separator") + BASE_FILENAME.replace("XX.file", index + extension);
                com.sfc.sf2.graphics.io.RawImageManager.exportImage(backgrounds[i].getTiles(), filePath, 32, fileFormat);
            }
            System.out.println("com.sfc.sf2.background.io.RawImageManager.exportImage() - Image files exported.");
        } catch (Exception ex) {
            Logger.getLogger(RawImageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
