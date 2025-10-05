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
import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.animation.MapAnimationManager;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.gui.MapLayoutPanel;
import com.sfc.sf2.map.layout.MapLayout;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class MapManager extends AbstractManager {
    
    private MapAnimationManager mapAnimationManager = new MapAnimationManager();
    
    private Map map;

    @Override
    public void clearData() {
        mapAnimationManager.clearData();
        map = null;
    }
    
    public Map importDisassemblyFromEntries(Path paletteEntriesPath, Path tilesetEntriesPath, Path mapEntriesPath, int mapIndex) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassemblyFromEntries");
        MapAnimation animation = mapAnimationManager.importDisassemblyFromEntries(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, mapIndex);
        MapLayout layout = mapAnimationManager.getMapLayout();
        /*
        MapArea[] areas = DisassemblyManager.importAreas(areasPath);
        map.setAreas(areas);
        MapFlagCopy[] flagCopies = DisassemblyManager.importFlagCopies(flagCopiesPath);
        map.setFlagCopies(flagCopies);
        MapStepCopy[] stepCopies = DisassemblyManager.importStepCopies(stepCopiesPath);
        map.setStepCopies(stepCopies);
        MapLayer2Copy[] layer2Copies = DisassemblyManager.importLayer2Copies(layer2CopiesPath);
        map.setLayer2Copies(layer2Copies);
        MapWarp[] warps = DisassemblyManager.importWarps(warpsPath);
        map.setWarps(warps);
        MapItem[] chestItems = DisassemblyManager.importChestItems(chestItemsPath);
        map.setChestItems(chestItems);
        MapItem[] otherItems = DisassemblyManager.importOtherItems(otherItemsPath);
        map.setOtherItems(otherItems);
        */
        map = new Map(layout.getBlockset(), layout, new MapArea[0], new MapFlagCopy[0], new MapStepCopy[0], new MapLayer2Copy[0], new MapWarp[0], new MapItem[0], new MapItem[0], animation);
        Console.logger().finest("EXITING importDisassemblyFromEntries");
        return map;
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
    
    public String getSharedAnimationInfo() {
        return mapAnimationManager.getSharedAnimationInfo();
    }
}
