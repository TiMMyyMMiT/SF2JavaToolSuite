/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellAnimation.gui;

import com.sfc.sf2.spellAnimation.SpellAnimationFrame;
import com.sfc.sf2.spellAnimation.SpellSubAnimation;
import com.sfc.sf2.spellAnimation.layout.SpellAnimationLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author TiMMy
 */
public class SpellAnimationFramesTableModel extends AbstractTableModel {
    
    private final Object[][] tableData;
    private final String[] columns = { "Index", "Tile Index", "X", "Y", "Tiles Width", "Tiles Height", "Foreground" };
    private SpellSubAnimation animation = null;
    private SpellAnimationLayout layout = null;
 
    public SpellAnimationFramesTableModel(SpellSubAnimation animation) {
        super();
        this.animation = animation;
        tableData = new Object[256][];
        int i = 0;
        SpellAnimationFrame[] frames = animation.getFrames();
        if (frames!=null) {
            while (i<frames.length) {
                tableData[i] = new Object[7];
                tableData[i][0] = frames[i].getFrameIndex();
                tableData[i][1] = frames[i].getTileIndex();
                tableData[i][2] = frames[i].getX();
                tableData[i][3] = frames[i].getY();
                tableData[i][4] = frames[i].getW();
                tableData[i][5] = frames[i].getH();
                tableData[i][6] = frames[i].getForeground();
                i++;
            }
        }
        while (i<tableData.length) {
            tableData[i] = new Object[7];
            tableData[i][0] = (short)0;
            tableData[i][1] = (short)0;
            tableData[i][2] = (short)0;
            tableData[i][3] = (short)0;
            tableData[i][4] = (byte)0;
            tableData[i][5] = (byte)0;
            tableData[i][6] = (byte)0;
            i++;
        }
    }
    
    @Override
    public Class getColumnClass(int column) {
        if(column <= 3) {
            return java.lang.Short.class;
        } else {
            return java.lang.Byte.class;
        }
    } 
    
    public void updateFrameProperties() {
        List<SpellAnimationFrame> entries = new ArrayList<>();
        for(Object[] entry : tableData) {
            try {
                SpellAnimationFrame frame = new SpellAnimationFrame();
                frame.setFrameIndex((short)entry[0]);
                frame.setTileIndex((short)entry[1]);
                frame.setX((short)entry[2]);
                frame.setY((short)entry[3]);
                frame.setW((byte)entry[4]);
                frame.setH((byte)entry[5]);
                frame.setForeground((byte)entry[6]);
                entries.add(frame);
            } catch(Exception e) {
                break;
            }
        }
        SpellAnimationFrame[] returnedEntries = new SpellAnimationFrame[entries.size()];
        animation.setFrames(entries.toArray(returnedEntries));
    }    
    
    @Override
    public Object getValueAt(int row, int col) {
        return tableData[row][col];
    }
    @Override
    public void setValueAt(Object value, int row, int col) {
        tableData[row][col] = value;
        updateFrameProperties();
        layout.updateDisplayProperties();
        layout.repaintAnim();
    }
 
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    
    @Override
    public int getRowCount() {
        return tableData.length;
    }
 
    @Override
    public int getColumnCount() {
        return columns.length;
    }
 
    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }
    
    public void setLayout(SpellAnimationLayout layout) {
        this.layout = layout;
    }
    
    public void setSpellAnimation(SpellSubAnimation animation) {
        this.animation = animation;
    }
}
