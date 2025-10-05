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
import com.sfc.sf2.map.Map;
import com.sfc.sf2.map.MapArea;
import com.sfc.sf2.map.MapFlagCopyEvent;
import com.sfc.sf2.map.MapCopyEvent;
import com.sfc.sf2.map.MapWarpEvent;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapTile;
import com.sfc.sf2.map.block.gui.BlockSlotPanel;
import com.sfc.sf2.map.block.gui.MapBlocksetLayoutPanel;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_HEIGHT;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_WIDTH;
import com.sfc.sf2.map.layout.gui.resources.MapLayoutFlagImages;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wiz
 */
public class MapLayoutPanel extends com.sfc.sf2.map.layout.gui.MapLayoutPanel {
        
    private static final int ACTION_CHANGE_BLOCK_VALUE = 0;
    private static final int ACTION_CHANGE_BLOCK_FLAGS = 1;
    private static final int ACTION_MASS_COPY = 2;
    
    public static final int MODE_BLOCK = 0;
    public static final int MODE_OBSTRUCTED = 1;
    public static final int MODE_STAIRS = 2;
    public static final int MODE_WARP = 3;
    public static final int MODE_BARREL = 4;
    public static final int MODE_VASE = 5;
    public static final int MODE_TABLE = 6;
    public static final int MODE_TRIGGER = 7;
        
    public static final int DRAW_MODE_NONE = 0;
    public static final int DRAW_MODE_EXPLORATION_FLAGS = 1;
    public static final int DRAW_MODE_INTERACTION_FLAGS = 1<<1;
    public static final int DRAW_MODE_GRID = 1<<2;
    public static final int DRAW_MODE_AREAS = 1<<3;
    public static final int DRAW_MODE_FLAG_COPIES = 1<<4;
    public static final int DRAW_MODE_STEP_COPIES = 1<<5;
    public static final int DRAW_MODE_ROOF_COPIES = 1<<6;
    public static final int DRAW_MODE_WARPS = 1<<7;
    public static final int DRAW_MODE_ITEMS = 1<<8;
    public static final int DRAW_MODE_TRIGGERS = 1<<9;
    public static final int DRAW_MODE_ACTION_FLAGS = 1<<10;
    public static final int DRAW_MODE_ALL = (1<<11)-1;
        
    MapBlocksetLayoutPanel MapBlockLayoutPanel = null;
    BlockSlotPanel leftSlot = null;
    private boolean isOnActionsTab = true;
    
    private int currentMode = 0;
    private int togglesDrawMode = 0;
    private int selectedTabsDrawMode = 0;
    private int selectedItemIndex;
    
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
        mouseInput = new LayoutMouseInput(this, this::onMouseButtonInput, BLOCK_WIDTH, BLOCK_HEIGHT);
        
        //MapBlockLayoutPanel.setLeftSelectedIndex(-1);
        //MapBlockLayoutPanel.setRightSelectedIndex(-1);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(previewImage, copiedBlocksDrawX, copiedBlocksDrawY, null);
    }

    @Override
    protected void drawImage(Graphics graphics) {
        super.drawImage(graphics);
        
        Graphics2D g2 = (Graphics2D)graphics;
        if (shouldDraw(DRAW_MODE_AREAS)) {
            for (int i = 0; i < map.getAreas().length; i++) {
                drawMapArea(g2, map.getAreas()[i], false);
            }
        }
        if (shouldDraw(DRAW_MODE_FLAG_COPIES)) {
            for (int i = 0; i < map.getFlagCopies().length; i++) {
                drawMapFlagCopy(g2, map.getFlagCopies()[i], false);
            }
        }
        if (shouldDraw(DRAW_MODE_STEP_COPIES)) {
            for (int i = 0; i < map.getStepCopies().length; i++) {
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
        if (shouldDraw(DRAW_MODE_ITEMS)) {
            drawMapItems(g2);
        }
        if (shouldDraw(DRAW_MODE_TRIGGERS)) {
            drawMapTriggers(g2);
        }
        drawSelected(g2);
        buildPreviewImage();
    }
    
    private BufferedImage buildPreviewImage() {
        BufferedImage preview = null;
        boolean overlayRect = false;
        
        switch (currentMode) {
            case MODE_BLOCK:
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
            case MODE_OBSTRUCTED:
                overlayRect = true;
                preview = MapLayoutFlagImages.getObstructedImage();
                break;
            case MODE_STAIRS:
                overlayRect = true;
                preview = MapLayoutFlagImages.getLeftUpstairsImage();
                break;
            case MODE_WARP:
                preview = MapLayoutFlagImages.getWarpImage();
                break;
            case MODE_BARREL:
                overlayRect = true;
                preview = MapLayoutFlagImages.getBarrelImage();
                break;
            case MODE_VASE:
                overlayRect = true;
                preview = MapLayoutFlagImages.getVaseImage();
                break;
            case MODE_TABLE:
                overlayRect = true;
                preview = MapLayoutFlagImages.getTableImage();
                break;
            case MODE_TRIGGER:
                preview = MapLayoutFlagImages.getTriggerImage();
                break;
            default:
                preview = null;
                break;
        }
        
        if (preview != null || overlayRect) {
            if (preview == null) {
                previewImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            } else {
                previewImage = new BufferedImage(preview.getWidth(), preview.getHeight(), BufferedImage.TYPE_INT_ARGB);
            }
            Graphics2D graphics = (Graphics2D)previewImage.getGraphics();
            if (preview != null) {
                graphics.setColor(Color.WHITE);
                graphics.drawImage(preview, 0, 0, null);
            }
            if (overlayRect) {
                graphics.setColor(Color.YELLOW);
                graphics.setStroke(new BasicStroke(2));
                graphics.drawRect(0,0, previewImage.getWidth(), previewImage.getHeight());
            }
            graphics.dispose();
            previewImage = scale.resizeImage(previewImage);
        }
        
        return previewImage;
    }
    
    private void drawMapArea(Graphics2D g2, MapArea area, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        g2.setColor(selected ? Color.YELLOW : Color.WHITE);
        int width = area.getLayer1EndX()-area.getLayer1StartX()+1;
        int heigth = area.getLayer1EndY()-area.getLayer1StartY()+1;
        g2.drawRect(area.getLayer1StartX()*PIXEL_WIDTH+3, area.getLayer1StartY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        g2.setColor(selected ? Color.DARK_GRAY : Color.LIGHT_GRAY);
        if (area.getForegroundLayer2StartX()!=0 || area.getForegroundLayer2StartY() != 0) {
            g2.drawRect((area.getLayer1StartX()+area.getForegroundLayer2StartX())*PIXEL_WIDTH+3, (area.getLayer1StartY()+area.getForegroundLayer2StartY())*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        }
        if (area.getBackgroundLayer2StartX()!=0 || area.getBackgroundLayer2StartY() != 0) {
            g2.drawRect(area.getBackgroundLayer2StartX()*PIXEL_WIDTH+3, area.getBackgroundLayer2StartY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        }
    }
    
    private void drawMapFlagCopy(Graphics2D g2, MapFlagCopyEvent flagCopy, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        g2.setColor(selected ? Color.YELLOW : Color.CYAN);
        int width = flagCopy.getWidth();
        int heigth = flagCopy.getHeight();
        g2.drawRect(flagCopy.getSourceX()*PIXEL_WIDTH+3,flagCopy.getSourceY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        g2.setColor(selected ? Color.DARK_GRAY : Color.LIGHT_GRAY);
        g2.drawRect(flagCopy.getDestX()*PIXEL_WIDTH+3, flagCopy.getDestY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
    }
    
    private void drawMapStepCopy(Graphics2D g2, MapCopyEvent stepCopy, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        g2.setColor(selected ? Color.YELLOW : Color.WHITE);
        g2.drawRect(stepCopy.getTriggerX()*PIXEL_WIDTH, stepCopy.getTriggerY()*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
        g2.setColor(Color.CYAN);
        int width = stepCopy.getWidth();
        int heigth = stepCopy.getHeight();
        g2.drawRect(stepCopy.getSourceX()*PIXEL_WIDTH+3,stepCopy.getSourceY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        g2.setColor(selected ? Color.DARK_GRAY : Color.LIGHT_GRAY);
        g2.drawRect(stepCopy.getDestX()*PIXEL_WIDTH+3, stepCopy.getDestY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
    }
    
    private void drawMapRoofCopy(Graphics2D g2, MapCopyEvent roofCopy, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        g2.setColor(selected ? Color.YELLOW : Color.WHITE);
        g2.drawRect(roofCopy.getTriggerX()*PIXEL_WIDTH, roofCopy.getTriggerY()*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
        g2.setColor(selected ? Color.DARK_GRAY : Color.LIGHT_GRAY);
        g2.drawRect(roofCopy.getTriggerX()*PIXEL_WIDTH, (roofCopy.getTriggerY()+1)*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
        g2.setColor(Color.CYAN);
        int width = roofCopy.getWidth();
        int heigth = roofCopy.getHeight();
        if(roofCopy.getSourceX() >=0 && roofCopy.getSourceX() < BLOCK_WIDTH && roofCopy.getSourceY() >= 0 && roofCopy.getSourceY() < BLOCK_HEIGHT) {
            g2.drawRect(roofCopy.getSourceX()*PIXEL_WIDTH+3,roofCopy.getSourceY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        }
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRect(roofCopy.getDestX()*PIXEL_WIDTH + 3, roofCopy.getDestY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
    }
    
    private void drawMapWarps(Graphics2D g2) {
        MapBlock[] blocks = map.getLayout().getBlockset().getBlocks();
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(1));
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                int itemFlag = blocks[x+y*BLOCK_WIDTH].getFlags()&0x3C00;
                if (itemFlag == 0x1000)
                    g2.drawImage(MapLayoutFlagImages.getWarpImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
            }
        }
    }
    
    private void drawMapWarp(Graphics2D g2, MapWarpEvent warp, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.CYAN);
        if (warp.getTriggerX() == 0xFF || warp.getTriggerY() == 0xFF) {
            MapArea mainArea = map.getAreas()[0];
            int x, w, y, h;
            if (warp.getTriggerX()==0xFF) {
                x = mainArea.getLayer1StartX();
                w = mainArea.getLayer1EndX()-x+1;
            } else {
                x = warp.getTriggerX();
                w = 1;
            }
            if (warp.getTriggerY()==0xFF) {
                y = mainArea.getLayer1StartY();
                h = mainArea.getLayer1EndY()-y+1;
            } else {
                y = warp.getTriggerY();
                h = 1;
            }
            g2.drawRect(x*PIXEL_WIDTH, y*PIXEL_HEIGHT, w*PIXEL_WIDTH, h*PIXEL_HEIGHT);
        } else {
            g2.drawImage(MapLayoutFlagImages.getWarpImage(), warp.getTriggerX()*PIXEL_WIDTH, warp.getTriggerY()*PIXEL_HEIGHT, null);
        }
        if (warp.getDestMap().equals("MAP_CURRENT")) {
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.BLUE);
            GraphicsHelpers.drawArrowLine(g2, warp.getTriggerX()*PIXEL_WIDTH+12, warp.getTriggerY()*PIXEL_HEIGHT+12, warp.getDestX()*PIXEL_WIDTH+12, warp.getDestY()*PIXEL_HEIGHT+12);
        }
    }    

    private void drawMapItems(Graphics2D g2) {
        g2.setStroke(new BasicStroke(3));
        MapBlock[] blocks = map.getLayout().getBlockset().getBlocks();
        for (int y=0;y< BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                int itemFlag = blocks[x+y*BLOCK_WIDTH].getFlags()&0x3C00;
                g2.drawImage(MapLayoutFlagImages.getBlockItemFlagImage(itemFlag), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
            }
        }
    }
        
    private void drawMapTriggers(Graphics2D g2) {
        MapBlock[] blocks = map.getLayout().getBlockset().getBlocks();
        for (int y=0;y< BLOCK_HEIGHT; y++) {
            for (int x=0;x < BLOCK_WIDTH; x++) {
                int itemFlag = blocks[x+y*BLOCK_WIDTH].getFlags()&0x3C00;
                if (itemFlag == 0x1400)
                    g2.drawImage(MapLayoutFlagImages.getTriggerImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
            }
        }
    }
    
    private void drawSelected(Graphics2D g2) {
        if (selectedItemIndex == -1) return;
    }

    private void onMouseButtonInput(BaseMouseCoordsComponent.GridMousePressedEvent evt) {
        if (!isOnActionsTab)
            return;
        int x = evt.x();
        int y = evt.y();
        switch (currentMode) {
            case MODE_BLOCK:
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
                                            int flags = (0xC000 & copiedBlocks[j][i].getFlags()) + (0x3C00 & origFlags);
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
                break;
            case MODE_OBSTRUCTED:
                switch (evt.mouseButton()) {
                    case MouseEvent.BUTTON1:
                        int flagVal = 0x0000;    
                        switch (evt.mouseButton()) {
                            case MODE_OBSTRUCTED: flagVal = 0xC000; break;
                            case MODE_STAIRS: flagVal = 0x4000; break;
                            case MODE_WARP: flagVal = 0x1000; break;
                            case MODE_BARREL: flagVal = 0x1000; break;
                            case MODE_VASE: flagVal = 0x2C00; break;
                            case MODE_TABLE: flagVal = 0x2800; break;
                            case MODE_TRIGGER: flagVal = 0x1400; break;
                        }
                        setFlagValue(x, y, flagVal);
                        break;
                    case MouseEvent.BUTTON2:
                        clearFlagValue(x, y);
                        break;
                    case MouseEvent.BUTTON3:
                        setFlagValue(x, y, 0x0000);
                        break;
                }
                break;
        }
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
        if(block.getFlags()!=value){
            int[] action = new int[3];
            action[0] = ACTION_CHANGE_BLOCK_FLAGS;
            action[1] = x+y*BLOCK_WIDTH;
            int origFlags = block.getFlags();
            action[2] = origFlags;
            int newFlags = (0xC000 & value) + (0x3C00 & origFlags);
            block.setFlags(newFlags);
            actions.add(action);
            redraw();
        }
    }
    
    public void clearFlagValue(int x, int y){
        MapBlock[] blocks = layout.getBlockset().getBlocks();
        MapBlock block = blocks[x+y*BLOCK_WIDTH];
        if(block.getFlags()!=0){
            int[] action = new int[3];
            action[0] = ACTION_CHANGE_BLOCK_FLAGS;
            action[1] = x+y*BLOCK_WIDTH;
            int origFlags = block.getFlags();
            action[2] = origFlags;
            int newFlags = 0;
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
    }

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
            case MODE_OBSTRUCTED:
            case MODE_STAIRS:
                return (DRAW_MODE_EXPLORATION_FLAGS & drawFlag) > 0;
            case MODE_BARREL:
            case MODE_VASE:
            case MODE_TABLE:
                return (DRAW_MODE_ITEMS & drawFlag) > 0;
            case MODE_WARP:
                return (DRAW_MODE_WARPS & drawFlag) > 0;
            case MODE_TRIGGER:
                return (DRAW_MODE_TRIGGERS & drawFlag) > 0;
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
}
