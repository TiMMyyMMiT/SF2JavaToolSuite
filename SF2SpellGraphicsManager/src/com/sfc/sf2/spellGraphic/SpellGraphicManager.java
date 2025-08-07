/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic;

import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.spellGraphic.io.SpellDisassemblyManager;
import com.sfc.sf2.palette.PaletteManager;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author TiMMy
 */
public class SpellGraphicManager {
       
    private final PaletteManager paletteManager = new PaletteManager();
    private final TilesetManager tilesetManager = new TilesetManager();
    private Palette defaultPalette;
    private Tileset spellTileset;

    public void importDisassembly(String filepath, String defaultPalettePath) {
        /*System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importDisassembly() - Importing disassembly ...");
        filepath = getAbsoluteFilepath(filepath);
        importDefaultPalette(defaultPalettePath);
        spellTileset = SpellDisassemblyManager.importDisassembly(filepath, defaultPalette);
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importDisassembly() - Disassembly imported.");*/
    }
    
    public void exportDisassembly(String filepath) {
        /*System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importDisassembly() - Exporting disassembly ...");
        filepath = getAbsoluteFilepath(filepath);
        SpellDisassemblyManager.exportDisassembly(spellTileset, filepath);
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importDisassembly() - Disassembly exported.");*/
    }   
        
    public void importPng(String filepath, String defaultPalettePath) {
        /*System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importPng() - Importing PNG ...");
        filepath = getAbsoluteFilepath(filepath);
        importDefaultPalette(defaultPalettePath);
        tilesetManager.importImage(filepath);
        spellTileset = new SpellGraphic();
        spellTileset.setTiles(tilesetManager.getTiles());
        Palette palette = spellTileset.getPalette();
        adjustImportedPalette(defaultPalette, palette);
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importPng() - PNG imported.");*/
    }
    
    public void exportPng(String filepath, int tilesPerRow) {
        /*System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.exportPng() - Exporting PNG ...");
        filepath = getAbsoluteFilepath(filepath);
        tilesetManager.exportImage(filepath, tilesPerRow);
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.exportPng() - PNG exported.");*/
    }
    
    private void importDefaultPalette(String palettePath) {        
        /*if (defaultPalette == null) {
            paletteManager.importDisassembly(palettePath);
            defaultPalette = paletteManager.getPalette();
        }*/
    }
    
    private static void adjustImportedPalette(Palette defaultPalette, Palette importedPalette) {
        /*for (int i = 0; i < defaultPalette.getColors().length; i++) {
            if (i != 9 && i != 13 && i != 14)
                importedPalette.getColors()[i] = defaultPalette.getColors()[i];
        }*/
    }
    
    public void clearData() {
        defaultPalette = null;
        spellTileset = null;
    }

    public Tileset getSpellTileset() {
        return spellTileset;
    }

    public void setSpellTileset(Tileset spellTileset) {
        this.spellTileset = spellTileset;
    }

    public Palette getDefaultPalette() {
        return defaultPalette;
    }
}
