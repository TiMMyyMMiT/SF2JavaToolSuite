/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapcoords;

/**
 *
 * @author wiz
 */
public class BattleMapCoords {
    
    private int map;
    private int x;
    private int y;
    private int width;
    private int height;
    private int trigX;
    private int trigY;

    public BattleMapCoords(int map, int x, int y, int width, int height, int trigX, int trigY) {
        this.map = map;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.trigX = trigX;
        this.trigY = trigY;
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
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

    public int getTrigX() {
        return trigX;
    }

    public void setTrigX(int trigX) {
        this.trigX = trigX;
    }

    public int getTrigY() {
        return trigY;
    }

    public void setTrigY(int trigY) {
        this.trigY = trigY;
    }
    
    public static BattleMapCoords EmptyBattleMapCoords() {
        return new BattleMapCoords(3, 10, 10, 10, 10, 255, 255);
    }

    @Override
    public BattleMapCoords clone() {
        return new BattleMapCoords(map, x, y, width, height, trigX, trigY);
    }
}
