/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.spellGraphic.io.InvocationDisassemblyProcessor;
import com.sfc.sf2.spellGraphic.io.InvocationMetadataProcessor;
import com.sfc.sf2.spellGraphic.io.InvocationPackage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author TiMMy
 */
public class InvocationGraphicManager extends AbstractManager {
    private final TilesetManager tilesetManager = new TilesetManager();
    private final InvocationDisassemblyProcessor invocationDisassemblyProcessor = new InvocationDisassemblyProcessor();
    private final InvocationMetadataProcessor invocationMetadataProcessor = new InvocationMetadataProcessor();
    
    private InvocationGraphic invocationGraphic;

    @Override
    public void clearData() {
        invocationGraphic = null;
    }
       
    public InvocationGraphic importDisassembly(Path filePath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        InvocationPackage pckg = new InvocationPackage(PathHelpers.filenameFromPath(filePath));
        invocationGraphic = invocationDisassemblyProcessor.importDisassembly(filePath, pckg);
        Console.logger().info("Invocation with " + invocationGraphic.getFrames().length + " frames successfully imported from : " + filePath);
        Console.logger().finest("EXITING importDisassembly");
        return invocationGraphic;
    }
    
    public void exportDisassembly(Path filePath, InvocationGraphic invocationGraphic) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.invocationGraphic = invocationGraphic;
        invocationDisassemblyProcessor.exportDisassembly(filePath, invocationGraphic, null);
        Console.logger().info("Invocation successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public InvocationGraphic importImage(Path filePath) throws RawImageException, IOException, DisassemblyException {
        Console.logger().finest("ENTERING importImage");
        Path dir = filePath.getParent();
        String name = filePath.getFileName().toString();
        String extension = null;
        String meta = null;
        String pattern = null;
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0 && name.length()-dotIndex < 5) {
            extension = name.substring(dotIndex);
        } else {
            extension = AbstractRawImageProcessor.GetFileExtensionString(AbstractRawImageProcessor.FileFormat.PNG);
        }
        int suffixIndex = name.indexOf("-frame-");
        if (suffixIndex > 0) {
            meta = name.substring(0, suffixIndex) + ".meta";
            pattern = name.substring(0, suffixIndex+7);
        } else {
            meta = name + ".meta";
            pattern = name + "-frame-";
        }
        
        List<Tileset> frames = new ArrayList<>();
        File[] files = dir.toFile().listFiles();
        for (File f : files) { 
            if (f.getName().startsWith(pattern) && f.getName().endsWith(extension)) {
                Tileset frame = tilesetManager.importImage(f.toPath(), true);
                frames.add(frame);
            }
        }
        if (frames.isEmpty()) {
            throw new DisassemblyException("ERROR : No frames found for pattern : " + dir.resolve(pattern + "XX" + extension));
        }
        invocationGraphic = new InvocationGraphic(frames.toArray(new Tileset[frames.size()]));
        Console.logger().info("Invocation with " + invocationGraphic.getFrames().length + " frames successfully imported from : " + filePath);
        Path metaPath = dir.resolve(meta);
        try {
            invocationMetadataProcessor.importMetadata(metaPath, invocationGraphic);
            Console.logger().info("Invocation meta successfully imported from : " + metaPath);
        } catch (Exception e) {
            Console.logger().log(Level.SEVERE, "Invocation metadata could not be loaded from : " + metaPath);
        }
        Console.logger().finest("EXITING importImage");
        return invocationGraphic;
    }
    
    public void exportImage(Path filePath, InvocationGraphic invocationGraphic) throws RawImageException, IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportImage");
        Path dir = filePath.getParent();
        String name = filePath.getFileName().toString();
        String extension = null;
        int dotIndex = name.indexOf('.');
        if (dotIndex >= 0) {
            extension = name.substring(dotIndex);
        } else {
            extension = AbstractRawImageProcessor.GetFileExtensionString(AbstractRawImageProcessor.FileFormat.PNG);
        }
        name = name.replaceAll("\\d+\\.[^\\.]+$", "");
        
        this.invocationGraphic = invocationGraphic;
        Tileset[] frames = this.invocationGraphic.getFrames();
        for (int i = 0; i < frames.length; i++) {
            Path framePath = dir.resolve(name + "-frame-" + String.format("%02d", i) + extension);
            tilesetManager.exportImage(framePath, frames[i]);
        }
        Console.logger().info("Invocation successfully exported to : " + dir.resolve(name));
        Path metaPath = dir.resolve(name + ".meta");
        try {
            invocationMetadataProcessor.exportMetadata(metaPath, invocationGraphic);
            Console.logger().info("Invocation meta successfully exported to : " + metaPath);
        } catch (Exception e) {
            Console.logger().log(Level.SEVERE, "Invocation metadata could not be loaded from : " + metaPath);
        }
        Console.logger().finest("EXITING exportImage");
    }

    public InvocationGraphic getInvocationGraphic() {
        return invocationGraphic;
    }

    public void setInvocationGraphic(InvocationGraphic invocationGraphic) {
        this.invocationGraphic = invocationGraphic;
    }
}
