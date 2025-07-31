/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellAnimation.io;

import com.sfc.sf2.spellAnimation.SpellAnimation;
import com.sfc.sf2.spellAnimation.SpellAnimationFrame;
import com.sfc.sf2.spellAnimation.SpellSubAnimation;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TiMMy
 */
public class DisassemblyManager {
    
    private static String header;

    public static SpellAnimation importDisassembly(String spellAnimationPath) throws IOException {
        SpellAnimation spellAnimation = null;
        System.out.println("com.sfc.sf2.spellAnimation.io.DisassemblyManager.importDisassembly() - Importing disassembly file ...");
        try {
            header = "";
            boolean isHeader = true;
            File file = new File(spellAnimationPath);
            Scanner scan = new Scanner(file);
            header = "";
            byte frameIndex = -1;
            List<SpellSubAnimation> subAnimations = new ArrayList();
            SpellSubAnimation spellSubAnimation = null;
            List<SpellAnimationFrame> animationFrames = null;
            while(scan.hasNext()) {
                String line = scan.nextLine().trim();
                short newLineVal;
                if (line.length() > 2 && !line.startsWith(";")) {
                    isHeader = false;
                    if (!line.startsWith("dc.")) {
                        //Store previous animation
                        if (spellSubAnimation != null) {
                            SpellAnimationFrame[] frames = new SpellAnimationFrame[animationFrames.size()];
                            frames = animationFrames.toArray(frames);
                            spellSubAnimation.setFrames(frames);
                        }
                        //Setup next animation
                        frameIndex = 0;
                        spellSubAnimation = new SpellSubAnimation();
                        spellSubAnimation.setName(line.substring(0, line.indexOf(":")).trim());
                        subAnimations.add(spellSubAnimation);
                        animationFrames = new ArrayList();
                        line = line.substring(line.indexOf("dc.")+4).trim();
                        String line2 = scan.nextLine();
                        line2 = line2.substring(line2.indexOf("dc.")+4).trim();
                        newLineVal = CombineBytesToWord(valueOf(line), valueOf(line2));
                    } else {
                        frameIndex++;
                        line = line.substring(line.indexOf("dc.")+4).trim();
                        String line2 = scan.nextLine();
                        line2 = line2.substring(line2.indexOf("dc.")+4).trim();
                        newLineVal = CombineBytesToWord(valueOf(line), valueOf(line2));
                    }
                    
                    SpellAnimationFrame frame = new SpellAnimationFrame();
                    frame.setFrameIndex(frameIndex);
                    frame.setX(newLineVal);
                    frame.setY(getNextWordLine(scan));
                    frame.setTileIndex((short)(getNextWordLine(scan) - 0x520));
                    int flags = getNextWordLine(scan);
                    frame.setW((byte)(((flags >> 8) & 3)+1));
                    frame.setH((byte)(((flags >> 10) & 3)+1));
                    frame.setForeground((byte)(flags & 0xFF));
                    animationFrames.add(frame);
                    
                } else if (isHeader) {
                    header += line + "\n";
                }
            }   
            
            if (spellSubAnimation != null) {
                SpellAnimationFrame[] frames = new SpellAnimationFrame[animationFrames.size()];
                frames = animationFrames.toArray(frames);
                spellSubAnimation.setFrames(frames);
            }
            spellAnimation = new SpellAnimation();
            SpellSubAnimation[] subAnims = new SpellSubAnimation[subAnimations.size()];
            subAnims = subAnimations.toArray(subAnims);
            spellAnimation.setSpellSubAnimations(subAnims);
            
        } catch (Exception e) {
             System.err.println("com.sfc.sf2.spellAnimation.io.DisassemblyManager.parseGraphics() - Error while parsing animation data : "+e);
             e.printStackTrace();
        }    
        System.out.println("com.sfc.sf2.spellAnimation.io.DisassemblyManager.importDisassembly() - Disassembly imported.");
        return spellAnimation;
    }
    
    public static void exportDisassembly(SpellAnimation anim, String filepath) {
        System.out.println("com.sfc.sf2.spellAnimation.io.DisassemblyManager.exportDisassembly() - Exporting disassembly ...");
        try {
            /*byte[] animationFileBytes;
            int frameNumber = anim.getFrames().length;
            animationFileBytes = new byte[8 + frameNumber*8];

            animationFileBytes[0] = (byte)(frameNumber);
            animationFileBytes[1] = (byte)(anim.getSpellInitFrame());
            animationFileBytes[2] = (byte)(anim.getSpellAnim());
            animationFileBytes[3] = (byte)(anim.getEndSpellAnim());

            for(int i=0;i<frameNumber;i++){
                animationFileBytes[8+i*8+0] = (byte)(anim.getFrames()[i].getFrameIndex());
                animationFileBytes[8+i*8+1] = (byte)(anim.getFrames()[i].getDuration());
                animationFileBytes[8+i*8+2] = (byte)(anim.getFrames()[i].getX());
                animationFileBytes[8+i*8+3] = (byte)(anim.getFrames()[i].getY());
            }
            Path animFilePath = Paths.get(filepath);
            Files.write(animFilePath,animationFileBytes);
            System.out.println(animationFileBytes.length + " bytes into " + animFilePath); */  
        } catch (Exception ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.out.println(ex);
        }  
        System.out.println("com.sfc.sf2.spellAnimation.io.DisassemblyManager.exportDisassembly() - Disassembly exported.");        
    }
    
    /**
     * Reads 2 lines and concatenates the data (a word) and then returns the data
     * Temporary code until animation format is made readable
     */
    private static short getNextWordLine(Scanner scan) {
        if (scan.hasNext()) {
            String line1 = scan.nextLine();
            line1 = line1.substring(line1.indexOf("dc.")+4).trim();
            String line2 = scan.nextLine();
            line2 = line2.substring(line2.indexOf("dc.")+4).trim();
            return CombineBytesToWord(valueOf(line1), valueOf(line2));
        }
        
        return -1;
    }
    
    /**
     * Temporary code until animation format is made readable
     */
    private static short CombineBytesToWord(int value1, int value2) {
        return (short)(((value1 << 8) & 0xFF00) + (value2 & 0xFF));
    }
    
    /*private static short getNextWord(byte[] data, int cursor){
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(data[cursor+1]);
        bb.put(data[cursor]);
        short s = bb.getShort(0);
        return s;
    }*/
    
    private static int valueOf(String s) {
        s = s.trim();
        if (s.startsWith("equ")) {
            s = s.substring(3).trim();
        }
        if (s.startsWith("$")) {
            return Integer.parseInt(s.substring(1), 16);
        } else {
            return Integer.parseInt(s);
        }
    }
}
