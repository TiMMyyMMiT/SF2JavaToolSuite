/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.map.block.MapBlock;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author wiz
 */
public class BlockSlotPanel extends AbstractLayoutPanel {
    
    protected MapBlock block;
    protected Tileset[] tilesets;
    private BufferedImage overrideImage;    //Required to render a non-block
    
    public BlockSlotPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH/2);
        scale = new LayoutScale(2);
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT);
        coordsGrid = null;
        coordsHeader = null;
        mouseInput = null;
        scroller = null;
    }

    @Override
    protected boolean hasData() {
        return block != null || overrideImage != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        return new Dimension(PIXEL_WIDTH*3, PIXEL_HEIGHT*3);
    }

    @Override
    protected void drawImage(Graphics graphics) {
        if (overrideImage != null) {
            graphics.drawImage(overrideImage, 0, 0, null);
        } else if (block != null) {
            graphics.drawImage(block.getIndexedColorImage(tilesets), 0, 0, null);
        }
    }
    
    public MapBlock getBlock() {
        return block;
    }
    
    public int getBlockIndex() {
        return block == null ? -1 : block.getIndex();
    }

    public void setBlock(MapBlock block) {
        this.block = block;
        overrideImage = null;
        redraw();
    }

    public void setTilesets(Tileset[] tilesets) {
        this.tilesets = tilesets;
    }
    
    public BufferedImage getOverrideImage() {
        return overrideImage;
    }

    public void setOverrideImage(BufferedImage overrideImage) {
        this.overrideImage = overrideImage;
        block = null;
        redraw();
    }
}
