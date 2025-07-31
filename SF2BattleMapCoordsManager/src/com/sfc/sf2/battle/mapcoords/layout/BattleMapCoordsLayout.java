/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapcoords.layout;

import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.map.layout.layout.StaticMapLayout;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author wiz
 */
public class BattleMapCoordsLayout extends StaticMapLayout {
    
    protected BattleMapCoords coords;    
    protected boolean drawCoords = true;
    
    public BattleMapCoordsLayout() {
        super();
    }
    
    @Override
    public BufferedImage buildImage(MapLayout layout, int tilesPerRow) {
        BufferedImage image = super.buildImage(layout, tilesPerRow);
        Graphics graphics = image.getGraphics();
        if (drawCoords) {
            drawCoords(image);
        }
        graphics.dispose();
        return image;
    }
    
    private void drawCoords(BufferedImage image){
        Graphics2D g2 = (Graphics2D)image.getGraphics();
        g2.setStroke(new BasicStroke(3)); 
        g2.setColor(Color.YELLOW);
        int width = coords.getWidth();
        int heigth = coords.getHeight();
        g2.drawRect(coords.getX()*24+3, coords.getY()*24+3, width*24-6, heigth*24-6);
        g2.setColor(Color.ORANGE);
        if (coords.getTrigX() < 64 && coords.getTrigY() < 64) {
            g2.drawRect(coords.getTrigX()*24+3, coords.getTrigY()*24+3, 1*24-6, 1*24-6);
        }
    }

    public void setDrawCoords(boolean drawCoords) {
        this.drawCoords = drawCoords;
        this.redraw = true;
    }

    public BattleMapCoords getCoords() {
        return coords;
    }

    public void setCoords(BattleMapCoords coords) {
        this.coords = coords;
    }
    
    public void updateCoordsDisplay(){
        this.redraw = true;
    }
}
