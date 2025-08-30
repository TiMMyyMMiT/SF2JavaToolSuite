/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.graphics;

import com.sfc.sf2.palette.Palette;
import java.awt.Dimension;

/**
 *
 * @author TiMMy
 */
public class Block extends Tileset {
    public static final int TILE_WIDTH = 3;
    public static final int TILE_HEIGHT = 3;
    public static final int TILES_COUNT = TILE_WIDTH*TILE_HEIGHT;
    public static final int PIXEL_WIDTH = TILE_WIDTH*Tile.PIXEL_WIDTH;
    public static final int PIXEL_HEIGHT = TILE_HEIGHT*Tile.PIXEL_HEIGHT;
    
    private int index;
    private int blocksPerRow;
    
    public Block(int index, Tile[] tiles, int blocksPerRow) {
        super(String.format("Block-%02d", index), tiles, blocksPerRow*TILE_WIDTH);
        this.index = index;
        this.blocksPerRow = blocksPerRow;
    }
    
    public Block(int index, Tileset tileset) {
        super(tileset.getName(), tileset.getTiles(), tileset.tilesPerRow);
        this.index = index;
        blocksPerRow = tileset.tilesPerRow/TILE_WIDTH;
    }
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public int getBlocksPerRow() {
        return blocksPerRow;
    }

    public void setBlocksPerRow(int blocksPerRow) {
        setTilesPerRow(blocksPerRow*TILE_WIDTH);
        this.blocksPerRow = blocksPerRow;
    }  
    
    @Override
    public Dimension getDimensions(int blocksPerRow) {
        this.setBlocksPerRow(blocksPerRow);
        return getDimensions();
    }
    
    @Override 
    public Block clone() {
        return new Block(index, tiles.clone(), blocksPerRow);
    }
    
    public static Block EmptyBlock(int index, Palette palette, int blocksPerRow) {
        return new Block(index, EmptyTilset(palette, blocksPerRow*TILE_WIDTH));
    }
}
