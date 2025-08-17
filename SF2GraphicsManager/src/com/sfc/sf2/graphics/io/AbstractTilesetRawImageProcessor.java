/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author TiMMy
 */
public abstract class AbstractTilesetRawImageProcessor<TType extends Object, TPackage extends Object> extends AbstractRawImageProcessor<TType, TPackage> {
    
    protected void checkImageDimensions(WritableRaster raster) {
        int imageWidth = raster.getWidth();
        int imageHeight = raster.getHeight();
        if (imageWidth%PIXEL_WIDTH != 0 || imageHeight%PIXEL_HEIGHT != 0) {
            Console.logger().warning("Warning : image dimensions are not a multiple of 8 (pixels per tile). Some data may be lost");
        }
    }
    
    protected Tileset parseTileset(WritableRaster raster, Palette palette) {
        return parseTileset(raster, 0, 0, raster.getWidth()/PIXEL_WIDTH, raster.getHeight()/PIXEL_HEIGHT, palette);
    }
    
    protected Tileset[] parseTileset(WritableRaster raster, int tilesetHeight, Palette palette) {
        int tilesetCount = raster.getHeight()/PIXEL_HEIGHT/tilesetHeight;
        Tileset[] tilesets = new Tileset[tilesetCount];
        for (int i = 0; i < tilesetCount; i++) {
            tilesets[i] = parseTileset(raster, 0, tilesetHeight*i, raster.getWidth()/PIXEL_WIDTH, raster.getHeight()/PIXEL_HEIGHT, palette);
        }
        return tilesets;
    }
    
    protected Tileset parseTileset(WritableRaster raster, int tileX, int tileY, int tileW, int tileH, Palette palette) {
        Tile[] tiles = new Tile[tileW*tileH];
        int tileId = 0;
        for(int t = 0; t < tiles.length; t++) {
            int[] pixels = new int[PIXEL_WIDTH*PIXEL_WIDTH];
            int x = tileX + (t%tileW)*PIXEL_WIDTH;
            int y = tileY + (t/tileW)*PIXEL_HEIGHT;
            //onsole.logger().finest("Building tile from coordinates "+x+":"+y);
            Tile tile = new Tile();
            tile.setId(tileId);
            tile.setPalette(palette);
            raster.getPixels(x, y, PIXEL_WIDTH, PIXEL_HEIGHT, pixels);
            tile.setPixels(pixels);
            //Console.logger().finest(tile.toString());
            tiles[tileId] = tile;   
            tileId++;
        }
        return new Tileset(null, tiles, tileW);
    }
    
    protected BufferedImage setupImage(Tileset tileset) {
        return setupImage(tileset.getTiles().length, tileset.getTilesPerRow(), tileset.getPalette().getIcm());
    }
    
    protected BufferedImage setupImage(int tilesCount, int tilesPerRow, IndexColorModel icm) {
        int imageWidth = tilesPerRow*PIXEL_WIDTH;
        int imageHeight = tilesCount/tilesPerRow*PIXEL_HEIGHT;
        if (tilesCount % tilesPerRow != 0) {
            imageHeight += PIXEL_HEIGHT;
        }
        return new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_BINARY, icm);
    }
    
    protected BufferedImage setupImage(Tileset[] tilesets) {
        int tilesPerRow = tilesets[0].getTilesPerRow();
        int height = 0;
        for (int i = 0; i < tilesets.length; i++) {
            int h = tilesets[i].getTiles().length/tilesPerRow;
            if (tilesets[i].getTiles().length % tilesPerRow != 0) {
                h++;
            }
            height += h;
        }
        return new BufferedImage(tilesPerRow*PIXEL_WIDTH, height*PIXEL_HEIGHT, BufferedImage.TYPE_BYTE_BINARY, tilesets[0].getPalette().getIcm());
    }
    
    protected void writeTileset(WritableRaster raster, Tileset tileset) {
        writeTileset(raster, tileset, 0, tileset.getTiles().length, 0, 0, tileset.getTilesPerRow());
    }
    
    protected void writeTileset(WritableRaster raster, Tileset[] tilesets) {
        int tilesPerRow = tilesets[0].getTilesPerRow();
        int height = 0;
        for (int i = 0; i < tilesets.length; i++) {
            height = tilesets[i].getTiles().length/tilesPerRow;
            if (tilesets[i].getTiles().length % tilesPerRow != 0) {
                height++;
            }
            writeTileset(raster, tilesets[i], 0, tilesets[i].getTiles().length, 0, height*PIXEL_HEIGHT, tilesets[i].getTilesPerRow());
        }
    }
    
    protected void writeTileset(WritableRaster raster, Tileset tileset, int tileStart, int tileEnd, int tileX, int tileY, int tilesPerRow) {
        Tile[] tiles = tileset.getTiles();
        for (int t = tileStart; t < tileEnd; t++) {
            if (tiles[t] != null) {
                int x = tileX + t%tilesPerRow*PIXEL_WIDTH;
                int y = tileY + t/tilesPerRow*PIXEL_HEIGHT;
                raster.setPixels(x, y, PIXEL_WIDTH, PIXEL_HEIGHT, tiles[t].getPixels());
            }
        }
    }
}
