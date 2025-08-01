/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author TiMMy
 * @param <TType> The type of data being loaded
 * @param <TPackage> The input data required to load the TType
 */
public abstract class AbstractRawImageManager<TType extends Object, TPackage extends Object> {
    private static final Logger LOG = Logger.getLogger(AbstractDisassemblyManager.class.getName());
    
    public enum FileFormat {
        PNG,
        GIF,
    }
    
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
                LOG.throwing(LOG.getName(),"UNKNOWN FILE FORMAT", new IOException());
                return "png";
        }
    }
    
    public TType importRawImage(Path filePath, TPackage pckg, FileFormat fileFormat) {
        LOG.entering(LOG.getName(), "importRawImage : " + filePath);
        TType item = null;
        try {
            BufferedImage image = ImageIO.read(filePath.toFile());
            ColorModel cm = image.getColorModel();
            if(!(cm instanceof IndexColorModel)){
                throw new RawImageException("IMAGE FORMAT ERROR : COLORS ARE NOT INDEXED AS EXPECTED. Format : " + cm.getColorSpace().toString());
            }else{
                IndexColorModel icm = (IndexColorModel)cm;
                WritableRaster raster = image.getRaster();
                item = parseImageData(raster, icm, pckg);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not import image : " + filePath, e);
            item = null;
        }
        LOG.exiting(LOG.getName(), "importRawImage");
        return item;
    }
    
    protected abstract TType parseImageData(WritableRaster raster, IndexColorModel icm, TPackage pckg) throws DisassemblyException;
    
    public boolean exportRawImage(Path filePath, TType item, FileFormat fileFormat) {
        LOG.entering(LOG.getName(), "exportRawImage : " + filePath);
        boolean error = false;
        try {
            BufferedImage image = packageImageData(item);
            File outputfile = filePath.toFile();
            ImageIO.write(image, GetFileExtensionString(fileFormat), outputfile);
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not export image : " + filePath, e);
            error = true;
        }
        LOG.exiting(LOG.getName(), "exportRawImage");
        return error;
    }

    protected abstract BufferedImage packageImageData(TType item) throws DisassemblyException;
}
