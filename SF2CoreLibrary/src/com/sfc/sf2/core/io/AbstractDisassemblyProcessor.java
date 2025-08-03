/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io;

import com.sfc.sf2.core.gui.controls.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

/**
 *
 * @author TiMMy
 * @param <TType> The type of data being loaded
 * @param <TPackage> The input data required to load the TType
 */
public abstract class AbstractDisassemblyProcessor<TType extends Object, TPackage extends Object> {
    
    public TType importDisassembly(Path filePath, TPackage pckg) throws DisassemblyException, IOException {
        
        Console.logger().finest("ENTERING importDisassembly : " + filePath);
        TType item = null;
        byte[] data = Files.readAllBytes(filePath);
        item = parseDisassemblyData(data, pckg);
        Console.logger().finest("EXITING importDisassembly");
        return item;
    }
    
    protected abstract TType parseDisassemblyData(byte[] data, TPackage pckg) throws DisassemblyException;
    
    public void exportDisassembly(Path filePath, TType item, TPackage pckg) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly : " + filePath);
        byte[] disasmData = packageDisassemblyData(item, pckg);
        Files.write(filePath, disasmData);
        Console.logger().finest("EXITING exportDisassembly");
    }

    protected abstract byte[] packageDisassemblyData(TType item, TPackage pckg) throws DisassemblyException;
}
