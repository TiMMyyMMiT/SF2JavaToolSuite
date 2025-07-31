/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.application.settings;

import com.sfc.sf2.helpers.PathHelpers;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class CoreSettings implements AbstractSettings {

    String basePath = null;
    String incbinPath = null;
    
    Color LayoutBackgroundColor;
        
    public String getBasePath() {
        return basePath;
    }
    
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
    
    public String getIncbinPath() {
        return incbinPath;
    }
    
    public void setIncbinPath(String incbinPath) {
        this.incbinPath = incbinPath;
    }
    
    public boolean arePathsSet() {
        return basePath != null;
    }
    
    public Color getLayoutBackgroundColor() {
        return LayoutBackgroundColor;
    }
    
    public void setLayoutBackgroundColor(Color LayoutBackgroundColor) {
        this.LayoutBackgroundColor = LayoutBackgroundColor;
    }

    @Override
    public void initialiseNewUser() {
        String appPath = PathHelpers.getApplicationpath().toString();
        int incbinIndex = appPath.indexOf("disasem\\data\\");
        if (incbinIndex >= 0) {    //In SF2DISASM
            incbinPath = appPath.substring(0, incbinIndex);
            basePath =  appPath;
        } else {    //A dev build?
            basePath = incbinPath = null;
        }
        LayoutBackgroundColor = new Color(200, 200, 200);
    }
    
    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("basePath")) {
            basePath = data.get("basePath");
        }
        if (data.containsKey("incbinPath")) {
            incbinPath = data.get("incbinPath");
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("basePath", basePath);
        data.put("incbinPath", incbinPath);
    }
}
