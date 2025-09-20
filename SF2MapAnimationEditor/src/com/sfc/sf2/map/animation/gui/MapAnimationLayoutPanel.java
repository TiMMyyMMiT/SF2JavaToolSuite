/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.animation.gui;

import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.layout.gui.StaticMapLayoutPanel;
import java.awt.Graphics;

/**
 *
 * @author wiz
 */
public class MapAnimationLayoutPanel extends StaticMapLayoutPanel {
    
    private MapAnimation animation;
    private int selectedAnimFrame = 0;

    @Override
    protected void drawImage(Graphics graphics) {
        super.drawImage(graphics);
    }

    /*public BufferedImage buildImage(Map map, int tilesPerRow, boolean pngExport){
    renderCounter++;
    System.out.println("Map render "+renderCounter);
    this.map = map;
    this.layout = map.getLayout();
    if(redraw){
    MapBlock[] blocks = this.map.getAnimation().getFrames()[this.selectedAnimFrame].getBlocks();
    int imageHeight = 64*3*8;
    currentImage = new BufferedImage(tilesPerRow*8, imageHeight , BufferedImage.TYPE_INT_ARGB);
    Graphics graphics = currentImage.getGraphics();
    for(int y=0;y<64;y++){
    for(int x=0;x<64;x++){
    MapBlock block = blocks[y*64+x];
    graphics.drawImage(block.getIndexedColorImage(), x*3*8, y*3*8, null);
    }
    }
    if(drawGrid){
    graphics.drawImage(getGridImage(), 0, 0, null);
    }
    if(drawTriggers){
    graphics.drawImage(getTriggersImage(),0,0,null);
    }
    redraw = false;
    currentImage = resize(currentImage);
    }
    return currentImage;
    }*/
    
    public MapAnimation getAnimation() {
        return animation;
    }
    
    public void setAnimation(MapAnimation animation) {
        this.animation = animation;
        this.redraw();
    }

    public int getSelectedAnimFrame() {
        return selectedAnimFrame;
    }

    public void setSelectedAnimFrame(int selectedAnimFrame) {
        this.selectedAnimFrame = selectedAnimFrame;
        this.redraw();
    }
}
