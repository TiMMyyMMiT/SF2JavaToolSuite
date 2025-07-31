/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.vwfont.io;

import com.sfc.sf2.vwfont.FontSymbol;
import com.sfc.sf2.vwfont.graphics.VWFontDecoder;
import com.sfc.sf2.vwfont.graphics.VWFontEncoder;
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
    
    
    public static FontSymbol[] importDisassembly(String filePath){
        System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.importDisassembly() - Importing disassembly ...");
        FontSymbol[] symbols = DisassemblyManager.parseVWFont(filePath);        
        System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.importDisassembly() - Disassembly imported.");
        return symbols;
    }
    
    private static FontSymbol[] parseVWFont(String filePath){
        System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.parseTextbank() - Parsing VW Font ...");
        FontSymbol[] symbols = null;       
        try{
            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);
            symbols = VWFontDecoder.parseVWFont(data);
        }catch(Exception e){
             System.err.println("com.sfc.sf2.vwfont.ioDisassemblyManager.parseTextbank() - Error while parsing VW Font data : "+e);
        } 
        System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.parseTextbank() - VW Font parsed.");
        return symbols;
    }
    
    public static void exportDisassembly(FontSymbol[] symbols, String filePath){
        System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.exportDisassembly() - Exporting disassembly ...");
        byte[] newVWFontFileBytes = VWFontEncoder.produceVWFont(symbols);
        DisassemblyManager.writeFiles(filePath, newVWFontFileBytes);
        System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.exportDisassembly() - Disassembly exported.");        
    }
  
    private static void writeFiles(String filePath, byte[] newVWFontFileBytes){
        try {
            System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.writeFiles() - Writing file ...");
            Path vwfontFilePath = Paths.get(filePath);
            Files.write(vwfontFilePath,newVWFontFileBytes);
            System.out.println(newVWFontFileBytes.length + " bytes into " + vwfontFilePath);
            System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.writeFiles() - File written.");
        } catch (IOException ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    
}
