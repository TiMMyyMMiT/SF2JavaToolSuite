/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.mapsprite;

import com.sfc.sf2.core.gui.controls.Console;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 *
 * @author TiMMy
 */
public class MapSpriteEntries {
    
    private final MapSprite mapSprites[];
    private final int[] entries;
    
    public MapSpriteEntries(int totalEntries) {
        this.mapSprites = new MapSprite[totalEntries];
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
    
    public MapSprite[] optimisePerPair(MapSprite[] unreferencedSprites) {
        ArrayList<Integer> optimised = new ArrayList<>();
        ArrayList<MapSprite> unreferenced = new ArrayList<>(Arrays.asList(unreferencedSprites));
        for (int i = 0; i < entries.length; i++) {
            if (entries[i] == i) {  //Not a reference (therefore may duplicate sprites)
                for (int j = i+1; j < entries.length; j++) {
                    if (entries[j] == j && (mapSprites[entries[i]] != null && mapSprites[entries[i]].equals(mapSprites[entries[j]]))) { //Is a duplicate
                        entries[j] = entries[i];
                        if (mapSprites[j] != null) {
                            unreferenced.add(mapSprites[j]);
                            mapSprites[j].clearIndexedColorImage(false);
                            mapSprites[j] = null;
                        }
                        optimised.add(j);
                        //Also check if anything references this sprite and make it point to the new reference point
                        for (int k = j+1; k < entries.length; k++) {
                            if (entries[k] == j) {
                                entries[k] = entries[i];
                            }
                        }
                    }
                }
            }
        }
        
        if (optimised.size() == 0) {
            JOptionPane.showMessageDialog(null, "No sprites require optimisation.");
        } else {
            optimised.sort(null);
            StringBuilder sb = new StringBuilder();
            sb.append(optimised.size());
            sb.append(" MapSprites optimised :\n");
            for (int i = 0; i < optimised.size(); i++) {
                sb.append(String.format("- MapSprite %03d-%d now references MapSprite %03d-%d\n", optimised.get(i)/3, optimised.get(i)%3, entries[optimised.get(i)]/3, entries[optimised.get(i)]%3));
            }
            Console.logger().info(sb.toString());
            JOptionPane.showMessageDialog(null, sb.toString());
        }
        
        unreferenced.sort((o1, o2) -> { return (o1.getIndex()*3+o1.getFacingIndex()) - (o2.getIndex()*3+o2.getFacingIndex()); });
        unreferencedSprites = new MapSprite[unreferenced.size()];
        unreferencedSprites = unreferenced.toArray(unreferencedSprites);
        return unreferencedSprites;
    }
    
    public MapSprite[] optimisePerRow(MapSprite[] unreferencedSprites) {
        ArrayList<Integer> optimised = new ArrayList<>();
        ArrayList<MapSprite> unreferenced = new ArrayList<>(Arrays.asList(unreferencedSprites));
        for (int i = 0; i < entries.length; i += 3) {
            if (entries[i] == i || entries[i+1] == i+1 || entries[i+2] == i+2) {  //Not a reference (therefore may duplicate sprites)
                for (int j = i+3; j < entries.length; j += 3) {
                    if (entries[j] == j && (mapSprites[entries[i]] != null && mapSprites[entries[i]].equals(mapSprites[entries[j]])) &&
                        entries[j+1] == j+1 && (mapSprites[entries[i+1]] != null && mapSprites[entries[i+1]].equals(mapSprites[entries[j+1]])) &&
                        entries[j+2] == j+2 && (mapSprites[entries[i+1]] != null && mapSprites[entries[i+2]].equals(mapSprites[entries[j+2]]))) { //Is a duplicate
                        for (int m = 0; m < 3; m++) {
                            entries[j+m] = entries[i+m];
                            if (mapSprites[j+m] != null) {
                                unreferenced.add(mapSprites[j+m]);
                                mapSprites[j+m].clearIndexedColorImage(false);
                                mapSprites[j+m] = null;
                            }
                            optimised.add(j+m);
                        }
                        //Also check if anything references this sprite and make it point to the new reference point
                        for (int k = j+3; k < entries.length; k += 3) {
                            if (entries[k] == j && entries[k+1] == j+1 && entries[k+2] == j+2) {
                                entries[k] = entries[i];
                                entries[k+1] = entries[i+1];
                                entries[k+2] = entries[i+2];
                            }
                        }
                    }
                }
            }
        }
        
        if (optimised.size() == 0) {
            JOptionPane.showMessageDialog(null, "No sprites require optimisation.");
        } else {
            optimised.sort(null);
            StringBuilder sb = new StringBuilder();
            sb.append(optimised.size());
            sb.append(" MapSprites optimised :\n");
            for (int i = 0; i < optimised.size(); i++) {
                sb.append(String.format("- MapSprite %03d-%d now references MapSprite %03d-%d\n", optimised.get(i)/3, optimised.get(i)%3, entries[optimised.get(i)]/3, entries[optimised.get(i)]%3));
            }
            Console.logger().info(sb.toString());
            JOptionPane.showMessageDialog(null, sb.toString());
        }
        
        unreferenced.sort((o1, o2) -> { return (o1.getIndex()*3+o1.getFacingIndex()) - (o2.getIndex()*3+o2.getFacingIndex()); });
        unreferencedSprites = new MapSprite[unreferenced.size()];
        unreferencedSprites = unreferenced.toArray(unreferencedSprites);
        return unreferencedSprites;
    }
}
