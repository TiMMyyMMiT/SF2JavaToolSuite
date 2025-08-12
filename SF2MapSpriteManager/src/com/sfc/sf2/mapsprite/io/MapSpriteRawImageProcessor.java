/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
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
public class MapSpriteRawImageProcessor extends AbstractRawImageProcessor<Tileset[], MapSpritePackage> {

    @Override
    protected Tileset[] parseImageData(WritableRaster raster, IndexColorModel icm, MapSpritePackage pckg) throws DisassemblyException {
        int imageWidth = raster.getWidth();
        int imageHeight = raster.getHeight();
        if (imageWidth%8 != 0 || imageHeight%8 != 0) {
            Console.logger().warning("Warning : image dimensions are not a multiple of 8 (pixels per tile). Some data may be lost");
        }
        Palette palette = pckg.palette();
        if (palette == null) {
            new Palette(pckg.name(), Palette.fromICM(icm), true);
        }
        int framesCount = imageWidth*imageHeight/24/24;
        Tileset[] frames = new Tileset[framesCount];
        if (frames.length != 1 && frames.length != 2 && frames.length != 6) {
            Console.logger().warning("Warning : Wrong number of sprites detected, may not import correctly. Expecting 1, 2, or 6. Found : " + frames.length);
        }
        int frameRows = (imageWidth/8)/3;
        int tileId = 0;
        for (int f = 0; f < frames.length; f++) {
            int fx = (f%frameRows)*3;
            int fy = (f/frameRows)*3;
            Tile[] tiles = new Tile[9];
            for (int i = 0; i < tiles.length; i++) {
                int[] pixels = new int[64];
                int x = i%3;
                int y = i/3;
                //Console.logger().finest("Building tile from coordinates "+x+":"+y);
                Tile tile = new Tile();
                tile.setId(tileId);
                tile.setPalette(palette);
                raster.getPixels((fx+x)*PIXEL_WIDTH, (fy+y)*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT, pixels);
                tile.setPixels(pixels);
                //Console.logger().finest(tile.toString());
                tiles[i] = tile;
                tileId++;
            }
            frames[f] = new Tileset(pckg.name(), tiles, imageWidth/8);
        }
        
        return frames;
    }

    @Override
    protected BufferedImage packageImageData(Tileset[] item, MapSpritePackage pckg) throws DisassemblyException {
        int tilesPerRow = item.length <= 1 ? 1 : 2;
        int imageHeight = item.length/2;
        if (item.length%2 != 0) {
            imageHeight++;
        }
        Palette palette = null;
        for (int i = 0; i < item.length; i++) {
            if (item[i] != null && item[i].getTiles() != null) {
                palette = item[i].getTiles()[0].getPalette();
                break;
            }
        }
        BufferedImage image = new BufferedImage(tilesPerRow*3*PIXEL_WIDTH, imageHeight*3*PIXEL_HEIGHT, BufferedImage.TYPE_BYTE_BINARY, palette.getIcm());
        WritableRaster raster = image.getRaster();

        for (int f = 0; f < item.length; f++) {
            if (item[f] == null || item[f].getTiles() == null) {
                continue;
            }
            int fx = (f%tilesPerRow)*3;
            int fy = (f/tilesPerRow)*3;
            Tile[] tiles = item[f].getTiles();
            for(int i = 0; i < tiles.length; i++) {
                if (tiles[i] != null) {
                    raster.setPixels((fx+i%3)*PIXEL_WIDTH, (fy+i/3)*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT, tiles[i].getPixels());
                }
            }
        }
        return image;
    }
    
}
