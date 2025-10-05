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
import com.sfc.sf2.map.MapCopyEvent;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class MapStepEventsAsmProcessor extends AbstractAsmProcessor<MapCopyEvent[], EmptyPackage> {

    @Override
    protected MapCopyEvent[] parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        ArrayList<MapCopyEvent> stepCopiesList = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = StringHelpers.trimAndRemoveComments(line);
            if (line.startsWith("sbc")) {
                int triggerX, triggerY, sourceX, sourceY, width, height, destX, destY;
                
                String[] split = line.substring(line.indexOf(" ")).split(",");
                triggerX = StringHelpers.getValueInt(split[0]);
                triggerY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "sbcSource", stepCopiesList.size()+1);
                sourceX = StringHelpers.getValueInt(split[0]);
                sourceY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "sbcSize", stepCopiesList.size()+1);
                width = StringHelpers.getValueInt(split[0]);
                height = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "sbcDest", stepCopiesList.size()+1);
                destX = StringHelpers.getValueInt(split[0]);
                destY = StringHelpers.getValueInt(split[1]);
                
                stepCopiesList.add(new MapCopyEvent(triggerX, triggerY, sourceX, sourceY, width, height, destX, destY));
            }
        }
        MapCopyEvent[] stepCopies = new MapCopyEvent[stepCopiesList.size()];
        stepCopies = stepCopiesList.toArray(stepCopies);
        return stepCopies;
    }

    @Override
    protected String getHeaderName(MapCopyEvent[] item, EmptyPackage pckg) {
        return "Map step events";
    }

    @Override
    protected void packageAsmData(FileWriter writer, MapCopyEvent[] item, EmptyPackage pckg) throws IOException, AsmException {
        writer.write("\n");
        for (int i = 0; i < item.length; i++) {
            writer.write(String.format("\t\t\t\tsbc %d, %d\n", item[i].getTriggerX(), item[i].getTriggerY()));
            writer.write(String.format("\t\t\t\t\tsbcSource\t%d, %d\n", item[i].getSourceX(), item[i].getSourceY()));
            writer.write(String.format("\t\t\t\t\tsbcSize\t%d, %d\n", item[i].getWidth(), item[i].getHeight()));
            writer.write(String.format("\t\t\t\t\tsbcDest\t%d, %d\n", item[i].getDestX(), item[i].getDestY()));
        }
        writer.write("\t\t\t\tendWord\n");
    }
}
