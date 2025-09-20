/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.animation;

import com.sfc.sf2.graphics.Tileset;

/**
 *
 * @author wiz
 */
public class MapAnimation {
    
    private int tilesetId;
    private int length;
    private Tileset tileset;
    private MapAnimationFrame[] frames;

    public MapAnimation(int tilesetId, int length, MapAnimationFrame[] frames) {
        this.tilesetId = tilesetId;
        this.length = length;
        this.frames = frames;
    }

    public int getTilesetId() {
        return tilesetId;
    }

    public void setTilesetId(int tilesetId) {
        this.tilesetId = tilesetId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Tileset getTileset() {
        return tileset;
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
    }

    public MapAnimationFrame[] getFrames() {
        return frames;
    }

    public void setFrames(MapAnimationFrame[] frames) {
        this.frames = frames;
    }
}
