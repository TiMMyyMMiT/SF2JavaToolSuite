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
public class BattleSpriteset {
    private int index;
    private Ally[] allies;
    private Enemy[] enemies;
    private AIRegion[] aiRegions;
    private AIPoint[] aiPoints;

    public BattleSpriteset(int index, Ally[] allies, Enemy[] enemies, AIRegion[] aiRegions, AIPoint[] aiPoints) {
        this.index = index;
        this.allies = allies;
        this.enemies = enemies;
        this.aiRegions = aiRegions;
        this.aiPoints = aiPoints;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Ally[] getAllies() {
        return allies;
    }

    public void setAllies(Ally[] allies) {
        this.allies = allies;
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    public void setEnemies(Enemy[] enemies) {
        this.enemies = enemies;
    }

    public AIRegion[] getAiRegions() {
        return aiRegions;
    }

    public void setAiRegions(AIRegion[] aiRegions) {
        this.aiRegions = aiRegions;
    }

    public AIPoint[] getAiPoints() {
        return aiPoints;
    }

    public void setAiPoints(AIPoint[] aiPoints) {
        this.aiPoints = aiPoints;
    }
    
    
}
