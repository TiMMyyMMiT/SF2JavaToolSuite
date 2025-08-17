/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.weaponsprite;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.core.io.asm.EntriesAsmData;
import com.sfc.sf2.core.io.asm.EntriesAsmProcessor;
import com.sfc.sf2.helpers.FileHelpers;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.io.PaletteDisassemblyProcessor;
import com.sfc.sf2.palette.io.PalettePackage;
import com.sfc.sf2.weaponsprite.io.WeaponSpriteDisassemblyProcessor;
import com.sfc.sf2.weaponsprite.io.WeaponSpritePackage;
import com.sfc.sf2.weaponsprite.io.WeaponSpriteRawImageProcessor;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 *
 * @author wiz
 */
public class WeaponSpriteManager extends AbstractManager {

    private final PaletteDisassemblyProcessor paletteDisassemblyProcessor = new PaletteDisassemblyProcessor();
    private final WeaponSpriteDisassemblyProcessor weaponDisassemblyProcessor = new WeaponSpriteDisassemblyProcessor();
    private final WeaponSpriteRawImageProcessor weaponSpriteRawImageProcessor = new WeaponSpriteRawImageProcessor();
    private final EntriesAsmProcessor entriesAsmProcessor = new EntriesAsmProcessor();
    
    private WeaponSprite weaponsprite;
    private Palette basePalette;
    private Palette[] palettes;

    @Override
    public void clearData() {
        basePalette = null;
        palettes = null;
        if (weaponsprite != null) {
            weaponsprite.clearIndexedColorImage(true);
            weaponsprite = null;
        }
    }
       
    public void importDisassembly(Path paletteEntriesPath, Path filePath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassembly");
        ImportPalettesFromEntries(paletteEntriesPath);
        int index = FileHelpers.getNumberFromFileName(filePath.toFile());
        WeaponSpritePackage pckg = new WeaponSpritePackage(index, basePalette);
        weaponsprite = weaponDisassemblyProcessor.importDisassembly(filePath, pckg);
        Console.logger().finest("EXITING importDisassembly");
    }
    
    public void exportDisassembly(Path filePath, WeaponSprite weaponsprite) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.weaponsprite = weaponsprite;
        weaponDisassemblyProcessor.exportDisassembly(filePath, weaponsprite, null);
        Console.logger().finest("EXITING exportDisassembly");       
    }
    
    public void importImage(Path paletteEntriesPath, Path filePath) throws IOException, RawImageException, AsmException {
        Console.logger().finest("ENTERING importImage");
        ImportPalettesFromEntries(paletteEntriesPath);
        int index = FileHelpers.getNumberFromFileName(filePath.toFile());
        WeaponSpritePackage pckg = new WeaponSpritePackage(index, null);
        weaponsprite = weaponSpriteRawImageProcessor.importRawImage(filePath, pckg);
        Console.logger().finest("EXITING importImage");
    }
    
    public void exportImage(Path filePath, WeaponSprite weaponsprite) throws IOException, RawImageException {
        Console.logger().finest("ENTERING exportImage");
        this.weaponsprite = weaponsprite;
        WeaponSpritePackage pckg = new WeaponSpritePackage(0, basePalette);
        weaponSpriteRawImageProcessor.exportRawImage(filePath, weaponsprite, pckg);
        Console.logger().finest("EXITING exportImage");
    }
    
    private void ImportPalettesFromEntries(Path entriesPath) throws IOException, AsmException {
        Console.logger().finest("ENTERING ImportPalettesFromEntries");
        EntriesAsmData entriesData = entriesAsmProcessor.importAsmData(entriesPath);
        Console.logger().info("Weapon palettes entries successfully imported. Entries found : " + entriesData.entriesCount());
        ArrayList<Palette> palettesList = new ArrayList<>();
        int palettesCount = 0;
        int failedToLoad = 0;
        for (int i = 0; i < entriesData.entriesCount(); i++) {
            Path palettePath = PathHelpers.getIncbinPath().resolve(entriesData.getPathForEntry(i));
            try {
                PalettePackage pckg = new PalettePackage(palettePath.getFileName().toString(), true);
                palettesList.add(paletteDisassemblyProcessor.importDisassembly(palettePath, pckg));
                palettesCount++;
            } catch (Exception e) {
                failedToLoad++;
                Console.logger().warning("Palette could not be imported : " + palettePath + " : " + e);
            }
        }
        palettes = new Palette[palettesList.size()];
        palettes = palettesList.toArray(palettes);
        basePalette = palettes[0];
        Console.logger().info(palettes.length + " palettes with " + palettesCount + " frames successfully imported from entries file : " + entriesPath);
        Console.logger().info((entriesData.entriesCount() - entriesData.uniquePathsCount()) + " duplicate palette entries found.");
        if (failedToLoad > 0) {
            Console.logger().severe(failedToLoad + " palettes failed to import. See logs above");
        }
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
