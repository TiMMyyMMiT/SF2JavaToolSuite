/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette.graphics;

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
    
    public static Color[] parsePalette(byte[] data){
        return parsePalette(data,false);
    }
    
    public static Color[] parsePalette(byte[] data, boolean firstColorTransparent){
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
}
