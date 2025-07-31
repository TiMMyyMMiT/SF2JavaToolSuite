/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.animation;

import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.layout.MapLayout;

/**
 *
 * @author wiz
 */
public class Map {
    private MapBlock[] blocks;
    private MapLayout layout;
    private MapAnimation animation;
    
    public MapBlock[] getBlocks() {
        return blocks;
    }

    public void setBlocks(MapBlock[] blocks) {
        this.blocks = blocks;
    }

    public MapLayout getLayout() {
        return layout;
    }

    public void setLayout(MapLayout layout) {
        this.layout = layout;
    }

    public void setActionFlag(int x, int y, int value){
        MapBlock block = this.layout.getBlocks()[y*64+x];
        int origFlags = block.getFlags();
        int newValue = value;
        if((origFlags&0x0400)!=0 && newValue==0x0800){
            newValue = 0x0400;
        }
        int newFlags = (origFlags & 0xC000) + (newValue & 0x3FFF);
        block.setFlags(newFlags);
    }

    public MapAnimation getAnimation() {
        return animation;
    }

    public void setAnimation(MapAnimation animation) {
        this.animation = animation;
    }
    
    
    
}
