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
public class MapItem {
    
    private int x;
    private int y;
    private int flag;
    private String item;

    public MapItem(int x, int y, int flag, String item) {
        this.x = x;
        this.y = y;
        this.flag = flag;
        this.item = item;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
    
    public static MapItem createEmpty() {
        return new MapItem(0, 0, 0, "MEDICAL_HERB");
    }

    @Override
    public MapItem clone() {
        return new MapItem(x, y, flag, item);
    }
}
