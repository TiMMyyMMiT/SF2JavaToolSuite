/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation.io;

import com.sfc.sf2.battlesprite.BattleSprite;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimation;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimationFrame;
import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.helpers.BinaryHelpers;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimationDisassemblyProcessor extends AbstractDisassemblyProcessor<BattleSpriteAnimation, BattleSpriteAnimationPackage> {

    @Override
    protected BattleSpriteAnimation parseDisassemblyData(byte[] data, BattleSpriteAnimationPackage pckg) throws DisassemblyException {
        
        if (pckg.type() == BattleSprite.BattleSpriteType.ALLY) {
            int frameNumber = BinaryHelpers.getByte(data, 0);
            int initFrame = BinaryHelpers.getByte(data, 1);
            int spellAnim = BinaryHelpers.getByte(data, 2);
            int endSpellAnim = BinaryHelpers.getByte(data, 3);
            int idle1WeaponFrame = BinaryHelpers.getByte(data, 4);
            int idle1WeaponZ = BinaryHelpers.getByte(data, 5);
            int idle1WeaponX = BinaryHelpers.getByte(data, 6);
            int idle1WeaponY = BinaryHelpers.getByte(data, 7);
            
            BattleSpriteAnimationFrame[] frames = new BattleSpriteAnimationFrame[frameNumber];
            for (int i=0; i < frames.length; i++) {
                int index = BinaryHelpers.getByte(data, 8+i*8+0);
                int duration = BinaryHelpers.getByte(data, 8+i*8+1);
                int x = BinaryHelpers.getByte(data, 8+i*8+2);
                int y = BinaryHelpers.getByte(data, 8+i*8+3);
                int weaponFrame = BinaryHelpers.getByte(data, 8+i*8+4);
                int weaponZ = BinaryHelpers.getByte(data, 8+i*8+5);
                int weaponX = BinaryHelpers.getByte(data, 8+i*8+6);
                int weaponY = BinaryHelpers.getByte(data, 8+i*8+7);
                frames[i] = new BattleSpriteAnimationFrame(index, duration, x, y, weaponFrame, weaponZ, weaponX, weaponY);
            }
            return new BattleSpriteAnimation(frames, frameNumber, initFrame, spellAnim, endSpellAnim, idle1WeaponFrame, idle1WeaponZ, idle1WeaponX, idle1WeaponY);
        } else {
            int frameNumber = BinaryHelpers.getByte(data, 0);
            int initFrame = BinaryHelpers.getByte(data, 1);
            int spellAnim = BinaryHelpers.getByte(data, 2);
            int endSpellAnim = BinaryHelpers.getByte(data, 3);

            BattleSpriteAnimationFrame[] frames = new BattleSpriteAnimationFrame[frameNumber];
            for (int i=0; i < frames.length; i++) {
                int index = BinaryHelpers.getByte(data, 4+i*4+0);
                int duration = BinaryHelpers.getByte(data, 4+i*4+1);
                int x = BinaryHelpers.getByte(data, 4+i*4+2);
                int y = BinaryHelpers.getByte(data, 4+i*4+3);
                frames[i] = new BattleSpriteAnimationFrame(index, duration, x, y);
            }
            return new BattleSpriteAnimation(frames, frameNumber, initFrame, spellAnim, endSpellAnim);
        }
    }

    @Override
    protected byte[] packageDisassemblyData(BattleSpriteAnimation item, BattleSpriteAnimationPackage pckg) throws DisassemblyException {
        
        if (pckg.type() == BattleSprite.BattleSpriteType.ALLY) {
            int frameNumber = item.getFrames().length;
            byte[] animationFileBytes = new byte[8 + frameNumber*8];

            animationFileBytes[0] = (byte)(frameNumber);
            animationFileBytes[1] = (byte)(item.getSpellInitFrame());
            animationFileBytes[2] = (byte)(item.getSpellAnim());
            animationFileBytes[3] = (byte)(item.getEndSpellAnim());
            animationFileBytes[4] = (byte)(item.getIdle1WeaponFrame());
            animationFileBytes[5] = (byte)(item.getIdle1WeaponZ());
            animationFileBytes[6] = (byte)(item.getIdle1WeaponX());
            animationFileBytes[7] = (byte)(item.getIdle1WeaponY());

            for (int i=0; i < frameNumber; i++) {
                animationFileBytes[8+i*8+0] = (byte)(item.getFrames()[i].getIndex());
                animationFileBytes[8+i*8+1] = (byte)(item.getFrames()[i].getDuration());
                animationFileBytes[8+i*8+2] = (byte)(item.getFrames()[i].getX());
                animationFileBytes[8+i*8+3] = (byte)(item.getFrames()[i].getY());
                animationFileBytes[8+i*8+4] = (byte)(item.getFrames()[i].getWeaponFrame());
                animationFileBytes[8+i*8+5] = (byte)(item.getFrames()[i].getWeaponZ());
                animationFileBytes[8+i*8+6] = (byte)(item.getFrames()[i].getWeaponX());
                animationFileBytes[8+i*8+7] = (byte)(item.getFrames()[i].getWeaponY());
            }
            return animationFileBytes;
        } else {
            int frameNumber = item.getFrames().length;
            byte[] animationFileBytes = new byte[4 + frameNumber*4];

            animationFileBytes[0] = (byte)(frameNumber);
            animationFileBytes[1] = (byte)(item.getSpellInitFrame());
            animationFileBytes[2] = (byte)(item.getSpellAnim());
            animationFileBytes[3] = (byte)(item.getEndSpellAnim());

            for (int i=0; i < frameNumber; i++) {
                animationFileBytes[4+i*4+0] = (byte)(item.getFrames()[i].getIndex());
                animationFileBytes[4+i*4+1] = (byte)(item.getFrames()[i].getDuration());
                animationFileBytes[4+i*4+2] = (byte)(item.getFrames()[i].getX());
                animationFileBytes[4+i*4+3] = (byte)(item.getFrames()[i].getY());
            }
            return animationFileBytes;
        }
    }
}
