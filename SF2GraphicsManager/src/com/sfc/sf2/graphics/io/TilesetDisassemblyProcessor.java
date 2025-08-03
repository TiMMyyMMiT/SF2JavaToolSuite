/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.compression.BasicGraphicsDecoder;
import com.sfc.sf2.graphics.compression.StackGraphicsDecoder;
import com.sfc.sf2.graphics.compression.UncompressedGraphicsDecoder;
import com.sfc.sf2.helpers.BinaryHelpers;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.palette.Palette;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author TiMMy
 */
public class TilesetDisassemblyProcessor extends AbstractDisassemblyProcessor<Tileset, TilesetPackage> {

    public enum TilesetCompression {
        NONE,
        BASIC,
        STACK,
    }
    
    @Override
    protected Tileset parseDisassemblyData(byte[] data, TilesetPackage pckg) throws DisassemblyException {
        Tile[] tiles = null;
        switch (pckg.compression()) {
            case NONE:
                tiles = new UncompressedGraphicsDecoder().decode(data, pckg.palette());
                break;
            case BASIC:
                tiles = new BasicGraphicsDecoder().decode(data, pckg.palette());
                break;
            case STACK:
                tiles = new StackGraphicsDecoder().decode(data, pckg.palette());
                break;
            default:
                throw new DisassemblyException("Compression mode not recognosed. Compression : " + pckg.compression());
        }
        if (tiles == null || tiles.length == 0) {
            throw new DisassemblyException("Tileset not loaded. Tiles are empty.");
        }
        return new Tileset(pckg.name(), tiles, pckg.tilesPerRow());
    }

    @Override
    protected byte[] packageDisassemblyData(Tileset item, TilesetPackage pckg) throws DisassemblyException {
        byte[] bytes = null;
        switch(pckg.compression()){
            case NONE:
                bytes = new UncompressedGraphicsDecoder().encode(item.getTiles());
                break;
            case BASIC:
                bytes = new BasicGraphicsDecoder().encode(item.getTiles());
                break;
            case STACK:
                bytes = new StackGraphicsDecoder().encode(item.getTiles());
                break;
            default:
                throw new DisassemblyException("Compression mode not recognosed. Compression : " + pckg.compression());
        }
        if (bytes == null || bytes.length == 0) {
            throw new DisassemblyException("Tileset not loaded. Tiles are empty.");
        }
        return bytes;
    }

    public Tile[] importDisassemblyWithLayout(Path baseTilesetPath, Palette[] palettes, Path tileset1FilePath, String tileset1Offset, Path tileset2FilePath, String tileset2Offset, TilesetCompression compression, int tilesPerRow, Path layoutPath) {
        try {
            Console.logger().finest("ENTERING importDisassemblyWithLayout");
            TilesetDisassemblyProcessor processor = new TilesetDisassemblyProcessor();
            TilesetPackage basePckg = new TilesetPackage(PathHelpers.filenameFromPath(baseTilesetPath), TilesetCompression.STACK, palettes[0], tilesPerRow);
            Tile[] baseTiles = processor.importDisassembly(baseTilesetPath, basePckg).getTiles();
            TilesetPackage tilesetPckg1 = new TilesetPackage(PathHelpers.filenameFromPath(tileset1FilePath), compression, palettes[0], tilesPerRow);
            TilesetPackage tilesetPckg2 = new TilesetPackage(PathHelpers.filenameFromPath(tileset2FilePath), compression, palettes[0], tilesPerRow);
            Tile[] tileset1 = processor.importDisassembly(tileset1FilePath, tilesetPckg1).getTiles();
            Tile[] tileset2 = processor.importDisassembly(tileset2FilePath, tilesetPckg2).getTiles();
            Tile[] vRamTiles = new Tile[0x800];
            Tile[] tiles = null;
            int t1offset = Integer.valueOf(tileset1Offset, 16) / 0x20;
            int t2offset = Integer.valueOf(tileset2Offset, 16) / 0x20;
            System.arraycopy(baseTiles, 0, vRamTiles, 0, baseTiles.length);
            System.arraycopy(tileset1, 0, vRamTiles, t1offset, tileset1.length);
            System.arraycopy(tileset2, 0, vRamTiles, t2offset, tileset2.length);
            try {
                byte[] data = Files.readAllBytes(layoutPath);
                Tile[] layoutTiles = new Tile[data.length/2];
                for(int i=0;i<layoutTiles.length;i++){
                    int layoutValue = BinaryHelpers.getWord(data,i*2);
                    int priority = (layoutValue&0x8000)>>15;
                    int palette = (layoutValue&0x6000)>>13;
                    int vFlip = (layoutValue&0x1000)>>12;
                    int hFlip = (layoutValue&0x0800)>>11;
                    int tileId = (layoutValue&0x7FF);
                    if(tileId>=0&&tileId<vRamTiles.length){
                        Tile outputTile = vRamTiles[tileId];
                        if(outputTile!=null&&palette!=0){
                            outputTile = Tile.paletteSwap(outputTile,palettes[palette]);
                        }
                        if(outputTile!=null&&vFlip!=0){
                            outputTile = Tile.vFlip(outputTile);
                        }
                        if(outputTile!=null&&hFlip!=0){
                            outputTile = Tile.hFlip(outputTile);
                        }
                        layoutTiles[i] = outputTile;
                    }
                    if(layoutTiles[i]==null){
                        Console.logger().fine("Layout tile "+i+" : wrong tile id "+tileId);
                        layoutTiles[i] = baseTiles[0];
                    }
                }
                tiles = layoutTiles;
            } catch (IOException ex) {
                Console.logger().log(Level.SEVERE, null, ex);
            }
            Console.logger().finest("EXITING importDisassemblyWithLayout");
            return tiles;
        } catch (DisassemblyException | IOException ex) {
            System.getLogger(TilesetDisassemblyProcessor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return null;
        }
    }

    public void exportTilesAndLayout(Tile[] tiles, Path tilesPath, Path layoutPath, String graphicsOffset, TilesetCompression compression, Palette palette) {
        Console.logger().finest("ENTERING exportTilesAndLayout");
        
        int vramTileOffset = Integer.parseInt(graphicsOffset,16) / 0x20;
        try{
            int tilesetIndex = -1;
            byte[] layout = new byte[tiles.length*2];
            List<Tile> tilesList = new ArrayList();
            for(int i=0;i<tiles.length;i++){
                Tile tile = tiles[i];
                for(int j=0;j<tilesList.size();j++){
                    Tile t = tilesList.get(j);
                    if(t.equals(tile)){
                        tilesetIndex = j + vramTileOffset;
                        break;
                    }
                }
                if(tilesetIndex==-1){
                    tilesList.add(tile);
                    tilesetIndex = tilesList.size()-1+vramTileOffset;
                }
                layout[i*2] = (byte)(((tilesetIndex&0x700)>>8)&0xFF);
                layout[i*2+1] = (byte)(tilesetIndex&0xFF);
                tilesetIndex=-1;
            }

            Tile[] outputTiles = new Tile[tilesList.size()];
            outputTiles = tilesList.toArray(outputTiles);

            Tileset tileset = new Tileset(PathHelpers.filenameFromPath(tilesPath), outputTiles, 0);
            TilesetPackage pckg = new TilesetPackage(tileset.getName(), compression, palette, 0);
            this.exportDisassembly(tilesPath, tileset, pckg);
            Files.write(layoutPath, layout);
        }catch(Exception e){
            Console.logger().log(Level.SEVERE, null, e);
        }
        Console.logger().finest("EXITING exportTilesAndLayout");
    }
}
