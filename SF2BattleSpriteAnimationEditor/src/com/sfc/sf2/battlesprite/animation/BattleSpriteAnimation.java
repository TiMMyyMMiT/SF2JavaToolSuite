/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation;

import com.sfc.sf2.battlesprite.BattleSprite;
import com.sfc.sf2.battlesprite.BattleSprite.BattleSpriteType;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimation {
        
    private BattleSprite battleSprite;
    private BattleSpriteAnimationFrame[] frames;
    
    private byte spellInitFrame;
    private byte spellAnim;
    private boolean endSpellAnim;

    public BattleSpriteAnimation(BattleSprite battleSprite, BattleSpriteAnimationFrame[] frames, byte spellInitFrame, byte spellAnim, boolean endSpellAnim) {
        this.battleSprite = battleSprite;
        this.spellInitFrame = spellInitFrame;
        this.spellAnim = spellAnim;
        this.endSpellAnim = endSpellAnim;
        setFrames(frames);
    }

    public BattleSprite getBattleSprite() {
        return battleSprite;
    }

    public BattleSpriteType getType() {
        return battleSprite.getType();
    }

    public BattleSpriteAnimationFrame[] getFrames() {
        return frames;
    }

    public void setFrames(BattleSpriteAnimationFrame[] frames) {
        this.frames = frames;
        if (frames != null) {
            for (int i = 0; i < frames.length; i++) {
                frames[i].setBattleSpriteAnim(this);
            }
        }
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
    
    @Override
    public BattleSpriteAnimation clone() {
        return new BattleSpriteAnimation(battleSprite, frames, spellInitFrame, spellAnim, endSpellAnim);
    }
        
    public static BattleSpriteAnimation EmptyAnimation(BattleSprite battleSprite) {
        BattleSpriteAnimationFrame[] frames = new BattleSpriteAnimationFrame[] { BattleSpriteAnimationFrame.EmptyFrame() };
        return new BattleSpriteAnimation(battleSprite, frames, (byte)0, (byte)0, true);
    }
}
