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
            byte frameCount = BinaryHelpers.getByte(data, 0);
            byte initFrame = BinaryHelpers.getByte(data, 1);
            byte spellAnim = BinaryHelpers.getByte(data, 2);
            boolean endSpellAnim = BinaryHelpers.getByte(data, 3) != 0;
            //byte idle1WeaponFrame = BinaryHelpers.getByte(data, 4);
            //byte idle1WeaponZ = BinaryHelpers.getByte(data, 5);
            //byte idle1WeaponX = BinaryHelpers.getByte(data, 6);
            //byte idle1WeaponY = BinaryHelpers.getByte(data, 7);
            
            BattleSpriteAnimationFrame[] frames = new BattleSpriteAnimationFrame[frameCount];
            for (byte i=0; i < frames.length; i++) {
                byte index = BinaryHelpers.getByte(data, 8+i*8+0);
                byte duration = BinaryHelpers.getByte(data, 8+i*8+1);
                byte x = BinaryHelpers.getByte(data, 8+i*8+2);
                byte y = BinaryHelpers.getByte(data, 8+i*8+3);
                byte weaponData = BinaryHelpers.getByte(data, 8+i*8+4);
                byte weaponFrame = (byte)(weaponData&0xF);
                boolean weaponFlipX = (weaponData&0x10) != 0;
                boolean weaponFlipY = (weaponData&0x20) != 0;
                boolean behind = BinaryHelpers.getByte(data, 8+i*8+5) != 0;
                byte weaponX = BinaryHelpers.getByte(data, 8+i*8+6);
                byte weaponY = BinaryHelpers.getByte(data, 8+i*8+7);                
                frames[i] = new BattleSpriteAnimationFrame(index, duration, x, y, weaponFrame, weaponFlipX, weaponFlipY, behind, weaponX, weaponY);
            }
            return new BattleSpriteAnimation(frames, initFrame, spellAnim, endSpellAnim);
        } else {
            byte frameCount = BinaryHelpers.getByte(data, 0);
            byte initFrame = BinaryHelpers.getByte(data, 1);
            byte spellAnim = BinaryHelpers.getByte(data, 2);
            boolean endSpellAnim = BinaryHelpers.getByte(data, 3) != 0;

            BattleSpriteAnimationFrame[] frames = new BattleSpriteAnimationFrame[frameCount];
            for (byte i=0; i < frames.length; i++) {
                byte index = BinaryHelpers.getByte(data, 4+i*4+0);
                byte duration = BinaryHelpers.getByte(data, 4+i*4+1);
                byte x = BinaryHelpers.getByte(data, 4+i*4+2);
                byte y = BinaryHelpers.getByte(data, 4+i*4+3);
                frames[i] = new BattleSpriteAnimationFrame(index, duration, x, y);
            }
            return new BattleSpriteAnimation(frames, initFrame, spellAnim, endSpellAnim);
        }
    }

    @Override
    protected byte[] packageDisassemblyData(BattleSpriteAnimation item, BattleSpriteAnimationPackage pckg) throws DisassemblyException {
        
        if (pckg.type() == BattleSprite.BattleSpriteType.ALLY) {
            byte frameCount = (byte)item.getFrameCount();
            byte[] animationFileBytes = new byte[8 + frameCount*8];

            animationFileBytes[0] = frameCount;
            animationFileBytes[1] = item.getSpellInitFrame();
            animationFileBytes[2] = item.getSpellAnim();
            animationFileBytes[3] = (byte)(item.getEndSpellAnim() ? 1 : 0);
            //animationFileBytes[4] = item.getIdle1WeaponFrame();
            //animationFileBytes[5] = item.getIdle1WeaponZ();
            //animationFileBytes[6] = item.getIdle1WeaponX();
            //animationFileBytes[7] = item.getIdle1WeaponY();

            for (byte i=0; i < frameCount; i++) {
                BattleSpriteAnimationFrame frame = item.getFrames()[i];
                animationFileBytes[8+i*8+0] = frame.getIndex();
                animationFileBytes[8+i*8+1] = frame.getDuration();
                animationFileBytes[8+i*8+2] = frame.getX();
                animationFileBytes[8+i*8+3] = frame.getY();
                byte weaponData = (byte)(frame.getWeaponFrame() + (frame.getWeaponFlipH() ? 0x10 : 0) + (frame.getWeaponFlipV() ? 0x20 : 0));
                animationFileBytes[8+i*8+4] = weaponData;
                animationFileBytes[8+i*8+5] = (byte)(frame.getWeaponBehind() ? 1 : 0);
                animationFileBytes[8+i*8+6] = frame.getWeaponX();
                animationFileBytes[8+i*8+7] = frame.getWeaponY();
            }
            return animationFileBytes;
        } else {
            byte frameCount = (byte)item.getFrameCount();
            byte[] animationFileBytes = new byte[4 + frameCount*4];

            animationFileBytes[0] = frameCount;
            animationFileBytes[1] = item.getSpellInitFrame();
            animationFileBytes[2] = item.getSpellAnim();
            animationFileBytes[3] = (byte)(item.getEndSpellAnim() ? 1 : 0);

            for (byte i=0; i < frameCount; i++) {
                BattleSpriteAnimationFrame frame = item.getFrames()[i];
                animationFileBytes[4+i*4+0] = frame.getIndex();
                animationFileBytes[4+i*4+1] = frame.getDuration();
                animationFileBytes[4+i*4+2] = frame.getX();
                animationFileBytes[4+i*4+3] = frame.getY();
            }
            return animationFileBytes;
        }
    }
}
