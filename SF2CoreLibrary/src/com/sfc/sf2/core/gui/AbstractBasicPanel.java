/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui;

import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.helpers.GraphicsHelpers;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author TiMMy
 */
public abstract class AbstractBasicPanel extends JPanel {
    
    private static Dimension NO_OFFSET = new Dimension();
    
    protected BufferedImage currentImage;
    private boolean redraw = true;
    private int displayScale = 1;
    
    protected boolean bgCheckerPattern = true;  
    protected boolean showGrid = false;    
    protected int gridWidth = -1;
    protected int gridHeight = -1;
    
    protected Color bgColor = Color.WHITE;

    public AbstractBasicPanel() {
        super();
        bgColor = SettingsManager.getGlobalSettings().getTransparentBGColor();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (hasData()) {
            Dimension dims = getImageDimensions();
            Dimension offset = getImageOffset();
            g.drawImage(paintImage(dims), offset.width, offset.height, this);
            Dimension size = new Dimension(currentImage.getWidth()+offset.width, currentImage.getHeight()+offset.height);
            setSize(size);
            setPreferredSize(size);
        }
    }
    
    public BufferedImage paintImage(Dimension dims) {
        if (!redraw || !hasData()) return currentImage;
        
        if (currentImage != null) {
            currentImage.flush();
        }
        //Setup image
        currentImage = new BufferedImage(dims.width, dims.height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = currentImage.getGraphics();
        //Render BG color
        if (bgCheckerPattern) {
            GraphicsHelpers.drawBackgroundTransparencyPattern(currentImage, bgColor, 4);
        } else {
            GraphicsHelpers.drawFlatBackgroundColor(currentImage, bgColor);
        }
        //Render main image
        buildImage(graphics);
        graphics.dispose();
        //Resize
        currentImage = resize(currentImage);
        //DrawGrid
        if (showGrid) { drawGrid(currentImage); }
        getParent().revalidate();

        redraw = false;
        return currentImage;
    }
    
    protected void drawGrid(BufferedImage image) {
        if (gridWidth >= 0 || gridHeight >= 0) {
            GraphicsHelpers.drawGrid(image, gridWidth, gridHeight, 1);
        }
    }
    
    protected BufferedImage resize(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth()*displayScale, image.getHeight()*displayScale, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(image, 0, 0, image.getWidth()*displayScale, image.getHeight()*displayScale, null);
        g.dispose();
        image.flush();
        return newImage;
    }
    
    protected abstract boolean hasData();    
    protected abstract Dimension getImageDimensions();
    protected abstract void buildImage(Graphics graphics);
    
    protected Dimension getImageOffset() {
        return NO_OFFSET;
    }
    
    protected void setGridDimensions(int gridW, int gridH) {
        this.gridWidth = gridW;
        this.gridHeight = gridH;
    }
    
    public boolean isRedrawing() {
        return redraw;
    }

    public void redraw() {
        this.redraw = true;
    }

    public int getDisplayScale() {
        return displayScale;
    }

    public void setDisplayScale(int displayScale) {
        this.displayScale = displayScale;
        redraw();
    }

    public Color getBGColor() {
        return bgColor;
    }

    public void setBGColor(Color bgColor) {
        this.bgColor = bgColor;
        redraw();
    }
    
    public boolean isShowGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        redraw();
    }
}

