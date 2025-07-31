/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.background.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.background.Background;
import com.sfc.sf2.graphics.compressed.StackGraphicsDecoder;
import com.sfc.sf2.graphics.compressed.StackGraphicsEncoder;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.graphics.PaletteDecoder;
import com.sfc.sf2.palette.graphics.PaletteEncoder;
import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class DisassemblyManager {

    private static final String BASE_FILENAME = "backgroundXX.bin";
    private static final int BACKGROUND_TILE_LENGTH = 384;
    
    public static Background[] importDisassembly(String basepath){
        System.out.println("com.sfc.sf2.background.io.DisassemblyManager.importDisassembly() - Importing disassembly ...");
        List<Background> backgrounds = new ArrayList();
        try{
            for(int i=0;i<255;i++){
                String index = String.format("%02d", i);
                String filePath = basepath + BASE_FILENAME.replace("XX.bin", index+".bin");
                Tile[] tiles = parseGraphics(filePath);
                if(tiles!=null){
                    if(tiles.length==BACKGROUND_TILE_LENGTH){
                       Background background = new Background();
                       background.setIndex(i);                   
                       background.setTiles(tiles);
                       backgrounds.add(background);
                       System.out.println("Created Background " + i + " with " + tiles.length + " tiles.");                       
                    }else{
                        System.out.println("Could not create Background " + i + " because of wrong length : tiles=" + tiles.length);
                    }
                }
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.background.io.PngManager.importPng() - Error while parsing graphics data : "+e);
        }         
                
        System.out.println("com.sfc.sf2.background.io.DisassemblyManager.importDisassembly() - Disassembly imported.");
        return backgrounds.toArray(new Background[backgrounds.size()]);
    }
    
    public static Background importSingleBackground(String filepath){
        System.out.println("com.sfc.sf2.background.io.DisassemblyManager.importDisassembly() - Importing disassembly ...");
        Background background = new Background();
        try{
            Tile[] tiles = parseGraphics(filepath);
            if(tiles!=null){
                if(tiles.length==BACKGROUND_TILE_LENGTH){
                   background.setIndex(0);                   
                   background.setTiles(tiles);
                   System.out.println("Created Background with " + tiles.length + " tiles.");                       
                }else{
                    System.out.println("Could not create Background because of wrong length : tiles=" + tiles.length);
                }
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.background.io.PngManager.importPng() - Error while parsing graphics data : "+e);
        }         
                
        System.out.println("com.sfc.sf2.background.io.DisassemblyManager.importDisassembly() - Disassembly imported.");
        return background;
    }
    
    private static Tile[] parseGraphics(String filePath){
        Tile[] tiles = null;
        Palette palette = null;
        try{
            Path path = Paths.get(filePath);
            if(path.toFile().exists()){
                byte[] data = Files.readAllBytes(path);
                if(data.length>2){
                    short tileset1Offset = getNextWord(data,0);
                    short tileset2Offset = (short)(getNextWord(data,2)+2);
                    short paletteOffset = (short)(getNextWord(data,4)+4);
                    byte[] tileset1Data = new byte[data.length-tileset1Offset];
                    System.arraycopy(data, tileset1Offset, tileset1Data, 0, tileset1Data.length);
                    byte[] tileset2Data = new byte[data.length-tileset2Offset];
                    System.arraycopy(data, tileset2Offset, tileset2Data, 0, tileset2Data.length);
                    byte[] paletteData = new byte[32];
                    System.arraycopy(data, paletteOffset, paletteData, 0, paletteData.length);
                    Color[] colors = PaletteDecoder.parsePalette(paletteData);
                    palette = new Palette("Background Palette", colors);
                    Tile[] tileset1 = new StackGraphicsDecoder().decodeStackGraphics(tileset1Data, palette);
                    Tile[] tileset2 = new StackGraphicsDecoder().decodeStackGraphics(tileset2Data, palette);
                    tiles = new Tile[tileset1.length+tileset2.length];
                    System.arraycopy(tileset1, 0, tiles, 0, tileset1.length);
                    System.arraycopy(tileset2, 0, tiles, tileset1.length, tileset2.length);
                    tiles = com.sfc.sf2.graphics.io.DisassemblyManager.reorderTilesSequentially(tiles, 8, 3, 4);
                }else{
                    System.out.println("com.sfc.sf2.background.io.DisassemblyManager.parseGraphics() - File ignored because of too small length (must be a dummy file) " + data.length + " : " + filePath);
                }
            }            
        }catch(Exception e){
             System.err.println("com.sfc.sf2.background.io.DisassemblyManager.parseGraphics() - Error while parsing graphics data : "+e);
             e.printStackTrace();
        } 
        return tiles;
    }
    
    public static void exportDisassembly(Background[] backgrounds, String basepath){
        System.out.println("com.sfc.sf2.background.io.DisassemblyManager.exportDisassembly() - Exporting disassembly ...");
        try {
            for(Background background : backgrounds){
                String index = String.format("%02d", background.getIndex());
                String filePath = basepath + System.getProperty("file.separator") + BASE_FILENAME.replace("XX.bin", index+".bin");
                Tile[] tileset1 = new Tile[192];
                Tile[] tileset2 = new Tile[192];
                Tile[] tiles = background.getTiles();
                tiles = com.sfc.sf2.graphics.io.DisassemblyManager.reorderTilesForDisasssembly(tiles, 8, 3, 4);
                System.arraycopy(tiles,0,tileset1,0,192);
                System.arraycopy(tiles,192,tileset2,0,192);
                StackGraphicsEncoder.produceGraphics(tileset1);
                byte[] newTileset1 = StackGraphicsEncoder.getNewGraphicsFileBytes();
                StackGraphicsEncoder.produceGraphics(tileset2);
                byte[] newTileset2 = StackGraphicsEncoder.getNewGraphicsFileBytes(); 
                byte[] newBackgroundFileBytes = new byte[2+2+2+32+newTileset1.length+newTileset2.length];
                short tileset2Offset = (short) (newTileset1.length + 6 + 32 - 2);
                newBackgroundFileBytes[0] = 0;
                newBackgroundFileBytes[1] = 0x26;
                newBackgroundFileBytes[2] = (byte)((tileset2Offset>>8)&0xFF);
                newBackgroundFileBytes[3] = (byte)(tileset2Offset&0xFF);
                newBackgroundFileBytes[4] = 0;
                newBackgroundFileBytes[5] = 2;
                PaletteEncoder.producePalette(tileset1[0].getPalette().getColors());
                byte[] palette = PaletteEncoder.getNewPaletteFileBytes();
                System.arraycopy(palette, 0, newBackgroundFileBytes, 6, palette.length);
                System.arraycopy(newTileset1, 0, newBackgroundFileBytes, 0x26, newTileset1.length);
                System.arraycopy(newTileset2, 0, newBackgroundFileBytes, 0x26+newTileset1.length, newTileset2.length);
                Path graphicsFilePath = Paths.get(filePath);
                Files.write(graphicsFilePath,newBackgroundFileBytes);
                System.out.println(newBackgroundFileBytes.length + " bytes into " + graphicsFilePath);                
            }
        } catch (Exception ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.out.println(ex);
        }            
        System.out.println("com.sfc.sf2.background.io.DisassemblyManager.exportDisassembly() - Disassembly exported.");        
    }
    
    private static short getNextWord(byte[] data, int cursor){
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(data[cursor+1]);
        bb.put(data[cursor]);
        short s = bb.getShort(0);
        return s;
    }
}
