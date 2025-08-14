/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.vwfont.io;

import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.vwfont.FontSymbol;
import java.util.Arrays;
import java.util.BitSet;

/**
 *
 * @author wiz
 */
public class VWFontDisassemblyProcessor extends AbstractDisassemblyProcessor<FontSymbol[], EmptyPackage> {
    

    @Override
    protected FontSymbol[] parseDisassemblyData(byte[] data, EmptyPackage pckg) throws DisassemblyException {
        FontSymbol[] symbols = new FontSymbol[data.length/32];
        for (int i = 0; i < symbols.length; i++) {
            byte[] character = new byte[32];
            System.arraycopy(data, i*32, character, 0, 32);
            symbols[i] = decodeSymbol(character);
        }
            System.out.println("Parsed Font Symbols : " + symbols.length);
        return symbols;
    }
    
    private FontSymbol decodeSymbol(byte[] data) {
        int width = data[1];
        int[] pixels = new int[16*16];
        BitSet bits = BitSet.valueOf(data);
        int startIndex = 16;
        for (int y = 0; y < 16; y++) {
            int rowStart = startIndex + y * 16;
            for (int x = 0; x < 8; x++) {
                pixels[7-x + y*16] = bits.get(rowStart + x) ? 1 : 0;      //Left byte (bits 0-7)
                pixels[15-x + y*16] = bits.get(rowStart + 8 + x) ? 1 : 0; //Right byte (bits 8-15)
            }
        }
        
        FontSymbol symbol = new FontSymbol();
        symbol.setWidth(width);
        symbol.setPixels(pixels);
        return symbol;
    }

    @Override
    protected byte[] packageDisassemblyData(FontSymbol[] item, EmptyPackage pckg) throws DisassemblyException {
        byte[] data = new byte[0];
        for (int i = 0; i < item.length; i++) {
            byte[] symbolData = encodeSymbol(item[i]);
            int previousLength = data.length;
            data = Arrays.copyOf(data,data.length+32);
            System.arraycopy(symbolData, 0, data, previousLength, symbolData.length);
        }
        return data;
    }
    
    private byte[] encodeSymbol(FontSymbol symbol) {
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
