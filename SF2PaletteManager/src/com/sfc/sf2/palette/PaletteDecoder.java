/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wiz
 */
public class PaletteDecoder {
    
    public static final Map<Integer, Integer> VALUE_MAP = new HashMap(){
        {
            put(0, 0);
            put(2, 52);
            put(4, 87);
            put(6, 116);
            put(8, 144);
            put(10, 172);
            put(12, 206);
            put(14, 255);
	}
    };
    
    public static Color[] decodePalette(byte[] data){
        return decodePalette(data,false);
    }
    
    public static Color[] decodePalette(byte[] data, boolean firstColorTransparent){
        Color[] colors = new Color[data.length/2];
        int a = firstColorTransparent?0:255;
        for(int i=0;i*2<data.length;i++){
            if(i*2+1<data.length){
                byte first = data[i*2];
                byte second = data[i*2+1];
                int b = (0x0F & first);
                int g = ((0xF0 & second)>>4);
                int r = (0x0F & second);
                r = VALUE_MAP.get(r&0xE);
                g = VALUE_MAP.get(g&0xE);
                b = VALUE_MAP.get(b&0xE);
                Color color = new Color(r,g,b,a);
                colors[i] = color;
            }
            if(i==0){
                a=255;
            }
        }
        
        return colors;
    }
    
    public static final int[] VALUE_ARRAY = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14};
    
    private static byte[] newPaletteBytes;
    
    public static byte[] encodePalette(Color[] palette){

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
