/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.map.MapItem;
import com.sfc.sf2.map.layout.MapLayout;

/**
 *
 * @author TiMMy
 */
public class MapItemTableModel extends AbstractTableModel<MapItem> {
    
    public MapItemTableModel() {
        super(new String[] { "Index", "X", "Y", "Flag", "Item" }, 64);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return col == 4 ? String.class : Integer.class;
    }

    @Override
    protected MapItem createBlankItem(int row) {
        return MapItem.createEmpty();
    }

    @Override
    protected MapItem cloneItem(MapItem item) {
        return item.clone();
    }

    @Override
    protected Object getValue(MapItem item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getX();
            case 2: return item.getY();
            case 3: return item.getFlag();
            case 4: return item.getItem();
        }
        return -1;
    }

    @Override
    protected MapItem setValue(MapItem item, int row, int col, Object value) {
        switch (col) {
            case 1: item.setX((int)value); break;
            case 2: item.setY((int)value); break;
            case 3: item.setFlag((int)value); break;
            case 4: item.setItem((String)value); break;
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(MapItem item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(MapItem item, int col) {
        return MapLayout.BLOCK_WIDTH-1;
    }
}
