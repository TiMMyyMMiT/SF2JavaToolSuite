/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.layout.io;

import com.sfc.sf2.core.io.AbstractMetadataProcessor;
import com.sfc.sf2.core.io.MetadataException;
import com.sfc.sf2.graphics.Block;
import com.sfc.sf2.graphics.TileFlags;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author TiMMy
 */
public class MapLayoutMetaProcessor extends AbstractMetadataProcessor<MapBlockset> {
        
    @Override
    protected void parseMetaData(BufferedReader reader, MapBlockset item) throws IOException, MetadataException {
        int lineIndex = 0;
        int cursor = 0;
        int blocksPerRow = item.getBlocksPerRow();
        String line;
        while ((line = reader.readLine()) != null) {
            while (cursor < line.length()) {
                int blockIndex = lineIndex*blocksPerRow + cursor/2;
                int value = Integer.parseInt(line.substring(cursor, cursor+2), 16);
                item.getBlocks()[blockIndex].setFlags(value<<8);
                cursor++;
            }
            cursor = 0;
            lineIndex++;
        }
    }

    @Override
    protected void packageMetaData(FileWriter writer, MapBlockset item) throws IOException, MetadataException {
        int blocksPerRow = item.getBlocksPerRow();
        MapBlock[] blocks = item.getBlocks();
        int rows = blocks.length/blocksPerRow;
        if (blocks.length%blocksPerRow != 0) rows++;
        for (int b = 0; b < blocks.length; b++) {
            if (b > 0 && (b%blocksPerRow) == 0) {
                writer.write("\n");
            }
            writer.write(String.format("%02X", blocks[b].getFlags()>>8));
        }
    }
}
