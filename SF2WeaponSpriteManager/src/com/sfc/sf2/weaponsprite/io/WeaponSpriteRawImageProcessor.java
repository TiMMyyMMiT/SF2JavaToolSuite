/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.weaponsprite.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.io.AbstractTilesetRawImageProcessor;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.weaponsprite.WeaponSprite;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author wiz
 */
public class WeaponSpriteRawImageProcessor extends AbstractTilesetRawImageProcessor<WeaponSprite, WeaponSpritePackage> {
    
    @Override
    protected WeaponSprite parseImageData(WritableRaster raster, IndexColorModel icm, WeaponSpritePackage pckg) throws RawImageException {
        checkImageDimensions(raster);
        Palette palette = new Palette("Imported Palette", Palette.fromICM(icm), true);
        Tileset[] frames = parseTileset(raster, WeaponSprite.FRAME_TILE_WIDTH, WeaponSprite.FRAME_TILE_HEIGHT, palette);
        if(frames.length == 0) {
            throw new RawImageException("ERROR Weapon sprite image could not be loaded. Less than 1 frame loaded.");
        } else if (frames.length != 4) {
            Console.logger().severe("WARNING Weapon sprite image wrong number of frames. Expected 4, found : " + frames.length);
        }
        return new WeaponSprite(pckg.index(), frames);
    }

    @Override
    protected BufferedImage packageImageData(WeaponSprite item, WeaponSpritePackage pckg) throws RawImageException {
        Tileset[] frames = item.getFrames();
        BufferedImage image = setupImage(frames, 1);
        writeTileset(image.getRaster(), frames, 1);
        return image;
    }
}
