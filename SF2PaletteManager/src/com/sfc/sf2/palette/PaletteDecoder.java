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
 *  //https://segaretro.org/Sega_Mega_Drive/Palettes_and_CRAM#Format
 * @author wiz
 */
public class PaletteDecoder {
    
    /*
    * The CRAM colors. Valid values are even numbers from 0-14
    */
    private static final Map<Integer, Integer> CRAM_VALUE_MAP = new HashMap(){ {
        put(0, 0);
        put(2, 52);
        put(4, 87);
        put(6, 116);
        put(8, 144);
        put(10, 172);
        put(12, 206);
        put(14, 255);
    } };
    
    /*
    * Offset values half way between each CRAM value. Allows converting from 255 color brightness to CRAM color
    * 
    */                                                      //  0, 52,  87, 116, 144, 172, 206, 255
    private static final int[] CRAM_OFFSET_ARRAY = new int[] { 26, 69, 101, 130, 158, 189, 230, 999 };
    
    public static int brightnessToCramIndex(int brightness) {
        for (Map.Entry<Integer, Integer> entry : CRAM_VALUE_MAP.entrySet()) {
            if (brightness <= entry.getValue())
                return entry.getKey();
        }
        return 0;
    }
    
    private static int brightnessToCramValue(int brightness) {
        for (int i = 0; i < CRAM_OFFSET_ARRAY.length; i++) {
            if (brightness <= CRAM_OFFSET_ARRAY[i])
                return CRAM_VALUE_MAP.get(i*2);
        }
        return 0;
    }
    
    public static int cramIndexToBrightness(int cram) {
        if (CRAM_VALUE_MAP.containsKey(cram)) {
            return CRAM_VALUE_MAP.get(cram);
        } else {
            return 0;
        }
    }
    
    public static CRAMColor[] decodePalette(byte[] data) {
        CRAMColor[] colors = new CRAMColor[data.length/2];
        int a = 255;
        for(int i=0;i*2<data.length;i++){
            if(i*2+1<data.length){
                byte first = data[i*2];
                byte second = data[i*2+1];
                int b = (0x0F & first);
                int g = ((0xF0 & second)>>4);
                int r = (0x0F & second);
                r = CRAM_VALUE_MAP.get(r&0xE);
                g = CRAM_VALUE_MAP.get(g&0xE);
                b = CRAM_VALUE_MAP.get(b&0xE);
                CRAMColor color = CRAMColor.fromPremadeCramColor(new Color(r,g,b,a));
                colors[i] = color;
            }
            if(i==0){
                a=255;
            }
        }
        
        return colors;
    }
    
    public static byte[] encodePalette(CRAMColor[] palette) {
        byte[] paletteBytes = new byte[palette.length*2];
        for(int i=0;i<palette.length;i++){
            byte first = 0x00;
            byte second = 0x00;
            CRAMColor color = palette[i];
            int b = brightnessToCramIndex(color.CRAMColor().getBlue());
            int g = brightnessToCramIndex(color.CRAMColor().getGreen());
            int r = brightnessToCramIndex(color.CRAMColor().getRed());
            first = (byte)b;
            second = (byte)(((g*16)&0xF0) | (r&0x0F));
            paletteBytes[i*2] = first;
            paletteBytes[i*2+1] = second;
        }        
        return paletteBytes;
    }
    
    public static Color conformColorToCram(Color color) {
        int rgba = color.getRGB();
        int r = (rgba >> 16) & 0xFF;
        int g = (rgba >>  8) & 0xFF;
        int b = (rgba >>  0) & 0xFF;
        int a = (rgba >> 24) & 0xFF;
        r = brightnessToCramValue(r);
        g = brightnessToCramValue(g);
        b = brightnessToCramValue(b);
        return new Color(r, g, b, a);
    }
}
