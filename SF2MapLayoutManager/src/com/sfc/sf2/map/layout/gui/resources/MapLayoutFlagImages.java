/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.layout.gui.resources;

import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author TiMMy
 */
public class MapLayoutFlagImages {
    //Various images for map flags
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
    
    public static BufferedImage getBlockExplorationFlagImage(int explorationFlags) {
        switch (explorationFlags) {
            case 0xC000:
                return getObstructedImage();
            case 0x8000:
                return getRightUpstairsImage();
            case 0x4000:
                return getLeftUpstairsImage();
            default:
                return null;
        }
    }
    
    public static BufferedImage getBlockInteractionFlagImage(int interactionFlags) {
        switch (interactionFlags) {
            case 0x1800:
                return getChestImage();
            case 0x1000:
                return getWarpImage();
            case 0x1400:
                return getEventImage();
            case 0x2800:
                return getTableImage();
            case 0x3000:
                return getBarrelImage();
            default:
                return null;
        }
    }
    
    public static BufferedImage getObstructedImage() {
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
    
    public static BufferedImage getLeftUpstairsImage() {
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
    
    public static BufferedImage getRightUpstairsImage() {
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
    
    public static BufferedImage getChestImage() {
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
    
    public static BufferedImage getTableImage() {
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
    
    public static BufferedImage getBarrelImage() {
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
    
    public static BufferedImage getVaseImage() {
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
    
    public static BufferedImage getWarpImage() {
        if (warpImage==null) {
            warpImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) warpImage.getGraphics();
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(0, 0, 23, 23);
            g2.dispose();
        }
        return warpImage;
    }
        
    public static BufferedImage getTriggerImage() {
        if (triggerImage==null) {
            triggerImage = new BufferedImage(3*8*64, 3*8*64, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) triggerImage.getGraphics();
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.GREEN);
            g2.drawRect(0,0, 23, 23);
            g2.dispose();
        }
        return triggerImage;
    }
    
    public static BufferedImage getEventImage() {
        if (eventImage==null) {
            eventImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) eventImage.getGraphics();  
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(1, 1, 21, 21);
        }
        return eventImage;
    }
    
    public static BufferedImage getSearchImage() {
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
    
    public static BufferedImage getCopyImage() {
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
    
    public static BufferedImage getShowImage() {
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
    
    public static BufferedImage getHideImage() {
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
}
