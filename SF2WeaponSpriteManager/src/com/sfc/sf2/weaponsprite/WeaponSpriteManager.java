/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.weaponsprite;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.weaponsprite.io.DisassemblyManager;
import com.sfc.sf2.weaponsprite.io.RawImageManager;
import java.awt.Color;

/**
 *
 * @author wiz
 */
public class WeaponSpriteManager {

    private final GraphicsManager graphicsManager = new GraphicsManager();
    private WeaponSprite weaponsprite;
    private Palette basePalette;
    private Palette[] palettes;
       
    public void importDisassembly(String paletteEntriesPath, String filepath){
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.importDisassembly() - Importing disassembly ...");
        ImportPalettes(paletteEntriesPath);
        weaponsprite = DisassemblyManager.importDisassembly(filepath, basePalette);
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String graphicsPath){
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.importDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportDisassembly(weaponsprite, graphicsPath);
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.importDisassembly() - Disassembly exported.");        
    }   
    
    public void importRom(String romFilePath, String paletteOffset, String paletteLength, String graphicsOffset, String graphicsLength){
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.importOriginalRom() - Importing original ROM ...");
        graphicsManager.importRom(romFilePath, paletteOffset, paletteLength, graphicsOffset, graphicsLength,GraphicsManager.COMPRESSION_BASIC);
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.importOriginalRom() - Original ROM imported.");
    }
    
    public void exportRom(String originalRomFilePath, String graphicsOffset){
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.exportOriginalRom() - Exporting original ROM ...");
        graphicsManager.exportRom(originalRomFilePath, graphicsOffset, GraphicsManager.COMPRESSION_BASIC);
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.exportOriginalRom() - Original ROM exported.");        
    }      
    
    public void importPng(String paletteEntriesPath, String filepath){
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.importPng() - Importing PNG ...");
        ImportPalettes(paletteEntriesPath);
        weaponsprite = RawImageManager.importImage(filepath);
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.importPng() - PNG imported.");
    }
    
    public void exportPng(String filepath){
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.exportPng() - Exporting PNG ...");
        RawImageManager.exportImage(weaponsprite, filepath, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_PNG);
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.exportPng() - PNG exported.");       
    }
    
    public void importGif(String paletteEntriesPath, String filepath){
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.importGif() - Importing GIF ...");
        ImportPalettes(paletteEntriesPath);
        weaponsprite = RawImageManager.importImage(filepath);
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.importGif() - GIF imported.");
    }
    
    public void exportGif(String filepath){
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.exportGif() - Exporting GIF ...");
        RawImageManager.exportImage(weaponsprite, filepath, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_GIF);
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.exportGif() - GIF exported.");       
    }
    
    private void ImportPalettes(String palettesEntriesPath) {
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.ImportPalettes() - Importing palettes ...");
        if (basePalette == null) {
            Color[] baseColors = new Color[16];
            baseColors[0] = new Color(0xFF00FF00);
            baseColors[1] = Color.WHITE;
            for (int i = 2; i < baseColors.length; i++) {
                baseColors[i] = Color.BLACK;
            }
            basePalette = new Palette("Base Palette", baseColors);        
        }
        palettes = DisassemblyManager.importPalettes(basePalette, palettesEntriesPath);
        System.out.println("com.sfc.sf2.weaponsprite.WeaponSpriteManager.ImportPalettes() - palettes imported.");
    }

    public WeaponSprite getWeaponsprite() {
        return weaponsprite;
    }

    public void setWeaponsprite(WeaponSprite weaponsprite) {
        this.weaponsprite = weaponsprite;
    }

    public Palette[] getPalettes() {
        return palettes;
    }

    public void setPalettes(Palette[] palettes) {
        this.palettes = palettes;
    }
}
