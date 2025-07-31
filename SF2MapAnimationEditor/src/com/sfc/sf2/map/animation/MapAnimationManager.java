/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.animation;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.animation.gui.MapAnimationPanel;
import com.sfc.sf2.map.animation.io.DisassemblyManager;
import com.sfc.sf2.map.animation.io.PngManager;
import com.sfc.sf2.map.layout.DisassemblyException;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.map.layout.MapLayoutManager;

/**
 *
 * @author wiz
 */
public class MapAnimationManager {
       
    private MapLayoutManager mapLayoutManager = new MapLayoutManager();
    private Map map;
    
    public void importDisassembly(String palettesPath, String tilesetsPath, String tilesetsFilePath, String blocksPath, String layoutPath, String animationsPath)
            throws DisassemblyException {
        System.out.println("com.sfc.sf2.map.MapManager.importDisassembly() - Importing disassembly ...");
        map = new Map();
        mapLayoutManager.importDisassembly(palettesPath, tilesetsPath, tilesetsFilePath, blocksPath, layoutPath);
        MapLayout layout = mapLayoutManager.getLayout();
        MapBlock[] blockset = mapLayoutManager.getBlockset();
        map.setLayout(layout);
        map.setBlocks(blockset);        
        MapAnimation animation = DisassemblyManager.importAnimation(animationsPath);
        map.setAnimation(animation);        
        MapAnimationFrame[] animFrames = animation.getFrames();
        int animTileset = animation.getTileset();
        int animLength = animation.getLength();
        for(int i=0;i<animFrames.length;i++){
            int animFrameStart = animFrames[i].getStart();
            int animFrameLength = animFrames[i].getLength();
            int animFrameDest = animFrames[i].getDest();
            mapLayoutManager.importDisassembly(palettesPath, tilesetsPath, tilesetsFilePath, blocksPath, layoutPath, animTileset, animLength, animFrameStart, animFrameLength, animFrameDest);
            //MapLayout layout = mapLayoutManager.getLayout();
            MapBlock[] animFrameBlocks = mapLayoutManager.getLayout().getBlocks();  
            animFrames[i].setBlocks(animFrameBlocks);
        }
        System.out.println("com.sfc.sf2.map.MapManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String animationsPath){
        System.out.println("com.sfc.sf2.map.MapManager.importDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportAnimation(map.getAnimation(), animationsPath);
        System.out.println("com.sfc.sf2.map.MapManager.importDisassembly() - Disassembly exported.");        
    }      
    
    public void exportPng(MapAnimationPanel mapPanel, String filepath){
        System.out.println("com.sfc.sf2.maplayout.MapAnimationEditor.exportPng() - Exporting PNG ...");
        PngManager.exportPng(mapPanel, filepath);
        System.out.println("com.sfc.sf2.maplayout.MapAnimationEditor.exportPng() - PNG exported.");       
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
    
    
    
}
