/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.vwfont;

import com.sfc.sf2.palette.Palette;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 *
 * @author TiMMy
 */
public class FontSymbol {
    
    public static final int PIXEL_WIDTH = 16;
    public static final int PIXEL_HEIGHT = 16;
    private static final Palette defaultPalette = new Palette(new Color[] { new Color(0xFFF0), Color.BLACK, Color.LIGHT_GRAY });
    
    private int id;
    private int width;
    private int[][] pixels = new int[PIXEL_WIDTH][PIXEL_HEIGHT];
    private BufferedImage image = null;

    public int[][] getPixels() {
        return pixels;
    }

    public void setPixels(int[][] pixels) {
        this.pixels = pixels;
    }
    
    public Palette getPalette() {
        return defaultPalette;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public BufferedImage getIndexColoredImage() {
        if(image == null) {
            image = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_BYTE_INDEXED, defaultPalette.getIcm());
            byte[] data = ((DataBufferByte)(image.getRaster().getDataBuffer())).getData();
            for (int j = 0; j < PIXEL_HEIGHT; j++) {
                for (int i = 0; i < PIXEL_WIDTH; i++) {
                    data[i + j*PIXEL_HEIGHT] = (byte)pixels[i][j];
                }
            }
        }
        return image;        
    }
    
    public void clearImage() {
        image = null;
    }
    
    public static FontSymbol EmptySymbol() {
        FontSymbol emptySymbol = new FontSymbol();
        return emptySymbol;
    }
}
