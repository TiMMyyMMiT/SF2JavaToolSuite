/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.models;

import com.sfc.sf2.battle.mapterrain.BattleMapTerrain;
import com.sfc.sf2.battle.mapterrain.LandEffectMovementType;
import com.sfc.sf2.core.models.AbstractTableModel;

/**
 *
 * @author TiMMy
 */
public class LandEffectTableModel extends AbstractTableModel<LandEffectMovementType> {

    public LandEffectTableModel() {
        super(BattleMapTerrain.TERRAIN_BASE_NAMES, 16);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return col%2 == 0 ? Integer.class : String.class;
    }
 
    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    protected LandEffectMovementType createBlankItem(int row) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected LandEffectMovementType cloneItem(LandEffectMovementType item) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected Object getValue(LandEffectMovementType item, int row, int col) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected LandEffectMovementType setValue(LandEffectMovementType item, int row, int col, Object value) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected Comparable<?> getMinLimit(LandEffectMovementType item, int col) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected Comparable<?> getMaxLimit(LandEffectMovementType item, int col) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
