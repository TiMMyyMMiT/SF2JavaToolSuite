/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.weaponsprite;

import com.sfc.sf2.graphics.Tile;
import java.awt.Color;

/**
 *
 * @author wiz
 */
public class WeaponSprite {
    
    private int index;
    
    private Tile[][] frames;
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Tile[][] getFrames() {
        return frames;
    }

    public void setFrames(Tile[][] frames) {
        this.frames = frames;
    }
}
