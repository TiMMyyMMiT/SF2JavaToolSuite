/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.graphics.compression;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.palette.Palette;
import java.util.logging.Logger;

/**
 *
 * @author TiMMy
 */
public abstract class AbstractGraphicsDecoder {
    protected final Logger LOG = Logger.getLogger(UncompressedGraphicsDecoder.class.getName());    
    
    public abstract Tile[] decode(byte[] input, Palette palette);
    public abstract byte[] encode(Tile[] tiles);
}
