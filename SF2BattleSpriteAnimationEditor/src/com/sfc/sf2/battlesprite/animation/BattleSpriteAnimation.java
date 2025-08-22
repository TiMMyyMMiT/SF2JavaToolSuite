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
    
    private byte spellInitFrame;
    private byte spellAnim;
    private boolean endSpellAnim; 
    
    private BattleSpriteAnimationLayoutPanel layout;

    public BattleSpriteAnimation(BattleSpriteAnimationFrame[] frames, byte spellInitFrame, byte spellAnim, boolean endSpellAnim) {
        this.type = BattleSpriteType.ENEMY;
        this.frames = frames;
        this.spellInitFrame = spellInitFrame;
        this.spellAnim = spellAnim;
        this.endSpellAnim = endSpellAnim;
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

    public byte getFrameCount() {
        return (byte)frames.length;
    }

    public byte getSpellInitFrame() {
        return spellInitFrame;
    }

    public void setSpellInitFrame(byte spellInitFrame) {
        this.spellInitFrame = spellInitFrame;
    }

    public byte getSpellAnim() {
        return spellAnim;
    }

    public void setSpellAnim(byte spellAnim) {
        this.spellAnim = spellAnim;
    }

    public boolean getEndSpellAnim() {
        return endSpellAnim;
    }

    public void setEndSpellAnim(boolean endSpellAnim) {
        this.endSpellAnim = endSpellAnim;
    }

    public BattleSpriteAnimationLayoutPanel getLayout() {
        return layout;
    }

    public void setLayout(BattleSpriteAnimationLayoutPanel layout) {
        this.layout = layout;
    }
    
    @Override
    public BattleSpriteAnimation clone() {
        return new BattleSpriteAnimation(frames, spellInitFrame, spellAnim, endSpellAnim);
    }
        
    public static BattleSpriteAnimation EmptyAnimation() {
        BattleSpriteAnimationFrame[] frames = new BattleSpriteAnimationFrame[] { BattleSpriteAnimationFrame.EmptyFrame() };
        return new BattleSpriteAnimation(frames, (byte)0, (byte)0, true);
    }
}
