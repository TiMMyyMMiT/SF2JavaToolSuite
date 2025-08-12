/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.settings;

import com.sfc.sf2.core.settings.AbstractSettings;
import com.sfc.sf2.mapsprite.MapSpriteManager.MapSpriteExportMode;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class MapSpriteSettings implements AbstractSettings {

    private MapSpriteExportMode exportMode;
    
    public MapSpriteExportMode getExportMode() {
        return exportMode;
    }
    
    public void setExportMode(MapSpriteExportMode exportMode) {
        this.exportMode = exportMode;
    }
    
    @Override
    public void initialiseNewUser() {
        exportMode = MapSpriteExportMode.INDIVIDUAL_FILES;
    }

    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("exportMode")) {
            try {
                exportMode = MapSpriteExportMode.valueOf((String)data.get("exportMode"));
            } catch (Exception e) {
                initialiseNewUser();
            }
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("exportMode", exportMode.toString());
    }
}
