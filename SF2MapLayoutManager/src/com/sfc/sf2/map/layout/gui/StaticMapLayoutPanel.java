/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.layout.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.layout.MapLayout;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_HEIGHT;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_WIDTH;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author TiMMy
 */
public class StaticMapLayoutPanel extends AbstractLayoutPanel {
    
    protected static final int DEFAULT_BLOCKS_PER_ROW = MapLayout.BLOCK_WIDTH;
    protected static final Dimension mapDimensions = new Dimension(BLOCK_WIDTH*PIXEL_WIDTH, BLOCK_HEIGHT*PIXEL_HEIGHT);
        
    protected MapLayout layout;
    
    private boolean drawExplorationFlags = true;
    private boolean drawInteractionFlags = false;

    public StaticMapLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH/3);
        scale = new LayoutScale(1);
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT);
        coordsGrid = new LayoutCoordsGridDisplay(PIXEL_WIDTH, PIXEL_HEIGHT, false, 0, 0, 1);
        coordsHeader = new LayoutCoordsHeader(this, PIXEL_WIDTH, PIXEL_HEIGHT, false);
        mouseInput = null;
        scroller = new LayoutScrollNormaliser(this);
        setItemsPerRow(DEFAULT_BLOCKS_PER_ROW);
    }

    @Override
    protected boolean hasData() {
        return layout != null && layout.getBlockset() != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        return mapDimensions;
    }

    @Override
    protected void drawImage(Graphics graphics) {
        MapBlock[] blocks = layout.getBlockset().getBlocks();
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                drawBlock(blocks[y * BLOCK_WIDTH + x], graphics, x, y);
            }
        }
    }
    
    protected void drawBlock(MapBlock block, Graphics graphics, int x, int y) {
        
        BufferedImage blockImage = block.getIndexedColorImage();
        graphics.drawImage(blockImage, x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
        if (drawExplorationFlags || drawInteractionFlags) { 
            if (drawExplorationFlags){
                int explorationFlags = block.getFlags()&0xC000;
                graphics.drawImage(MapLayoutFlagImages.getBlockExplorationFlagImage(explorationFlags), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null); 
            }
            if (drawInteractionFlags) {
                int interactionFlags = block.getFlags()&0x3C00;
                graphics.drawImage(MapLayoutFlagImages.getBlockInteractionFlagImage(interactionFlags), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null); 
            }
        }
    }

    public MapLayout getMapLayout() {
        return layout;
    }

    public void setMapLayout(MapLayout layout) {
        this.layout = layout;
    }

    public boolean isDrawExplorationFlags() {
        return drawExplorationFlags;
    }

    public void setDrawExplorationFlags(boolean drawExplorationFlags) {
        this.drawExplorationFlags = drawExplorationFlags;
        redraw();
    }
    public boolean isDrawInteractionFlags() {
        return drawInteractionFlags;
    }

    public void setDrawInteractionFlags(boolean drawInteractionFlags) {
        this.drawInteractionFlags = drawInteractionFlags;
        redraw();
    }
}
