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
import com.sfc.sf2.map.layout.io.MapEntryData;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class MapManager extends AbstractManager {
    
    private MapAnimationManager mapAnimationManager = new MapAnimationManager();
    private MapEnumsAsmProcessor mapEnumsAsmProcessor = new MapEnumsAsmProcessor();
    private MapAreaAsmProcessor mapAreasAsmProcessor = new MapAreaAsmProcessor();
    private MapFlagEventsAsmProcessor mapFlagsAsmProcessor = new MapFlagEventsAsmProcessor();
    private MapStepEventsAsmProcessor mapStepsAsmProcessor = new MapStepEventsAsmProcessor();
    private MapRoofEventsAsmProcessor mapRoofsAsmProcessor = new MapRoofEventsAsmProcessor();
    private MapWarpEventsAsmProcessor mapWarpsAsmProcessor = new MapWarpEventsAsmProcessor();
    private MapItemAsmProcessor mapItemAsmProcessor = new MapItemAsmProcessor();
            
    private Map map;
    private MapEnums mapEnums;

    @Override
    public void clearData() {
        mapAnimationManager.clearData();
        map = null;
        mapEnums = null;
    }
    
    public Map importDisassemblyFromEntries(Path paletteEntriesPath, Path tilesetEntriesPath, Path mapEntriesPath, Path sf2enumsPath, int mapIndex) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassemblyFromEntries");
        ImportMapEnums(sf2enumsPath);
        MapAnimation animation = mapAnimationManager.importDisassemblyFromEntries(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, mapIndex);
        MapLayout layout = mapAnimationManager.getMapLayout();
        MapBlockset blockset = mapAnimationManager.getMapBlockset();
        
        MapEntryData mapEntry = mapAnimationManager.getMapEntries()[mapIndex];
        MapArea[] areas = null;
        MapFlagCopyEvent[] flagCopies = null;
        MapCopyEvent[] stepCopies = null;
        MapCopyEvent[] roofCopies = null;
        MapWarpEvent[] warps = null;
        MapItem[] chestItems = null;
        MapItem[] otherItems = null;
        if (mapEntry.getAreasPath() != null) {
            Path areasPath = PathHelpers.getIncbinPath().resolve(mapEntry.getAreasPath());
            areas = mapAreasAsmProcessor.importAsmData(areasPath, mapEnums);
        }
        if (mapEntry.getFlagEventsPath() != null) {
            Path flagsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getFlagEventsPath());
            flagCopies = mapFlagsAsmProcessor.importAsmData(flagsPath, null);
        }
        if (mapEntry.getStepEventsPath() != null) {
            Path stepsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getStepEventsPath());
            stepCopies = mapStepsAsmProcessor.importAsmData(stepsPath, null);
        }
        if (mapEntry.getRoofEventsPath() != null) {
            Path roofsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getRoofEventsPath());
            roofCopies = mapRoofsAsmProcessor.importAsmData(roofsPath, null);
        }
        if (mapEntry.getWarpEventsPath() != null) {
            Path warpsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getWarpEventsPath());
            warps = mapWarpsAsmProcessor.importAsmData(warpsPath, null);
        }
        if (mapEntry.getChestItemsPath()!= null) {
            Path chestItemsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getChestItemsPath());
            chestItems = mapItemAsmProcessor.importAsmData(chestItemsPath, null);
        }
        if (mapEntry.getOtherItemsPath()!= null) {
            Path otherItemsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getOtherItemsPath());
            otherItems = mapItemAsmProcessor.importAsmData(otherItemsPath, null);
        }
        map = new Map(blockset, layout, areas, flagCopies, stepCopies, roofCopies, warps, chestItems, otherItems, animation);
        Console.logger().finest("EXITING importDisassemblyFromEntries");
        return map;
    }
    
    private void ImportMapEnums(Path sf2enumsPath) throws IOException, AsmException {
        if (mapEnums == null) {
            mapEnums = mapEnumsAsmProcessor.importAsmData(sf2enumsPath, null);
            Console.logger().info("Map enums successfully loaded from : " + sf2enumsPath);
        }
    }
    
    public void exportDisassembly(Path blocksPath, Path layoutPath, Path areasPath, Path flagCopiesPath, Path stepCopiesPath, Path layer2CopiesPath, Path warpsPath, Path chestItemsPath, Path otherItemsPath, Path animationsPath) {
        /*System.out.println("com.sfc.sf2.map.MapManager.importDisassembly() - Exporting disassembly ...");
        mapLayoutManager.exportDisassembly(blocksPath, layoutPath);
        DisassemblyManager.exportAreas(map.getAreas(), areasPath);
        DisassemblyManager.exportFlagCopies(map.getFlagCopies(), flagCopiesPath);
        DisassemblyManager.exportStepCopies(map.getStepCopies(), stepCopiesPath);
        DisassemblyManager.exportLayer2Copies(map.getLayer2Copies(), layer2CopiesPath);
        DisassemblyManager.exportWarps(map.getWarps(), warpsPath);
        DisassemblyManager.exportChestItems(map.getChestItems(), chestItemsPath);
        DisassemblyManager.exportOtherItems(map.getOtherItems(), otherItemsPath);
        DisassemblyManager.exportAnimation(map.getAnimation(), animationsPath);
        System.out.println("com.sfc.sf2.map.MapManager.importDisassembly() - Disassembly exported.");*/
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
        return mapAnimationManager.getSharedBlockInfo();
    }
    
    public String getSharedAnimationInfo() {
        return mapAnimationManager.getSharedAnimationInfo();
    }
}
