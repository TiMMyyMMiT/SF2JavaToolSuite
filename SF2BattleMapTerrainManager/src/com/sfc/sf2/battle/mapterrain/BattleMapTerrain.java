/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain;

/**
 *
 * @author wiz
 */
public class BattleMapTerrain {
    
    public static String[] TERRAIN_BASE_NAMES = new String[] { "Sky/Wall", "Plains", "Path", "Overgrowth", "Forest", "Hills", "Desert", "Mountain", "Water" };
    public static String[] TERRAIN_EXTENDED_NAMES = new String[] { "Sky/Wall", "Plains", "Path", "Overgrowth", "Forest", "Hills", "Desert", "Mountain", "Water", "Add1", "Add2", "Add3", "Add4", "Add5", "Add6", "Add7" };
    
    private byte[] data;

    public BattleMapTerrain(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
