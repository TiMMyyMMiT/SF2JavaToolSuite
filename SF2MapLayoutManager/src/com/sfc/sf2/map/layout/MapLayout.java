/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.layout;

import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.palette.Palette;

/**
 *
 * @author wiz
 */
public class MapLayout {
    
    public static final int BLOCK_WIDTH = 64;
    public static final int BLOCK_HEIGHT = 64;
    public static final int BLOCK_COUNT = BLOCK_WIDTH*BLOCK_HEIGHT;
    
    private int index;
     
    private Tileset[] tilesets;
    private MapLayoutBlockset blockset;

    public MapLayout(int index, Tileset[] tilesets, MapLayoutBlockset blockset) {
        this.index = index;
        this.tilesets = tilesets;
        this.blockset = blockset;
    }
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Tileset[] getTilesets() {
        return tilesets;
    }

    public void setTilesets(Tileset[] tilesets) {
        this.tilesets = tilesets;
    }

    public MapLayoutBlockset getBlockset() {
        return blockset;
    }

    public void setBlockset(MapLayoutBlockset blockset) {
        this.blockset = blockset;
    }
    
    public MapBlockset layoutToBlockset() {
        MapBlock[] blocks = new MapBlock[BLOCK_COUNT];
        for (int i = 0; i < BLOCK_COUNT; i++) {
            blocks[i] = blockset.getBlocks()[i].getMapBlock();
        }
        return new MapBlockset(blocks, BLOCK_WIDTH);
    }

    public Palette getPalette() {
        if (tilesets == null) return null;
        for (int i = 0; i < tilesets.length; i++) {
            if (tilesets[i] != null) {
                return tilesets[i].getPalette();
            }
        }
        return null;
    }
}
