/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import java.awt.Dimension;

/**
 *
 * @author TiMMy
 */
public class LayoutMouseInput extends BaseMouseCoordsComponent {
        
    public LayoutMouseInput(AbstractLayoutPanel panel, GridMousePressedListener mouseButtonListener, Dimension mouseCoordsGrid) {
        super(mouseCoordsGrid);
        setupListeners(panel, mouseButtonListener, null);
    }
}
