/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.layout.layout;

import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.layout.MapLayout;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JPanel;

/**
 *
 * @author TiMMy
 */
public class StaticMapLayout extends JPanel {
    
    protected static final int DEFAULT_TILES_PER_ROW = 64*3;
    
    protected int tilesPerRow = DEFAULT_TILES_PER_ROW;
    protected MapLayout layout;
    protected MapBlockset blockset;
    protected int displaySize = 1;
    protected int gridSize = 24;
    
    protected BufferedImage currentImage;
    protected boolean redraw = true;
    protected int renderCounter = 0;
    protected boolean drawExplorationFlags = true;
    protected boolean drawInteractionFlags = false;
    protected boolean showGrid = false;
    
    protected Color bgColor = null;
    protected Color bgDarkerColor = null;
    
    private BufferedImage gridImage;
    private BufferedImage obstructedImage;
    private BufferedImage leftUpstairsImage;
    private BufferedImage rightUpstairsImage;
    private BufferedImage warpImage;
    private BufferedImage triggerImage;
    private BufferedImage eventImage;
    private BufferedImage tableImage;
    private BufferedImage chestImage;
    private BufferedImage barrelImage;
    private BufferedImage vaseImage;
    private BufferedImage searchImage;
    private BufferedImage copyImage;
    private BufferedImage showImage;
    private BufferedImage hideImage;

    public StaticMapLayout() {
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public BufferedImage buildImage(){
        if (redraw) {
            if (layout == null || layout.getBlockset() == null) {
                currentImage = null;
            } else {
                currentImage = buildImage(this.layout,this.tilesPerRow);
                currentImage = resize(currentImage);
                setSize(currentImage.getWidth(), currentImage.getHeight());
                if (showGrid) { drawGrid(currentImage); }
                getParent().revalidate();
                getParent().repaint();
            }
            redraw = false;
        }
        return currentImage;
    }
    
    protected BufferedImage buildImage(MapLayout layout, int tilesPerRow) {
        renderCounter++;
        System.out.println("Map render "+renderCounter);
        this.layout = layout;
        if(redraw){
            MapBlock[] blocks = layout.getBlockset().getBlocks();
            int imageHeight = 64*3*8;
            currentImage = new BufferedImage(tilesPerRow*8, imageHeight , BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = currentImage.getGraphics();
            drawBackgroundPattern(currentImage);
            for(int y=0;y<64;y++){
                for(int x=0;x<64;x++){
                    drawBlock(blocks[y*64+x], graphics, x, y);
                }
            }
        }
        return currentImage;
    }
    
    protected void drawBlock(MapBlock block, Graphics graphics, int x, int y) {
        
        BufferedImage blockImage = block.getIndexedColorImage();
        BufferedImage explorationFlagImage = block.getExplorationFlagImage();
        BufferedImage interactionFlagImage = block.getInteractionFlagImage();
        graphics.drawImage(blockImage, x*3*8, y*3*8, null);
        if(drawExplorationFlags || drawInteractionFlags){ 
            if(drawExplorationFlags){
                int explorationFlags = block.getFlags()&0xC000;
                if(explorationFlagImage==null){
                    explorationFlagImage = new BufferedImage(3*8, 3*8, BufferedImage.TYPE_INT_ARGB);
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
                graphics.drawImage(explorationFlagImage, x*3*8, y*3*8, null); 
            }
            if(drawInteractionFlags){
                int interactionFlags = block.getFlags()&0x3C00;
                if(interactionFlagImage==null){
                    interactionFlagImage = new BufferedImage(3*8, 3*8, BufferedImage.TYPE_INT_ARGB);
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
                graphics.drawImage(interactionFlagImage, x*3*8, y*3*8, null); 
            }
        }
    }
    
    //TODO make common for all layout panels
    private void drawBackgroundPattern(BufferedImage image) {
        if (bgColor == null) {
            bgColor = getBackground();
            bgDarkerColor = bgColor.darker();
        }
        int bgInt = bgColor.getRGB();
        int darkInt = bgDarkerColor.getRGB();
        int[] data = ((DataBufferInt)(image.getRaster().getDataBuffer())).getData();
        int width = image.getWidth();
        int height = image.getHeight();
        int offset;
        for (int j = 0; j < height; j++) {
            offset = ((j/12)%2) == 0 ? 12 : 0;
            for (int i = 0; i < width; i++) {
                data[i+j*width] = ((i+j*width+offset)%24) < 12 ? bgInt : darkInt;
            }
        }
    }
    
    private void drawGrid(BufferedImage image) {
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.BLACK);
        int x = 0;
        int y = 0;
        while (x < image.getWidth()) {
            graphics.drawLine(x, 0, x, image.getHeight());
            x += gridSize*displaySize;
        }
        graphics.drawLine(x-1, 0, x-1, image.getHeight());
        while (y < image.getHeight()) {
            graphics.drawLine(0, y, image.getWidth(), y);
            y += gridSize*displaySize;
        }
        graphics.drawLine(0, y-1, image.getWidth(), y-1);
        graphics.dispose();
    }
    
    private BufferedImage getObstructedImage(){
        if(obstructedImage==null){
            obstructedImage = new BufferedImage(3*8, 3*8, BufferedImage.TYPE_INT_ARGB);
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
    
    private BufferedImage getLeftUpstairsImage(){
        if(leftUpstairsImage==null){
            leftUpstairsImage = new BufferedImage(3*8, 3*8, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) leftUpstairsImage.getGraphics();  
            g2.setColor(Color.CYAN);
            g2.setStroke(new BasicStroke(3));
            Line2D line1 = new Line2D.Double(3, 3, 21, 21);
            g2.draw(line1);
            g2.dispose();
        }
        return leftUpstairsImage;
    }
    
    private BufferedImage getRightUpstairsImage(){
        if(rightUpstairsImage==null){
            rightUpstairsImage = new BufferedImage(3*8, 3*8, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) rightUpstairsImage.getGraphics();  
            g2.setColor(Color.CYAN);
            g2.setStroke(new BasicStroke(3));
            Line2D line1 = new Line2D.Double(3, 21, 21, 3);
            g2.draw(line1);
            g2.dispose();
        }
        return rightUpstairsImage;
    }     
    
    private BufferedImage getChestImage(){
        if(chestImage==null){
            chestImage = new BufferedImage(3*8, 3*8, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) chestImage.getGraphics();  
            g2.setColor(Color.ORANGE);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(1, 1, 21, 21);
            g2.dispose();
        }
        return chestImage;
    } 
    
    private BufferedImage getTableImage(){
        if(tableImage==null){
            tableImage = new BufferedImage(3*8, 3*8, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) tableImage.getGraphics();  
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(8, 8, 16, 8);
            g2.drawLine(12, 8, 12, 16);
            g2.dispose();
        }
        return tableImage;
    } 
    
    private BufferedImage getBarrelImage(){
        if(barrelImage==null){
            barrelImage = new BufferedImage(3*8, 3*8, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) barrelImage.getGraphics();  
            g2.setColor(Color.ORANGE);
            g2.setStroke(new BasicStroke(3));
            //g2.drawRoundRect(6, 4, 12, 16, 8, 8);
            g2.drawOval(6, 4, 12, 16);
            g2.dispose();
        }
        return barrelImage;
    } 
    
    private BufferedImage getVaseImage(){
        if(vaseImage==null){
            vaseImage = new BufferedImage(3*8, 3*8, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) vaseImage.getGraphics(); 
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            //g2.drawRoundRect(6, 4, 12, 16, 8, 8);
            g2.drawOval(6, 4, 12, 16);
            g2.dispose();
        }
        return vaseImage;
    }
    
    private BufferedImage getWarpImage(){
        if(warpImage==null){
            warpImage = new BufferedImage(3*8, 3*8, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) warpImage.getGraphics();
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(0, 0, 24, 24);
            g2.dispose();
        }
        return warpImage;
    }
        
    private BufferedImage getTriggerImage(){
        if(triggerImage==null){
            triggerImage = new BufferedImage(3*8*64, 3*8*64, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) triggerImage.getGraphics();
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.GREEN);
            g2.drawRect(0,0, 24, 24);
            g2.dispose();
        }
        return triggerImage;
    }
    
    private BufferedImage getEventImage(){
        if(eventImage==null){
            eventImage = new BufferedImage(3*8, 3*8, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) eventImage.getGraphics();  
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(1, 1, 21, 21);
        }
        return eventImage;
    }
    
    private BufferedImage getSearchImage(){
        if(rightUpstairsImage==null){
            rightUpstairsImage = new BufferedImage(3*8, 3*8, BufferedImage.TYPE_INT_ARGB);
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
    
    private BufferedImage getCopyImage(){
        if(rightUpstairsImage==null){
            rightUpstairsImage = new BufferedImage(3*8, 3*8, BufferedImage.TYPE_INT_ARGB);
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
    
    private BufferedImage getShowImage(){
        if(rightUpstairsImage==null){
            rightUpstairsImage = new BufferedImage(3*8, 3*8, BufferedImage.TYPE_INT_ARGB);
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
    
    private BufferedImage getHideImage(){
        if(rightUpstairsImage==null){
            rightUpstairsImage = new BufferedImage(3*8, 3*8, BufferedImage.TYPE_INT_ARGB);
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
    
    public void resize(int size){
        this.displaySize = size;
        currentImage = resize(currentImage);
    }
    
    private BufferedImage resize(BufferedImage image){
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
    
    public int getTilesPerRow() {
        return tilesPerRow;
    }

    public void setTilesPerRow(int tilesPerRow) {
        this.tilesPerRow = tilesPerRow;
    }

    public int getCurrentDisplaySize() {
        return displaySize;
    }

    public void setCurrentDisplaySize(int currentDisplaySize) {
        this.displaySize = currentDisplaySize;
        redraw = true;
    }

    public MapLayout getMapLayout() {
        return layout;
    }

    public void setMapLayout(MapLayout layout) {
        this.layout = layout;
    }

    public MapBlockset getBlockset() {
        return blockset;
    }

    public void setBlockset(MapBlockset blockset) {
        this.blockset = blockset;
    }

    public boolean isDrawExplorationFlags() {
        return drawExplorationFlags;
    }

    public void setDrawExplorationFlags(boolean drawExplorationFlags) {
        this.drawExplorationFlags = drawExplorationFlags;
        this.redraw = true;
    }
    public boolean isDrawInteractionFlags() {
        return drawInteractionFlags;
    }

    public void setDrawInteractionFlags(boolean drawInteractionFlags) {
        this.drawInteractionFlags = drawInteractionFlags;
        this.redraw = true;
    }

    public Color getBGColor() {
        return bgColor;
    }

    public void setBGColor(Color bgColor) {
        this.bgColor = bgColor;
        this.bgDarkerColor = bgColor.darker();
        redraw = true;
    }

    public boolean isRedraw() {
        return redraw;
    }

    public void setRedraw(boolean redraw) {
        this.redraw = redraw;
    }

    public boolean isDrawGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        this.redraw = true;
    }
}

