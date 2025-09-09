/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.gui.controls;

import com.sfc.sf2.battle.mapterrain.gui.BattleMapTerrainLayoutPanel.TerrainDrawMode;
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
    private TerrainDrawMode terrainDrawMode;

    public int getNumber() {
        return terrainValue;
    }

    @BeanProperty(preferred = true, visualUpdate = true, description = "The terrainType value.")
    public void setNumber(int number) {
        this.terrainValue = number;
        repaint();
    }

    @BeanProperty(preferred = true, visualUpdate = true, description = "To show the number, color, or icon representation for the terrainType.")
    public void setTerrainDrawMode(TerrainDrawMode terrainDrawMode) {
        this.terrainDrawMode = terrainDrawMode;
            repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        if (this.terrainDrawMode == null) {
            super.paint(g);
            return;
        }
        Dimension dims = getSize();
        switch (terrainDrawMode) {
            case Icons:
                g.setColor(BattleTerrainIcons.TERRAIN_BG);
                g.fillRect(0, 0, dims.width, dims.height);
                g.drawImage(BattleTerrainIcons.getTerrainIcon(terrainValue).getImage(), 4, 4, dims.width-8, dims.height-8, null);
                break;
            case Colors:
                g.setColor(BattleTerrainIcons.TERRAIN_BG);
                g.fillRect(0, 0, dims.width, dims.height);
                g.setColor(BattleTerrainIcons.getTerrainTextColor(terrainValue));
                g.fillRect(4, 4, dims.width-8, dims.height-8);
                break;
            case Numbers:
                g.setColor(BattleTerrainIcons.TERRAIN_TEXT_BG);
                g.fillRect(0, 0, dims.width, dims.height);
                g.setColor(BattleTerrainIcons.getTerrainTextColor(terrainValue));
                g.drawString(Integer.toString(terrainValue), dims.width/4, dims.height-4);
                break;
        }
    }
}
