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
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author TiMMy
 */
public class PaletteRawImageProcessor extends AbstractRawImageProcessor<Palette, PalettePackage> {

    @Override
    protected Palette parseImageData(WritableRaster raster, IndexColorModel icm, PalettePackage pckg) throws DisassemblyException {
        return new Palette(pckg.name(), Palette.fromICM(icm));
    }

    @Override
    protected BufferedImage packageImageData(Palette item, PalettePackage pckg) throws DisassemblyException {
        BufferedImage image = new BufferedImage(16, 1, BufferedImage.TYPE_BYTE_INDEXED, item.getIcm());
        byte[] data = ((DataBufferByte)(image.getRaster().getDataBuffer())).getData();
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte)i;
        }
        return image;
    }
}
