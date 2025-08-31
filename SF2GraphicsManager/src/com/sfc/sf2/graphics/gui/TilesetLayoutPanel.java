/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.LayoutBackground;
import com.sfc.sf2.core.gui.layout.LayoutGrid;
import com.sfc.sf2.core.gui.layout.LayoutScale;
import com.sfc.sf2.graphics.Tileset;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author wiz
 */
public class TilesetLayoutPanel extends AbstractLayoutPanel { 
    
    private Tileset tileset;
    
    public TilesetLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, 4);
        scale = new LayoutScale(1);
        grid = new LayoutGrid(8, 8);
    }

    @Override
    protected boolean hasData() {
        return tileset != null && tileset.getTiles().length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        return tileset.getDimensions(tilesPerRow);
    }

    @Override
    protected void paintImage(Graphics graphics) {
        tileset.clearIndexedColorImage(false);
        graphics.drawImage(tileset.getIndexedColorImage(), 0, 0, null);
    }
    
    public Tileset getTileset() {
        return tileset;
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
        redraw();
    }
}
