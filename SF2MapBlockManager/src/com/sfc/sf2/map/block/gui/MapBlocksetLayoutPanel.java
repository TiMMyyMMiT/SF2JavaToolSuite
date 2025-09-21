/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import com.sfc.sf2.graphics.Block;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.MapBlockHelpers;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 *
 * @author wiz
 */
public class MapBlocksetLayoutPanel extends AbstractLayoutPanel {
    
    private static final int DEFAULT_BLOCKS_PER_ROW = 10;
    
    public static int selectedBlockIndexLeft;
    public static int selectedBlockIndexRight;
    
    private BlockSlotPanel leftSlotBlockPanel;
    private BlockSlotPanel rightSlotBlockPanel;
    private Color leftSlotColor = Color.YELLOW;
    private Color rightSlotColor = Color.GREEN;
    
    private MapBlockset blockset;
    private Tileset[] tilesets;
    private boolean showPriority = false;

    public MapBlocksetLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH/3);
        scale = new LayoutScale(2);
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT);
        coordsGrid = new LayoutCoordsGridDisplay(PIXEL_WIDTH, PIXEL_HEIGHT, true, 0, 10, 1);
        coordsHeader = new LayoutCoordsHeader(this, PIXEL_WIDTH, PIXEL_HEIGHT, true);
        mouseInput = new LayoutMouseInput(this, this::onMousePressed, PIXEL_WIDTH, PIXEL_HEIGHT);
        scroller = new LayoutScrollNormaliser(this);
        setItemsPerRow(DEFAULT_BLOCKS_PER_ROW);
    }

    @Override
    protected boolean hasData() {
        return blockset != null && blockset.getBlocks().length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        return blockset.getDimensions(getItemsPerRow());
    }

    @Override
    protected void drawImage(Graphics graphics) {
        int blocksPerRow = getItemsPerRow();
        graphics.drawImage(blockset.getIndexedColorImage(tilesets), 0, 0, null);
        if (showPriority) {
            MapBlockHelpers.drawTilePriorities(graphics, blockset.getBlocks(), tilesets, blocksPerRow);
        }
        if (selectedBlockIndexLeft >= 0) {
            Graphics2D g2 = (Graphics2D)graphics;
            g2.setStroke(new BasicStroke(2));
            g2.setColor(leftSlotColor);
            int baseX = (selectedBlockIndexLeft%blocksPerRow)*PIXEL_WIDTH;
            int baseY = (selectedBlockIndexLeft/blocksPerRow)*PIXEL_HEIGHT;
            g2.drawRect(baseX-2, baseY-2, PIXEL_WIDTH+4, PIXEL_HEIGHT+4);
        }
        if (selectedBlockIndexRight >= 0) {
            Graphics2D g2 = (Graphics2D)graphics;
            g2.setStroke(new BasicStroke(2));
            g2.setColor(rightSlotColor);
            int baseX = (selectedBlockIndexRight%blocksPerRow)*PIXEL_WIDTH;
            int baseY = (selectedBlockIndexRight/blocksPerRow)*PIXEL_HEIGHT;
            g2.drawRect(baseX-2, baseY-2, PIXEL_WIDTH+4, PIXEL_HEIGHT+4);
        }
    }
    
    public MapBlockset getBlockset() {
        return blockset;
    }

    public void setBlockset(MapBlockset blockset) {
        this.blockset = blockset;
        selectedBlockIndexLeft = selectedBlockIndexRight = -1;
        this.redraw();
    }

    public Tileset[] getTilesets() {
        return tilesets;
    }

    public void setTilesets(Tileset[] tilesets) {
        this.tilesets = tilesets;
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
    
    public int getLeftSelectedIndex() {
        return selectedBlockIndexLeft;
    }
    
    public void setLeftSelectedIndex(int index) {
        if (leftSlotBlockPanel!=null) {
            if (index < 3 || index >= blockset.getBlocks().length) {
                selectedBlockIndexLeft = -1;
                leftSlotBlockPanel.setBlock(null);
            } else {
                selectedBlockIndexLeft = index;
                leftSlotBlockPanel.setBlock(blockset.getBlocks()[index]);
            }
            this.redraw();
        }
    }
    
    public int getRightSelectedIndex() {
        return selectedBlockIndexRight;
    }
    
    public void setRightSelectedIndex(int index) {
        if (rightSlotBlockPanel!=null) {
            if (index < 3 || index >= blockset.getBlocks().length) {
                selectedBlockIndexRight = -1;
                rightSlotBlockPanel.setBlock(null);
            } else {
                selectedBlockIndexRight = index;
                rightSlotBlockPanel.setBlock(blockset.getBlocks()[index]);
            }
            this.redraw();
        }
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

    public void setLeftSlotColor(Color leftSlotColor) {
        this.leftSlotColor = leftSlotColor;
    }

    public void setRightSlotColor(Color rightSlotColor) {
        this.rightSlotColor = rightSlotColor;
    }

    private void onMousePressed(BaseMouseCoordsComponent.GridMousePressedEvent evt) {
        int x = evt.x();
        int y = evt.y();
        int blockIndex = x+y*getItemsPerRow();
        if (blockIndex < 0 || blockIndex >= blockset.getBlocks().length) {
            return;
        }
        if (evt.mouseButton() == MouseEvent.BUTTON1) {
            if (selectedBlockIndexLeft == blockIndex) {
                setLeftSelectedIndex(-1);
            } else {
                setLeftSelectedIndex(blockIndex);
            }
            this.revalidate();
            this.repaint();
        } else if (evt.mouseButton() == MouseEvent.BUTTON3) {
            if (selectedBlockIndexRight == blockIndex) {
                setRightSelectedIndex(-1);
            } else {
                setRightSelectedIndex(blockIndex);
            }
            this.revalidate();
            this.repaint();
        }
        //System.out.println("Blockset press "+evt.mouseButton()+" "+x+" - "+y);
    }
}
