/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.animation.MapAnimationManager;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.gui.MapLayoutPanel;
import com.sfc.sf2.map.io.*;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.map.layout.MapLayoutManager;
import com.sfc.sf2.map.layout.io.MapEntryData;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class MapManager extends AbstractManager {
    
    private final MapLayoutManager mapLayoutManager = new MapLayoutManager();
    private final MapAnimationManager mapAnimationManager = new MapAnimationManager();
    private final MapEnumsAsmProcessor mapEnumsAsmProcessor = new MapEnumsAsmProcessor();
    private final MapAreaAsmProcessor mapAreasAsmProcessor = new MapAreaAsmProcessor();
    private final MapFlagEventsAsmProcessor mapFlagsAsmProcessor = new MapFlagEventsAsmProcessor();
    private final MapStepEventsAsmProcessor mapStepsAsmProcessor = new MapStepEventsAsmProcessor();
    private final MapRoofEventsAsmProcessor mapRoofsAsmProcessor = new MapRoofEventsAsmProcessor();
    private final MapWarpEventsAsmProcessor mapWarpsAsmProcessor = new MapWarpEventsAsmProcessor();
    private final MapItemAsmProcessor mapItemAsmProcessor = new MapItemAsmProcessor();
            
    private Map map;
    private MapEnums mapEnums;

    @Override
    public void clearData() {
        mapLayoutManager.clearData();
        mapAnimationManager.clearData();
        map = null;
        mapEnums = null;
    }
    
    private Map importDisassembly(MapBlockset blockset, MapLayout layout, MapAnimation mapAnimation, Path areasPath, Path flagsPath, Path stepsPath, Path roofsPath,
            Path warpsPath, Path chestItemsPath, Path otherItemsPath) throws IOException, AsmException {
        Console.logger().finest("ENTERING importDisassembly");
        MapArea[] areas = null;
        MapFlagCopyEvent[] flagCopies = null;
        MapCopyEvent[] stepCopies = null;
        MapCopyEvent[] roofCopies = null;
        MapWarpEvent[] warps = null;
        MapItem[] chestItems = null;
        MapItem[] otherItems = null;
        MapAnimation animation = null;
        if (areasPath != null) areas = mapAreasAsmProcessor.importAsmData(areasPath, mapEnums);
        if (flagsPath != null) flagCopies = mapFlagsAsmProcessor.importAsmData(flagsPath, null);
        if (stepsPath != null) stepCopies = mapStepsAsmProcessor.importAsmData(stepsPath, null);
        if (roofsPath != null) roofCopies = mapRoofsAsmProcessor.importAsmData(roofsPath, null);
        if (warpsPath != null) warps = mapWarpsAsmProcessor.importAsmData(warpsPath, null);
        if (chestItemsPath != null) chestItems = mapItemAsmProcessor.importAsmData(chestItemsPath, null);
        if (otherItemsPath != null) otherItems = mapItemAsmProcessor.importAsmData(otherItemsPath, null);
        map = new Map(blockset, layout, areas, flagCopies, stepCopies, roofCopies, warps, chestItems, otherItems, animation);
        Console.logger().finest("EXITING importDisassembly");
        return map;
    }
    
    public Map importDisassemblyFromData(Path palettesEntriesPath, Path tilesetsEntriesPath, Path tilesetsFilePath, Path blocksPath, Path layoutPath, Path areasPath,
            Path flagsPath, Path stepsPath, Path roofsPath, Path warpsPath, Path chestItemsPath, Path otherItemsPath, Path animationPath) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassemblyFromData");
        mapLayoutManager.importDisassembly(palettesEntriesPath, tilesetsEntriesPath, tilesetsFilePath, blocksPath, layoutPath);
        MapLayout layout = mapLayoutManager.getMapLayout();
        MapBlockset blockset = mapLayoutManager.getMapBlockset();
        MapAnimation animation = mapAnimationManager.importDisassembly(animationPath, tilesetsEntriesPath);
        importDisassembly(blockset, layout, animation, areasPath, flagsPath, stepsPath, roofsPath, warpsPath, chestItemsPath, otherItemsPath);
        Console.logger().info("Map successfully imported from data for : " + blocksPath.getParent());
        Console.logger().finest("EXITING importDisassemblyFromData");
        return map;
    }
    
    public Map importDisassemblyFromRawFiles(Path palettePath, Path[] tilesetsFilePath, Path blocksetPath, Path layoutPath, Path areasPath,
            Path flagsPath, Path stepsPath, Path roofsPath, Path warpsPath, Path chestItemsPath, Path otherItemsPath, Path animationPath, Path tilesetEntriesPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassemblyFromRawFiles");
        mapLayoutManager.importDisassemblyFromRawFiles(palettePath, tilesetsFilePath, blocksetPath, layoutPath);
        MapLayout layout = mapLayoutManager.getMapLayout();
        MapBlockset blockset = mapLayoutManager.getMapBlockset();
        MapAnimation animation = mapAnimationManager.importDisassembly(animationPath, tilesetEntriesPath);
        importDisassembly(blockset, layout, animation, areasPath, flagsPath, stepsPath, roofsPath, warpsPath, chestItemsPath, otherItemsPath);
        Console.logger().finest("EXITING importDisassemblyFromRawFiles");
        return map;
    }
    
    public Map importDisassemblyFromEntries(Path paletteEntriesPath, Path tilesetEntriesPath, Path mapEntriesPath, Path sf2enumsPath, int mapId) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassemblyFromEntries");
        ImportMapEnums(sf2enumsPath);
        mapLayoutManager.importDisassemblyFromMapEntries(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, mapId);
        MapLayout layout = mapLayoutManager.getMapLayout();
        MapBlockset blockset = mapLayoutManager.getMapBlockset();
        
        MapEntryData[] mapEntries = mapLayoutManager.getMapEntries();
        MapEntryData mapEntry = mapEntries[mapId];
        MapAnimation animation = null;
        if (mapEntry.getAnimationsPath() != null) {
            Path animationPath = PathHelpers.getIncbinPath().resolve(mapEntry.getAnimationsPath());
            animation = mapAnimationManager.importDisassembly(animationPath, tilesetEntriesPath);
            mapAnimationManager.checkForSharedAnimations(mapEntries, mapId, mapEntry.getAnimationsPath());
        }
        importDisassembly(blockset, layout, animation, Path.of(mapEntry.getAreasPath()), Path.of(mapEntry.getFlagEventsPath()), Path.of(mapEntry.getStepEventsPath()), Path.of(mapEntry.getRoofEventsPath()),
                Path.of(mapEntry.getWarpEventsPath()), Path.of(mapEntry.getChestItemsPath()), Path.of(mapEntry.getOtherItemsPath()));
        Console.logger().info("Map successfully imported from entries for : " + mapId);
        Console.logger().finest("EXITING importDisassemblyFromEntries");
        return map;
    }
    
    private void ImportMapEnums(Path sf2enumsPath) throws IOException, AsmException {
        if (mapEnums == null) {
            mapEnums = mapEnumsAsmProcessor.importAsmData(sf2enumsPath, null);
            Console.logger().info("Map enums successfully loaded from : " + sf2enumsPath);
        }
    }
    
    private void exportDisassembly(Path areasPath, Path flagsPath, Path stepsPath, Path roofsPath, Path warpsPath, Path chestItemsPath, Path otherItemsPath, Map map) throws IOException, AsmException {
        Console.logger().finest("ENTERING exportDisassembly");
        if (areasPath != null) mapAreasAsmProcessor.exportAsmData(areasPath, map.getAreas(), mapEnums);
        if (flagsPath != null) mapFlagsAsmProcessor.exportAsmData(flagsPath, map.getFlagCopies(), null);
        if (stepsPath != null) mapStepsAsmProcessor.exportAsmData(stepsPath, map.getStepCopies(), null);
        if (roofsPath != null) mapRoofsAsmProcessor.exportAsmData(roofsPath, map.getRoofCopies(), null);
        if (warpsPath != null) mapWarpsAsmProcessor.exportAsmData(warpsPath, map.getWarps(), null);
        if (chestItemsPath != null) mapItemAsmProcessor.exportAsmData(chestItemsPath, map.getChestItems(), null);
        if (otherItemsPath != null) mapItemAsmProcessor.exportAsmData(otherItemsPath, map.getOtherItems(), null);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void exportDisassemblyFromData(Path tilesetsFilePath, Path blocksPath, Path layoutPath, Path areasPath, Path flagsPath, Path stepsPath, Path roofsPath, Path warpsPath,
            Path chestItemsPath, Path otherItemsPath, Path animationPath, Map map) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassemblyFromData");
        this.map = map;
        mapLayoutManager.exportDisassembly(tilesetsFilePath, blocksPath, layoutPath, map.getBlockset(), map.getLayout());
        if (map.getAnimation() != null) {
            mapAnimationManager.exportDisassembly(animationPath, map.getAnimation());
        }
        exportDisassembly(areasPath, flagsPath, stepsPath, roofsPath, warpsPath, chestItemsPath, otherItemsPath, map);
        Console.logger().info("Map successfully exported from data for : " + blocksPath.getParent());
        Console.logger().finest("EXITING exportDisassemblyFromData");
    }
    
    public void exportDisassemblyFromMapEntries(Path mapEntriesPath, int mapId, Map map) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.map = map;
        MapEntryData[] mapEntries = mapLayoutManager.ImportMapEntries(mapEntriesPath);
        if (mapId < 0 || mapId >= mapEntries.length || mapEntries[mapId] == null) {
            throw new DisassemblyException("Cannot export map " + mapId + ". Map entry was not found or map entries are corrupted.");
        }
        MapEntryData mapEntry = mapEntries[mapId];
        mapLayoutManager.exportDisassemblyFromMapEntries(mapEntriesPath, mapId, map.getBlockset(), map.getLayout());
        if (map.getAnimation() != null) {
            mapAnimationManager.exportDisassemblyFromMapEntries(mapEntriesPath, mapId, map.getAnimation());
        }
        exportDisassembly(Path.of(mapEntry.getAreasPath()), Path.of(mapEntry.getFlagEventsPath()), Path.of(mapEntry.getStepEventsPath()), Path.of(mapEntry.getRoofEventsPath()),
                Path.of(mapEntry.getWarpEventsPath()), Path.of(mapEntry.getChestItemsPath()), Path.of(mapEntry.getOtherItemsPath()), map);
        Console.logger().info("Map successfully exported from entries for : " + mapId);
        Console.logger().finest("EXITING exportDisassembly");   
    }
    
    public void exportMapImage(Map map, Path filepath) {
        /*System.out.println("com.sfc.sf2.maplayout.MapEditor.exportPng() - Exporting PNG ...");
        RawImageManager.exportMapAsRawImage(map.getLayout(), filepath, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_PNG);
        System.out.println("com.sfc.sf2.maplayout.MapEditor.exportPng() - PNG exported.");*/
    }
    
    public void exportBlocksetImage(MapBlockset mapblockset, Path filepath, Path hpFilepath, int blocksPerRow) {
        /*System.out.println("com.sfc.sf2.maplayout.MapEditor.exportPng() - Exporting PNG ...");
        mapBlockManager.setBlocks(map.getBlocks());
        mapBlockManager.exportPng(filepath, hpFilepath, blocksPerRow);
        System.out.println("com.sfc.sf2.maplayout.MapEditor.exportPng() - PNG exported.");*/
    }
    
    public void exportMapLayoutImage(MapLayoutPanel mapPanel, Path filepath, Path flagsPath, Path hpTilesPath) {
        /*System.out.println("com.sfc.sf2.maplayout.MapEditor.exportPng() - Exporting PNG ...");
        RawImageManager.exportImageMapLayout(mapPanel, filepath, flagsPath, hpTilesPath, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_PNG);
        System.out.println("com.sfc.sf2.maplayout.MapEditor.exportPng() - PNG exported.");*/
    }

    public MapAnimationManager getMapAnimationManager() {
        return mapAnimationManager;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
    
    public String getSharedBlockInfo() {
        return mapLayoutManager.getSharedBlockInfo();
    }
    
    public String getSharedAnimationInfo() {
        return mapAnimationManager.getSharedAnimationInfo();
    }
}
