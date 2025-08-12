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
import com.sfc.sf2.mapsprite.MapSprite;

/**
 *
 * @author TiMMy
 */
public class MapSpriteDisassemblyProcessor extends AbstractDisassemblyProcessor<MapSprite, MapSpritePackage> {
    
    @Override
    protected MapSprite parseDisassemblyData(byte[] data, MapSpritePackage pckg) throws DisassemblyException {
        Tile[] tiles = new BasicGraphicsDecoder().decode(data, pckg.palette());
        if (tiles == null || tiles.length == 0) {
            throw new DisassemblyException("Mapsprite tileset not loaded, tiles are empty. Mapsprite : " + pckg.name());
        } else if (tiles.length != 18) {
            System.out.println("Mapsprite tileset not loaded, expected 18 tiles but found " + tiles.length + ". Mapsprite : " + pckg.name());
        }
        tiles = TileHelpers.reorderTilesSequentially(tiles, 2, 1, 3);
        Tileset tileset = new Tileset(pckg.name(), tiles, 6);
        return new MapSprite(tileset, pckg.indices());
    }

    @Override
    protected byte[] packageDisassemblyData(MapSprite item, MapSpritePackage pckg) throws DisassemblyException {
        Tile[] tiles = TileHelpers.reorderTilesForDisasssembly(item.getTileset().getTiles(), 2, 1, 3);
        byte[] bytes = new BasicGraphicsDecoder().encode(tiles);
        if (bytes == null || bytes.length == 0) {
            throw new DisassemblyException("Tileset not loaded. Tiles are empty.");
        }
        return bytes;
    }
}
