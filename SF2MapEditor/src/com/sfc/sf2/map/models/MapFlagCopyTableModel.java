/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.map.MapFlagCopy;
import com.sfc.sf2.map.layout.MapLayout;

/**
 *
 * @author wiz
 */
public class MapFlagCopyTableModel extends AbstractTableModel<MapFlagCopy> {
    
    public MapFlagCopyTableModel() {
        super(new String[] { "Index", "Flag", "Comment", "Source X", "Source Y", "Width", "Height", "Dest X", "Dest Y" }, 64);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return col == 8 ? String.class : Integer.class;
    }

    @Override
    protected MapFlagCopy createBlankItem(int row) {
        return MapFlagCopy.createEmpty();
    }

    @Override
    protected MapFlagCopy cloneItem(MapFlagCopy item) {
        return item.clone();
    }

    @Override
    protected Object getValue(MapFlagCopy item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getFlag();
            case 2: return item.getComment();
            case 3: return item.getSourceX();
            case 4: return item.getSourceY();
            case 5: return item.getWidth();
            case 6: return item.getHeight();
            case 7: return item.getDestX();
            case 8: return item.getDestY();
        }
        return -1;
    }

    @Override
    protected MapFlagCopy setValue(MapFlagCopy item, int row, int col, Object value) {
        switch (col) {
            case 1: item.setFlag((int)value); break;
            case 2: item.setComment((String)value); break;
            case 3: item.setSourceX((int)value); break;
            case 4: item.setSourceY((int)value); break;
            case 5: item.setWidth((int)value); break;
            case 6: item.setHeight((int)value); break;
            case 7: item.setDestX((int)value); break;
            case 8: item.setDestY((int)value); break;
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(MapFlagCopy item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(MapFlagCopy item, int col) {
        return MapLayout.BLOCK_WIDTH-1;
    }
}
