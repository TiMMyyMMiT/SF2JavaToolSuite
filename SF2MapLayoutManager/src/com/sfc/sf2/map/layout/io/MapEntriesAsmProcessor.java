/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.layout.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.StringHelpers;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author TiMMy
 */
public class MapEntriesAsmProcessor extends AbstractAsmProcessor<MapEntryData[]> {

    @Override
    protected MapEntryData[] parseAsmData(BufferedReader reader) throws IOException, AsmException {
        MapEntryData[] items = new MapEntryData[255];   //Number large enough to not be likely to be reached
        String line;
        int largest = 0;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Map")) {   //Found a map data item
                int numIndex = 6;
                int underscoreIndex = line.indexOf('_');
                int colonIndex = line.indexOf(':');
                int includeIndex = line.indexOf("include")+7;
                if (includeIndex == -1) includeIndex = line.indexOf("incbin")+6;
                int commentIndex = line.indexOf(';');
                int mapNum = StringHelpers.getNumberFromString(line.substring(3, numIndex));
                if (items[mapNum] == null) {
                    items[mapNum] = new MapEntryData(mapNum);
                    if (mapNum > largest) largest = mapNum;
                }
                MapEntryData item = items[mapNum];
                String identifier;
                if (underscoreIndex == -1 || underscoreIndex > colonIndex) {
                    identifier = "Tilesets";
                } else {
                    identifier = line.substring(underscoreIndex+1, colonIndex);
                }
                String path = commentIndex == -1 ? line.substring(includeIndex) : line.substring(includeIndex, commentIndex);
                path = path.trim();
                switch (identifier) {
                    case "Tilesets":
                        item.setTilesetsPath(path);
                        break;
                    case "Blocks":
                        item.setBlocksPath(path);
                        break;
                    case "Layout":
                        item.setLayoutPath(path);
                        break;
                    case "Areas":
                        item.setAreasPath(path);
                        break;
                    case "FlagEvents":
                        item.setFlagEventsPath(path);
                        break;
                    case "StepEvents":
                        item.setStepEventsPath(path);
                        break;
                    case "RoofEvents":
                        item.setRoofEventsPath(path);
                        break;
                    case "WarpEvents":
                        item.setWarpEventsPath(path);
                        break;
                    case "ChestItems":
                        item.setChestItemsPath(path);
                        break;
                    case "OtherItems":
                        item.setOtherItemsPath(path);
                        break;
                    case "Animations":
                        item.setAnimationsPath(path);
                        break;
                    default:
                        Console.logger().warning("WARNING Identifier '" + identifier + "' unknown in map entries.asm.");
                        break;
                }
            }
        }
        if (largest == 0) {
            throw new AsmException("Map entries data was not found.");
        }
        MapEntryData[] finalItems = new MapEntryData[largest+1];
        System.arraycopy(items, 0, finalItems, 0, largest+1);
        return finalItems;
    }

    @Override
    protected String getHeaderName(MapEntryData[] item) {
        return "Map entries";
    }

    @Override
    protected void packageAsmData(FileWriter writer, MapEntryData[] item) throws IOException, AsmException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
