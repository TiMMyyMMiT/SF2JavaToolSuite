/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.settings;

import com.sfc.sf2.core.settings.AbstractSettings;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class TerrainSettings implements AbstractSettings {
    
    private int terrainDrawMode;

    public int getTerrainDrawMode() {
        return terrainDrawMode;
    }

    public void setTerrainDrawMode(int terrainDrawMode) {
        this.terrainDrawMode = terrainDrawMode;
    }

    @Override
    public void initialiseNewUser() {
        terrainDrawMode = 0;
    }

    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("terrainDrawMode")) {
            terrainDrawMode = Integer.parseInt(data.get("terrainDrawMode"));
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("terrainDrawMode", Integer.toString(terrainDrawMode));
    }
}
