/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.gui;

import com.sfc.sf2.core.gui.AbstractBasicPanel;
import com.sfc.sf2.map.block.MapBlock;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author wiz
 */
public class BlockSlotPanel extends AbstractBasicPanel {
    
    protected MapBlock block;
    private BufferedImage overrideImage;    //Required to render a non-block

    @Override
    protected boolean hasData() {
        return block != null || overrideImage != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        return getSize();
    }

    @Override
    protected void buildImage(Graphics graphics) {
        if (overrideImage != null) {
            graphics.drawImage(overrideImage, 0, 0, this.getWidth(), this.getHeight(), null);
        } else if (block != null) {
            graphics.drawImage(block.getIndexedColorImage(), 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }
    
    public MapBlock getBlock() {
        return block;
    }

    public void setBlock(MapBlock block) {
        this.block = block;
        overrideImage = null;
        redraw();
        this.repaint();
    }
    
    public BufferedImage getOverrideImage() {
        return overrideImage;
    }

    public void setOverrideImage(BufferedImage overrideImage) {
        this.overrideImage = overrideImage;
        block = null;
        redraw();
        this.repaint();
    }
}
