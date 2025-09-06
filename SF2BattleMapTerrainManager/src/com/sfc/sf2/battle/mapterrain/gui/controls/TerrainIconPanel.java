/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.gui.controls;

import com.sfc.sf2.battle.mapterrain.gui.resources.BattleTerrainIcons;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.BeanProperty;

/**
 *
 * @author TiMMy
 */
public class TerrainIconPanel extends Component {
    
    private int terrainValue;
    boolean showNumber;

    public int getNumber() {
        return terrainValue;
    }

    @BeanProperty(preferred = true, visualUpdate = true, description = "The number for the icon.")
    public void setNumber(int number) {
        this.terrainValue = number;
        repaint();
    }

    @BeanProperty(preferred = true, visualUpdate = true, description = "To show the number or icon representation.")
    public void setShowNumber(boolean showNumber) {
        this.showNumber = showNumber;
        repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        Dimension dims = getSize();
        if (showNumber) {
            g.setColor(BattleTerrainIcons.TERRAIN_TEXT_BG);
            g.fillRect(0, 0, dims.width, dims.height);
            g.setColor(BattleTerrainIcons.getTerrainTextColor(terrainValue));
            g.drawString(Integer.toString(terrainValue), dims.width/4, dims.height-4);
        } else {
            g.setColor(BattleTerrainIcons.TERRAIN_BG);
            g.fillRect(0, 0, dims.width, dims.height);
            g.drawImage(BattleTerrainIcons.getTerrainIcon(terrainValue).getImage(), 4, 4, dims.width-8, dims.height-8, null);
        }
    }
}
