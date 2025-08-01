/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette;

import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.palette.io.PaletteDisassemblyProcessor;
import com.sfc.sf2.palette.io.PalettePackage;
import com.sfc.sf2.palette.io.PaletteRawImageProcessor;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class PaletteManager {
    private static final Logger LOG = Logger.getLogger(PaletteManager.class.getName());
    
    private final PaletteDisassemblyProcessor paletteDisassemblyProcessor = new PaletteDisassemblyProcessor();
    private final PaletteRawImageProcessor paletteImageProcessor = new PaletteRawImageProcessor();
    
    private Palette palette;

    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }
       
    public Palette importDisassembly(Path filePath, boolean firstColorTransparent){
        LOG.entering(LOG.getName(),"importDisassembly");
        PalettePackage pckg = new PalettePackage(PathHelpers.filenameFromPath(filePath), firstColorTransparent);
        this.palette = paletteDisassemblyProcessor.importDisassembly(filePath, pckg);
        LOG.exiting(LOG.getName(),"importDisassembly");
        return palette;
    }
    
    public void exportDisassembly(Path filePath, Palette palette, boolean firstColorTransparent){
        LOG.entering(LOG.getName(),"importDisassembly");
        this.palette = palette;
        PalettePackage pckg = new PalettePackage(palette.getName(), firstColorTransparent);
        paletteDisassemblyProcessor.exportDisassembly(filePath, palette, pckg);
        LOG.exiting(LOG.getName(),"importDisassembly"); 
    }
    
    public Palette importImage(Path filePath, boolean firstColorTransparent) {
        LOG.entering(LOG.getName(),"importImage");
        PalettePackage pckg = new PalettePackage(PathHelpers.filenameFromPath(filePath), firstColorTransparent);
        this.palette = paletteImageProcessor.importRawImage(filePath, pckg);
        LOG.exiting(LOG.getName(),"importImage");
        return palette;
    }
    
    public void exportImage(Path filePath, Palette currentPalette, boolean firstColorTransparent) {
        LOG.entering(LOG.getName(),"exportImage");
        this.palette = currentPalette;
        PalettePackage pckg = new PalettePackage(palette.getName(), firstColorTransparent);
        paletteImageProcessor.exportRawImage(filePath, palette, pckg);
        LOG.exiting(LOG.getName(),"exportImage");  
    }
}
