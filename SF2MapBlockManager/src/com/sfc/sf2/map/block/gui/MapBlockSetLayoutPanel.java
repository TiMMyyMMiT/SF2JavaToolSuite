/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.graphics.Block;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author wiz
 */
public class MapBlockSetLayoutPanel extends AbstractLayoutPanel implements MouseListener, MouseMotionListener {
    
    private static final int DEFAULT_BLOCKS_PER_ROW = 8;
    
    public static int selectedBlockIndex0;
    public static int selectedBlockIndex1;
    
    private BlockSlotPanel leftSlotBlockPanel;
    private BlockSlotPanel rightSlotBlockPanel;
    
    private MapBlockset blockset;
    private boolean showPriority = false;

    public MapBlockSetLayoutPanel() {
        super();
        tilesPerRow = DEFAULT_BLOCKS_PER_ROW;
        setGridDimensions(MapBlock.PIXEL_WIDTH, MapBlock.PIXEL_HEIGHT);
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected boolean hasData() {
        return blockset != null && blockset.getBlocks().length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        return blockset.getDimensions(getBlocksPerRow());
    }

    @Override
    protected void buildImage(Graphics graphics) {
        graphics.drawImage(blockset.getIndexedColorImage(), 0, 0, null);
        if (showPriority) {
            MapBlock[] blocks = blockset.getBlocks();
            for (int i=0; i < blocks.length; i++) {
                int baseX = (i%tilesPerRow)*PIXEL_WIDTH;
                int baseY = (i/tilesPerRow)*PIXEL_HEIGHT;
                Tile[] tiles = blocks[i].getTiles();
                for (int t = 0; t < tiles.length; t++) {
                    if (tiles[t].isHighPriority()) {
                        graphics.setColor(Color.BLACK);
                        graphics.fillRect(baseX+(t%Block.TILE_WIDTH)*8+2, baseY+(t/Block.TILE_WIDTH)*8+2, 4, 4);
                        graphics.setColor(Color.YELLOW);
                        graphics.fillRect(baseX+(t%Block.TILE_WIDTH)*8+3, baseY+(t/Block.TILE_WIDTH)*8+3, 2, 2);
                    }
                }
            }
        }
    }

    public MapBlockset getBlockSet() {
        return blockset;
    }

    public void setBlockSet(MapBlockset blockset) {
        this.blockset = blockset;
        this.redraw();
    }
    
    public int getBlocksPerRow() {
        return tilesPerRow;
    }

    public void setBlocksPerRow(int blocksPerRow) {
        setTilesPerRow(blocksPerRow);
    }
    
    public boolean getShowPriority() {
        return showPriority;
    }

    public void setShowPriority(boolean showPriority) {
        this.showPriority = showPriority;
        this.redraw();
    }

    public BlockSlotPanel getLeftSlotBlockPanel() {
        return leftSlotBlockPanel;
    }

    public void setLeftSlotBlockPanel(BlockSlotPanel leftSlotBlockPanel) {
        this.leftSlotBlockPanel = leftSlotBlockPanel;
    }

    public BlockSlotPanel getRightSlotBlockPanel() {
        return rightSlotBlockPanel;
    }

    public void setRightSlotBlockPanel(BlockSlotPanel rightSlotBlockPanel) {
        this.rightSlotBlockPanel = rightSlotBlockPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX() / (getDisplayScale()*PIXEL_WIDTH);
        int y = e.getY() / (getDisplayScale()*PIXEL_HEIGHT);
        int blockIndex = x+y*getBlocksPerRow();
        if(e.getButton()==MouseEvent.BUTTON1){
            MapBlockSetLayoutPanel.selectedBlockIndex0 = blockIndex;
            if(leftSlotBlockPanel!=null){
                leftSlotBlockPanel.setBlock(blockset.getBlocks()[blockIndex]);
                leftSlotBlockPanel.revalidate();
                leftSlotBlockPanel.repaint(); 
            }
        }else if(e.getButton()==MouseEvent.BUTTON3){
            MapBlockSetLayoutPanel.selectedBlockIndex1 = blockIndex;
            if(rightSlotBlockPanel!=null){
                rightSlotBlockPanel.setBlock(blockset.getBlocks()[blockIndex]);
                rightSlotBlockPanel.revalidate();
                rightSlotBlockPanel.repaint();
            }
        }
        //System.out.println("Blockset press "+e.getButton()+" "+x+" - "+y);
    }    

    @Override
    public void mouseReleased(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
    @Override
    public void mouseDragged(MouseEvent e) { }
    @Override
    public void mouseMoved(MouseEvent e) { }
}
