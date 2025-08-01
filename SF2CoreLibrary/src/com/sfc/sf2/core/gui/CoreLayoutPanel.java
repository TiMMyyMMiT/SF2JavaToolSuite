/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui;

import com.sfc.sf2.application.settings.CoreSettings;
import com.sfc.sf2.application.settings.SettingsManager;
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
public abstract class CoreLayoutPanel extends JPanel {
        
    protected int tilesPerRow = 8;
    private int displayScale = 1;
    
    private BufferedImage currentImage;
    private boolean redraw = true;
    private int renderCounter = 0;
    
    protected boolean showGrid = false;    
    private int gridWidth = -1;
    private int gridHeight = -1;
    private int thickGridWidth = -1;
    private int thickGridHeigth = -1;
    
    protected Color bgColor = null;

    public CoreLayoutPanel() {
        CoreSettings core = SettingsManager.getSettingsStore("core");
        bgColor = core.getTransparentBGColor();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public BufferedImage buildImage(){
        if (redraw) {
            //Setup image
            Dimension imageSize = calculateImageSize();
            currentImage = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = currentImage.getGraphics();
            //Render BG color
            GraphicsHelpers.drawBackgroundTransparencyPattern(currentImage, bgColor);
            //Render main image
            renderCounter++;
            System.getLogger(CoreLayoutPanel.class.getName()).log(System.Logger.Level.ALL, "render " + renderCounter);
            currentImage = buildImage(currentImage);
            graphics.dispose();
            //Resize
            currentImage = resize(currentImage);
            setSize(currentImage.getWidth(), currentImage.getHeight());
            //DrawGrid
            if (showGrid) { drawGrid(currentImage); }
            getParent().revalidate();
                
            redraw = false;
        }
        return currentImage;
    }
    
    protected abstract Dimension calculateImageSize();
    
    protected abstract BufferedImage buildImage(BufferedImage image);
    
    protected void setGridDimensions(int gridW, int gridH) {
        setGridDimensions(gridW, gridH, -1, -1);
    }
    
    protected void setGridDimensions(int gridW, int gridH, int thickGridW, int thickGridH) {
        this.gridWidth = gridW;
        this.gridHeight = gridH;
        this.thickGridWidth = thickGridW;
        this.thickGridHeigth = thickGridH;
    }
    
    private void drawGrid(BufferedImage image) {
        if (gridWidth >= 0 || gridHeight >= 0) {
            GraphicsHelpers.drawGrid(image, gridWidth*displayScale, gridHeight*displayScale, 1);
            if (thickGridWidth >= 0 || thickGridHeigth >= 0) {
                GraphicsHelpers.drawGrid(image, thickGridWidth*displayScale, thickGridHeigth*displayScale, 3);
            }
        }
    }
    
    public void resize(int size){
        this.displayScale = size;
        currentImage = resize(currentImage);
    }
    
    private BufferedImage resize(BufferedImage image){
        BufferedImage newImage = new BufferedImage(image.getWidth()*displayScale, image.getHeight()*displayScale, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(image, 0, 0, image.getWidth()*displayScale, image.getHeight()*displayScale, null);
        g.dispose();
        return newImage;
    }    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }
    
    public int getTilesPerRow() {
        return tilesPerRow;
    }

    public void setTilesPerRow(int tilesPerRow) {
        this.tilesPerRow = tilesPerRow;
    }

    public int getDisplayScale() {
        return displayScale;
    }

    public void setDisplayScale(int displayScale) {
        this.displayScale = displayScale;
        redraw = true;
    }

    public Color getBGColor() {
        return bgColor;
    }

    public void setBGColor(Color bgColor) {
        this.bgColor = bgColor;
        redraw = true;
    }

    public void redraw() {
        this.redraw = true;
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        this.redraw = true;
    }
}

