/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io;

import com.sfc.sf2.helpers.FileHelpers;
import com.sfc.sf2.helpers.PathHelpers;
import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;

/**
 *
 * @author TiMMy
 */

public enum FileFormat {
    ANY_IMAGE(-2),
    UNKNOWN(-1),
    //Binay
    BIN(1),
    ASM(2),
    //Image
    PNG(3),
    GIF(4),
    //Metadata
    META(5);
    
    private final int format;

    private FileFormat(int format) {
        this.format = format;
    }

    public int getFormat() {
        return format;
    }
    
    /**
     *
     * @return a file extension with the dot. i.e. ".png" or ".gif"
     */
    public String getExt() {
        return "."+getName();
    }
    
    /**
     *
     * @return a file extension without the dot. i.e. "png" or "gif"
     * Required for exporting raw images
     */
    public String getName() {
        switch (this) {
            case BIN:
                return "bin";
            case ASM:
                return "asm";
            case PNG:
            case ANY_IMAGE:
                return "png";
            case GIF:
                return "gif";
            case META:
                return "meta";
            default:
                return "unkn";
        }
    }
    
    public static FileFormat getFormat(Path filePath) {
        return getFormat(PathHelpers.extensionFromPath(filePath));
    }
    
    public static FileFormat getFormat(File file) {
        return getFormat(FileHelpers.getExtension(file));
    }
    
    public static FileFormat getFormat(String extension) {
        if (extension == null || extension.length() == 0) return UNKNOWN;
        if (extension.charAt(0) == '.') {
            extension = extension.substring(1);
        }
        switch (extension) {
            case "bin":
                return BIN;
            case "asm":
                return ASM;
            case "png":
                return PNG;
            case "gif":
                return GIF;
            case "meta":
            case "txt":
                return META;
            default:
                return UNKNOWN;
        }
    }
    
    public FileFormatFilter getFileFilter() {
        return new FileFormatFilter(this);
    }
    
    private class FileFormatFilter implements FileFilter {

        private FileFormat format;

        public FileFormatFilter(FileFormat format) {
            this.format = format;
        }
        
        @Override
        public boolean accept(File f) {
            FileFormat format = FileFormat.getFormat(f);
            switch (this.format) {
                case ANY_IMAGE:
                    return format == PNG || format == GIF;
                default:
                    return this.format == format;
            }
        }
    }
}
