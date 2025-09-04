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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

/**
 *
 * @author wiz
 */
public class BattleMapTerrainLayoutPanel extends BattleMapCoordsLayout {
    
    protected BattleMapTerrain terrain;    
    protected boolean drawTerrain = true;
    
    private final ImageIcon obstructedIcon;
    private final ImageIcon[] terrainIcons;
    
    public BattleMapTerrainLayoutPanel() {
        super();
        mouseInput = new LayoutMouseInput(this, this::onMouseInteraction, PIXEL_WIDTH, PIXEL_HEIGHT);
        
        terrainIcons = new ImageIcon[9];
        ClassLoader loader = getClass().getClassLoader();
        obstructedIcon = new ImageIcon(loader.getResource("terrain.icons/XX_Obstructed.png"));
        terrainIcons[0] = new ImageIcon(loader.getResource("terrain.icons/00_Sky.png"));
        terrainIcons[1] = new ImageIcon(loader.getResource("terrain.icons/01_Plains.png"));
        terrainIcons[2] = new ImageIcon(loader.getResource("terrain.icons/02_Path.png"));
        terrainIcons[3] = new ImageIcon(loader.getResource("terrain.icons/03_Grass.png"));
        terrainIcons[4] = new ImageIcon(loader.getResource("terrain.icons/04_Forest.png"));
        terrainIcons[5] = new ImageIcon(loader.getResource("terrain.icons/05_Hills.png"));
        terrainIcons[6] = new ImageIcon(loader.getResource("terrain.icons/06_Desert.png"));
        terrainIcons[7] = new ImageIcon(loader.getResource("terrain.icons/07_Mountain.png"));
        terrainIcons[8] = new ImageIcon(loader.getResource("terrain.icons/08_Water.png"));
    }
    
    @Override
    protected void drawImage(Graphics graphics) {
        super.drawImage(graphics);
        if (!drawTerrain) return;
        
        Dimension dims = getImageDimensions();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, dims.width, dims.height);
        byte[] data = terrain.getData();
        int x = battleCoords.getX();
        int y = battleCoords.getY();
        int width = battleCoords.getWidth();
        int height = battleCoords.getHeight();
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                int value = data[i+j*48];
                graphics.drawImage(getIcon(value).getImage(), x, y, this);
                //graphics.drawString(String.valueOf(value), (x+i)*PIXEL_WIDTH+8, (y+j)*PIXEL_HEIGHT+16);
            }
        }
    }
    
    private ImageIcon getIcon(int terrainValue) {
        if (terrainValue < 0 || terrainValue >= terrainIcons.length) {
            return obstructedIcon;
        } else {
            return terrainIcons[terrainValue];
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
