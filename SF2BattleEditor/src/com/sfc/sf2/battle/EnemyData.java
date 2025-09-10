/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle;

import com.sfc.sf2.mapsprite.MapSprite;

/**
 *
 * @author TiMMy
 */
public class EnemyData {
    private int id;
    private String name;
    private MapSprite mapSprite;

    public EnemyData(int id, String name, MapSprite mapSprite) {
        this.id = id;
        this.name = name;
        this.mapSprite = mapSprite;
    }

    public int getID() {
        return id;
    }
        
    public String getName() {
        return name;
    }

    public MapSprite getMapSprite() {
        return mapSprite;
    }
}
