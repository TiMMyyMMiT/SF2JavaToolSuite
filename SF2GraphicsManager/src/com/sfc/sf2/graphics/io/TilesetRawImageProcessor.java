/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Tile;
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
public class TilesetRawImageProcessor extends AbstractRawImageProcessor<Tileset, PalettePackage> {
    
    @Override
    protected Tileset parseImageData(WritableRaster raster, IndexColorModel icm, PalettePackage pckg) throws DisassemblyException {
        int imageWidth = raster.getWidth();
        int imageHeight = raster.getHeight();
        if(imageWidth%8!=0 || imageHeight%8!=0){
            Console.logger().warning("IWarning : image dimensions are not a multiple of 8 (pixels per tile). Some data may be lost");
        }
        Palette palette = new Palette(pckg.name(), Palette.fromICM(icm), pckg.firstColorTransparent());
        int tilesPerRow = imageWidth/8;
        Console.logger().fine("Tiles per row : " + tilesPerRow);
        Tile[] tiles = new Tile[(imageWidth/8)*(imageHeight/8)];
        int tileId = 0;
        int[] pixels = new int[64];
        for(int t = 0; t < tiles.length; t++) {
            int x = t%tilesPerRow*8;
            int y = t/tilesPerRow*8;
            Console.logger().fine("Building tile from coordinates "+x+":"+y);
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
        
        return new Tileset(pckg.name(), tiles, tilesPerRow);
    }

    @Override
    protected BufferedImage packageImageData(Tileset item, PalettePackage pckg) throws DisassemblyException {
        Tile[] tiles = item.getTiles();
        int tilesPerRow = item.getTilesPerRow();
        int imageHeight = tiles.length/tilesPerRow*8;
        if (tiles.length%tilesPerRow != 0) {
            imageHeight += 8;
        }
        int imageWidth = item.getTilesPerRow()*8;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_BINARY, tiles[0].getIcm());
        WritableRaster raster = image.getRaster();

        int[] pixels = new int[64];
        for(int t = 0; t < tiles.length; t++) {
            if (tiles[t] != null) {
                for(int j=0;j<8;j++){
                    for(int i=0;i<8;i++){
                        pixels[i+j*8] = tiles[t].getPixels()[i][j];
                    }
                }
                int x = t%tilesPerRow*8;
                int y = t/tilesPerRow*8;
                raster.setPixels(x, y, 8, 8, pixels);
            }
        }
        return image;
    }
}
