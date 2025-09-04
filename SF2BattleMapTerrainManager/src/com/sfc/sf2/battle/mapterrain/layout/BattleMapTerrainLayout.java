/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.layout;

import com.sfc.sf2.battle.mapcoords.layout.BattleMapCoordsLayout;
import com.sfc.sf2.battle.mapterrain.BattleMapTerrain;
import com.sfc.sf2.core.gui.layout.BaseMouseCoordsComponent.GridMousePressedEvent;
import com.sfc.sf2.core.gui.layout.LayoutMouseInput;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 *
 * @author wiz
 */
public class BattleMapTerrainLayout extends BattleMapCoordsLayout {
    
    protected BattleMapTerrain terrain;    
    protected boolean drawTerrain = true;
    
    public BattleMapTerrainLayout() {
        super();
        mouseInput = new LayoutMouseInput(this, this::onMouseInteraction, PIXEL_WIDTH, PIXEL_HEIGHT);
    }
    
    @Override
    protected void drawImage(Graphics graphics) {
        super.drawImage(graphics);
        if (!drawTerrain) return;
        
        byte[] data = terrain.getData();
        int x = battleCoords.getX();
        int y = battleCoords.getY();
        int width = battleCoords.getWidth();
        int height = battleCoords.getHeight();
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                int value = data[i+j*48];
                graphics.drawString(String.valueOf(value), (x+i)*3*8+8, (y+j)*3*8+16);
            }
        }
    }

    public BattleMapTerrain getTerrain() {
        return terrain;
    }

    public void setTerrain(BattleMapTerrain terrain) {
        this.terrain = terrain;
        redraw();
    }

    public void setDrawTerrain(boolean drawTerrain) {
        this.drawTerrain = drawTerrain;
        redraw();
    }
    
    private void onMouseInteraction(GridMousePressedEvent evt) {
        int x = evt.x();
        int y = evt.y();
        int startX = battleCoords.getX();
        int startY = battleCoords.getY();
        int width = battleCoords.getWidth();
        int height = battleCoords.getHeight();
        if(x >= startX && x <= startX+width && y >= startY && y <= startY+height) {
            switch (evt.mouseButton()) {
                case MouseEvent.BUTTON1:
                    terrain.getData()[(startY+y)*48+startX+x]++;
                    break;
                case MouseEvent.BUTTON3:
                    terrain.getData()[(startY+y)*48+startX+x]--;
                    break;
                default:
                    break;
            }
            redraw();
        }
        //System.out.println("Map press "+e.getButton()+" "+x+" - "+y);
    }
}
