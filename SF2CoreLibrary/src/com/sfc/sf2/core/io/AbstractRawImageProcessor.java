/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.helpers.PathHelpers;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

/**
 *
 * @author TiMMy
 * @param <TType> The type of data being loaded
 * @param <TPackage> The input data required to load the TType
 */
public abstract class AbstractRawImageProcessor<TType extends Object, TPackage extends Object> {
    
    public enum FileFormat {
        UNKNOWN,
        PNG,
        GIF,
    }
    
    public TType importRawImage(Path filePath, TPackage pckg) throws RawImageException, DisassemblyException, IOException {
        FileFormat fileFormat = fileExtensionToFormat(filePath);
        Console.logger().finest("ENTERING importRawImage : " + filePath + ". Format : " + fileFormat);
        TType item = null;
        BufferedImage image = ImageIO.read(filePath.toFile());
        ColorModel cm = image.getColorModel();
        if(!(cm instanceof IndexColorModel)){
            throw new RawImageException("ERROR: Image must be in an indexed color format. Format : " + cm.getColorSpace().toString());
        }
        IndexColorModel icm = (IndexColorModel)cm;
        WritableRaster raster = image.getRaster();
        item = parseImageData(raster, icm, pckg);
        Console.logger().finest("EXITING importRawImage");
        return item;
    }
    
    protected abstract TType parseImageData(WritableRaster raster, IndexColorModel icm, TPackage pckg) throws DisassemblyException;
    
    public void exportRawImage(Path filePath, TType item, TPackage pckg) throws IOException, RawImageException, DisassemblyException {
        FileFormat fileFormat = fileExtensionToFormat(filePath);
        Console.logger().finest("ENTERING exportRawImage : " + filePath + ". Format : " + fileFormat);
        BufferedImage image = packageImageData(item, pckg);
        ColorModel cm = image.getColorModel();
        if(!(cm instanceof IndexColorModel)){
            throw new RawImageException("ERROR: Image must be in an indexed color format. Format : " + cm.getColorSpace().toString());
        }
        File outputfile = filePath.toFile();
        ImageIO.write(image, GetFileExtensionName(fileFormat), outputfile);
        Console.logger().finest("EXITING exportRawImage");
    }

    protected abstract BufferedImage packageImageData(TType item, TPackage pckg) throws DisassemblyException;
    
    
    public static String GetFileExtensionString(FileFormat fileFormat) {
        return "."+GetFileExtensionName(fileFormat);
    }
    
    public static String GetFileExtensionName(FileFormat fileFormat) {
        switch (fileFormat) {
            case PNG:
                return "png";
            case GIF:
                return "gif";
            default:
                return "unkn";
        }
    }
    
    public static FileFormat fileExtensionToFormat(Path filePath) {
        String extension = PathHelpers.extensionFromPath(filePath);
        switch (extension) {
            case "png":
            case ".png":
                return FileFormat.PNG;
            case "gif":
            case ".gif":
                return FileFormat.GIF;
            default:
                return FileFormat.UNKNOWN;
        }
    }
}
