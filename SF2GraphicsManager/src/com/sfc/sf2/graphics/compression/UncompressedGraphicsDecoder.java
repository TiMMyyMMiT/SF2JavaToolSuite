/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.compression;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.palette.Palette;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class UncompressedGraphicsDecoder extends AbstractGraphicsDecoder {
    
    @Override
    public Tile[] decode(byte[] input, Palette palette) {
        LOG.entering(LOG.getName(),"decode");
        LOG.fine("Data length = " + input.length + ", -> expecting " + input.length/32 + " tiles to parse.");
        Tile[] tiles = new Tile[input.length/32];
        for(int i=0;i<tiles.length;i++){
            Tile tile = new Tile();
            tile.setId(i);
            tile.setPalette(palette);
            for(int y=0;y<8;y++){
                for(int x=0;x<8;x+=2){
                    byte currentByte = input[i*32+(y*8+x)/2];
                    int firstPixel = (currentByte & 0xF0)/16;
                    int secondPixel = currentByte & 0x0F;
                    tile.setPixel(x, y, firstPixel);
                    tile.setPixel(x+1, y, secondPixel);
                }
            }
            LOG.finest(tile.toString());
            tiles[i] = tile;
        }
        LOG.exiting(LOG.getName(),"decode");
        return tiles;
    }
    
    @Override
    public byte[] encode(Tile[] tiles) {
        LOG.entering(LOG.getName(),"encode");
        LOG.fine("Tiles length = " + tiles.length + ", -> expecting " + tiles.length*32 + " byte output.");
        byte[] output = new byte[tiles.length*32];
        for(int i=0;i<tiles.length;i++){
            int[][] pixels = tiles[i].getPixels();
            for(int y=0;y<8;y++){
                for(int x=0;x<8;x+=2){
                    byte first = (byte)pixels[x][y];
                    byte second = (byte)pixels[x+1][y];
                    output[(i*64+y*8+x)/2] = (byte)(first*16 | second);
                }
            }
        }
        LOG.exiting(LOG.getName(),"encode");
        return output;
    }
}
