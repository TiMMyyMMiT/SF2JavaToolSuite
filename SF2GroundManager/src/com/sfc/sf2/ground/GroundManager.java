/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.ground;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.ground.io.GroundDisassemblyProcessor;
import com.sfc.sf2.ground.io.GroundPackage;
import com.sfc.sf2.helpers.PaletteHelpers;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class GroundManager extends AbstractManager {
    private static int[] paletteInsertInto = new int[] { 3, 4, 8 };
    private static int[] paletteInsertFrom = new int[] { 0, 1, 2 };
       
    private final PaletteManager paletteManager = new PaletteManager();
    private final TilesetManager tilesetManager = new TilesetManager();
    private final GroundDisassemblyProcessor groundDisassemblyProcessor = new GroundDisassemblyProcessor();
    
    private Palette basePalette;
    private Ground ground;
    
    @Override
    public void clearData() {
        paletteManager.clearData();
        basePalette = null;
        if (ground != null && ground.getTileset() != null) {
            ground.getTileset().clearIndexedColorImage(true);
            ground = null;
        }
    }
    
    public Ground importDisassembly(Path basePalettePath, Path palettePath, Path graphicsPath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        importBasePalette(basePalettePath);
        Palette palette = paletteManager.importDisassembly(palettePath, false);
        palette = PaletteHelpers.combinePalettes(basePalette, palette, paletteInsertInto, paletteInsertFrom);
        ground = groundDisassemblyProcessor.importDisassembly(graphicsPath, new GroundPackage(palette));
        Console.logger().finest("EXITING importDisassembly");
        return ground;
    }

    public Ground importImage(Path basePalettePath, Path filePath) throws IOException, DisassemblyException, RawImageException {
        Console.logger().finest("ENTERING importImage");
        importBasePalette(basePalettePath);
        Tileset tileset = tilesetManager.importImage(filePath, true);
        ground = new Ground(tileset);
        Palette palette = PaletteHelpers.combinePalettes(basePalette, tileset.getPalette(), paletteInsertInto, paletteInsertInto);
        tileset.setPalette(palette);
        Console.logger().finest("EXITING importImage");
        return ground;
    }
    
    public void exportDisassembly(Path graphicsPath, Ground ground) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        groundDisassemblyProcessor.exportDisassembly(graphicsPath, ground, null);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void exportPalette(Path palettePath, Ground ground) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportPalette");
        Palette palette = PaletteHelpers.extractColors(ground.getPalette(), paletteInsertInto);
        paletteManager.exportDisassembly(palettePath, palette);
        Console.logger().finest("EXITING exportPalette");
    }
    
    public void exportImage(Path filePath, Ground ground) throws IOException, DisassemblyException, RawImageException {
        Console.logger().finest("ENTERING exportImage");
        tilesetManager.exportImage(filePath, ground.getTileset());
        Console.logger().finest("EXITING exportImage");
    }
    
    private void importBasePalette(Path palettePath) throws IOException, DisassemblyException {
        if (basePalette == null) {
            basePalette = paletteManager.importDisassembly(palettePath, true);
        }
    }

    public Ground getGround() {
        return ground;
    }

    public void setGround(Ground ground) {
        this.ground = ground;
    }
}
