/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.animation.gui;

import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.gui.TilesetLayoutPanel;
import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.animation.MapAnimationFrame;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class MapAnimationTilesetLayoutPanel extends TilesetLayoutPanel {
    
    private MapAnimation mapAnimation;
    private int selectedFrame = -1;
    
    private boolean showAnimationFrames;
    private boolean previewAnimation;

    @Override
    protected void drawImage(Graphics graphics) {
        super.drawImage(graphics);
        if (!showAnimationFrames) return;
        
        graphics.setColor(Color.WHITE);
        int tilesPerRow = getItemsPerRow();
        MapAnimationFrame[] frames = mapAnimation.getFrames();
        for (int i = 0; i < frames.length; i++) {
            if (i == selectedFrame) {
                continue;
            } else {
                drawFrame(graphics, frames[i], tilesPerRow);
            }
        }
        if (selectedFrame >= 0 && selectedFrame < frames.length) {
            graphics.setColor(Color.YELLOW);
            drawFrame(graphics, frames[selectedFrame], tilesPerRow);
        }
    }
    
    private void drawFrame(Graphics graphics, MapAnimationFrame frame, int tilesPerRow) {
            int start = frame.getStart();
            int length = frame.getLength();
            int rows = length/tilesPerRow;
            if (frame.getLength()%tilesPerRow != 0) {
                rows++;
            }
            length -= 1;
            graphics.fillArc((start%tilesPerRow)*PIXEL_WIDTH+3, (start/tilesPerRow)*PIXEL_HEIGHT-1, PIXEL_WIDTH+1, PIXEL_HEIGHT+2, 135, 90);
            for (int r = 0; r < rows; r++) {
                int x1 = r == 0 ? ((start+1)%tilesPerRow)*PIXEL_WIDTH-2 : -10;
                int x2 = r == rows-1 ? ((start+length)%tilesPerRow)*PIXEL_WIDTH+2 : tilesPerRow*PIXEL_WIDTH+10;
                int y = (start/tilesPerRow+r)*PIXEL_HEIGHT+4;
                graphics.drawLine(x1, y, x2, y);
            }
            graphics.fillArc(((start+length)%tilesPerRow)*PIXEL_WIDTH-4, ((start+length)/tilesPerRow)*PIXEL_HEIGHT-1, PIXEL_WIDTH+1, PIXEL_HEIGHT+2, 315, 90);
    }

    public void setMapAnimation(MapAnimation mapAnimation) {
        this.mapAnimation = mapAnimation;
    }

    public void setSelectedFrame(int selectedFrame) {
        if (this.selectedFrame != selectedFrame) {
            this.selectedFrame = selectedFrame;
            redraw();
        }
    }

    public void setShowAnimationFrames(boolean showAnimationFrames) {
        this.showAnimationFrames = showAnimationFrames;
        redraw();
    }

    public void setPreviewAnimation(boolean previewAnimation) {
        this.previewAnimation = previewAnimation;
    }
}
