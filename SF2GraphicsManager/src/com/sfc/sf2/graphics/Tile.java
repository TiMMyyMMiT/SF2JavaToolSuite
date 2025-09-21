/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics;

import com.sfc.sf2.palette.Palette;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;

/**
 *
 * @author wiz
 */
public class Tile {
    
    public static final int PIXEL_WIDTH = 8;
    public static final int PIXEL_HEIGHT = 8;
    public static final int PIXEL_COUNT = PIXEL_WIDTH*PIXEL_HEIGHT;
    
    private int id;
    protected byte[] pixels = new byte[PIXEL_COUNT];
    private Palette palette;
    
    private final BufferedImage[] indexedColorImages = new BufferedImage[4];

    public Tile(int id, byte[] pixels, Palette palette) {
        this.id = id;
        this.pixels = pixels;
        this.palette = palette;
    }    
        
    public byte[] getPixels() {
        return pixels;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
        clearIndexedColorImage();
    }

    public IndexColorModel getIcm() {
        if (palette == null) {
            return null;
        } else {
            return palette.getIcm();
        }
    }
    
    public BufferedImage getIndexedColorImage() {
        return getIndexedColorImage(TileFlags.None);
    }

    public BufferedImage getIndexedColorImage(TileFlags tileFlags) {
        BufferedImage image = indexedColorImages[tileFlags.value()];
        if (image == null) {
            IndexColorModel icm = palette.getIcm();
            image = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_BYTE_INDEXED, icm);
            indexedColorImages[tileFlags.value()] = image;
            byte[] data = ((DataBufferByte)(image.getRaster().getDataBuffer())).getData();
            
            if (tileFlags.value() == 0) {
                for (int j=0; j < PIXEL_HEIGHT; j++) {
                    for (int i=0; i < PIXEL_WIDTH; i++) {
                        int index = i + j*PIXEL_WIDTH;
                        data[i+j*PIXEL_WIDTH] = pixels[index];
                    }
                }
            } else if (tileFlags.isHFlip()) {
                for (int j=0; j < PIXEL_HEIGHT; j++) {
                    for (int i=0; i < PIXEL_WIDTH; i++) {
                        int index = i + (PIXEL_HEIGHT-1-j)*PIXEL_WIDTH;
                        data[i+j*PIXEL_WIDTH] = pixels[index];
                    }
                }
            } else if (tileFlags.isVFlip()) {
                for (int j=0; j < PIXEL_HEIGHT; j++) {
                    for (int i=0; i < PIXEL_WIDTH; i++) {
                        int index = (PIXEL_WIDTH-1-i) + (PIXEL_HEIGHT-1-j)*PIXEL_WIDTH;
                        data[i+j*PIXEL_WIDTH] = pixels[index];
                    }
                }
            } else if (tileFlags.isBothFlip()) {
                for (int j=0; j < PIXEL_HEIGHT; j++) {
                    for (int i=0; i < PIXEL_WIDTH; i++) {
                        int index = (PIXEL_WIDTH-1-i) + j*PIXEL_WIDTH;
                        data[i+j*PIXEL_WIDTH] = pixels[index];
                    }
                }
            }
        }
        return image;        
    }
    
    public void clearIndexedColorImage() {
        for (int i = 0; i < indexedColorImages.length; i++) {
            if (indexedColorImages[i] != null) {
                indexedColorImages[i].flush();
                indexedColorImages[i] = null;
            }
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tile " + id +" :\n");
        for(int y=0;y<PIXEL_HEIGHT;y++){
            for(int x=0;x<PIXEL_WIDTH;x++){
                sb.append("\t"+pixels[x+y*PIXEL_HEIGHT]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Tile)) return false;
        Tile tile = (Tile)obj;
        if (tile.id != this.id) {
            return false;
        }
        for (int i=0; i < pixels.length; i++) {
            if (this.pixels[i] != tile.pixels[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static Tile paletteSwap(Tile tile, Palette palette) {
        return tile.clone(palette);
    }
    
    @Override
    public Tile clone() {
        return clone(getPalette());
    }
    
    public Tile clone(Palette newPalette) {
        return new Tile(id, pixels, newPalette);
    }
    
    public boolean isTileEmpty() {
        if (pixels == null) {
            return true;
        }
        for (int i=0; i < pixels.length; i++) {
            if (pixels[i] > 0) {
                return false;
            }
        }
        return true;
    }
    
    public static Tile EmptyTile(Palette palette) {
        return new Tile(-1, new byte[PIXEL_COUNT], palette);
    }
}
