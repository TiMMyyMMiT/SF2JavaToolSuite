/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette;

import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.palette.io.PaletteDisassemblyProcessor;
import com.sfc.sf2.palette.io.PalettePackage;
import com.sfc.sf2.palette.io.PaletteRawImageProcessor;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class PaletteManager {
    private PaletteDisassemblyProcessor paletteDisassemblyProcessor = new PaletteDisassemblyProcessor();
    private PaletteRawImageProcessor paletteImageProcessor = new PaletteRawImageProcessor();
    private Palette palette;

    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }
       
    public void importDisassembly(Path filePath, boolean firstColorTransparent){
        System.out.println("com.sfc.sf2.palette.PaletteManager.importDisassembly() - Importing disassembly ...");
        PalettePackage pckg = new PalettePackage(PathHelpers.filenameFromPath(filePath), firstColorTransparent);
        this.palette = paletteDisassemblyProcessor.importDisassembly(filePath, pckg);
        System.out.println("com.sfc.sf2.palette.PaletteManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(Path filePath, Palette palette, boolean firstColorTransparent){
        System.out.println("com.sfc.sf2.palette.PaletteManager.importDisassembly() - Exporting disassembly ...");
        this.palette = palette;
        PalettePackage pckg = new PalettePackage(palette.getName(), firstColorTransparent);
        paletteDisassemblyProcessor.exportDisassembly(filePath, palette, pckg);
        System.out.println("com.sfc.sf2.palette.PaletteManager.importDisassembly() - Disassembly exported.");
    }
    
    public void importImage(Path filePath, boolean firstColorTransparent) {
        System.out.println("com.sfc.sf2.palette.PaletteManager.importPng() - Importing Image ...");
        PalettePackage pckg = new PalettePackage(PathHelpers.filenameFromPath(filePath), firstColorTransparent);
        this.palette = paletteImageProcessor.importRawImage(filePath, pckg);
        System.out.println("com.sfc.sf2.palette.PaletteManager.importPng() - PNG imported.");
    }
    
    public void exportImage(Path filePath, Palette currentPalette, boolean firstColorTransparent) {
        System.out.println("com.sfc.sf2.palette.PaletteManager.exportImage() - Exporting Image ...");
        this.palette = currentPalette;
        PalettePackage pckg = new PalettePackage(palette.getName(), firstColorTransparent);
        paletteImageProcessor.exportRawImage(filePath, palette, pckg);
        System.out.println("com.sfc.sf2.palette.PaletteManager.exportImage() - GIF exported.");
    }
}
