/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.models;

/**
 *
 * @author TiMMy
 */
public class StringTableModel extends AbstractTableModel<String> {

    public StringTableModel() {
        super(new String[] { "String", "Length" }, -1);
    }
    
    @Override
    public Class<?> getColumnType(int col) {
        switch (col) {
            case 0: return String.class;
            case 1: return Integer.class;
        }
        return Object.class;
    }
 
    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 0;
    }

    @Override
    protected String createBlankItem(int row) {
        return "New String : " + row;
    }

    @Override
    protected String cloneItem(String item) {
        return new String(item);
    }

    @Override
    protected Object getValue(String item, int col) {
        switch (col) {
            case 0: return item;
            case 1: return item.length();
        }
        return null;
    }

    @Override
    protected String setValue(String item, int col, Object value) {
        switch (col) {
            case 0: return (String)value;
        }
        return null;
    }
}
