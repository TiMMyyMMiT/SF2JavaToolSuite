/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.gui;

import com.sfc.sf2.battle.mapcoords.layout.BattleMapCoordsLayout;
import com.sfc.sf2.battle.mapterrain.BattleMapTerrain;
import com.sfc.sf2.core.gui.layout.BaseMouseCoordsComponent.GridMousePressedEvent;
import com.sfc.sf2.core.gui.layout.LayoutMouseInput;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

/**
 *
 * @author TiMMy
 */
public class BattleMapTerrainLayoutPanel extends BattleMapCoordsLayout {
    private static Color DARKEN = new Color(0, 0, 0, 50);
    private static Color TEXT_DARKEN = new Color(0, 0, 0, 150);
    
    protected BattleMapTerrain terrain;
    protected boolean drawTerrain;
    protected boolean drawTerrainAsText;
    
    private final ImageIcon[] terrainIcons;
    private byte selectedTerrainType;
    
    public BattleMapTerrainLayoutPanel() {
        super();
        mouseInput = new LayoutMouseInput(this, this::onMouseInteraction, PIXEL_WIDTH, PIXEL_HEIGHT);
        
        terrainIcons = new ImageIcon[10];
        ClassLoader loader = getClass().getClassLoader();
        terrainIcons[0] = new ImageIcon(loader.getResource("terrain/icons/XX_Obstructed.png"));
        terrainIcons[1] = new ImageIcon(loader.getResource("terrain/icons/00_Wall.png"));
        terrainIcons[2] = new ImageIcon(loader.getResource("terrain/icons/01_Plains.png"));
        terrainIcons[3] = new ImageIcon(loader.getResource("terrain/icons/02_Path.png"));
        terrainIcons[4] = new ImageIcon(loader.getResource("terrain/icons/03_Grass.png"));
        terrainIcons[5] = new ImageIcon(loader.getResource("terrain/icons/04_Forest.png"));
        terrainIcons[6] = new ImageIcon(loader.getResource("terrain/icons/05_Hills.png"));
        terrainIcons[7] = new ImageIcon(loader.getResource("terrain/icons/06_Desert.png"));
        terrainIcons[8] = new ImageIcon(loader.getResource("terrain/icons/07_Mountain.png"));
        terrainIcons[9] = new ImageIcon(loader.getResource("terrain/icons/08_Water.png"));
    }
    
    @Override
    protected void drawImage(Graphics graphics) {
        super.drawImage(graphics);
        if (!drawTerrain) return;
        
        byte[] data = terrain.getData();
        int coordsX = battleCoords.getX();
        int coordsY = battleCoords.getY();
        int width = battleCoords.getWidth();
        int height = battleCoords.getHeight();
        
        graphics.setColor(DARKEN);
        graphics.fillRect(coordsX*PIXEL_WIDTH, coordsY*PIXEL_HEIGHT, width*PIXEL_WIDTH, width*PIXEL_HEIGHT);
        graphics.setColor(Color.BLACK);
        if (drawTerrainAsText) {
            drawTerrainText(graphics, data, coordsX, coordsY, width, height);
        } else {
            drawTerrainIcons(graphics, data, coordsX, coordsY, width, height);
        }
    }
    
    private void drawTerrainIcons(Graphics graphics, byte[] data, int coordsX, int coordsY, int width, int height) {
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                int x = (coordsX+i)*PIXEL_WIDTH;
                int y = (coordsY+j)*PIXEL_HEIGHT;
                int value = data[i+j*48]+1;
                graphics.fillRect(x+6, y+6, PIXEL_WIDTH-12, PIXEL_HEIGHT-12);
                graphics.drawImage(terrainIcons[value].getImage(), x+8, y+8, null);
            }
        }
    }
    
    private void drawTerrainText(Graphics graphics, byte[] data, int coordsX, int coordsY, int width, int height) {
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                int x = (coordsX+i)*PIXEL_WIDTH;
                int y = (coordsY+j)*PIXEL_HEIGHT;
                int value = data[i+j*48];
                graphics.setColor(TEXT_DARKEN);
                graphics.fillRect(x+6, y+5, PIXEL_WIDTH-12, PIXEL_HEIGHT-11);
                graphics.setColor(Color.WHITE);
                if (value < 0) {
                    graphics.drawString(Integer.toString(value), x+8, y+16);
                } else {
                    graphics.drawString(Integer.toString(value), x+9, y+16);
                }
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
        if (this.drawTerrain != drawTerrain) {
            this.drawTerrain = drawTerrain;
            redraw();
        }
    }

    public void setDrawTerrainText(boolean drawTerrainAsText) {
        if (this.drawTerrainAsText != drawTerrainAsText) {
            this.drawTerrainAsText = drawTerrainAsText;
            redraw();
        }
    }

    public void setSelectedTerrainType(int selectedTerrainType) {
        this.selectedTerrainType = (byte)(selectedTerrainType);
    }
    
    private void onMouseInteraction(GridMousePressedEvent evt) {
        if (evt.mouseButton() != MouseEvent.BUTTON1) return;
        if (battleCoords == null) return;
        int x = evt.x();
        int y = evt.y();
        int startX = battleCoords.getX();
        int startY = battleCoords.getY();
        int width = battleCoords.getWidth();
        int height = battleCoords.getHeight();
        if (((x < startX || x > startX+width) || y < startY) || y > startY+height) {
            return;
        }
        x -= startX;
        y -=startY;
        if (terrain.getData()[x+y*48] != selectedTerrainType) {
            terrain.getData()[x+y*48] = selectedTerrainType;
            redraw();
        }
    }
}
