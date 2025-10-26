/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle;

import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import com.sfc.sf2.battle.mapterrain.BattleMapTerrain;

/**
 *
 * @author wiz
 */
public class Battle {
    
    private int index;
    private BattleMapCoords mapCoords;
    private BattleMapTerrain terrain;
    private BattleSpriteset spriteset;

    public Battle(int index, BattleMapCoords mapCoords, BattleMapTerrain terrain, BattleSpriteset spriteset) {
        this.index = index;
        this.mapCoords = mapCoords;
        this.terrain = terrain;
        this.spriteset = spriteset;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public BattleMapCoords getMapCoords() {
        return mapCoords;
    }

    public void setMapCoords(BattleMapCoords mapCoords) {
        this.mapCoords = mapCoords;
    }

    public BattleMapTerrain getTerrain() {
        return terrain;
    }

    public void setTerrain(BattleMapTerrain terrain) {
        this.terrain = terrain;
    }

    public BattleSpriteset getSpriteset() {
        return spriteset;
    }

    public void setSpriteset(BattleSpriteset spriteset) {
        this.spriteset = spriteset;
    }
}
