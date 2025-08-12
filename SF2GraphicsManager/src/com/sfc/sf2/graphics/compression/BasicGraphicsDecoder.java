/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.compression;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.helpers.BinaryHelpers;
import com.sfc.sf2.palette.Palette;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wiz
 */
public class BasicGraphicsDecoder extends AbstractGraphicsDecoder {
    
    @Override
    public Tile[] decode(byte[] input, Palette palette) {
        Console.logger().finest("ENTERING decode");
        Console.logger().finest("Data length = " + input.length + " bytes.");
        List<Byte> output = new ArrayList();
        boolean done = false;
        int pointer = 0;
        Tile[] tiles = null;
        while(!done){
            ByteBuffer bbCommand = ByteBuffer.allocate(2);
            bbCommand.order(ByteOrder.LITTLE_ENDIAN);
            bbCommand.put(input[pointer+1]);
            bbCommand.put(input[pointer]);
            short commands = bbCommand.getShort(0);
            pointer+=2;
            //Console.logger().finest("commands = " + Integer.toHexString(commands&0xFFFF)); 
            for(int i=0;i<16;i++){
                if((commands & (1<<15-i)) != 0){
                    // apply repeat
                    ByteBuffer bbRepeat = ByteBuffer.allocate(2);
                    bbRepeat.order(ByteOrder.LITTLE_ENDIAN);
                    bbRepeat.put(input[pointer+1]);
                    bbRepeat.put(input[pointer]);          
                    short repeatCommand = bbRepeat.getShort(0); 
                    pointer+=2;
                    //Console.logger().finest("repeatCommand = " + Integer.toHexString(repeatCommand&0xFFFF)); 
                    if(repeatCommand==0){
                        done = true;
                        break;
                    }else{
                        byte repeats = (byte)(repeatCommand & 0x1F);
                        short wordIndex = (short)((repeatCommand - repeats)>>5);
                        //Console.logger().finest("pointer = " + pointer);
                        //Console.logger().finest("repeats = " + Integer.toHexString(repeats&0xFFFF) + ", wordIndex = " + Integer.toHexString(wordIndex&0xFFFF));
                        if(wordIndex==1){
                            // repeat last word (4 pixels)
                            byte firstByte = output.get(output.size()-2);
                            byte secondByte = output.get(output.size()-1);
                            for(int r=0;r<33-repeats;r++){
                                output.add(firstByte);
                                output.add(secondByte);
                            }
                        }else{
                            // repeat pointed 2 words (2 x 4 pixels)
                            int copyPointer = output.size()-wordIndex*2;
                            for(int r=0;r<33-repeats;r++){
                                output.add(output.get(copyPointer));
                                output.add(output.get(copyPointer+1));
                                copyPointer+=2;
                            }
                        }
                        //Console.logger().finest("output = " + BinaryHelpers.byteListToHex(output));
                    }
                }else{
                    // copy word
                    output.add(input[pointer]);
                    output.add(input[pointer+1]);
                    pointer+=2;
                    //Console.logger().finest("output = " + BinaryHelpers.byteListToHex(output));
                }
            }

        }
        byte[] bytes = new byte[output.size()];
        for(int i=0;i<bytes.length;i++){
            bytes[i] = output.get(i);
        }
        tiles = new UncompressedGraphicsDecoder().decode(bytes, palette);
        Console.logger().finest("EXITING decode");
        return tiles;
    }
    
    @Override
    public byte[] encode(Tile[] tiles) {
        Console.logger().finest("ENTERING encode");
        byte[] input = new UncompressedGraphicsDecoder().encode(tiles);
        Console.logger().finest("input = " + BinaryHelpers.bytesToHex(input));
        byte[] output = null;
        List<Short> outputWords = new ArrayList();
        Short currentCommandWord = null;
        int commandWordCursor = 0;
        int commandWordIndex = 0;
        int inputPointer = 0;
        Short previousWord = null;
        while(inputPointer<input.length){
            
            if(commandWordCursor % 16 == 0){
                currentCommandWord = (short)0;
                commandWordIndex = outputWords.size();
                outputWords.add((short)(currentCommandWord & 0xFFFF));
                commandWordCursor = 0;
            }
            
            ByteBuffer inputbb = ByteBuffer.allocate(2);
            inputbb.order(ByteOrder.LITTLE_ENDIAN);
            inputbb.put(input[inputPointer+1]);
            inputbb.put(input[inputPointer]);          
            Short inputWord = inputbb.getShort(0);
            
            //Console.logger().finest("inputWord = " + Integer.toHexString(inputWord & 0xFFFF));
            
            /* Get number of potentially repeatable words */
            int potentialRepeats = 0;
            if(inputWord.equals(previousWord)){
                Short nextWord = inputWord;
                int testCursor = inputPointer;
                while(nextWord.equals(previousWord)){
                    potentialRepeats++;
                    // Push further if possible or stop
                    if(potentialRepeats==33){
                        break;
                    }                    
                    testCursor+=2;
                    if(testCursor+1<input.length){
                        ByteBuffer testbb = ByteBuffer.allocate(2);
                        testbb.order(ByteOrder.LITTLE_ENDIAN);
                        testbb.put(input[testCursor+1]);
                        testbb.put(input[testCursor]);          
                        nextWord = testbb.getShort(0);                        
                    }else{
                        break;
                    }
                } 
                //Console.logger().finest("Potential repeats = " + potentialRepeats);
            }
        
            /* Get number of potential word sequence to copy */
            int potentialCopyLength = 0;
            int candidateSourceCursor = 0;
            int sourceCursor = inputPointer-4;
            while(sourceCursor>=0){
                int testLength = 0;
                Short destWord = inputWord;
                ByteBuffer sourcebb = ByteBuffer.allocate(2);
                sourcebb.order(ByteOrder.LITTLE_ENDIAN);
                sourcebb.put(input[sourceCursor+1]);
                sourcebb.put(input[sourceCursor]);          
                Short sourceWord = sourcebb.getShort(0);
                while(sourceWord.equals(destWord)){
                    testLength++;
                    // Push further if possible or stop
                    if(testLength==33){
                        break;
                    }
                    if((inputPointer+testLength*2)<input.length){
                        sourcebb = ByteBuffer.allocate(2);
                        sourcebb.order(ByteOrder.LITTLE_ENDIAN);
                        sourcebb.put(input[sourceCursor+testLength*2+1]);
                        sourcebb.put(input[sourceCursor+testLength*2]);          
                        sourceWord = sourcebb.getShort(0);                        
                        ByteBuffer destbb = ByteBuffer.allocate(2);
                        destbb.order(ByteOrder.LITTLE_ENDIAN);
                        destbb.put(input[inputPointer+testLength*2+1]);
                        destbb.put(input[inputPointer+testLength*2]);          
                        destWord = destbb.getShort(0);
                    }else{
                        break;
                    }
                } 
                if(testLength>potentialCopyLength){
                    candidateSourceCursor = sourceCursor;
                    potentialCopyLength = testLength;
                }
                sourceCursor-=2;      
            }
            //Console.logger().finest("Potential copy length from " + candidateSourceCursor + " = " + potentialCopyLength); 
            
            if(potentialRepeats>1 || potentialCopyLength>1){
                if(potentialRepeats>=potentialCopyLength){
                    // Apply word repeat
                    int repeatValue = 33 - potentialRepeats;
                    short repeatWord = (short) (0x0020 | repeatValue);
                    outputWords.add(repeatWord);
                    //Console.logger().finest("repeatWord = " + Integer.toHexString(repeatWord & 0xFFFF));
                    inputPointer+=2*potentialRepeats;
                }else{
                    // Apply word sequence copy
                    int startOffset = (inputPointer - candidateSourceCursor) / 2;
                    int sequenceLength = 33 - potentialCopyLength;
                    short repeatWord = (short) ((short)(startOffset<<5) | sequenceLength);
                    outputWords.add(repeatWord);
                    //Console.logger().finest("repeatWord = " + Integer.toHexString(repeatWord & 0xFFFF));
                    inputPointer+=2*potentialCopyLength;
                }
                outputWords.set(commandWordIndex, (short) (outputWords.get(commandWordIndex) | (0x8000 >> commandWordCursor)));
            }else{
                // No repeat or copy : just output the word
                outputWords.add(inputWord);
                inputPointer+=2;
            }
            ByteBuffer previousbb = ByteBuffer.allocate(2);
            previousbb.order(ByteOrder.LITTLE_ENDIAN);
            previousbb.put(input[inputPointer-2+1]);
            previousbb.put(input[inputPointer-2]);          
            previousWord = previousbb.getShort(0);            
            commandWordCursor++;
            //Console.logger().finest("output = " + BinaryHelpers.shortListToHex(outputWords));
        }
        if(commandWordCursor % 16 == 0){
            currentCommandWord = (short)0;
            commandWordIndex = outputWords.size();
            outputWords.add(currentCommandWord);
            commandWordCursor = 0;
        }  
        outputWords.set(commandWordIndex, (short) (outputWords.get(commandWordIndex) | (0x8000 >> commandWordCursor)));
        outputWords.add((short)0);
        Console.logger().finest("output = " + BinaryHelpers.shortListToHex(outputWords));
        
        output = new byte[outputWords.size()*2];
        for(int i=0;i<outputWords.size();i++){
            short word = outputWords.get(i);
            output[i*2] = (byte)((word >> 8) & 0xff);
            output[i*2+1] = (byte)(word & 0xff);
        }
        Console.logger().finest("output bytes length = " + output.length);
        Console.logger().finest("EXITING decode");
        return output;
    }
}
