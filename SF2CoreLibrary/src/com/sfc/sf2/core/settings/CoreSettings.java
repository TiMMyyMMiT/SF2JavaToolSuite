/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.core.settings;

import com.sfc.sf2.helpers.PathHelpers;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class CoreSettings implements AbstractSettings {

    private String basePath = null;
    private String incbinPath = null;
    
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
    
    public int getLogLevel() {
        return logLevel;
    }
    
    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public void initialiseNewUser() {
        String appPath = PathHelpers.getApplicationpath().toString();
        int incbinIndex = appPath.indexOf("\\disasm\\data\\");
        if (incbinIndex >= 0) {    //In SF2DISASM
            incbinPath = appPath.substring(0, incbinIndex+8);
            basePath =  appPath;
        } else {    //A dev build?
            basePath = incbinPath = null;
        }
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
        data.put("logLevel", Integer.toString(logLevel));
    }
}
