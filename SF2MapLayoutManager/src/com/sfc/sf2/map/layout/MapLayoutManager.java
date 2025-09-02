/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.layout;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.map.block.MapBlocksetManager;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.layout.io.MapEntryData;
import com.sfc.sf2.map.layout.io.MapLayoutDisassemblyProcessor;
import com.sfc.sf2.map.layout.io.MapLayoutPackage;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class MapLayoutManager extends AbstractManager {
    private final MapBlocksetManager mapBlocksetManager = new MapBlocksetManager();
    private final MapLayoutDisassemblyProcessor layoutDisassemblyProcessor = new MapLayoutDisassemblyProcessor();
    
    private MapBlockset blockset;
    private MapLayout layout;

    @Override
    public void clearData() {
        mapBlocksetManager.clearData();
        if (blockset != null) {
            blockset.clearIndexedColorImage(true);
            blockset = null;
        }
        if (layout != null) {
            layout.getBlockset().clearIndexedColorImage(true);
            layout = null;
        }
    }
       
    public MapLayout importDisassembly(Path palettePath, Path[] tilesetsFilePath, Path blocksetPath, Path layoutPath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        blockset = mapBlocksetManager.importDisassembly(palettePath, tilesetsFilePath, blocksetPath);
        MapLayoutPackage pckg = new MapLayoutPackage(blockset);
        layout = layoutDisassemblyProcessor.importDisassembly(layoutPath, pckg);
        Console.logger().info("Map layout successfully imported from palette and tilesets. Layout data : " + layoutPath);
        Console.logger().finest("EXITING importDisassembly");
        return layout;
    }
    
    public MapLayout importDisassemblyFromMapEntry(Path paletteEntriesPath, Path tilesetEntriesPath, MapEntryData mapEntry) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassemblyFromMapEntry");
        Path layoutPath = mapEntry.getLayoutPath() == null ? null : PathHelpers.getIncbinPath().resolve(mapEntry.getLayoutPath());
        Path blocksetPath = mapEntry.getBlocksPath() == null ? null : PathHelpers.getIncbinPath().resolve(mapEntry.getBlocksPath());
        Path tilesetsPath = mapEntry.getTilesetsPath() == null ? null : PathHelpers.getIncbinPath().resolve(mapEntry.getTilesetsPath());
        importDisassemblyFromEntryFiles(paletteEntriesPath, tilesetEntriesPath, tilesetsPath, blocksetPath, layoutPath);
        Console.logger().finest("EXITING importDisassemblyFromMapEntry");
        return layout;
    }
    
    public MapLayout importDisassemblyFromEntryFiles(Path paletteEntriesPath, Path tilesetEntriesPath, Path tilesetsFilePath, Path blocksetPath, Path layoutPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassemblyFromEntryFiles");
        blockset = mapBlocksetManager.importDisassemblyFromEntries(paletteEntriesPath, tilesetEntriesPath, tilesetsFilePath, blocksetPath);
        MapLayoutPackage pckg = new MapLayoutPackage(blockset);
        layout = layoutDisassemblyProcessor.importDisassembly(layoutPath, pckg);
        Console.logger().info("Map layout successfully imported from entries paths. Layout data : " + layoutPath);
        Console.logger().finest("EXITING importDisassemblyFromEntryFiles");
        return layout;
    }
    
    public void exportDisassembly(Path tilesetsPath, Path blocksetPath, Path layoutPath, MapBlockset mapBlockset, MapLayout mapLayout) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING exportDisassembly");
        blockset = mapBlockset;
        layout = mapLayout;
        mapBlocksetManager.exportDisassembly(tilesetsPath, blocksetPath, blockset, mapBlocksetManager.getTilesets());
        MapLayoutPackage pckg = new MapLayoutPackage(blockset);
        layoutDisassemblyProcessor.exportDisassembly(layoutPath, layout, pckg);
        Console.logger().info("Map layout successfully exported to : " + layoutPath);
        Console.logger().finest("EXITING exportDisassembly");   
    }

    public MapLayout getLayout() {
        return layout;
    }

    public MapBlockset getBlockset() {
        return blockset;
    }
}
