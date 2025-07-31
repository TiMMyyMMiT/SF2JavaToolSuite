/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation;

import com.sfc.sf2.graphics.Tile;
import java.awt.Color;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimationFrame {
    
    public static final int TYPE_ALLY = 0;
    public static final int TYPE_ENEMY = 1;
    
    private int type;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    private int index;
    private int duration;
    private int x;
    private int y;
    
    private int weaponFrame;
    private int weaponZ;
    private int weaponX;
    private int weaponY;   

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWeaponFrame() {
        return weaponFrame;
    }

    public void setWeaponFrame(int weaponFrame) {
        this.weaponFrame = weaponFrame;
    }

    public int getWeaponZ() {
        return weaponZ;
    }

    public void setWeaponZ(int weaponZ) {
        this.weaponZ = weaponZ;
    }

    public int getWeaponX() {
        return weaponX;
    }

    public void setWeaponX(int weaponX) {
        this.weaponX = weaponX;
    }

    public int getWeaponY() {
        return weaponY;
    }

    public void setWeaponY(int weaponY) {
        this.weaponY = weaponY;
    }
}
