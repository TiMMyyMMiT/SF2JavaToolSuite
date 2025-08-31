/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.controls.Console;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;

/**
 *
 * @author TiMMy
 */
public abstract class BaseMouseCoordsComponent extends BaseLayoutComponent implements MouseListener, MouseMotionListener {
    
    private final Dimension mouseCoordsGrid;
    private GridMousePressedListener buttonListener;
    private GridMouseMoveListener motionListener;
    
    private Dimension coordsOffset;
    private int displayScale = 1;
    
    private int lastX = -1;
    private int lastY = -1;
    private int buttonHeld = -1;
    

    public BaseMouseCoordsComponent(Dimension mouseCoordsGrid) {
        this.mouseCoordsGrid = mouseCoordsGrid;
            setEnabled(false);  //Disabled until listeners are setup
    }
    
    protected void setupListeners(AbstractLayoutPanel panel, GridMousePressedListener buttonListener, GridMouseMoveListener motionListener) {
        this.buttonListener = buttonListener;
        this.motionListener = motionListener;
        if (buttonListener != null) {
            panel.addMouseListener(this);
        }
        if (motionListener != null) {
            panel.addMouseMotionListener(this);
        }
        if (buttonListener == null && motionListener == null) {
            Console.logger().warning("WARNING Mouse coords component instantiated in " + panel.getName() + " without any callbacks. Will be disabled.");
            setEnabled(false);
        } else {
            setEnabled(true);
        }
    }
    
    public void updateDisplayParameters(int displayScale, Dimension coordsOffset) {
        this.displayScale = displayScale;
        this.coordsOffset = coordsOffset;
    }
    
    private int getXCoord(int mouseX) {
        int x = mouseX - coordsOffset.width;
        x /= (displayScale * mouseCoordsGrid.width);
        return x;
    }
    
    private int getYCoord(int mouseY) {
        int y = mouseY - coordsOffset.height;
        y /= (displayScale * mouseCoordsGrid.height);
        return y;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseMoved(MouseEvent e) {
        if (motionListener == null) return;
        int x = getXCoord(e.getX());
        int y = getYCoord(e.getY());
        if (x == lastX && y == lastY) return;
        motionListener.mouseMoved(new GridMouseMoveEvent(x, y));
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (buttonListener == null) return;
        int x = getXCoord(e.getX());
        int y = getYCoord(e.getY());
        if (x == lastX && y == lastY) return;
        lastX = x;
        lastY = y;
        buttonHeld = e.getButton();
        buttonListener.mousePressed(new GridMousePressedEvent(x, y, buttonHeld, false));
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (buttonListener == null) return;
        int x = getXCoord(e.getX());
        int y = getYCoord(e.getY());
        if (x == lastX && y == lastY) return;
        lastX = x;
        lastY = y;
        buttonListener.mousePressed(new GridMousePressedEvent(x, y, buttonHeld, true));
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        buttonHeld = -1;
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        buttonHeld = -1;
    }
    
    public record GridMousePressedEvent(int x, int y, int mouseButton, boolean dragging) { }
    public interface GridMousePressedListener extends EventListener {
        public void mousePressed(GridMousePressedEvent evt);
    }
    
    public record GridMouseMoveEvent(int x, int y) { }
    public interface GridMouseMoveListener extends EventListener {
        public void mouseMoved(GridMouseMoveEvent evt);
    }
}