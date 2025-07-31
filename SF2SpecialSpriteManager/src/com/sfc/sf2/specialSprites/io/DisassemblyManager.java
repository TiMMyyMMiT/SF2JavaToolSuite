/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.specialSprites.io;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.compressed.StackGraphicsDecoder;
import com.sfc.sf2.graphics.compressed.StackGraphicsEncoder;
import com.sfc.sf2.palette.graphics.PaletteDecoder;
import com.sfc.sf2.palette.graphics.PaletteEncoder;
import java.awt.Color;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TiMMy
 */
public class DisassemblyManager {
    
    public static Color[] importDisassembly(String filepath, GraphicsManager graphicsManager, int blockRows, int blockColumns, int tilesPerBlock, String paletteFilepath) {
        System.out.println("com.sfc.sf2.specialSprites.io.disassemblyManager.importDisassembly() - Importing disassembly file ...");
        Color[] palette = null;
        try {
            boolean separatePalette = paletteFilepath != null && paletteFilepath.length() > 0;
            Path path = Paths.get(filepath);
            Path palettePath = separatePalette ? Paths.get(paletteFilepath) : path;
            if (separatePalette && !palettePath.isAbsolute()) {
                palettePath = Paths.get(filepath.substring(0, filepath.lastIndexOf("\\")+1), paletteFilepath);
            }
            if (palettePath.toFile().exists()) {
                byte[] data = Files.readAllBytes(palettePath);
                if (data.length >= 32) {
                    byte[] colorData = new byte[32];
                    System.arraycopy(data, 0, colorData, 0, 32);
                    palette = PaletteDecoder.parsePalette(colorData);
                } else {
                    System.out.println("com.sfc.sf2.specialSprites.io.disassemblyManager.parseGraphics() - Palette file ignored because of too small length (must be a dummy file) " + data.length + " : " + filepath);
                }
            }
            if (path.toFile().exists()) {
                byte[] data = Files.readAllBytes(path);
                if (data.length > 40) {
                    int paletteOffset = separatePalette ? 0 : 32;
                    byte[] tileData = new byte[data.length - paletteOffset];
                    System.arraycopy(data, paletteOffset, tileData, 0, tileData.length);
                    Tile[] tiles = new StackGraphicsDecoder().decodeStackGraphics(tileData, palette);
                    tiles = reorderTilesSequentially(tiles, blockColumns, blockRows, tilesPerBlock);
                    graphicsManager.setTiles(tiles);
                } else {
                    System.out.println("com.sfc.sf2.specialSprites.io.disassemblyManager.parseGraphics() - File ignored because of too small length (must be a dummy file) " + data.length + " : " + filepath);
                }
            }            
        } catch(Exception e) {
             System.err.println("com.sfc.sf2.specialSprites.io.disassemblyManager.parseGraphics() - Error while parsing graphics data : "+e);
             e.printStackTrace();
        }    
        System.out.println("com.sfc.sf2.specialSprites.io.disassemblyManager.importDisassembly() - Disassembly imported.");
        return palette;
    }
    
    public static void exportDisassembly(String filepath, Color[] palette, Tile[] tiles, int blockRows, int blockColumns, int tilesPerBlock) {
        System.out.println("com.sfc.sf2.specialSprites.io.disassemblyManager.exportDisassembly() - Exporting disassembly ...");
        try {
            byte[] paletteBytes = new byte[0];
            int paletteOffset = palette == null ? 0 : 32;
            if (palette != null) {
                PaletteEncoder.producePalette(palette);
                paletteBytes = PaletteEncoder.getNewPaletteFileBytes();
            }
            tiles = reorderTilesForDisasssembly(tiles, blockColumns, blockRows, tilesPerBlock);
            StackGraphicsEncoder.produceGraphics(tiles);
            byte[] tilesBytes = StackGraphicsEncoder.getNewGraphicsFileBytes();

            byte[] newSpellGraphicFileBytes = new byte[paletteBytes.length + tilesBytes.length];
            if (palette != null) {
                System.arraycopy(paletteBytes, 0, newSpellGraphicFileBytes, 0, paletteBytes.length);
            }
            System.arraycopy(tilesBytes, 0, newSpellGraphicFileBytes, paletteOffset, tilesBytes.length);

            Path graphicsFilePath = Paths.get(filepath);
            Files.write(graphicsFilePath,newSpellGraphicFileBytes);
            System.out.println(newSpellGraphicFileBytes.length + " bytes into " + graphicsFilePath);
        } catch (Exception ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.out.println(ex);
        }  
        System.out.println("com.sfc.sf2.specialSprites.io.disassemblyManager.exportDisassembly() - Disassembly exported.");        
    }
    
    private static Tile[] reorderTilesSequentially(Tile[] tiles, int blockColumns, int blockRows, int tilesPerBlock) {
        if (tilesPerBlock == 1) return tiles;
        /* Disassembly tiles are stored in 3x3 chunks (top-bottom, left-right)
            1  4  7 19 22 25 37             
            2  5  8 20 23 26 38             
            3  6  9 21 24 27 .              
           10 13 16 28 31 34                
           11 14 17 29 32 35            . 71
           12 15 18 30 33 36            . 72
        */
        // \/ Edit these variables \/
        int blockColumnCount = blockColumns;
        int blockRowCount = blockRows;
        //int tilesPerBlock = tilesPerBlock;
        // /\ Edit these variables /\
        int blockTotalTiles = tilesPerBlock*tilesPerBlock;
        Tile[] newTiles = new Tile[tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            int bc = (i/tilesPerBlock) % blockColumnCount;
            int br = i/(blockColumnCount*blockTotalTiles);
            int tc = i%tilesPerBlock;
            int tr = (i/(tilesPerBlock*blockColumnCount)) % tilesPerBlock;
            newTiles[i] = tiles[bc*(blockTotalTiles*blockRowCount) + br*blockTotalTiles + tc*tilesPerBlock + tr];
        }
        return newTiles;
    }
    
    private static Tile[] reorderTilesForDisasssembly(Tile[] tiles, int blockColumns, int blockRows, int tilesPerBlock) {
        if (tilesPerBlock == 1) return tiles;
        // \/ Edit these variables \/
        int blockColumnCount = blockColumns;
        int blockRowCount = blockRows;
        //int tilesPerBlock = tilesPerBlock;
        // /\ Edit these variables /\
        int blockTotalTiles = tilesPerBlock*tilesPerBlock;
        Tile[] newTiles = new Tile[tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            int bc = (i/tilesPerBlock) % blockColumnCount;
            int br = i/(blockColumnCount*blockTotalTiles);
            int tc = i%tilesPerBlock;
            int tr = (i/(tilesPerBlock*blockColumnCount)) % tilesPerBlock;
            newTiles[bc*(blockTotalTiles*blockRowCount) + br*blockTotalTiles + tc*tilesPerBlock + tr] = tiles[i];
        }
        return newTiles;
    }
}
