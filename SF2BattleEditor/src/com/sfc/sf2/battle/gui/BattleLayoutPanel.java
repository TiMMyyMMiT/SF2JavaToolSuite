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
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.map.block.MapBlock;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wiz
 */
public class BattleLayoutPanel extends BattleMapTerrainLayoutPanel {
    
    public enum BattlePaintMode {
        None,
        Terrain,
        Spriteset,
    }
    
    public enum SpritesetPaintMode {
        Ally,
        Enemy,
        AiRegion,
        AiPoint,
    }
    
    private Battle battle;
    
    private BattlePaintMode paintMode = BattlePaintMode.None;
    private SpritesetPaintMode spritesetMode = SpritesetPaintMode.Ally;
    private int selectedSpritesetEntity = -1;
    
    private boolean drawSprites = true;
    private boolean drawAiRegions = false;
    private boolean drawAiPoints = false;
    
    private List<int[]> actions = new ArrayList<>();
            
    @Override
    public void drawImage(Graphics graphics) {
        super.drawImage(graphics);
        Graphics2D g2 = (Graphics2D)graphics;
        
        int x = battle.getMapCoords().getX();
        int y = battle.getMapCoords().getY();
        if (drawSprites) {
            drawAllies(g2, x, y);
            drawEnemies(g2, x, y);
        }
        if (drawAiRegions) {
            drawAIRegions(g2, x, y);
        }
        if (drawAiPoints) {
            drawAIPoints(g2, x, y);
        }
    }
    
    private void drawAllies(Graphics2D g2, int battleX, int battleY) {
        Ally[] allies = battle.getSpriteset().getAllies();
        for (int i=0; i < allies.length; i++) {
            Ally ally = allies[i];
            Font font = new Font("Courier", Font.BOLD, 16);
            g2.setFont(font);
            int offset = (i+1 < 10) ? 0 : -4;
            int targetX = (battleX+ally.getX())*PIXEL_WIDTH + 8+offset;
            int targetY = (battleY+ally.getY())*PIXEL_HEIGHT + 18;
            String val = String.valueOf(i+1);
            g2.setColor(Color.BLACK);
            g2.drawString(val, targetX-1, targetY-1);
            g2.drawString(val, targetX-1, targetY+1);
            g2.drawString(val, targetX+1, targetY-1);
            g2.drawString(val, targetX+1, targetY+1);
            g2.setColor(Color.YELLOW);
            g2.drawString(val, targetX, targetY);
            if (paintMode == BattlePaintMode.Spriteset && spritesetMode == SpritesetPaintMode.Ally && i == selectedSpritesetEntity) {
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect((battleX+ally.getX())*PIXEL_WIDTH, (battleY+ally.getY())*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
            }
        }
    }
    
    private void drawEnemies(Graphics2D g2, int battleX, int battleY) {
        Enemy[] enemies = battle.getSpriteset().getEnemies();
        for (int i=0; i < enemies.length; i++) {
            Enemy enemy = enemies[i];
            int targetX = (battleX+enemy.getX())*PIXEL_WIDTH;
            int targetY = (battleY+enemy.getY())*PIXEL_HEIGHT;
            int id = enemy.getEnemyData().getID();
            Tileset sprite = enemy.getEnemyData().getMapSprite();
            if (sprite == null) {
                targetX += 8 + ((enemy.getEnemyData().getID() + 1 < 10) ? 0 : -4);
                targetY += 3;
                g2.setColor(Color.BLACK);
                g2.drawString(String.valueOf(id), targetX-1, targetY+16-1);
                g2.drawString(String.valueOf(id), targetX-1, targetY+16+1);
                g2.drawString(String.valueOf(id), targetX+1, targetY+16-1);
                g2.drawString(String.valueOf(id), targetX+1, targetY+16+1);
                g2.setColor(Color.RED);
                g2.drawString(String.valueOf(id), targetX, targetY+16);
            } else if (enemy.getEnemyData().isIsSpecialSprite()) {
                g2.drawImage(sprite.getIndexedColorImage().getSubimage(0, 0, PIXEL_WIDTH*2, PIXEL_HEIGHT*2), targetX-PIXEL_WIDTH/2, targetY-PIXEL_HEIGHT, null);
            } else {
                g2.drawImage(sprite.getIndexedColorImage(), targetX, targetY, null);
            }
            if (paintMode == BattlePaintMode.Spriteset && spritesetMode == SpritesetPaintMode.Enemy && i == selectedSpritesetEntity) {
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect((battleX+enemy.getX())*PIXEL_WIDTH, (battleY+enemy.getY())*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
            }
        }
        for (int i = 0; i < enemies.length; i++) {
            Enemy enemy = enemies[i];
            drawAiTarget(g2, battleX, battleY, enemy.getX(), enemy.getY(), enemy.getMoveOrder1());
            drawAiTarget(g2, battleX, battleY, enemy.getX(), enemy.getY(), enemy.getMoveOrder2());
        }
    }

    private void drawAiTarget(Graphics2D g2, int mapOffsetX, int mapOffsetY, int enemyX, int enemyY, String order) {
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
            g2.drawLine((mapOffsetX + enemyX)*PIXEL_WIDTH+12, (mapOffsetY + enemyY)*PIXEL_HEIGHT+12, (mapOffsetX + targetX)*PIXEL_WIDTH+12, (mapOffsetY + targetY)*PIXEL_HEIGHT+12);
        }
    }

    private void drawAIRegions(Graphics2D g2, int battleX, int battleY) {
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        AIRegion[] regions = battle.getSpriteset().getAiRegions();
        for (int i=0; i < regions.length; i++) {
            if (selectedSpritesetEntity == -1 || i == selectedSpritesetEntity) {
                AIRegion r = regions[i];
                int x1 = battleX + r.getX1();
                int y1 = battleY + r.getY1();
                int x2 = battleX + r.getX2();
                int y2 = battleY + r.getY2();
                int x3 = battleX + r.getX3();
                int y3 = battleY + r.getY3();
                int x4 = battleX + r.getX4();
                int y4 = battleY + r.getY4();
                g2.drawLine(x1 * PIXEL_WIDTH + 12, y1 * PIXEL_HEIGHT + 12, x2 * PIXEL_WIDTH + 12, y2 * PIXEL_HEIGHT + 12);
                g2.drawLine(x2 * PIXEL_WIDTH + 12, y2 * PIXEL_HEIGHT + 12, x3 * PIXEL_WIDTH + 12, y3 * PIXEL_HEIGHT + 12);
                g2.drawLine(x3 * PIXEL_WIDTH + 12, y3 * PIXEL_HEIGHT + 12, x4 * PIXEL_WIDTH + 12, y4 * PIXEL_HEIGHT + 12);
                g2.drawLine(x4 * PIXEL_WIDTH + 12, y4 * PIXEL_HEIGHT + 12, x1 * PIXEL_WIDTH + 12, y1 * PIXEL_HEIGHT + 12);
            }
        }
    }

    private void drawAIPoints(Graphics2D g2, int battleX, int battleY) {
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        AIPoint[] points = battle.getSpriteset().getAiPoints();
        for (int i = 0; i < points.length; i++) {
            if (selectedSpritesetEntity == -1 || i == selectedSpritesetEntity) {
                AIPoint p = points[i];
                int px = battleX + p.getX();
                int py = battleY + p.getY();
                g2.drawRect(px * PIXEL_WIDTH, py * PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
            }
        }
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
            redraw();
    }

    public boolean isDrawSprites() {
        return drawSprites;
    }

    public void setDrawSprites(boolean drawSprites) {
        if (this.drawSprites != drawSprites) {
            this.drawSprites = drawSprites;
            redraw();
        }
    }

    public void setDrawAiRegions(boolean drawAiRegions, int selectedAIRegion) {
        if (this.drawAiRegions != drawAiRegions && this.selectedSpritesetEntity != selectedAIRegion) {
            this.drawAiRegions = drawAiRegions;
            this.selectedSpritesetEntity = selectedAIRegion;
            redraw();
        }
    }

    public void setDrawAiPoints(boolean drawAiPoints, int selectedAIPoint) {
        if (this.drawAiPoints != drawAiPoints && this.selectedSpritesetEntity != selectedAIPoint) {
            this.drawAiPoints = drawAiPoints;
            this.selectedSpritesetEntity = selectedAIPoint;
            redraw();
        }
    }

    public BattlePaintMode getPaintMode() {
        return paintMode;
    }

    public void setPaintMode(BattlePaintMode battlePaintMode) {
        if (this.paintMode != battlePaintMode) {
            this.paintMode = battlePaintMode;
            redraw();
        }
    }

    public SpritesetPaintMode getSpritesetMode() {
        return spritesetMode;
    }

    public void setSpritesetMode(SpritesetPaintMode spritesetPaintMode) {
        if (this.spritesetMode != spritesetPaintMode) {
            this.setPaintMode(BattlePaintMode.Spriteset);
            this.spritesetMode = spritesetPaintMode;
            redraw();
        }
    }

    public int getSelectedAlly() {
        return selectedSpritesetEntity;
    }

    public void setSelectedAlly(int selectedAlly) {
        if (this.selectedSpritesetEntity != selectedAlly) {
            this.selectedSpritesetEntity = selectedAlly;
            redraw();
        }
    }

    public int getSelectedEnemy() {
        return selectedSpritesetEntity;
    }

    public void setSelectedEnemy(int selectedEnemy) {
        if (this.selectedSpritesetEntity != selectedEnemy) {
            this.selectedSpritesetEntity = selectedEnemy;
            redraw();
        }
    }

    public int getSelectedAIRegion() {
        return selectedSpritesetEntity;
    }

    public int getSelectedAIPoint() {
        return selectedSpritesetEntity;
    }

    public List<int[]> getActions() {
        return actions;
    }

    public void setActions(List<int[]> actions) {
        this.actions = actions;
    }
    
    @Override
    protected void onMouseInteraction(BaseMouseCoordsComponent.GridMousePressedEvent evt) {
        if (paintMode == BattlePaintMode.None) return;
        else if (paintMode == BattlePaintMode.Terrain) {
            super.onMouseInteraction(evt);
            return;
        }
        
        //Edit spritesets
        int x = evt.x();
        int y = evt.y();
        int startX = battle.getMapCoords().getX();
        int startY = battle.getMapCoords().getY();
        int width = battle.getMapCoords().getWidth();
        int height = battle.getMapCoords().getHeight();  
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
    }
}
