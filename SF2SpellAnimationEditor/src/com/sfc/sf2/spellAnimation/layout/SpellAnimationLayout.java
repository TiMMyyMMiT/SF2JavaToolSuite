/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellAnimation.layout;

import com.sfc.sf2.background.Background;
import com.sfc.sf2.background.layout.BackgroundLayout;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.spellAnimation.SpellAnimation;
import com.sfc.sf2.ground.Ground;
import com.sfc.sf2.ground.layout.GroundLayout;
import com.sfc.sf2.spellAnimation.SpellAnimationFrame;
import com.sfc.sf2.spellAnimation.SpellSubAnimation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author TiMMy
 */
public class SpellAnimationLayout extends JPanel {
        
    private static final int SPELL_BASE_X = 136;
    private static final int SPELL_BASE_Y = 100;
    private static final int SPELL_MIRROR_X = 50;
    private static final int SPELL_MIRROR_Y = 75;
    private static final int BACKGROUND_BASE_X = 0;
    private static final int BACKGROUND_BASE_Y = 56;
    private static final int GROUND_BASE_X = 136;
    private static final int GROUND_BASE_Y = 140;
    
    private int displaySize = 1;
    private boolean showGrid = false;
    
    private Background background;
    private Ground ground;
    private SpellAnimation spellAnimation;
    private SpellSubAnimation subAnimation;
    
    private BufferedImage spellAnimationFrameImage = null;
    private BufferedImage backgroundImage = null;
    private BufferedImage groundImage = null;
    
    private int currentSubAnimation = 0;
    private int currentAnimationFrame = 0;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public BufferedImage buildImage() {
        BufferedImage image = buildImage(false, false);
        setSize(image.getWidth(), image.getHeight());
        image = resize(image);
        if (showGrid) { drawGrid(image); }
        return image;
    }
    
    public BufferedImage buildImage(boolean pngExport, boolean spellAnimationOnly) {
        
        BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        
        if (!pngExport && background != null)
            g.drawImage(backgroundImage, BACKGROUND_BASE_X, BACKGROUND_BASE_Y, null);
        if (!pngExport && ground != null)
            g.drawImage(groundImage, GROUND_BASE_X, GROUND_BASE_Y, null);
        BufferedImage frameImage = drawAnimationFrame(currentAnimationFrame);
        if (frameImage != null) {
            if (false) {   //Mirror
                g.drawImage(frameImage, SPELL_BASE_X, SPELL_BASE_Y, null);
            } else {
                g.drawImage(frameImage, SPELL_MIRROR_X, SPELL_MIRROR_Y, null);
            }
        }
        return resize(image);
    }
    
    private BufferedImage drawAnimationFrame(int index) {
        if (subAnimation == null || index < 0 || index >= subAnimation.getFrames().length)
            return null;
        if (spellAnimationFrameImage != null)
            return spellAnimationFrameImage;
        System.out.println("Animation: " + subAnimation.getName() + ". Frame: " + index);
        BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        
        for (int f = 0; f < subAnimation.getFrames().length; f++) {
            
        SpellAnimationFrame frame = subAnimation.getFrames()[index];
        
        int startIndex = frame.getTileIndex();
        if (startIndex >= 0 && startIndex < spellAnimation.getSpellGraphic().getTiles().length) {
            int x = frame.getX();
            int y = frame.getY();
            for (int j = 0; j < frame.getH(); j++) {
                for (int i = 0; i < frame.getW(); i++) {
                    Tile tile = spellAnimation.getSpellGraphic().getTiles()[startIndex + i + j * 8];
                    g.drawImage(tile.getIndexedColorImage(), x + i*8, y + j*8, null);
                }
            }
        }
        }
        
        g.dispose();
        spellAnimationFrameImage = image;
        return image;
    }
    
    private void drawGrid(BufferedImage image) {
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.BLACK);
        int x = 0;
        int y = 0;
        while (x < image.getWidth()) {
            graphics.drawLine(x, 0, x, image.getHeight());
            x += 8*displaySize;
        }
        graphics.drawLine(x-1, 0, x-1, image.getHeight());
        while (y < image.getHeight()) {
            graphics.drawLine(0, y, image.getWidth(), y);
            y += 8*displaySize;
        }
        graphics.drawLine(0, y-1, image.getWidth(), y-1);
        graphics.dispose();
    }
    
    private static BufferedImage flipH(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(image, image.getWidth(), 0, -image.getWidth(), image.getHeight(), null);
        g.dispose();
        return newImage;
    }

    private static BufferedImage flipV(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(image, 0, image.getHeight(), image.getWidth(), -image.getHeight(), null);
        g.dispose();
        return newImage;
    }
    
    private BufferedImage resize(BufferedImage image) {
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
    
    public void repaintAnim() {
        this.revalidate();
        this.repaint();
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
        repaintAnim();
    } 

    public void setBackground(Background background) {
        this.background = background;
        if (this.background != null) {
            BackgroundLayout backgroundLayout = new BackgroundLayout();
            backgroundLayout.setTiles(background.getTiles());
            backgroundImage = backgroundLayout.buildImage();        
        }
        backgroundImage = null;
        repaintAnim();
    }

    public void setGround(Ground ground) {
        this.ground = ground;
        if (ground != null) {
            GroundLayout groundLayout = new GroundLayout();
            groundLayout.setTiles(ground.getTiles());        
            groundImage = groundLayout.buildImage();
        }
        groundImage = null;
        repaintAnim();
    }

    public void setSpellAnimation(SpellAnimation spellAnimation) {
        this.spellAnimation = spellAnimation;
        spellAnimationFrameImage = null;
        currentSubAnimation = 0;
        currentAnimationFrame = 0;
    }

    public int getSubAnimationIndex() {
        return currentSubAnimation;
    }

    public void setSubAnimationIndex(int subAnimationIndex) {
        this.currentSubAnimation = subAnimationIndex;
        if (spellAnimation == null || subAnimationIndex >= spellAnimation.getSpellSubAnimations().length) {
            subAnimation = null;
        } else {
            subAnimation = spellAnimation.getSpellSubAnimations()[subAnimationIndex];
        }
        spellAnimationFrameImage = null;
        repaintAnim();
    }

    public int getCurrentAnimationFrame() {
        return currentAnimationFrame;
    }

    public void setCurrentAnimationFrame(int currentAnimationFrame) {
        this.currentAnimationFrame = currentAnimationFrame;
        spellAnimationFrameImage = null;
        repaintAnim();
    }

    public int getCurrentDisplaySize() {
        return displaySize;
    }

    public void setCurrentDisplaySize(int displaySize) {
        this.displaySize = displaySize;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        repaintAnim();
    }
    
    public void updateDisplayProperties() {
        
    }
}
