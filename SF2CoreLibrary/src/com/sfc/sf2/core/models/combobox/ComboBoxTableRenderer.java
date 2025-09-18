/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.models.combobox;

import com.sfc.sf2.core.models.AbstractTableModel;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author TiMMy
 */
public class ComboBoxTableRenderer implements TableCellRenderer {

    private final JTextField text = new JTextField();
    private final JComboBox comboBox = new JComboBox();
    
    public ComboBoxTableRenderer(ComboBoxModel model) {
        this();
        comboBox.setModel(model);
    }
    
    public ComboBoxTableRenderer() {
        super();
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, 28));
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        text.setEnabled(false);
        text.setBorder(null);
        text.setDisabledTextColor(null);
    }
    
    public void setData(ComboBoxModel model) {
        comboBox.setModel(model);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (table.isCellEditable(row, column)) {
            if (table.getModel() instanceof AbstractTableModel) {
                ComboBoxModel model = ((AbstractTableModel)table.getModel()).getComboBoxModel(row, column);
                if (model != null) {
                    comboBox.setSelectedItem(value);
                    comboBox.setModel(model);
                }
            }
            comboBox.setSelectedItem(null);
            comboBox.setSelectedItem(value);
            return comboBox;
        } else {
            text.setText(value == null ? "" : value.toString());
            return text;
        }
    }
}
