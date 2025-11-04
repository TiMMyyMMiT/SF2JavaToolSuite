/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.settings;

import com.sfc.sf2.core.io.FileFormat;
import com.sfc.sf2.core.settings.AbstractSettings;
import com.sfc.sf2.mapsprite.MapSpriteManager.MapSpriteExportMode;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class MapSpriteSettings implements AbstractSettings {

    private MapSpriteExportMode exportMode;
    private FileFormat exportFileFormat;
    
    public MapSpriteExportMode getExportMode() {
        return exportMode;
    }
    
    public void setExportMode(MapSpriteExportMode exportMode) {
        this.exportMode = exportMode;
    }
    
    public FileFormat getExportFileFormat() {
        return exportFileFormat;
    }
    
    public void setExportFileFormat(FileFormat exportFileFormat) {
        this.exportFileFormat = exportFileFormat;
    }
    
    @Override
    public void initialiseNewUser() {
        exportMode = MapSpriteExportMode.INDIVIDUAL_FILES;
        exportFileFormat = exportFileFormat.PNG;
    }

    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("exportMode")) {
            try {
                exportMode = MapSpriteExportMode.valueOf((String)data.get("exportMode"));
                exportFileFormat = FileFormat.valueOf((String)data.get("exportFileFormat"));
            } catch (Exception e) {
                initialiseNewUser();
            }
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("exportMode", exportMode.toString());
        data.put("exportFileFormat", exportFileFormat.toString());
    }
}
