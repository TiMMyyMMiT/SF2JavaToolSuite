/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic;

import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;

/**
 *
 * @author TiMMy
 */
public class InvocationGraphic {

    private Tileset[] frames;
    private short unknown1;
    private short unknown2;
    private short unknown3;
    
    public InvocationGraphic(Tileset[] frames, short unknown1, short unknown2, short unknown3) {
        this.frames = frames;
        this.unknown1 = unknown1;
        this.unknown2 = unknown2;
        this.unknown3 = unknown3;
    }

    public Tileset[] getFrames() {
        return frames;
    }

    public void setFrames(Tileset[] frames) {
        this.frames = frames;
    }

    public Palette getPalette() {
        if (frames == null || frames.length == 0) {
            return null;
        }
        return frames[0].getPalette();
    }

    public short getUnknown1() {
        return unknown1;
    }

    public void setUnknown1(short unknown1) {
        this.unknown1 = unknown1;
    }

    public short getUnknown2() {
        return unknown2;
    }

    public void setUnknown2(short unknown2) {
        this.unknown2 = unknown2;
    }

    public short getUnknown3() {
        return unknown3;
    }

    public void setUnknown3(short unknown3) {
        this.unknown3 = unknown3;
    }
}
