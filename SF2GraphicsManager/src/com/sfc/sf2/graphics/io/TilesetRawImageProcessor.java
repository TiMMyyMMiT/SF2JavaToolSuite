/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.core.io.RawImageException;
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
public class TilesetRawImageProcessor extends AbstractTilesetRawImageProcessor<Tileset, PalettePackage> {
    
    @Override
    protected Tileset parseImageData(WritableRaster raster, IndexColorModel icm, PalettePackage pckg) throws RawImageException {
        checkImageDimensions(raster);
        Palette palette = pckg.preLoadedPalette();
        if (palette == null) {
            palette = new Palette(pckg.name(), Palette.fromICM(icm), pckg.firstColorTransparent());
        }
        //Console.logger().finest("Tiles per row : " + raster.getWidth()/PIXEL_WIDTH);
        Tileset tileset = parseTileset(raster, palette);
        tileset.setName(pckg.name());
        return tileset;
    }

    @Override
    protected BufferedImage packageImageData(Tileset item, PalettePackage pckg) throws RawImageException {
        BufferedImage image = setupImage(item);
        writeTileset(image.getRaster(), item);
        return image;
    }
}
