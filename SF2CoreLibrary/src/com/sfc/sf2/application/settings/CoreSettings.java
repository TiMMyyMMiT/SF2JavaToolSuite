/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.application.settings;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.helpers.PathHelpers;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class CoreSettings implements AbstractSettings {

    private String basePath = null;
    private String incbinPath = null;
    
    private boolean darkTheme;
    private Color transparentBGColor;
    private int logLevel;
        
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
        return basePath != null && basePath.length() > 0;
    }
    
    public boolean getIsDarkTheme() {
        return darkTheme;
    }
    
    public void setTransparentBGColor(Color transparentBGColor) {
        this.transparentBGColor = transparentBGColor;
    }
    
    public Color getTransparentBGColor() {
        return transparentBGColor;
    }
    
    public void setIsDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
    }
    
    public int getLogLevel() {
        return logLevel;
    }
    
    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public void initialiseNewUser() {
        String appPath = PathHelpers.getApplicationpath().toString();
        int incbinIndex = appPath.indexOf("disasm\\data\\");
        if (incbinIndex >= 0) {    //In SF2DISASM
            incbinPath = appPath.substring(0, incbinIndex);
            basePath =  appPath;
        } else {    //A dev build?
            basePath = incbinPath = null;
        }
        darkTheme = false;
        transparentBGColor = new Color(200, 0, 200);
        logLevel = 1;
    }
    
    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("basePath")) {
            basePath = data.get("basePath");
        }
        if (data.containsKey("incbinPath")) {
            incbinPath = data.get("incbinPath");
        }
        if (data.containsKey("darkTheme")) {
            darkTheme = Boolean.parseBoolean(data.get("darkTheme"));
        }
        if (data.containsKey("transparentBGColor")) {
            String[] colorSplit = data.get("transparentBGColor").split(",");
            transparentBGColor = new Color(Integer.parseInt(colorSplit[0].trim()), Integer.parseInt(colorSplit[1].trim()), Integer.parseInt(colorSplit[2].trim()));
        }
        if (data.containsKey("logLevel")) {
            logLevel = Integer.parseInt(data.get("logLevel"));
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        if (basePath != null) {
            data.put("basePath", basePath);
            data.put("incbinPath", incbinPath);
        }
        data.put("darkTheme", Boolean.toString(darkTheme));
        data.put("transparentBGColor", String.format("%d, %d, %d", transparentBGColor.getRed(), transparentBGColor.getGreen(), transparentBGColor.getBlue()));
        data.put("logLevel", Integer.toString(logLevel));
    }
}
