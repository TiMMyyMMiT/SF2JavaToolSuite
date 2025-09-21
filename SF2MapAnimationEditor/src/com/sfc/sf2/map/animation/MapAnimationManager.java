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
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.map.layout.MapLayoutManager;
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
    private final MapAnimationAsmProcessor mapAnimationAsmProcessor = new MapAnimationAsmProcessor();
    private final EntriesAsmProcessor entriesAsmProcessor = new EntriesAsmProcessor();

    private MapAnimation animation;
    
    @Override
    public void clearData() {
        mapLayoutManager.clearData();
        tilesetmanager.clearData();
        animation = null;
    }
    
    public MapAnimation importDisassembly(Path palettesEntriesPath, Path tilesetsEntriesPath, Path tilesetsFilePath, Path blocksPath, Path layoutPath, Path animationsPath) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        clearData();
        mapLayoutManager.importDisassemblyFromEntryFiles(palettesEntriesPath, tilesetsEntriesPath, tilesetsFilePath, blocksPath, layoutPath);
        if (animationsPath.toFile().exists()) {
            animation = mapAnimationAsmProcessor.importAsmData(animationsPath, new MapAnimationPackage(getMapLayout().getTilesets()));
            importTileset(getMapLayout().getPalette(), tilesetsEntriesPath, animation.getTilesetId());
            Console.logger().info("Map layout and animation succesfully imported for : " + animationsPath);
        } else {
            animation = new MapAnimation(-1, 0, new MapAnimationFrame[0], getMapLayout().getTilesets());
            animation.setAnimationTileset(null);
            Console.logger().warning("WARNING Map has no animation.");
        }
        Console.logger().finest("EXITING importDisassembly");
        return animation;
    }
    
    public Tileset importTileset(Palette palette, Path tilesetEntriesPath, int tilesetId) throws IOException, AsmException, DisassemblyException {
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
        getMapLayout().setTilesets(animation.getModifiedTilesets());
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
        Console.logger().finest("EXITING exportDisassembly");  
    }

    public MapAnimation getMapAnimation() {
        return animation;
    }

    public MapLayout getMapLayout() {
        return mapLayoutManager.getMapLayout();
    }
    
    public Tileset[] getMapTilesets() {
        if (getMapLayout() == null) {
            return null;
        } else {
            return getMapLayout().getTilesets();
        }
    }
}
