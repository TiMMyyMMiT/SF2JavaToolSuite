/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapcoords;

import com.sfc.sf2.battle.mapcoords.io.DisassemblyManager;
import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.map.layout.MapLayoutManager;
import com.sfc.sf2.map.layout.io.MapLayoutPackage;

/**
 *
 * @author wiz
 */
public class BattleMapCoordsManager extends AbstractManager {

    private final DisassemblyManager disassemblyManager = new DisassemblyManager();
    private final MapLayoutManager mapLayoutManager = new MapLayoutManager();
    
    private BattleMapCoords[] coords;
    private MapLayout battleMapLayout;
    private String[][] mapEntries = null;

    @Override
    public void clearData() {
        
    }
    
    public void importDisassembly(Path basePath, Path mapEntriesPath, Path battleMapCoordsPath) {
        /*System.out.println("com.sfc.sf2.battlemapcoords.BattleMapCoordsManager.importDisassembly() - Importing disassembly ...");
        mapEntries = disassemblyManager.importMapEntryFile(basePath, mapEntriesPath);
        coords = disassemblyManager.importDisassembly(battleMapCoordsPath);
        System.out.println("com.sfc.sf2.battlemapcoords.BattleMapCoordsManager.importDisassembly() - Disassembly imported.");*/
    }
    
    public void importLayoutDisassembly(BattleMapCoords coords, Path palettesPath, Path tilesetsPath) {
        /*System.out.println("com.sfc.sf2.battlemapcoords.BattleMapCoordsManager.importDisassembly() - Importing disassembly ...");
        int mapIndex = coords.getMap();
        try {
            mapLayoutManager.importDisassembly(palettesPath, tilesetsPath, mapEntries[mapIndex][0], mapEntries[mapIndex][1], mapEntries[mapIndex][2]);
            battleMapLayout = mapLayoutManager.getLayout();
        } catch (DisassemblyException ex) {
            System.getLogger(BattleMapCoordsManager.class.getName()).log(System.Logger.Level.ERROR, (Path) null, ex);
        }
        System.out.println("com.sfc.sf2.battlemapcoords.BattleMapCoordsManager.importDisassembly() - Disassembly imported.");*/
    }
    
    public void exportDisassembly(Path battleMapCoordsPath, BattleMapCoords[] coords) {
        Console.logger().finest("ENTERING exportDisassembly");
        this.coords = coords;
        
        Console.logger().info("Battle coords successfully exported to : " + battleMapCoordsPath);
        Console.logger().finest("EXITING exportDisassembly");   
        /*System.out.println("com.sfc.sf2.battlemapcoords.BattleMapCoordsManager.importDisassembly() - Exporting disassembly ...");
        this.coords = coords;
        disassemblyManager.exportDisassembly(coords,battleMapCoordsPath);
        System.out.println("com.sfc.sf2.battlemapcoords.BattleMapCoordsManager.importDisassembly() - Disassembly exported.");*/
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

    public String[][] getMapEntries() {
        return mapEntries;
    }

    public void setMapEntries(String[][] mapEntries) {
        this.mapEntries = mapEntries;
    }
}
