/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.animation.gui;

import com.sfc.sf2.map.animation.Map;
import com.sfc.sf2.map.animation.MapAnimationFrame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author wiz
 */
public class MapAnimationFramePropertiesTableModel extends AbstractTableModel {
    
    private final Integer[][] tableData;
    private final String[] columns = {"Start", "Length", "Dest", "Delay"};
    private Map map;
    private MapAnimationPanel mapPanel;
    
    public MapAnimationFramePropertiesTableModel(Map map, MapAnimationPanel mapPanel) {
        super();
        this.map = map;
        this.mapPanel = mapPanel;
        tableData = new Integer[64][];
        int i = 0;
        MapAnimationFrame[] frames = map.getAnimation().getFrames();
        if(frames!=null){
            while(i<frames.length){
                tableData[i] = new Integer[4];
                tableData[i][0] = frames[i].getStart();
                tableData[i][1] = frames[i].getLength();
                tableData[i][2] = frames[i].getDest();
                tableData[i][3] = frames[i].getDelay();
                i++;
            }
        }
        while(i<tableData.length){
            tableData[i] = new Integer[4];
            i++;
        }
    }
    
    public void updateProperties() {
        for(int i=0;i<tableData.length;i++){
            Integer[] entry = tableData[i];
            if(entry[0] != null && entry[1] != null
                    && entry[2] != null && entry[3] != null){
                if(i<map.getAnimation().getFrames().length){
                    MapAnimationFrame frame = map.getAnimation().getFrames()[i];
                    frame.setStart(entry[0]);
                    frame.setLength(entry[1]);
                    frame.setDest(entry[2]);
                    frame.setDelay(entry[3]);  
                }else{
                    MapAnimationFrame[] newFrames = new MapAnimationFrame[i+1];
                    System.arraycopy(map.getAnimation().getFrames(), 0, newFrames, 0, map.getAnimation().getFrames().length);
                    newFrames[i] = new MapAnimationFrame();
                    newFrames[i].setStart(entry[0]);
                    newFrames[i].setLength(entry[1]);
                    newFrames[i].setDest(entry[2]);
                    newFrames[i].setDelay(entry[3]);  
                    map.getAnimation().setFrames(newFrames);
                }
            }
        }
    }
    
    @Override
    public Class getColumnClass(int column) {
        return Integer.class;
    }    
    
    @Override
    public Object getValueAt(int row, int col) {
        return tableData[row][col];
    }
    @Override
    public void setValueAt(Object value, int row, int col) {
        tableData[row][col] = (Integer)value;
        updateProperties();
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

    public MapAnimationPanel getMapPanel() {
        return mapPanel;
    }

    public void setMapPanel(MapAnimationPanel mapPanel) {
        this.mapPanel = mapPanel;
    }
    
}
