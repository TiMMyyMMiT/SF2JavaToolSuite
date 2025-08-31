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
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
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
        super(
            /*Background*/    new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH/2),
            /*Scale*/         new LayoutScale(1),
            /*Grid*/          new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT),
            /*Coords*/        null,
            /*Coords Header*/ null,
            /*Input*/         null);
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
