/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.palette.io;

import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.palette.Palette;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author TiMMy
 */
public class PaletteRawImageProcessor extends AbstractRawImageProcessor<Palette, PalettePackage> {

    @Override
    protected Palette parseImageData(WritableRaster raster, IndexColorModel icm, PalettePackage pckg) throws DisassemblyException {
        return new Palette(pckg.name(), Palette.fromICM(icm), pckg.firstColorTransparent());
    }

    @Override
    protected BufferedImage packageImageData(Palette item, PalettePackage pckg) throws DisassemblyException {
        BufferedImage image = new BufferedImage(item.getColors().length, 1, BufferedImage.TYPE_BYTE_INDEXED, item.getIcm());
        WritableRaster raster = image.getRaster();
        int[] colors = new int[item.getColors().length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = i;
        }
        raster.setPixels(0, 0, colors.length, 1, colors);
        return image;
    }
}
