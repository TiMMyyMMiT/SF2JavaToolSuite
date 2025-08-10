/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io.asm;

import com.sfc.sf2.core.gui.controls.Console;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class EntriesAsmData {
    private final ArrayList<String> entries = new ArrayList<>();
    private final ArrayList<String> uniqueEntries = new ArrayList<>();
    private final HashMap<String, Path> pathMap = new HashMap<>();
    
    public int entriesCount() {
        return entries.size();
    }
    
    public int uniquePathsCount() {
        return entries.size();
    }
    
    public String getEntry(int index) {
        if (index >= 0 && index < entries.size()) {
            return entries.get(index);
        } else {
            return null;
        }
    }
    
    public String getUniqueEntries(int index) {
        if (index >= 0 && index < uniqueEntries.size()) {
            return uniqueEntries.get(index);
        } else {
            return null;
        }
    }
    
    public Path getPathForEntry(int index) {
        if (index >= 0 && index < entries.size()) {
            return pathMap.get(entries.get(index));
        } else {
            return null;
        }
    }
    
    public Path getPathForUnique(int index) {
        if (index >= 0 && index < uniqueEntries.size()) {
            return pathMap.get(uniqueEntries.get(index));
        } else {
            return null;
        }
    }
    
    public void addEntry(String entry) {
        entries.add(entry);
        if (!pathMap.containsKey(entry)) {
            uniqueEntries.add(entry);
            pathMap.put(entry, null);
        }
    }
    
    public void addPath(String entry, Path path) {
        if (!pathMap.containsKey(entry)) {
            addEntry(entry);
        }
        if (pathMap.get(entry) == null) {
            pathMap.replace(entry, path);
        }
    }
}
