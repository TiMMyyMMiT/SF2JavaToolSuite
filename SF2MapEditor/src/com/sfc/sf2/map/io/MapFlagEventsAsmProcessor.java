/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.io;

import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.MapStringHelpers;
import com.sfc.sf2.helpers.StringHelpers;
import com.sfc.sf2.map.MapFlagCopyEvent;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class MapFlagEventsAsmProcessor extends AbstractAsmProcessor<MapFlagCopyEvent[], EmptyPackage> {

    @Override
    protected MapFlagCopyEvent[] parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        ArrayList<MapFlagCopyEvent> flagCopiesList = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = StringHelpers.trimAndRemoveComments(line);
            if (line.startsWith("fbcFlag")) {
                int flag, sourceX, sourceY, width, height, destX, destY;
                
                flag = StringHelpers.getValueInt(line.substring(line.indexOf(" ")));
                
                String[] split = MapStringHelpers.getNextLineMulti(reader, "fbcSource", flagCopiesList.size()+1);
                sourceX = StringHelpers.getValueInt(split[0]);
                sourceY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "fbcSize", flagCopiesList.size()+1);
                width = StringHelpers.getValueInt(split[0]);
                height = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "fbcDest", flagCopiesList.size()+1);
                destX = StringHelpers.getValueInt(split[0]);
                destY = StringHelpers.getValueInt(split[1]);
                
                flagCopiesList.add(new MapFlagCopyEvent(flag, sourceX, sourceY, width, height, destX, destY));
            }
        }
        MapFlagCopyEvent[] flagCopies = new MapFlagCopyEvent[flagCopiesList.size()];
        flagCopies = flagCopiesList.toArray(flagCopies);
        return flagCopies;
    }

    @Override
    protected String getHeaderName(MapFlagCopyEvent[] item, EmptyPackage pckg) {
        return "Map flag events";
    }

    @Override
    protected void packageAsmData(FileWriter writer, MapFlagCopyEvent[] item, EmptyPackage pckg) throws IOException, AsmException {
        for (int i = 0; i < item.length; i++) {
            writer.write(String.format("\t\t\t\tfbcFlag %3d\n", item[i].getFlag()));
            writer.write(String.format("\t\t\t\t\tfbcSource %2d, %2d\n", item[i].getSourceX(), item[i].getSourceY()));
            writer.write(String.format("\t\t\t\t\tfbcSize   %2d, %2d\n", item[i].getWidth(), item[i].getHeight()));
            writer.write(String.format("\t\t\t\t\tfbcDest   %2d, %2d\n", item[i].getDestX(), item[i].getDestY()));
        }
        writer.write("\t\t\t\tendWord\n");
    }
}
