/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.models;

import com.sfc.sf2.core.gui.controls.Console;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TiMMy
 */
public abstract class AbstractTableModel<T> extends javax.swing.table.AbstractTableModel {

    protected String[] columns;
    
    private List<T> tableItems = new ArrayList();
    private int rowLimit;
    
    /**
     *
     * @param rowLimit set to -1 for no limit
     */
    public AbstractTableModel(String[] columns, int rowLimit) {
        super();
        this.columns = columns;
        this.rowLimit = rowLimit;
    }

    public T[] getTableData() {
        Object[] data = tableItems.toArray();
        return (T[])data;
    }

    public void setTableData(T[] tableData) {
        this.tableItems.clear();
        if (tableData != null) {
            for (int i = 0; i < tableData.length; i++) {
                this.tableItems.add(tableData[i]);
            }
        }
        fireTableDataChanged();
    }
    
    public abstract Class<?> getColumnType(int col);
    protected abstract T createBlankItem(int row);
    protected abstract T cloneItem(T item);
    protected abstract Object getValue(T item, int col);    
    protected abstract T setValue(T item, int col, Object value);
    
    
    public T getRow(int row) {
        if (row < 0 || row >= tableItems.size()) {
            return null;
        }
        return tableItems.get(row);
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        if (row < 0 || row >= tableItems.size() || col < 0 || col >= columns.length) {
            return null;
        }
        return getValue(tableItems.get(row), col);
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        if (row < 0 || row >= tableItems.size() || col < 0 || col >= columns.length) {
            return;
        }
        tableItems.set(row, setValue(tableItems.get(row), col, value));
        fireTableDataChanged();
    }
    
    public boolean canAddMoreRows() {
        return rowLimit == -1 || tableItems.size() < rowLimit;
    }
    
    public void addRow(int row) {
        if (rowLimit == -1 || tableItems.size() < rowLimit) {
            if (row < 0 || tableItems.size() == 0) {
                tableItems.add(createBlankItem(tableItems.size()));
            } else {
                tableItems.add(row+1, createBlankItem(row+1));
            }
            fireTableDataChanged();
        }
    }
    
    public void cloneRow(int row, int addOffset) {
        if (rowLimit == -1 || tableItems.size() < rowLimit) {
            T item;
            if (tableItems.size() == 0 || row < 0 || row >= tableItems.size()) {
                item = createBlankItem(row);
            } else {
                item = cloneItem(tableItems.get(row));
            }
            tableItems.add(row+addOffset, item);
            fireTableDataChanged();
        }
    }
    
    public void removeRow(int row) {
        if (tableItems.size() > 0) {
            if (row >= 0) {
                tableItems.remove(row);
            } else {
                tableItems.remove(tableItems.size()-1);
            }
            fireTableDataChanged();
        }
    }
    
    public boolean shiftUp(int row, int range) {
        if (row < 1 || row+range > tableItems.size()) {
            Console.logger().finest("Cannot shift up because selection is out of range");
            return false;
        }
        T item = tableItems.remove(row-1);
        tableItems.add(row+range-1, item);
        fireTableDataChanged();
        return true;
    }
    
    public boolean shiftDown(int row, int range) {
        if (row < 0 || row+range+1 > tableItems.size()) {
            Console.logger().finest("Cannot shift down because selection is out of range");
            return false;
        }
        T item = tableItems.remove(row+range);
        tableItems.add(row, item);
        fireTableDataChanged();
        return true;
    }
 
    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }
    
    @Override
    public int getRowCount() {
        return tableItems.size();
    }
 
    @Override
    public int getColumnCount() {
        return columns.length;
    }
 
    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return getColumnType(columnIndex);
    }
}
