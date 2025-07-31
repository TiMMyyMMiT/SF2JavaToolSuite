/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.vwfont.io;

import com.sfc.sf2.vwfont.FontSymbol;
import com.sfc.sf2.vwfont.graphics.VWFontDecoder;
import com.sfc.sf2.vwfont.graphics.VWFontEncoder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class RomManager {
    
    public static final int ORIGINAL_ROM_TYPE = 0;
    public static final int CARAVAN_ROM_TYPE = 1;
    
    private static final int[][] VWFONT_OFFSETS = {   {0x29002,0x29A02},
                                                            {0x29002,0x29A02}
                                                        };
    
    private static File romFile;  
    private static byte[] romData;
    
    public static FontSymbol[] importRom(int romType, String romFilePath){
        System.out.println("com.sfc.sf2.vwfont.io.RomManager.importRom() - Importing ROM ...");
        RomManager.openFile(romFilePath);
        FontSymbol[] symbols = RomManager.parseVWFont(romType);        
        System.out.println("com.sfc.sf2.vwfont.io.RomManager.importRom() - ROM imported.");
        return symbols;
    }
    
    private static void openFile(String romFilePath){
        try {
            System.out.println("com.sfc.sf2.vwfont.io.RomManager.openFile() - ROM file path : " + romFilePath);
            romFile = new File(romFilePath);
            romData = Files.readAllBytes(Paths.get(romFile.getAbsolutePath()));
            System.out.println("com.sfc.sf2.vwfont.io.RomManager.openFile() - File opened.");
        } catch (IOException ex) {
            Logger.getLogger(RomManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static FontSymbol[] parseVWFont(int romType){
        System.out.println("com.sfc.sf2.vwfont.io.RomManager.parseVWFont() - Parsing VW Font ...");
        byte[] data = Arrays.copyOfRange(romData,VWFONT_OFFSETS[romType][0],VWFONT_OFFSETS[romType][1]);        
        FontSymbol[] symbols = VWFontDecoder.parseVWFont(data);
        System.out.println("com.sfc.sf2.vwfont.io.RomManager.parseVWFont() - VW Font parsed.");
        return symbols;
    }
    
    public static void exportRom(int romType, FontSymbol[] symbols, String romFilePath){
        System.out.println("com.sfc.sf2.vwfont.io.RomManager.exportRom() - Exporting ROM ...");
        byte[] newVWFontFileBytes = VWFontEncoder.produceVWFont(symbols);
        RomManager.writeFile(romType, romFilePath, newVWFontFileBytes);
        System.out.println("com.sfc.sf2.vwfont.io.RomManager.exportRom() - ROM exported.");        
    }
  
    private static void writeFile(int romType, String romFilePath, byte[] newVWFontFileBytes){
        try {
            System.out.println("com.sfc.sf2.vwfont.io.RomManager.writeFile() - Writing file ...");
            romFile = new File(romFilePath);
            Path romPath = Paths.get(romFile.getAbsolutePath());
            romData = Files.readAllBytes(romPath);
            System.arraycopy(newVWFontFileBytes, 0, romData, VWFONT_OFFSETS[romType][0], newVWFontFileBytes.length);
            Files.write(romPath,romData);
            System.out.println(romData.length + " bytes into " + romFilePath);  
            System.out.println("com.sfc.sf2.vwfont.io.RomManager.writeFile() - File written.");
        } catch (IOException ex) {
            Logger.getLogger(RomManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
