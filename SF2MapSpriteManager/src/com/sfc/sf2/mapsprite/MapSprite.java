/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite;

import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.palette.Palette;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;

/**
 *
 * @author wiz
 */
public class MapSprite {
    
    private int index;
    
    private Tile[] tiles;
    private BufferedImage indexedColorImage = null;
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }
    
    public Palette getPalette() {
        if (tiles == null || tiles.length == 0) {
            return null;
        }
        return tiles[0].getPalette();
    }
    
    public BufferedImage getIndexedColorImage() {
        if (tiles == null || tiles.length == 0) {
            return null;
        }
        if (indexedColorImage == null) {
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
    
    public void clearIndexedColorImage() {
        indexedColorImage = null;
        for (int i = 0; i < tiles.length; i++) {
            tiles[i].clearIndexedColorImage();
        }
    }
}
