/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.layout;

import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_COUNT;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_HEIGHT;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_WIDTH;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author TiMMy
 */
public class MapLayoutBlockset {
    
    protected MapLayoutBlock[] blocks;
    
    private BufferedImage indexedColorImage = null;
    
    public MapLayoutBlockset(MapLayoutBlock[] blocks) {
        this.blocks = blocks;
    }
    
    public MapLayoutBlock[] getBlocks() {
        return blocks;
    }
    
    public void setBlocks(MapLayoutBlock[] blocks) {
        this.blocks = blocks;
        clearIndexedColorImage(false);
    }
    
    public BufferedImage getIndexedColorImage(Tileset[] tilesets) {
        if (blocks == null || blocks.length == 0) {
            return null;
        }
        if (indexedColorImage == null) {
            indexedColorImage = new BufferedImage(BLOCK_WIDTH*PIXEL_WIDTH, BLOCK_HEIGHT*PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = indexedColorImage.getGraphics();
            for(int j=0;j<BLOCK_HEIGHT;j++){
                for(int i=0;i<BLOCK_WIDTH;i++){
                    int tileID = i+j*BLOCK_WIDTH;
                    if (blocks[tileID] != null && blocks[tileID].getMapBlock() != null) {
                        graphics.drawImage(blocks[tileID].getMapBlock().getIndexedColorImage(tilesets), i*PIXEL_WIDTH, j*PIXEL_HEIGHT, null);
                    }
                }
            }
            graphics.dispose();
        }
        return indexedColorImage;
    }
    
    public void clearIndexedColorImage(boolean alsoClearBlocks) {
        if (this.indexedColorImage != null) {
            indexedColorImage.flush();
            indexedColorImage = null;
        }
        
        if (alsoClearBlocks) {
            for (int i = 0; i < blocks.length; i++) {
                blocks[i].getMapBlock().clearIndexedColorImage();
            }
        }
    }
    
    public void insertBlock(int index, MapLayoutBlock block) {
        if (index < 0 || index > blocks.length) return;
        MapLayoutBlock[] newBlocks = new MapLayoutBlock[blocks.length+1];
        System.arraycopy(blocks, 0, newBlocks, 0, index);
        newBlocks[index] = block.clone();
        for (int i = index+1; i < newBlocks.length; i++) {
            newBlocks[i] = blocks[i-1];
        }
        setBlocks(newBlocks);
    }
    
    public void removeBlock(int index) {
        if (index < 0 || index >= blocks.length) return;
        MapLayoutBlock[] newBlocks = new MapLayoutBlock[blocks.length-1];
        System.arraycopy(blocks, 0, newBlocks, 0, index);
        for (int i = index; i < newBlocks.length; i++) {
            newBlocks[i] = blocks[i+1];
        }
        setBlocks(newBlocks);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return this == null;
        if (obj == this) return true;
        if (!(obj instanceof MapLayoutBlockset)) return false;
        MapLayoutBlockset blockset = (MapLayoutBlockset)obj;
        for (int i=0; i < this.blocks.length; i++) {
            if (!this.blocks[i].equals(blockset.getBlocks()[i])) {
                return false;
            }
        }
        return true;
    }
    
    public MapLayoutBlockset clone() {
        return new MapLayoutBlockset(this.blocks.clone());
    }
    
    public static MapLayoutBlockset EmptyMapLayoutBlockset() {
        MapLayoutBlock emptyBlock = MapLayoutBlock.EmptyMapLayoutBlock();
        MapLayoutBlock[] blocks = new MapLayoutBlock[BLOCK_COUNT];
        for (int i=0; i < blocks.length; i++) {
            blocks[i] = emptyBlock;
        }
        return new MapLayoutBlockset(blocks);
    }
}
