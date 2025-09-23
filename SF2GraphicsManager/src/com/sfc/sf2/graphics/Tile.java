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
    protected int[] pixels = new int[PIXEL_COUNT];
    private Palette palette;
    private boolean highPriority = false;
    private boolean hFlip = false;
    private boolean vFlip = false;
    
    private BufferedImage indexedColorImage = null;

    public Tile(int id, int[] pixels, Palette palette) {
        this(id, pixels, palette, false, false, false);
    }
    
    public Tile(int id, int[] pixels, Palette palette, boolean highPriority, boolean hFlip, boolean vFlip) {
        this.id = id;
        this.pixels = pixels;
        this.palette = palette;
        this.highPriority = highPriority;
        this.hFlip = hFlip;
        this.vFlip = vFlip;
    }    
        
    public int[] getPixels() {
        return pixels;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public boolean isHighPriority() {
        return highPriority;
    }

    public void setHighPriority(boolean highPriority) {
        this.highPriority = highPriority;
    }

    public boolean ishFlip() {
        return hFlip;
    }

    public void sethFlip(boolean hFlip) {
        if (this.hFlip != hFlip) {
            this.hFlip = hFlip;
            clearIndexedColorImage();
            pixels = flipPixels(pixels, true, false);
        }
    }

    public boolean isvFlip() {
        return vFlip;
    }

    public void setvFlip(boolean vFlip) {
        if (this.vFlip != vFlip) {
            this.vFlip = vFlip;
            clearIndexedColorImage();
            pixels = flipPixels(pixels, false, true);
        }
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
        if (indexedColorImage == null) {
            IndexColorModel icm = palette.getIcm();
            indexedColorImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_BYTE_INDEXED, icm);
            byte[] data = ((DataBufferByte)(indexedColorImage.getRaster().getDataBuffer())).getData();
            for (int i=0; i < pixels.length; i++) {
                data[i] = (byte)(pixels[i]);
            }
        }
        return indexedColorImage;        
    }
    
    public void clearIndexedColorImage() {
        if (indexedColorImage != null) {
            indexedColorImage.flush();
            indexedColorImage = null;
        }
    }
    
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
        if (obj == null) return this == null;
        if (obj == this) return true;
        if (!(obj instanceof Tile)) return false;
        Tile tile = (Tile)obj;
        if (tile.id != this.id || tile.vFlip != this.vFlip || tile.hFlip != this.hFlip) {
            return false;
        }
        for (int i=0; i < pixels.length; i++) {
            if (this.pixels[i] != tile.pixels[i]) {
                return false;
            }
        }
        return true;
    }
    
    public boolean equalsWithPriority(Object obj) {
        boolean equals = equals(obj);
        if (equals) {
            Tile tile = (Tile)obj;
            equals = this.highPriority == tile.isHighPriority();
        }
        return equals;
    }
    
    public static int[] flipPixels(int[] pixels, boolean hflip, boolean vflip) {
        if (!hflip && !vflip) return pixels;
        int flippedPixels[] = new int[PIXEL_COUNT];
        for (int i=0; i < pixels.length; i++) {
            int x = i%PIXEL_WIDTH;
            if (hflip) x = PIXEL_WIDTH-x-1;
            int y = i/PIXEL_WIDTH;
            if (vflip) y = PIXEL_HEIGHT-y-1;
            flippedPixels[i] = pixels[x+y*PIXEL_WIDTH];
        }
        return flippedPixels;
    }
    
    public static Tile vFlip(Tile tile) {
        int[] pixels = flipPixels(tile.getPixels(), false, true);
        return new Tile(tile.id, pixels, tile.palette, tile.highPriority, tile.hFlip, !tile.vFlip);
    }
    
    public static Tile hFlip(Tile tile) {
        int[] pixels = flipPixels(tile.getPixels(), true, false);
        return new Tile(tile.id, pixels, tile.palette, tile.highPriority, !tile.hFlip, tile.vFlip);
    }
    
    public static Tile clearFlip(Tile tile) {
        if (!tile.hFlip && !tile.vFlip) return tile;
        int[] pixels = flipPixels(tile.getPixels(), tile.hFlip, tile.vFlip);
        return new Tile(tile.id, pixels, tile.palette, tile.highPriority, false, false);
    }
    
    public static Tile paletteSwap(Tile tile, Palette palette) {
        return tile.clone(tile.isHighPriority(), tile.ishFlip(), tile.isvFlip(), palette);
    }
    
    @Override
    public Tile clone() {
        return clone(isHighPriority(), ishFlip(), isvFlip(), getPalette());
    }
    
    public Tile clone(boolean priority, boolean hFlip, boolean vFlip, Palette newPalette) {
        return new Tile(id, pixels, newPalette, highPriority, hFlip, vFlip);
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
        return new Tile(-1, new int[PIXEL_COUNT], palette);
    }
}
