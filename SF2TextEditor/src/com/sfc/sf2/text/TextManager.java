/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.text;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.core.io.asm.EntriesAsmData;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.graphics.io.TilesetDisassemblyProcessor;
import com.sfc.sf2.text.io.asm.AsciiTableAsmProcessor;
import com.sfc.sf2.text.io.AsmManager;
import com.sfc.sf2.text.io.TxtManager;
import com.sfc.sf2.text.io.DisassemblyManager;
import com.sfc.sf2.text.io.asm.AllyNamesAsmProcessor;
import com.sfc.sf2.vwfont.FontSymbol;
import com.sfc.sf2.vwfont.VWFontManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class TextManager extends AbstractManager {
    private final TilesetManager tilesetManager = new TilesetManager();
    private final VWFontManager fontManager = new VWFontManager();
    private final AsciiTableAsmProcessor asciiAsmProcessor = new AsciiTableAsmProcessor();
    private final AllyNamesAsmProcessor allyNamesAsmProcessor = new AllyNamesAsmProcessor();
    
    private String[] gamescript;
    private Tileset baseTiles;
    private FontSymbol[] fontSymbols;
    private int[] asciiToSymbolMap;
    private String[] allyNames;

    @Override
    public void clearData() {
        tilesetManager.clearData();
        fontManager.clearData();
        
        gamescript = null;
        if (baseTiles != null) {
            baseTiles.clearIndexedColorImage(true);
        }
        if (fontSymbols != null) {
            for (int i = 0; i < fontSymbols.length; i++) {
                fontSymbols[i].clearIndexedColorImage();
            }
        }
    }
       
    public String[] importDisassembly(Path basePath) {
        Console.logger().finest("ENTERING importDisassembly");
        gamescript = DisassemblyManager.importDisassembly(basePath);
        Console.logger().finest("EXITING importDisassembly");
        return gamescript;
    }
    
    public void exportDisassembly(Path basePath) {
        Console.logger().finest("ENTERING exportDisassembly");
        DisassemblyManager.exportDisassembly(gamescript, basePath.toString());
        Console.logger().finest("EXITING exportDisassembly");       
    }
    
    public String[] importTxt(Path filepath) {
        Console.logger().finest("ENTERING importTxt");
        gamescript = TxtManager.importTxt(filepath.toString());
        Console.logger().finest("EXITING importTxt");
        return gamescript;
    }
    
    public void exportTxt(Path filepath) {
        Console.logger().finest("ENTERING exportTxt");
        TxtManager.exportTxt(gamescript, filepath.toString());
        Console.logger().finest("EXITING exportTxt");     
    }
       
    public String[] importAsm(Path path) {
        Console.logger().finest("ENTERING importAsm");
        gamescript = AsmManager.importAsm(path.toString(), gamescript);
        Console.logger().finest("EXITING importAsm");
        return gamescript;
    }
    
    public Tileset importBaseTiles(Path palettePath, Path tilesetPath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importBaseTiles");
        baseTiles = tilesetManager.importDisassembly(palettePath, tilesetPath, TilesetDisassemblyProcessor.TilesetCompression.STACK, 16);
        Console.logger().finest("EXITING importBaseTiles");
        return baseTiles;
    }
    
    public FontSymbol[] importVWFonts(Path vwFontPath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importVWFonts");
        fontSymbols = fontManager.importDisassembly(vwFontPath);
        Console.logger().finest("EXITING importVWFonts");
        return fontSymbols;
    }
    
    public int[] importAsciiMap(Path asciiMapPath) throws IOException, FileNotFoundException, AsmException {
        Console.logger().finest("ENTERING importAsciiMap");
        asciiToSymbolMap = asciiAsmProcessor.importAsmData(asciiMapPath);
        Console.logger().finest("EXITING importAsciiMap");
        return asciiToSymbolMap;
    }
    
    public String[] importAllyNames(Path allyNamesPath) throws IOException, FileNotFoundException, AsmException {
        Console.logger().finest("ENTERING importAllyNames");
        EntriesAsmData data = allyNamesAsmProcessor.importAsmData(allyNamesPath);
        allyNames = new String[data.uniqueEntriesCount()];
        for (int i = 0; i < allyNames.length; i++) {
            allyNames[i] = data.getUniqueEntries(i);
        }
        Console.logger().finest("EXITING importAllyNames");
        return allyNames;
    }
    
    public String[] getGameScript() {
        return gamescript;
    }
    
    public void setGameScript(String[] gamescript) {
        this.gamescript = gamescript;
    }
    
    public Tileset getBaseTiles() {
        return baseTiles;
    }
    
    public FontSymbol[] getFontSymbols() {
        return fontSymbols;
    }
    
    public int[] getAsciiToSymbolMap() {
        return asciiToSymbolMap;
    }
    
    public String[] getAllyNames() {
        return allyNames;
    }
}
