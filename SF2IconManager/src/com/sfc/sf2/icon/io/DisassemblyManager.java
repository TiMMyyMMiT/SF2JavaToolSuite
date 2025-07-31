/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.icon.io;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.graphics.Tile;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xeno
 */
public class DisassemblyManager {
    
    private static final String CHARACTER_FILENAME = "iconXXX.bin";
    
    public static Tile[] importDisassembly(String paletteFilePath, String basepath){
        System.out.println("com.sfc.sf2.icon.io.DisassemblyManager.importDisassembly() - Importing disassembly ...");
        File[] files = new File(basepath).listFiles();
        GraphicsManager graphicsManager = new GraphicsManager();
        List<Tile[]> icons = new ArrayList();
        try{
            for(File file : files){
                if (file.toString().toLowerCase().endsWith(".bin")){
                    String graphicsFilePath = basepath + file.getName();
                    System.out.println("Parsing " + graphicsFilePath);
                    graphicsManager.importDisassembly(paletteFilePath, graphicsFilePath, GraphicsManager.COMPRESSION_NONE);
                    Tile[] iconTiles = graphicsManager.getTiles();
                    icons.add(iconTiles);
                    System.out.println("Parsed " + graphicsFilePath);
                }
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.icon.io.DisassemblyManager.importDisassembly() - Error while parsing icon data : "+e);
        }        
        Tile[] iconsArray = new Tile[icons.size()*6];
        for(int i=0;i<icons.size();i++){
            System.arraycopy(icons.get(i), 0, iconsArray, i*6, 6);
        }
        System.out.println("com.sfc.sf2.icon.io.DisassemblyManager.importDisassembly() - Disassembly imported.");
        return iconsArray;                
    }
    
    
    public static void exportDisassembly(Tile[] tiles, String basepath){
        GraphicsManager graphicsManager = new GraphicsManager();
        try {
            System.out.println("com.sfc.sf2.icon.io.DisassemblyManager.exportDisassembly() - Exporting disassembly ...");
            for(int i = 0;i<tiles.length;i+=6){
                String index = String.format("%03d", i/6);
                String graphicsFilePath = basepath + System.getProperty("file.separator") + CHARACTER_FILENAME.replace("XXX.bin", index+".bin");
                graphicsManager.setTiles(Arrays.copyOfRange(tiles, i, i+6));
                graphicsManager.exportDisassembly(graphicsFilePath, GraphicsManager.COMPRESSION_NONE);
            }
            System.out.println("com.sfc.sf2.icon.io.DisassemblyManager.exportDisassembly() - Disassembly exported.");
        } catch (Exception ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
    }  
    
}
