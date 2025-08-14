/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.vwfont.graphics;

import com.sfc.sf2.vwfont.FontSymbol;
import java.util.Arrays;
import java.util.BitSet;

/**
 *
 * @author wiz
 */
public class VWFontEncoder {
        
    public static byte[] produceVWFont(FontSymbol[] symbols) {
        System.out.println("com.sfc.sf2.vwfont.graphics.VWFontEncoder.produceVWFont() - Producing VWFont ...");
        byte[] data = new byte[0];
        for (int i = 0; i < symbols.length; i++) {
            byte[] symbolData = encodeSymbol(symbols[i]);
            int previousLength = data.length;
            data = Arrays.copyOf(data,data.length+32);
            System.arraycopy(symbolData, 0, data, previousLength, symbolData.length);
        }
        System.out.println("com.sfc.sf2.vwfont.graphics.VWFontEncoder.produceVWFont() - VWFont produced.");
        return data;
    }
    
    private static byte[] encodeSymbol(FontSymbol symbol) {
        byte[] data = new byte[32];
        BitSet bits = new BitSet(256);
        int[] pixels = symbol.getPixels();
        int startIndex = 16;
        for (int y = 0; y < 16; y++) {
            int rowStart = startIndex + y * 16;
            for (int x = 0; x < 8; x++) {
                if (pixels[7-x + y*16] == 1) {     // Left byte
                    bits.set(rowStart + x);
                }
                if (pixels[15-x + y*16] == 1) {    // Right byte
                    bits.set(rowStart + 8 + x);
                }
            }
        }
        byte[] bitsArray = bits.toByteArray();
        System.arraycopy(bitsArray, 0, data, 0, Math.min(bitsArray.length, data.length));
        data[0] = 0;
        data[1] = (byte)symbol.getWidth();
        return data;
    }
}
