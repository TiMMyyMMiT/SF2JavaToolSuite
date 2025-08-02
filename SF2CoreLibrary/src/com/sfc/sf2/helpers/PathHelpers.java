/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

import com.sfc.sf2.application.settings.CoreSettings;
import com.sfc.sf2.application.settings.SettingsManager;
import java.io.File;
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
    
    public static String filenameFromPath(Path path) {
        String name = path.getFileName().toString();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1) {
            return name;
        } else {
            return name.substring(0, dotIndex+1);
        }
    }
    
    public static String extensionFromPath(Path path) {
        String name = path.getFileName().toString();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1) {
            return "";
        } else {
            return name.substring(dotIndex+1);
        }
    }
    
    public static File getNearestValidParent(Path path) {
        if (!path.isAbsolute()) {
            path = getBasePath().resolve(path);
        }
        return getNearestValidParent(path.toFile());
    }
    
    public static File getNearestValidParent(File file) {
        if (file.exists()) return file;
        File parent = file.getParentFile();
        if (parent == null) return file;
        return getNearestValidParent(parent);
    }
}
