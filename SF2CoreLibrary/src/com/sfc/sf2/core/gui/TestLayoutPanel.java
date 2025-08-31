/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui;

import com.sfc.sf2.core.gui.layout.LayoutBackground;
import com.sfc.sf2.core.gui.layout.LayoutCoordsGridDisplay;
import com.sfc.sf2.core.gui.layout.LayoutGrid;
import com.sfc.sf2.core.gui.layout.LayoutScale;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class TestLayoutPanel extends AbstractLayoutPanel {

    public TestLayoutPanel() {
        super(
            /*Background*/    new LayoutBackground(Color.YELLOW, 8),
            /*Scale*/         new LayoutScale(2),
            /*Grid*/          new LayoutGrid(8, 8, 24, 24),
            /*Coords*/        new LayoutCoordsGridDisplay(24, 24, true, 10, 0, 2),
            /*Coords Header*/ null,
            /*Input*/         null);
    }
    
    @Override
    protected boolean hasData() {
        return true;
    }

    @Override
    protected Dimension getImageDimensions() {
        return getSize();
    }

    @Override
    protected void paintImage(Graphics graphics) {
        Dimension d = getSize();
        int halfHeight = d.height/2;
        Color c = null;
        for (int j = 0; j < d.height; j++) {
            for (int i = 0; i < d.width; i++) {
                if (j <= halfHeight) {
                    c = Color.getHSBColor(i*0.01f, 1f, (halfHeight-j)*2*0.01f);
                } else {
                    c = Color.getHSBColor(i*0.01f, (j-halfHeight)*2*0.01f, 1f);
                }
                graphics.setColor(c);
                graphics.fillRect(i, j, 1, 1);
            }
        }
    }
    
}
