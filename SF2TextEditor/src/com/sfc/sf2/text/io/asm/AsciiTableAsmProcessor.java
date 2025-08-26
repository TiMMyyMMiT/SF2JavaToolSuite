/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.text.io.asm;

import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.StringHelpers;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class AsciiTableAsmProcessor extends AbstractAsmProcessor<byte[]> {

    @Override
    protected byte[] parseAsmData(BufferedReader reader) throws IOException, AsmException {
        ArrayList<Byte> bytesList = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("table_666E:")) {   //Found start of list
                line = line.substring(11);
                do {
                    if (line.length() == 0) {
                        continue;   //empty line
                    } else if (line.charAt(0) == ';') {
                        break;  //Found end of list
                    }
                    line = StringHelpers.trimAndRemoveComments(line);
                    line = line.replace("dc.b ", "");
                    bytesList.add(StringHelpers.getValueByte(line));
                } while((line = reader.readLine()) != null);
                break;  //End of list
            }
        }
        byte[] data = new byte[bytesList.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = bytesList.get(i);
        }
        return data;
    }

    @Override
    protected String getHeaderName(byte[] item) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void packageAsmData(FileWriter writer, byte[] item) throws IOException, AsmException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
