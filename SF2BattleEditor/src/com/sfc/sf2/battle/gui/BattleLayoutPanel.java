/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.gui;

import com.sfc.sf2.battle.AIPoint;
import com.sfc.sf2.battle.AIRegion;
import com.sfc.sf2.battle.Ally;
import com.sfc.sf2.battle.Battle;
import com.sfc.sf2.battle.Enemy;
import com.sfc.sf2.battle.mapterrain.gui.BattleMapTerrainLayoutPanel;
import com.sfc.sf2.core.gui.layout.BaseMouseCoordsComponent;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.mapsprite.MapSprite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 *
 * @author wiz
 */
public class BattleLayoutPanel extends BattleMapTerrainLayoutPanel {
    
    public static final int SPRITESETMODE_ALLY = 0;
    public static final int SPRITESETMODE_ENEMY = 1;
    public static final int SPRITESETMODE_AIREGION = 2;
    public static final int SPRITESETMODE_AIPOINT = 3;
    
    public static final int MODE_NONE = 0;
    public static final int MODE_TERRAIN = 1;
    public static final int MODE_SPRITE = 2;
    
    protected Battle battle;
    
    protected int currentMode = 0;
    protected int currentSpritesetMode = 0;
    protected int selectedAlly = -1;
    protected int selectedEnemy = -1;
    protected int selectedAIRegion = -1;
    protected int selectedAIPoint = -1;
    protected int applicableTerrainValue = -1;
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    int lastMapX = 0;
    int lastMapY = 0;
    
    protected TitledBorder titledBorder = null;
    protected JPanel titledPanel = null;
    
    protected MapBlock selectedBlockLeft;
    protected MapBlock[][] copiedBlocks;
    
    protected List<int[]> actions = new ArrayList<>();
    
    protected boolean drawSprites = true;
    protected boolean drawAiRegions = true;
    protected boolean drawAiPoints = true;
    
    private final BufferedImage[] mapspriteImages = new BufferedImage[256];
            
    @Override
    public void drawImage(Graphics graphics) {
        Graphics2D g2 = (Graphics2D)graphics;
        if (drawSprites) {
            getSpritesImage(g2);
        }
        if (drawAiRegions) {
            getAiRegionsImage(g2);
        }
        if (drawAiPoints) {
            getAiPointsImage(g2);
        }
    }
    
    private void getSpritesImage(Graphics2D g2) {
        int x = battle.getMapCoords().getX();
        int y = battle.getMapCoords().getY();
        Ally[] allies = battle.getSpriteset().getAllies();
        for (int i = 0; i < allies.length; i++) {
            Ally ally = allies[i];
            Font font = new Font("Courier", Font.BOLD, 16);
            g2.setFont(font);
            int offset = (i + 1 < 10) ? 0 : -4;
            int targetX = (x + ally.getX()) * 3 * 8 + 8 + offset;
            int targetY = (y + ally.getY()) * 3 * 8 + 18;
            String val = String.valueOf(i + 1);
            g2.setColor(Color.BLACK);
            g2.drawString(val, targetX - 1, targetY - 1);
            g2.drawString(val, targetX - 1, targetY + 1);
            g2.drawString(val, targetX + 1, targetY - 1);
            g2.drawString(val, targetX + 1, targetY + 1);
            g2.setColor(Color.YELLOW);
            g2.drawString(val, targetX, targetY);
            if (currentMode == MODE_SPRITE && currentSpritesetMode == SPRITESETMODE_ALLY && i == selectedAlly) {
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect((x + ally.getX()) * 3 * 8, (y + ally.getY()) * 3 * 8, 1 * 24, 1 * 24);
            }
        }
        Enemy[] enemies = battle.getSpriteset().getEnemies();
        for (int i = 0; i < enemies.length; i++) {
            Enemy enemy = enemies[i];
            int targetX = (x + enemy.getX()) * 3 * 8;
            int targetY = (y + enemy.getY()) * 3 * 8;
            int id = enemy.getEnemyData().getID();
            MapSprite sprite = enemy.getEnemyData().getMapSprite();
            if (sprite == null) {
                targetX += 8 + ((enemy.getEnemyData().getID() + 1 < 10) ? 0 : -4);
                targetY += 3;
                g2.setColor(Color.BLACK);
                g2.drawString(String.valueOf(id), targetX - 1, targetY + 16 - 1);
                g2.drawString(String.valueOf(id), targetX - 1, targetY + 16 + 1);
                g2.drawString(String.valueOf(id), targetX + 1, targetY + 16 - 1);
                g2.drawString(String.valueOf(id), targetX + 1, targetY + 16 + 1);
                g2.setColor(Color.RED);
                g2.drawString(String.valueOf(id), targetX, targetY + 16);
            } else {
                if (mapspriteImages[id] == null) {
                    mapspriteImages[id] = sprite.getIndexedColorImage().getSubimage(0, 0, 3 * 8, 3 * 8);
                }
                g2.drawImage(mapspriteImages[id], targetX, targetY, null);
            }
            if (currentMode == MODE_SPRITE && currentSpritesetMode == SPRITESETMODE_ENEMY && i == selectedEnemy) {
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect((x + enemy.getX()) * 3 * 8, (y + enemy.getY()) * 3 * 8, 1 * 24, 1 * 24);
            }
        }
        for (int i = 0; i < enemies.length; i++) {
            Enemy enemy = enemies[i];
            drawAiTargets(g2, x, y, enemy.getX(), enemy.getY(), enemy.getMoveOrder1());
            drawAiTargets(g2, x, y, enemy.getX(), enemy.getY(), enemy.getMoveOrder2());
        }
    }

    private void drawAiTargets(Graphics2D g2, int mapOffsetX, int mapOffsetY, int enemyX, int enemyY, String order) {
        int target = 0;
        int targetX = -1, targetY = -1;
        String[] orderSplit = order.split("\\|");
        if (orderSplit.length > 1) {
            target = Integer.parseInt(orderSplit[1]);
        }
        switch (orderSplit[0]) {
            case "FOLLOW_TARGET":     //Follow target (ally)
                Ally[] allies = battle.getSpriteset().getAllies();
                if (target >= 0 && target < allies.length) {
                    targetX = allies[target].getX();
                    targetY = allies[target].getY();
                }
                break;
            case "FOLLOW_ENEMY":     //Follow enemy
                Enemy[] enemies = battle.getSpriteset().getEnemies();
                if (target >= 0 && target < enemies.length) {
                    targetX = enemies[target].getX();
                    targetY = enemies[target].getY();
                }
                break;
            case "MOVE_TO":
                AIPoint[] points = battle.getSpriteset().getAiPoints();
                if (target >= 0 && target < points.length) {
                    targetX = points[target].getX();
                    targetY = points[target].getY();
                }
                break;
            default:
                break;
        }
        if (targetX >= 0 && targetY >= 0) {
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(1));
            g2.drawLine((mapOffsetX + enemyX) * 3 * 8 + 12, (mapOffsetY + enemyY) * 3 * 8 + 12, (mapOffsetX + targetX) * 3 * 8 + 12, (mapOffsetY + targetY) * 3 * 8 + 12);
        }
    }

    private void getAiRegionsImage(Graphics2D g2) {
        int x = battle.getMapCoords().getX();
        int y = battle.getMapCoords().getY();
        AIRegion[] regions = battle.getSpriteset().getAiRegions();
        for (int i = 0; i < regions.length; i++) {
            if (currentMode == MODE_SPRITE && currentSpritesetMode == SPRITESETMODE_AIREGION && i == selectedAIRegion) {
                AIRegion r = regions[i];
                int x1 = x + r.getX1();
                int y1 = y + r.getY1();
                int x2 = x + r.getX2();
                int y2 = y + r.getY2();
                int x3 = x + r.getX3();
                int y3 = y + r.getY3();
                int x4 = x + r.getX4();
                int y4 = y + r.getY4();
                g2.setColor(Color.GREEN);
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(x1 * 3 * 8 + 12, y1 * 3 * 8 + 12, x2 * 3 * 8 + 12, y2 * 3 * 8 + 12);
                g2.drawLine(x2 * 3 * 8 + 12, y2 * 3 * 8 + 12, x3 * 3 * 8 + 12, y3 * 3 * 8 + 12);
                g2.drawLine(x3 * 3 * 8 + 12, y3 * 3 * 8 + 12, x4 * 3 * 8 + 12, y4 * 3 * 8 + 12);
                g2.drawLine(x4 * 3 * 8 + 12, y4 * 3 * 8 + 12, x1 * 3 * 8 + 12, y1 * 3 * 8 + 12);
            }
        }
    }

    private void getAiPointsImage(Graphics2D g2) {
        int x = battle.getMapCoords().getX();
        int y = battle.getMapCoords().getY();
        AIPoint[] points = battle.getSpriteset().getAiPoints();
        for (int i = 0; i < points.length; i++) {
            if (currentMode == MODE_SPRITE && currentSpritesetMode == SPRITESETMODE_AIPOINT && i == selectedAIPoint) {
                AIPoint p = points[i];
                int px = x + p.getX();
                int py = y + p.getY();
                g2.setColor(Color.GREEN);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(px * 3 * 8, py * 3 * 8, 3 * 8, 3 * 8);
            }
        }
    }

    public MapBlock getSelectedBlockLeft() {
        return selectedBlockLeft;
    }

    public void setSelectedBlockLeft(MapBlock selectedBlockLeft) {
        this.selectedBlockLeft = selectedBlockLeft;
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
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public boolean isDrawSprites() {
        return drawSprites;
    }

    public void setDrawSprites(boolean drawSprites) {
        this.drawSprites = drawSprites;
        this.redraw();
    }

    public int getSelectedAlly() {
        return selectedAlly;
    }

    public void setSelectedAlly(int selectedAlly) {
        this.selectedAlly = selectedAlly;
    }

    public int getSelectedEnemy() {
        return selectedEnemy;
    }

    public void setSelectedEnemy(int selectedEnemy) {
        this.selectedEnemy = selectedEnemy;
    }

    public int getSelectedAIRegion() {
        return selectedAIRegion;
    }

    public void setSelectedAIRegion(int selectedAIRegion) {
        this.selectedAIRegion = selectedAIRegion;
    }

    public int getSelectedAIPoint() {
        return selectedAIPoint;
    }

    public void setSelectedAIPoint(int selectedAIPoint) {
        this.selectedAIPoint = selectedAIPoint;
    }

    public int getCurrentSpritesetMode() {
        return currentSpritesetMode;
    }

    public void setCurrentSpritesetMode(int currentSpritesetMode) {
        this.currentSpritesetMode = currentSpritesetMode;
    }

    public boolean isDrawAiRegions() {
        return drawAiRegions;
    }

    public void setDrawAiRegions(boolean drawAiRegions) {
        this.drawAiRegions = drawAiRegions;
    }

    public boolean isDrawAiPoints() {
        return drawAiPoints;
    }

    public void setDrawAiPoints(boolean drawAiPoints) {
        this.drawAiPoints = drawAiPoints;
    }

    public int getApplicableTerrainValue() {
        return applicableTerrainValue;
    }

    public void setApplicableTerrainValue(int applicableTerrainValue) {
        this.applicableTerrainValue = applicableTerrainValue;
    }
    
    @Override
    protected void onMouseInteraction(BaseMouseCoordsComponent.GridMousePressedEvent evt) {
        if (currentMode == MODE_NONE) return;
        else if (currentMode == MODE_TERRAIN) {
            super.onMouseInteraction(evt);
            return;
        }
        int x = evt.x();
        int y = evt.y();
        int startX = battle.getMapCoords().getX();
        int startY = battle.getMapCoords().getY();
        int width = battle.getMapCoords().getWidth();
        int height = battle.getMapCoords().getHeight();  
        switch (currentMode) {
            case MODE_SPRITE:
                switch (evt.mouseButton()) {
                    case MouseEvent.BUTTON1:
                        /*if (currentSpritesetMode==SPRITESETMODE_ALLY && selectedAlly >= 0) {
                            alliesTable.setValueAt(x-startX, selectedAlly, 1);
                            alliesTable.setValueAt(y-startY, selectedAlly, 2);
                            redraw();
                        }
                        if (currentSpritesetMode==SPRITESETMODE_ENEMY && selectedEnemy >= 0) {
                            enemiesTable.setValueAt(x-startX, selectedEnemy, 1);
                            enemiesTable.setValueAt(y-startY, selectedEnemy, 2);
                            redraw();
                        }*/
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}
