/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite;

import com.sfc.sf2.battlesprite.BattleSprite.BattleSpriteType;
import com.sfc.sf2.battlesprite.io.BattleSpriteDisassemblyProcessor;
import com.sfc.sf2.battlesprite.io.BattleSpriteMetadataProcessor;
import com.sfc.sf2.battlesprite.io.BattleSpritePackage;
import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.core.io.AbstractRawImageProcessor.FileFormat;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.graphics.io.TilesetDisassemblyProcessor;
import com.sfc.sf2.helpers.FileHelpers;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class BattleSpriteManager extends AbstractManager {
    
    private final BattleSpriteDisassemblyProcessor battleSpriteDisassemblyProcessor = new BattleSpriteDisassemblyProcessor();
    private final BattleSpriteMetadataProcessor battleSpriteMetadataProcessor = new BattleSpriteMetadataProcessor();
    private final TilesetManager tilesetManager = new TilesetManager();
    private final PaletteManager paletteManager = new PaletteManager();
    
    private BattleSprite battlesprite;
    
    @Override
    public void clearData() {
        if (battlesprite != null) {
            battlesprite.clearIndexedColorImage(true);
            battlesprite = null;
        }
    }
    
    public BattleSprite importDisassembly(Path filePath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        battlesprite = battleSpriteDisassemblyProcessor.importDisassembly(filePath, null);
        Console.logger().info("Battle Sprite successfully imported from : " + filePath);
        Console.logger().finest("EXITING importDisassembly");
        return battlesprite;
    }
    
    public void exportDisassembly(Path filePath, BattleSprite battlesprite) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.battlesprite = battlesprite;
        battleSpriteDisassemblyProcessor.exportDisassembly(filePath, battlesprite, null);
        Console.logger().info("Battle Sprite successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public BattleSprite importImage(Path filePath, boolean useImagePalette) throws IOException, RawImageException, DisassemblyException {
        Console.logger().finest("ENTERING importImage");
        String filename = filePath.getFileName().toString();
        String extension = ".png";
        int dotIndex = filename.indexOf('.');
        int frameIndex = filename.indexOf("-frame-");
        if (dotIndex != -1) {
            extension = filename.substring(dotIndex+1);
        }
        if (frameIndex != -1) {
            filename = filename.substring(0, frameIndex);
            filePath = filePath.getParent();
        } else {
            filename = filename.substring(0, dotIndex);
            filePath = filePath.getParent();
        }
        File[] files = FileHelpers.findAllFilesInDirectory(filePath, filename + "-palette-", extension);
        Palette[] palettes = new Palette[useImagePalette ? files.length+1 : files.length];
        for (int f=0; f < files.length; f++) {
            Palette palette = paletteManager.importDisassembly(files[f].toPath(), true);
            palettes[f] = palette;
        }
        Palette defaultPalette = useImagePalette || palettes.length == 0 || palettes[0] == null ? null : palettes[0];
        files = FileHelpers.findAllFilesInDirectory(filePath, filename + "-frame-", extension);
        Tileset[] frames = new Tileset[files.length];
        for (int f=0; f < files.length; f++) {
            Tileset frame = tilesetManager.importImage(files[f].toPath(), true);
            if (useImagePalette && defaultPalette == null) {
                defaultPalette = frame.getPalette();
                palettes[palettes.length-1] = defaultPalette;
            }
            frame.setPalette(defaultPalette);
            frames[f] = frame;
        }
        BattleSpriteType type = frames[0].getTiles().length > 144 ? BattleSpriteType.ENEMY : BattleSpriteType.ALLY;
        battlesprite = new BattleSprite(type, frames, palettes);
        
        Path metaPath = filePath.resolve(".meta");
        battleSpriteMetadataProcessor.importMetadata(metaPath, battlesprite);
        Console.logger().info("Battle Sprite successfully imported from : " + filePath);
        Console.logger().finest("EXITING importImage");
        return battlesprite;
    }
    
    public void exportImage(Path filePath, BattleSprite battlesprite, int selectedPalette, FileFormat format) throws IOException, DisassemblyException, RawImageException {
        Console.logger().finest("ENTERING exportImage");
        this.battlesprite = battlesprite;
        Tileset[] frames = battlesprite.getFrames();
        String filename = filePath.getFileName().toString();
        int frameIndex = filename.indexOf("-frame-");
        if (frameIndex != -1) {
            filename = filename.substring(0, frameIndex);
            filePath = filePath.getParent();
        } else {
            int dotIndex = filename.indexOf('.');
            filename = filename.substring(0, dotIndex);
            filePath = filePath.getParent();
        }
        for (int i=0; i < frames.length; i++) {
            Path framePath = filePath.resolve("-frame-" + String.valueOf(i) + AbstractRawImageProcessor.GetFileExtensionString(format));
            tilesetManager.exportImage(framePath, frames[i]);
        }
        Palette[] palettes = battlesprite.getPalettes();
        if (palettes.length > 0) {
            for (int i=0; i < palettes.length; i++) {
                Path palettePath = filePath.resolve(filename + "-palette-" + String.valueOf(i) + ".bin");
                paletteManager.exportDisassembly(palettePath, palettes[i]);
            }
            Console.logger().info(palettes.length + " Battle Sprite palettes successfully exported");
        }
        Path metaPath = filePath.resolve(".meta");
        battleSpriteMetadataProcessor.exportMetadata(metaPath, battlesprite);
        Console.logger().info("Battle Sprite metadata successfully exported to : " + metaPath);
        Console.logger().info("Battle Sprite successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportImage");
    }

    public BattleSprite getBattleSprite() {
        return battlesprite;
    }

    public void setBattleSprite(BattleSprite battlesprite) {
        this.battlesprite = battlesprite;
    }
}
