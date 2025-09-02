/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapcoords.gui;

import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.map.layout.MapLayout;

/**
 *
 * @author wiz
 */
public class BattleMapCoordsTableModel extends AbstractTableModel<BattleMapCoords> {

    public BattleMapCoordsTableModel() {
        super(new String[] { "Id", "Map", "X", "Y", "Width", "Height", "Trig. X", "Trig. Y" }, 255); //Arbitrary large number
    }
    
    @Override
    public Class<?> getColumnType(int col) {
        return Integer.class;
    }

    @Override
    protected BattleMapCoords createBlankItem(int row) {
        return BattleMapCoords.EmptyBattleMapCoords();
    }

    @Override
    protected BattleMapCoords cloneItem(BattleMapCoords item) {
        return item.clone();
    }

    @Override
    protected Object getValue(BattleMapCoords item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getMap();
            case 2: return item.getX();
            case 3: return item.getY();
            case 4: return item.getWidth();
            case 5: return item.getHeight();
            case 6: return item.getTrigX();
            case 7: return item.getTrigY();
            default: return -1;
        }
    }

    @Override
    protected BattleMapCoords setValue(BattleMapCoords item, int col, Object value) {
        switch (col) {
            case 1: item.setMap((int)value);
            case 2: item.setX((int)value);
            case 3: item.setY((int)value);
            case 4: item.setWidth((int)value);
            case 5: item.setHeight((int)value);
            case 6: item.setTrigX((int)value);
            case 7: item.setTrigY((int)value);
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(BattleMapCoords item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(BattleMapCoords item, int col) {
        return 255;
    }
}
