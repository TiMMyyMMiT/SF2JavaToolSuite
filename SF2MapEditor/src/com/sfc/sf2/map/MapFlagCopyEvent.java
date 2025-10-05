/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map;

/**
 *
 * @author wiz
 */
public class MapFlagCopyEvent extends MapCopyEvent {
    
    public MapFlagCopyEvent(int flag, int sourceX, int sourceY, int width, int height, int destX, int destY) {
        super(flag, flag, sourceX, sourceY, width, height, destX, destY);
    }

    public int getFlag() {
        return getSourceX();
    }

    public void setFlag(int flag) {
        setSourceX(flag);
    }
    
    public static MapFlagCopyEvent createEmpty() {
        return new MapFlagCopyEvent(100, 0, 0, 1, 1, 1, 1);
    }

    @Override
    public MapFlagCopyEvent clone() {
        return new MapFlagCopyEvent(getFlag(), getSourceX(), getSourceY(), getWidth(), getHeight(), getDestX(), getDestY());
    }
}
