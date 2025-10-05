/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.map.MapCopyEvent;
import com.sfc.sf2.map.layout.MapLayout;

/**
 *
 * @author TiMMy
 */
public class MapCopyEventTableModel extends AbstractTableModel<MapCopyEvent> {
    
    public MapCopyEventTableModel() {
        super(new String[] { "Index", "Trigger X", "Trigger Y", "Source X", "Source Y", "Width", "Height", "Dest X", "Dest Y" }, 64);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return Integer.class;
    }

    @Override
    protected MapCopyEvent createBlankItem(int row) {
        return MapCopyEvent.createEmpty();
    }

    @Override
    protected MapCopyEvent cloneItem(MapCopyEvent item) {
        return item.clone();
    }

    @Override
    protected Object getValue(MapCopyEvent item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getTriggerX();
            case 2: return item.getTriggerY();
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
    protected MapCopyEvent setValue(MapCopyEvent item, int row, int col, Object value) {
        switch (col) {
            case 1: item.setTriggerX((int)value); break;
            case 2: item.setTriggerY((int)value); break;
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
    protected Comparable<?> getMinLimit(MapCopyEvent item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(MapCopyEvent item, int col) {
        return MapLayout.BLOCK_WIDTH-1;
    }
}
