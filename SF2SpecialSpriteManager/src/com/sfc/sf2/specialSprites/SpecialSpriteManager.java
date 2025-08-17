/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.specialSprites;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.helpers.FileHelpers;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import com.sfc.sf2.specialSprites.io.SpecialSpriteDisassemblyProcessor;
import com.sfc.sf2.specialSprites.io.SpecialSpritePackage;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author TiMMy
 */
public class SpecialSpriteManager extends AbstractManager { 

    private final SpecialSpriteDisassemblyProcessor specialSpriteDisassemblyProcessor = new SpecialSpriteDisassemblyProcessor();
    private final PaletteManager paletteManager = new PaletteManager();
    private final TilesetManager tilesetManager = new TilesetManager();
    
    private Tileset tileset;
    
    @Override
    public void clearData() {
        if (tileset != null) {
            tileset.clearIndexedColorImage(true);
            tileset = null;
        }
    }
    
    public void importDisassembly(Path filePath, int blockRows, int blockColumns, int tilesPerBlock, Path paletteFilepath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        Palette optionalPalette = null;
        if (paletteFilepath != null) {
            optionalPalette = paletteManager.importDisassembly(paletteFilepath, true);
        }
        SpecialSpritePackage pckg = new SpecialSpritePackage(PathHelpers.filenameFromPath(filePath), blockRows, blockColumns, tilesPerBlock, optionalPalette);
        tileset = specialSpriteDisassemblyProcessor.importDisassembly(filePath, pckg);
        Console.logger().finest("EXITING importDisassembly");
    }
    
    public void exportDisassembly(Path filePath, Tileset tileset, int blockRows, int blockColumns, int tilesPerBlock, boolean savePaletteInFile) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.tileset = tileset;
        SpecialSpritePackage pckg = new SpecialSpritePackage(null, blockRows, blockColumns, tilesPerBlock, savePaletteInFile ? tileset.getPalette() : null);
        specialSpriteDisassemblyProcessor.exportDisassembly(filePath, tileset, pckg);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void importImage(Path filePath) throws IOException, RawImageException {
        Console.logger().finest("ENTERING importImage");
        tileset = tilesetManager.importImage(filePath, true);
        Console.logger().finest("EXITING importImage");
    }
    
    public void exportImage(Path filePath, Tileset tileset, int tilesPerRow) throws IOException, RawImageException {
        Console.logger().finest("ENTERING exportImage");
        this.tileset = tileset;
        tilesetManager.exportImage(filePath, tileset);
        Console.logger().finest("EXITING exportImage");
    }

    public Tileset getTileset() {
        return tileset;
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
    }
}
