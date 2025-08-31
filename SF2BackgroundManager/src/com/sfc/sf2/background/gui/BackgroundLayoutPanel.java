/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.background.gui;

import com.sfc.sf2.background.Background;
import static com.sfc.sf2.background.Background.BG_TILES_HEIGHT;
import static com.sfc.sf2.background.Background.BG_TILES_WIDTH;
import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.LayoutCoordsGridDisplay;
import com.sfc.sf2.core.gui.layout.LayoutGrid;
import com.sfc.sf2.core.gui.layout.LayoutScale;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class BackgroundLayoutPanel extends AbstractLayoutPanel {
    
    private Background[] backgrounds;
    
    public BackgroundLayoutPanel() {
        super(
            /*Background*/    null,
            /*Scale*/         new LayoutScale(1),
            /*Grid*/          new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT, -1, BG_TILES_HEIGHT*PIXEL_HEIGHT),
            /*Coords*/        new LayoutCoordsGridDisplay(0, BG_TILES_HEIGHT*PIXEL_HEIGHT, false, 10, 0, 2),
            /*Coords Header*/ null,
            /*Input*/         null);
        tilesPerRow = BG_TILES_WIDTH;
    }

    @Override
    protected boolean hasData() {
        return backgrounds != null && backgrounds.length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        return  new Dimension(BG_TILES_WIDTH*PIXEL_WIDTH, BG_TILES_HEIGHT*backgrounds.length*PIXEL_HEIGHT);
    }

    @Override
    protected void paintImage(Graphics graphics) {
        for(int b = 0; b < backgrounds.length; b++) {
            graphics.drawImage(backgrounds[b].getTileset().getIndexedColorImage(), 0, b*BG_TILES_HEIGHT*PIXEL_HEIGHT, null);
        }
    }
    
    public Background[] getBackgrounds() {
        return backgrounds;
    }

    public void setBackgrounds(Background[] backgrounds) {
        this.backgrounds = backgrounds;
        redraw();
    }
}
