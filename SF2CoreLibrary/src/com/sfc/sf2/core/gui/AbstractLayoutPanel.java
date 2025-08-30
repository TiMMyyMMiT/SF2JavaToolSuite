/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui;

import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.helpers.GraphicsHelpers;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 *
 * @author TiMMy
 */
public abstract class AbstractLayoutPanel extends AbstractBasicPanel implements MouseMotionListener {
    private static final int COORDS_PADDING_X = 6;
    private static final int COORDS_PADDING_Y = 6;
    private static final int COORDS_PADDING_SCALE = 2;
    
    protected int tilesPerRow = 8;
    
    private int thickGridWidth = -1;
    private int thickGridHeight = -1;
    
    private BufferedImage coordsImageX;
    private BufferedImage coordsImageY;
    private boolean showCoords = false;
    private int coordsX = -1;
    private int coordsY = -1;
    private int verticalPadding = 0;
    
    protected boolean showCoordsInTitle = true;
    private int lastX;
    private int lastY;
    private JComponent coordsContainer;
    private TitledBorder coordsTitle;
    private String origTitle;

    public AbstractLayoutPanel() {
        super();
        addMouseMotionListener(this);
        java.awt.EventQueue.invokeLater(() -> { getCoordsTitle(); });
    }
    
    private void getCoordsTitle() {
        if (!showCoords) return;
        try {
            Border border = null;
            Container parent = this;
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
        if (coordsTitle != null) {
            origTitle = coordsTitle.getTitle();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        boolean redrawing = isRedrawing();
        super.paintComponent(g);
        if (hasData()) {
            Dimension dims = getImageDimensions();
            int offsetX = 0;
            int offsetY = 0;
            if (showCoords) {
                if (redrawing) {
                    coordsImageX = paintCoordsImage(true, dims.width, coordsX);
                    coordsImageY = paintCoordsImage(false, dims.height, coordsY);
                }
                g.drawImage(coordsImageX, offsetX, 0, this);
                g.drawImage(coordsImageY, 0, offsetY, this);
            }
        }
    }
    
    @Override
    protected void drawGrid(BufferedImage image) {
        if (gridWidth >= 0 || gridHeight >= 0) {
            GraphicsHelpers.drawGrid(image, gridWidth*getDisplayScale(), gridHeight*getDisplayScale(), 1);
            if (thickGridWidth >= 0 || thickGridHeight >= 0) {
                GraphicsHelpers.drawGrid(image, thickGridWidth*getDisplayScale(), thickGridHeight*getDisplayScale(), 3);
            }
        }
    }
    
    private BufferedImage paintCoordsImage(boolean xAxis, int imageSize, int coordsSize) {
        imageSize *= getDisplayScale();
        coordsSize *= getDisplayScale();
        int padding = (xAxis ? COORDS_PADDING_Y : COORDS_PADDING_X+verticalPadding) + COORDS_PADDING_SCALE*getDisplayScale();
        BufferedImage image = new BufferedImage(xAxis ? imageSize : padding, xAxis ? padding : imageSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D)image.getGraphics();
        int fontSize = 4+2*getDisplayScale();
        g2.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        FontMetrics fontMetrics = g2.getFontMetrics();
        g2.setColor(SettingsManager.getGlobalSettings().getIsDarkTheme() ? Color.WHITE : Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int count = imageSize/coordsSize;
        float halfPadding = padding*0.5f;
        float offset = coordsSize*0.5f + 1 + getDisplayScale();
        for (int i = 0; i <= count; i++) {
            String item = Integer.toString(i);
            float textWidth = (float)fontMetrics.getStringBounds(item, g2).getWidth();
            float x = xAxis ? i*coordsSize + offset - textWidth : halfPadding-1 - textWidth*0.5f;
            float y = xAxis ? halfPadding+1 + getDisplayScale()*0.5f : i*coordsSize+offset;
            g2.drawString (Integer.toString(i), x, y);
        }
        g2.setColor(SettingsManager.getGlobalSettings().getIsDarkTheme() ? Color.BLACK : Color.WHITE);
        for (int i = 0; i <= count; i++) {
            if (xAxis) {
                g2.drawLine(i*coordsSize, 0, i*coordsSize, padding);
            } else {
                g2.drawLine(0, i*coordsSize, padding, i*coordsSize);
            }
        }
        if (xAxis) {
            g2.drawLine(imageSize-1, 0, imageSize-1, padding);
        } else {
            g2.drawLine(0, imageSize-1, padding, imageSize-1);
        }
        g2.dispose();
        return image;
    }
    
    @Override
    protected Dimension getImageOffset() {
        if (showCoords) {
            return new Dimension(verticalPadding+COORDS_PADDING_X+getDisplayScale()*COORDS_PADDING_SCALE, COORDS_PADDING_Y+getDisplayScale()*COORDS_PADDING_SCALE);
        } else {
            return super.getImageOffset();
        }
    }
    
    @Override
    protected void setGridDimensions(int gridW, int gridH) {
        setGridDimensions(gridW, gridH, -1, -1);
    }
    
    protected void setGridDimensions(int gridW, int gridH, int thickGridW, int thickGridH) {
        super.setGridDimensions(gridW, gridH);
        this.thickGridWidth = thickGridW;
        this.thickGridHeight = thickGridH;
    }
    
    protected void setCoordsDimensions(int rowSize, int columnSize, int verticalPadding) {
        this.showCoords = true;
        this.coordsX = rowSize;
        this.coordsY = columnSize;
        this.verticalPadding = verticalPadding;
    }
    
    public int getTilesPerRow() {
        return tilesPerRow;
    }

    public void setTilesPerRow(int tilesPerRow) {
        this.tilesPerRow = tilesPerRow;
        redraw();
    }

    @Override
    public void mouseDragged(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!hasData() || !showCoordsInTitle || coordsTitle == null) return;
        Dimension coordsPadding = getImageOffset();
        int x = e.getX() - coordsPadding.width;
        int y = e.getY() - coordsPadding.height;
        x /= (getDisplayScale() * coordsX);
        y /= (getDisplayScale() * coordsY);
        if (lastX != x || lastY != y) {
            lastX = x;
            lastY = y;
            coordsTitle.setTitle(String.format("%s : (%d, %d)", origTitle, x, y));
            coordsContainer.repaint();
        }
    }
}

