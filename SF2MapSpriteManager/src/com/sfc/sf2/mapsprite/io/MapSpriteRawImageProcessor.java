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
import com.sfc.sf2.palette.io.PalettePackage;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author TiMMy
 */
public class MapSpriteRawImageProcessor extends AbstractRawImageProcessor<Tileset[], PalettePackage> {

    @Override
    protected Tileset[] parseImageData(WritableRaster raster, IndexColorModel icm, PalettePackage pckg) throws DisassemblyException {
        int imageWidth = raster.getWidth();
        int imageHeight = raster.getHeight();
        if (imageWidth%8 != 0 || imageHeight%8 != 0) {
            Console.logger().warning("Warning : image dimensions are not a multiple of 8 (pixels per tile). Some data may be lost");
        }
        Palette palette = pckg.preLoadedPalette();
        if (palette == null) {
            new Palette(pckg.name(), Palette.fromICM(icm), pckg.firstColorTransparent());
        }
        int framesCount = imageWidth*imageHeight/24/24;
        Tileset[] frames = new Tileset[framesCount];
        if (frames.length > 6 || frames.length%2 == 1) {
            Console.logger().warning("Warning : Wrong number of sprites detected, may not import correctly. Expecting 2, 4, or 6. Found : " + frames.length);
        }
        int tilesWidth = imageWidth/24;
        for (int f = 0; f < frames.length; f++) {
            Tile[] tiles = new Tile[9];
            int tileId = 0;
            int[] pixels = new int[64];
            for(int t = 0; t < tiles.length; t++) {
                int x = t%tilesWidth*8;
                int y = t/tilesWidth*8;
                //Console.logger().finest("Building tile from coordinates "+x+":"+y);
                Tile tile = new Tile();
                tile.setId(tileId);
                tile.setPalette(palette);
                raster.getPixels(x, y, 8, 8, pixels);
                for(int j=0;j<8;j++){
                    for(int i=0;i<8;i++){
                        tile.setPixel(i, j, pixels[i+j*8]);
                    }
                }
                Console.logger().finest(tile.toString());
                tiles[tileId] = tile;   
                tileId++;
            }
            frames[f] = new Tileset(pckg.name(), tiles, tilesWidth);
        }
        
        return frames;
    }

    @Override
    protected BufferedImage packageImageData(Tileset[] item, PalettePackage pckg) throws DisassemblyException {
        int tilesPerRow = 2;
        int imageHeight = item.length/2;
        Palette palette = null;
        for (int i = 0; i < item.length; i++) {
            if (item[i] != null && item[i].getTiles() != null) {
                palette = item[i].getTiles()[0].getPalette();
                break;
            }
        }
        BufferedImage image = new BufferedImage(tilesPerRow*3*PIXEL_WIDTH, imageHeight*3*PIXEL_HEIGHT, BufferedImage.TYPE_BYTE_BINARY, palette.getIcm());
        WritableRaster raster = image.getRaster();

        int[] pixels = new int[64];
        for (int f = 0; f < item.length; f++) {
            if (item[f] == null || item[f].getTiles() == null) {
                continue;
            }
            int fx = (f%tilesPerRow)*3*PIXEL_WIDTH;
            int fy = (f/tilesPerRow)*3*PIXEL_HEIGHT;
            Tile[] tiles = item[f].getTiles();
            for(int t = 0; t < tiles.length; t++) {
                if (tiles[t] != null) {
                    for(int j=0;j<8;j++){
                        for(int i=0;i<8;i++){
                            pixels[i+j*8] = tiles[t].getPixels()[i][j];
                        }
                    }
                    int x = t%tilesPerRow*8;
                    int y = t/tilesPerRow*8;
                    raster.setPixels(fx+x, fy+y, 8, 8, pixels);
                }
            }
        }
        return image;
    }
    
}
