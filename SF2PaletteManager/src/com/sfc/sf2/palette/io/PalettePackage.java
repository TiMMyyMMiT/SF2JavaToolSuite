/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.palette.io;

import com.sfc.sf2.palette.Palette;

/**
 *
 * @author TiMMy
 */
public class PalettePackage
{
    private final String name;
    public String name() { return name; }
    
    private final boolean firstColorTransparent;
    public boolean firstColorTransparent() { return firstColorTransparent; }
    
    private final Palette preLoadedPalette;
    public Palette preLoadedPalette() { return preLoadedPalette; }
    
    public PalettePackage(String name, boolean firstColorTransparent) {
        this.name = name;
        this.firstColorTransparent = firstColorTransparent;
        this.preLoadedPalette = null;
    }
    
    public PalettePackage(Palette preLoadedPalette) {
        this.name = preLoadedPalette.getName();
        this.firstColorTransparent = true;
        this.preLoadedPalette = preLoadedPalette;
    }
}
