/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.core.io.RawImageException;
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
    
    protected void checkImageDimensions(WritableRaster raster) throws RawImageException {
        int imageWidth = raster.getWidth();
        int imageHeight = raster.getHeight();
        if (imageWidth%PIXEL_WIDTH != 0 || imageHeight%PIXEL_HEIGHT != 0) {
            throw new RawImageException("Warning : image dimensions are not a multiple of 8 (pixels per tile).");
        }
    }
    
    protected void checkImageDimensions(WritableRaster raster, int tileWidthOfTileset, int tileHeightOfTileset) throws RawImageException {
        checkImageDimensions(raster);
        if (raster.getWidth()%(tileWidthOfTileset*PIXEL_WIDTH) != 0) {
            throw new RawImageException("Warning : image dimensions are wrong size. Width is " + raster.getWidth() + " but expected a multiple of " + (tileWidthOfTileset*PIXEL_WIDTH));
        }
        if (raster.getHeight()%(tileHeightOfTileset*PIXEL_HEIGHT) != 0) {
            throw new RawImageException("Warning : image dimensions are wrong size. Height is " + raster.getHeight() + " but expected a multiple of " + (tileHeightOfTileset*PIXEL_HEIGHT));
        }
    }
    
    protected Tileset parseTileset(WritableRaster raster, Palette palette) {
        return parseTileset(raster, 0, 0, raster.getWidth()/PIXEL_WIDTH, raster.getHeight()/PIXEL_HEIGHT, palette);
    }
    
    protected Tileset[] parseTileset(WritableRaster raster, int tileWidthOfTileset, int tileHeightOfTileset, Palette palette) {
        int tilesPerRow = raster.getWidth()/PIXEL_WIDTH;
        int tilesPerColumn = raster.getHeight()/PIXEL_HEIGHT;
        int tilesetsPerRow = tilesPerRow/tileWidthOfTileset;
        int tilesetsPerColumn = tilesPerColumn/tileHeightOfTileset;
        
        int tilesetCount = (tilesetsPerRow*tilesetsPerColumn);
        Tileset[] tilesets = new Tileset[tilesetCount];
        for (int j = 0; j < tilesetsPerColumn; j++) {
            for (int i = 0; i < tilesetsPerRow; i++) {
                tilesets[i+j*tilesetsPerRow] = parseTileset(raster, i*tileWidthOfTileset, j*tileHeightOfTileset, tileWidthOfTileset, tileHeightOfTileset, palette);
            }
        }
        return tilesets;
    }
    
    protected Tileset parseTileset(WritableRaster raster, int tileX, int tileY, int tilesPerRow, int tilesPerColumn, Palette palette) {
        Tile[] tiles = new Tile[tilesPerRow*tilesPerColumn];
        int tileId = 0;
        //Console.logger().finest("Building tileset from coordinates "+tileX+":"+tileY+":"+(tileX+tilesPerRow)+":"+(tileY+tilesPerColumn));
        for (int t = 0; t < tiles.length; t++) {
            int[] pixels = new int[PIXEL_WIDTH*PIXEL_WIDTH];
            int x = (tileX + t%tilesPerRow)*PIXEL_WIDTH;
            int y = (tileY + t/tilesPerRow)*PIXEL_HEIGHT;
            //Console.logger().finest("Building tile from coordinates "+x+":"+y);
            Tile tile = new Tile();
            tile.setId(tileId);
            tile.setPalette(palette);
            raster.getPixels(x, y, PIXEL_WIDTH, PIXEL_HEIGHT, pixels);
            tile.setPixels(pixels);
            //Console.logger().finest(tile.toString());
            tiles[tileId] = tile;   
            tileId++;
        }
        return new Tileset(null, tiles, tilesPerRow);
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
    
    protected BufferedImage setupImage(Tileset[] tilesets, int tilesetsPerRow) {
        Palette palette = null;
        int tilesetTilesWidth = 1;
        int tilesetTilesheight = 1;
        for (Tileset tileset : tilesets) {
            if (tileset != null) {
                palette = tileset.getPalette();
                tilesetTilesWidth = tileset.getTilesPerRow();
                tilesetTilesheight = (tileset.getTiles().length/tilesetTilesWidth);
                if (tileset.getTiles().length % tilesetTilesWidth != 0) {
                    tilesetTilesheight++;
                }
                break;
            }
        }
        
        int width = tilesetsPerRow*tilesetTilesWidth;
        int height = (tilesets.length/tilesetsPerRow);
        if (tilesets.length % tilesetsPerRow != 0) {
            height++;
        }
        height *= tilesetTilesheight;
        return new BufferedImage(width*PIXEL_WIDTH, height*PIXEL_HEIGHT, BufferedImage.TYPE_BYTE_BINARY, palette.getIcm());
    }
    
    protected void writeTileset(WritableRaster raster, Tileset tileset) {
        writeTileset(raster, tileset, 0, tileset.getTiles().length, 0, 0, tileset.getTilesPerRow());
    }
    
    protected void writeTileset(WritableRaster raster, Tileset[] tilesets, int tilesetsPerRow) {
        int tilesetTilesWidth = 1;
        int tilesetTilesheight = 1;
        for (Tileset tileset : tilesets) {
            if (tileset != null) {
                tilesetTilesWidth = tileset.getTilesPerRow();
                tilesetTilesheight = (tileset.getTiles().length/tilesetTilesWidth);
                if (tileset.getTiles().length % tilesetTilesWidth != 0) {
                    tilesetTilesheight++;
                }
                break;
            }
        }
        
        for (int i = 0; i < tilesets.length; i++) {
            if (tilesets[i] != null) {
                int x = (i % tilesetsPerRow)*tilesetTilesWidth;
                int y = (i/tilesetsPerRow)*tilesetTilesheight;
                writeTileset(raster, tilesets[i], 0, tilesets[i].getTiles().length, x, y, tilesets[i].getTilesPerRow());
            }
        }
    }
    
    protected void writeTileset(WritableRaster raster, Tileset tileset, int tileStart, int tileEnd, int tileX, int tileY, int tilesPerRow) {
        Tile[] tiles = tileset.getTiles();
        for (int t = tileStart; t < tileEnd; t++) {
            if (tiles[t] != null) {
                int x = (tileX + t%tilesPerRow)*PIXEL_WIDTH;
                int y = (tileY + t/tilesPerRow)*PIXEL_HEIGHT;
                raster.setPixels(x, y, PIXEL_WIDTH, PIXEL_HEIGHT, tiles[t].getPixels());
            }
        }
    }
}
