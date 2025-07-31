/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette.io;

import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.graphics.PaletteDecoder;
import com.sfc.sf2.palette.graphics.PaletteEncoder;
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
    
    private static final int BASE_PALETTE_OFFSET = 0x309E;
    
    private static File romFile;  
    private static byte[] romData;
    
    public static Palette importRom(String romFilePath, String offsetString, String lengthString) {
        System.out.println("com.sfc.sf2.palette.io.RomManager.importRom() - Importing ROM ...");
        RomManager.openFile(romFilePath);
        int offset = Integer.parseInt(offsetString,16);
        int length = Integer.parseInt(lengthString);
        Palette palette = RomManager.parsePalette(offset,length);
        System.out.println("com.sfc.sf2.palette.io.RomManager.importRom() - ROM imported.");
        return palette;
    }
    
    public static void exportRom(Palette palette, String romFilePath, String offsetString) {
        System.out.println("com.sfc.sf2.palette.io.RomManager.exportRom() - Exporting ROM ...");
        RomManager.producePalette(palette);
        int offset = Integer.parseInt(offsetString,16);
        RomManager.writeFile(romFilePath,offset);
        System.out.println("com.sfc.sf2.palette.io.RomManager.exportRom() - ROM exported.");
    }    
    
    private static void openFile(String romFilePath){
        try {
            System.out.println("com.sfc.sf2.palette.io.RomManager.openFile() - ROM file path : " + romFilePath);
            romFile = new File(romFilePath);
            romData = Files.readAllBytes(Paths.get(romFile.getAbsolutePath()));
            System.out.println("com.sfc.sf2.palette.io.RomManager.openFile() - File opened.");
        } catch (IOException ex) {
            Logger.getLogger(RomManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static Palette parsePalette(int offset, int length) {
        System.out.println("com.sfc.sf2.palette.io.RomManager.parsePalette() - Parsing Palette ...");
        byte[] data = Arrays.copyOfRange(romData,offset,offset+length);
        Palette palette = new Palette(PaletteDecoder.parsePalette(data));
        System.out.println("com.sfc.sf2.palette.io.RomManager.parsePalette() - Palette parsed.");
        return palette;
    }

    private static void producePalette(Palette palette) {
        System.out.println("com.sfc.sf2.palette.io.DisassemblyManager.producePalette() - Producing Palette ...");
        PaletteEncoder.producePalette(palette.getColors());
        System.out.println("com.sfc.sf2.palette.io.DisassemblyManager.producePalette() - Palette produced.");
    }    
  
    private static void writeFile(String romFilePath, int offset) {
        try {
            System.out.println("com.sfc.sf2.palette.io.RomManager.writeFile() - Writing file ...");
            romFile = new File(romFilePath);
            Path romPath = Paths.get(romFile.getAbsolutePath());
            romData = Files.readAllBytes(romPath);
            byte[] palette = PaletteEncoder.getNewPaletteFileBytes();
            System.arraycopy(palette, 0, romData, offset, palette.length);
            Files.write(romPath,romData);
            System.out.println(romData.length + " bytes into " + romFilePath);
            System.out.println("com.sfc.sf2.palette.io.RomManager.writeFile() - File written.");
        } catch (IOException ex) {
            Logger.getLogger(RomManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
