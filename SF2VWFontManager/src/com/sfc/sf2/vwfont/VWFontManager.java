/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.vwfont;

import com.sfc.sf2.vwfont.io.DisassemblyManager;
import com.sfc.sf2.vwfont.io.PngManager;
import com.sfc.sf2.vwfont.io.RomManager;

/**
 *
 * @author wiz
 */
public class VWFontManager {
       
    public FontSymbol[] symbols;
       
    public void importDisassembly(String filePath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importDisassembly() - Importing disassembly ...");
        symbols = DisassemblyManager.importDisassembly(filePath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String filePath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportDisassembly(symbols, filePath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importDisassembly() - Disassembly exported.");        
    }   
    
    public void importOriginalRom(String originalRomFilePath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importOriginalRom() - Importing original ROM ...");
        symbols = RomManager.importRom(RomManager.ORIGINAL_ROM_TYPE,originalRomFilePath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importOriginalRom() - Original ROM imported.");
    }
    
    public void exportOriginalRom(String originalRomFilePath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.exportOriginalRom() - Exporting original ROM ...");
        RomManager.exportRom(RomManager.ORIGINAL_ROM_TYPE, symbols, originalRomFilePath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.exportOriginalRom() - Original ROM exported.");        
    }   
    
    public void importCaravanRom(String caravanRomFilePath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importCaravanRom() - Importing Caravan ROM ...");
        symbols = RomManager.importRom(RomManager.CARAVAN_ROM_TYPE,caravanRomFilePath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importCaravanRom() - Caravan ROM imported.");
    }
    
    public void exportCaravanRom(String caravanRomFilePath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.exportCaravanRom() - Exporting original ROM ...");
        RomManager.exportRom(RomManager.CARAVAN_ROM_TYPE, symbols, caravanRomFilePath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.exportCaravanRom() - Caravan ROM exported.");        
    }    
    
    public void importPng(String basepath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importPng() - Importing PNG ...");
        symbols = PngManager.importPng(basepath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importPng() - PNG imported.");
    }
    
    public void exportPng(String basepath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.exportPng() - Exporting PNG ...");
        PngManager.exportPng(symbols, basepath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.exportPng() - PNG exported.");       
    }
    
    public FontSymbol[] getFontSymbols() {
        return symbols;
    }
}
