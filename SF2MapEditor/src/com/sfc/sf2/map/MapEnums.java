/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map;

import com.sfc.sf2.core.AbstractEnums;
import java.util.LinkedHashMap;

/**
 *
 * @author TiMMy
 */
public class MapEnums extends AbstractEnums {
        
    private final LinkedHashMap<String, Integer> items;

    public MapEnums(LinkedHashMap<String, Integer> items) {
        this.items = items;
    }

    public LinkedHashMap<String, Integer> getItems() {
        return items;
    }
}
