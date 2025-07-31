/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.weaponsprite.layout;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.weaponsprite.WeaponSprite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author wiz
 */
public class WeaponSpriteLayout extends JPanel {
    
    private static final int TILES_PER_ROW = 8;
    private WeaponSprite weaponsprite;
    private Palette palette;
    
    private int displaySize = 1;
    private boolean showGrid = false;
    
    BufferedImage currentImage;
    private boolean redraw = true;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public BufferedImage buildImage(){
        if(redraw) {
            currentImage = buildImage(this.weaponsprite, this.palette, TILES_PER_ROW);
            currentImage = resize(currentImage);
            setSize(currentImage.getWidth(), currentImage.getHeight());
            if (showGrid) { drawGrid(currentImage); }
        }
        return currentImage;
    }
    
    public static BufferedImage buildImage(WeaponSprite weaponsprite, Palette palette, int tilesPerRow) {
                
        int frames = weaponsprite.getFrames().length;
        int imageWidth = tilesPerRow*8;
        int imageHeight = frames*8*8;
        BufferedImage image;
        image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        for(int f = 0; f < frames; f++) {
            Tile[] frameTiles = weaponsprite.getFrames()[f];
            for(int t = 0; t < frameTiles.length; t++) {
                int x = (t%tilesPerRow)*8;
                int y = (t/tilesPerRow)*8 + f*8*8;
                frameTiles[t].setPalette(palette);
                frameTiles[t].clearIndexedColorImage();
                graphics.drawImage(frameTiles[t].getIndexedColorImage(), x, y, null);
            }
        }
        graphics.dispose();
        return image;
    }
    
    private void drawGrid(BufferedImage image) {
        Graphics2D g2 = (Graphics2D)image.getGraphics();
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        int x = 0;
        int y = 0;
        while (x < image.getWidth()) {
            g2.drawLine(x, 0, x, image.getHeight());
            x += 8*displaySize;
        }
        g2.drawLine(x-1, 0, x-1, image.getHeight());
        while (y < image.getHeight()) {
            g2.drawLine(0, y, image.getWidth(), y);
            y += 8*displaySize;
        }
        g2.setStroke(new BasicStroke(3));
        y = 0;
        while (y <= image.getHeight()) {
            g2.drawLine(0, y-1, image.getWidth(), y-1);
            y += 8*8*displaySize;
        }
        g2.dispose();
    }
    
    public void resize(int size) {
        this.displaySize = size;
        currentImage = resize(currentImage);
        this.redraw = true;
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
    
    public WeaponSprite getWeaponSprite() {
        return weaponsprite;
    }

    public void setWeaponSprite(WeaponSprite weaponsprite) {
        this.weaponsprite = weaponsprite;
        this.redraw = true;
    }

    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
        this.redraw = true;
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
