/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.vwfont.layout;

import com.sfc.sf2.vwfont.FontSymbol;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author wiz
 */
public class FontSymbolLayout extends JPanel {
    
    private static final int SYMBOL_WIDTH = FontSymbol.PIXEL_WIDTH;
    private static final int SYMBOL_HEIGHT = FontSymbol.PIXEL_HEIGHT;
    private static final int DEFAULT_TILES_PER_ROW = 8;
    
    private int tilesPerRow = DEFAULT_TILES_PER_ROW;
    private int displaySize = 1;
    private boolean showGrid = true;
    
    private FontSymbol[] symbols;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public BufferedImage buildImage() {
        BufferedImage image = null;
        if (symbols != null && symbols.length > 0) {
            image = buildImage(symbols, tilesPerRow, showGrid);
            image = resize(image);
            setSize(image.getWidth(), image.getHeight());
            if (showGrid) { drawGrid(image); }
        }
        return image;
    }
    
    public static BufferedImage buildImage(FontSymbol[] symbols, int tilesPerRow, boolean drawWidthMarker) {
        int imageWidth = tilesPerRow;
        int imageHeight = symbols.length/tilesPerRow + 1;
        BufferedImage image = new BufferedImage(imageWidth*SYMBOL_WIDTH, imageHeight*SYMBOL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        
        if (drawWidthMarker) {
            graphics.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i < symbols.length; i++) {
                int x = symbols[i].getWidth() + (i%imageWidth)*SYMBOL_WIDTH;
                int y = (i/imageWidth)*SYMBOL_HEIGHT;
                graphics.drawLine(x, y, x, y+SYMBOL_HEIGHT);
            }
        }
        
        for (int i = 0; i < symbols.length; i++) {
            graphics.drawImage(symbols[i].getIndexColoredImage(), (i%imageWidth)*SYMBOL_WIDTH, (i/imageWidth)*SYMBOL_HEIGHT, null);
        }
        graphics.dispose();
        return image;
    }
    
    private void drawGrid(BufferedImage image) {
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.BLACK);
        int x = 0;
        int y = 0;
        while (x < image.getWidth()) {
            graphics.drawLine(x, 0, x, image.getHeight());
            x += SYMBOL_WIDTH*displaySize;
        }
        graphics.drawLine(x-1, 0, x-1, image.getHeight());
        while (y < image.getHeight()) {
            graphics.drawLine(0, y, image.getWidth(), y);
            y += SYMBOL_HEIGHT*displaySize;
        }
        graphics.drawLine(0, y-1, image.getWidth(), y-1);
        graphics.dispose();
    }
    
    private BufferedImage resize(BufferedImage image) {
        if (displaySize == 1)
            return image;
        BufferedImage newImage = new BufferedImage(image.getWidth()*displaySize, image.getHeight()*displaySize, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(image, 0, 0, image.getWidth()*displaySize, image.getHeight()*displaySize, null);
        g.dispose();
        return newImage;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }

    public FontSymbol[] getFontSymbols() {
        return symbols;
    }

    public void setFontSymbols(FontSymbol[] symbols) {
        this.symbols = symbols;
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
        this.revalidate();
    } 

    public int getTilesPerRow() {
        return tilesPerRow;
    }

    public void setTilesPerRow(int tilesPerRow) {
        this.tilesPerRow = tilesPerRow;
        this.revalidate();
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        this.revalidate();
    }
}
