/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellAnimation;

import com.sfc.sf2.background.BackgroundManager;
import com.sfc.sf2.spellAnimation.io.DisassemblyManager;
import com.sfc.sf2.ground.GroundManager;
import com.sfc.sf2.spellAnimation.io.SpellAnimationAsmProcessor;
import com.sfc.sf2.spellGraphic.SpellGraphicManager;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author TiMMy
 */
public class SpellAnimationManager {
    private final SpellGraphicManager spellGraphicManager = new SpellGraphicManager();
    private final BackgroundManager backgroundManager = new BackgroundManager();
    private final GroundManager groundManager = new GroundManager();
    private final SpellAnimationAsmProcessor spellAnimationAsmProcessor = new SpellAnimationAsmProcessor();
    
    private SpellAnimation spellAnimation = new SpellAnimation();

    public void importDisassembly(String spellGraphicPath, String spellAnimationPath, String spellPalettePath) throws IOException {
        System.out.println("com.sfc.sf2.spellAnimation.SpellAnimationManager.importDisassembly() - Importing disassembly ...");
        spellAnimation = DisassemblyManager.importDisassembly(getAbsoluteFilepath(spellAnimationPath));
        spellGraphicManager.importDisassembly(spellGraphicPath, spellPalettePath);
        spellAnimation.setSpellGraphic(spellGraphicManager.getSpellGraphic());
        backgroundManager.setBackgrounds(null);
        groundManager.setGround(null);
        System.out.println("com.sfc.sf2.spellAnimation.SpellAnimationManager.importDisassembly() - Disassembly imported.");
    }
    
    public void importDisassembly(String spellGraphicPath, String spellAnimationPath, String spellPalettePath, String backgroundPath, String groundBasePalettePath, String groundPalettePath, String groundPath) throws IOException {
        System.out.println("com.sfc.sf2.spellAnimation.SpellAnimationManager.importDisassembly() - Importing disassembly ...");
        spellAnimation = DisassemblyManager.importDisassembly(getAbsoluteFilepath(spellAnimationPath));
        spellGraphicManager.importDisassembly(spellGraphicPath, spellPalettePath);
        spellAnimation.setSpellGraphic(spellGraphicManager.getSpellGraphic());
        backgroundManager.importSingleDisassembly(backgroundPath);
        groundManager.importDisassembly(groundBasePalettePath, groundPalettePath, groundPath);
        System.out.println("com.sfc.sf2.spellAnimation.SpellAnimationManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String filepath){
        System.out.println("com.sfc.sf2.spellAnimation.SpellAnimationManager.importDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportDisassembly(spellAnimation, filepath);
        System.out.println("com.sfc.sf2.spellAnimation.SpellAnimationManager.importDisassembly() - Disassembly exported.");        
    }
    
    private String getAbsoluteFilepath(String filepath) {
        String toolDir = System.getProperty("user.dir");
        Path toolPath = Paths.get(toolDir);
        Path filePath = Paths.get(filepath);
        if (!filePath.isAbsolute())
            filePath = toolPath.resolve(filePath);
        
        return filePath.toString();
    }

    public BackgroundManager getBackgroundManager() {
        return backgroundManager;
    }

    public GroundManager getGroundManager() {
        return groundManager;
    }

    public SpellAnimation getSpellAnimation() {
        return spellAnimation;
    }

    public void setSpellAnimation(SpellAnimation spellAnimation) {
        this.spellAnimation = spellAnimation;
    }

    public SpellAnimation getBattlespriteanimation() {
        return spellAnimation;
    }

    public void setBattlespriteanimation(SpellAnimation spellAnimation) {
        this.spellAnimation = spellAnimation;
    }
}
