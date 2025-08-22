/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimationFrame {
    
    private int index;
    private int duration;
    private int x;
    private int y;
    
    private int weaponFrame;
    private int weaponZ;
    private int weaponX;
    private int weaponY;

    /**
     * For creating Ally animations
     */
    public BattleSpriteAnimationFrame(int index, int duration, int x, int y, int weaponFrame, int weaponZ, int weaponX, int weaponY) {
        this.index = index;
        this.duration = duration;
        this.x = x;
        this.y = y;
        this.weaponFrame = weaponFrame;
        this.weaponZ = weaponZ;
        this.weaponX = weaponX;
        this.weaponY = weaponY;
    }
    
    /**
     * For creating Enemy animations
     */
    public BattleSpriteAnimationFrame(int index, int duration, int x, int y) {
        this.index = index;
        this.duration = duration;
        this.x = x;
        this.y = y;
        this.weaponFrame = -1;
        this.weaponZ = -1;
        this.weaponX = -1;
        this.weaponY = -1;
    }
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
