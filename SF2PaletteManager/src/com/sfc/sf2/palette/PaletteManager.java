/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.palette.io.PaletteDisassemblyProcessor;
import com.sfc.sf2.palette.io.PalettePackage;
import com.sfc.sf2.palette.io.PaletteRawImageProcessor;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

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
        try {
            PalettePackage pckg = new PalettePackage(PathHelpers.filenameFromPath(filePath), firstColorTransparent);
            palette = paletteDisassemblyProcessor.importDisassembly(filePath, pckg);
            Console.logger().info("Palette imported successfully from : " + filePath);
        } catch (DisassemblyException | IOException ex) {
            palette = null;
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("Palette could not be imported from  " + filePath);
        }
        Console.logger().finest("EXITING importDisassembly");
        return palette;
    }
    
    public void exportDisassembly(Path filePath, Palette palette, boolean firstColorTransparent) {
        Console.logger().finest("ENTERING exportDisassembly");
        this.palette = palette;
        try {
            PalettePackage pckg = new PalettePackage(palette.getName(), firstColorTransparent);
            paletteDisassemblyProcessor.exportDisassembly(filePath, palette, pckg);
        } catch (IOException | DisassemblyException ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("Palette could not be exported to  " + filePath);
        }
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public Palette importImage(Path filePath, boolean firstColorTransparent) {
        Console.logger().finest("ENTERING importImage");
        PalettePackage pckg = new PalettePackage(PathHelpers.filenameFromPath(filePath), firstColorTransparent);
        try {
            palette = paletteImageProcessor.importRawImage(filePath, pckg);
        } catch (RawImageException | DisassemblyException | IOException ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("Palette could not be imported from  " + filePath);
        }
        Console.logger().finest("EXITING importImage");
        return palette;
    }
    
    public void exportImage(Path filePath, Palette currentPalette, boolean firstColorTransparent) {
        Console.logger().finest("ENTERING exportImage");
        palette = currentPalette;
        PalettePackage pckg = new PalettePackage(palette.getName(), firstColorTransparent);
        try {
            paletteImageProcessor.exportRawImage(filePath, palette, pckg);
        } catch (IOException | DisassemblyException ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("Palette could not be exported to " + filePath);
        }
        Console.logger().finest("EXITING exportImage");
    }
}
