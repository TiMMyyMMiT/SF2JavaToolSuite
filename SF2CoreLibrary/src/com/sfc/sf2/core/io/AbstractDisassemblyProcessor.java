/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TiMMy
 * @param <TType> The type of data being loaded
 * @param <TPackage> The input data required to load the TType
 */
public abstract class AbstractDisassemblyProcessor<TType extends Object, TPackage extends Object> {
    protected static final Logger LOG = Logger.getLogger(AbstractDisassemblyProcessor.class.getName());
    
    public TType importDisassembly(Path filePath, TPackage pckg) {
        LOG.entering(LOG.getName(), "importDisassembly : " + filePath);
        TType item = null;
        try {
            byte[] data = Files.readAllBytes(filePath);
            item = parseDisassemblyData(data, pckg);
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not import disassembly : " + filePath, e);
            item = null;
        }
        LOG.exiting(LOG.getName(), "importDisassembly");
        return item;
    }
    
    protected abstract TType parseDisassemblyData(byte[] data, TPackage pckg) throws DisassemblyException;
    
    public boolean exportDisassembly(Path filePath, TType item, TPackage pckg) {
        LOG.entering(LOG.getName(), "exportDisassembly : " + filePath);
        boolean error = false;
        try {
            byte[] disasmData = packageDisassemblyData(item, pckg);
            Files.write(filePath, disasmData);
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not export disassembly : " + filePath, e);
            error = true;
        }
        LOG.exiting(LOG.getName(), "exportDisassembly");
        return error;
    }

    protected abstract byte[] packageDisassemblyData(TType item, TPackage pckg) throws DisassemblyException;
}
