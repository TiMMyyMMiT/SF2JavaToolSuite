/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.background.layout;

import com.sfc.sf2.background.Background;
import com.sfc.sf2.graphics.Tile;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author TiMMy
 */
public class BackgroundLayout extends JPanel {
    
    private static final int BACKGROUND_HEIGHT = 12;
    private static final int TILES_PER_ROW = 32;
    
    private Background[] backgrounds;
    
    private BufferedImage currentImage;
    private boolean redraw = true;
    
    private int displaySize = 1;
    private boolean showGrid = true;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public BufferedImage buildImage() {
        if (redraw) {
            if (backgrounds == null || backgrounds.length == 0) {
                currentImage = null;
            } else {
                currentImage = buildImage(backgrounds);
                currentImage = resize(currentImage);
                setSize(currentImage.getWidth(), currentImage.getHeight());
                if (showGrid) { drawGrid(currentImage); }
            }
        }
        return currentImage;
    }
    
    public static BufferedImage buildImage(Background[] backgrounds) {
        int count = backgrounds.length;
        int imageWidth = TILES_PER_ROW*8;
        int imageHeight = count*BACKGROUND_HEIGHT*8;
        BufferedImage image;
        image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        for(int b = 0; b < count; b++) {
            Tile[] tiles = backgrounds[b].getTiles();
            for(int t = 0; t < tiles.length; t++) {
                int x = (t%TILES_PER_ROW)*8;
                int y = (b*BACKGROUND_HEIGHT + t/TILES_PER_ROW)*8;
                graphics.drawImage(tiles[t].getIndexedColorImage(), x, y, null);
            }
        }
        graphics.dispose();
        return image;
    }
    
    private void drawGrid(BufferedImage image) {
        int height = BACKGROUND_HEIGHT*8;
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(1));
        int x = 0;
        int y = 0;
        while (x < image.getWidth()) {
            graphics.drawLine(x, 0, x, image.getHeight());
            x += 8*displaySize;
        }
        graphics.drawLine(x-1, 0, x-1, image.getHeight());
        while (y < image.getHeight()) {
            graphics.setStroke(new BasicStroke((y % (height*displaySize) == 0) ? 3 : 1));
            graphics.drawLine(0, y, image.getWidth(), y);
            y += 8*displaySize;
        }
        graphics.setStroke(new BasicStroke(3));
        graphics.drawLine(0, y-1, image.getWidth(), y-1);
        graphics.dispose();
    }
    
    public void resize(int size){
        this.displaySize = size;
        currentImage = resize(currentImage);
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
    
    public Background[] getBackgrounds() {
        return backgrounds;
    }

    public void setBackgrounds(Background[] backgrounds) {
        this.backgrounds = backgrounds;
        redraw = true;
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
        redraw = true;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        redraw = true;
    }
}
