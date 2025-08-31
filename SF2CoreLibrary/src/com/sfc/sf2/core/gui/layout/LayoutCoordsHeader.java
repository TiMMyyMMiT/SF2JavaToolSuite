/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.controls.Console;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 *
 * @author TiMMy
 */
public class LayoutCoordsHeader extends BaseMouseCoordsComponent {
    
    private JComponent coordsContainer;
    private TitledBorder coordsTitle;
    private String origTitle;

    public LayoutCoordsHeader(AbstractLayoutPanel panel, Dimension coordsGrid) {
        super(coordsGrid);
        setupListeners(panel, null, this::onMouseMotion);
        java.awt.EventQueue.invokeLater(() -> { getCoordsTitle(panel); });
    }
    
    private void getCoordsTitle(Container panel) {
        try {
            Border border = null;
            Container parent = panel;
            for (int i = 0; i < 3; i++) {
                parent = parent.getParent();
                if (parent instanceof JScrollPane) {
                    coordsContainer = (JComponent)parent;
                    border = ((JScrollPane)coordsContainer).getViewportBorder();
                } else if (parent instanceof JComponent) {
                    coordsContainer = (JComponent)parent;
                    border = coordsContainer.getBorder();
                }
                if (border != null && border instanceof TitledBorder) {
                    coordsTitle = (TitledBorder)border;
                    break;
                }
            }
        } catch (Exception e) { }
        if (coordsTitle == null) {
            Console.logger().warning("LayoutCoordsHeader could not find title for " + panel.getName() + ". LayoutCoordsHeader will be disabled");
            setEnabled(false);
        } else {
            origTitle = coordsTitle.getTitle();
        }
    }

    private void onMouseMotion(GridMouseMoveEvent evt) {
        if (evt.x() < 0 || evt.y() < 0) {
            coordsTitle.setTitle(String.format("%s : (-, -)", origTitle));
        } else {
            coordsTitle.setTitle(String.format("%s : (%d, %d)", origTitle, evt.x(), evt.y()));
        }
        coordsContainer.repaint();
    }
}
