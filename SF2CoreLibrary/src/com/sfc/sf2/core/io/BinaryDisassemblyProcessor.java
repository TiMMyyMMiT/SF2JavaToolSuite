/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io;

/**
 *
 * @author TiMMy
 */
public class BinaryDisassemblyProcessor extends AbstractDisassemblyProcessor<byte[], EmptyPackage> {

    @Override
    protected byte[] parseDisassemblyData(byte[] data, EmptyPackage pckg) throws DisassemblyException {
        return data;
    }

    @Override
    protected byte[] packageDisassemblyData(byte[] item, EmptyPackage pckg) throws DisassemblyException {
        return item;
    }
    
}
