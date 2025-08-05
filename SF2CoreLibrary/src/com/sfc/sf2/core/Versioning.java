package com.sfc.sf2.core;

import com.sfc.sf2.core.gui.controls.Console;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */

/**
 *
 * @author TiMMy
 */
public class Versioning {
    
    public static String getVersion() {
        String version = String.class.getPackage().getImplementationVersion();
        if (version == null) {
            version = manuallyLoadManifest();
        }
        if (version == null) {
            Console.logger().warning("Could not load program version.");
        } else {
            Console.logger().info("Program version : " + version);
        }
        return version;
    }
    
    private static String manuallyLoadManifest() {
        File manifest = new File(System.getProperty("user.dir") + "\\version.properties");
        if (manifest.exists()) {
            try {
                //Probably running in editor
                BufferedReader reader = new BufferedReader(new FileReader(manifest));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("version")) {
                        String version = line.substring(line.indexOf(':')+1).trim();
                        reader.close();
                        return version;
                    }
                }
                reader.close();
            } catch (Exception ex) { }
        }
        return null;
    }
}
