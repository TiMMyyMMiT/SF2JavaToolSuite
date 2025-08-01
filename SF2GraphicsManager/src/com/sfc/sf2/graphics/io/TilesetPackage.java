/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.graphics.io.TilesetDisassemblyProcessor.TilesetCompression;
import com.sfc.sf2.palette.Palette;

/**
 *
 * @author TiMMy
 */
public record TilesetPackage(String name, TilesetCompression compression, Palette palette, int tilesPerRow) { }
