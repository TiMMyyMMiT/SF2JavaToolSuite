/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite;

import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author wiz
 */
public class MapSprite {
    
    private int index;
    private int facingIndex;
    private int frameIndex;
    private Tileset tileset;
    
    private BufferedImage indexedColorImage = null;
        
    public MapSprite(Tileset tileset, int index, int facingIndex, int frameIndex) {
        this.index = index;
        this.facingIndex = facingIndex;
        this.frameIndex = frameIndex;
        this.tileset = tileset;
    }
    
    public MapSprite(Tileset tileset, int[] indices) {
        this.index = indices.length > 0 ? indices[0] : -1;
        this.facingIndex = indices.length > 1 ? indices[1] : -1;
        this.frameIndex = indices.length > 2 ? indices[2] : -1;
        this.tileset = tileset;
    }
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public int getFacingIndex() {
        return facingIndex;
    }

    public void setFacingIndex(int facingIndex) {
        this.facingIndex = facingIndex;
    }
    
    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }

    public Tileset getTileset() {
        return tileset;
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
    }
    
    public Palette getPalette() {
        if (tileset == null) {
            return null;
        }
        return tileset.getPalette();
    }
    
    public BufferedImage getIndexedColorImage() {
        if (tileset == null || tileset.getTiles().length == 0) {
            return null;
        }
        if (indexedColorImage == null) {
            Tile[] tiles = tileset.getTiles();
            int height = 3;
            int width = tiles.length/height;
            if ((tiles.length%height) != 0)
                width++;
            indexedColorImage = new BufferedImage(width*PIXEL_WIDTH, height*PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = indexedColorImage.getGraphics();
            for (int i = 0; i < tiles.length; i++) {
                graphics.drawImage(tiles[i].getIndexedColorImage(), (i/height)*PIXEL_WIDTH, (i%height)*PIXEL_HEIGHT, null);
            }
            graphics.dispose();
        }
        return indexedColorImage;
    }
    
    public void clearIndexedColorImage(boolean alsoClearTiles) {
        indexedColorImage = null;
        tileset.clearIndexedColorImage(alsoClearTiles);
    }
}
