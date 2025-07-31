/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette.graphics;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wiz
 */
public class PaletteEncoder {
    
    
    public static final int[] VALUE_ARRAY = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14};
    
    private static byte[] newPaletteBytes;
    
    public static byte[] producePalette(Color[] palette){

        byte[] paletteBytes = new byte[palette.length*2];
        
        Color[] standardizedPalette = new Color[palette.length];
        
        for(int i=0;i<palette.length;i++){
            byte first = 0x00;
            byte second = 0x00;
            Color color = palette[i];
            int b = VALUE_ARRAY[color.getBlue()];
            int g = VALUE_ARRAY[color.getGreen()];
            int r = VALUE_ARRAY[color.getRed()];
            standardizedPalette[i] = new Color(r,g,b);
            first = (byte)b;
            second = (byte)(((g*16)&0xF0) | (r&0x0F));
            paletteBytes[i*2] = first;
            paletteBytes[i*2+1] = second;
        }
        
        newPaletteBytes = paletteBytes;
        
        return paletteBytes;
    }
    
    public static byte[] getNewPaletteFileBytes(){
        return newPaletteBytes;
    }
    
}
