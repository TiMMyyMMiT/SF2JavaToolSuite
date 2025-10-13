/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.io;

import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.MapStringHelpers;
import com.sfc.sf2.helpers.StringHelpers;
import com.sfc.sf2.map.MapArea;
import com.sfc.sf2.map.MapEnums;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class MapAreaAsmProcessor extends AbstractAsmProcessor<MapArea[], MapEnums> {

    @Override
    protected MapArea[] parseAsmData(BufferedReader reader, MapEnums pckg) throws IOException, AsmException {
        ArrayList<MapArea> areasList = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = StringHelpers.trimAndRemoveComments(line);
            if (line.startsWith("mainLayerStart")) {
                int l1StartX, l1StartY, l1EndX, l1EndY;
                int fg2StartX, fg2StartY, bg2StartX, bg2StartY;
                int l1ParallaxX, l1ParallaxY, l2ParallaxX, l2ParallaxY;
                int l1AutoscrollX, l1AutoscrollY, l2AutoscrollX, l2AutoscrollY;
                int layerType;
                String defaultMusic;
                
                String[] split = line.substring(line.indexOf(" ")).split(",");
                l1StartX = StringHelpers.getValueInt(split[0]);
                l1StartY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "mainLayerEnd", areasList.size()+1);
                l1EndX = StringHelpers.getValueInt(split[0]);
                l1EndY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "scndLayerFgndStart", areasList.size()+1);
                fg2StartX = StringHelpers.getValueInt(split[0]);
                fg2StartY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "scndLayerBgndStart", areasList.size()+1);
                bg2StartX = StringHelpers.getValueInt(split[0]);
                bg2StartY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "mainLayerParallax", areasList.size()+1);
                l1ParallaxX = StringHelpers.getValueInt(split[0]);
                l1ParallaxY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "scndLayerParallax", areasList.size()+1);
                l2ParallaxX = StringHelpers.getValueInt(split[0]);
                l2ParallaxY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "mainLayerAutoscroll", areasList.size()+1);
                l1AutoscrollX = StringHelpers.getValueInt(split[0]);
                l1AutoscrollY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "scndLayerAutoscroll", areasList.size()+1);
                l2AutoscrollX = StringHelpers.getValueInt(split[0]);
                l2AutoscrollY = StringHelpers.getValueInt(split[1]);
                
                layerType = StringHelpers.getValueInt(MapStringHelpers.getNextLineSingle(reader, "mainLayerType", areasList.size()+1));
                defaultMusic = MapStringHelpers.getNextLineSingle(reader, "areaDefaultMusic", areasList.size()+1);
                defaultMusic = MapEnums.toEnumString(defaultMusic, pckg.getMusic());
                
                areasList.add(new MapArea(l1StartX, l1StartY, l1EndX, l1EndY, fg2StartX, fg2StartY, bg2StartX, bg2StartY, l1ParallaxX, l1ParallaxY, l2ParallaxX, l2ParallaxY, l1AutoscrollX, l1AutoscrollY, l2AutoscrollX, l2AutoscrollY, layerType, defaultMusic));
            }
        }
        MapArea[] areas = new MapArea[areasList.size()];
        areas = areasList.toArray(areas);
        return areas;
    }

    @Override
    protected String getHeaderName(MapArea[] item, MapEnums pckg) {
        return "Map areas";
    }

    @Override
    protected void packageAsmData(FileWriter writer, MapArea[] item, MapEnums pckg) throws IOException, AsmException {
        writer.write("\n");
        for (int i = 0; i < item.length; i++) {
            writer.write(String.format("\t\t\t\t\tmainLayerStart      %3d, %3d\n", item[i].getLayer1StartX(), item[i].getLayer1StartY()));
            writer.write(String.format("\t\t\t\t\tmainLayerEnd        %3d, %3d\n", item[i].getLayer1EndX(), item[i].getLayer1EndY()));
            writer.write(String.format("\t\t\t\t\tscndLayerFgndStart  %3d, %3d\n", item[i].getForegroundLayer2StartX(), item[i].getForegroundLayer2StartY()));
            writer.write(String.format("\t\t\t\t\tscndLayerBgndStart  %3d, %3d\n", item[i].getBackgroundLayer2StartX(), item[i].getBackgroundLayer2StartY()));
            writer.write(String.format("\t\t\t\t\tmainLayerParallax   %3d, %3d\n", item[i].getLayer1ParallaxX(), item[i].getLayer1ParallaxY()));
            writer.write(String.format("\t\t\t\t\tscndLayerParallax   %3d, %3d\n", item[i].getLayer2ParallaxX(), item[i].getLayer2ParallaxY()));
            writer.write(String.format("\t\t\t\t\tmainLayerAutoscroll %3d, %3d\n", item[i].getLayer1AutoscrollX(), item[i].getLayer1AutoscrollY()));
            writer.write(String.format("\t\t\t\t\tscndLayerAutoscroll %3d, %3d\n", item[i].getLayer2AutoscrollX(), item[i].getLayer2AutoscrollY()));
            writer.write(String.format("\t\t\t\t\tmainLayerType       %3d\n", item[i].getLayerType()));
            int music = MapEnums.toEnumInt(item[i].getDefaultMusic(), pckg.getMusic());
            writer.write(String.format("\t\t\t\t\tareaDefaultMusic    %3d\n", music));
            writer.write("\n");
        }
        writer.write("\t\t\t\tendWord\n");
    }
}
