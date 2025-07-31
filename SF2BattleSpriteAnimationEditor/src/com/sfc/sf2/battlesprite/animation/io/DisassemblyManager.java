/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation.io;

import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimation;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimationFrame;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class DisassemblyManager {
    
    public static BattleSpriteAnimation importDisassembly(String filepath, int animationType){
        System.out.println("com.sfc.sf2.battlespriteanimation.io.DisassemblyManager.importDisassembly() - Importing disassembly file ...");
        
        BattleSpriteAnimation battlespriteanimation = new BattleSpriteAnimation();
        try{
            Path path = Paths.get(filepath);
            if(path.toFile().exists()){
                byte[] data = Files.readAllBytes(path);
                
                if(animationType == BattleSpriteAnimation.TYPE_ALLY){
                
                    battlespriteanimation.setFrameNumber(getNextByte(data,0));
                    battlespriteanimation.setSpellInitFrame(getNextByte(data,1));
                    battlespriteanimation.setSpellAnim(getNextByte(data,2));
                    battlespriteanimation.setEndSpellAnim(getNextByte(data,3));
                    battlespriteanimation.setIdle1WeaponFrame(getNextByte(data,4));
                    battlespriteanimation.setIdle1WeaponZ(getNextByte(data,5));
                    battlespriteanimation.setIdle1WeaponX(getNextByte(data,6));
                    battlespriteanimation.setIdle1WeaponY(getNextByte(data,7));

                    BattleSpriteAnimationFrame[] frames = new BattleSpriteAnimationFrame[battlespriteanimation.getFrameNumber()];

                    for(int i=0;i<frames.length;i++){
                        BattleSpriteAnimationFrame frame = new BattleSpriteAnimationFrame();

                        frame.setIndex(getNextByte(data,8+i*8+0));
                        frame.setDuration(getNextByte(data,8+i*8+1));
                        frame.setX(getNextByte(data,8+i*8+2));
                        frame.setY(getNextByte(data,8+i*8+3));
                        frame.setWeaponFrame(getNextByte(data,8+i*8+4));
                        frame.setWeaponZ(getNextByte(data,8+i*8+5));
                        frame.setWeaponX(getNextByte(data,8+i*8+6));
                        frame.setWeaponY(getNextByte(data,8+i*8+7));

                        frames[i] = frame;
                    }

                    battlespriteanimation.setFrames(frames);
                    
                }else{
                    
                    battlespriteanimation.setFrameNumber(getNextByte(data,0));
                    battlespriteanimation.setSpellInitFrame(getNextByte(data,1));
                    battlespriteanimation.setSpellAnim(getNextByte(data,2));
                    battlespriteanimation.setEndSpellAnim(getNextByte(data,3));

                    BattleSpriteAnimationFrame[] frames = new BattleSpriteAnimationFrame[battlespriteanimation.getFrameNumber()];

                    for(int i=0;i<frames.length;i++){
                        BattleSpriteAnimationFrame frame = new BattleSpriteAnimationFrame();

                        frame.setIndex(getNextByte(data,4+i*4+0));
                        frame.setDuration(getNextByte(data,4+i*4+1));
                        frame.setX(getNextByte(data,4+i*4+2));
                        frame.setY(getNextByte(data,4+i*4+3));

                        frames[i] = frame;
                    }

                    battlespriteanimation.setFrames(frames);
                    
                }
                
                
            }            
        }catch(Exception e){
             System.err.println("com.sfc.sf2.battlespriteanimation.io.DisassemblyManager.parseGraphics() - Error while parsing graphics data : "+e);
             e.printStackTrace();
        }    
        System.out.println("com.sfc.sf2.battlespriteanimation.io.DisassemblyManager.importDisassembly() - Disassembly imported.");
        return battlespriteanimation;
    }
    
    public static void exportDisassembly(BattleSpriteAnimation anim, String filepath, int animationType){
        System.out.println("com.sfc.sf2.battlespriteanimation.io.DisassemblyManager.exportDisassembly() - Exporting disassembly ...");
        try{
            byte[] animationFileBytes;
            if(animationType == BattleSpriteAnimation.TYPE_ALLY){
                int frameNumber = anim.getFrames().length;
                animationFileBytes = new byte[8 + frameNumber*8];
            
                animationFileBytes[0] = (byte)(frameNumber);
                animationFileBytes[1] = (byte)(anim.getSpellInitFrame());
                animationFileBytes[2] = (byte)(anim.getSpellAnim());
                animationFileBytes[3] = (byte)(anim.getEndSpellAnim());
                animationFileBytes[4] = (byte)(anim.getIdle1WeaponFrame());
                animationFileBytes[5] = (byte)(anim.getIdle1WeaponZ());
                animationFileBytes[6] = (byte)(anim.getIdle1WeaponX());
                animationFileBytes[7] = (byte)(anim.getIdle1WeaponY());
                
                for(int i=0;i<frameNumber;i++){
                    animationFileBytes[8+i*8+0] = (byte)(anim.getFrames()[i].getIndex());
                    animationFileBytes[8+i*8+1] = (byte)(anim.getFrames()[i].getDuration());
                    animationFileBytes[8+i*8+2] = (byte)(anim.getFrames()[i].getX());
                    animationFileBytes[8+i*8+3] = (byte)(anim.getFrames()[i].getY());
                    animationFileBytes[8+i*8+4] = (byte)(anim.getFrames()[i].getWeaponFrame());
                    animationFileBytes[8+i*8+5] = (byte)(anim.getFrames()[i].getWeaponZ());
                    animationFileBytes[8+i*8+6] = (byte)(anim.getFrames()[i].getWeaponX());
                    animationFileBytes[8+i*8+7] = (byte)(anim.getFrames()[i].getWeaponY());
                }                
            }else{
                int frameNumber = anim.getFrames().length;
                animationFileBytes = new byte[4 + frameNumber*4];
            
                animationFileBytes[0] = (byte)(frameNumber);
                animationFileBytes[1] = (byte)(anim.getSpellInitFrame());
                animationFileBytes[2] = (byte)(anim.getSpellAnim());
                animationFileBytes[3] = (byte)(anim.getEndSpellAnim());
                
                for(int i=0;i<frameNumber;i++){
                    animationFileBytes[4+i*4+0] = (byte)(anim.getFrames()[i].getIndex());
                    animationFileBytes[4+i*4+1] = (byte)(anim.getFrames()[i].getDuration());
                    animationFileBytes[4+i*4+2] = (byte)(anim.getFrames()[i].getX());
                    animationFileBytes[4+i*4+3] = (byte)(anim.getFrames()[i].getY());
                }    
            }


            Path animFilePath = Paths.get(filepath);
            Files.write(animFilePath,animationFileBytes);
            System.out.println(animationFileBytes.length + " bytes into " + animFilePath);   

        } catch (Exception ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.out.println(ex);
        }  
        System.out.println("com.sfc.sf2.battlespriteanimation.io.DisassemblyManager.exportDisassembly() - Disassembly exported.");        
    }     
    
    private static short getNextWord(byte[] data, int cursor){
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(data[cursor+1]);
        bb.put(data[cursor]);
        short s = bb.getShort(0);
        return s;
    }
    
    private static byte getNextByte(byte[] data, int cursor){
        ByteBuffer bb = ByteBuffer.allocate(1);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(data[cursor]);
        byte b = bb.get(0);
        return b;
    }    

    
}
