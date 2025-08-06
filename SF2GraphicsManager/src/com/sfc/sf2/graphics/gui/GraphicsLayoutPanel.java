/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author wiz
 */
public class GraphicsLayoutPanel extends AbstractLayoutPanel { 
    
    private Tileset tileset;
    
    public GraphicsLayoutPanel() {
        super();
        setGridDimensions(8, 8);
    }

    @Override
    protected boolean hasData() {
        return tileset != null && tileset.getTiles().length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        tileset.setTilesPerRow(tilesPerRow);
        int width = tileset.getTilesPerRow()*PIXEL_WIDTH;
        int height = tileset.getTiles().length/tileset.getTilesPerRow()*PIXEL_HEIGHT;
        if (tileset.getTiles().length%tileset.getTilesPerRow() != 0)
            height += PIXEL_HEIGHT;
        return new Dimension(width, height);
    }

    @Override
    protected void buildImage(Graphics graphics) {
        tileset.clearIndexedColorImage();
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
