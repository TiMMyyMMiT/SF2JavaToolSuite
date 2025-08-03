/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 *
 * @author wiz
 */
public class GraphicsLayoutPanel extends AbstractLayoutPanel {
    
    private Tileset tileset;

    @Override
    protected boolean hasData() {
        return tileset != null && tileset.getTiles().length > 0;
    }

    @Override
    protected BufferedImage buildImage() {
        return tileset.getIndexedColorImage();
    }
    
    public Tileset getTileset() {
        return tileset;
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
        redraw();
    }
}
