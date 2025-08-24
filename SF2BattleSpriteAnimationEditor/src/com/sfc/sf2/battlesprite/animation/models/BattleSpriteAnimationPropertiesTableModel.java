/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation.models;

import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimation;
import com.sfc.sf2.core.models.AbstractTableModel;

/**
 *
 * @author TiMMy
 */
public class BattleSpriteAnimationPropertiesTableModel extends AbstractTableModel<BattleSpriteAnimation> {
    
    public BattleSpriteAnimationPropertiesTableModel() {
        super(new String[] { "Frame Count", "Spell Init Frame", "Spell Anim", "End Spell" }, 255);
        //super(new String[] { "Frame Count", "Spell Init Frame", "Spell Anim", "End Spell", "Idle 1 Weapon Frame", "H Flip", "V Flip", "Idle 1 Z", "Idle 1 Weapon X", "Idle 1 Weapon Y" }, 255);
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return row != 0;
    }

    @Override
    public Class getColumnType(int col) {
        switch (col) {
            case 3: return Boolean.class;
            default: return Byte.class;
        }
    }

    @Override
    protected BattleSpriteAnimation createBlankItem(int row) {
        return BattleSpriteAnimation.EmptyAnimation();
    }

    @Override
    protected BattleSpriteAnimation cloneItem(BattleSpriteAnimation item) {
        return item.clone();
    }

    @Override
    protected Object getValue(BattleSpriteAnimation item, int row, int col) {
        switch (col) {
            case 0: return item.getFrameCount();
            case 1: return item.getSpellInitFrame();
            case 2: return item.getSpellAnim();
            case 3: return item.getEndSpellAnim();
        }
        return null;
    }

    @Override
    protected BattleSpriteAnimation setValue(BattleSpriteAnimation item, int col, Object value) {
        switch (col) {
            case 0: break;  //Frame count should not be editable
            case 1: item.setSpellInitFrame((byte)value); break;
            case 2: item.setSpellAnim((byte)value); break;
            case 3: item.setEndSpellAnim((boolean)value); break;
        }
        return item;
    }
}
