/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.gui;

import com.sfc.sf2.core.gui.CoreLayoutPanel;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 *
 * @author wiz
 */
public class GraphicsLayoutPanel extends CoreLayoutPanel {
    
    private Tileset tileset;
    
    @Override
    protected Dimension calculateImageSize() {
        int tileCount = tileset.getTiles().length;
        int imageHeight = (tileCount/tilesPerRow);
        if(tileCount%tilesPerRow != 0) {
            imageHeight++;
        }
        return new Dimension(tilesPerRow*Tile.PIXEL_WIDTH, imageHeight*Tile.PIXEL_HEIGHT);
    }

    @Override
    protected BufferedImage buildImage(BufferedImage image) {
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
