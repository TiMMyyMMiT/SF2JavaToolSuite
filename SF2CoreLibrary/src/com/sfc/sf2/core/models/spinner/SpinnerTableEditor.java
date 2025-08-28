/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.models.spinner;

import java.awt.*;
import java.text.ParseException;
import java.util.*;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author TiMMy
 */
public class SpinnerTableEditor extends AbstractCellEditor implements TableCellEditor {
            
    private final JSpinner spinner = new JSpinner();
    
    public SpinnerTableEditor(AbstractSpinnerModel model) {
        super();
        spinner.setModel(model);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        spinner.setValue(value);
        return spinner;
    }

    @Override
    public boolean isCellEditable(EventObject evt) {
        return true;
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }
    
    @Override
    public boolean stopCellEditing() {
        try {
            spinner.commitEdit();
        } catch (ParseException ex) { }
        return super.stopCellEditing();
    }
    
    @Override
    public void addCellEditorListener(CellEditorListener l) {
        super.addCellEditorListener(l);
        spinner.addChangeListener(this::spinnerStateChanged);
    }
    
    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
        spinner.removeChangeListener(this::spinnerStateChanged);
    }
    
    private void spinnerStateChanged(ChangeEvent evt) {
        fireEditingStopped();
    }  
}
