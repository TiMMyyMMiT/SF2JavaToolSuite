/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.graphics;

/**
 *
 * @author TiMMy
 */
public enum TileFlags {
    None((byte)0x0),
    HFlip((byte)0x1),
    VFlip((byte)0x2),
    Priority((byte)0x4);
    
    private byte flag;

    private TileFlags(byte flag) {
        this.flag = flag;
    }
    
    public byte value() {
        return flag;
    }
    
    public void addFlag(TileFlags flag) {
        this.flag |= flag.value();
    }
    
    public void removeFlag(TileFlags flag) {
        this.flag &= ~flag.value();
    }
    
    public boolean isHFlip() {
        return (flag & HFlip.value()) != 0;
    }
    
    public boolean isVFlip() {
        return (flag & VFlip.value()) != 0;
    }
    
    public boolean isBothFlip() {
        return (flag & VFlip.value() & HFlip.value()) != 0;
    }
    
    public boolean isPriority() {
        return (flag & Priority.value()) != 0;
    }
    
    public boolean equals(TileFlags flag) {
        return value() == flag.value();
    }
}
