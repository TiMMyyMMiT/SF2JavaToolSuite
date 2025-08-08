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
import static com.sfc.sf2.spellGraphic.InvocationGraphic.INVOCATION_TILE_HEIGHT;
import static com.sfc.sf2.spellGraphic.InvocationGraphic.INVOCATION_TILE_WIDTH;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class InvocationLayoutPanel extends AbstractLayoutPanel {
    
    private InvocationGraphic invocationGraphic;
    
    public InvocationLayoutPanel() {
        super();
        setGridDimensions(8, 8, -1, INVOCATION_TILE_HEIGHT);
    }

    @Override
    protected boolean hasData() {
        return invocationGraphic != null && invocationGraphic.getFrames().length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        int width = invocationGraphic.getFrameWidth()*PIXEL_WIDTH;
        int height = invocationGraphic.getTotalHeight()*PIXEL_HEIGHT;
        return new Dimension(width, height);
    }

    @Override
    protected void buildImage(Graphics graphics) {
        for(int f = 0; f < invocationGraphic.getFrames().length; f++) {
            Tile[] frameTiles = invocationGraphic.getFrames()[f].getTiles();
            for(int t = 0; t < frameTiles.length; t++) {
                int x = (t%INVOCATION_TILE_WIDTH)*8;
                int y = ((f*INVOCATION_TILE_HEIGHT)*8 + t/INVOCATION_TILE_WIDTH)*8;
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
        redraw();
    }
}
