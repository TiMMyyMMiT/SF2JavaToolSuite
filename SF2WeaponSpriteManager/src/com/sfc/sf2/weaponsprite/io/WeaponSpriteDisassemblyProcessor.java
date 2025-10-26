/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.weaponsprite.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.compression.StackGraphicsDecoder;
import com.sfc.sf2.helpers.TileHelpers;
import com.sfc.sf2.weaponsprite.WeaponSprite;
import static com.sfc.sf2.weaponsprite.WeaponSprite.FRAME_TILE_HEIGHT;
import static com.sfc.sf2.weaponsprite.WeaponSprite.FRAME_TILE_WIDTH;
import static com.sfc.sf2.weaponsprite.WeaponSprite.WEAPONSPRITE_FRAMES_LENGTH;

/**
 *
 * @author wiz
 */
public class WeaponSpriteDisassemblyProcessor extends AbstractDisassemblyProcessor<WeaponSprite, WeaponSpritePackage> {
        
    @Override
    protected WeaponSprite parseDisassemblyData(byte[] data, WeaponSpritePackage pckg) throws DisassemblyException {
        if(data.length <= 2) {
            throw new DisassemblyException("Weapon file ignored because it is too small (must be a dummy file) " + data.length);
        }
        Tile[] tiles = new StackGraphicsDecoder().decode(data, pckg.palette());
        int frameTilesCount = FRAME_TILE_WIDTH*FRAME_TILE_HEIGHT;
        int frameCount = tiles.length / frameTilesCount;
        if (frameCount != 4) {
            Console.logger().warning("Weapon file has wrong number of frames. It may not import properly. Expected 4 frames (" + (4*frameTilesCount) + " tiles). Found " + frameCount + " frames (" + tiles.length + " tiles).");
        }
        Tileset[] frames = new Tileset[frameCount];
        for (int i = 0; i < frames.length; i++) {
            Tile[] tileData = new Tile[64];
            System.arraycopy(tiles, i*64, tileData, 0, 64);
            tileData = TileHelpers.reorderTilesSequentially(tileData, 2, 2, 4);
            frames[i] = new Tileset("Frame_"+i, tileData, FRAME_TILE_WIDTH);
        }
        return new WeaponSprite(pckg.index(), frames);
    }

    @Override
    protected byte[] packageDisassemblyData(WeaponSprite item, WeaponSpritePackage pckg) throws DisassemblyException {
        Tileset[] frames = item.getFrames();
        int frameTilesCount = FRAME_TILE_WIDTH*FRAME_TILE_HEIGHT;
        Tile[] tiles = new Tile[WEAPONSPRITE_FRAMES_LENGTH*frameTilesCount];
        for (int i = 0; i < frames.length; i++) {
            Tile[] tileData = TileHelpers.reorderTilesForDisasssembly(frames[i].getTiles(), 2, 2, 4);
            System.arraycopy(tileData, 0, tiles, i*frameTilesCount, frameTilesCount);
        }
        byte[] newWeaponSpriteFileBytes = new StackGraphicsDecoder().encode(tiles);
        return newWeaponSpriteFileBytes;
    }
}
