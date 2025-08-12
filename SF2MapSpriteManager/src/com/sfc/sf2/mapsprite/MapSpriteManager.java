/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.core.io.AbstractRawImageProcessor.FileFormat;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.core.io.asm.EntriesAsmData;
import com.sfc.sf2.core.io.asm.EntriesAsmProcessor;
import com.sfc.sf2.graphics.io.TilesetRawImageProcessor;
import com.sfc.sf2.helpers.FileHelpers;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.mapsprite.io.MapSpriteDisassemblyProcessor;
import com.sfc.sf2.mapsprite.io.MapSpritePackage;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import com.sfc.sf2.palette.io.PalettePackage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class MapSpriteManager extends AbstractManager {
    private final PaletteManager paletteManager = new PaletteManager();
    private final MapSpriteDisassemblyProcessor mapSpriteDisassemblyProcessor = new MapSpriteDisassemblyProcessor();
    private final TilesetRawImageProcessor tilesetRawImageProcessor = new TilesetRawImageProcessor();
    private final EntriesAsmProcessor entriesAsmProcessor = new EntriesAsmProcessor();
    
    private MapSprite[] mapSprites;
    
    @Override
    public void clearData() {
        paletteManager.clearData();
        mapSprites = null;
    }

    public MapSprite[] importDisassembly(Path paletteFilePath, Path graphicsFilePath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        mapSprites = new MapSprite[1];
        Palette palette = paletteManager.importDisassembly(paletteFilePath, true);
        int[] indices = getIndicesFromFilename(graphicsFilePath.getFileName());
        MapSpritePackage pckg = new MapSpritePackage(graphicsFilePath.getFileName().toString(), palette, indices);
        mapSprites[0] = mapSpriteDisassemblyProcessor.importDisassembly(graphicsFilePath, pckg);
        Console.logger().info("Mapsprite successfully imported from : " + graphicsFilePath);
        Console.logger().finest("EXITING importDisassembly");
        return mapSprites;
    }
        
    //TODO update to new format
    public MapSprite[] importDisassemblyFromEntryFile(Path paletteFilePath, Path entriesPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassemblyFromEntryFile");
        Palette palette = paletteManager.importDisassembly(paletteFilePath, true);
        EntriesAsmData entriesData = entriesAsmProcessor.importAsmData(entriesPath);
        Console.logger().info("Mapsprites entries successfully imported. Entries found : " + entriesData.entriesCount());
        mapSprites = new MapSprite[entriesData.entriesCount()];
        int failedToLoad = 0;
        for (int i = 0; i < mapSprites.length; i++) {
            Path tilesetPath = PathHelpers.getIncbinPath().resolve(entriesData.getPathForEntry(i));
            try {
                int[] indices = getIndicesFromFilename(tilesetPath.getFileName());
                MapSpritePackage pckg = new MapSpritePackage(tilesetPath.getFileName().toString(), palette, indices);
                mapSprites[i] = mapSpriteDisassemblyProcessor.importDisassembly(tilesetPath, pckg);
            } catch (Exception e) {
                failedToLoad++;
                Console.logger().warning("Mapsprite could not be imported : " + tilesetPath + " : " + e);
            }
        }
        Console.logger().info(mapSprites.length + " mapsprites successfully imported from entries file : " + entriesPath);
        Console.logger().info((entriesData.entriesCount() - entriesData.uniquePathsCount()) + " duplicate mapsprite entries found.");
        if (failedToLoad > 0) {
            Console.logger().severe(failedToLoad + " mapsprites failed to import. See logs above");
        }
        Console.logger().finest("EXITING importDisassemblyFromEntryFile");
        return mapSprites;
    }
    
    public void exportAllDisassemblies(Path basePath) {
        Console.logger().finest("ENTERING exportDisassembly");
        int failedToSave = 0;
        Path filePath = null;
        for (MapSprite mapSprite : mapSprites) {
            try {
                int index = mapSprite.getIndex();
                int facing = mapSprite.getFacingIndex();
                filePath = basePath.resolve(String.format("mapsprite%3d-%d.bin", index, facing));
                mapSpriteDisassemblyProcessor.exportDisassembly(filePath, mapSprite, null);
            } catch (Exception e) {
                failedToSave++;
                Console.logger().warning("Mapsprite could not be exported : " + filePath + " : " + e);
            }
        }
        Console.logger().info((mapSprites.length - failedToSave) + " mapsprites successfully exported.");
        if (failedToSave > 0) {
            Console.logger().severe(failedToSave + " mapsprites failed to export. See logs above");
        }
        Console.logger().finest("EXITING exportDisassembly");
    } 
    
    public void importAllImages(Path paletteFilePath, Path basePath, FileFormat format) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importImage");
        Palette palette = paletteManager.importDisassembly(paletteFilePath, true);
        File[] files = FileHelpers.findAllFilesInDirectory(basePath, "mapsprite", AbstractRawImageProcessor.GetFileExtensionString(format));
        mapSprites = new MapSprite[files.length];
        int failedToLoad = 0;
        for (int i = 0; i < mapSprites.length; i++) {
            Path filePath = files[i].toPath();
            try {
                int[] indices = getIndicesFromFilename(filePath.getFileName());
                PalettePackage pckg = new PalettePackage(palette);
                mapSprites[i] = new MapSprite(tilesetRawImageProcessor.importRawImage(filePath, pckg), indices);
            } catch (Exception e) {
                failedToLoad++;
                Console.logger().warning("Mapsprite could not be imported : " + files[i] + " : " + e);
            }
        }
        Console.logger().info(mapSprites.length + " mapsprites successfully imported from images : " + basePath);
        if (failedToLoad > 0) {
            Console.logger().severe(failedToLoad + " mapsprites failed to import. See logs above");
        }
        Console.logger().finest("EXITING importImage");
    }
    
    public void exportAllImages(Path basePath, FileFormat format) {
        Console.logger().finest("ENTERING exportImage");
        int failedToSave = 0;
        Path filePath = null;
        for (MapSprite mapSprite : mapSprites) {
            try {
                int index = mapSprite.getIndex();
                int facing = mapSprite.getFacingIndex();
                int frame = mapSprite.getFrameIndex();
                filePath = basePath.resolve(String.format("mapsprite%3d-%d-%d%s", index, facing, frame, AbstractRawImageProcessor.GetFileExtensionString(format)));
                tilesetRawImageProcessor.exportRawImage(filePath, mapSprite.getTileset(), null);
            } catch (Exception e) {
                failedToSave++;
                Console.logger().warning("Mapsprite could not be exported : " + filePath + " : " + e);
            }
        }
        Console.logger().info((mapSprites.length - failedToSave) + " mapsprites successfully exported.");
        if (failedToSave > 0) {
            Console.logger().severe(failedToSave + " mapsprites failed to export. See logs above");
        }
        Console.logger().finest("EXITING exportImage");
    }
    
    public MapSprite[] getMapSprites() {
        return mapSprites;
    }
    
    private int[] getIndicesFromFilename(Path filename) {
        String name = filename.toString();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0) {
            name = name.substring(0, dotIndex);
        }
        String[] split = name.substring(9).split("-");
        int[] indices = new int[split.length];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = Integer.parseInt(split[i]);
        }
        return indices;
    }
}
