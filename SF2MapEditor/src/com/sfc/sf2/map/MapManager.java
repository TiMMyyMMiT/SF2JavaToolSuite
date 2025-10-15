/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.MetadataException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.animation.MapAnimationManager;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.block.MapBlocksetManager;
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
    private final MapBlocksetManager mapBlocksetManager = new MapBlocksetManager();
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
    
    private Map importDisassembly(MapBlockset blockset, MapLayout layout, MapAnimation animation, Path areasPath, Path flagsPath, Path stepsPath, Path roofsPath,
            Path warpsPath, Path chestItemsPath, Path otherItemsPath) throws IOException, AsmException {
        Console.logger().finest("ENTERING importDisassembly");
        MapArea[] areas = null;
        MapFlagCopyEvent[] flagCopies = null;
        MapCopyEvent[] stepCopies = null;
        MapCopyEvent[] roofCopies = null;
        MapWarpEvent[] warps = null;
        MapItem[] chestItems = null;
        MapItem[] otherItems = null;
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
        MapAnimation animation = mapAnimationManager.importDisassembly(animationPath, tilesetsEntriesPath, layout);
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
        MapAnimation animation = mapAnimationManager.importDisassembly(animationPath, tilesetEntriesPath, layout);
        importDisassembly(blockset, layout, animation, areasPath, flagsPath, stepsPath, roofsPath, warpsPath, chestItemsPath, otherItemsPath);
        Console.logger().finest("EXITING importDisassemblyFromRawFiles");
        return map;
    }
    
    public Map importDisassemblyFromEntries(Path paletteEntriesPath, Path tilesetEntriesPath, Path mapEntriesPath, int mapId) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassemblyFromEntries");
        mapLayoutManager.importDisassemblyFromMapEntries(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, mapId);
        MapLayout layout = mapLayoutManager.getMapLayout();
        MapBlockset blockset = mapLayoutManager.getMapBlockset();
        
        MapEntryData[] mapEntries = mapLayoutManager.getMapEntries();
        MapEntryData mapEntry = mapEntries[mapId];
        MapAnimation animation = null;
        if (mapEntry.getAnimationsPath() != null) {
            Path animationPath = PathHelpers.getIncbinPath().resolve(mapEntry.getAnimationsPath());
            animation = mapAnimationManager.importDisassembly(animationPath, tilesetEntriesPath, layout);
            mapAnimationManager.checkForSharedAnimations(mapEntries, mapId, mapEntry.getAnimationsPath());
        }
        Path areasPath = null, flagsPath = null, stepsPath = null, roofsPath = null, warpsPath = null, chestItemsPath = null, otherItemsPath = null;
        if (mapEntry.getAreasPath() != null) areasPath = PathHelpers.getIncbinPath().resolve(mapEntry.getAreasPath());
        if (mapEntry.getFlagEventsPath() != null) flagsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getFlagEventsPath());
        if (mapEntry.getStepEventsPath() != null) stepsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getStepEventsPath());
        if (mapEntry.getRoofEventsPath() != null) roofsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getRoofEventsPath());
        if (mapEntry.getWarpEventsPath() != null) warpsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getWarpEventsPath());
        if (mapEntry.getChestItemsPath() != null) chestItemsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getChestItemsPath());
        if (mapEntry.getOtherItemsPath() != null) otherItemsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getOtherItemsPath());
        importDisassembly(blockset, layout, animation, areasPath, flagsPath, stepsPath, roofsPath, warpsPath, chestItemsPath, otherItemsPath);
        Console.logger().info("Map successfully imported from entries for : " + mapId);
        Console.logger().finest("EXITING importDisassemblyFromEntries");
        return map;
    }
    
    public void ImportMapEnums(Path sf2enumsPath) throws IOException, AsmException {
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
        if (chestItemsPath != null) mapItemAsmProcessor.exportAsmData(chestItemsPath, map.getChestItems(), new MapItemPackage(true));
        if (otherItemsPath != null) mapItemAsmProcessor.exportAsmData(otherItemsPath, map.getOtherItems(), new MapItemPackage(false));
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
        Path areasPath = null, flagsPath = null, stepsPath = null, roofsPath = null, warpsPath = null, chestItemsPath = null, otherItemsPath = null;
        if (mapEntry.getAreasPath() != null) areasPath = PathHelpers.getIncbinPath().resolve(mapEntry.getAreasPath());
        if (mapEntry.getFlagEventsPath() != null) flagsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getFlagEventsPath());
        if (mapEntry.getStepEventsPath() != null) stepsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getStepEventsPath());
        if (mapEntry.getRoofEventsPath() != null) roofsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getRoofEventsPath());
        if (mapEntry.getWarpEventsPath() != null) warpsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getWarpEventsPath());
        if (mapEntry.getChestItemsPath() != null) chestItemsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getChestItemsPath());
        if (mapEntry.getOtherItemsPath() != null) otherItemsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getOtherItemsPath());
        exportDisassembly(areasPath, flagsPath, stepsPath, roofsPath, warpsPath, chestItemsPath, otherItemsPath, map);
        Console.logger().info("Map successfully exported from entries for : " + mapId);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void exportMapBlocksetImage(Path filepath, Path hpFilepath, int blocksPerRow, MapBlockset mapblockset, Tileset[] tilesets) throws IOException, RawImageException, MetadataException {
        mapBlocksetManager.exportImage(filepath, hpFilepath, blocksPerRow, mapblockset, tilesets);
    }
    
    public void exportMapLayoutImage(Path filepath, Path flagsPath, Path hpTilesPath, MapLayout layout) throws IOException, RawImageException, MetadataException {
        mapLayoutManager.exportImage(filepath, flagsPath, hpTilesPath, layout);
    }

    public MapAnimationManager getMapAnimationManager() {
        return mapAnimationManager;
    }

    public Map getMap() {
        return map;
    }

    public MapEnums getMapEnums() {
        return mapEnums;
    }
    
    public String getSharedBlockInfo() {
        return mapLayoutManager.getSharedBlockInfo();
    }
    
    public String getSharedAnimationInfo() {
        return mapAnimationManager.getSharedAnimationInfo();
    }
}
