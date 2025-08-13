/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io.asm;

import com.sfc.sf2.helpers.StringHelpers;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author TiMMy
 */
public class EntriesAsmProcessor extends AbstractAsmProcessor<EntriesAsmData> {

    @Override
    protected EntriesAsmData parseAsmData(BufferedReader reader) throws IOException, AsmException {
        EntriesAsmData data = new EntriesAsmData();
        String line;
        boolean foundStart = false;
        while ((line = reader.readLine()) != null) {
            if (line.length() == 0 || line.charAt(0) == ';') {
                continue;
            } else if (line.length() > 5 && !foundStart) {
                foundStart = true;
                line = line.substring(line.indexOf(':')+1);
            }

            if (foundStart) {
                line = StringHelpers.trimAndRemoveComments(line);
                int incbinIndex = line.indexOf("incbin");
                if (incbinIndex > 0) {          //Is filepath
                    String entry = line.substring(0, line.indexOf(':'));
                    Path path = Path.of(line.substring(incbinIndex+8, line.length()-1));
                    data.addPath(entry, path);
                }
                if (line.startsWith("dc.")) {   //Is entry
                    String entry = line.substring(5);
                    data.addEntry(entry);
                }
            }
        }
        return data;
    }

    @Override
    protected void packageAsmData(FileWriter writer, EntriesAsmData item) throws IOException, AsmException {
        writer.write("; Map sprites\n");
        writer.write("pt_Mapsprites:\n");
        for (int i = 0; i < item.entriesCount(); i++) {
            writer.write("/t/t/tdc.l ");
            writer.write(item.getEntry(i));
            writer.write("\n");
        }
        for (int i = 0; i < item.uniquePathsCount(); i++) {
            writer.write(item.getUniqueEntries(i));
            writer.write(" incbin \"");
            writer.write(item.getPathForUnique(i).toString());
            writer.write("\"\n");
        }
    }
}
