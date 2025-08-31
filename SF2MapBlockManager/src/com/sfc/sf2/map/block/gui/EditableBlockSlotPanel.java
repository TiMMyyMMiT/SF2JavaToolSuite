/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.gui;

import com.sfc.sf2.core.gui.layout.BaseMouseCoordsComponent.GridMousePressedEvent;
import com.sfc.sf2.core.gui.layout.LayoutMouseInput;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import com.sfc.sf2.map.block.MapBlock;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 *
 * @author TiMMy
 */
public class EditableBlockSlotPanel extends BlockSlotPanel {
    public enum BlockSlotEditMode {
        MODE_PAINT_TILE,
        MODE_TOGGLE_PRIORITY,
        MODE_TOGGLE_FLIP,
    }
    
    public enum ActiveMode {
        None,
        On,
        Off,
    }
    
    private MapBlocksetLayoutPanel mapBlocksetLayout;
    private TileSlotPanel leftTileSlotPanel;
    private TileSlotPanel rightTileSlotPanel;
    
    private BlockSlotEditMode currentMode = BlockSlotEditMode.MODE_PAINT_TILE;
    private boolean showPriorityFlag;
    
    public EditableBlockSlotPanel() {
        super();
        mouseInput = new LayoutMouseInput(this, this::onMousePressed, PIXEL_WIDTH, PIXEL_HEIGHT);
        setDisplayScale(4);
    }
    @Override
    protected void drawImage(Graphics graphics) {
        super.drawImage(graphics);
        Graphics2D g2 = (Graphics2D)graphics;
        if (showPriorityFlag) {
            Tile[] tiles = block.getTiles();
            for (int t = 0; t < tiles.length; t++) {
                if (tiles[t].isHighPriority()) {
                    g2.setColor(Color.BLACK);
                    g2.fillRect((t%3)*PIXEL_WIDTH+2, (t/3)*PIXEL_HEIGHT+2, 4, 4);
                    g2.setColor(Color.YELLOW);
                    g2.fillRect((t%3)*PIXEL_WIDTH+3, (t/3)*PIXEL_HEIGHT+3, 2, 2);
                }
            }
        }
    }
    
    public void setMapBlocksetLayout(MapBlocksetLayoutPanel mapBlocksetLayout) {
        this.mapBlocksetLayout = mapBlocksetLayout;
    }
    
    public BlockSlotEditMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(BlockSlotEditMode currentMode) {
        this.currentMode = currentMode;
    }
    
    public boolean getShowPriority() {
        return showPriorityFlag;
    }

    public void setShowPriority(boolean showPriorityFlag) {
        this.showPriorityFlag = showPriorityFlag;
        redraw();
    }

    @Override
    public void setBlock(MapBlock block) {
        super.setBlock(block);
    }
    
    private void onBlockUpdated() {
        block.clearIndexedColorImage(true);
        mapBlocksetLayout.getBlockset().clearIndexedColorImage(false);
        mapBlocksetLayout.redraw();
        mapBlocksetLayout.revalidate();
        mapBlocksetLayout.repaint();
        redraw();
        repaint();
    }
    
    public TileSlotPanel getLeftTileSlotPanel() {
        return leftTileSlotPanel;
    }

    public void setLeftTileSlotPanel(TileSlotPanel leftTileSlotPanel) {
        this.leftTileSlotPanel = leftTileSlotPanel;
    }
    
    public TileSlotPanel getRightTileSlotPanel() {
        return rightTileSlotPanel;
    }

    public void setRightTileSlotPanel(TileSlotPanel rightTileSlotPanel) {
        this.rightTileSlotPanel = rightTileSlotPanel;
    }
    
    private void onMousePressed(GridMousePressedEvent evt) {
        if (block == null) return;
        int x = evt.x();
        int y = evt.y();
        switch (currentMode) {
            case MODE_PAINT_TILE:
                if (evt.mouseButton() == MouseEvent.BUTTON1) {
                    Tile leftSlotTile = leftTileSlotPanel.getTile();
                    if (leftSlotTile != null) {
                        Tile[] tiles = block.getTiles();
                        tiles[x+y*3] = leftSlotTile.clone(tiles[x+y*3].isHighPriority(), leftSlotTile.ishFlip(), leftSlotTile.isvFlip(), leftSlotTile.getPalette());
                        onBlockUpdated();
                    }
                }
                else if (evt.mouseButton() == MouseEvent.BUTTON3) {
                    Tile rightSlotTile = rightTileSlotPanel.getTile();
                    if (rightSlotTile != null) {
                        Tile[] tiles = block.getTiles();
                        tiles[x+y*3] = rightSlotTile.clone(tiles[x+y*3].isHighPriority(), rightSlotTile.ishFlip(), rightSlotTile.isvFlip(), rightSlotTile.getPalette());
                        onBlockUpdated();
                    }
                }
                break;
            case MODE_TOGGLE_FLIP:
                if (evt.mouseButton() == MouseEvent.BUTTON1) {
                    block.getTiles()[x+y*3] = Tile.hFlip(block.getTiles()[x+y*3]);
                    onBlockUpdated();
                }
                else if (evt.mouseButton() == MouseEvent.BUTTON2) {
                    if (block.getTiles()[x+y*3].ishFlip() || block.getTiles()[x+y*3].isvFlip()) {
                        block.getTiles()[x+y*3] = Tile.clearFlip(block.getTiles()[x+y*3]);
                        onBlockUpdated();
                    }
                }
                else if (evt.mouseButton() == MouseEvent.BUTTON3) {
                    block.getTiles()[x+y*3] = Tile.vFlip(block.getTiles()[x+y*3]);
                    onBlockUpdated();
                }
                break;
            case MODE_TOGGLE_PRIORITY:
                if (evt.mouseButton() == MouseEvent.BUTTON1) {
                    block.getTiles()[x+y*3].setHighPriority(true);
                    onBlockUpdated();
                } else if (evt.mouseButton() == MouseEvent.BUTTON3) {
                    block.getTiles()[x+y*3].setHighPriority(false);
                    onBlockUpdated();
                }
                break;
        }
    }
}
