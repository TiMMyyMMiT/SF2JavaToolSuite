/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.weaponsprite.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.weaponsprite.WeaponSprite;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class RawImageManager {
    
    public static WeaponSprite importImage(String filepath) {
        System.out.println("com.sfc.sf2.weaponsprite.io.RawImageManager.importImage() - Importing Image files ...");
        WeaponSprite weaponsprite = new WeaponSprite();
        try {
            Tile[] tiles = com.sfc.sf2.graphics.io.RawImageManager.importImage(filepath);
            Tile[][] frames = new Tile[tiles.length/64][];
            for (int i = 0; i < frames.length; i++) {
                Tile[] tileData = new Tile[64];
                System.arraycopy(tiles, i*64, tileData, 0, 64);
                frames[i] = tileData;
            }
            if(frames.length == 0) {
                System.err.println("com.sfc.sf2.weaponsprite.io.RawImageManager.importImage() - ERROR : Image could not be loaded.");
            } else {
                weaponsprite.setFrames(frames);
            }
        } catch(Exception e) {
             System.err.println("com.sfc.sf2.weaponsprite.io.RawImageManager.importImage() - Error while parsing graphics data : "+e);
             e.printStackTrace();
        }        
        System.out.println("com.sfc.sf2.weaponsprite.io.RawImageManager.importImage() - PNG files imported.");
        return weaponsprite;                
    }
    
    public static void exportImage(WeaponSprite weaponSprite, String filepath, int fileFormat) {
        try {
            System.out.println("com.sfc.sf2.weaponsprite.io.RawImageManager.exportImage() - Exporting Image files ...");
            Tile[][] frames = weaponSprite.getFrames();
            Tile[] tiles = new Tile[frames.length*64];
            for(int i=0;i<frames.length;i++){
                System.arraycopy(frames[i], 0, tiles, i*64, 64);
            }
            com.sfc.sf2.graphics.io.RawImageManager.exportImage(tiles, filepath, 8, fileFormat);
            System.out.println("com.sfc.sf2.weaponsprite.io.RawImageManager.exportImage() - Image files and palettes exported.");
        } catch (Exception ex) {
            Logger.getLogger(RawImageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
