/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle;

/**
 *
 * @author wiz
 */
public class Enemy {
        
    private EnemyData data;
    private int x;
    private int y;
    private String ai;
    private String item;
    private String moveOrder1;
    private int triggerRegion1;
    private String moveOrder2;
    private int triggerRegion2;
    private int byte10;
    private String spawnParams;

    public Enemy(EnemyData data, int x, int y, String ai, String item, String moveOrder1, int triggerRegion1, String moveOrder2, int triggerRegion2, int byte10, String spawnParams) {
        this.data = data;
        this.x = x;
        this.y = y;
        this.ai = ai;
        this.item = item;
        this.moveOrder1 = moveOrder1;
        this.triggerRegion1 = triggerRegion1;
        this.moveOrder2 = moveOrder2;
        this.triggerRegion2 = triggerRegion2;
        this.byte10 = byte10;
        this.spawnParams = spawnParams;
    }

    public EnemyData getEnemyData() {
        return data;
    }

    public void setEnemyData(EnemyData data) {
        this.data = data;
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

    public String getAi() {
        return ai;
    }

    public void setAi(String ai) {
        this.ai = ai;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getMoveOrder1() {
        return moveOrder1;
    }

    public void setMoveOrder1(String moveOrder1) {
        this.moveOrder1 = moveOrder1;
    }

    public int getTriggerRegion1() {
        return triggerRegion1;
    }

    public void setTriggerRegion1(int triggerRegion1) {
        this.triggerRegion1 = triggerRegion1;
    }

    public String getMoveOrder2() {
        return moveOrder2;
    }

    public void setMoveOrder2(String moveOrder2) {
        this.moveOrder2 = moveOrder2;
    }

    public int getTriggerRegion2() {
        return triggerRegion2;
    }

    public void setTriggerRegion2(int triggerRegion2) {
        this.triggerRegion2 = triggerRegion2;
    }

    public int getByte10() {
        return byte10;
    }

    public void setByte10(int byte10) {
        this.byte10 = byte10;
    }

    public String getSpawnParams() {
        return spawnParams;
    }

    public void setSpawnParams(String spawnParams) {
        this.spawnParams = spawnParams;
    }

    @Override
    public Enemy clone() {
        return new Enemy(data, x, y, ai, item, moveOrder1, triggerRegion1, moveOrder2, triggerRegion2, byte10, spawnParams);
    }
    
    public static Enemy emptyEnemy() {
        return new Enemy(null, 0, 0, null, null, null, -1, null, -1, 0, null);
    }
}
