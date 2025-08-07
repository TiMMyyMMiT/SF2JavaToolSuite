/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic.io;

import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.compression.StackGraphicsDecoder;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteDecoder;
import java.awt.Color;
import com.sfc.sf2.helpers.BinaryHelpers;

/**
 *
 * @author TiMMy
 */
public class SpellDisassemblyProcessor extends AbstractDisassemblyProcessor<Tileset, SpellTilesetPackage> {
    
    @Override
    protected Tileset parseDisassemblyData(byte[] data, SpellTilesetPackage pckg) throws DisassemblyException {
        if(data.length < 42) {
            throw new DisassemblyException("Spell tileset ignored because of too small length (must be a dummy file).");
        }
        byte[] colorData = new byte[6];
        System.arraycopy(data, 2, colorData, 0, 6);
        Color[] swapColors = PaletteDecoder.decodePalette(colorData);
        Color[] paletteColors = new Color[pckg.defaultPalette().getColors().length];
        System.arraycopy(pckg.defaultPalette().getColors(), 0, paletteColors, 0, paletteColors.length);
        paletteColors[9] = swapColors[0];
        paletteColors[13] = swapColors[1];
        paletteColors[14] = swapColors[2];
        Palette palette = new Palette(pckg.name(), paletteColors, true);

        byte[] tileData = new byte[data.length - 8];
        System.arraycopy(data, 8, tileData, 0, tileData.length);
        Tile[] tiles = new StackGraphicsDecoder().decode(tileData, palette);

        return new Tileset(pckg.name(), tiles, pckg.tilesPerRow());
    }
    
    @Override
    protected byte[] packageDisassemblyData(Tileset item, SpellTilesetPackage pckg) throws DisassemblyException {
        Color[] paletteColors = item.getPalette().getColors();
        Color[] swapColors = new Color[3];
        swapColors[0] = paletteColors[9];
        swapColors[1] = paletteColors[13];
        swapColors[2] = paletteColors[14];
        byte[] colorSwapBytes = PaletteDecoder.encodePalette(swapColors);
        byte[] tilesBytes = new StackGraphicsDecoder().encode(item.getTiles());

        byte[] newSpellGraphicFileBytes = new byte[tilesBytes.length + 8];
        BinaryHelpers.setWord(newSpellGraphicFileBytes, 0, (short)(StackGraphicsDecoder.lastEncodedUncompressedBytes));
        System.arraycopy(colorSwapBytes, 0, newSpellGraphicFileBytes, 2, colorSwapBytes.length);
        System.arraycopy(tilesBytes, 0, newSpellGraphicFileBytes, 8, tilesBytes.length);
        
        return newSpellGraphicFileBytes;
    }
}
