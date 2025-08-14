/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.vwfont.io;

import com.sfc.sf2.vwfont.FontSymbol;
import static com.sfc.sf2.vwfont.FontSymbol.PIXEL_HEIGHT;
import static com.sfc.sf2.vwfont.FontSymbol.PIXEL_WIDTH;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author wiz
 */
public class PngManager {
    
    private static final String CHARACTER_FILENAME = "symbolXX.png";
    
    public static FontSymbol[] importPng(String basepath){
        System.out.println("com.sfc.sf2.vwfont.io.PngManager.importPng() - Importing PNG files ...");
        FontSymbol[] symbols = null;
        List<FontSymbol> symbolsList = new ArrayList();
        try{
            int count = 0;
            int index = 0;
            File directory = new File(basepath);
            File[] files = directory.listFiles();
            for (File f : files) { 
                if (f.getName().startsWith("symbol") && f.getName().endsWith(".png")) {
                    BufferedImage img = ImageIO.read(f);
                    if (img.getWidth() < PIXEL_WIDTH || img.getHeight() < PIXEL_HEIGHT) {
                        throw new IOException(String.format("Image dimensions are wrong. Image must be %d x %d", PIXEL_WIDTH, PIXEL_HEIGHT));
                    }
                    int symbolWidth = 0;
                    int[] symbolPixels = new int[PIXEL_WIDTH*PIXEL_HEIGHT];
                    int[] pixels = new int[PIXEL_WIDTH*PIXEL_HEIGHT];
                    WritableRaster raster = img.getRaster();
                    raster.getPixels(0, 0, PIXEL_WIDTH, PIXEL_HEIGHT, pixels);
                    for (int i = 0; i < pixels.length; i++) {
                        if (pixels[i] == 2) {   //Width marker
                            symbolWidth = i%PIXEL_WIDTH;
                            symbolPixels[i] = 0;
                        } else {
                            symbolPixels[i] = pixels[i];
                        }
                    }
                    index = count;
                    String fileNumber = f.getName().replaceAll("[^0-9]", "");
                    if (fileNumber.length() > 0)
                        index = Integer.parseInt(fileNumber);
                    FontSymbol symbol = new FontSymbol();
                    symbol.setId(index);
                    symbol.setPixels(symbolPixels);
                    symbol.setWidth(symbolWidth);
                    symbolsList.add(symbol);
                    count++;
                }
            }
            System.out.println("Loaded Font Symbols : " + symbolsList.size());
            symbols = new FontSymbol[symbolsList.size()];
            symbols = symbolsList.toArray(symbols);
        }catch(IOException e){
            System.out.println("No more character files to parse.");
        }catch(Exception e){
             System.err.println("com.sfc.sf2.text.io.DisassemblyManager.parseTextbank() - Error while parsing character data : "+e);
        }        
        System.out.println("com.sfc.sf2.vwfont.io.PngManager.importPng() - PNG files imported.");        
        return symbols;
    }
    
    public static void exportPng(FontSymbol[] symbols, String filepath) {
        try {
            System.out.println("com.sfc.sf2.vwfont.io.PngManager.exportPng() - Exporting PNG files ...");
            for(int s = 0; s<symbols.length; s++){
                String index = String.format("%03d", s);
                
                BufferedImage image = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_BYTE_BINARY, symbols[s].getPalette().getIcm());
                WritableRaster raster = image.getRaster();

                int[] data = symbols[s].getPixels();
                data[symbols[s].getWidth()] = 2;
                raster.setPixels(0, 0, PIXEL_WIDTH, PIXEL_HEIGHT, data);
                File outputfile = new File(filepath + System.getProperty("file.separator") + CHARACTER_FILENAME.replace("XX.png", index+".png"));
                ImageIO.write(image, "png", outputfile);
            }
            System.out.println("com.sfc.sf2.vwfont.io.PngManager.exportPng() - PNG files exported.");
        } catch (Exception ex) {
            Logger.getLogger(PngManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
