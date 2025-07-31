/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette.io;

import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.graphics.PaletteDecoder;
import com.sfc.sf2.palette.graphics.PaletteEncoder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class DisassemblyManager {
    
    public static Palette importDisassembly(String filePath) {
        return importDisassembly(filePath, true);
    }
    
    public static Palette importDisassembly(String filePath, boolean firstColorTransparent) {
        System.out.println("com.sfc.sf2.palette.io.DisassemblyManager.importDisassembly() - Importing disassembly ...");
        Palette palette = DisassemblyManager.parsePalette(filePath, firstColorTransparent);
        System.out.println("com.sfc.sf2.palette.io.DisassemblyManager.importDisassembly() - Disassembly imported.");
        return palette;
    }
    
    private static Palette parsePalette(String filePath, boolean firstColorTransparent) {
        System.out.println("com.sfc.sf2.palette.io.DisassemblyManager.parsePalette() - Parsing Palette ...");
        Palette palette = null;       
        try {
            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);
            String filename = path.getFileName().toString();
            filename = filename.substring(0, filename.lastIndexOf("."));
            palette = new Palette(filename, PaletteDecoder.parsePalette(data, firstColorTransparent), firstColorTransparent);
        } catch (Exception e) {
             System.err.println("com.sfc.sf2.palette.io.DisassemblyManager.parsePalette() - Error while parsing Palette data : "+e);
        } 
        System.out.println("com.sfc.sf2.palette.io.DisassemblyManager.parsePalette() - Palette parsed.");
        return palette;
    }
    
    public static void exportDisassembly(Palette palette, String filePath){
        System.out.println("com.sfc.sf2.palette.io.DisassemblyManager.exportDisassembly() - Exporting disassembly ...");
        DisassemblyManager.producePalette(palette);
        DisassemblyManager.writeFiles(filePath);
        System.out.println("com.sfc.sf2.palette.io.DisassemblyManager.exportDisassembly() - Disassembly exported.");        
    }   

    private static void producePalette(Palette palette) {
        System.out.println("com.sfc.sf2.palette.io.DisassemblyManager.producePalette() - Producing palette ...");
        PaletteEncoder.producePalette(palette.getColors());
        System.out.println("com.sfc.sf2.palette.io.DisassemblyManager.producePalette() - Palette produced.");
    }    
  
    private static void writeFiles(String filePath) {
        try {
            System.out.println("com.sfc.sf2.palette.io.DisassemblyManager.writeFiles() - Writing file ...");
            Path paletteFilePath = Paths.get(filePath);
            byte[] newPaletteFileBytes = PaletteEncoder.getNewPaletteFileBytes();
            Files.write(paletteFilePath,newPaletteFileBytes);
            System.out.println(newPaletteFileBytes.length + " bytes into " + paletteFilePath);
            System.out.println("com.sfc.sf2.palette.io.DisassemblyManager.writeFiles() - File written.");
        } catch (IOException ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
