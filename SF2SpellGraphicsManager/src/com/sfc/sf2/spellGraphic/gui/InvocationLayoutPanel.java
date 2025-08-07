/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.spellGraphic.InvocationGraphic;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class InvocationLayoutPanel extends AbstractLayoutPanel {
    
    private InvocationGraphic invocationGraphic;

    @Override
    protected boolean hasData() {
        return invocationGraphic != null && invocationGraphic.getFrames().length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        tilesPerRow = invocationGraphic.getFrames()[0].getTilesPerRow();
        int width = invocationGraphic.getFrameWidth()*PIXEL_WIDTH;
        int height = invocationGraphic.getTotalHeight()*PIXEL_HEIGHT;
            height += PIXEL_HEIGHT;
        return new Dimension(width, height);
    }

    @Override
    protected void buildImage(Graphics graphics) {
        for(int f = 0; f < invocationGraphic.getFrames().length; f++) {
            Tile[] frameTiles = invocationGraphic.getFrames()[f].getTiles();
            for(int t = 0; t < frameTiles.length; t++) {
                int x = (t%16)*8;
                int y = (f*8 + t/16)*8;
                graphics.drawImage(frameTiles[t].getIndexedColorImage(), x, y, null);
            }
        }
        graphics.dispose();
    }

    public InvocationGraphic getInvocationGraphic() {
        return invocationGraphic;
    }

    public void setInvocationGraphic(InvocationGraphic invocationGraphic) {
        this.invocationGraphic = invocationGraphic;
    }
}
