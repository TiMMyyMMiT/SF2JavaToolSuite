/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.vwfont.graphics;

import com.sfc.sf2.vwfont.FontSymbol;
import java.util.BitSet;

/**
 *
 * @author wiz
 */
public class VWFontDecoder {
    
    public static FontSymbol[] parseVWFont(byte[] data) {
        System.out.println("com.sfc.sf2.vwfont.graphics.VWFontDecoder.parseVWFont() - Parsing VW Font ...");
        FontSymbol[] symbols = new FontSymbol[data.length/32];
        for (int i = 0; i < symbols.length; i++) {
            byte[] character = new byte[32];
            System.arraycopy(data, i*32, character, 0, 32);
            symbols[i] = decodeSymbol(character);
        }
            System.out.println("Parsed Font Symbols : " + symbols.length);
        System.out.println("com.sfc.sf2.vwfont.graphics.VWFontDecoder.parseVWFont() - VW Font parsed.");
        return symbols;
    }
    
    private static FontSymbol decodeSymbol(byte[] data) {
        int width = data[1];
        int[][] pixels = new int[16][16];
        BitSet bits = BitSet.valueOf(data);
        int startIndex = 16;
        for (int y = 0; y < 16; y++) {
            int rowStart = startIndex + y * 16;
            for (int x = 0; x < 8; x++) {
                pixels[7 - x][y] = bits.get(rowStart + x) ? 1 : 0;      //Left byte (bits 0-7)
                pixels[15 - x][y] = bits.get(rowStart + 8 + x) ? 1 : 0; //Right byte (bits 8-15)
            }
        }
        
        FontSymbol symbol = new FontSymbol();
        symbol.setWidth(width);
        symbol.setPixels(pixels);
        return symbol;
    }
}
