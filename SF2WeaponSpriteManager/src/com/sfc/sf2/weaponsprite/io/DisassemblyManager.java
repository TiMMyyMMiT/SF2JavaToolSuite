/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.weaponsprite.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.weaponsprite.WeaponSprite;
import com.sfc.sf2.graphics.compressed.StackGraphicsDecoder;
import com.sfc.sf2.graphics.compressed.StackGraphicsEncoder;
import com.sfc.sf2.palette.Palette;
import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class DisassemblyManager {

    private static final String INCBIN_PATH = "\\..\\..\\..\\..\\..\\";
    private static final int WEAPONSPRITE_FRAMES_LENGTH = 4;
    
    public static Palette[] importPalettes(Palette basePalette, String paletteEntriesPath) {
        List<Palette> palettesList = new ArrayList<>();
        Palette[] palettes = null;
        System.out.println("com.sfc.sf2.battle.io.DisassemblyManager.importEnemyEnums() - Importing disassembly ...");            
        try {
            Path file = Path.of(paletteEntriesPath).normalize();
            Scanner scan = new Scanner(file);
            while(scan.hasNext()){
                String line = scan.nextLine().trim();
                if(line.length() > 2 && !line.startsWith(";")) {
                    String path = line.substring(line.indexOf("\"")+1, line.lastIndexOf("\""));
                    Palette palette = com.sfc.sf2.palette.io.DisassemblyManager.importDisassembly(file.getParent().toString() + INCBIN_PATH + path, false);
                    if (palette.getColors().length == 2) {  //Weapon palettes should only contain 2 colors
                        Color[] colors = new Color[16];
                        System.arraycopy(basePalette.getColors(), 0, colors, 0, 14);
                        colors[14] = palette.getColors()[0];
                        colors[15] = palette.getColors()[1];
                        palette.setColors(colors, true);
                    }
                    palettesList.add(palette);
                }
            }
            System.out.println("Palettes imported: " + palettesList.size() + " palettes.");
            
            palettes = new Palette[palettesList.size()];
            palettes = palettesList.toArray(palettes);
        } catch (IOException ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("com.sfc.sf2.battle.io.DisassemblyManager.importEnemyEnums() - Disassembly imported.");
        
        return palettes;
    }
    
    public static WeaponSprite importDisassembly(String graphicsPath, Palette basePalette) {
        System.out.println("com.sfc.sf2.weaponsprite.io.DisassemblyManager.importDisassembly() - Importing disassembly ...");
        WeaponSprite weaponsprite = null;

        Tile[][] frames = null;
        try {
            Path graphicspath = Paths.get(graphicsPath);
            if(graphicspath.toFile().exists()) {
                byte[] graphicsData = Files.readAllBytes(graphicspath);
                if(graphicsData.length>2){
                    Tile[] tiles = new StackGraphicsDecoder().decodeStackGraphics(graphicsData, basePalette);
                    frames = new Tile[tiles.length/64][];
                    for (int i = 0; i < frames.length; i++) {
                        Tile[] tileData = new Tile[64];
                        System.arraycopy(tiles, i*64, tileData, 0, 64);
                        frames[i] = com.sfc.sf2.graphics.io.DisassemblyManager.reorderTilesSequentially(tileData, 2, 2, 4);
                    }
                } else {
                    System.out.println("com.sfc.sf2.weaponsprite.io.DisassemblyManager.parseGraphics() - File ignored because of too small length (must be a dummy file) " + graphicsData.length + " : " + graphicsPath);
                }
            }

            if(frames!=null){
                if(frames.length == WEAPONSPRITE_FRAMES_LENGTH){
                   weaponsprite = new WeaponSprite();   
                   weaponsprite.setFrames(frames);
                   System.out.println("Created WeaponSprite with " + (frames.length*64) + " tiles.");
                }else{
                    System.out.println("Could not create WeaponSprite because of wrong length : frames=" + frames.length);
                }
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.weaponsprite.io.PngManager.importPng() - Error while parsing graphics data : "+e);
             e.printStackTrace();
        }
                
        System.out.println("com.sfc.sf2.weaponsprite.io.DisassemblyManager.importDisassembly() - Disassembly imported.");
        return weaponsprite;
    }
    
    public static void exportDisassembly(WeaponSprite weaponsprite, String graphicsPath){
        System.out.println("com.sfc.sf2.weaponsprite.io.DisassemblyManager.exportDisassembly() - Exporting disassembly ...");
        try {
            Tile[][] frames = weaponsprite.getFrames();
            Tile[] tiles = new Tile[WEAPONSPRITE_FRAMES_LENGTH*64];
            for (int i = 0; i < frames.length; i++) {
                Tile[] tileData = com.sfc.sf2.graphics.io.DisassemblyManager.reorderTilesForDisasssembly(frames[i], 2, 2, 4);
                System.arraycopy(tileData, 0, tiles, i*64, 64);
            }
            StackGraphicsEncoder.produceGraphics(tiles);
            byte[] newWeaponSpriteFileBytes = StackGraphicsEncoder.getNewGraphicsFileBytes();
            Path graphicsFilePath = Paths.get(graphicsPath);
            Files.write(graphicsFilePath,newWeaponSpriteFileBytes);
            System.out.println(newWeaponSpriteFileBytes.length + " bytes into " + graphicsFilePath);
        } catch (Exception ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.out.println(ex);
        }
        System.out.println("com.sfc.sf2.weaponsprite.io.DisassemblyManager.exportDisassembly() - Disassembly exported.");
    }
}
