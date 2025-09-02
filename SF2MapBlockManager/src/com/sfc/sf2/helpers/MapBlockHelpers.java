/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

import com.sfc.sf2.graphics.Block;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tile;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class MapBlockHelpers {
    public static final Color PRIORITY_COLOR = new Color(255, 255, 0, 100);
    public static final Color PRIORITY_DARKEN_COLOR = new Color(0, 0, 0, 100);
    
    public static void drawTilePriorities(Graphics graphics, Block[] blocks, int blocksPerRow) {
        for (int b=0; b < blocks.length; b++) {
            Tile[] tiles = blocks[b].getTiles();
            drawTilePriorities(graphics, tiles, (b%blocksPerRow)*PIXEL_WIDTH, (b/blocksPerRow)*PIXEL_HEIGHT);
        }
    }
    
    public static void drawTilePriorities(Graphics graphics, Tile[] tiles, int x, int y) {
        for (int t=0; t < tiles.length; t++) {
            if (tiles[t].isHighPriority()) {
                graphics.setColor(PRIORITY_COLOR);
            } else {
                graphics.setColor(PRIORITY_DARKEN_COLOR);
            }
            graphics.fillRect(x+(t%Block.TILE_WIDTH)*Tile.PIXEL_WIDTH, y+(t/Block.TILE_WIDTH)*Tile.PIXEL_HEIGHT, Tile.PIXEL_WIDTH, Tile.PIXEL_HEIGHT);
        }
    }
}
