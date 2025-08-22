/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation.models;

import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimationFrame;
import com.sfc.sf2.core.models.AbstractTableModel;

/**
 *
 * @author TiMMy
 */
public class BattleSpriteAnimationFramesTableModel extends AbstractTableModel<BattleSpriteAnimationFrame> {
    
    public BattleSpriteAnimationFramesTableModel() {
        super(new String[] { "Index", "Duration", "X", "Y", "Weapon Frame", "H Flip", "V Flip", "Behind", "Weapon X", "Weapon Y" }, 255);
    }

    @Override
    public Class<?> getColumnType(int col) {
        switch (col) {
            case 5: case 6: case 7: return Boolean.class;
            default: return Byte.class;
        }
    }

    @Override
    protected BattleSpriteAnimationFrame createBlankItem(int row) {
        return new BattleSpriteAnimationFrame((byte)row, (byte)20, (byte)0, (byte)0);
    }

    @Override
    protected BattleSpriteAnimationFrame cloneItem(BattleSpriteAnimationFrame item) {
        return item.clone();
    }

    @Override
    protected Object getValue(BattleSpriteAnimationFrame item, int col) {
        switch (col) {
            case 0: return item.getIndex();
            case 1: return item.getDuration();
            case 2: return item.getX();
            case 3: return item.getY();
            case 4: return item.getWeaponFrame();
            case 5: return item.getWeaponFlipH();
            case 6: return item.getWeaponFlipV();
            case 7: return item.getWeaponBehind();
            case 8: return item.getWeaponX();
            case 9: return item.getWeaponY();
        }
        return null;
    }

    @Override
    protected BattleSpriteAnimationFrame setValue(BattleSpriteAnimationFrame item, int col, Object value) {
        switch (col) {
            case 0: item.setIndex((byte)value); break;
            case 1: item.setDuration((byte)value); break;
            case 2: item.setX((byte)value); break;
            case 3: item.setY((byte)value); break;
            case 4: item.setWeaponFrame((byte)value); break;
            case 5: item.setWeaponFlipH((boolean)value); break;
            case 6: item.setWeaponFlipV((boolean)value); break;
            case 7: item.setWeaponBehind((boolean)value); break;
            case 8: item.setWeaponX((byte)value); break;
            case 9: item.setWeaponY((byte)value); break;
        }
        return item;
    }
}
