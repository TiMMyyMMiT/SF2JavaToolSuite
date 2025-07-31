/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation.gui;

import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimation;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimationFrame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimationFramesTableModel extends AbstractTableModel {
    
    private final Object[][] tableData;
    private final String[] columns = {"Index", "Duration", "X", "Y", "Weapon Frame", "H Flip", "V Flip", "Z", "Weapon X", "Weapon Y"};
    private BattleSpriteAnimation animation = null;
 
    public BattleSpriteAnimationFramesTableModel(BattleSpriteAnimation animation) {
        super();
        this.animation = animation;
        tableData = new Object[256][];
        int i = 0;
        BattleSpriteAnimationFrame[] frames = animation.getFrames();
        if(frames!=null){
            while(i<frames.length){
                tableData[i] = new Object[10];
                tableData[i][0] = Integer.toString(frames[i].getIndex());
                tableData[i][1] = Integer.toString(frames[i].getDuration());
                tableData[i][2] = Integer.toString(frames[i].getX());
                tableData[i][3] = Integer.toString(frames[i].getY());
                tableData[i][4] = Integer.toString(frames[i].getWeaponFrame()&0xF);
                tableData[i][5] = (frames[i].getWeaponFrame()&0x10)!=0;
                tableData[i][6] = (frames[i].getWeaponFrame()&0x20)!=0;
                tableData[i][7] = frames[i].getWeaponZ()!=1;
                tableData[i][8] = Integer.toString(frames[i].getWeaponX());
                tableData[i][9] = Integer.toString(frames[i].getWeaponY());
                i++;
            }
        }
        while(i<tableData.length){
            tableData[i] = new Object[10];
            tableData[i][0] = "";
            tableData[i][1] = "";
            tableData[i][2] = "0";
            tableData[i][3] = "0";
            tableData[i][4] = "0";
            tableData[i][5] = false;
            tableData[i][6] = false;
            tableData[i][7] = false;
            tableData[i][8] = "0";
            tableData[i][9] = "0";
            i++;
        }
    }
    
    @Override
    public Class getColumnClass(int column) {
        if(column == 5 || column == 6 || column == 7){
            return Boolean.class;
        }else {
            return String.class;
        }
    } 
    
    public void updateFrameProperties(){
        List<BattleSpriteAnimationFrame> entries = new ArrayList<>();
        for(Object[] entry : tableData){
            try{
                BattleSpriteAnimationFrame frame = new BattleSpriteAnimationFrame();
                frame.setIndex(Integer.parseInt((String)entry[0]));
                frame.setDuration(Integer.parseInt((String)entry[1]));
                frame.setX(Integer.parseInt((String)entry[2]));
                frame.setY(Integer.parseInt((String)entry[3]));
                frame.setWeaponFrame(Integer.parseInt((String)entry[4]) + (int)(((Boolean)entry[5])?0x10:0) + (int)((Boolean)entry[6]?0x20:0));
                frame.setWeaponZ(((Boolean)entry[7])?2:1);
                frame.setWeaponX(Integer.parseInt((String)entry[8]));
                frame.setWeaponY(Integer.parseInt((String)entry[9]));
                entries.add(frame);
            }catch(Exception e){
                break;
            }
        }
        BattleSpriteAnimationFrame[] returnedEntries = new BattleSpriteAnimationFrame[entries.size()];
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
