/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.models;

import com.sfc.sf2.battle.AIRegion;
import com.sfc.sf2.core.models.AbstractTableModel;

/**
 *
 * @author wiz
 */
public class AIRegionPropertiesTableModel extends AbstractTableModel<AIRegion> {

    public AIRegionPropertiesTableModel() {
        super(new String[] {"Id", "Type", "X1", "Y1", "X2", "Y2", "X3", "Y3", "X4", "Y4"}, 64);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return Integer.class;
    }
 
    @Override
    public boolean isCellEditable(int row, int column) {
        return column > 0;
    }

    @Override
    protected AIRegion createBlankItem(int row) {
        return AIRegion.emptyAIRegion();
    }

    @Override
    protected AIRegion cloneItem(AIRegion item) {
        return item.clone();
    }

    @Override
    protected Object getValue(AIRegion item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getType();
            case 2: return item.getX1();
            case 3: return item.getY1();
            case 4: return item.getX2();
            case 5: return item.getY2();
            case 6: return item.getX3();
            case 7: return item.getY3();
            case 8: return item.getX4();
            case 9: return item.getY4();
            default: return 0;
        }
    }

    @Override
    protected AIRegion setValue(AIRegion item, int row, int col, Object value) {
        switch (col) {
            case 1: item.setType((int)value);
            case 2: item.setX1((int)value);
            case 3: item.setY1((int)value);
            case 4: item.setX2((int)value);
            case 5: item.setY2((int)value);
            case 6: item.setX3((int)value);
            case 7: item.setY3((int)value);
            case 8: item.setX4((int)value);
            case 9: item.setY4((int)value);
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(AIRegion item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(AIRegion item, int col) {
        return 64;
    }
}
