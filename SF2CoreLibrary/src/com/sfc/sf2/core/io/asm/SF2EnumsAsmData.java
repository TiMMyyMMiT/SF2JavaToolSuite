/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io.asm;

import com.sfc.sf2.core.gui.controls.Console;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author TiMMy
 */
public class SF2EnumsAsmData {
    private String[] categories;
    private HashMap<String, LinkedHashMap<String, Integer>> enums;
    
    public SF2EnumsAsmData(String... categories) {
        this.categories = categories;
    }

    public String[] getCategories() {
        return categories;
    }
    
    public void addEnum(String category, String item, int value) {
        if (enums == null) {
            enums = new HashMap<>(categories.length);
        }
        if (!enums.containsKey(category)) {
            enums.put(category, new LinkedHashMap<>());
        }
        LinkedHashMap<String, Integer> set = enums.get(category);
        if (set.containsKey(item)) {
            set.replace(item, value);
        } else {
            set.put(item, value);
        }
    }
    
    public LinkedHashMap<String, Integer> getSet(String category) {
        if (enums.containsKey(category)) {
            return enums.get(category);
        } else {
            Console.logger().warning("WARNING Did not import SF2Enums data for category : " + category);
            return null;
        }
    }
}
