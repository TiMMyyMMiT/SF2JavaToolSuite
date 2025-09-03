/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.specialSprites.io;

import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.compression.StackGraphicsDecoder;
import com.sfc.sf2.helpers.TileHelpers;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteDecoder;

/**
 *
 * @author TiMMy
 */
public class SpecialSpriteDisassemblyProcessor extends AbstractDisassemblyProcessor<Tileset, SpecialSpritePackage> {
    
    @Override
    protected Tileset parseDisassemblyData(byte[] data, SpecialSpritePackage pckg) throws DisassemblyException {
        Palette palette = pckg.palette();
        boolean separatePalette = palette != null;
        int paletteOffset = separatePalette ? 0 : 32;
        if (!separatePalette) {
            byte[] paletteData = new byte[32];
            System.arraycopy(data, 0, paletteData, 0, paletteOffset);
            palette = new Palette(pckg.name(), PaletteDecoder.decodePalette(paletteData), true);
        }
        byte[] tileData = new byte[data.length - paletteOffset];
        System.arraycopy(data, paletteOffset, tileData, 0, tileData.length);
        Tile[] tiles = new StackGraphicsDecoder().decode(tileData, palette);
        tiles = TileHelpers.reorderTilesSequentially(tiles, pckg.blockColumns(), pckg.blockRows(), pckg.tilesPerBlock());
        return new Tileset(pckg.name(), tiles, pckg.blockColumns()*pckg.tilesPerBlock());
    }

    @Override
    protected byte[] packageDisassemblyData(Tileset item, SpecialSpritePackage pckg) throws DisassemblyException {
        byte[] paletteBytes = new byte[0];
        int paletteOffset = pckg.palette() == null ? 0 : 32;
        if (pckg.palette() != null) {
            paletteBytes = PaletteDecoder.encodePalette(pckg.palette().getColors());
        }
        Tile[] tiles = TileHelpers.reorderTilesForDisasssembly(item.getTiles(), pckg.blockColumns(), pckg.blockRows(), pckg.tilesPerBlock());
        byte[] tilesBytes = new StackGraphicsDecoder().encode(tiles);
        byte[] newSpecialGraphicFileBytes = new byte[paletteBytes.length + tilesBytes.length];
        if (paletteBytes.length > 0) {
            System.arraycopy(paletteBytes, 0, newSpecialGraphicFileBytes, 0, paletteBytes.length);
        }
        System.arraycopy(tilesBytes, 0, newSpecialGraphicFileBytes, paletteOffset, tilesBytes.length);
        return newSpecialGraphicFileBytes;
    }
}
