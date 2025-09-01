/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.layout.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.layout.MapLayout;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_HEIGHT;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_WIDTH;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author TiMMy
 */
public class StaticMapLayoutPanel extends AbstractLayoutPanel {
    
    protected static final int DEFAULT_BLOCKS_PER_ROW = MapLayout.BLOCK_WIDTH;
    protected static final Dimension mapDimensions = new Dimension(BLOCK_WIDTH*PIXEL_WIDTH, BLOCK_HEIGHT*PIXEL_HEIGHT);
    
    //Various images for map flags
    private static BufferedImage gridImage;
    private static BufferedImage obstructedImage;
    private static BufferedImage leftUpstairsImage;
    private static BufferedImage rightUpstairsImage;
    private static BufferedImage warpImage;
    private static BufferedImage triggerImage;
    private static BufferedImage eventImage;
    private static BufferedImage tableImage;
    private static BufferedImage chestImage;
    private static BufferedImage barrelImage;
    private static BufferedImage vaseImage;
    private static BufferedImage searchImage;
    private static BufferedImage copyImage;
    private static BufferedImage showImage;
    private static BufferedImage hideImage;
    
    protected MapLayout layout;
    
    private boolean drawExplorationFlags = true;
    private boolean drawInteractionFlags = false;

    public StaticMapLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH/3);
        scale = new LayoutScale(1);
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT);
        coordsGrid = new LayoutCoordsGridDisplay(PIXEL_WIDTH, PIXEL_HEIGHT, false, 0, 0, 1);
        coordsHeader = new LayoutCoordsHeader(this, PIXEL_WIDTH, PIXEL_HEIGHT, false);
        mouseInput = null;
        setItemsPerRow(DEFAULT_BLOCKS_PER_ROW);
    }

    @Override
    protected boolean hasData() {
        return layout != null && layout.getBlockset() != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        return mapDimensions;
    }

    @Override
    protected void drawImage(Graphics graphics) {
        MapBlock[] blocks = layout.getBlockset().getBlocks();
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                drawBlock(blocks[y * BLOCK_WIDTH + x], graphics, x, y);
            }
        }
    }
    
    protected void drawBlock(MapBlock block, Graphics graphics, int x, int y) {
        
        BufferedImage blockImage = block.getIndexedColorImage();
        BufferedImage explorationFlagImage = block.getExplorationFlagImage();
        BufferedImage interactionFlagImage = block.getInteractionFlagImage();
        graphics.drawImage(blockImage, x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
        if (drawExplorationFlags || drawInteractionFlags) { 
            if (drawExplorationFlags){
                int explorationFlags = block.getFlags()&0xC000;
                if (explorationFlagImage==null) {
                    explorationFlagImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = (Graphics2D) explorationFlagImage.getGraphics();
                    switch (explorationFlags) {
                        case 0xC000:
                            g2.drawImage(getObstructedImage(), 0, 0, null);
                            break;
                        case 0x8000:
                            g2.drawImage(getRightUpstairsImage(), 0, 0, null);
                            break;
                        case 0x4000:
                            g2.drawImage(getLeftUpstairsImage(), 0, 0, null);
                            break;
                        default:
                            break;
                    }
                    block.setExplorationFlagImage(explorationFlagImage);
                }
                graphics.drawImage(explorationFlagImage, x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null); 
            }
            if (drawInteractionFlags) {
                int interactionFlags = block.getFlags()&0x3C00;
                if (interactionFlagImage==null) {
                    interactionFlagImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = (Graphics2D) interactionFlagImage.getGraphics();
                    switch (interactionFlags) {
                        case 0x1800:
                            g2.drawImage(getChestImage(), 0, 0, null);
                            break;
                        case 0x1000:
                            g2.drawImage(getWarpImage(), 0, 0, null);
                            break;
                        case 0x1400:
                            g2.drawImage(getEventImage(), 0, 0, null);
                            break;
                        case 0x2800:
                            g2.drawImage(getTableImage(), 0, 0, null);
                            break;
                        case 0x3000:
                            g2.drawImage(getBarrelImage(), 0, 0, null);
                            break;
                        default:
                            break;
                    }
                    block.setInteractionFlagImage(interactionFlagImage);
                }
                graphics.drawImage(interactionFlagImage, x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null); 
            }
        }
    }
    
    private BufferedImage getObstructedImage() {
        if (obstructedImage==null) {
            obstructedImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) obstructedImage.getGraphics();  
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(3));
            Line2D line1 = new Line2D.Double(6, 6, 18, 18);
            g2.draw(line1);
            Line2D line2 = new Line2D.Double(6, 18, 18, 6);
            g2.draw(line2);  
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2));
            line1 = new Line2D.Double(6, 6, 18, 18);
            g2.draw(line1);
            line2 = new Line2D.Double(6, 18, 18, 6);
            g2.draw(line2);
            g2.dispose();
        }
        return obstructedImage;
    }
    
    private BufferedImage getLeftUpstairsImage() {
        if (leftUpstairsImage==null) {
            leftUpstairsImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) leftUpstairsImage.getGraphics();  
            g2.setColor(Color.CYAN);
            g2.setStroke(new BasicStroke(3));
            Line2D line1 = new Line2D.Double(3, 3, 21, 21);
            g2.draw(line1);
            g2.dispose();
        }
        return leftUpstairsImage;
    }
    
    private BufferedImage getRightUpstairsImage() {
        if (rightUpstairsImage==null) {
            rightUpstairsImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) rightUpstairsImage.getGraphics();  
            g2.setColor(Color.CYAN);
            g2.setStroke(new BasicStroke(3));
            Line2D line1 = new Line2D.Double(3, 21, 21, 3);
            g2.draw(line1);
            g2.dispose();
        }
        return rightUpstairsImage;
    }     
    
    private BufferedImage getChestImage() {
        if (chestImage==null) {
            chestImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) chestImage.getGraphics();  
            g2.setColor(Color.ORANGE);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(1, 1, 21, 21);
            g2.dispose();
        }
        return chestImage;
    } 
    
    private BufferedImage getTableImage() {
        if(tableImage==null){
            tableImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) tableImage.getGraphics();  
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(8, 8, 16, 8);
            g2.drawLine(12, 8, 12, 16);
            g2.dispose();
        }
        return tableImage;
    } 
    
    private BufferedImage getBarrelImage() {
        if (barrelImage==null) {
            barrelImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) barrelImage.getGraphics();  
            g2.setColor(Color.ORANGE);
            g2.setStroke(new BasicStroke(3));
            //g2.drawRoundRect(6, 4, 12, 16, 8, 8);
            g2.drawOval(6, 4, 12, 16);
            g2.dispose();
        }
        return barrelImage;
    } 
    
    private BufferedImage getVaseImage() {
        if (vaseImage==null) {
            vaseImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) vaseImage.getGraphics(); 
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            //g2.drawRoundRect(6, 4, 12, 16, 8, 8);
            g2.drawOval(6, 4, 12, 16);
            g2.dispose();
        }
        return vaseImage;
    }
    
    private BufferedImage getWarpImage() {
        if (warpImage==null) {
            warpImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) warpImage.getGraphics();
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(0, 0, 24, 24);
            g2.dispose();
        }
        return warpImage;
    }
        
    private BufferedImage getTriggerImage() {
        if (triggerImage==null) {
            triggerImage = new BufferedImage(3*8*64, 3*8*64, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) triggerImage.getGraphics();
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.GREEN);
            g2.drawRect(0,0, 24, 24);
            g2.dispose();
        }
        return triggerImage;
    }
    
    private BufferedImage getEventImage() {
        if (eventImage==null) {
            eventImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) eventImage.getGraphics();  
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(1, 1, 21, 21);
        }
        return eventImage;
    }
    
    private BufferedImage getSearchImage() {
        if (rightUpstairsImage==null) {
            rightUpstairsImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) rightUpstairsImage.getGraphics();  
            g2.setColor(Color.BLUE);
            Line2D line0 = new Line2D.Double(3-1, 21, 21-1, 3);
            Line2D line1 = new Line2D.Double(3, 21, 21, 3);
            Line2D line2 = new Line2D.Double(3+1, 21, 21+1, 3);
            g2.draw(line0);
            g2.draw(line1);
            g2.draw(line2);
        }
        return rightUpstairsImage;
    } 
    
    private BufferedImage getCopyImage() {
        if (rightUpstairsImage==null) {
            rightUpstairsImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) rightUpstairsImage.getGraphics();  
            g2.setColor(Color.BLUE);
            Line2D line0 = new Line2D.Double(3-1, 21, 21-1, 3);
            Line2D line1 = new Line2D.Double(3, 21, 21, 3);
            Line2D line2 = new Line2D.Double(3+1, 21, 21+1, 3);
            g2.draw(line0);
            g2.draw(line1);
            g2.draw(line2);
        }
        return rightUpstairsImage;
    } 
    
    private BufferedImage getShowImage() {
        if (rightUpstairsImage==null) {
            rightUpstairsImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) rightUpstairsImage.getGraphics();  
            g2.setColor(Color.BLUE);
            Line2D line0 = new Line2D.Double(3-1, 21, 21-1, 3);
            Line2D line1 = new Line2D.Double(3, 21, 21, 3);
            Line2D line2 = new Line2D.Double(3+1, 21, 21+1, 3);
            g2.draw(line0);
            g2.draw(line1);
            g2.draw(line2);
        }
        return rightUpstairsImage;
    } 
    
    private BufferedImage getHideImage() {
        if (rightUpstairsImage==null) {
            rightUpstairsImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) rightUpstairsImage.getGraphics();  
            g2.setColor(Color.BLUE);
            Line2D line0 = new Line2D.Double(3-1, 21, 21-1, 3);
            Line2D line1 = new Line2D.Double(3, 21, 21, 3);
            Line2D line2 = new Line2D.Double(3+1, 21, 21+1, 3);
            g2.draw(line0);
            g2.draw(line1);
            g2.draw(line2);
        }
        return rightUpstairsImage;
    }

    public MapLayout getMapLayout() {
        return layout;
    }

    public void setMapLayout(MapLayout layout) {
        this.layout = layout;
    }

    public boolean isDrawExplorationFlags() {
        return drawExplorationFlags;
    }

    public void setDrawExplorationFlags(boolean drawExplorationFlags) {
        this.drawExplorationFlags = drawExplorationFlags;
        redraw();
    }
    public boolean isDrawInteractionFlags() {
        return drawInteractionFlags;
    }

    public void setDrawInteractionFlags(boolean drawInteractionFlags) {
        this.drawInteractionFlags = drawInteractionFlags;
        redraw();
    }
}
