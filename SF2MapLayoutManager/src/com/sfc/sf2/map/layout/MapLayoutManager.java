/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.layout;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.map.block.MapBlockManager;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.layout.io.MapLayoutDisassemblyProcessor;
import com.sfc.sf2.map.layout.io.MapLayoutPackage;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class MapLayoutManager {
    private final TilesetManager tilesetManager = new TilesetManager();
    private final MapBlockManager mapBlockManager = new MapBlockManager();
    private final MapLayoutDisassemblyProcessor layoutDisassemblyProcessor = new MapLayoutDisassemblyProcessor();
    
    private MapBlockset blockset;
    private MapLayout layout;
       
    public void importDisassembly(Path palettePath, Path[] tilesetPaths, Path blocksPath, Path layoutPath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        blockset = mapBlockManager.importDisassembly(palettePath, tilesetPaths, blocksPath);
        MapLayoutPackage pckg = new MapLayoutPackage(blockset);
        layout = layoutDisassemblyProcessor.importDisassembly(layoutPath, pckg);
        Console.logger().finest("EXITING importDisassembly");
    }
    
    public void importDisassembly(String palettesPath, String tilesetsPath, String tilesetsFilePath, String blocksPath, String layoutPath)
        throws DisassemblyException {
        //importDisassembly(palettesPath, tilesetsPath, tilesetsFilePath, blocksPath, layoutPath, null, 0, 0, 0, 0);
    }
    
    public void importDisassembly(String palettesPath, String tilesetsPath, String tilesetsFilePath, String blocksPath, String layoutPath, Integer animTileset, int animLength, int animFrameStart, int animFrameLength, int animFrameDest)
        throws DisassemblyException {
        /*System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassembly() - Importing disassembly ...");
        disassemblyManager = new DisassemblyManager();
        layout = disassemblyManager.importDisassembly(palettesPath, tilesetsPath, tilesetsFilePath, blocksPath, layoutPath, animTileset, animLength, animFrameStart, animFrameLength, animFrameDest);
        blockset = disassemblyManager.getBlockset();
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassembly() - Disassembly imported.");*/
    }
    
    public void importDisassemblyFromEntryFiles(String incbinPath, String paletteEntriesPath, String tilesetEntriesPath, String tilesetsFilePath, String blocksPath, String layoutPath)
        throws DisassemblyException {
        
        //importDisassemblyFromEntryFiles(incbinPath, paletteEntriesPath, tilesetEntriesPath, tilesetsFilePath, blocksPath, layoutPath, null, 0, 0, 0, 0);
    
    }
    
    public void importDisassemblyFromEntryFiles(String incbinPath, String paletteEntriesPath, String tilesetEntriesPath, String tilesetsFilePath, String blocksPath, String layoutPath, Integer animTileset, int animLength, int animFrameStart, int animFrameLength, int animFrameDest) 
        throws DisassemblyException {
        
        /*System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassemblyFromEntryFiles() - Importing disassembly ...");
        disassemblyManager = new DisassemblyManager();
        layout = disassemblyManager.importDisassemblyFromEntryFiles(incbinPath, paletteEntriesPath, tilesetEntriesPath, tilesetsFilePath, blocksPath, layoutPath, animTileset, animLength, animFrameStart, animFrameLength, animFrameDest);
        blockset = disassemblyManager.getBlockset();
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassemblyFromEntryFiles() - Disassembly imported.");*/
    }
    
    public void exportDisassembly(String tilesetsPath, String blocksPath, String layoutPath){
        /*System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassembly() - Exporting disassembly ...");
        disassemblyManager.exportDisassembly(blocks, layout.getTilesets(), blocksPath, layout, layoutPath);
        disassemblyManager.exportTilesetsFile(tilesetsPath, layout.getPalette(), layout.getTilesets());
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassembly() - Disassembly exported.");  */      
    }
    
    public void exportDisassembly(String blocksPath, String layoutPath){
        /*System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassembly() - Exporting disassembly ...");
        disassemblyManager.exportDisassembly(blocks, layout.getTilesets(), blocksPath, layout, layoutPath);
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassembly() - Disassembly exported.");    */    
    }
    
    public void exportPng(String filepath){
        /*System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.exportPng() - Exporting PNG ...");
        com.sfc.sf2.map.block.io.RawImageManager.exportRawImage(layout.getBlocks(), filepath, 64, com.sfc.sf2.graphics.io.RawImageProcessor.FILE_FORMAT_PNG);
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.exportPng() - PNG exported.");  */     
    }

    public MapLayout getLayout() {
        return layout;
    }

    public void setLayout(MapLayout layout) {
        this.layout = layout;
    }

    public MapBlockset getBlockset() {
        return blockset;
    }

    public void setBlockset(MapBlockset blockset) {
        this.blockset = blockset;
    }
}
