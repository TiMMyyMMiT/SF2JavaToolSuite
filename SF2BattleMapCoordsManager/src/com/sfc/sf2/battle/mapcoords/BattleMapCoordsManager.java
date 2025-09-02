/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapcoords;

import com.sfc.sf2.battle.mapcoords.io.BattleMapCoordsAsmProcessor;
import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.map.layout.MapLayoutManager;
import com.sfc.sf2.map.layout.io.MapEntriesAsmProcessor;
import com.sfc.sf2.map.layout.io.MapEntryData;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class BattleMapCoordsManager extends AbstractManager {

    private final MapLayoutManager mapLayoutManager = new MapLayoutManager();
    private final MapEntriesAsmProcessor mapEntriesAsmProcessor = new MapEntriesAsmProcessor();
    private final BattleMapCoordsAsmProcessor coordsAsmProcessor = new BattleMapCoordsAsmProcessor();
    
    private BattleMapCoords[] coords;
    private MapLayout battleMapLayout;
    private MapEntryData[] mapEntries = null;

    @Override
    public void clearData() {
        
    }
    
    public BattleMapCoords[] importDisassembly(Path mapEntriesPath, Path battleMapCoordsPath) throws IOException, AsmException {
        Console.logger().finest("ENTERING importDisassembly");
        mapEntries = mapEntriesAsmProcessor.importAsmData(mapEntriesPath);
        coords = coordsAsmProcessor.importAsmData(battleMapCoordsPath);
        Console.logger().info("Battle map coords successfully imported from : " + battleMapCoordsPath);
        Console.logger().finest("EXITING importDisassembly");
        return coords;
    }
    
    public MapLayout importMap(Path paletteEntriesPath, Path tilesetsEntriesPath, int mapId) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importMap");
        if (mapId < 0 || mapId >= mapEntries.length || mapEntries[mapId] == null) {
            throw new DisassemblyException("Cannot import map " + mapId + ". Data was not found or is corrupt.");
        }
        MapEntryData mapEntry = mapEntries[mapId];
        battleMapLayout = mapLayoutManager.importDisassemblyFromMapEntry(paletteEntriesPath, tilesetsEntriesPath, mapEntry);
        Console.logger().info("Map data imported for map : " + mapEntry.getMapId());
        Console.logger().finest("EXITING importMap");
        return battleMapLayout;
    }
    
    public void exportDisassembly(Path battleMapCoordsPath, BattleMapCoords[] coords) throws IOException, AsmException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.coords = coords;
        coordsAsmProcessor.exportAsmData(battleMapCoordsPath, coords);
        Console.logger().info("Battle coords successfully exported to : " + battleMapCoordsPath);
        Console.logger().finest("EXITING exportDisassembly");
    }

    public BattleMapCoords[] getCoords() {
        return coords;
    }

    public void setCoords(BattleMapCoords[] coords) {
        this.coords = coords;
    }

    public MapLayout getMapLayout() {
        return battleMapLayout;
    }
}
