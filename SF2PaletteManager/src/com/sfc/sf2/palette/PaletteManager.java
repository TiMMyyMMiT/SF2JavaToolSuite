/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette;

import com.sfc.sf2.core.gui.controls.Console;
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
    private final PaletteDisassemblyProcessor paletteDisassemblyProcessor = new PaletteDisassemblyProcessor();
    private final PaletteRawImageProcessor paletteImageProcessor = new PaletteRawImageProcessor();
    
    private Palette palette;

    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }
       
    public Palette importDisassembly(Path filePath, boolean firstColorTransparent) {
        Console.logger().finest("ENTERING importDisassembly");
        PalettePackage pckg = new PalettePackage(PathHelpers.filenameFromPath(filePath), firstColorTransparent);
        this.palette = paletteDisassemblyProcessor.importDisassembly(filePath, pckg);
        Console.logger().finest("EXITING importDisassembly");
        return palette;
    }
    
    public void exportDisassembly(Path filePath, Palette palette, boolean firstColorTransparent) {
        Console.logger().finest("ENTERING exportDisassembly");
        this.palette = palette;
        PalettePackage pckg = new PalettePackage(palette.getName(), firstColorTransparent);
        paletteDisassemblyProcessor.exportDisassembly(filePath, palette, pckg);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public Palette importImage(Path filePath, boolean firstColorTransparent) {
        Console.logger().finest("ENTERING importImage");
        PalettePackage pckg = new PalettePackage(PathHelpers.filenameFromPath(filePath), firstColorTransparent);
        this.palette = paletteImageProcessor.importRawImage(filePath, pckg);
        Console.logger().finest("EXITING importImage");
        return palette;
    }
    
    public void exportImage(Path filePath, Palette currentPalette, boolean firstColorTransparent) {
        Console.logger().finest("ENTERING exportImage");
        this.palette = currentPalette;
        PalettePackage pckg = new PalettePackage(palette.getName(), firstColorTransparent);
        paletteImageProcessor.exportRawImage(filePath, palette, pckg);
        Console.logger().finest("EXITING exportImage");
    }
}
