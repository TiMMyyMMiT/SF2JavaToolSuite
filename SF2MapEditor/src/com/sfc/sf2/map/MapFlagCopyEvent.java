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
        return getTriggerX();
    }

    public void setFlag(int flag) {
        setTriggerX(flag);
    }
    
    public static MapFlagCopyEvent createEmpty() {
        return new MapFlagCopyEvent(100, 0, 0, 1, 1, 1, 1);
    }

    @Override
    public MapFlagCopyEvent clone() {
        return new MapFlagCopyEvent(getFlag(), getSourceX(), getSourceY(), getWidth(), getHeight(), getDestX(), getDestY());
    }
    
    public static String getFlagInfo(int flag) {
             if (flag < 32)   return String.format("Party member %d is in the force.", flag);
        else if (flag < 64)   return String.format("Party member %d is in the battle party.", (flag-32));
        else if (flag < 90)   return String.format("Map follower %d is present.", (flag-64));
        else if (flag < 127)  return String.format("Temp battle flag %d is set.", flag);
        else if (flag < 135)  return String.format("???", (flag-127));
        else if (flag < 256)  return String.format("Item flag %d is acquired (found in chest or other).", (flag-135));
        else if (flag < 280)  return String.format("Temp map setup flag %d is set.", flag);
        else if (flag < 400)  return String.format("???", (flag-280));
        else if (flag < 450)  return String.format("Battle %d is unlocked.", (flag-400));
        else if (flag < 500)  return String.format("Battle %d intro has been seen.", (flag-450));
        else if (flag < 550)  return String.format("Battle %d has been completed.", (flag-500));
        else if (flag < 1000) return String.format("Game progress flag %d is set.", (flag-550));
        else                  return String.format("???", (flag-1000));
    }
}
