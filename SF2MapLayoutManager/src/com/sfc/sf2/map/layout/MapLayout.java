/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.layout;

import com.sfc.sf2.graphics.Tileset;
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
    private MapBlockset blockset;
    
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

    public MapBlockset getBlockset() {
        return blockset;
    }

    public void setBlockset(MapBlockset blockset) {
        this.blockset = blockset;
    }

    public Palette getPalette() {
        if (blockset == null) {
            return null;
        } else {
            return blockset.getPalette();
        }
    }
}
