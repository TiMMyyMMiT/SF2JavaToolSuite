/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block;

import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.map.block.io.DisassemblyManager;
import com.sfc.sf2.map.block.io.MetaManager;
import com.sfc.sf2.map.block.io.RawImageManager;
import com.sfc.sf2.palette.PaletteManager;

/**
 *
 * @author wiz
 */
public class MapBlockManager {
    private final PaletteManager paletteManager = new PaletteManager();
    private final TilesetManager tilesetManager = new TilesetManager();
    private final DisassemblyManager disassemblyManager = new DisassemblyManager();
    
    private Tileset[] tilesets;
    private MapBlock[] blocks;
       
    public void importDisassembly(String incbinPath, String paletteEntriesPath, String tilesetEntriesPath, String tilesetsFilePath, String blocksPath) {
        /*System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Importing disassembly ...");
        paletteManager.importDisassembly(blocksPath);
        blocks = disassemblyManager.importDisassembly(incbinPath, paletteEntriesPath, tilesetEntriesPath, tilesetsFilePath, blocksPath);
        tilesets = disassemblyManager.getTilesets();
        //graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Disassembly imported.");*/
    }
       
    public void importDisassembly(String palettePath, String[] tilesetPaths, String blocksPath){
        /*System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Importing disassembly ...");
        blocks = disassemblyManager.importDisassembly(palettePath, tilesetPaths, blocksPath);
        tilesets = disassemblyManager.getTilesets();
        //graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Disassembly imported.");*/
    }
       
    public void importDisassembly(String palettePath, String[] tilesetPaths, String blocksPath, String animTilesetPath, int animTilesetStart, int animTilesetLength, int animTilesetDest){
        /*System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Importing disassembly ...");
        blocks = disassemblyManager.importDisassembly(palettePath, tilesetPaths, blocksPath, animTilesetPath, animTilesetStart, animTilesetLength, animTilesetDest);
        tilesets = disassemblyManager.getTilesets();
        //graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Disassembly imported.");*/
    }
    
    public void exportDisassembly(String graphicsPath){
        /*System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Exporting disassembly ...");
        disassemblyManager.exportDisassembly(blocks, tilesets, graphicsPath);
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Disassembly exported.");*/
    }
    
    public void importImage(String filepath, String hpFilePath){
        /*System.out.println("com.sfc.sf2.mapblock.MapBlockManager.exportGif() - Exporting GIF ...");
        blocks = RawImageManager.importImage(filepath);
        MetaManager.importBlockHpTilesFile(hpFilePath, blocks, RawImageManager.getImportedImageBlockWidth());
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.exportGif() - GIF exported.");*/
    }
    
    public void exportImage(String filepath, String hpFilePath, int blocksPerRow){
        /*System.out.println("com.sfc.sf2.mapblock.MapBlockManager.exportPng() - Exporting PNG ...");
        RawImageManager.exportRawImage(blocks, filepath, blocksPerRow, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_PNG);
        MetaManager.exportBlockHpTilesFile(blocks, blocksPerRow, hpFilePath);
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.exportPng() - PNG exported.");*/
    }

    public MapBlock[] getBlocks() {
        return blocks;
    }

    public void setBlocks(MapBlock[] blocks) {
        this.blocks = blocks;
    }

    public Tileset[] getTilesets() {
        return tilesets;
    }

    public void setTilesets(Tileset[] tilesets) {
        this.tilesets = tilesets;
    }
}
