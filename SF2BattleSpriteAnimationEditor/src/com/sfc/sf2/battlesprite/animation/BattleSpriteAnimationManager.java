/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation;

import com.sfc.sf2.background.BackgroundManager;
import com.sfc.sf2.battlesprite.BattleSprite;
import com.sfc.sf2.battlesprite.BattleSpriteManager;
import com.sfc.sf2.battlesprite.animation.io.DisassemblyManager;
import com.sfc.sf2.battlesprite.animation.io.PngManager;
import com.sfc.sf2.ground.GroundManager;
import com.sfc.sf2.weaponsprite.WeaponSpriteManager;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimationManager {
       
    private BackgroundManager backgroundManager = new BackgroundManager();
    private GroundManager groundManager = new GroundManager();
    private BattleSpriteManager battlespriteManager = new BattleSpriteManager();
    private WeaponSpriteManager weaponspriteManager = new WeaponSpriteManager();
    private BattleSpriteAnimation battlespriteanimation = new BattleSpriteAnimation();
       
    public void importDisassembly(String backgroundPath, String groundBasePalettePath, String groundPalettePath, String groundPath, String battlespritePath, String weaponPalettesPath, String weaponPath, String animationPath){
        System.out.println("com.sfc.sf2.battlespriteanimation.BattleSpriteAnimationManager.importDisassembly() - Importing disassembly ...");
        backgroundManager.importSingleDisassembly(backgroundPath);
        groundManager.importDisassembly(groundBasePalettePath, groundPalettePath, groundPath);
        battlespriteManager.importDisassembly(battlespritePath);
        if(battlespriteManager.getBattleSprite().getType()==BattleSprite.TYPE_ALLY && !weaponPalettesPath.isEmpty() && !weaponPath.isEmpty()){
            weaponspriteManager.importDisassembly(weaponPalettesPath, weaponPath);
        }
        int animationType = (battlespriteManager.getBattleSprite().getType()==BattleSprite.TYPE_ALLY)?BattleSpriteAnimation.TYPE_ALLY:BattleSpriteAnimation.TYPE_ENEMY;
        battlespriteanimation = DisassemblyManager.importDisassembly(animationPath, animationType);
        System.out.println("com.sfc.sf2.battlespriteanimation.BattleSpriteAnimationManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String filepath){
        System.out.println("com.sfc.sf2.battlespriteanimation.BattleSpriteAnimationManager.importDisassembly() - Exporting disassembly ...");
        int animationType = (battlespriteManager.getBattleSprite().getType()==BattleSprite.TYPE_ALLY)?BattleSpriteAnimation.TYPE_ALLY:BattleSpriteAnimation.TYPE_ENEMY;
        DisassemblyManager.exportDisassembly(battlespriteanimation, filepath, animationType);
        System.out.println("com.sfc.sf2.battlespriteanimation.BattleSpriteAnimationManager.importDisassembly() - Disassembly exported.");        
    }   
    
    
    public void exportPng(String filepath, int selectedPalette){
        System.out.println("com.sfc.sf2.battlespriteanimation.BattleSpriteAnimationManager.exportPng() - Exporting PNG ...");
        PngManager.exportPng(battlespriteanimation, filepath, selectedPalette);
        System.out.println("com.sfc.sf2.battlespriteanimation.BattleSpriteAnimationManager.exportPng() - PNG exported.");       
    }

    public BattleSpriteAnimation getBattleSpriteAnimation() {
        return battlespriteanimation;
    }

    public void setBattleSpriteAnimation(BattleSpriteAnimation battlespriteanimation) {
        this.battlespriteanimation = battlespriteanimation;
    }

    public BackgroundManager getBackgroundManager() {
        return backgroundManager;
    }

    public void setBackgroundManager(BackgroundManager backgroundManager) {
        this.backgroundManager = backgroundManager;
    }

    public GroundManager getGroundManager() {
        return groundManager;
    }

    public void setGroundManager(GroundManager groundManager) {
        this.groundManager = groundManager;
    }

    public BattleSpriteManager getBattlespriteManager() {
        return battlespriteManager;
    }

    public void setBattlespriteManager(BattleSpriteManager battlespriteManager) {
        this.battlespriteManager = battlespriteManager;
    }

    public WeaponSpriteManager getWeaponspriteManager() {
        return weaponspriteManager;
    }

    public void setWeaponspriteManager(WeaponSpriteManager weaponspriteManager) {
        this.weaponspriteManager = weaponspriteManager;
    }

    public BattleSpriteAnimation getBattlespriteanimation() {
        return battlespriteanimation;
    }

    public void setBattlespriteanimation(BattleSpriteAnimation battlespriteanimation) {
        this.battlespriteanimation = battlespriteanimation;
    }

    
}
