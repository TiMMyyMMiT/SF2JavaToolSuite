/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block;

import com.sfc.sf2.graphics.Block;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author wiz
 */
public class MapBlock {
    public static final int TILES_COUNT = Block.TILES_COUNT;
    public static final int MAP_FLAG_MASK_EVENTS = 0x3C00;
    public static final int MAP_FLAG_MASK_NAV = 0xC000;
    
    public static final int MAP_FLAG_STEP = 0x0400;
    public static final int MAP_FLAG_SHOW = 0x0800;
    public static final int MAP_FLAG_HIDE = 0x0C00;
    public static final int MAP_FLAG_WARP = 0x1000;    
    public static final int MAP_FLAG_TRIGGER = 0x1400;
    public static final int MAP_FLAG_CHEST = 0x1800;
    public static final int MAP_FLAG_SEARCH = 0x1C00;
    public static final int MAP_FLAG_LAYER_UP = 0x2000;
    public static final int MAP_FLAG_LAYER_DOWN = 0x2400;
    public static final int MAP_FLAG_TABLE = 0x2800;
    public static final int MAP_FLAG_VASE = 0x2C00;
    public static final int MAP_FLAG_BARREL = 0x3000;
    public static final int MAP_FLAG_SHELF = 0x3400;
    public static final int MAP_FLAG_CARAVAN = 0x3800;
    public static final int MAP_FLAG_RAFT = 0x3C00;
    public static final int MAP_FLAG_STAIRS_RIGHT = 0x4000;
    public static final int MAP_FLAG_STAIRS_LEFT = 0x8000;
    public static final int MAP_FLAG_OBSTRUCTED = 0xC000;
       
    private int index;
    private int flags;
    private MapTile[] mapTiles;
    
    private BufferedImage indexedColorImage = null;
    
    public MapBlock(int index, int flags) {
        this.index = index;
        this.flags = flags;
        
        MapTile[] mapTiles = new MapTile[TILES_COUNT];
        for (int i = 0; i < TILES_COUNT; i++) {
            mapTiles[i] = MapTile.EmptyMapTile();
        }
        setMapTiles(mapTiles);
    }
    
    public MapBlock(int index, int flags, MapTile[] mapTiles) {
        this.index = index;
        this.flags = flags;
        setMapTiles(mapTiles);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getFlags() {
        return flags;
    }

    public int getNavFlags() {
        return flags & MAP_FLAG_MASK_NAV;
    }

    public int getEventFlags() {
        return flags & MAP_FLAG_MASK_EVENTS;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public MapTile[] getMapTiles() {
        return mapTiles;
    }

    public void setMapTiles(MapTile[] mapTiles) {
        if (mapTiles == null || mapTiles.length != TILES_COUNT) {
            throw new IndexOutOfBoundsException("MapBlock mapTiles must be exactly" + TILES_COUNT + " in size.");
        }
        this.mapTiles = mapTiles;
    }
    
    public BufferedImage getIndexedColorImage(Tileset[] tilesets) {
        if (indexedColorImage == null) {
            indexedColorImage = new BufferedImage(Block.TILE_WIDTH*PIXEL_WIDTH, Block.TILE_HEIGHT*PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = indexedColorImage.getGraphics();
            for (int i=0; i < TILES_COUNT; i++) {
                graphics.drawImage(mapTiles[i].getIndexedColorImage(tilesets), (i%Block.TILE_WIDTH)*PIXEL_WIDTH, (i/Block.TILE_WIDTH)*PIXEL_HEIGHT, null);
            }
            graphics.dispose();
        }
        return indexedColorImage;
    }
    
    public void clearIndexedColorImage() {
        if (this.indexedColorImage != null) {
            indexedColorImage.flush();
            indexedColorImage = null;
        }
    }
    
    public boolean isEmpty() {
        for (int i = 0; i < mapTiles.length; i++) {
            if (mapTiles[i].getTilesetIndex()!= -1) return false;
            if (mapTiles[i].getTileIndex() != -1) return false;
        }
        return true;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!equalsIgnoreTiles(obj)) return false;
        if (obj == null) return false;
        MapBlock block = (MapBlock)obj;
        return this.mapTiles.equals(block.mapTiles);
    }
    
    public boolean equalsIgnoreTiles(Object obj) {
        if (obj == null) return this == null;
        if (obj == this) return true;
        if (!(obj instanceof MapBlock)) return false;
        MapBlock block = (MapBlock)obj;
        if (this.index == block.getIndex() && this.flags == block.getFlags()) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override 
    public MapBlock clone() {
        MapBlock clone = new MapBlock(index, flags, mapTiles);
        return clone;
    }
    
    public static MapBlock EmptyMapBlock(int index, int flags) {
        return new MapBlock(index, flags);
    }
}
