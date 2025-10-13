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
public class MapCopyEvent {
    
    private int triggerX;
    private int triggerY;
    private int sourceX;
    private int sourceY;
    private int width;
    private int height;
    private int destX;
    private int destY;
    private String comment;

    public MapCopyEvent(int triggerX, int triggerY, int sourceX, int sourceY, int width, int height, int destX, int destY, String comment) {
        this.triggerX = triggerX;
        this.triggerY = triggerY;
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.width = width;
        this.height = height;
        this.destX = destX;
        this.destY = destY;
        this.comment = comment;
    }

    public int getTriggerX() {
        return triggerX;
    }

    public void setTriggerX(int triggerX) {
        this.triggerX = triggerX;
    }

    public int getTriggerY() {
        return triggerY;
    }

    public void setTriggerY(int triggerY) {
        this.triggerY = triggerY;
    }
    
    public int getSourceX() {
        return sourceX;
    }

    public void setSourceX(int sourceX) {
        this.sourceX = sourceX;
    }

    public int getSourceY() {
        return sourceY;
    }

    public void setSourceY(int sourceY) {
        this.sourceY = sourceY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDestX() {
        return destX;
    }

    public void setDestX(int destX) {
        this.destX = destX;
    }

    public int getDestY() {
        return destY;
    }

    public void setDestY(int destY) {
        this.destY = destY;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public static MapCopyEvent createEmpty() {
        return new MapCopyEvent(0, 0, 0, 0, 1, 1, 1, 1, null);
    }

    @Override
    public MapCopyEvent clone() {
        return new MapCopyEvent(triggerX, triggerY, sourceX, sourceY, width, height, destX, destY, comment);
    }
}
