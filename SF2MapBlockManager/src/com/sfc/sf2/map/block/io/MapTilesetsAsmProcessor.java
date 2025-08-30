/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.block.io;

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
public class MapTilesetsAsmProcessor extends AbstractAsmProcessor<MapTilesetData> {

    @Override
    protected MapTilesetData parseAsmData(BufferedReader reader) throws IOException, AsmException {
        String line;
        int paletteIndex = -1;
        boolean tilesetFound = false;
        int[] tilesetIndices = new int[5];
        for (int i = 0; i < tilesetIndices.length; i++) {
            tilesetIndices[i] = -1;
        }
        while ((line = reader.readLine()) != null) {
            line = StringHelpers.trimAndRemoveComments(line);
            if (line.length() == 0) continue;
            if (line.startsWith("mapPalette")) {
                if (paletteIndex != -1) {
                    Console.logger().warning("WARNING Map tileset data contains more than one 'mapPalette' reference. Only the first will be used. MapPalette : " + paletteIndex);
                } else {
                    paletteIndex = StringHelpers.getValueInt(line.substring(11));
                }
            } else if (line.startsWith("mapTileset")) {
                int tilesetIndex = StringHelpers.getValueInt(line.substring(10, 11))-1;
                if (tilesetIndex < 0 || tilesetIndex >= tilesetIndices.length) {
                    Console.logger().warning("WARNING Map tileset data contains wrong 'tileset' index. Tileset" + (tilesetIndex+1) + " will be ignored.");
                } else if (tilesetIndices[tilesetIndex] != -1) {
                    Console.logger().warning("WARNING Map tileset data contains same 'tileset' index more than once. Duplicates of tileset" + (tilesetIndex+1) + " will be ignored.");
                } else {
                    tilesetFound = true;
                    tilesetIndices[tilesetIndex] = StringHelpers.getValueInt(line.substring(11));
                }
            }
        }
        if (paletteIndex == -1) {
            throw new AsmException("ERROR Map Tileset data could not be loaded. No palette defined.");
        } else if (!tilesetFound) {
            throw new AsmException("ERROR Map Tileset data could not be loaded. No tileset defined.");
        }
        return new MapTilesetData(paletteIndex, tilesetIndices);
    }

    @Override
    protected String getHeaderName(MapTilesetData item) {
        return "Map Tileset Data";
    }

    @Override
    protected void packageAsmData(FileWriter writer, MapTilesetData item) throws IOException, AsmException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
