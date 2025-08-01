/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics;

import com.sfc.sf2.graphics.io.TilesetDisassemblyProcessor;
import com.sfc.sf2.graphics.io.TilesetDisassemblyProcessor.TilesetCompression;
import com.sfc.sf2.graphics.io.TilesetPackage;
import com.sfc.sf2.graphics.io.TilesetRawImageProcessor;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class TilesetsManager {
    private static final Logger LOG = Logger.getLogger(TilesetsManager.class.getName());
    
    private final PaletteManager paletteManager = new PaletteManager();
    private final TilesetDisassemblyProcessor tilesetDisassemblyProcessor = new TilesetDisassemblyProcessor();
    private final TilesetRawImageProcessor tilesetImageProcessor = new TilesetRawImageProcessor();
    
    private Tileset tileset;
       
    public Tileset importDisassembly(Path paletteFilePath, Path graphicsFilePath, TilesetCompression compression, int tilesPerRow) {
        LOG.entering(LOG.getName(),"importDisassembly");
        Palette palette = paletteManager.importDisassembly(paletteFilePath, true);
        TilesetPackage pckg = new TilesetPackage(PathHelpers.filenameFromPath(graphicsFilePath), compression, palette, tilesPerRow);
        tileset = tilesetDisassemblyProcessor.importDisassembly(graphicsFilePath, pckg);
        LOG.exiting(LOG.getName(),"importDisassembly");
        return tileset;
    }
    
    public void exportDisassembly(Path graphicsFilePath, TilesetCompression compression) {
        LOG.entering(LOG.getName(),"importDisassembly");
        TilesetPackage pckg = new TilesetPackage(PathHelpers.filenameFromPath(graphicsFilePath), compression, tileset.getPalette(), tileset.getTilesPerRow());
        tilesetDisassemblyProcessor.exportDisassembly(graphicsFilePath, tileset, pckg);
        LOG.exiting(LOG.getName(),"importDisassembly");        
    }
    
    public Tileset importImage(Path filePath) {
        LOG.entering(LOG.getName(),"importImage");
        TilesetPackage pckg = new TilesetPackage(PathHelpers.filenameFromPath(filePath), TilesetCompression.NONE, null, 0);
        tileset = tilesetImageProcessor.importRawImage(filePath, pckg);
        paletteManager.setPalette(tileset.getPalette());
        LOG.exiting(LOG.getName(),"importImage");
        return tileset;
    }
    
    public void exportImage(Path filePath) {
        LOG.entering(LOG.getName(),"exportImage");
        TilesetPackage pckg = new TilesetPackage(PathHelpers.filenameFromPath(filePath), TilesetCompression.NONE, null, 0);
        tilesetImageProcessor.exportRawImage(filePath, tileset, pckg);
        LOG.exiting(LOG.getName(),"exportImage");
    }
       
    public void importDisassemblyWithLayout(String baseTilesetFilePath, 
        String palette1FilePath, String palette1Offset,
        String palette2FilePath, String palette2Offset,
        String palette3FilePath, String palette3Offset,
        String palette4FilePath, String palette4Offset,
        String tileset1FilePath, String tileset1Offset,
        String tileset2FilePath, String tileset2Offset,
        String layoutFilePath, int compression){/*
        LOG.entering(LOG.getName(),"importDisassemblyWithLayout");
        LOG.info("info");
        LOG.fine("fine");
        LOG.finer("finer");
        LOG.finest("finest");
        Palette[] palettes = new Palette[4];
        paletteManager.importRom(palette1FilePath, palette1Offset,"32");
        palettes[0] = paletteManager.getPalette();
        paletteManager.importRom(palette2FilePath, palette2Offset,"32");
        palettes[1] = paletteManager.getPalette();
        paletteManager.importRom(palette3FilePath, palette3Offset,"32");
        palettes[2] = paletteManager.getPalette();
        paletteManager.importRom(palette4FilePath, palette4Offset,"32");
        palettes[3] = paletteManager.getPalette();
        //palette[0] = new Color(255, 255, 255, 0);
        tiles = DisassemblyManager.importDisassemblyWithLayout(baseTilesetFilePath, palettes, tileset1FilePath, tileset1Offset, tileset2FilePath, tileset2Offset, compression, layoutFilePath);
        LOG.exiting(LOG.getName(),"importDisassemblyWithLayout");*/
    }
    
    public void exportTilesAndLayout(String palettePath, String tilesPath, String layoutPath, String graphicsOffset, int compression, int palette){
        /*LOG.entering(LOG.getName(),"exportTilesAndLayout");
        paletteManager.exportDisassembly(palettePath, tiles[0].getPalette());
        DisassemblyManager.exportTilesAndLayout(tiles, tilesPath, layoutPath, graphicsOffset, compression, palette);
        LOG.exiting(LOG.getName(),"exportTilesAndLayout");    */
    }

    public Tileset getTileset() {
        return tileset;
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
    }
}
