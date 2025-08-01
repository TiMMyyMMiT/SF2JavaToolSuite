/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette.io;

import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteDecoder;

/**
 *
 * @author TiMMy
 */
public class PaletteDisassemblyProcessor extends AbstractDisassemblyProcessor<Palette, PalettePackage> {
    
    @Override
    protected Palette parseDisassemblyData(byte[] data, PalettePackage pckg) throws DisassemblyException {
        return new Palette(pckg.name(), PaletteDecoder.decodePalette(data, pckg.firstColorTransparent()));
    }

    @Override
    protected byte[] packageDisassemblyData(Palette item, PalettePackage pckg) throws DisassemblyException {
        return PaletteDecoder.encodePalette(item.getColors());
    }
}
