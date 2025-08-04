package com.sfc.sf2.core;

import com.sfc.sf2.core.gui.controls.Console;
import java.io.File;
import java.util.Scanner;

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
        File manifest = new File(System.getProperty("user.dir") + "\\manifest.mf");
        if (manifest.exists()) {
            try {
                //Probably running in editor
                Scanner scan = new Scanner(manifest);
                while (scan.hasNext()) {
                    String line = scan.nextLine();
                    if (line.startsWith("Implementation-Version")) {
                        String version = line.substring(line.indexOf(':')+1).trim();
                        scan.close();return version;
                    }
                }
                scan.close();
            } catch (Exception ex) { }
        }
        return null;
    }
}
