/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.gui;

import com.sfc.sf2.map.Map;
import com.sfc.sf2.map.MapArea;
import com.sfc.sf2.map.MapFlagCopy;
import com.sfc.sf2.map.MapRoofCopy;
import com.sfc.sf2.map.MapStepCopy;
import com.sfc.sf2.map.MapWarp;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.gui.BlockSlotPanel;
import com.sfc.sf2.map.block.gui.MapBlocksetLayoutPanel;
import com.sfc.sf2.map.layout.gui.resources.MapLayoutFlagImages;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    
    private MapBlock selectedBlock0;
    MapBlock[][] copiedBlocks;
    private int copiedBlocksStartX = -1;
    private int copiedBlocksStartY = -1;
    private int copiedBlocksDrawX = -1;
    private int copiedBlocksDrawY = -1;
    
    private List<int[]> actions = new ArrayList<int[]>();
    
    private Map map;

    public MapLayoutPanel() {
        super();
        //MapBlockLayoutPanel.setLeftSelectedIndex(-1);
        //MapBlockLayoutPanel.setRightSelectedIndex(-1);
    }

    @Override
    protected void drawImage(Graphics graphics) {
        super.drawImage(graphics);
        
        if (shouldDraw(DRAW_MODE_AREAS)) {
            drawMapAreasImage(graphics);
        }
        if (shouldDraw(DRAW_MODE_FLAG_COPIES)) {
            drawMapFlagCopies(graphics);
        }
        if (shouldDraw(DRAW_MODE_STEP_COPIES)) {
            drawMapStepCopiesImage(graphics);
        }
        if (shouldDraw(DRAW_MODE_ROOF_COPIES)) {
            drawMapRoofCopiesImage(graphics);
        }
        if (shouldDraw(DRAW_MODE_WARPS)) {
            drawMapWarpsImage(graphics);
        }
        if (shouldDraw(DRAW_MODE_ITEMS)) {
            drawMapItemsImage(graphics);
        }
        if (shouldDraw(DRAW_MODE_TRIGGERS)) {
            drawMapTriggersImage(graphics);
        }
    }
    
    /*private BufferedImage buildPreviewImage() {
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
                    preview = new BufferedImage(width*24, height*24, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D graphics = (Graphics2D)preview.getGraphics();
                    graphics.setColor(Color.YELLOW);
                    graphics.setStroke(new BasicStroke(2));
                    graphics.drawRect(0, 0, width*24, height*24);
                    overlayRect = false;
                } else if (copiedBlocks != null) {
                    overlayRect = true;
                    preview = new BufferedImage(copiedBlocks[0].length*24, copiedBlocks.length*24, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D graphics = (Graphics2D)preview.getGraphics();
                    graphics.setColor(Color.WHITE);
                    for (int i = 0; i < copiedBlocks.length; i++) {
                        for (int j = 0; j < copiedBlocks[i].length; j++) {
                            graphics.drawImage(copiedBlocks[i][j].getIndexedColorImage(), j*24, i*24, null);
                        }
                    }
                } else if (MapBlockLayoutPanel.selectedBlockIndex0 == -1) {
                    //No block
                    previewImage = null;
                    previewIndex = -1;
                } else {
                    //Block selected
                    previewIndex = MapBlockLayoutPanel.selectedBlockIndex0;
                    selectedBlock0 = blockset[previewIndex];
                    //"layout" is not MapBlockLayout. How to get that?
                    if (selectedBlock0 != null) {
                        preview = selectedBlock0.getIndexedColorImage();
                    }
                }
                break;
            case MODE_OBSTRUCTED :
                overlayRect = true;
                preview = getObstructedImage();
                break;
            case MODE_STAIRS :
                overlayRect = true;
                preview = getLeftUpstairsImage();
                break;
            case MODE_WARP :
                preview = getWarpImage();
                break;
            case MODE_BARREL :
                overlayRect = true;
                preview = getBarrelImage();
                break;
            case MODE_VASE :
                overlayRect = true;
                preview = getVaseImage();
                break;
            case MODE_TABLE :
                overlayRect = true;
                preview = getTableImage();
                break;
            case MODE_TRIGGER :
                preview = getTriggerImage();
                break;
            default:
                preview = null;
                break;
        }
        
        if (preview != null || overlayRect) {
            if (preview == null) {
                previewImage = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
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
            previewImage = resize(previewImage);
        }
        
        return previewImage;
    }*/
    
    private void drawMapFlagCopies(Graphics graphics) {   
        Graphics2D g2 = (Graphics2D)graphics;
        g2.setStroke(new BasicStroke(3));
        for (MapFlagCopy flagCopy : map.getFlagCopies()) {
            g2.setColor(Color.CYAN);
            int width = flagCopy.getWidth();
            int heigth = flagCopy.getHeight();
            g2.drawRect(flagCopy.getSourceX()*24 + 3,flagCopy.getSourceY()*24+3, width*24-6, heigth*24-6);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRect(flagCopy.getDestX()*24 + 3, flagCopy.getDestY()*24+3, width*24-6, heigth*24-6);
        }
    }
    
    private void drawMapStepCopiesImage(Graphics graphics) {   
        Graphics2D g2 = (Graphics2D)graphics;
        g2.setStroke(new BasicStroke(3));
        for (MapStepCopy stepCopy : map.getStepCopies()) {
            g2.setColor(Color.WHITE);
            g2.drawRect(stepCopy.getTriggerX()*24,stepCopy.getTriggerY()*24, 24, 24);
            g2.setColor(Color.CYAN);
            int width = stepCopy.getWidth();
            int heigth = stepCopy.getHeight();
            g2.drawRect(stepCopy.getSourceX()*24 + 3,stepCopy.getSourceY()*24+3, width*24-6, heigth*24-6);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRect(stepCopy.getDestX()*24 + 3, stepCopy.getDestY()*24+3, width*24-6, heigth*24-6);
        }
    }
    
    private void drawMapRoofCopiesImage(Graphics graphics) {   
        Graphics2D g2 = (Graphics2D)graphics;
        g2.setStroke(new BasicStroke(3));
        for (MapRoofCopy roofCopy : map.getRoofCopies()) {
            g2.setColor(Color.WHITE);
            g2.drawRect(roofCopy.getTriggerX()*24,roofCopy.getTriggerY()*24, 24, 24);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRect(roofCopy.getTriggerX()*24,(roofCopy.getTriggerY()+1)*24, 24, 24);
            g2.setColor(Color.CYAN);
            int width = roofCopy.getWidth();
            int heigth = roofCopy.getHeight();
            if(roofCopy.getSourceX()>=0 && roofCopy.getSourceX()<64 && roofCopy.getSourceY()>=0 && roofCopy.getSourceY()<64){
                g2.drawRect(roofCopy.getSourceX()*24 + 3,roofCopy.getSourceY()*24+3, width*24-6, heigth*24-6);
            }
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRect(roofCopy.getDestX()*24 + 3, roofCopy.getDestY()*24+3, width*24-6, heigth*24-6);
        }
    }
    
    private void drawMapWarpsImage(Graphics graphics) {   
        Graphics2D g2 = (Graphics2D)graphics;
        MapBlock[] blocks = map.getLayout().getBlockset().getBlocks();
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(1));
        for (int y=0; y < 64; y++) {
            for (int x=0; x < 64; x++) {
                int itemFlag = blocks[y*64+x].getFlags()&0x3C00;
                if (itemFlag == 0x1000)
                    g2.drawImage(MapLayoutFlagImages.getWarpImage(),x*24,y*24, null);
            }
        }
        g2.setStroke(new BasicStroke(3));
        for (MapWarp warp : map.getWarps()) {
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
                g2.drawRect(x*24,y*24, w*24, h*24);
            }else {
                g2.drawImage(MapLayoutFlagImages.getWarpImage(), warp.getTriggerX()*24, warp.getTriggerY()*24, null);
            }

            if (warp.getDestMap().equals("MAP_CURRENT")) {
                g2.setStroke(new BasicStroke(1));
                g2.setColor(Color.BLUE);
                g2.drawLine(warp.getTriggerX()*24+12, warp.getTriggerY()*24+12, warp.getDestX()*24+12, warp.getDestY()*24+12);
            }
        }
    }    

    private void drawMapItemsImage(Graphics graphics) {   
        Graphics2D g2 = (Graphics2D)graphics;
        g2.setStroke(new BasicStroke(3));
        MapBlock[] blocks = map.getLayout().getBlockset().getBlocks();
        for (int y=0;y< 64; y++) {
            for (int x=0; x < 64; x++) {
                int itemFlag = blocks[y*64+x].getFlags()&0x3C00;
                g2.drawImage(MapLayoutFlagImages.getBlockItemFlagImage(itemFlag),x*24,y*24, null);
            }
        }
    }
        
    private void drawMapTriggersImage(Graphics graphics) {   
        Graphics2D g2 = (Graphics2D)graphics;
        MapBlock[] blocks = map.getLayout().getBlockset().getBlocks();
        for(int y=0;y< 64; y++) {
            for(int x=0;x < 64; x++) {
                int itemFlag = blocks[y*64+x].getFlags()&0x3C00;
                if (itemFlag==0x1400)
                    g2.drawImage(MapLayoutFlagImages.getTriggerImage(),x*24,y*24, null);
            }
        }
    }
    
    private void drawMapAreasImage(Graphics graphics) {   
        Graphics2D g2 = (Graphics2D)graphics;
        g2.setStroke(new BasicStroke(3));
        for (MapArea area : map.getAreas()) { 
            g2.setColor(Color.WHITE);
            int width = area.getLayer1EndX() - area.getLayer1StartX() + 1;
            int heigth = area.getLayer1EndY() - area.getLayer1StartY() + 1;
            g2.drawRect(area.getLayer1StartX()*24 + 3, area.getLayer1StartY()*24+3, width*24-6, heigth*24-6);
            g2.setColor(Color.LIGHT_GRAY);
            if (area.getForegroundLayer2StartX()!=0 || area.getForegroundLayer2StartY() != 0) {
                g2.drawRect((area.getLayer1StartX() + area.getForegroundLayer2StartX())*24 + 3, (area.getLayer1StartY() + area.getForegroundLayer2StartY())*24+3, width*24-6, heigth*24-6);
            }
            if (area.getBackgroundLayer2StartX()!=0 || area.getBackgroundLayer2StartY() != 0) {
                g2.drawRect(area.getBackgroundLayer2StartX()*24 + 3, area.getBackgroundLayer2StartY()*24+3, width*24-6, heigth*24-6);
            }
        }
    }
    
    /*public void mousePressed(MouseEvent e) {
        if (!isOnActionsTab)
            return;
        int x = e.getX() / (currentDisplaySize * 3*8);
        int y = e.getY() / (currentDisplaySize * 3*8);
        switch (currentMode) {
            case MODE_BLOCK :
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        if(MapBlockLayout.selectedBlockIndex0!=-1){
                            setBlockValue(x, y, MapBlockLayout.selectedBlockIndex0);
                            if(selectedBlock0!=null && selectedBlock0.getIndex()==MapBlockLayout.selectedBlockIndex0){
                                setFlagValue(x, y, selectedBlock0.getFlags());
                            }
                        }else if (copiedBlocks != null) {
                            int height = copiedBlocks.length;
                            int width = copiedBlocks[0].length;
                            int[] action = new int[4+2*height*width];
                            action[0] = ACTION_MASS_COPY;
                            int blockIndex = y*64+x;
                            action[1] = blockIndex;
                            action[2] = width;
                            action[3] = height;
                            for(int j=0;j<height;j++){
                                for(int i=0;i<width;i++){
                                    if((blockIndex+j*64+i)<4096 && ((blockIndex%64)+i)<64){
                                        MapBlock previousBlock = layout.getBlocks()[blockIndex+j*64+i];
                                        action[4+2*(j*width+i)] = previousBlock.getIndex();
                                        int origFlags = previousBlock.getFlags();
                                        action[4+2*(j*width+i)+1] = origFlags;
                                        MapBlock newBlock = new MapBlock();
                                        MapBlock modelBlock = copiedBlocks[j][i];
                                        newBlock.setIndex(modelBlock.getIndex());
                                        newBlock.setFlags((0xC000 & modelBlock.getFlags()) + (0x3C00 & origFlags));
                                        newBlock.setTiles(modelBlock.getTiles());
                                        layout.getBlocks()[blockIndex+j*64+i] = newBlock; 
                                    }else{
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
                        setBlockValue(x, y, MapBlockLayout.selectedBlockIndex1);
                        break;
                    default:
                        break;
                } 
                break;
            case MODE_OBSTRUCTED :
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        setFlagValue(x, y, 0xC000);
                        break;
                    case MouseEvent.BUTTON2:
                        clearFlagValue(x, y);
                        break;
                    case MouseEvent.BUTTON3:
                        setFlagValue(x, y, 0x0000);
                        break;
                    default:
                        break;
                }
                break;
            case MODE_STAIRS :
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        setFlagValue(x, y, 0x4000);
                        break;
                    case MouseEvent.BUTTON2:
                        setFlagValue(x, y, 0x0000);
                        break;
                    case MouseEvent.BUTTON3:
                        setFlagValue(x, y, 0x8000);
                        break;
                    default:
                        break;
                }
                break;
            case MODE_WARP :
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        map.setActionFlag(x, y, 0x1000);
                        this.mapWarpsImage = null;
                        this.redraw();
                        break;
                    case MouseEvent.BUTTON2:
                        clearFlagValue(x, y);
                        this.mapWarpsImage = null;
                        this.redraw();
                        break;
                    case MouseEvent.BUTTON3:
                        map.setActionFlag(x, y, 0x0000);
                        this.mapWarpsImage = null;
                        this.redraw();
                        break;
                    default:
                        break;
                }
                break;
            case MODE_BARREL :
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        map.setActionFlag(x, y, 0x3000);
                        this.mapItemsImage = null;
                        this.redraw();
                        break;
                    case MouseEvent.BUTTON2:
                        clearFlagValue(x, y);
                        this.mapItemsImage = null;
                        this.redraw();
                        break;
                    case MouseEvent.BUTTON3:
                        map.setActionFlag(x, y, 0x0000);
                        this.mapItemsImage = null;
                        this.redraw();
                        break;
                    default:
                        break;
                }
                break;
            case MODE_VASE :
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        map.setActionFlag(x, y, 0x2C00);
                        this.mapItemsImage = null;
                        this.redraw();
                        break;
                    case MouseEvent.BUTTON2:
                        clearFlagValue(x, y);
                        this.mapItemsImage = null;
                        this.redraw();
                        break;
                    case MouseEvent.BUTTON3:
                        map.setActionFlag(x, y, 0x0000);
                        this.mapItemsImage = null;
                        this.redraw();
                        break;
                    default:
                        break;
                }
                break;
            case MODE_TABLE :
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        map.setActionFlag(x, y, 0x2800);
                        this.mapItemsImage = null;
                        this.redraw();
                        break;
                    case MouseEvent.BUTTON2:
                        clearFlagValue(x, y);
                        this.mapItemsImage = null;
                        this.redraw();
                        break;
                    case MouseEvent.BUTTON3:
                        map.setActionFlag(x, y, 0x0000);
                        this.mapItemsImage = null;
                        this.redraw();
                        break;
                    default:
                        break;
                }
                break;
            case MODE_TRIGGER :
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        map.setActionFlag(x, y, 0x1400);
                        this.mapTtriggersImage = null;
                        this.redraw();
                        break;
                    case MouseEvent.BUTTON2:
                        clearFlagValue(x, y);
                        this.mapTtriggersImage = null;
                        this.redraw();
                        break;
                    case MouseEvent.BUTTON3:
                        map.setActionFlag(x, y, 0x0000);
                        this.mapTtriggersImage = null;
                        this.redraw();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        //System.out.println("Map press "+e.getButton()+" "+x+" - "+y);
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if (!isOnActionsTab)
            return;
        int x = e.getX() / (currentDisplaySize * 3*8);
        int y = e.getY() / (currentDisplaySize * 3*8);
        switch (e.getButton()) {
            case MouseEvent.BUTTON2:
                if(currentMode==MODE_BLOCK){                
                    if(x==copiedBlocksStartX && y==copiedBlocksStartY){
                        selectedBlock0 = layout.getBlocks()[y*64+x];
                        MapBlockLayout.selectedBlockIndex0 = selectedBlock0.getIndex();
                        updateLeftSlot(selectedBlock0);
                    }else{
                        // Mass copy
                        int xStart;
                        int xEnd;
                        int yStart;
                        int yEnd;
                        if(x>copiedBlocksStartX){
                            xStart = copiedBlocksStartX;
                            xEnd = x;
                        }else{
                            xStart = x;
                            xEnd = copiedBlocksStartX;
                        }
                        if(y>copiedBlocksStartY){
                            yStart = copiedBlocksStartY;
                            yEnd = y;
                        }else{
                            yStart = y;
                            yEnd = copiedBlocksStartY;
                        }
                        int width = xEnd - xStart + 1;
                        int height = yEnd - yStart + 1;
                        copiedBlocks = new MapBlock[height][width];
                        for(int j=0;j<height;j++){
                            for(int i=0;i<width;i++){
                                copiedBlocks[j][i] = layout.getBlocks()[(yStart+j)*64+xStart+i];
                            }
                        }

                        MapBlockLayout.selectedBlockIndex0 = -1;
                        
                        BufferedImage img = new BufferedImage(3*8,3*8,BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2 = (Graphics2D) img.getGraphics();
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
                break;
                
            default:
                break;
        }         
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX() / (currentDisplaySize * 3*8);
        int y = e.getY() / (currentDisplaySize * 3*8);
        
        if(x!=lastMouseX||y!=lastMouseY){
            if (copiedBlocksStartX >= 0) {
                previewImage = null;
                lastMapX = x;
                lastMapY = y;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX() / (currentDisplaySize * 3*8);
        int y = e.getY() / (currentDisplaySize * 3*8);
        
        if(x!=lastMouseX||y!=lastMouseY){
            lastMouseX=x;
            lastMouseY=y;
            titledBorder = (TitledBorder)(titledPanel.getBorder());
            titledBorder.setTitle("Cursor : "+x+","+y);
            //System.out.println("New cursor pos : "+x+","+y);
                     
            lastMapX = x;
            lastMapY = y;
        }
    }*/
    
    private void updateLeftSlot(MapBlock block){
        leftSlot.setBlock(block);
    }
    
    public void setBlockValue(int x, int y, int value){
        if (value == -1) return;
        MapBlock[] blocks = map.getLayout().getBlockset().getBlocks();
        MapBlock block = blocks[y*64+x];
        if (block.getIndex() != value) {
            int[] action = new int[3];
            action[0] = ACTION_CHANGE_BLOCK_VALUE;
            action[1] = y*64+x;
            action[2] = block.getIndex();
            block.setIndex(value);
            block.clearIndexedColorImage();
            block.setMapTiles(map.getLayout().getBlockset().getBlocks()[block.getIndex()].getMapTiles());
            actions.add(action);
            redraw();
        }
    }
    
    public void setFlagValue(int x, int y, int value){
        MapBlock[] blocks = layout.getBlockset().getBlocks();
        MapBlock block = blocks[y*64+x];
        if(block.getFlags()!=value){
            int[] action = new int[3];
            action[0] = ACTION_CHANGE_BLOCK_FLAGS;
            action[1] = y*64+x;
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
        MapBlock block = blocks[y*64+x];
        if(block.getFlags()!=0){
            int[] action = new int[3];
            action[0] = ACTION_CHANGE_BLOCK_FLAGS;
            action[1] = y*64+x;
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
                            layout.getBlockset().getBlocks()[blockIndex+j*64+i] = block;
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
        return selectedBlock0;
    }

    public void setSelectedBlock0(MapBlock selectedBlock0) {
        this.selectedBlock0 = selectedBlock0;
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
    
    public boolean getIsOnActionsTab() {        
        return isOnActionsTab;
    }
    
    public void setIsOnActionsTab(boolean isOnActionsTab) {
        this.isOnActionsTab = isOnActionsTab;
    }
}
