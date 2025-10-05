/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map;

import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.block.MapBlockset;

/**
 *
 * @author wiz
 */
public class Map {
    private MapBlockset blockset;
    private MapLayout layout;
    private MapArea[] areas;
    private MapFlagCopy[] flagCopies;
    private MapStepCopy[] stepCopies;
    private MapRoofCopy[] roofCopies;
    private MapWarp[] warps;
    private MapItem[] chestItems;
    private MapItem[] otherItems;
    private MapAnimation animation;

    public Map(MapBlockset blockset, MapLayout layout, MapArea[] areas, MapFlagCopy[] flagCopies, MapStepCopy[] stepCopies, MapRoofCopy[] roofCopies, MapWarp[] warps, MapItem[] chestItems, MapItem[] otherItems, MapAnimation animation) {
        this.blockset = blockset;
        this.layout = layout;
        this.areas = areas;
        this.flagCopies = flagCopies;
        this.stepCopies = stepCopies;
        this.roofCopies = roofCopies;
        this.warps = warps;
        this.chestItems = chestItems;
        this.otherItems = otherItems;
        this.animation = animation;
    }

    public MapBlockset getBlockset() {
        return blockset;
    }

    public void setBlockset(MapBlockset blockset) {
        this.blockset = blockset;
    }

    public MapLayout getLayout() {
        return layout;
    }

    public void setLayout(MapLayout layout) {
        this.layout = layout;
    }

    public MapArea[] getAreas() {
        return areas;
    }

    public void setAreas(MapArea[] areas) {
        this.areas = areas;
    }

    public MapFlagCopy[] getFlagCopies() {
        return flagCopies;
    }

    public void setFlagCopies(MapFlagCopy[] flagCopies) {
        this.flagCopies = flagCopies;
    }

    public MapStepCopy[] getStepCopies() {
        return stepCopies;
    }

    public void setStepCopies(MapStepCopy[] stepCopies) {
        this.stepCopies = stepCopies;
    }

    public MapRoofCopy[] getRoofCopies() {
        return roofCopies;
    }

    public void setRoofCopies(MapRoofCopy[] roofCopies) {
        this.roofCopies = roofCopies;
    }

    public MapWarp[] getWarps() {
        return warps;
    }

    public void setWarps(MapWarp[] warps) {
        this.warps = warps;
    }

    public MapItem[] getChestItems() {
        return chestItems;
    }

    public void setChestItems(MapItem[] chestItems) {
        this.chestItems = chestItems;
    }

    public MapItem[] getOtherItems() {
        return otherItems;
    }

    public void setOtherItems(MapItem[] otherItems) {
        this.otherItems = otherItems;
    }

    public MapAnimation getAnimation() {
        return animation;
    }

    public void setAnimation(MapAnimation animation) {
        this.animation = animation;
    }

    public void setActionFlag(int x, int y, int value) {
        MapBlock block = this.layout.getBlockset().getBlocks()[y*64+x];
        int origFlags = block.getFlags();
        int newValue = value;
        if ((origFlags&0x0400) != 0 && newValue == 0x0800) {
            newValue = 0x0400;
        }
        int newFlags = (origFlags&0xC000)+(newValue&0x3FFF);
        block.setFlags(newFlags);
    }
}
