/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import com.sfc.sf2.graphics.Block;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.mapsprite.MapSprite;
import com.sfc.sf2.mapsprite.MapSpriteEntries;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

/**
 *
 * @author wiz
 */
public class MapSpriteLayoutPanel extends AbstractLayoutPanel {
    private static Color LABEL_BG = Color.YELLOW;
        
    private MapSpriteEntries mapsprites;
    private boolean drawReferenceLabels;
    
    public MapSpriteLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH);
        scale = new LayoutScale(1);
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT, Block.PIXEL_WIDTH*2, Block.PIXEL_HEIGHT);
        coordsGrid = new LayoutCoordsGridDisplay(Block.PIXEL_WIDTH, Block.PIXEL_HEIGHT, false, 0, PIXEL_WIDTH, 1);
        coordsHeader = new LayoutCoordsHeader(this, Block.PIXEL_WIDTH, Block.PIXEL_HEIGHT);
        mouseInput = null;
        scroller = new LayoutScrollNormaliser(this);
    }

    @Override
    protected boolean hasData() {
        return mapsprites != null && mapsprites.getMapSprites().length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        if (mapsprites.getMapSprites().length == 1) {
            return new Dimension(mapsprites.getMapSprites()[0].getSpritesWidth()*3*PIXEL_WIDTH, mapsprites.getMapSprites()[0].getSpritesHeight()*3*PIXEL_HEIGHT);
        } else {
            int w = 6*3*PIXEL_WIDTH;    //6 sprites per mapsprite (2x up, 2x left, 2x down)
            int h = mapsprites.getMapSprites().length*3*PIXEL_HEIGHT;
            return new Dimension(w, h);
        }
    }

    @Override
    protected void drawImage(Graphics graphics) {
        if (drawReferenceLabels) {
            graphics.setFont(new Font("SansSerif", 0, 11));
        }
        MapSprite[] sprites = mapsprites.getMapSprites();
        for (int i = 0; i < sprites.length; i++) {
            int index = mapsprites.getEntries()[i];
            if (index >= 0) {
                if (sprites[index] != null) {
                    graphics.drawImage(sprites[index].getIndexedColorImage(), 0, i*3*PIXEL_HEIGHT, null);
                }
                if (drawReferenceLabels && mapsprites.isDuplicateEntry(i)) {
                    graphics.setColor(LABEL_BG);
                    graphics.drawRect(0, i*3*PIXEL_HEIGHT, 6*3*PIXEL_WIDTH-1, 3*PIXEL_HEIGHT-1);
                    graphics.fillRect(5*3*PIXEL_WIDTH+4, i*3*PIXEL_HEIGHT+6, 3*PIXEL_WIDTH, 12);
                    graphics.setColor(Color.BLACK);
                    graphics.drawString(String.format("%03d", index), 5*3*PIXEL_WIDTH+6, i*3*PIXEL_HEIGHT+16);
                }
            }
        }
    }

    public MapSpriteEntries getMapSprites() {
        return mapsprites;
    }

    public void setMapSprites(MapSpriteEntries mapsprites) {
        this.mapsprites = mapsprites;
        redraw();
    }

    public void setDrawReferenceLabels(boolean drawReferenceLabels) {
        this.drawReferenceLabels = drawReferenceLabels;
        redraw();
    }
}
