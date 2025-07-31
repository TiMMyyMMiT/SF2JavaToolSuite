/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.animation.io;

import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.animation.MapAnimationFrame;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class DisassemblyManager {
 
    private static String animationsHeader;

    
    private static short getNextWord(byte[] data, int cursor){
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(data[cursor+1]);
        bb.put(data[cursor]);
        short s = bb.getShort(0);
        //System.out.println("Next input word = $"+Integer.toString(s, 16)+" / "+Integer.toString(s, 2));
        return s;
    }    
  
    public static MapAnimation importAnimation(String animationPath){
        System.out.println("com.sfc.sf2.map.io.DisassemblyManager.importAnimation() - Importing disassembly ...");
        MapAnimation anim = new MapAnimation();
        try {
            MapAnimationFrame[] frames = null;
            List<MapAnimationFrame> frameList = new ArrayList();
            if(animationPath.endsWith(".asm")){
                File file = new File(animationPath);
                Scanner scan = new Scanner(file);
                MapAnimationFrame entry = null;
                boolean inHeader = true;
                animationsHeader = "";
                while(scan.hasNext()){
                    String line = scan.nextLine();
                    if(line.trim().startsWith("mapAnimation")){
                        inHeader = false;
                        String[] params = line.trim().substring("mapAnimation".length()).trim().split(",");
                        anim.setTileset(valueOf(params[0].trim()));
                        anim.setLength(valueOf(params[1].trim()));
                    }else if(line.trim().startsWith("mapAnimEntry")){
                        entry = new MapAnimationFrame();
                        frameList.add(entry);
                        String[] params = line.trim().substring("mapAnimEntry".length()).trim().split(",");
                        entry.setStart(valueOf(params[0].trim()));
                        entry.setLength(valueOf(params[1].trim()));
                        entry.setDest(valueOf(params[2].trim()));
                        entry.setDelay(valueOf(params[3].trim()));
                    }else{
                        if(inHeader){
                            animationsHeader+=line;
                            animationsHeader+="\n";
                        }
                    }
                }  
                
            }else{  
                int cursor = 0;
                Path animpath = Paths.get(animationPath);
                byte[] data = Files.readAllBytes(animpath);
                anim.setTileset(getNextWord(data,cursor+0));
                anim.setLength(getNextWord(data,cursor+2));
                cursor = 4;
                while(true){
                    int start = getNextWord(data,cursor+0);
                    if(start == -1 || (cursor+8) > data.length){
                        break;
                    }
                    MapAnimationFrame frame = new MapAnimationFrame();
                    frame.setStart(getNextWord(data,cursor+0));
                    frame.setLength(getNextWord(data,cursor+2));
                    frame.setDest(getNextWord(data,cursor+4));
                    frame.setDelay(getNextWord(data,cursor+6));
                    frameList.add(frame);
                    cursor += 8;
                }
            }
            
            frames = new MapAnimationFrame[frameList.size()];
            frames = frameList.toArray(frames);
            
            anim.setFrames(frames);
            
        } catch (IOException ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("com.sfc.sf2.map.io.DisassemblyManager.importAnimation() - Disassembly imported.");  
        return anim;
    }
    
    public static void exportAnimation(MapAnimation animation, String animationPath){
        System.out.println("com.sfc.sf2.map.io.DisassemblyManager.exportAnimation() - Exporting disassembly ...");
        try { 
            if(animationPath.endsWith(".asm")){
                StringBuilder asm = new StringBuilder();
                asm.append(animationsHeader);
                asm.append(produceAnimationAsm(animation));
                Path path = Paths.get(animationPath);
                Files.write(path,asm.toString().getBytes());
                System.out.println(asm);
            }else{
                byte[] animBytes = produceAnimBytes(animation);
                Path animFilepath = Paths.get(animationPath);
                Files.write(animFilepath,animBytes);
                System.out.println(animBytes.length + " bytes into " + animFilepath);
            }
        } catch (Exception ex) {
            Logger.getLogger(com.sfc.sf2.map.layout.io.DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.out.println(ex);
        }            
        System.out.println("com.sfc.sf2.map.io.DisassemblyManager.exportAnimation() - Disassembly exported.");     
    }
   
    private static String produceAnimationAsm(MapAnimation animation){
        StringBuilder asm = new StringBuilder();
        asm.append("                "+"mapAnimation"+" "+animation.getTileset()+", "+animation.getLength()+"\n");
        for(int i=0;i<animation.getFrames().length;i++){
            MapAnimationFrame frame = animation.getFrames()[i];
            asm.append("                "+"  mapAnimEntry"+" "+frame.getStart()+", "+frame.getLength()+", "+frame.getDest()+", "+frame.getDelay()+"\n");
        }
        asm.append("                "+"endWord"+"\n");
        return asm.toString();
    } 
    
    private static byte[] produceAnimBytes(MapAnimation anim){
        byte[] animBytes = new byte[anim.getFrames().length*8+6];
        animBytes[0] = (byte)((anim.getTileset()&0xFF00)>>8);
        animBytes[1] = (byte)(anim.getTileset()&0xFF);
        animBytes[2] = (byte)((anim.getLength()&0xFF00)>>8);
        animBytes[3] = (byte)(anim.getLength()&0xFF);
        for(int i=0;i<anim.getFrames().length;i++){
            MapAnimationFrame frame = anim.getFrames()[i];
            animBytes[4+i*8] = (byte)((frame.getStart()&0xFF00)>>8);
            animBytes[4+i*8+1] = (byte)((frame.getStart()&0xFF));
            animBytes[4+i*8+2] = (byte)((frame.getLength()&0xFF00)>>8);
            animBytes[4+i*8+3] = (byte)((frame.getLength()&0xFF));
            animBytes[4+i*8+4] = (byte)((frame.getDest()&0xFF00)>>8);
            animBytes[4+i*8+5] = (byte)((frame.getDest()&0xFF));
            animBytes[4+i*8+6] = (byte)((frame.getDelay()&0xFF00)>>8);
            animBytes[4+i*8+7] = (byte)((frame.getDelay()&0xFF));
        }
        animBytes[animBytes.length-2] = -1;
        animBytes[animBytes.length-1] = -1;
        return animBytes;
    }   
    
    private static int valueOf(String s){
        s = s.trim();
        if(s.startsWith("$")){
            return Integer.valueOf(s.substring(1),16);
        }else{
            return Integer.valueOf(s);
        }
    }
    
}
