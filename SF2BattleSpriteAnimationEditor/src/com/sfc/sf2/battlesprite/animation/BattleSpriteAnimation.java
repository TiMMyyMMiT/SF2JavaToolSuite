/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation;

import com.sfc.sf2.battlesprite.BattleSprite.BattleSpriteType;
import com.sfc.sf2.battlesprite.animation.gui.BattleSpriteAnimationLayoutPanel;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimation {
        
    private BattleSpriteType type;    
    private BattleSpriteAnimationFrame[] frames;
    
    private int frameNumber;
    private int spellInitFrame;
    private int spellAnim;
    private int endSpellAnim;
    
    private int idle1WeaponFrame;
    private int idle1WeaponZ;
    private int idle1WeaponX;
    private int idle1WeaponY;   
    
    private BattleSpriteAnimationLayoutPanel layout;

    /**
     * For creating Ally animations
     */
    public BattleSpriteAnimation(BattleSpriteAnimationFrame[] frames, int frameNumber, int spellInitFrame, int spellAnim, int endSpellAnim, int idle1WeaponFrame, int idle1WeaponZ, int idle1WeaponX, int idle1WeaponY) {
        this.type = BattleSpriteType.ALLY;
        this.frames = frames;
        this.frameNumber = frameNumber;
        this.spellInitFrame = spellInitFrame;
        this.spellAnim = spellAnim;
        this.endSpellAnim = endSpellAnim;
        this.idle1WeaponFrame = idle1WeaponFrame;
        this.idle1WeaponZ = idle1WeaponZ;
        this.idle1WeaponX = idle1WeaponX;
        this.idle1WeaponY = idle1WeaponY;
        layout = null;
    }
    
    /**
     * For creating Enemy animations
     */
    public BattleSpriteAnimation(BattleSpriteAnimationFrame[] frames, int frameNumber, int spellInitFrame, int spellAnim, int endSpellAnim) {
        this.type = BattleSpriteType.ENEMY;
        this.frames = frames;
        this.frameNumber = frameNumber;
        this.spellInitFrame = spellInitFrame;
        this.spellAnim = spellAnim;
        this.endSpellAnim = endSpellAnim;
        this.idle1WeaponFrame = -1;
        this.idle1WeaponZ = -1;
        this.idle1WeaponX = -1;
        this.idle1WeaponY = -1;
        layout = null;
    }

    public BattleSpriteType getType() {
        return type;
    }

    public void setType(BattleSpriteType type) {
        this.type = type;
    }

    public BattleSpriteAnimationFrame[] getFrames() {
        return frames;
    }

    public void setFrames(BattleSpriteAnimationFrame[] frames) {
        this.frames = frames;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public int getSpellInitFrame() {
        return spellInitFrame;
    }

    public void setSpellInitFrame(int spellInitFrame) {
        this.spellInitFrame = spellInitFrame;
    }

    public int getSpellAnim() {
        return spellAnim;
    }

    public void setSpellAnim(int spellAnim) {
        this.spellAnim = spellAnim;
    }

    public int getEndSpellAnim() {
        return endSpellAnim;
    }

    public void setEndSpellAnim(int endSpellAnim) {
        this.endSpellAnim = endSpellAnim;
    }

    public int getIdle1WeaponFrame() {
        return idle1WeaponFrame;
    }

    public void setIdle1WeaponFrame(int idle1WeaponFrame) {
        this.idle1WeaponFrame = idle1WeaponFrame;
    }

    public int getIdle1WeaponZ() {
        return idle1WeaponZ;
    }

    public void setIdle1WeaponZ(int idle1WeaponZ) {
        this.idle1WeaponZ = idle1WeaponZ;
    }

    public int getIdle1WeaponX() {
        return idle1WeaponX;
    }

    public void setIdle1WeaponX(int idle1WeaponX) {
        this.idle1WeaponX = idle1WeaponX;
    }

    public int getIdle1WeaponY() {
        return idle1WeaponY;
    }

    public void setIdle1WeaponY(int idle1WeaponY) {
        this.idle1WeaponY = idle1WeaponY;
    }

    public BattleSpriteAnimationLayoutPanel getLayout() {
        return layout;
    }

    public void setLayout(BattleSpriteAnimationLayoutPanel layout) {
        this.layout = layout;
    }
}
