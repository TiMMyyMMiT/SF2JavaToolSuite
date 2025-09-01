/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.layout.gui;

import com.sfc.sf2.core.gui.layout.*;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.block.gui.BlockSlotPanel;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_WIDTH;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wiz
 */
public class EditableMapLayoutPanel extends StaticMapLayoutPanel {
        
    private static final int ACTION_CHANGE_BLOCK_VALUE = 0;
    private static final int ACTION_CHANGE_BLOCK_FLAGS = 1;
    private static final int ACTION_MASS_COPY = 2;
    
    public static final int MODE_BLOCK = 0;
    public static final int MODE_OBSTRUCTED = 1;
    public static final int MODE_STAIRS = 2;
    
    MapBlockset mapBlockset;

    private int currentMode = 0;
    
    BlockSlotPanel leftSlot = null;
    private MapBlock selectedBlockLeft;
    MapBlock[][] copiedBlocks;
    
    private List<int[]> actions = new ArrayList<int[]>();

    public EditableMapLayoutPanel() {
        super();
        mouseInput = new LayoutMouseInput(this, this::onMousePressed, PIXEL_WIDTH, PIXEL_HEIGHT);
    }
    
    private void updateLeftSlot(MapBlock block) {
        leftSlot.setBlock(block);
        leftSlot.revalidate();
        leftSlot.repaint(); 
    }
    
    public void setBlockValue(int x, int y, int value) {
        MapBlock[] blocks = layout.getBlockset().getBlocks();
        MapBlock block = blocks[x+y*BLOCK_WIDTH];
        if (block.getIndex()!=value) {
            int[] action = new int[3];
            action[0] = ACTION_CHANGE_BLOCK_VALUE;
            action[1] = y*BLOCK_WIDTH+x;
            action[2] = block.getIndex();
            block.setIndex(value);
            block.clearIndexedColorImage(false);
            block.setTiles(mapBlockset.getBlocks()[block.getIndex()].getTiles());
            actions.add(action);
            redraw();
        }
    }
    
    public void setFlagValue(int x, int y, int value) {
        MapBlock[] blocks = layout.getBlockset().getBlocks();
        MapBlock block = blocks[x+y*BLOCK_WIDTH];
        if (block.getFlags()!=value) {
            int[] action = new int[3];
            action[0] = ACTION_CHANGE_BLOCK_FLAGS;
            action[1] = y*BLOCK_WIDTH+x;
            int origFlags = block.getFlags();
            action[2] = origFlags;
            int newFlags = (0xC000 & value) + (0x3C00 & origFlags);
            block.setFlags(newFlags);
            block.setExplorationFlagImage(null);
            actions.add(action);
            redraw();
        }
    }
    
    public void revertLastAction() {
        if (actions.size() == 0) return;
        int[] action = actions.get(actions.size()-1);
        switch (action[0]) {
            case ACTION_CHANGE_BLOCK_VALUE:
            {
                MapBlock block = layout.getBlockset().getBlocks()[action[1]];
                block.setIndex(action[2]);
                block.clearIndexedColorImage(false);
                block.setTiles(mapBlockset.getBlocks()[block.getIndex()].getTiles());
                actions.remove(actions.size()-1);
                redraw();
                this.repaint();
                break;
            }
            case ACTION_CHANGE_BLOCK_FLAGS:
            {
                MapBlock block = layout.getBlockset().getBlocks()[action[1]];
                block.setFlags(action[2]);               
                block.setExplorationFlagImage(null);
                actions.remove(actions.size()-1);
                redraw();
                this.repaint();
                break;
            }
            case ACTION_MASS_COPY:
            {
                int blockIndex = action[1];
                int width = action[2];
                int height = action[3];
                for (int j=0; j < height; j++) {
                    for (int i=0; i < width; i++) {
                        int value = action[4+2 * (i+j*width)];
                        int flags = action[4+2 * (i+j*width)+1];
                        if (value != -1 && flags != -1) {
                            MapBlock block = new MapBlock(value, flags, mapBlockset.getBlocks()[value].getTiles());
                            layout.getBlockset().getBlocks()[blockIndex+j*64+i] = block;
                        }
                    }
                }   actions.remove(actions.size()-1);
                redraw();
                this.repaint();
                break;
            }
            default:
                break;
        }
    }
    public MapBlockset getMapBlockset() {
        return mapBlockset;
    }

    public void setMapBlockset(MapBlockset mapBlockset) {
        this.mapBlockset = mapBlockset;
    }

    public int getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(int currentMode) {
        this.currentMode = currentMode;
    }

    public MapBlock getSelectedBlockLeft() {
        return selectedBlockLeft;
    }

    public void setSelectedBlockLeft(MapBlock selectedBlockLeft) {
        this.selectedBlockLeft = selectedBlockLeft;
    }

    public BlockSlotPanel getLeftSlot() {
        return leftSlot;
    }

    public void setLeftSlot(BlockSlotPanel leftSlot) {
        this.leftSlot = leftSlot;
    }

    public List<int[]> getActions() {
        return actions;
    }

    public void setActions(List<int[]> actions) {
        this.actions = actions;
    }
    
    private void onMousePressed(BaseMouseCoordsComponent.GridMousePressedEvent evt) {
        /*int x = e.getX() / (displaySize * 3*8);
        int y = e.getY() / (displaySize * 3*8);
        switch (currentMode) {
            case MODE_BLOCK :
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        if(MapBlocksetLayoutPanel.selectedBlockIndexLeft!=-1){
                            setBlockValue(x, y, MapBlocksetLayoutPanel.selectedBlockIndexLeft);
                            if(selectedBlockLeft!=null && selectedBlockLeft.getIndex()==MapBlocksetLayoutPanel.selectedBlockIndexLeft){
                                setFlagValue(x, y, selectedBlockLeft.getFlags());
                            }
                        }else{
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
                                        MapBlock previousBlock = layout.getBlockset().getBlocks()[blockIndex+j*64+i];
                                        action[4+2*(j*width+i)] = previousBlock.getIndex();
                                        int origFlags = previousBlock.getFlags();
                                        action[4+2*(j*width+i)+1] = origFlags;
                                        MapBlock modelBlock = copiedBlocks[j][i];
                                        MapBlock newBlock = new MapBlock(modelBlock.getIndex(), (0xC000 & modelBlock.getFlags()) + (0x3C00 & origFlags), modelBlock.getTiles());
                                        layout.getBlockset().getBlocks()[blockIndex+j*64+i] = newBlock;
                                    }else{
                                        action[4+2*(j*width+i)] = -1;
                                        action[4+2*(j*width+i)+1] = -1;
                                    }
                                }
                            }
                            actions.add(action);
                            redraw = true;
                        }
                        break;
                    case MouseEvent.BUTTON2:
                        lastMapX = x;
                        lastMapY = y;
                        break;
                    case MouseEvent.BUTTON3:
                        setBlockValue(x, y, MapBlocksetLayoutPanel.selectedBlockIndexRight);
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
                        setFlagValue(x, y, 0x0000);;
                        break;
                    case MouseEvent.BUTTON3:
                        setFlagValue(x, y, 0x0000);;
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
                        setFlagValue(x, y, 0x0000);;
                        break;
                    case MouseEvent.BUTTON3:
                        setFlagValue(x, y, 0x8000);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        this.repaint();
        //System.out.println("Map press "+e.getButton()+" "+x+" - "+y);
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX() / (displaySize * 3*8);
        int y = e.getY() / (displaySize * 3*8);
        switch (e.getButton()) {
            case MouseEvent.BUTTON2:
                if(currentMode==MODE_BLOCK){                
                    if(x==lastMapX && y==lastMapY){
                        selectedBlockLeft = layout.getBlockset().getBlocks()[y*64+x];
                        MapBlocksetLayoutPanel.selectedBlockIndexLeft = selectedBlockLeft.getIndex();
                        updateLeftSlot(selectedBlockLeft);
                    }else{
                        /* Mass copy */
                        /*int xStart;
                        int xEnd;
                        int yStart;
                        int yEnd;
                        if(x>lastMapX){
                            xStart = lastMapX;
                            xEnd = x;
                        }else{
                            xStart = x;
                            xEnd = lastMapX;
                        }
                        if(y>lastMapY){
                            yStart = lastMapY;
                            yEnd = y;
                        }else{
                            yStart = y;
                            yEnd = lastMapY;
                        }
                        int width = xEnd - xStart + 1;
                        int height = yEnd - yStart + 1;
                        copiedBlocks = new MapBlock[height][width];
                        for(int j=0;j<height;j++){
                            for(int i=0;i<width;i++){
                                copiedBlocks[j][i] = layout.getBlockset().getBlocks()[(yStart+j)*64+xStart+i];
                            }
                        }

                        MapBlocksetLayoutPanel.selectedBlockIndexLeft = -1;

                        leftSlot.setBlock(null);
                        leftSlot.revalidate();
                        leftSlot.repaint(); 

                    }
                }

                break;
            default:
                break;
        }  */       
    }
}
