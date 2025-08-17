/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.io;

import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.io.AbstractTilesetRawImageProcessor;
import com.sfc.sf2.palette.Palette;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author TiMMy
 */
public class MapSpriteRawImageProcessor extends AbstractTilesetRawImageProcessor<Tileset[], MapSpritePackage> {

    @Override
    protected Tileset[] parseImageData(WritableRaster raster, IndexColorModel icm, MapSpritePackage pckg) throws RawImageException {
        checkImageDimensions(raster);
        Palette palette = pckg.palette();
        if (palette == null) {
            palette = new Palette(pckg.name(), Palette.fromICM(icm), true);
        }
        Tileset[] frames = parseTileset(raster, 3, palette);
        for (int i = 0; i < frames.length; i++) {
            frames[i].setName(pckg.name());
        }        
        return frames;
    }

    @Override
    protected BufferedImage packageImageData(Tileset[] item, MapSpritePackage pckg) throws RawImageException {
        BufferedImage image = setupImage(item);
        writeTileset(image.getRaster(), item);
        return image;
    }
    
}
