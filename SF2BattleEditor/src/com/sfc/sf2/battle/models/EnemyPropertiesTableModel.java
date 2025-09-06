/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.models;

import com.sfc.sf2.battle.Enemy;
import com.sfc.sf2.core.models.AbstractTableModel;

/**
 *
 * @author TiMMy
 */
public class EnemyPropertiesTableModel extends AbstractTableModel<Enemy> {
    
    public EnemyPropertiesTableModel() {
        super(new String[] {"Id", "Name", "X", "Y", "AI", "Item", "Order 1", "Region 1", "Order 2", "Region 2", "Byte10", "Spawn"}, 64);
    }
    
    @Override
    public Class<?> getColumnType(int col) {
        switch (col) {
            case 1:
            case 4:
            case 5:
            case 6:
            case 8:
            case 11:
                return String.class;
            default: return Integer.class;
        }
    }
 
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;//column > 1;
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
            case 6: return item.getMoveOrder1();
            case 7: return item.getTriggerRegion1();
            case 8: return item.getMoveOrder2();
            case 9: return item.getTriggerRegion2();
            case 10: return item.getByte10();
            case 11: return item.getSpawnParams();
            default: return null;
        }
    }

    @Override
    protected Enemy setValue(Enemy item, int row, int col, Object value) {
        switch (col) {
            //case 1: item.getEnemyData().getName(); break;
            case 2: item.setX((int)value);
            case 3: item.setY((int)value);
            case 4: item.setAi((String)value);
            case 5: item.setItem((String)value);
            case 6: item.setMoveOrder1((String)value);
            case 7: item.setTriggerRegion1((int)value);
            case 8: item.setMoveOrder2((String)value);
            case 9: item.setTriggerRegion2((int)value);
            case 10: item.setByte10((int)value);
            case 11: item.setSpawnParams((String)value);
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
            case 9:
            case 10:
                return 64;
            default: return Byte.MAX_VALUE;
        }
    }
}
