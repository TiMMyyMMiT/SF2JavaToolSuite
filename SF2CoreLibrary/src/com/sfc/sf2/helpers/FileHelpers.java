/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author TiMMy
 */
public class FileHelpers {
    public static File getTempFile(String prefix) throws IOException {
        return File.createTempFile(prefix, Long.toString(System.currentTimeMillis()), Path.of(System.getenv("APPDATA")).resolve("SF2").toFile());
    }
}
