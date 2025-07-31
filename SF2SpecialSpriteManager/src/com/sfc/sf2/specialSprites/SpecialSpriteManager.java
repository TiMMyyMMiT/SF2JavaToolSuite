/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.specialSprites;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.specialSprites.io.DisassemblyManager;
import java.awt.Color;

/**
 *
 * @author TiMMy
 */
public class SpecialSpriteManager { 

    private GraphicsManager graphicsManager = new GraphicsManager();
    private Tile[] tiles;
    private Color[] palette;
       
    public void importDisassembly(String graphicsFilePath, int blockRows, int blockColumns, int tilesPerBlock, String paletteFilepath) {
        System.out.println("com.sfc.sf2.spellGraphic.SpecialSpriteManager.importDisassembly() - Importing disassembly ...");
        palette = DisassemblyManager.importDisassembly(graphicsFilePath, graphicsManager, blockRows, blockColumns, tilesPerBlock, paletteFilepath);
        tiles = graphicsManager.getTiles();
        System.out.println("com.sfc.sf2.spellGraphic.SpecialSpriteManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String graphicsFilePath, int blockRows, int blockColumns, int tilesPerBlock, boolean savePaletteInFile) {
        System.out.println("com.sfc.sf2.spellGraphic.SpecialSpriteManager.exportDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportDisassembly(graphicsFilePath, savePaletteInFile ? palette : null, tiles, blockRows, blockColumns, tilesPerBlock);
        System.out.println("com.sfc.sf2.spellGraphic.SpecialSpriteManager.exportDisassembly() - Disassembly exporting.");
    }
    
    public void importPng(String filepath) {
        System.out.println("com.sfc.sf2.spellGraphic.SpecialSpriteManager.importDisassembly() - Importing disassembly ...");
        graphicsManager.importPng(filepath);
        tiles = graphicsManager.getTiles();
        palette = tiles[0].getPalette();
        System.out.println("com.sfc.sf2.spellGraphic.SpecialSpriteManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportPng(String filepath, int tilesPerRow) {
        System.out.println("com.sfc.sf2.spellGraphic.SpecialSpriteManager.exportDisassembly() - Exporting disassembly ...");
        graphicsManager.setTiles(tiles);
        graphicsManager.exportPng(filepath, tilesPerRow);
        System.out.println("com.sfc.sf2.spellGraphic.SpecialSpriteManager.exportDisassembly() - Disassembly exporting.");     
    }
    
    public void importGif(String filepath) {
        System.out.println("com.sfc.sf2.spellGraphic.SpecialSpriteManager.importDisassembly() - Importing disassembly ...");
        graphicsManager.importGif(filepath);
        tiles = graphicsManager.getTiles();
        palette = tiles[0].getPalette();
        System.out.println("com.sfc.sf2.spellGraphic.SpecialSpriteManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportGif(String filepath, int tilesPerRow) {
        System.out.println("com.sfc.sf2.spellGraphic.SpecialSpriteManager.exportDisassembly() - Exporting disassembly ...");
        graphicsManager.setTiles(tiles);
        graphicsManager.exportGif(filepath, tilesPerRow);
        System.out.println("com.sfc.sf2.spellGraphic.SpecialSpriteManager.exportDisassembly() - Disassembly exporting.");   
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }

    public Color[] getPalette() {
        return palette;
    }

    public void setPalette(Color[] palette) {
        this.palette = palette;
    }
}
