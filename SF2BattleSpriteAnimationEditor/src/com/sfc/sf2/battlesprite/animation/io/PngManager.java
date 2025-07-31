/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.layout.DefaultLayout;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimation;
import com.sfc.sf2.battlesprite.animation.layout.BattleSpriteAnimationLayout;
import com.sfc.sf2.palette.graphics.PaletteDecoder;
import com.sfc.sf2.palette.graphics.PaletteEncoder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
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
public class PngManager {
    
    public static Tile[] loadPngFile(String filepath) throws IOException{
        Tile[] tiles = null;
        try{
            Path path = Paths.get(filepath);
            if(path.toFile().exists()){
                BufferedImage img = ImageIO.read(path.toFile());
                ColorModel cm = img.getColorModel();
                if(!(cm instanceof IndexColorModel)){
                    System.out.println("PNG FORMAT ERROR : COLORS ARE NOT INDEXED AS EXPECTED.");
                }else{
                    IndexColorModel icm = (IndexColorModel) cm;
                    Color[] palette = buildColors(icm);

                    int imageWidth = img.getWidth();
                    int imageHeight = img.getHeight();
                    if(imageWidth%8!=0 || imageHeight%8!=0){
                        System.out.println("PNG FORMAT WARNING : DIMENSIONS ARE NOT MULTIPLES OF 8. (8 pixels per tile)");
                    }else{
                        tiles = new Tile[(imageWidth/8)*(imageHeight/8)];
                        int blockColumnsNumber = (imageWidth>96)?4:3;
                        int tileId = 0;
                            for(int blockColumn=0;blockColumn<blockColumnsNumber;blockColumn++){
                                for(int blockLine=0;blockLine<3;blockLine++){
                                    for(int tileColumn=0;tileColumn<4;tileColumn++){
                                        for(int tileLine=0;tileLine<4;tileLine++){
                                            int x = (blockColumn*4+tileColumn)*8;
                                            int y = (blockLine*4+tileLine)*8;
                                            //System.out.println("Building tile from coordinates "+x+":"+y);
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
                                            //System.out.println(tile);
                                            tiles[tileId] = tile;   
                                            tileId++;
                                        }
                                    }
                                }
                            }                 

                    }
                }                
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.battlespriteanimation.io.PngManager.importPng() - Error while parsing PNG data : "+e);
             throw e;
        }                
        return tiles;                
    }
    
    private static Color[] buildColors(IndexColorModel icm){
        Color[] colors = new Color[16];
        if(icm.getMapSize()>16){
            System.out.println("com.sfc.sf2.battlespriteanimation.io.PngManager.buildColors() - PNG FORMAT HAS MORE THAN 16 INDEXED COLORS : "+icm.getMapSize());
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
    
    
    public static void exportPng(BattleSpriteAnimation battlespriteanimation, String filepath, int selectedPalette){
        try {
            System.out.println("com.sfc.sf2.battlespriteanimation.io.PngManager.exportPng() - Exporting PNG files and palettes ...");
/*            Tile[][] frames = battlespriteanimation.getFrames();
            for(int i=0;i<frames.length;i++){
                String framePath = filepath + "-frame-" + String.valueOf(i) + ".png";
                writePngFile(frames[i],battlespriteanimation.getType(),framePath); 
            }
            Color[][] palettes = battlespriteanimation.getPalettes();
            for(int i=0;i<palettes.length;i++){
                String palettePath = filepath + "-palette-" + String.valueOf(i) + ".bin";
                PaletteEncoder.producePalette(palettes[i]);
                byte[] palette = PaletteEncoder.getNewPaletteFileBytes();
                Path graphicsFilePath = Paths.get(palettePath);
                Files.write(graphicsFilePath,palette);
            }
*/                           
            System.out.println("com.sfc.sf2.battlespriteanimation.io.PngManager.exportPng() - PNG files and palettes exported.");
        } catch (Exception ex) {
            Logger.getLogger(PngManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public static void writePngFile(Tile[] tiles, int battlespriteanimationType, String filepath){
        try {
/*            System.out.println("com.sfc.sf2.battlespriteanimation.io.PngManager.exportPng() - Exporting PNG file ...");
            BattleSpriteAnimationLayout battleSpriteAnimationLayout = new BattleSpriteAnimationLayout();
            BufferedImage image = battleSpriteAnimationLayout.buildImage(true);
            File outputfile = new File(filepath);
            System.out.println("File path : "+outputfile.getAbsolutePath());
            ImageIO.write(image, "png", outputfile);
            System.out.println("PNG file exported : " + outputfile.getAbsolutePath());*/
        } catch (Exception ex) {
            Logger.getLogger(PngManager.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
       
    
}
