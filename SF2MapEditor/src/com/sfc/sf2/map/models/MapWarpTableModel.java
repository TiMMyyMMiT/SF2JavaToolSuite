/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.map.MapWarp;
import com.sfc.sf2.map.layout.MapLayout;
/**
 *
 * @author TiMMy
 */
public abstract class MapWarpTableModel extends AbstractTableModel<MapWarp> {
    
    public MapWarpTableModel() {
        super(new String[] { "Index", "Trigger X", "Trigger Y", "Dest Map", "Dest X", "Dest Y", "Facing" }, 64);
    }

    @Override
    public Class getColumnType(int col) {
        switch (col) {
            case 3:
            case 4:
            case 7:
                return String.class;
            default:
                return Integer.class;
        }
    }

    @Override
    protected MapWarp createBlankItem(int row) {
        return MapWarp.createEmpty();
    }

    @Override
    protected MapWarp cloneItem(MapWarp item) {
        return item.clone();
    }

    @Override
    protected Object getValue(MapWarp item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getTriggerX();
            case 2: return item.getTriggerY();
            case 3: return item.getScrollDirection();
            case 4: return item.getDestMap();
            case 5: return item.getDestX();
            case 6: return item.getDestY();
            case 7: return item.getFacing();
        }
        return null;
    }

    @Override
    protected MapWarp setValue(MapWarp item, int row, int col, Object value) {
        switch (col) {
            case 1: item.setTriggerX((int)value); break;
            case 2: item.setTriggerY((int)value); break;
            case 3: item.setScrollDirection((String)value); break;
            case 4: item.setDestMap((String)value); break;
            case 5: item.setDestX((int)value); break;
            case 6: item.setDestY((int)value); break;
            case 7: item.setFacing((String)value); break;
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(MapWarp item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(MapWarp item, int col) {
        return MapLayout.BLOCK_WIDTH-1;
    }
}
