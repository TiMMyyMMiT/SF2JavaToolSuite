/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation.gui;

import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimation;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimationProperties1TableModel extends AbstractTableModel {
    
    private final Object[][] tableData;
    private final String[] columns = {"Frame Number", "Spell Init Frame", "Spell Anim", "End Spell", "Idle 1 Weapon Frame", "H Flip", "V Flip", "Idle 1 Z", "Idle 1 Weapon X", "Idle 1 Weapon Y"};
    private BattleSpriteAnimation animation = null;
    
    public BattleSpriteAnimationProperties1TableModel(BattleSpriteAnimation animation) {
        super();
        this.animation = animation;
        tableData = new Object[1][];
        int i = 0;
        if(animation!=null){
            tableData[i] = new Object[10];
            tableData[i][0] = Integer.toString(animation.getFrameNumber());
            tableData[i][1] = Integer.toString(animation.getSpellInitFrame());
            tableData[i][2] = Integer.toString(animation.getSpellAnim());
            tableData[i][3] = (animation.getEndSpellAnim()!=0);
            tableData[i][4] = Integer.toString(animation.getIdle1WeaponFrame()&0xF);
            tableData[i][5] = (animation.getIdle1WeaponFrame()&0x10)!=0;
            tableData[i][6] = (animation.getIdle1WeaponFrame()&0x20)!=0;
            tableData[i][7] = animation.getIdle1WeaponZ()!=1;
            tableData[i][8] = Integer.toString(animation.getIdle1WeaponX());
            tableData[i][9] = Integer.toString(animation.getIdle1WeaponY());
        }
    }
    
    public void updateProperties() {
        animation.setFrameNumber(Integer.parseInt((String)tableData[0][0]));
        animation.setSpellInitFrame(Integer.parseInt((String)tableData[0][1]));
        animation.setSpellAnim(Integer.parseInt((String)tableData[0][2]));
        animation.setEndSpellAnim(((Boolean)tableData[0][3])?-1:0);
        animation.setIdle1WeaponFrame(Integer.parseInt((String)tableData[0][4]) + (int)(((Boolean)tableData[0][5])?0x10:0) + (int)((Boolean)tableData[0][6]?0x20:0));
        animation.setIdle1WeaponZ(((Boolean)tableData[0][7])?2:1);
        animation.setIdle1WeaponX(Integer.parseInt((String)tableData[0][8]));
        animation.setIdle1WeaponY(Integer.parseInt((String)tableData[0][9]));
    }
    
    @Override
    public Class getColumnClass(int column) {
        if(column == 3 || column == 5 || column == 6 || column == 7){
            return Boolean.class;
        }else {
            return String.class;
        }
    }    
    
    @Override
    public Object getValueAt(int row, int col) {
        return tableData[row][col];
    }
    @Override
    public void setValueAt(Object value, int row, int col) {
        tableData[row][col] = value;
        updateProperties();
        animation.getLayout().updateDisplayProperties();
        animation.getLayout().getPanel().revalidate();
        animation.getLayout().getPanel().repaint();
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
    
}
