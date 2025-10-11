/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.gui;

import com.sfc.sf2.core.gui.layout.BaseMouseCoordsComponent;
import com.sfc.sf2.core.gui.layout.LayoutMouseInput;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.helpers.GraphicsHelpers;
import com.sfc.sf2.helpers.MapBlockHelpers;
import com.sfc.sf2.map.Map;
import com.sfc.sf2.map.MapArea;
import com.sfc.sf2.map.MapFlagCopyEvent;
import com.sfc.sf2.map.MapCopyEvent;
import com.sfc.sf2.map.MapItem;
import com.sfc.sf2.map.MapWarpEvent;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapTile;
import com.sfc.sf2.map.block.gui.BlockSlotPanel;
import com.sfc.sf2.map.block.gui.MapBlocksetLayoutPanel;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_HEIGHT;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_WIDTH;
import com.sfc.sf2.map.layout.gui.resources.MapLayoutFlagIcons;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author wiz
 */
public class MapLayoutPanel extends com.sfc.sf2.map.layout.gui.MapLayoutPanel {
    private static final Color COLOR_SELECTED = Color.YELLOW;
    private static final Color COLOR_SELECTED_SECONDARY = new Color(0xFFFFFF88);
    
    private static final int ACTION_CHANGE_BLOCK_VALUE = 0;
    private static final int ACTION_CHANGE_BLOCK_FLAGS = 1;
    private static final int ACTION_MASS_COPY = 2;
    
    public static final int MAP_FLAG_EDIT_BLOCK = 0;
        
    public static final int DRAW_MODE_NONE = 0;
    public static final int DRAW_MODE_EXPLORATION_FLAGS = 1;
    public static final int DRAW_MODE_INTERACTION_FLAGS = 1<<1;
    public static final int DRAW_MODE_GRID = 1<<2;
    public static final int DRAW_MODE_AREAS = 1<<3;
    public static final int DRAW_MODE_FLAG_COPIES = 1<<4;
    public static final int DRAW_MODE_STEP_COPIES = 1<<5;
    public static final int DRAW_MODE_ROOF_COPIES = 1<<6;
    public static final int DRAW_MODE_WARPS = 1<<7;
    public static final int DRAW_MODE_CHEST_ITEMS = 1<<8;
    public static final int DRAW_MODE_OTHER_ITEMS = 1<<9;
    public static final int DRAW_MODE_TRIGGERS = 1<<10;
    public static final int DRAW_MODE_VEHICLES = 1<<11;
    public static final int DRAW_MODE_ACTION_FLAGS = 1<<12;
    public static final int DRAW_MODE_ALL = (1<<13)-1;
        
    MapBlocksetLayoutPanel MapBlockLayoutPanel = null;
    BlockSlotPanel leftSlot = null;
    private boolean isOnActionsTab = true;
    
    private int currentMode = 0;
    private int togglesDrawMode = 0;
    private int selectedTabsDrawMode = 0;
    private int selectedItemIndex = -1;
    
    private boolean showAreasOverlay;
    private boolean showAreasUnderlay;
    private boolean showFlagCopyResult;
    private boolean showStepCopyResult;
    
    private MapBlock selectedBlock;
    MapBlock[][] copiedBlocks;
    private int copiedBlocksStartX = -1;
    private int copiedBlocksStartY = -1;
    private int copiedBlocksDrawX = -1;
    private int copiedBlocksDrawY = -1;
    private BufferedImage previewImage;
    private int lastMapX;
    private int lastMapY;
    private int previewIndex = -1;
    
    private List<int[]> actions = new ArrayList<int[]>();
    
    private Map map;

    public MapLayoutPanel() {
        super();
        mouseInput = new LayoutMouseInput(this, this::onMouseButtonInput, this::onMouseMove, PIXEL_WIDTH, PIXEL_HEIGHT);
        
        //MapBlockLayoutPanel.setLeftSelectedIndex(-1);
        //MapBlockLayoutPanel.setRightSelectedIndex(-1);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D)g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        Dimension offset = getImageOffset();
        g2.drawImage(previewImage, offset.width+(copiedBlocksDrawX*PIXEL_WIDTH)*getDisplayScale(), offset.height+(copiedBlocksDrawY*PIXEL_HEIGHT)*getDisplayScale(), null);
    }

    @Override
    protected void drawImage(Graphics graphics) {
        Graphics2D g2 = (Graphics2D)graphics;
        
        if (shouldDraw(DRAW_MODE_AREAS)) {
            if (showAreasUnderlay) {
                for (int i = 0; i < map.getAreas().length; i++) {
                    underlayMapBackground(g2, map.getAreas()[i]);
                }
            }
            if (showAreasOverlay) {
                for (int i = 0; i < map.getAreas().length; i++) {
                    underlayMapUpperLayer(g2, map.getAreas()[i]);
                }
            }
        }
        
        super.drawImage(graphics);  //Draw map blocks
                
        if (shouldDraw(DRAW_MODE_AREAS)) {
            for (int i = 0; i < map.getAreas().length; i++) {
                drawMapArea(g2, map.getAreas()[i], false);
                if (showAreasOverlay) {
                    overlayMapUpperLayer(g2, map.getAreas()[i]);
                }
            }
        }
        if (shouldDraw(DRAW_MODE_FLAG_COPIES)) {
            for (int i = 0; i < map.getFlagCopies().length; i++) {
                if (showFlagCopyResult) {
                    drawFlagCopyResult(g2, map.getFlagCopies()[i]);
                }
                drawMapFlagCopy(g2, map.getFlagCopies()[i], false);
            }
        }
        if (shouldDraw(DRAW_MODE_STEP_COPIES)) {
            for (int i = 0; i < map.getStepCopies().length; i++) {
                if (showStepCopyResult) {
                    drawStepCopyResult(g2, map.getStepCopies()[i]);
                }
                drawMapStepCopy(g2, map.getStepCopies()[i], false);
            }
        }
        if (shouldDraw(DRAW_MODE_ROOF_COPIES)) {
            for (int i = 0; i < map.getRoofCopies().length; i++) {
                drawMapRoofCopy(g2, map.getRoofCopies()[i], false);
            }
        }
        if (shouldDraw(DRAW_MODE_WARPS)) {
            drawMapWarps(g2);
            for (int i = 0; i < map.getWarps().length; i++) {
                drawMapWarp(g2, map.getWarps()[i], false);
            }
        }
        if (shouldDraw(DRAW_MODE_CHEST_ITEMS) || shouldDraw(DRAW_MODE_OTHER_ITEMS)) {
            drawMapItems(g2);
        }
        if (shouldDraw(DRAW_MODE_TRIGGERS)) {
            drawMapTriggers(g2);
        }
        if (shouldDraw(DRAW_MODE_VEHICLES)) {
            drawMapVehicleFlags(g2);
        }
        drawSelected(g2);
        buildPreviewImage();
    }
    
    private Image buildPreviewImage() {
        Image preview = null;
        boolean overlayRect = false;
        
        switch (currentMode) {
            case MAP_FLAG_EDIT_BLOCK:
                overlayRect = true;
                if (copiedBlocksStartX >= 0) {
                    //Dragging for mass copy
                    int width;
                    int height;
                    if (lastMapX < copiedBlocksStartX) {
                        width = copiedBlocksStartX - lastMapX + 1;
                        copiedBlocksDrawX = lastMapX;
                    } else {
                        width = lastMapX - copiedBlocksStartX + 1;
                        copiedBlocksDrawX = copiedBlocksStartX;
                    }
                    if (lastMapY < copiedBlocksStartY) {
                        height = copiedBlocksStartY - lastMapY + 1;
                        copiedBlocksDrawY = lastMapY;
                    } else {
                        height = lastMapY - copiedBlocksStartY + 1;
                        copiedBlocksDrawY = copiedBlocksStartY;
                    }
                    preview = new BufferedImage(width*PIXEL_WIDTH, height*PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D graphics = (Graphics2D)preview.getGraphics();
                    graphics.setColor(Color.YELLOW);
                    graphics.setStroke(new BasicStroke(2));
                    graphics.drawRect(0, 0, width*PIXEL_WIDTH, height*PIXEL_HEIGHT);
                    overlayRect = false;
                    graphics.dispose();
                } else if (copiedBlocks != null) {
                    overlayRect = true;
                    preview = new BufferedImage(copiedBlocks[0].length*PIXEL_WIDTH, copiedBlocks.length*PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D graphics = (Graphics2D)preview.getGraphics();
                    graphics.setColor(Color.WHITE);
                    for (int j=0; j < copiedBlocks.length; j++) {
                        for (int i=0; i < copiedBlocks[j].length; i++) {
                            graphics.drawImage(copiedBlocks[j][i].getIndexedColorImage(map.getLayout().getTilesets()), i*PIXEL_WIDTH, j*PIXEL_HEIGHT, null);
                        }
                    }
                    graphics.dispose();
                } else if (MapBlockLayoutPanel.selectedBlockIndexLeft == -1) {
                    //No block
                    preview = null;
                    previewIndex = -1;
                } else {
                    //Block selected
                    previewIndex = MapBlockLayoutPanel.selectedBlockIndexLeft;
                    selectedBlock = map.getBlockset().getBlocks()[previewIndex];
                    //"layout" is not MapBlockLayout. How to get that?
                    if (selectedBlock != null) {
                        preview = selectedBlock.getIndexedColorImage(map.getLayout().getTilesets());
                    }
                }
                break;
            default:
                preview = MapLayoutFlagIcons.getFlagIcon(currentMode).getImage();
                if (preview != null) {
                    overlayRect = true;
                }
                break;
        }
        
        if (preview != null || overlayRect) {
            if (preview == null) {
                previewImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            } else {
                previewImage = new BufferedImage(preview.getWidth(null), preview.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            }
            Graphics2D graphics = (Graphics2D)previewImage.getGraphics();
            if (preview != null) {
                graphics.setColor(Color.WHITE);
                graphics.drawImage(preview, 0, 0, null);
            }
            if (overlayRect) {
                graphics.setColor(Color.YELLOW);
                graphics.setStroke(new BasicStroke(3));
                graphics.drawRect(0,0, previewImage.getWidth(null), previewImage.getHeight(null));
            }
            graphics.dispose();
            previewImage = scale.resizeImage(previewImage);
        }
        
        return previewImage;
    }
    
    private void drawMapArea(Graphics2D g2, MapArea area, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        g2.setColor(selected ? COLOR_SELECTED : Color.WHITE);
        int width = area.getLayer1EndX()-area.getLayer1StartX()+1;
        int heigth = area.getLayer1EndY()-area.getLayer1StartY()+1;
        g2.drawRect(area.getLayer1StartX()*PIXEL_WIDTH+3, area.getLayer1StartY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        g2.setColor(selected ? COLOR_SELECTED_SECONDARY : Color.LIGHT_GRAY);
        if (area.getForegroundLayer2StartX() != 0 || area.getForegroundLayer2StartY() != 0) {
            g2.drawRect((area.getLayer1StartX()+area.getForegroundLayer2StartX())*PIXEL_WIDTH+3, (area.getLayer1StartY()+area.getForegroundLayer2StartY())*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        }
        if (area.getBackgroundLayer2StartX() != 0 || area.getBackgroundLayer2StartY() != 0) {
            g2.drawRect(area.getBackgroundLayer2StartX()*PIXEL_WIDTH+3, area.getBackgroundLayer2StartY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        }
    }
    
    private void overlayMapUpperLayer(Graphics2D g2, MapArea area) {
        if (!area.hasForegroundLayer2()) return;
        int width = area.getLayer1EndX()-area.getLayer1StartX();
        int height = area.getLayer1EndY()-area.getLayer1StartY();
        //g2.setColor(MapBlockHelpers.PRIORITY_DARKEN_COLOR);
        //g2.fillRect(area.getLayer1StartX()*PIXEL_WIDTH, area.getLayer1StartY()*PIXEL_HEIGHT, (width+1)*PIXEL_WIDTH, (height+1)*PIXEL_HEIGHT);
        MapBlock[] blocks = map.getLayout().getBlockset().getBlocks();
        for (int y = 0; y <= height; y++) {
            for (int x = 0; x <= width; x++) {
                int destX = (area.getLayer1StartX()+x)*PIXEL_WIDTH;
                int destY = (area.getLayer1StartY()+y)*PIXEL_HEIGHT;
                int index = area.getLayer1StartX()+area.getForegroundLayer2StartX()+x + (area.getLayer1StartY()+area.getForegroundLayer2StartY()+y)*BLOCK_WIDTH;
                g2.drawImage(blocks[index].getIndexedColorImage(map.getLayout().getTilesets()), destX, destY, null);
            }
        }
    }
    
    private void underlayMapUpperLayer(Graphics2D g2, MapArea area) {
        if (!area.hasForegroundLayer2()) return;
        int width = area.getLayer1EndX()-area.getLayer1StartX();
        int height = area.getLayer1EndY()-area.getLayer1StartY();
        MapBlock[] blocks = map.getLayout().getBlockset().getBlocks();
        for (int y = 0; y <= height; y++) {
            for (int x = 0; x <= width; x++) {
                int destX = (area.getForegroundLayer2StartX()+x)*PIXEL_WIDTH;
                int destY = (area.getForegroundLayer2StartY()+y)*PIXEL_HEIGHT;
                int index = area.getLayer1StartX()+x + (area.getLayer1StartY()+y)*BLOCK_WIDTH;
                g2.drawImage(blocks[index].getIndexedColorImage(map.getLayout().getTilesets()), destX, destY, null);
            }
        }
        g2.setColor(MapBlockHelpers.PRIORITY_DARKEN_COLOR);
        g2.fillRect(area.getForegroundLayer2StartX()*PIXEL_WIDTH, area.getForegroundLayer2StartY()*PIXEL_HEIGHT, (width+1)*PIXEL_WIDTH, (height+1)*PIXEL_HEIGHT);
    }
    
    private void underlayMapBackground(Graphics2D g2, MapArea area) {
        if (!area.hasBackgroundLayer2()) return;
        int width = area.getLayer1EndX()-area.getLayer1StartX();
        int height = area.getLayer1EndY()-area.getLayer1StartY();
        MapBlock[] blocks = map.getLayout().getBlockset().getBlocks();
        for (int y = 0; y <= height; y++) {
            for (int x = 0; x <= width; x++) {
                int destX = (area.getLayer1StartX()+x)*PIXEL_WIDTH;
                int destY = (area.getLayer1StartY()+y)*PIXEL_HEIGHT;
                int index = area.getBackgroundLayer2StartX()+x + (area.getBackgroundLayer2StartY()+y)*BLOCK_WIDTH;
                g2.drawImage(blocks[index].getIndexedColorImage(map.getLayout().getTilesets()), destX, destY, null);
            }
        }
    }
    
    private void drawMapFlagCopy(Graphics2D g2, MapFlagCopyEvent flagCopy, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        int width = flagCopy.getWidth();
        int heigth = flagCopy.getHeight();
        g2.setColor(selected ? COLOR_SELECTED_SECONDARY : Color.LIGHT_GRAY);
        g2.drawRect(flagCopy.getDestX()*PIXEL_WIDTH+3, flagCopy.getDestY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        g2.setColor(selected ? COLOR_SELECTED : Color.CYAN);
        g2.drawRect(flagCopy.getSourceX()*PIXEL_WIDTH+3,flagCopy.getSourceY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        if (selected) {
            GraphicsHelpers.drawArrowLine(g2, flagCopy.getSourceX()*PIXEL_WIDTH+12, flagCopy.getSourceY()*PIXEL_HEIGHT+12, flagCopy.getDestX()*PIXEL_WIDTH+12, flagCopy.getDestY()*PIXEL_HEIGHT+12);
        }
    }
    
    private void drawFlagCopyResult(Graphics2D g2, MapFlagCopyEvent flagCopy) {
        int dx = (flagCopy.getDestX()-flagCopy.getSourceX())*PIXEL_WIDTH;
        int dy = (flagCopy.getDestY()-flagCopy.getSourceY())*PIXEL_HEIGHT;
        g2.copyArea(flagCopy.getSourceX()*PIXEL_WIDTH, flagCopy.getSourceY()*PIXEL_HEIGHT, flagCopy.getWidth()*PIXEL_WIDTH, flagCopy.getHeight()*PIXEL_HEIGHT, dx, dy);
    }
    
    private void drawMapStepCopy(Graphics2D g2, MapCopyEvent stepCopy, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        int width = stepCopy.getWidth();
        int heigth = stepCopy.getHeight();
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRect(stepCopy.getDestX()*PIXEL_WIDTH+3, stepCopy.getDestY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        g2.setColor(selected ? COLOR_SELECTED_SECONDARY : Color.CYAN);
        g2.drawRect(stepCopy.getSourceX()*PIXEL_WIDTH+3,stepCopy.getSourceY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        g2.setColor(selected ? COLOR_SELECTED : Color.WHITE);
        g2.drawRect(stepCopy.getTriggerX()*PIXEL_WIDTH, stepCopy.getTriggerY()*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
        if (selected) {
            GraphicsHelpers.drawArrowLine(g2, stepCopy.getSourceX()*PIXEL_WIDTH+12, stepCopy.getSourceY()*PIXEL_HEIGHT+12, stepCopy.getDestX()*PIXEL_WIDTH+12, stepCopy.getDestY()*PIXEL_HEIGHT+12);
        }
    }
    
    private void drawStepCopyResult(Graphics2D g2, MapCopyEvent stepCopy) {
        int dx = (stepCopy.getDestX()-stepCopy.getSourceX())*PIXEL_WIDTH;
        int dy = (stepCopy.getDestY()-stepCopy.getSourceY())*PIXEL_HEIGHT;
        g2.copyArea(stepCopy.getSourceX()*PIXEL_WIDTH, stepCopy.getSourceY()*PIXEL_HEIGHT, stepCopy.getWidth()*PIXEL_WIDTH, stepCopy.getHeight()*PIXEL_HEIGHT, dx, dy);
    }
    
    private void drawMapRoofCopy(Graphics2D g2, MapCopyEvent roofCopy, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        int width = roofCopy.getWidth();
        int heigth = roofCopy.getHeight();
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRect(roofCopy.getTriggerX()*PIXEL_WIDTH, (roofCopy.getTriggerY()+1)*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
        int sourceX = roofCopy.getSourceX();
        int sourceY = roofCopy.getSourceY();
        if (sourceX == 0xFF && sourceY == 0xFF) {
            MapArea area = map.getAreas()[0];
            sourceX = roofCopy.getDestX() - (area.getForegroundLayer2StartX()-area.getLayer1StartX());
            sourceY = roofCopy.getDestY() - (area.getForegroundLayer2StartY()-area.getLayer1StartY());
        }
        g2.setColor(selected ? COLOR_SELECTED_SECONDARY : Color.CYAN);
        g2.drawRect(sourceX*PIXEL_WIDTH+3, sourceY*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        g2.setColor(selected ? COLOR_SELECTED : Color.WHITE);
        g2.drawRect(roofCopy.getTriggerX()*PIXEL_WIDTH, roofCopy.getTriggerY()*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
        g2.setColor(selected ? COLOR_SELECTED_SECONDARY : Color.LIGHT_GRAY);
        g2.drawRect(roofCopy.getDestX()*PIXEL_WIDTH + 3, roofCopy.getDestY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        if (selected) {
            g2.setColor(Color.WHITE);
            GraphicsHelpers.drawArrowLine(g2, roofCopy.getTriggerX()*PIXEL_WIDTH+12, roofCopy.getTriggerY()*PIXEL_HEIGHT+12, roofCopy.getDestX()*PIXEL_WIDTH+12, roofCopy.getDestY()*PIXEL_HEIGHT+12);
            g2.setColor(COLOR_SELECTED);
            GraphicsHelpers.drawArrowLine(g2, roofCopy.getDestX()*PIXEL_WIDTH+12, roofCopy.getDestY()*PIXEL_HEIGHT+12, sourceX*PIXEL_WIDTH+12, sourceY*PIXEL_HEIGHT+12, true, true);
        }
    }
    
    private void drawMapWarps(Graphics2D g2) {
        MapBlock[] blocks = map.getLayout().getBlockset().getBlocks();
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(1));
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                int itemFlag = blocks[x+y*BLOCK_WIDTH].getEventFlags();
                if (itemFlag == MapBlock.MAP_FLAG_WARP) {
                    g2.drawImage(MapLayoutFlagIcons.getWarpIcon().getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
                }
            }
        }
    }
    
    private void drawMapWarp(Graphics2D g2, MapWarpEvent warp, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        g2.setColor(selected ? COLOR_SELECTED : Color.CYAN);
        if (warp.getTriggerX() == 0xFF || warp.getTriggerY() == 0xFF) {
            MapArea mainArea = map.getAreas()[0];
            int x, w, y, h;
            if (warp.getTriggerX() == 0xFF) {
                x = mainArea.getLayer1StartX();
                w = mainArea.getLayer1EndX()-x+1;
            } else {
                x = warp.getTriggerX();
                w = 1;
            }
            if (warp.getTriggerY() == 0xFF) {
                y = mainArea.getLayer1StartY();
                h = mainArea.getLayer1EndY()-y+1;
            } else {
                y = warp.getTriggerY();
                h = 1;
            }
            g2.drawRect(x*PIXEL_WIDTH, y*PIXEL_HEIGHT, w*PIXEL_WIDTH, h*PIXEL_HEIGHT);
        } else {
            g2.drawImage(MapLayoutFlagIcons.getWarpIcon().getImage(), warp.getTriggerX()*PIXEL_WIDTH, warp.getTriggerY()*PIXEL_HEIGHT, null);
            if (selected) {
                g2.drawRect(warp.getTriggerX()*PIXEL_WIDTH, warp.getTriggerY()*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
            }
        }
        if (warp.getDestMap().equals("MAP_CURRENT")) {
            g2.setColor(selected ? COLOR_SELECTED : Color.BLUE);
            GraphicsHelpers.drawArrowLine(g2, warp.getTriggerX()*PIXEL_WIDTH+12, warp.getTriggerY()*PIXEL_HEIGHT+12, warp.getDestX()*PIXEL_WIDTH+12, warp.getDestY()*PIXEL_HEIGHT+12);
        }
    }    

    private void drawMapItems(Graphics2D g2) {
        g2.setStroke(new BasicStroke(3));
        MapBlock[] blocks = map.getLayout().getBlockset().getBlocks();
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                ImageIcon icon = MapLayoutFlagIcons.getBlockItemFlagIcon(blocks[x+y*BLOCK_WIDTH].getEventFlags());
                if (icon != null) {
                    g2.drawImage(icon.getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
                }
            }
        }
    }
    
    private void drawItem(Graphics2D g2, MapItem item, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.YELLOW);
        g2.drawRect(item.getX()*PIXEL_WIDTH, item.getY()*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
    }
        
    private void drawMapTriggers(Graphics2D g2) {
        MapBlock[] blocks = map.getLayout().getBlockset().getBlocks();
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                ImageIcon icon = MapLayoutFlagIcons.getBlockTriggersFlagIcon(blocks[x+y*BLOCK_WIDTH].getEventFlags());
                if (icon != null) {
                    g2.drawImage(icon.getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
                }
            }
        }
    }
        
    private void drawMapVehicleFlags(Graphics2D g2) {
        MapBlock[] blocks = map.getLayout().getBlockset().getBlocks();
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                if (blocks[x+y*BLOCK_WIDTH].getEventFlags() == MapBlock.MAP_FLAG_CARAVAN) {
                    g2.drawImage(MapLayoutFlagIcons.getCaravanIcon().getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
                } else if (blocks[x+y*BLOCK_WIDTH].getEventFlags() == MapBlock.MAP_FLAG_RAFT) {
                    g2.drawImage(MapLayoutFlagIcons.getRaftIcon().getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
                }
            }
        }
    }
    
    private void drawSelected(Graphics2D g2) {
        if (selectedItemIndex == -1 || isOnActionsTab) return;
        switch (selectedTabsDrawMode) {
            case DRAW_MODE_AREAS:
                drawMapArea(g2, map.getAreas()[selectedItemIndex], true);
                break;
            case DRAW_MODE_FLAG_COPIES:
                drawMapFlagCopy(g2, map.getFlagCopies()[selectedItemIndex], true);
                break;
            case DRAW_MODE_STEP_COPIES:
                drawMapStepCopy(g2, map.getStepCopies()[selectedItemIndex], true);
                break;
            case DRAW_MODE_ROOF_COPIES:
                drawMapRoofCopy(g2, map.getRoofCopies()[selectedItemIndex], true);
                break;
            case DRAW_MODE_WARPS:
                drawMapWarp(g2, map.getWarps()[selectedItemIndex], true);
                break;
            case DRAW_MODE_CHEST_ITEMS:
                drawItem(g2, map.getChestItems()[selectedItemIndex], true);
                break;
            case DRAW_MODE_OTHER_ITEMS:
                drawItem(g2, map.getOtherItems()[selectedItemIndex], true);
                break;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Input">      
    private void onMouseButtonInput(BaseMouseCoordsComponent.GridMousePressedEvent evt) {
        if (!isOnActionsTab)
            return;
        int x = evt.x();
        int y = evt.y();
        if (currentMode == MAP_FLAG_EDIT_BLOCK) {
            if (evt.dragging()) {
                if (evt.mouseButton() == MouseEvent.BUTTON2 && copiedBlocksStartX >= 0) {
                    previewImage = null;
                    lastMapX = x;
                    lastMapY = y;
                    redraw();
                }
            } else if (evt.lifted()) {
                if (evt.mouseButton() == MouseEvent.BUTTON2) {
                    if (x == copiedBlocksStartX && y == copiedBlocksStartY) {
                        selectedBlock = layout.getBlockset().getBlocks()[x+y*BLOCK_WIDTH];
                        MapBlockLayoutPanel.selectedBlockIndexLeft = selectedBlock.getIndex();
                        updateLeftSlot(selectedBlock);
                    } else {
                        // Mass copy
                        int xStart;
                        int xEnd;
                        int yStart;
                        int yEnd;
                        if (x > copiedBlocksStartX) {
                            xStart = copiedBlocksStartX;
                            xEnd = x;
                        } else {
                            xStart = x;
                            xEnd = copiedBlocksStartX;
                        }
                        if (y > copiedBlocksStartY) {
                            yStart = copiedBlocksStartY;
                            yEnd = y;
                        } else {
                            yStart = y;
                            yEnd = copiedBlocksStartY;
                        }
                        int width = xEnd-xStart+1;
                        int height = yEnd-yStart+1;
                        copiedBlocks = new MapBlock[height][width];
                        for (int j=0;j < height; j++) {
                            for (int i=0;i < width; i++) {
                                copiedBlocks[j][i] = layout.getBlockset().getBlocks()[xStart+i+(yStart+j)*BLOCK_WIDTH];
                            }
                        }
                        MapBlockLayoutPanel.selectedBlockIndexLeft = -1;

                        BufferedImage img = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2 = (Graphics2D)img.getGraphics();
                        g2.setColor(Color.YELLOW);
                        g2.drawRect(0, 0, img.getWidth()-1, img.getHeight()-1);
                        g2.setColor(Color.BLACK);
                        g2.drawString("copy", -1, 15);
                        g2.dispose();

                        leftSlot.setOverrideImage(img);
                        leftSlot.revalidate();
                    }

                    copiedBlocksStartX = copiedBlocksStartY = -1;
                }
            } else {
                switch (evt.mouseButton()) {
                    case MouseEvent.BUTTON1:
                        if(MapBlockLayoutPanel.selectedBlockIndexLeft !=- 1) {
                            setBlockValue(x, y, MapBlockLayoutPanel.selectedBlockIndexLeft);
                            if (selectedBlock != null && selectedBlock.getIndex() == MapBlockLayoutPanel.selectedBlockIndexLeft) {
                                setFlagValue(x, y, selectedBlock.getFlags());
                            }
                        } else if (copiedBlocks != null) {
                            int height = copiedBlocks.length;
                            int width = copiedBlocks[0].length;
                            int[] action = new int[4+2*height*width];
                            action[0] = ACTION_MASS_COPY;
                            int blockIndex = x+y*BLOCK_WIDTH;
                            action[1] = blockIndex;
                            action[2] = width;
                            action[3] = height;
                            for (int j=0; j < height; j++) {
                                for (int i=0; i < width; i++) {
                                    if ((blockIndex+i+j*BLOCK_WIDTH) < 4096 && (i+(blockIndex%BLOCK_WIDTH)) < BLOCK_WIDTH) {
                                        MapBlock previousBlock = layout.getBlockset().getBlocks()[blockIndex+i+j*BLOCK_WIDTH];
                                        action[4+2*(i+j*width)] = previousBlock.getIndex();
                                        int origFlags = previousBlock.getFlags();
                                        action[4+2*(i+j*width)+1] = origFlags;
                                        int index = copiedBlocks[j][i].getIndex();
                                        int flags = copiedBlocks[j][i].getExplorationFlags()+(MapBlock.MAP_FLAG_MASK_EVENTS & origFlags);
                                        MapTile[] tiles = copiedBlocks[j][i].getMapTiles();
                                        layout.getBlockset().getBlocks()[blockIndex+i+j*64] = new MapBlock(index, flags, tiles);
                                    } else {
                                        action[4+2*(j*width+i)] = -1;
                                        action[4+2*(j*width+i)+1] = -1;
                                    }
                                }
                            }
                            actions.add(action);
                            redraw();
                        }
                        break;
                    case MouseEvent.BUTTON2:
                        copiedBlocksStartX = lastMapX = x;
                        copiedBlocksStartY = lastMapY = y;
                        copiedBlocks = null;
                        redraw();
                        break;
                    case MouseEvent.BUTTON3:
                        setBlockValue(x, y, MapBlockLayoutPanel.selectedBlockIndexRight);
                        break;
                }
            }
        } else {
            switch (evt.mouseButton()) {
                case MouseEvent.BUTTON1:
                    int flagVal = currentMode;
                    if (flagVal == MapBlock.MAP_FLAG_STAIRS_RIGHT) {
                        if (layout.getBlockset().getBlocks()[x+y*BLOCK_WIDTH].getExplorationFlags() == MapBlock.MAP_FLAG_STAIRS_RIGHT) {
                            flagVal = MapBlock.MAP_FLAG_STAIRS_LEFT;
                        }
                    }
                    setFlagValue(x, y, flagVal);
                    break;
                case MouseEvent.BUTTON2:
                    clearFlagValue(x, y, 0xFF00);
                    break;
                case MouseEvent.BUTTON3:
                    if ((currentMode & MapBlock.MAP_FLAG_MASK_EXPLORE) != 0) {
                        clearFlagValue(x, y, MapBlock.MAP_FLAG_MASK_EXPLORE);
                    } else {
                        clearFlagValue(x, y, MapBlock.MAP_FLAG_MASK_EVENTS);
                    }
                    break;
            }
        }
    }

    private void onMouseMove(BaseMouseCoordsComponent.GridMouseMoveEvent evt) {
        copiedBlocksDrawX = evt.x();
        copiedBlocksDrawY = evt.y();
        revalidate();
    }
    
    private void updateLeftSlot(MapBlock block){
        leftSlot.setBlock(block);
    }
    
    public void setBlockValue(int x, int y, int value) {
        if (value == -1) return;
        MapBlock[] blocks = map.getLayout().getBlockset().getBlocks();
        MapBlock block = blocks[x+y*BLOCK_WIDTH];
        if (block.getIndex() != value) {
            int[] action = new int[3];
            action[0] = ACTION_CHANGE_BLOCK_VALUE;
            action[1] = x+y*BLOCK_WIDTH;
            action[2] = block.getIndex();
            block.setIndex(value);
            block.clearIndexedColorImage();
            block.setMapTiles(map.getLayout().getBlockset().getBlocks()[block.getIndex()].getMapTiles());
            actions.add(action);
            redraw();
        }
    }
    
    public void setFlagValue(int x, int y, int value) {
        MapBlock[] blocks = layout.getBlockset().getBlocks();
        MapBlock block = blocks[x+y*BLOCK_WIDTH];
        if (block.getFlags() != value) {
            int[] action = new int[3];
            action[0] = ACTION_CHANGE_BLOCK_FLAGS;
            action[1] = x+y*BLOCK_WIDTH;
            int origFlags = block.getFlags();
            action[2] = origFlags;
            int newFlags = 0;
            if ((value & MapBlock.MAP_FLAG_MASK_EXPLORE) != 0) {
                newFlags = value & MapBlock.MAP_FLAG_MASK_EXPLORE;
                newFlags += origFlags & MapBlock.MAP_FLAG_MASK_EVENTS;
            } else {
                newFlags = origFlags & MapBlock.MAP_FLAG_MASK_EXPLORE;
                newFlags += value & MapBlock.MAP_FLAG_MASK_EVENTS;
            }
            block.setFlags(newFlags);
            actions.add(action);
            redraw();
        }
    }
    
    public void clearFlagValue(int x, int y, int value) {
        MapBlock[] blocks = layout.getBlockset().getBlocks();
        MapBlock block = blocks[x+y*BLOCK_WIDTH];
        if (block.getFlags() != 0) {
            int[] action = new int[3];
            action[0] = ACTION_CHANGE_BLOCK_FLAGS;
            action[1] = x+y*BLOCK_WIDTH;
            int origFlags = block.getFlags();
            action[2] = origFlags;
            int newFlags = origFlags & ~value;
            block.setFlags(newFlags);
            actions.add(action);
            redraw();
        }
    }
    
    public void revertLastAction() {
        if (actions.isEmpty()) return;
        int[] action = actions.get(actions.size()-1);
        switch (action[0]) {
            case ACTION_CHANGE_BLOCK_VALUE:
                {
                    MapBlock block = layout.getBlockset().getBlocks()[action[1]];
                    block.setIndex(action[2]);
                    block.setMapTiles(map.getLayout().getBlockset().getBlocks()[block.getIndex()].getMapTiles());
                    actions.remove(actions.size()-1);
                    redraw();
                    break;
                }
            case ACTION_CHANGE_BLOCK_FLAGS:
                {
                    MapBlock block = map.getLayout().getBlockset().getBlocks()[action[1]];
                    block.setFlags(action[2]);
                    actions.remove(actions.size()-1);
                    redraw();
                    break;
                }
            case ACTION_MASS_COPY:
                int blockIndex = action[1];
                int width = action[2];
                int height = action[3];
                for (int j=0; j < height; j++) {
                    for (int i=0; i < width; i++) {
                        int value = action[4+2*(j*width+i)];
                        int flags = action[4+2*(j*width+i)+1];
                        if (value != -1 && flags != -1) {
                            MapBlock block = new MapBlock(value, flags, map.getLayout().getBlockset().getBlocks()[value].getMapTiles());
                            layout.getBlockset().getBlocks()[i+blockIndex+j*BLOCK_WIDTH] = block;
                        }
                    }
                }   actions.remove(actions.size()-1);
                redraw();
                break;
            default:
                break;
        }
    }// </editor-fold>

    public MapBlock getSelectedBlock0() {
        return selectedBlock;
    }

    public void setSelectedBlock0(MapBlock selectedBlock0) {
        this.selectedBlock = selectedBlock0;
    }

    public List<int[]> getActions() {
        return actions;
    }

    public void setActions(List<int[]> actions) {
        this.actions = actions;
    }

    public int getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(int currentMode) {
        this.currentMode = currentMode;
        redraw();
    }

    public BlockSlotPanel getLeftSlot() {
        return leftSlot;
    }

    public void setLeftSlot(BlockSlotPanel leftSlot) {
        this.leftSlot = leftSlot;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
        if (map != null) {
            setMapLayout(map.getLayout());
        }
        redraw();
    }

    @Override
    public boolean getShowExplorationFlags() {
        return super.getShowExplorationFlags() || shouldDraw(DRAW_MODE_EXPLORATION_FLAGS);
    }
    
    private boolean shouldDraw(int drawFlag) {
        return isDrawMode_Toggles(drawFlag) || isDrawMode_Tabs(drawFlag) || isDrawMode_Actions(drawFlag);
    }

    public boolean isDrawMode_Toggles(int drawFlag) {
        return (togglesDrawMode & drawFlag) > 0;
    }

    public void setDrawMode_Toggles(int drawFlag, boolean on) {
        if (drawFlag == DRAW_MODE_ALL)
            togglesDrawMode = on ? drawFlag : 0;    
        else if (on)
            togglesDrawMode = (togglesDrawMode | drawFlag);
        else
            togglesDrawMode = (togglesDrawMode & ~drawFlag);
        redraw();
    }

    public boolean isDrawMode_Tabs(int drawFlag) {
        return (selectedTabsDrawMode & drawFlag) > 0;
    }

    public void setDrawMode_Tabs(int drawFlag) {
        selectedTabsDrawMode = drawFlag;
        redraw();
    }

    public boolean isDrawMode_Actions(int drawFlag) {
        if (!isOnActionsTab)
            return false;
        
        switch (currentMode)
        {
            case MapBlock.MAP_FLAG_OBSTRUCTED:
            case MapBlock.MAP_FLAG_STAIRS_RIGHT:
            case MapBlock.MAP_FLAG_STAIRS_LEFT:
                return (DRAW_MODE_EXPLORATION_FLAGS & drawFlag) != 0;
            case MapBlock.MAP_FLAG_CHEST:
            case MapBlock.MAP_FLAG_SEARCH:
            case MapBlock.MAP_FLAG_TABLE:
            case MapBlock.MAP_FLAG_VASE:
            case MapBlock.MAP_FLAG_BARREL:
            case MapBlock.MAP_FLAG_SHELF:
                return (DRAW_MODE_CHEST_ITEMS & drawFlag) != 0 || (DRAW_MODE_OTHER_ITEMS & drawFlag) != 0;
            case MapBlock.MAP_FLAG_WARP:
                return (DRAW_MODE_WARPS & drawFlag) != 0;
            case MapBlock.MAP_FLAG_TRIGGER:
            case MapBlock.MAP_FLAG_LAYER_UP:
            case MapBlock.MAP_FLAG_LAYER_DOWN:
                return (DRAW_MODE_TRIGGERS & drawFlag) != 0;
            case MapBlock.MAP_FLAG_CARAVAN:
            case MapBlock.MAP_FLAG_RAFT:
                return (DRAW_MODE_VEHICLES & drawFlag) != 0;
            default:
                return false;
        }
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
        redraw();
    }
    
    public boolean getIsOnActionsTab() {        
        return isOnActionsTab;
    }
    
    public void setIsOnActionsTab(boolean isOnActionsTab) {
        this.isOnActionsTab = isOnActionsTab;
    }

    public void setShowAreasOverlay(boolean showAreasOverlay) {
        this.showAreasOverlay = showAreasOverlay;
        redraw();
    }

    public void setShowAreasUnderlay(boolean showAreasUnderlay) {
        this.showAreasUnderlay = showAreasUnderlay;
        redraw();
    }

    public void setShowFlagCopyResult(boolean showFlagCopyResult) {
        this.showFlagCopyResult = showFlagCopyResult;
        redraw();
    }

    public void setShowStepCopyResult(boolean showStepCopyResult) {
        this.showStepCopyResult = showStepCopyResult;
        redraw();
    }
}
