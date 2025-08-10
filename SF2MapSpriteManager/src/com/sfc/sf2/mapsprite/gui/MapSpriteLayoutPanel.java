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
    
    private static final int DEFAULT_SPRITES_PER_ROW = 6;   //Sprite-pairs
    
    private MapSprite[] mapsprites;
    
    public MapSpriteLayoutPanel() {
        super();
        setGridDimensions(8, 8, -1, PIXEL_HEIGHT*2);
    }

    @Override
    protected boolean hasData() {
        return mapsprites != null && mapsprites.length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        return new Dimension(DEFAULT_SPRITES_PER_ROW*PIXEL_WIDTH, mapsprites.length*PIXEL_HEIGHT);
    }

    @Override
    protected void buildImage(Graphics graphics) {
        graphics.drawImage(mapsprites[0].getIndexedColorImage(), 0, PIXEL_HEIGHT, this);
        /*for (int i = 0; i < mapsprites.length; i++) {
            if (mapsprites[i] != null && mapsprites[i].getTileset() != null) {
                graphics.drawImage(mapsprites[i].getIndexedColorImage(), 0, i*PIXEL_HEIGHT, this);
            }
        }*/
    }

    public MapSprite[] getMapSprite() {
        return mapsprites;
    }

    public void setMapSprite(MapSprite[] mapsprites) {
        this.mapsprites = mapsprites;
    }
}
