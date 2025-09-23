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
import com.sfc.sf2.graphics.Block;
import com.sfc.sf2.helpers.FileHelpers;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.mapsprite.io.MapSpriteDisassemblyProcessor;
import com.sfc.sf2.mapsprite.io.MapSpritePackage;
import com.sfc.sf2.mapsprite.io.MapSpriteRawImageProcessor;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import jdk.jshell.spi.ExecutionControl;

/**
 *
 * @author wiz
 */
public class MapSpriteManager extends AbstractManager {
    
    public enum MapSpriteExportMode {
        INDIVIDUAL_FILES,
        FILE_PER_DIRECTION,
        FILE_PER_SET,
    }
    
    private final PaletteManager paletteManager = new PaletteManager();
    private final MapSpriteDisassemblyProcessor mapSpriteDisassemblyProcessor = new MapSpriteDisassemblyProcessor();
    private final MapSpriteRawImageProcessor mapSpriteRawImageProcessor = new MapSpriteRawImageProcessor();
    private final EntriesAsmProcessor entriesAsmProcessor = new EntriesAsmProcessor();
    
    private MapSpriteEntries mapSprites;
    
    @Override
    public void clearData() {
        paletteManager.clearData();
        if (mapSprites != null) {
            MapSprite[] sprites = mapSprites.getMapSprites();
            for (int i = 0; i < sprites.length; i++) {
                if (sprites[i] != null) {
                    sprites[i].clearIndexedColorImage(true);
                }
            }
            mapSprites = null;
        }
    }

    public MapSprite importDisassembly(Path paletteFilePath, Path graphicsFilePath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        Palette palette = paletteManager.importDisassembly(paletteFilePath, true);
        MapSprite sprite = importDisassembly(graphicsFilePath, palette);
        Console.logger().finest("EXITING importDisassembly");
        return sprite;
    }

    public MapSprite importDisassembly(Path graphicsFilePath, Palette palette) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        mapSprites = new MapSpriteEntries(1);
        int[] indices = getIndicesFromFilename(graphicsFilePath.getFileName());
        MapSpritePackage pckg = new MapSpritePackage(graphicsFilePath.getFileName().toString(), indices, palette, null);
        Block[] frames = mapSpriteDisassemblyProcessor.importDisassembly(graphicsFilePath, pckg);
        MapSprite newSprite = new MapSprite(indices[0]);
        newSprite.addFrame(frames[0], indices[1], 0);
        newSprite.addFrame(frames[1], indices[1], 1);
        mapSprites.addUniqueEntry(indices[0], newSprite);
        Console.logger().info("Mapsprite successfully imported from : " + graphicsFilePath);
        Console.logger().finest("EXITING importDisassembly");
        return newSprite;
    }
        
    //TODO update to new format
    public MapSpriteEntries importDisassemblyFromEntryFile(Path paletteFilePath, Path entriesPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassemblyFromEntryFile");
        Palette palette = paletteManager.importDisassembly(paletteFilePath, true);
        EntriesAsmData entriesData = entriesAsmProcessor.importAsmData(entriesPath, null);
        Console.logger().info("Mapsprites entries successfully imported. Entries found : " + entriesData.entriesCount());
        mapSprites = new MapSpriteEntries(entriesData);
        MapSprite lastMapSprite = null;
        int frameCount = 0;
        int failedToLoad = 0;
        int[] indices = new int[3];
        for (int i = 0; i < entriesData.entriesCount(); i++) {
            Path tilesetPath = null;
            try {
                indices[0] = i/3;
                indices[1] = i%3;
                indices[2] = -1;
                int[] loadIndices = getIndicesFromFilename(entriesData.getEntry(i), "_");
                if (indices[0] == loadIndices[0] && indices[1] == loadIndices[1] && indices[2] == loadIndices[2]) {
                    //Is unique
                    tilesetPath = PathHelpers.getIncbinPath().resolve(entriesData.getPathForEntry(i));
                    MapSpritePackage pckg = new MapSpritePackage(tilesetPath.getFileName().toString(), indices, palette, null);
                    Block[] frames = mapSpriteDisassemblyProcessor.importDisassembly(tilesetPath, pckg);
                    frameCount+=2;
                    if (lastMapSprite == null || lastMapSprite.getIndex() != indices[0]) {
                        lastMapSprite = new MapSprite(indices[0]);
                        mapSprites.addUniqueEntry(indices[0], lastMapSprite);
                    }
                    lastMapSprite.addFrame(frames[0], indices[1], 0);
                    lastMapSprite.addFrame(frames[1], indices[1], 1);
                } else {
                    //Is duplicate
                    if (indices[1] == 0) {
                        mapSprites.addDuplicateEntry(indices[0], loadIndices[0]);
                        i += 2; //Skip other duplicates
                    }
                }
            } catch (Exception e) {
                failedToLoad++;
                Console.logger().warning("Mapsprite could not be imported : " + tilesetPath + " : " + e);
            }
        }
        Console.logger().info(mapSprites.getMapSprites().length + " mapsprites with " + frameCount + " frames successfully imported from images : " + entriesPath);
        Console.logger().info((entriesData.entriesCount() - entriesData.uniqueEntriesCount()) + " duplicate mapsprite entries found.");
        if (failedToLoad > 0) {
            Console.logger().severe(failedToLoad + " mapsprites failed to import. See logs above");
        }
        Console.logger().finest("EXITING importDisassemblyFromEntryFile");
        return mapSprites;
    }
    
    public MapSpriteEntries importAllImages(Path paletteFilePath, Path basePath, Path entriesPath, FileFormat format) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importImage");
        Palette palette = paletteManager.importDisassembly(paletteFilePath, true);
        EntriesAsmData entriesData = entriesAsmProcessor.importAsmData(entriesPath, null);
        Console.logger().info("Mapsprites entries successfully imported. Entries found : " + entriesData.entriesCount());
        mapSprites = new MapSpriteEntries(entriesData);
        File[] files = FileHelpers.findAllFilesInDirectory(basePath, "mapsprite", AbstractRawImageProcessor.GetFileExtensionString(format));
        Console.logger().info(files.length + " mapsprite images found.");
        MapSprite lastMapSprite = null;
        int frameCount = 0;
        int failedToLoad = 0;
        for (File file : files) {
            Path tilesetPath = file.toPath();
            try {
                int[] indices = getIndicesFromFilename(tilesetPath.getFileName());
                MapSpritePackage pckg = new MapSpritePackage(tilesetPath.getFileName().toString(), indices, palette, null);
                Block[] frames = mapSpriteRawImageProcessor.importRawImage(tilesetPath, pckg);
                frameCount+=frames.length;
                if (lastMapSprite == null || lastMapSprite.getIndex() != indices[0]) {
                    lastMapSprite = new MapSprite(indices[0]);
                    mapSprites.addUniqueEntry(indices[0], lastMapSprite);
                }
                for (int i = 0; i < frames.length; i++) {
                    if (indices[1] == -1) {
                        lastMapSprite.addFrame(frames[i], i/2, i%2);
                    } else if (indices[2] == -1) {
                        lastMapSprite.addFrame(frames[i], indices[1], i%2);
                    } else {
                        lastMapSprite.addFrame(frames[i], indices[1], indices[2]);
                    }
                }
            } catch (Exception e) {
                failedToLoad++;
                Console.logger().warning("Mapsprite could not be imported : " + tilesetPath + " : " + e);
            }
        }
        Console.logger().info(mapSprites.getMapSprites().length + " mapsprites with " + frameCount + " frames successfully imported from images : " + basePath);
        if (failedToLoad > 0) {
            Console.logger().severe(failedToLoad + " mapsprites failed to import. See logs above");
        }
        int[] indices = new int[3];
        for (int i = 0; i < entriesData.entriesCount(); i++) {
            indices[0] = i/3;
            indices[1] = i%3;
            indices[2] = -1;
            int[] loadIndices = getIndicesFromFilename(entriesData.getEntry(i), "_");
            if (indices[0] == loadIndices[0] && indices[1] == loadIndices[1] && indices[2] == loadIndices[2]) {
                if (!mapSprites.hasData(indices[0])) {
                    Console.logger().warning(String.format("WARNING No sprite data for entry : mapsprite%03d_%d", indices[0], indices[1]));
                }
            } else {
                if (indices[1] == 0) {
                    mapSprites.addDuplicateEntry(indices[0], loadIndices[0]);
                }
            }
        }
        Console.logger().finest("EXITING importImage");
        return mapSprites;
    }
    
    public void exportAllDisassemblies(Path basePath, MapSpriteEntries mapSprites) {
        Console.logger().finest("ENTERING exportDisassembly");
        this.mapSprites = mapSprites;
        int failedToSave = 0;
        Path filePath = null;
        Block[] frames = new Block[2];
        int totalEntries = mapSprites.getEntries().length;
        for (int i=0; i < totalEntries; i++) {
            if (mapSprites.isDuplicateEntry(i)) continue;
            try {
                for (int s = 0; s < 3; s++) {   //For each facing direction
                    filePath = basePath.resolve(String.format("mapsprite%03d-%d.bin", i, s));
                    MapSprite mapSprite = mapSprites.getMapSprite(i);
                    if (mapSprite == null) {
                        frames[0] = null;
                        frames[1] = null;
                    } else {
                        frames[0] = mapSprite.getFrame(s, 0);
                        frames[1] = mapSprite.getFrame(s, 1);
                    }
                    mapSpriteDisassemblyProcessor.exportDisassembly(filePath, frames, null);
                }
            } catch (Exception e) {
                failedToSave++;
                Console.logger().warning("Mapsprite could not be exported : " + filePath + " : " + e);
            }
        }
        Console.logger().info((mapSprites.getMapSprites().length - failedToSave) + " mapsprites successfully exported.");
        if (failedToSave > 0) {
            Console.logger().severe(failedToSave + " mapsprites failed to export. See logs above");
        }
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void exportAllImages(Path basePath, MapSpriteEntries mapSprites, MapSpriteExportMode exportMode, FileFormat format) {
        Console.logger().finest("ENTERING exportImage");
        this.mapSprites = mapSprites;
        int failedToSave = 0;
        Path filePath = null;
        int files = 0;
        MapSprite[] uniques = mapSprites.getMapSprites();
        for (MapSprite mapSprite : uniques) {
            if (mapSprite == null) continue;
            try {
                int index = mapSprite.getIndex();
                MapSpritePackage pckg = new MapSpritePackage(null, new int[] { index }, mapSprite.getPalette(), exportMode);
                switch (exportMode) {
                    case INDIVIDUAL_FILES:
                        for (int i = 0; i < 6; i++) {
                            Block[] frames = new Block[1];
                            frames[0] = mapSprite.getFrame(i/2, i%2);
                            if (frames[0] != null) {
                                files++;
                                filePath = basePath.resolve(String.format("mapsprite%03d-%d-%d%s", index, i/2, i%2, AbstractRawImageProcessor.GetFileExtensionString(format)));
                                mapSpriteRawImageProcessor.exportRawImage(filePath, frames, pckg);
                            }
                        }
                    break;
                    case FILE_PER_DIRECTION:
                        for (int i = 0; i < 3; i++) {
                            Block[] frames = new Block[2];
                            frames[0] = mapSprite.getFrame(i, 0);
                            frames[1] = mapSprite.getFrame(i, 1);
                            if (frames[0] != null && frames[1] != null) {
                                files++;
                                filePath = basePath.resolve(String.format("mapsprite%03d-%d%s", index, i, AbstractRawImageProcessor.GetFileExtensionString(format)));
                                mapSpriteRawImageProcessor.exportRawImage(filePath, frames, pckg);
                            }
                        }
                        break;
                    case FILE_PER_SET:
                        files++;
                        filePath = basePath.resolve(String.format("mapsprite%03d%s", index, AbstractRawImageProcessor.GetFileExtensionString(format)));
                        mapSpriteRawImageProcessor.exportRawImage(filePath, mapSprite.getFrames(), pckg);
                        break;
                    default:
                        throw new ExecutionControl.NotImplementedException("Export format " + exportMode + "not supported.");
                }
            } catch (Exception e) {
                failedToSave++;
                Console.logger().warning("Mapsprite could not be exported : " + filePath + " : " + e);
            }
        }
        Console.logger().info((files - failedToSave) + " mapsprites successfully exported.");
        if (failedToSave > 0) {
            Console.logger().severe(failedToSave + " mapsprites failed to export. See logs above");
        }
        Console.logger().finest("EXITING exportImage");
    }
    
    public void exportEntries(Path entriesPath, MapSpriteEntries mapSprites) throws IOException, AsmException {
        Console.logger().finest("ENTERING exportEntries");
        this.mapSprites = mapSprites;
        EntriesAsmData asmData = new EntriesAsmData();
        asmData.setHeadername("Map sprites");
        asmData.setPointerListName("pt_Mapsprites");
        asmData.setIsDoubleList(true);
        Path entryPath = PathHelpers.getIncbinPath().relativize(PathHelpers.getBasePath());
        for (int i = 0; i < mapSprites.getEntries().length; i++) {
            if (mapSprites.getEntries()[i] == i) {
                String entry = String.format("Mapsprite%03d", i);
                asmData.addPath(entry+"_0", entryPath.resolve(entry+"-0.bin"));
                asmData.addPath(entry+"_1", entryPath.resolve(entry+"-1.bin"));
                asmData.addPath(entry+"_2", entryPath.resolve(entry+"-2.bin"));
            } else {
                String entry = String.format("Mapsprite%03d", mapSprites.getEntries()[i]);
                asmData.addEntry(entry+"_0");
                asmData.addEntry(entry+"_1");
                asmData.addEntry(entry+"_2");
            }
        }
        entriesAsmProcessor.exportAsmData(entriesPath, asmData, null);
        Console.logger().info("Mapsprites entires successfully exported to : " + entryPath);
        Console.logger().finest("EXITING exportEntries");
    }
    
    public MapSpriteEntries getMapSprites() {
        return mapSprites;
    }
    
    private int[] getIndicesFromFilename(Path filename) {
        return getIndicesFromFilename(filename.toString(), "-");
    }
    
    private int[] getIndicesFromFilename(String name, String splitter) {
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0) {
            name = name.substring(0, dotIndex);
        }
        String[] split = name.substring(9).split(splitter);
        int[] indices = new int[3];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i < split.length ? Integer.parseInt(split[i]) : -1;
        }
        return indices;
    }
}
