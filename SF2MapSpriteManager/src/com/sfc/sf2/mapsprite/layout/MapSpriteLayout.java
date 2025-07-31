/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.layout;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.mapsprite.MapSprite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author wiz
 */
public class MapSpriteLayout extends JPanel {
    
    private static final int DEFAULT_SPRITES_PER_ROW = 3;   //Sprite-pairs
    
    private int spritesPerRow = DEFAULT_SPRITES_PER_ROW;
    private int displaySize = 1;
    private boolean showGrid = true;
    
    private MapSprite[] mapsprites;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public BufferedImage buildImage() {
        BufferedImage image = null;
        if (mapsprites != null && mapsprites.length > 0) {
            image = buildImage(mapsprites, spritesPerRow);
            image = resize(image);
            setSize(image.getWidth(), image.getHeight());
            if (showGrid) { drawGrid(image); }
            getParent().revalidate();
            getParent().repaint();
        }
        return image;
    }
    
    public static BufferedImage buildImage(MapSprite[] mapsprites, int spritesPerRow) {
        int imageWidth = spritesPerRow*48;
        int imageHeight = mapsprites.length/spritesPerRow*24;
        if (mapsprites.length%spritesPerRow != 0)
            imageHeight++;
        BufferedImage image;
        image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        for (int i = 0; i < mapsprites.length; i++) {
            graphics.drawImage(mapsprites[i].getIndexedColorImage(), (i%spritesPerRow)*48, i/spritesPerRow*24, null);
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
            x += 24*displaySize;
        }
        graphics.drawLine(x-1, 0, x-1, image.getHeight());
        while (y < image.getHeight()) {
            graphics.drawLine(0, y, image.getWidth(), y);
            y += 24*displaySize;
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

    public MapSprite[] getMapSprite() {
        return mapsprites;
    }

    public void setMapSprite(MapSprite[] mapsprites) {
        this.mapsprites = mapsprites;
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
        this.revalidate();
    } 

    public int getTilesPerRow() {
        return spritesPerRow;
    }

    public void setTilesPerRow(int tilesPerRow) {
        this.spritesPerRow = tilesPerRow;
        this.revalidate();
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        this.revalidate();
    }
}
