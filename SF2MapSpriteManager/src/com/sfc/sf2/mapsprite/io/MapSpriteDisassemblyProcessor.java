/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.io;

import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.compression.BasicGraphicsDecoder;
import com.sfc.sf2.helpers.TileHelpers;

/**
 *
 * @author TiMMy
 */
public class MapSpriteDisassemblyProcessor extends AbstractDisassemblyProcessor<Tileset[], MapSpritePackage> {
    
    @Override
    protected Tileset[] parseDisassemblyData(byte[] data, MapSpritePackage pckg) throws DisassemblyException {
        Tile[] tiles = new BasicGraphicsDecoder().decode(data, pckg.palette());
        if (tiles == null || tiles.length == 0) {
            throw new DisassemblyException("Mapsprite tileset not loaded, tiles are empty. Mapsprite : " + pckg.name());
        } else if (tiles.length != 18) {
            System.out.println("Mapsprite tileset not loaded, expected 18 tiles but found " + tiles.length + ". Mapsprite : " + pckg.name());
        }
        Tile[] tiles_frame1 = new Tile[9];
        Tile[] tiles_frame2 = new Tile[9];
        System.arraycopy(tiles, 0, tiles_frame1, 0, 9);
        System.arraycopy(tiles, 9, tiles_frame2, 0, 9);
        tiles_frame1 = TileHelpers.reorderTilesSequentially(tiles_frame1, 1, 1, 3);
        tiles_frame2 = TileHelpers.reorderTilesSequentially(tiles_frame2, 1, 1, 3);
        Tileset[] tilesets = new Tileset[2];
        tilesets[0] = new Tileset(pckg.name(), tiles_frame1, 6);
        tilesets[1] = new Tileset(pckg.name(), tiles_frame2, 6);
        return tilesets;
    }

    @Override
    protected byte[] packageDisassemblyData(Tileset[] item, MapSpritePackage pckg) throws DisassemblyException {
        Tile[] tiles = new Tile[9*item.length];
        for (int i = 0; i < item.length; i++) {
            if (tiles != null)
                tiles = TileHelpers.reorderTilesForDisasssembly(tiles, 1, 1, 3);
                System.arraycopy(item[i].getTiles(), 0, tiles, i*9, 9);
        }
        
        byte[] bytes = new BasicGraphicsDecoder().encode(tiles);
        if (bytes == null || bytes.length == 0) {
            throw new DisassemblyException("Tileset not loaded. Tiles are empty.");
        }
        return bytes;
    }
}
