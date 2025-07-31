/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.mapsprite.MapSprite;
import com.sfc.sf2.palette.Palette;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class DisassemblyManager {

    private static final String BASE_FILENAME = "mapspriteXXX.bin";
    
    public static MapSprite[] importDisassembly(String basepath, Palette palette){
        System.out.println("com.sfc.sf2.mapsprite.io.DisassemblyManager.importDisassembly() - Importing disassembly ...");
        List<MapSprite> mapSprites = new ArrayList();
        try{
            for(int i=0;i<768;i++){
                String index = String.format("%03d", i);
                String filePath = basepath + BASE_FILENAME.replace("XXX.bin", index+".bin");
                Tile[] tiles = com.sfc.sf2.graphics.io.DisassemblyManager.importDisassembly(filePath, palette, com.sfc.sf2.graphics.GraphicsManager.COMPRESSION_BASIC);
                if(tiles!=null){
                    if(tiles.length==18){
                       MapSprite mapSprite = new MapSprite();
                       mapSprite.setIndex(i);                   
                       mapSprite.setTiles(tiles);
                       mapSprites.add(mapSprite);
                       System.out.println("Created MapSprite " + i + " with " + tiles.length + " tiles.");                       
                    }else{
                        System.out.println("Could not create MapSprite " + i + " because of wrong length : tiles=" + tiles.length);
                    }
                }
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.mapsprite.io.PngManager.importPng() - Error while parsing graphics data : "+e);
        }         
                
        System.out.println("com.sfc.sf2.mapsprite.io.DisassemblyManager.importDisassembly() - Disassembly imported.");
        return mapSprites.toArray(new MapSprite[mapSprites.size()]);
    }
    
    public static MapSprite[] importDisassemblyFromEntryFile(String basepath, String entriesPath, Palette palette){
        System.out.println("com.sfc.sf2.mapsprite.io.DisassemblyManager.importDisassembly() - Importing disassembly ...");
        List<MapSprite> mapSprites = new ArrayList();
        try{
            File entryFile = new File(entriesPath);
            Scanner scan = new Scanner(entryFile);
            List<String> filepaths = new ArrayList();
            while(scan.hasNext()){
                String line = scan.nextLine();
                if(line.contains("dc.l")){
                    String pointer = line.substring(line.indexOf("dc.l")+5).trim();
                    String filepath = null;
                    Scanner filescan = new Scanner(entryFile);
                    while(filescan.hasNext()){
                        String pathline = filescan.nextLine();
                        if(pathline.startsWith(pointer)){
                            filepath = pathline.substring(pathline.indexOf("\"")+1, pathline.lastIndexOf("\""));
                        }
                    }
                    filepaths.add(filepath);
                }
            }            
            for(int i=0;i<filepaths.size();i++){
                String filePath = basepath + filepaths.get(i);
                Tile[] tiles = com.sfc.sf2.graphics.io.DisassemblyManager.importDisassembly(filePath, palette, com.sfc.sf2.graphics.GraphicsManager.COMPRESSION_BASIC);
                if(tiles!=null){
                    if(tiles.length==18){
                       MapSprite mapSprite = new MapSprite();
                       mapSprite.setIndex(i);                   
                       mapSprite.setTiles(tiles);
                       mapSprites.add(mapSprite);
                       System.out.println("Created MapSprite " + i + " from " + filePath);                       
                    }else{
                        System.out.println("Could not create MapSprite " + i + " because of wrong length : tiles=" + tiles.length);
                    }
                }else{
                    //mapSprites.add(null);
                }
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.mapsprite.io.PngManager.importPng() - Error while parsing graphics data : "+e);
        }
                
        System.out.println("com.sfc.sf2.mapsprite.io.DisassemblyManager.importDisassembly() - Disassembly imported.");
        return mapSprites.toArray(new MapSprite[mapSprites.size()]);
    }
    
    public static void exportDisassembly(MapSprite[] mapSprites, String basepath){
        System.out.println("com.sfc.sf2.mapsprite.io.DisassemblyManager.exportDisassembly() - Exporting disassembly ...");
        try {
            for(MapSprite mapSprite : mapSprites){
                String index = String.format("%03d", mapSprite.getIndex()/3);
                int facing = mapSprite.getIndex()%3;
                String filePath = basepath + System.getProperty("file.separator") + BASE_FILENAME.replace("XXX.bin", index+"-"+facing+".bin");
                com.sfc.sf2.graphics.io.DisassemblyManager.exportDisassembly(mapSprite.getTiles(), filePath, com.sfc.sf2.graphics.GraphicsManager.COMPRESSION_BASIC);
            }
        } catch (Exception ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.out.println(ex);
        }
        System.out.println("com.sfc.sf2.mapsprite.io.DisassemblyManager.exportDisassembly() - Disassembly exported.");        
    }
}
