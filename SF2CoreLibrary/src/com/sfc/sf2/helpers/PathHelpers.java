/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

import com.sfc.sf2.application.settings.CoreSettings;
import com.sfc.sf2.application.settings.SettingsManager;
import java.nio.file.Path;

/**
 *
 * @author TiMMy
 */
public class PathHelpers {
    
    private static final CoreSettings coreSettings = SettingsManager.getSettingsStore("core");
    
    public static Path getApplicationpath() {
        return Path.of(System.getProperty("user.dir"));
    }
    
    public static Path getBasePath() {
        return Path.of(coreSettings.getBasePath());
    }
    
    public static Path getIncbinPath() {
        return Path.of(coreSettings.getIncbinPath());
    }
}
