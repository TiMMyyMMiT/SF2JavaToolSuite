/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.background;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.background.io.DisassemblyManager;
import com.sfc.sf2.background.io.RawImageManager;

/**
 *
 * @author wiz
 */
public class BackgroundManager {
       
    private final GraphicsManager graphicsManager = new GraphicsManager();
    private Background[] backgrounds;
       
    public void importDisassembly(String graphicsBasepath){
        System.out.println("com.sfc.sf2.background.BackgroundManager.importDisassembly() - Importing disassembly ...");
        backgrounds = DisassemblyManager.importDisassembly(graphicsBasepath);
        System.out.println("com.sfc.sf2.background.BackgroundManager.importDisassembly() - Disassembly imported.");
    }
    
    public void importSingleDisassembly(String filepath){
        System.out.println("com.sfc.sf2.background.BackgroundManager.importDisassembly() - Importing disassembly ...");
        backgrounds = new Background[1];
        backgrounds[0] = DisassemblyManager.importSingleBackground(filepath);
        System.out.println("com.sfc.sf2.background.BackgroundManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String basepath){
        System.out.println("com.sfc.sf2.background.BackgroundManager.importDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportDisassembly(backgrounds, basepath);
        System.out.println("com.sfc.sf2.background.BackgroundManager.importDisassembly() - Disassembly exported.");
    }
    
    public void importRom(String romFilePath, String paletteOffset, String paletteLength, String graphicsOffset, String graphicsLength){
        System.out.println("com.sfc.sf2.background.BackgroundManager.importOriginalRom() - Importing original ROM ...");
        graphicsManager.importRom(romFilePath, paletteOffset, paletteLength, graphicsOffset, graphicsLength,GraphicsManager.COMPRESSION_BASIC);
        System.out.println("com.sfc.sf2.background.BackgroundManager.importOriginalRom() - Original ROM imported.");
    }
    
    public void exportRom(String originalRomFilePath, String graphicsOffset){
        System.out.println("com.sfc.sf2.background.BackgroundManager.exportOriginalRom() - Exporting original ROM ...");
        graphicsManager.exportRom(originalRomFilePath, graphicsOffset, GraphicsManager.COMPRESSION_BASIC);
        System.out.println("com.sfc.sf2.background.BackgroundManager.exportOriginalRom() - Original ROM exported.");        
    }
    
    public void importPng(String basepath){
        System.out.println("com.sfc.sf2.background.BackgroundManager.importPng() - Importing PNG ...");
        backgrounds = RawImageManager.importImages(basepath, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_PNG);
        System.out.println("com.sfc.sf2.background.BackgroundManager.importPng() - PNG imported.");
    }
    
    public void exportPng(String basepath){
        System.out.println("com.sfc.sf2.background.BackgroundManager.exportPng() - Exporting PNG ...");
        RawImageManager.exportImages(backgrounds, basepath, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_PNG);
        System.out.println("com.sfc.sf2.background.BackgroundManager.exportPng() - PNG exported.");       
    }
    
    public void importGif(String basepath){
        System.out.println("com.sfc.sf2.background.BackgroundManager.importGif() - Importing PNG ...");
        backgrounds = RawImageManager.importImages(basepath, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_GIF);
        System.out.println("com.sfc.sf2.background.BackgroundManager.importGif() - PNG imported.");
    }
    
    public void exportGif(String basepath){
        System.out.println("com.sfc.sf2.background.BackgroundManager.exportGif() - Exporting GIF ...");
        RawImageManager.exportImages(backgrounds, basepath, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_GIF);
        System.out.println("com.sfc.sf2.background.BackgroundManager.exportGif() - GIF exported.");       
    }

    public Background[] getBackgrounds() {
        return backgrounds;
    }

    public void setBackgrounds(Background[] backgrounds) {
        this.backgrounds = backgrounds;
    }
}
