/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.mapsprite;

import com.sfc.sf2.core.io.asm.EntriesAsmData;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author TiMMy
 */
public class MapSpriteEntries {
    
    private final MapSprite mapSprites[];
    private final int[] entries;
    
    public MapSpriteEntries(EntriesAsmData mapspriteEntries) {
        String id = mapspriteEntries.getUniqueEntries(mapspriteEntries.uniqueEntriesCount()-1);
        int count = Integer.parseInt(id.substring(9, id.indexOf('_')))+1;
        mapSprites = new MapSprite[count];
        entries = new int[count];
    }

    public MapSpriteEntries(int totalEntries, int uniqueEntries) {
        this.mapSprites = new MapSprite[uniqueEntries];
        this.entries = new int[totalEntries];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = -1;
        }
    }

    public MapSprite[] getMapSprites() {
        return mapSprites;
    }

    public int[] getEntries() {
        return entries;
    }
    
    public boolean hasData(int index) {
        return entries[index] != -1;
    }
    
    public boolean isDuplicateEntry(int index) {
        return entries[index] != index;
    }
    
    public void addUniqueEntry(int index, MapSprite mapSprite) {
        mapSprites[index] = mapSprite;
        entries[index] = index;
    }
    
    public void addDuplicateEntry(int index, int reference) {
        entries[index] = reference;
    }
    
    public MapSprite getMapSprite(int index) {
        return mapSprites[entries[index]];
    }
    
    public void optimise() {
        ArrayList<Integer> optimised = new ArrayList<>();
        for (int i = 0; i < entries.length; i++) {
            if (entries[i] == i) {  //Not a reference
                for (int j = i+1; j < entries.length; j++) {
                    if (entries[j] == j && mapSprites[entries[i]].equals(mapSprites[entries[j]])) {
                        entries[j] = entries[i];
                        mapSprites[j] = null;
                        optimised.add(j);
                    }
                }
            }
        }
        
        if (optimised.size() == 0) {
            JOptionPane.showMessageDialog(null, "No sprites require optimisation.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(optimised.size());
            sb.append(" MapSprites optimised :\n\n");
            for (int i = 0; i < optimised.size(); i++) {
                sb.append(String.format("- MapSprite %03d now references MapSprite %03d\n", optimised.get(i), entries[optimised.get(i)]));
            }
            JOptionPane.showMessageDialog(null, sb.toString());
        }
    }
}
