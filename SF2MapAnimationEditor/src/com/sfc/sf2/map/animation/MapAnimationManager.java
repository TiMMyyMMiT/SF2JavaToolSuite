/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.animation;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.core.io.asm.EntriesAsmData;
import com.sfc.sf2.core.io.asm.EntriesAsmProcessor;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.graphics.io.TilesetDisassemblyProcessor;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.map.animation.io.MapAnimationAsmProcessor;
import com.sfc.sf2.map.animation.io.MapAnimationPackage;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.map.layout.MapLayoutManager;
import com.sfc.sf2.map.layout.io.MapEntriesAsmProcessor;
import com.sfc.sf2.map.layout.io.MapEntryData;
import com.sfc.sf2.palette.Palette;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class MapAnimationManager extends AbstractManager {
       
    private final MapLayoutManager mapLayoutManager = new MapLayoutManager();
    private final TilesetManager tilesetmanager = new TilesetManager();
    private final EntriesAsmProcessor entriesAsmProcessor = new EntriesAsmProcessor();
    private final MapEntriesAsmProcessor mapEntriesAsmProcessor = new MapEntriesAsmProcessor();
    private final MapAnimationAsmProcessor mapAnimationAsmProcessor = new MapAnimationAsmProcessor();

    private MapAnimation animation;
    private String sharedAnimationInfo;
    
    @Override
    public void clearData() {
        mapLayoutManager.clearData();
        tilesetmanager.clearData();
        animation = null;
        sharedAnimationInfo = null;
    }
    
    public MapAnimation importDisassembly(Path animationsPath, Path tilesetsEntriesPath) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        if (animationsPath != null && animationsPath.toFile().exists()) {
            animation = mapAnimationAsmProcessor.importAsmData(animationsPath, new MapAnimationPackage(getMapLayout().getTilesets()));
            importAnimationTileset(getMapLayout().getPalette(), tilesetsEntriesPath, animation.getTilesetId());
            getMapLayout().setTilesets(animation.getModifiedTilesets());
            Console.logger().info("Map layout and animation succesfully imported for : " + animationsPath);
        } else {
            animation = new MapAnimation(-1, 0, new MapAnimationFrame[0], getMapLayout().getTilesets());
            animation.setAnimationTileset(null);
            getMapLayout().setTilesets(animation.getOriginalTilesets());
            Console.logger().warning("WARNING Map has no animation.");
        }
        //checkForSharedAnimations(terrainEntries, 0);
        Console.logger().finest("EXITING importDisassembly");
        return animation;
    }
    
    public MapAnimation importDisassemblyFromMapData(Path palettesEntriesPath, Path tilesetsEntriesPath, Path tilesetsFilePath, Path blocksPath, Path layoutPath, Path animationsPath) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassemblyFromMapData");
        sharedAnimationInfo = null;
        mapLayoutManager.importDisassembly(palettesEntriesPath, tilesetsEntriesPath, tilesetsFilePath, blocksPath, layoutPath);
        animation = importDisassembly(animationsPath, tilesetsEntriesPath);
        Console.logger().finest("EXITING importDisassemblyFromMapData");
        return animation;
    }
    
    public MapAnimation importDisassemblyFromRawFiles(Path palettePath, Path[] tilesetsFilePath, Path blocksetPath, Path layoutPath, Path animationPath, Path tilesetEntriesPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassemblyFromRawFiles");
        sharedAnimationInfo = null;
        mapLayoutManager.importDisassemblyFromRawFiles(palettePath, tilesetsFilePath, blocksetPath, layoutPath);
        animation = importDisassembly(animationPath, tilesetEntriesPath);
        Console.logger().finest("EXITING importDisassemblyFromRawFiles");
        return animation;
    }
    
    public MapAnimation importDisassemblyFromEntries(Path palettesEntriesPath, Path tilesetsEntriesPath, Path mapEntriesPath, int mapIndex) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassemblyFromEntries");
        sharedAnimationInfo = null;
        mapLayoutManager.importDisassemblyFromMapEntries(palettesEntriesPath, tilesetsEntriesPath,mapEntriesPath, mapIndex);
        MapEntryData[] mapEntries = mapLayoutManager.getMapEntries();
        MapEntryData mapEntry = (mapIndex >= 0 && mapIndex < mapEntries.length) ? mapEntries[mapIndex] : null;
        if (mapEntry.getAnimationsPath() == null) {
            importDisassembly(null, tilesetsEntriesPath);
        } else {
            importDisassembly(PathHelpers.getIncbinPath().resolve(mapEntry.getAnimationsPath()), tilesetsEntriesPath);
            checkForSharedAnimations(mapEntries, mapIndex, mapEntry.getAnimationsPath());
        }
        Console.logger().finest("EXITING importDisassemblyFromEntries");
        return animation;
    }
    
    public Tileset importAnimationTileset(Palette palette, Path tilesetEntriesPath, int tilesetId) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importTileset");
        EntriesAsmData tilesetData = entriesAsmProcessor.importAsmData(tilesetEntriesPath, null);
        if (tilesetId < 0 || tilesetId >= tilesetData.entriesCount()) {
            animation.setAnimationTileset(null);
            Console.logger().warning("WARNING Map index out of range : " + tilesetId);
            return null;
        }
        Path tilesetPath = PathHelpers.getIncbinPath().resolve(tilesetData.getPathForEntry(tilesetId));
        Tileset tileset = tilesetmanager.importDisassembly(tilesetPath, palette, TilesetDisassemblyProcessor.TilesetCompression.STACK, 8);
        animation.setAnimationTileset(tileset);
        Console.logger().info("Tileset succesfully imported for : " + tilesetPath);
        Console.logger().finest("EXITING importTileset");
        return tileset;
    }
    
    public void exportDisassembly(Path animationsPath, MapAnimation animation) throws IOException, AsmException {
        Console.logger().finest("ENTERING exportDisassembly");
        if (!this.animation.equals(animation)) {
            this.animation.clearData();
        }
        this.animation = animation;
        mapAnimationAsmProcessor.exportAsmData(animationsPath, animation, null);
        Console.logger().info("Map animation succesfully exported to : " + animationsPath);
        Console.logger().finest("EXITING exportDisassembly");  
    }
    
    public void exportDisassemblyFromMapEntries(Path mapEntriesPath, int mapId, MapAnimation animation) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING exportDisassemblyFromMapEntries");
        this.animation = animation;
        MapEntryData[] mapEntries = ImportMapEntries(mapEntriesPath);
        if (mapId < 0 || mapId >= mapEntries.length || mapEntries[mapId] == null) {
            throw new DisassemblyException("Cannot export map " + mapId + ". Map entry was not found or map entries are corrupted.");
        }
        MapEntryData mapEntry = mapEntries[mapId];
        Path animationPath = mapEntry.getLayoutPath() == null ? null : PathHelpers.getIncbinPath().resolve(mapEntry.getAnimationsPath());
        mapAnimationAsmProcessor.exportAsmData(animationPath, animation, null);
        Console.logger().info("Map animation successfully exported from entries to : " + animationPath);
        Console.logger().finest("EXITING exportDisassemblyFromMapEntries");   
    }
    
    public MapEntryData[] ImportMapEntries(Path mapEntriesPath) throws IOException, AsmException {
        return mapLayoutManager.ImportMapEntries(mapEntriesPath);
    }
    
    public void checkForSharedAnimations(MapEntryData[] mapEntries, int mapId, String path) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = 0; i < mapEntries.length; i++) {
            if (path.equals(mapEntries[i].getAnimationsPath())) {
                sb.append(String.format("- Map%02d\n", i));
                count++;
            }
        }
        if (count <= 1) {
            sharedAnimationInfo = null;
            Console.logger().finest("Animation not shared with other maps");
        } else {
            sharedAnimationInfo = sb.toString();
            Console.logger().finest(String.format("Animation shared between %d maps", count));
        }
    }

    public MapAnimation getMapAnimation() {
        return animation;
    }

    public MapLayout getMapLayout() {
        return mapLayoutManager.getMapLayout();
    }
    
    public MapBlockset getMapBlockset() {
        return mapLayoutManager.getMapBlockset();
    }
    
    public Tileset[] getMapTilesets() {
        if (getMapLayout() == null) {
            return null;
        } else {
            return getMapLayout().getTilesets();
        }
    }

    public String getSharedBlockInfo() {
        return mapLayoutManager.getSharedBlockInfo();
    }

    public String getSharedAnimationInfo() {
        return sharedAnimationInfo;
    }
}
