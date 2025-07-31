/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette.io;

import com.sfc.sf2.palette.Palette;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author wiz
 */
public class RawImageManager {

    private static final Logger LOG = Logger.getLogger(RawImageManager.class.getName());
    
    public static Palette importImage(String filepath){
        Palette palette = null;
        try{
            Path path = Paths.get(filepath);
            BufferedImage img = ImageIO.read(path.toFile());
            ColorModel cm = img.getColorModel();
            if(!(cm instanceof IndexColorModel)){
                LOG.warning("IMAGE FORMAT ERROR : COLORS ARE NOT INDEXED AS EXPECTED.");
            }else{
                IndexColorModel icm = (IndexColorModel)cm;
                String filename = path.getFileName().toString();
                filename = filename.substring(0, filename.lastIndexOf("."));
                palette = new Palette(filename, Palette.fromICM(icm));
            }
        }catch(Exception e){
             LOG.throwing(LOG.getName(), "importImage", e);
        }
        return palette;
    }
    
    public static void exportImage(Palette palette, String filepath, int width, int height, boolean isPng) {
        try {
            LOG.entering(LOG.getName(),"exportImage");
            IndexColorModel icm = palette.getIcm();
            BufferedImage image = new BufferedImage(width, height , BufferedImage.TYPE_BYTE_BINARY, icm);
            File outputfile = new File(filepath);
            LOG.fine("File path : "+outputfile.getAbsolutePath());
            ImageIO.write(image, isPng ? "png" : "gif", outputfile);
            LOG.exiting(LOG.getName(),"exportImage");
        } catch (Exception ex) {
            LOG.throwing(LOG.getName(),"exportImage", ex);
        }       
    }
}
