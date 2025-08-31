/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

import com.sfc.sf2.helpers.GraphicsHelpers;
import java.awt.image.BufferedImage;

/**
 *
 * @author TiMMy
 */
public class LayoutGrid extends BaseLayoutComponent {
    
    private int gridWidth = -1;
    private int gridHeight = -1;
    private int thickGridWidth = -1;
    private int thickGridHeight = -1;
    
    public LayoutGrid(int gridWidth, int gridHeight) {
        this(gridWidth, gridHeight, -1, -1);
    }
    
    public LayoutGrid(int gridWidth, int gridHeight, int thickGridWidth, int thickGridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.thickGridWidth = thickGridWidth;
        this.thickGridHeight = thickGridHeight;
    }
    
    public void paintGrid(BufferedImage image, int displayScale) {
        if (gridWidth >= 0 || gridHeight >= 0) {
            GraphicsHelpers.drawGrid(image, gridWidth*displayScale, gridHeight*displayScale, 1);
        }
        if (thickGridWidth >= 0 || thickGridHeight >= 0) {
            GraphicsHelpers.drawGrid(image, thickGridWidth*displayScale, thickGridHeight*displayScale, 3);
        }
    }
}
