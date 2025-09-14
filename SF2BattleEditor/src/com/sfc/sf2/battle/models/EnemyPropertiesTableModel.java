/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.models;

import com.sfc.sf2.battle.Enemy;
import com.sfc.sf2.battle.EnemyData;
import com.sfc.sf2.battle.EnemyEnums;
import com.sfc.sf2.core.models.AbstractTableModel;

/**
 *
 * @author TiMMy
 */
public class EnemyPropertiesTableModel extends AbstractTableModel<Enemy> {
    
    private EnemyData[] enemyData;
    private EnemyEnums enemyEnums;
    
    public EnemyPropertiesTableModel() {
        super(new String[] { "Id", "Name", "X", "Y", "AI", "Item", "Move Order", "Region 1", "Region 2", "Bkup Order", "Byte10", "Spawn" }, 64);
    }
    
    public void setEnemyData(EnemyData[] enemyData, EnemyEnums enemyEnums) {
        this.enemyData = enemyData;
        this.enemyEnums = enemyEnums;
    }
    
    @Override
    public Class<?> getColumnType(int col) {
        switch (col) {
            case 1:
            case 4:
            case 5:
            case 6:
            case 9:
            case 11:
                return String.class;
            default: return Integer.class;
        }
    }
 
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    protected Enemy createBlankItem(int row) {
        return Enemy.emptyEnemy();
    }

    @Override
    protected Enemy cloneItem(Enemy item) {
        return item.clone();
    }

    @Override
    protected Object getValue(Enemy item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getEnemyData().getName();
            case 2: return item.getX();
            case 3: return item.getY();
            case 4: return item.getAi();
            case 5: return item.getItem();
            case 6: return item.getMoveOrder();
            case 7: return item.getTriggerRegion1();
            case 8: return item.getTriggerRegion2();
            case 9: return item.getBackupMoveOrder();
            case 10: return item.getUnknown();
            case 11: return item.getSpawnParams();
            default: return null;
        }
    }

    @Override
    protected Enemy setValue(Enemy item, int row, int col, Object value) {
        switch (col) {
            case 1:
                if (enemyEnums.getEnemies().containsKey(value)) {
                    int index = enemyEnums.getEnemies().get(value);
                    if (index >= 0 && index < enemyData.length)
                    item.setEnemyData(this.enemyData[index]);
                }
            break;
            case 2: item.setX((int)value); break;
            case 3: item.setY((int)value); break;
            case 4: item.setAi((String)value); break;
            case 5: item.setItem((String)value); break;
            case 6: item.setMoveOrder((String)value); break;
            case 7: item.setTriggerRegion1((int)value); break;
            case 8: item.setTriggerRegion2((int)value); break;
            case 9: item.setBackupMoveOrder((String)value); break;
            case 10: item.setUnknown((int)value); break;
            case 11: item.setSpawnParams((String)value); break;
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(Enemy item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(Enemy item, int col) {
        switch (col) {
            case 2:
            case 3:
            case 7:
            case 8:
            case 10:
                return 64;
            default: return Byte.MAX_VALUE;
        }
    }
}
