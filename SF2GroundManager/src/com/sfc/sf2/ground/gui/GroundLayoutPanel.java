/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.ground.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.ground.Ground;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class GroundLayoutPanel extends AbstractLayoutPanel {
        
    private Ground ground;
    
    public GroundLayoutPanel() {
        super();
        tilesPerRow = Ground.GROUND_TILES_PER_ROW;
        setGridDimensions(PIXEL_WIDTH, PIXEL_HEIGHT);
    }

    @Override
    protected boolean hasData() {
        return ground != null && ground.getTileset() != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        return ground.getTileset().getDimensions(tilesPerRow);
    }

    @Override
    protected void buildImage(Graphics graphics) {
        graphics.drawImage(ground.getTileset().getIndexedColorImage(), 0, 0, null);
    }
    
    public Ground getGround() {
        return ground;
    }

    public void setGround(Ground ground) {
        this.ground = ground;
        redraw();
    }
}
