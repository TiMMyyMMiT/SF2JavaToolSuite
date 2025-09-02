/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapcoords.layout;

import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import com.sfc.sf2.map.layout.gui.StaticMapLayoutPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author wiz
 */
public class BattleMapCoordsLayout extends StaticMapLayoutPanel {
    
    protected BattleMapCoords battleCoords;
    protected boolean showBattleCoords = true;
    
    @Override
    protected void drawImage(Graphics graphics) {
        Graphics2D g2 = (Graphics2D)graphics;
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.YELLOW);
        int width = battleCoords.getWidth();
        int heigth = battleCoords.getHeight();
        g2.drawRect(battleCoords.getX()*24+3, battleCoords.getY()*24+3, width*24-6, heigth*24-6);
        g2.setColor(Color.ORANGE);
        if (battleCoords.getTrigX() < 64 && battleCoords.getTrigY() < 64) {
            g2.drawRect(battleCoords.getTrigX()*24+3, battleCoords.getTrigY()*24+3, 1*24-6, 1*24-6);
        }
    }

    public BattleMapCoords getBattleCoords() {
        return battleCoords;
    }

    public void setBattleCoords(BattleMapCoords battleCoords) {
        this.battleCoords = battleCoords;
        redraw();
    }

    public boolean getShowBattleCoords() {
        return showBattleCoords;
    }

    public void setShowBattleCoords(boolean showBattleCoords) {
        this.showBattleCoords = showBattleCoords;
        redraw();
    }
}
