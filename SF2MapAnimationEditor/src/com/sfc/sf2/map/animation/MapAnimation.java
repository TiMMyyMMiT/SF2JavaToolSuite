/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.animation;

/**
 *
 * @author wiz
 */
public class MapAnimation {
    
    private int tileset;
    private int length;
    private MapAnimationFrame[] frames;

    public MapAnimation(int tileset, int length, MapAnimationFrame[] frames) {
        this.tileset = tileset;
        this.length = length;
        this.frames = frames;
    }

    public int getTileset() {
        return tileset;
    }

    public void setTileset(int tileset) {
        this.tileset = tileset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public MapAnimationFrame[] getFrames() {
        return frames;
    }

    public void setFrames(MapAnimationFrame[] frames) {
        this.frames = frames;
    }
}
