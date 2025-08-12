/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.mapsprite.MapSprite;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author wiz
 */
public class MapSpriteLayoutPanel extends AbstractLayoutPanel {
    
    private static final int DEFAULT_SPRITES_PER_ROW = 6;   //up, left, down
    
    private MapSprite[] mapsprites;
    
    public MapSpriteLayoutPanel() {
        super();
        setGridDimensions(8, 8, -1, PIXEL_HEIGHT*3);
    }

    @Override
    protected boolean hasData() {
        return mapsprites != null && mapsprites.length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        int w = DEFAULT_SPRITES_PER_ROW*3*PIXEL_WIDTH;
        int h = (mapsprites.length*2/DEFAULT_SPRITES_PER_ROW)*3*PIXEL_HEIGHT;
        return new Dimension(w, h);
    }

    @Override
    protected void buildImage(Graphics graphics) {
        int xStep = 2*3*PIXEL_WIDTH;
        int w = xStep*DEFAULT_SPRITES_PER_ROW;
        int yStep = 3*PIXEL_HEIGHT;
        int x = 0, y = 0;
        for (int i = 0; i < mapsprites.length; i++) {
            if (mapsprites[i] != null && mapsprites[i].getTileset() != null) {
                graphics.drawImage(mapsprites[i].getIndexedColorImage(), x, y, null);
                x += xStep;
                if (x >= w) {
                    x = 0;
                    y += yStep;
                }
            }
        }
    }

    public MapSprite[] getMapSprite() {
        return mapsprites;
    }

    public void setMapSprite(MapSprite[] mapsprites) {
        this.mapsprites = mapsprites;
    }
}
