/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.icon.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.layout.DefaultLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author wiz
 */
public class GifManager {
    
    private static final String CHARACTER_FILENAME = "iconXXX.gif";
    
    public static Tile[] importGif(String basepath){
        System.out.println("com.sfc.sf2.icon.io.GifManager.importGif() - Importing GIF files ...");
        int count = new File(basepath).list().length;
        List<Tile[]> icons = new ArrayList();
        try{
            for(int i=0;i<count;i++){
                String index = String.format("%03d", i);
                String filePath = basepath + CHARACTER_FILENAME.replace("XXX.gif", index+".gif");
                System.out.println("Parsing " + filePath);
                Tile[] iconTiles = loadGifFile(filePath);
                icons.add(iconTiles);
                System.out.println("Parsed " + filePath);
            }
        }catch(IOException e){
            System.out.println("No more icon files to parse.");
        }catch(Exception e){
             System.err.println("com.sfc.sf2.icon.io.GifManager.importGif() - Error while parsing icon data : "+e);
        }        
        Tile[] iconsArray = new Tile[icons.size()*6];
        for(int i=0;i<icons.size();i++){
            System.arraycopy(icons.get(i), 0, iconsArray, i*6, 6);
        }
        System.out.println("com.sfc.sf2.icon.io.GifManager.importGif() - GIF files imported.");
        return iconsArray;                
    }
    
    
    
    public static Tile[] loadGifFile(String filepath) throws IOException{
        System.out.println("com.sfc.sf2.icon.io.GifManager.importGif() - Importing GIF files ...");
        Tile[] tiles = null;
        try{
            Path path = Paths.get(filepath);
            BufferedImage img = ImageIO.read(path.toFile());
            ColorModel cm = img.getColorModel();
            if(!(cm instanceof IndexColorModel)){
                System.out.println("GIF FORMAT ERROR : COLORS ARE NOT INDEXED AS EXPECTED.");
            }else{
                IndexColorModel icm = (IndexColorModel) cm;
                Color[] palette = buildColors(icm);
                Graphics g = img.getGraphics();
                
                int imageWidth = img.getWidth();
                int imageHeight = img.getHeight();
                if(imageWidth%8!=0 || imageHeight%8!=0){
                    System.out.println("GIF FORMAT WARNING : DIMENSIONS ARE NOT MULTIPLES OF 8. (8 pixels per tile)");
                }else{
                    tiles = new Tile[(imageWidth/8)*(imageHeight/8)];
                    int tileId = 0;
                    for(int y=0;y<imageHeight;y+=8){
                        for(int x=0;x<imageWidth;x+=8){
                            System.out.println("Building tile from coordinates "+x+":"+y);
                            Tile tile = new Tile();
                            tile.setId(tileId);
                            tile.setPalette(palette);
                            for(int j=0;j<8;j++){
                                for(int i=0;i<8;i++){
                                    Color color = new Color(img.getRGB(x+i,y+j));
                                    for(int c=0;c<16;c++){
                                        if(color.equals(palette[c])){
                                            tile.setPixel(i, j, c);
                                            break;
                                        }
                                    }
                                }
                            }
                            System.out.println(tile);
                            tiles[tileId] = tile;   
                            tileId++;
                        }
                    }
                }
            }
        }catch(IOException e){
            throw e;
        }catch(Exception e){
             System.err.println("com.sfc.sf2.icon.io.GifManager.importGif() - Error while parsing GIF data : "+e);
        }        
        System.out.println("com.sfc.sf2.icon.io.GifManager.importGif() - GIF files imported.");        
        return tiles;                
    }
    
    private static Color[] buildColors(IndexColorModel icm){
        Color[] colors = new Color[16];
        if(icm.getMapSize()>16){
            System.out.println("com.sfc.sf2.icon.io.GifManager.buildColors() - GIF FORMAT HAS MORE THAN 16 INDEXED COLORS : "+icm.getMapSize());
        }
        byte[] reds = new byte[icm.getMapSize()];
        byte[] greens = new byte[icm.getMapSize()];
        byte[] blues = new byte[icm.getMapSize()];
        icm.getReds(reds);
        icm.getGreens(greens);
        icm.getBlues(blues);
        for(int i=0;i<16;i++){
            colors[i] = new Color((int)(reds[i]&0xff),(int)(greens[i]&0xff),(int)(blues[i]&0xff));
        }
        return colors;
    }
    
    
    public static void exportGif(Tile[] tiles, String basepath){
        try {
            System.out.println("com.sfc.sf2.icon.io.GifManager.exportGif() - Exporting GIF files ...");
            for(int i = 0;i<tiles.length;i+=6){
                String index = String.format("%03d", i/6);
                String filePath = basepath + System.getProperty("file.separator") + CHARACTER_FILENAME.replace("XXX.gif", index+".gif");
                writeGifFile(Arrays.copyOfRange(tiles, i, i+6),filePath);
            }
            System.out.println("com.sfc.sf2.icon.io.GifManager.exportGif() - GIF files exported.");
        } catch (Exception ex) {
            Logger.getLogger(GifManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
    }    
    
    public static void writeGifFile(Tile[] tiles, String filepath){
        try {
            System.out.println("com.sfc.sf2.icon.io.GifManager.exportGif() - Exporting GIF file ...");
            BufferedImage image = DefaultLayout.buildImage(tiles, 2);
            File outputfile = new File(filepath);
            System.out.println("File path : "+outputfile.getAbsolutePath());
            ImageIO.write(image, "gif", outputfile);
            System.out.println("com.sfc.sf2.icon.io.GifManager.exportGif() - GIF file exported.");
        } catch (Exception ex) {
            Logger.getLogger(GifManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
    }
    
}
