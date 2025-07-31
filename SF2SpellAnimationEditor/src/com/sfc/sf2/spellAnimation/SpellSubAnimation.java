/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellAnimation;

/**
 *
 * @author TiMMy
 */
public class SpellSubAnimation {
    
    private String name;
    private SpellAnimationFrame[] frames;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public SpellAnimationFrame[] getFrames() {
        return frames;
    }

    public void setFrames(SpellAnimationFrame[] frames) {
        this.frames = frames;
    }
}
