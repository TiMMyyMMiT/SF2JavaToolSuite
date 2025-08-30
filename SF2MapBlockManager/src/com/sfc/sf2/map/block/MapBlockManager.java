/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.graphics.io.TilesetDisassemblyProcessor;
import com.sfc.sf2.map.block.io.MapBlocksetDisassemblyProcessor;
import com.sfc.sf2.map.block.io.MapBlockPackage;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class MapBlockManager extends AbstractManager {
    private final PaletteManager paletteManager = new PaletteManager();
    private final TilesetManager tilesetManager = new TilesetManager();
    private final MapBlocksetDisassemblyProcessor blocksetDisassemblyProcessor = new MapBlocksetDisassemblyProcessor();
    
    private Tileset[] tilesets;
    private MapBlockset mapBlockset;

    @Override
    public void clearData() {
        paletteManager.clearData();
        tilesetManager.clearData();
        if (tilesets != null) {
            for (int i = 0; i < tilesets.length; i++) {
                if (tilesets[i] != null) {
                    tilesets[i].clearIndexedColorImage(true);
                }
            }
            tilesets = null;
        }
        if (mapBlockset != null) {
            mapBlockset.clearIndexedColorImage(true);
            mapBlockset = null;
        }
    }
       
    public void importDisassembly(Path incbinPath, Path paletteEntriesPath, Path tilesetEntriesPath, Path tilesetsFilePath, Path blocksPath) {
        Console.logger().finest("ENTERING importDisassembly");
        /*paletteManager.importDisassembly(blocksPath);
        blocks = disassemblyManager.importDisassembly(incbinPath, paletteEntriesPath, tilesetEntriesPath, tilesetsFilePath, blocksPath);
        tilesets = disassemblyManager.getTilesets();
        //graphicsManager.setTiles(tiles);*/
        Console.logger().finest("EXITING importDisassembly");
    }
       
    public void importDisassembly(Path palettePath, Path[] tilesetPaths, Path blocksPath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        Palette palette = paletteManager.importDisassembly(palettePath, true);
        tilesets = new Tileset[tilesetPaths.length];
        for (int i = 0; i < tilesetPaths.length; i++) {
            if (tilesetPaths[i] != null) {
                tilesets[i] = tilesetManager.importDisassembly(tilesetPaths[i], palette, TilesetDisassemblyProcessor.TilesetCompression.STACK, 24);
            }
        }
        MapBlockPackage pckg = new MapBlockPackage(tilesets, palette);
        mapBlockset = blocksetDisassemblyProcessor.importDisassembly(blocksPath, pckg);
        Console.logger().finest("EXITING importDisassembly");
    }
       
    public void importDisassembly(Path palettePath, Path[] tilesetPaths, Path blocksPath, Path animTilesetPath, int animTilesetStart, int animTilesetLength, int animTilesetDest){
        Console.logger().finest("ENTERING importDisassembly");
        //blocks = disassemblyManager.importDisassembly(palettePath, tilesetPaths, blocksPath, animTilesetPath, animTilesetStart, animTilesetLength, animTilesetDest);
        //tilesets = disassemblyManager.getTilesets();
        //graphicsManager.setTiles(tiles);
        Console.logger().finest("EXITING importDisassembly");
    }
    
    public void exportDisassembly(Path graphicsPath){
        /*System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Exporting disassembly ...");
        disassemblyManager.exportDisassembly(blocks, tilesets, graphicsPath);
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Disassembly exported.");*/
    }
    
    public void importImage(Path filepath, Path hpFilePath){
        /*System.out.println("com.sfc.sf2.mapblock.MapBlockManager.exportGif() - Exporting GIF ...");
        blocks = RawImageManager.importImage(filepath);
        MetaManager.importBlockHpTilesFile(hpFilePath, blocks, RawImageManager.getImportedImageBlockWidth());
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.exportGif() - GIF exported.");*/
    }
    
    public void exportImage(Path filepath, Path hpFilePath, int blocksPerRow){
        /*System.out.println("com.sfc.sf2.mapblock.MapBlockManager.exportPng() - Exporting PNG ...");
        RawImageManager.exportRawImage(blocks, filepath, blocksPerRow, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_PNG);
        MetaManager.exportBlockHpTilesFile(blocks, blocksPerRow, hpFilePath);
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.exportPng() - PNG exported.");*/
    }

    public MapBlockset getMapBlockset() {
        return mapBlockset;
    }

    public void setMapBlockset(MapBlockset mapBlockset) {
        this.mapBlockset = mapBlockset;
    }

    public Tileset[] getTilesets() {
        return tilesets;
    }

    public void setTilesets(Tileset[] tilesets) {
        this.tilesets = tilesets;
    }
}
