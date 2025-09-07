/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle;

import com.sfc.sf2.battle.io.BattleSpritesetAsmProcessor;
import com.sfc.sf2.battle.io.BattleSpritesetPackage;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.battle.io.EnemyEnumsAsmProcessor;
import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import com.sfc.sf2.battle.mapcoords.BattleMapCoordsManager;
import com.sfc.sf2.battle.mapterrain.BattleMapTerrain;
import com.sfc.sf2.battle.mapterrain.BattleMapTerrainManager;
import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.core.io.asm.EntriesAsmData;
import com.sfc.sf2.core.io.asm.EntriesAsmProcessor;
import com.sfc.sf2.mapsprite.MapSprite;
import com.sfc.sf2.mapsprite.MapSpriteManager;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author wiz
 */
public class BattleManager extends AbstractManager {
    private final PaletteManager paletteManager = new PaletteManager();
    private final BattleMapCoordsManager mapCoordsManager = new BattleMapCoordsManager();
    private final BattleMapTerrainManager mapTerrainManager = new BattleMapTerrainManager();
    private final MapSpriteManager mapspriteManager = new MapSpriteManager();
    private final BattleSpritesetAsmProcessor battleSpritesetAsmProcessor = new BattleSpritesetAsmProcessor();
    private final EntriesAsmProcessor entriesAsmProcessor = new EntriesAsmProcessor();
    private final EnemyEnumsAsmProcessor enemyEnumsProcessor = new EnemyEnumsAsmProcessor();
    
    private Battle battle;
    private String[][] mapEntries;
    private String[] spritesetEntries;
    private EnemyData[] enemyData;
    private EnemyEnums enemyEnums;

    @Override
    public void clearData() {
        mapCoordsManager.clearData();
        mapTerrainManager.clearData();
        mapspriteManager.clearData();
        battle = null;
        mapEntries = null;
        spritesetEntries = null;
        enemyData = null;
        enemyEnums = null;
    }
        
    public Battle importDisassembly(Path paletteEntriesPath, Path tilesetEntriesPath, Path mapEntriesPath, Path terrainEntriesPath, Path battleMapCoordsPath, Path spritesetEntriesPath, int battleIndex) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        BattleMapCoords[] coords = mapCoordsManager.importDisassembly(mapEntriesPath, battleMapCoordsPath);
        BattleMapTerrain terrain = mapTerrainManager.importDisassembly(paletteEntriesPath, tilesetEntriesPath, terrainEntriesPath, battleIndex, coords);
        BattleSpritesetPackage pckg = new BattleSpritesetPackage(battleIndex, enemyData, enemyEnums);
        BattleSpriteset spriteset = battleSpritesetAsmProcessor.importAsmData(spritesetEntriesPath, pckg);
        battle = new Battle(battleIndex, coords[battleIndex], terrain, spriteset);
        Console.logger().info("Battle " + battleIndex + " and spritesets imported from : " + spritesetEntriesPath);
        Console.logger().finest("EXITING importDisassembly");
        return battle;
    }
    
    public void importMapspriteData(Path basePalettePath, Path mapspriteEntriesPath, Path mapspriteEnumsPath) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importEnemyData");
        if (enemyEnums == null) {
            enemyEnums = enemyEnumsProcessor.importAsmData(mapspriteEnumsPath, null);
            Palette palette = paletteManager.importDisassembly(basePalettePath, true);
            EntriesAsmData entries = entriesAsmProcessor.importAsmData(mapspriteEntriesPath, null);
            enemyData = processEnemyData(enemyEnums, entries, palette);
            Console.logger().info("Mapsprite data loaded from " + mapspriteEntriesPath + " and " + mapspriteEnumsPath);
        } else {
            Console.logger().warning("Mapsprite data already loaded.");
        }
        Console.logger().finest("EXITING importEnemyData");
    }
    
    public void exportDisassembly(Path mapcoordsPath, Path terrainPath, Path spritesetPath, Battle battle) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.battle = battle;
        BattleMapCoords[] allCoords = mapCoordsManager.getCoords();
        allCoords[battle.getIndex()] = battle.getMapCoords();
        mapCoordsManager.exportDisassembly(mapcoordsPath, allCoords);
        mapTerrainManager.exportDisassembly(terrainPath, battle.getTerrain());
        BattleSpritesetPackage pckg = new BattleSpritesetPackage(battle.getIndex(), enemyData, enemyEnums);
        battleSpritesetAsmProcessor.exportAsmData(spritesetPath, battle.getSpriteset(), pckg);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    private EnemyData[] processEnemyData(EnemyEnums enemyEnums, EntriesAsmData mapspriteEntries, Palette palette) {
        ArrayList<EnemyData> enemyDataList = new ArrayList(enemyEnums.getEnemies().size());
        LinkedHashMap<String, Integer> enemies = enemyEnums.getEnemies();
        for (Map.Entry<String, Integer> entry : enemies.entrySet()) {
            EnemyData enemy = new EnemyData();
            enemy.setName(entry.getKey());
            enemy.setID(entry.getValue());

            if (enemy.getMapSprite() == null && entry.getValue()*3 < mapspriteEntries.entriesCount()) {
                try {
                    MapSprite[] mapsprite = mapspriteManager.importDisassembly(mapspriteEntries.getPathForEntry(entry.getValue()*3), palette);
                    enemy.setMapSprite(mapsprite[0]);
                } catch (Exception e) {
                    Console.logger().warning("Map sprite could not be loaded for : " + mapspriteEntries.getPathForEntry(entry.getValue()*3));
                }
            }

            while (enemyDataList.size() <= enemy.getID()) {
                enemyDataList.add(null);
            }
            enemyDataList.set(entry.getValue(), enemy);
        }

        /*for (int i = mapspriteEntries.uniqueEntriesCount()) {
            //Matches enemy id with mapsprite id. Handles special case of ENEMY_MASTER_MAGE_0, ENEMY_NECROMANCER_0, & ENEMY_BLUE_SHAMAN_0
            String name = mapspriteEntries.getEntry(i);
            if (enemies.containsKey(name) || enemies.containsKey(name+"_0")) {
                int index = enemies.get(name);
                if (index < enemyDataList.size() && enemyDataList.get(index) != null && value * 3 < mapsprites.length) {
                    enemyDataList.get(index).setMapSprite(mapsprites[value * 3 + 2]);
                }
            }
        }*/
        /*File enumFile = new File(mapspriteEnumPath);
        Scanner enumScan = new Scanner(enumFile);
        while(enumScan.hasNext()){
            String line = enumScan.nextLine();
            if(line.trim().startsWith("; enum Mapsprites")){
                line = enumScan.nextLine();
                while(!line.startsWith("; enum")){
                    if(line.startsWith("MAPSPRITE")){
                        int valStart = line.indexOf(":");
                        int valEnd = line.indexOf(";");
                        if (valEnd == -1) valEnd = line.length();
                        String key = line.substring(10, valStart);
                        int value = valueOf(line.substring(valStart + 1, valEnd).trim());

                    }
                    line = enumScan.nextLine();
                }
            }
        }*/
        EnemyData[] enemyData = new EnemyData[enemyDataList.size()];
        enemyData = enemyDataList.toArray(enemyData);
        return enemyData;
    }

    public String[][] getMapEntries() {
        return mapEntries;
    }

    public void setMapEntries(String[][] mapEntries) {
        this.mapEntries = mapEntries;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public BattleMapCoords getBattleCoords() {
        return this.battle.getMapCoords();
    }

    public MapLayout getMapLayout() {
        return mapTerrainManager.getMapLayout();
    }

    public EnemyData[] getEnemyData() {
        return enemyData;
    }    

    public EnemyEnums getEnemyEnums() {
        return enemyEnums;
    }
}
