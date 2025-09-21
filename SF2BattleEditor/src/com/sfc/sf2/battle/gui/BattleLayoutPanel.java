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
import com.sfc.sf2.battle.BattleSpriteset;
import com.sfc.sf2.battle.Enemy;
import com.sfc.sf2.battle.mapterrain.gui.BattleMapTerrainLayoutPanel;
import com.sfc.sf2.battle.mapterrain.gui.resources.BattleTerrainIcons;
import com.sfc.sf2.core.gui.layout.BaseMouseCoordsComponent;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.GraphicsHelpers;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

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
    private int closestRegionPoint;
    
    private boolean drawSprites = true;
    private boolean drawAiRegions = false;
    private boolean drawAiPoints = false;
    private Image alertImage;
    
    private ActionListener spritesetEditedListener;
    private List<int[]> actions = new ArrayList<>();

    public BattleLayoutPanel() {
        super();
        mouseInput.setMouseMotionListener(this::onMouseMoved);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawAIRegionNode(g);
    }
            
    @Override
    public void drawImage(Graphics graphics) {
        super.drawImage(graphics);
        Graphics2D g2 = (Graphics2D)graphics;
        
        int x = battle.getMapCoords().getX();
        int y = battle.getMapCoords().getY();
        drawAllies(g2, x, y);
        drawEnemies(g2, x, y);
        drawAIRegions(g2, x, y);
        drawAIPoints(g2, x, y);
        drawSelected(g2, x, y);
        drawAlerts(g2, x, y);
    }
    
    private void drawAllies(Graphics2D g2, int battleX, int battleY) {
        if (!drawSprites) return;
        Ally[] allies = battle.getSpriteset().getAllies();
        for (int i=0; i < allies.length; i++) {
            Ally ally = allies[i];
            Font font = new Font("SansSerif", Font.BOLD, 16);
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
        if (!drawSprites) return;
        Enemy[] enemies = battle.getSpriteset().getEnemies();
        for (int i=0; i < enemies.length; i++) {
            Enemy enemy = enemies[i];
            int targetX = (battleX+enemy.getX())*PIXEL_WIDTH;
            int targetY = (battleY+enemy.getY())*PIXEL_HEIGHT;
            int id = enemy.getEnemyData().getID();
            Tileset sprite = enemy.getEnemyData().getMapSprite();
            if (sprite == null) {
                //Draw number to represent sprite
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
                //Draw special sprite
                g2.drawImage(sprite.getIndexedColorImage().getSubimage(0, 0, PIXEL_WIDTH*2, PIXEL_HEIGHT*2), targetX-PIXEL_WIDTH/2, targetY-PIXEL_HEIGHT, null);
            } else {
                //Draw regular sprite
                g2.drawImage(sprite.getIndexedColorImage(), targetX, targetY, null);
            }
        }
        for (int i = 0; i < enemies.length; i++) {
            Enemy enemy = enemies[i];
            g2.setColor(Color.WHITE);
            drawAiTarget(g2, battleX, battleY, enemy.getX(), enemy.getY(), enemy.getMoveOrder(), enemy.getMoveOrderTarget());
        }
    }

    private void drawAiTarget(Graphics2D g2, int mapOffsetX, int mapOffsetY, int enemyX, int enemyY, String order, int target) {
        int targetX = -1, targetY = -1;
        switch (order) {
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
            GraphicsHelpers.drawArrowLine(g2, (mapOffsetX+enemyX)*PIXEL_WIDTH+12, (mapOffsetY+enemyY)*PIXEL_HEIGHT+12, (mapOffsetX+targetX)*PIXEL_WIDTH+12, (mapOffsetY+targetY)*PIXEL_HEIGHT+12);
        }
    }

    private void drawAIRegions(Graphics2D g2, int battleX, int battleY) {
        if (!drawAiRegions) return;
        g2.setStroke(new BasicStroke(3));
        AIRegion[] regions = battle.getSpriteset().getAiRegions();
        for (int i=0; i < regions.length; i++) {
            g2.setColor(Color.WHITE);
            drawRegionBounds(g2, battleX, battleY, regions[i]);
        }
    }
    
    private void drawRegionBounds(Graphics2D g2, int battleX, int battleY, AIRegion region) {
        int x1 = battleX+region.getX1();
        int y1 = battleY+region.getY1();
        int x2 = battleX+region.getX2();
        int y2 = battleY+region.getY2();
        int x3 = battleX+region.getX3();
        int y3 = battleY+region.getY3();
        int x4 = battleX+region.getX4();
        int y4 = battleY+region.getY4();
        g2.drawLine(x1*PIXEL_WIDTH+12, y1*PIXEL_HEIGHT+12, x2*PIXEL_WIDTH+12, y2*PIXEL_HEIGHT+12);
        g2.drawLine(x2*PIXEL_WIDTH+12, y2*PIXEL_HEIGHT+12, x3*PIXEL_WIDTH+12, y3*PIXEL_HEIGHT+12);
        g2.drawLine(x3*PIXEL_WIDTH+12, y3*PIXEL_HEIGHT+12, x4*PIXEL_WIDTH+12, y4*PIXEL_HEIGHT+12);
        g2.drawLine(x4*PIXEL_WIDTH+12, y4*PIXEL_HEIGHT+12, x1*PIXEL_WIDTH+12, y1*PIXEL_HEIGHT+12);
    }

    private void drawAIPoints(Graphics2D g2, int battleX, int battleY) {
        if (!drawAiPoints) return;
        g2.setStroke(new BasicStroke(3));
        AIPoint[] points = battle.getSpriteset().getAiPoints();
        for (int i = 0; i < points.length; i++) {
            g2.setColor(Color.WHITE);
            drawAIPoint(g2, battleX, battleY, points[i]);
        }
    }
    
    private void drawAIPoint(Graphics2D g2, int battleX, int battleY, AIPoint point) {
            int px = battleX + point.getX();
            int py = battleY + point.getY();
            g2.drawRect(px*PIXEL_WIDTH, py*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
    }
    
    private void drawSelected(Graphics2D g2, int battleX, int battleY) {
        if (selectedSpritesetEntity == -1 || spritesetMode == SpritesetPaintMode.Ally) return;
        BattleSpriteset spriteset = battle.getSpriteset();
        switch (spritesetMode) {
            case Enemy:
                Enemy enemy = spriteset.getEnemies()[selectedSpritesetEntity];
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect((battleX+enemy.getX())*PIXEL_WIDTH, (battleY+enemy.getY())*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
                g2.setColor(Color.RED);
                drawAiTarget(g2, battleX, battleY, enemy.getX(), enemy.getY(), enemy.getBackupMoveOrder(), enemy.getBackupMoveOrderTarget());
                if (enemy.getBackupMoveOrder().equals("MOVE_TO")) {
                    int target = enemy.getBackupMoveOrderTarget();
                    if (target >= 0 && target < spriteset.getAiPoints().length) {
                        drawAIPoint(g2, battleX, battleY, spriteset.getAiPoints()[target]);
                    }
                }
                g2.setColor(Color.GREEN);
                drawAiTarget(g2, battleX, battleY, enemy.getX(), enemy.getY(), enemy.getMoveOrder(), enemy.getMoveOrderTarget());
                if (enemy.getMoveOrder().equals("MOVE_TO")) {
                    int target = enemy.getMoveOrderTarget();
                    if (target >= 0 && target < spriteset.getAiPoints().length) {
                        drawAIPoint(g2, battleX, battleY, spriteset.getAiPoints()[target]);
                    }
                }
                int triggerRegion = enemy.getTriggerRegion2();
                if (triggerRegion >=0 && triggerRegion < spriteset.getAiRegions().length) {
                    g2.setColor(Color.RED);
                    drawRegionBounds(g2, battleX, battleY, spriteset.getAiRegions()[triggerRegion]);
                }
                triggerRegion = enemy.getTriggerRegion1();
                if (triggerRegion >=0 && triggerRegion < spriteset.getAiRegions().length) {
                    g2.setColor(Color.GREEN);
                    drawRegionBounds(g2, battleX, battleY, spriteset.getAiRegions()[triggerRegion]);
                }
                break;
            case AiRegion:
                g2.setColor(Color.YELLOW);
                drawRegionBounds(g2, battleX, battleY, spriteset.getAiRegions()[selectedSpritesetEntity]);
                break;
            case AiPoint:
                g2.setColor(Color.YELLOW);
                drawAIPoint(g2, battleX, battleY, spriteset.getAiPoints()[selectedSpritesetEntity]);
                break;
        }   
    }
    
    private void drawAlerts(Graphics2D g2, int battleX, int battleY) {
        //With the feature added to shift the map without shifting Spriteset items, it is possible for them to be out of bounds. This alerts the user if anything is out of bounds
        Ally[] allies = battle.getSpriteset().getAllies();
        for (int i=0; i < allies.length; i++) {
            drawAlertIfOutOfBounds(g2, battleX, battleY, allies[i].getX(), allies[i].getY());
        }
        Enemy[] enemies = battle.getSpriteset().getEnemies();
        for (int i=0; i < enemies.length; i++) {
            drawAlertIfOutOfBounds(g2, battleX, battleY, enemies[i].getX(), enemies[i].getY());
        }
        AIRegion[] regions = battle.getSpriteset().getAiRegions();
        for (int i=0; i < regions.length; i++) {
            drawAlertIfOutOfBounds(g2, battleX, battleY, regions[i].getX1(), regions[i].getY1());
            drawAlertIfOutOfBounds(g2, battleX, battleY, regions[i].getX2(), regions[i].getY2());
            drawAlertIfOutOfBounds(g2, battleX, battleY, regions[i].getX3(), regions[i].getY3());
            drawAlertIfOutOfBounds(g2, battleX, battleY, regions[i].getX4(), regions[i].getY4());
        }
        AIPoint[] points = battle.getSpriteset().getAiPoints();
        for (int i = 0; i < points.length; i++) {
            drawAlertIfOutOfBounds(g2, battleX, battleY, points[i].getX(), points[i].getY());
        }
    }
    
    private void drawAlertIfOutOfBounds(Graphics2D g2, int battleX, int battleY, int x, int y) {
        if (x < 0 || x >= battleCoords.getWidth() || y < 0 || y >= battleCoords.getHeight()) {
            g2.drawImage(getAlertImage(), (battleX+x)*PIXEL_WIDTH, (battleY+y)*PIXEL_HEIGHT, null);
        }
    }
    
    private Image getAlertImage() {
        if (alertImage == null) {
            ClassLoader loader = BattleTerrainIcons.class.getClassLoader();
            alertImage = new ImageIcon(loader.getResource("battle/icons/alert.png")).getImage();
        }
        return alertImage;
    }
    
    private void drawAIRegionNode(Graphics graphics) {
        //Draw a node to show closest node to mouse cursor (when in AI Region edit mode)
        if (spritesetMode != SpritesetPaintMode.AiRegion || selectedSpritesetEntity == -1) return;
        int battleX = battle.getMapCoords().getX();
        int battleY = battle.getMapCoords().getY();
        AIRegion region = battle.getSpriteset().getAiRegions()[selectedSpritesetEntity];
        graphics.setColor(Color.BLUE);
        int nodeX = -1;
        int nodeY = -1;
        switch (closestRegionPoint) {
            case 0: nodeX = region.getX1(); nodeY = region.getY1(); break;
            case 1: nodeX = region.getX2(); nodeY = region.getY2(); break;
            case 2: nodeX = region.getX3(); nodeY = region.getY3(); break;
            case 3: nodeX = region.getX4(); nodeY = region.getY4(); break;
        }
        int scale = getDisplayScale();
        nodeX = (battleX+nodeX)*PIXEL_WIDTH+16;
        nodeY = (battleY+nodeY)*PIXEL_HEIGHT+16;
        graphics.fillArc(nodeX*scale, nodeY*scale, 8*scale, 8*scale, 0, 360);
    }

    public void setSpritesetEditedListener(ActionListener spritesetEditedListener) {
        this.spritesetEditedListener = spritesetEditedListener;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
        selectedSpritesetEntity = -1;
        redraw();
    }

    public void setDrawSprites(boolean drawSprites) {
        if (this.drawSprites != drawSprites) {
            this.drawSprites = drawSprites;
            redraw();
        }
    }

    public void setDrawAiRegions(boolean drawAiRegions) {
        if (this.drawAiRegions != drawAiRegions) {
            this.drawAiRegions = drawAiRegions;
            redraw();
        }
    }

    public void setDrawAiPoints(boolean drawAiPoints) {
        if (this.drawAiPoints != drawAiPoints) {
            this.drawAiPoints = drawAiPoints;
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
        setSelectedAlly(selectedEnemy);
    }

    public int getSelectedAIRegion() {
        return selectedSpritesetEntity;
    }

    public void setSelectedAIRegion(int selectedRegion) {
        setSelectedAlly(selectedRegion);
    }

    public int getSelectedAIPoint() {
        return selectedSpritesetEntity;
    }

    public void setSelectedAIPoint(int selectedPoit) {
        setSelectedAlly(selectedPoit);
    }

    public List<int[]> getActions() {
        return actions;
    }

    public void setActions(List<int[]> actions) {
        this.actions = actions;
    }
    
    private void onMouseMoved(BaseMouseCoordsComponent.GridMouseMoveEvent evt) {
        if (spritesetMode == SpritesetPaintMode.AiRegion && selectedSpritesetEntity >= 0) {
            int x = evt.x() - battle.getMapCoords().getX();
            int y = evt.y() - battle.getMapCoords().getY();
            int region = findClosestRegionPoint(battle.getSpriteset().getAiRegions()[selectedSpritesetEntity], x, y);
            if (closestRegionPoint != region) {
                closestRegionPoint = region;
                this.repaint();
            }
        } 
    }
    
    @Override
    protected void onMouseInteraction(BaseMouseCoordsComponent.GridMousePressedEvent evt) {
        if (paintMode == BattlePaintMode.None) return;
        else if (paintMode == BattlePaintMode.Terrain) {
            if (isDrawTerrain()) {
                super.onMouseInteraction(evt);
            }
            return;
        }
        if (selectedSpritesetEntity == -1) return;
        
        //Edit spritesets
        int x = evt.x() - battle.getMapCoords().getX();
        int y = evt.y() - battle.getMapCoords().getY();
        switch (evt.mouseButton()) {
            case MouseEvent.BUTTON1:
                switch (spritesetMode) {
                    case Ally:
                        Ally ally = battle.getSpriteset().getAllies()[selectedSpritesetEntity];
                        ally.setX(x);
                        ally.setY(y);
                        redraw();
                        if (spritesetEditedListener != null) {
                            spritesetEditedListener.actionPerformed(new ActionEvent(this, selectedSpritesetEntity, "Ally"));
                        }
                        break;
                    case Enemy:
                        Enemy enemy = battle.getSpriteset().getEnemies()[selectedSpritesetEntity];
                        enemy.setX(x);
                        enemy.setY(y);
                        redraw();
                        if (spritesetEditedListener != null) {
                            spritesetEditedListener.actionPerformed(new ActionEvent(this, selectedSpritesetEntity, "Enemy"));
                        }
                        break;
                    case AiRegion:
                        AIRegion region = battle.getSpriteset().getAiRegions()[selectedSpritesetEntity];
                        if (evt.dragging()) {
                            switch (closestRegionPoint) {
                                case 0: region.setX1(x); region.setY1(y); break;
                                case 1: region.setX2(x); region.setY2(y); break;
                                case 2: region.setX3(x); region.setY3(y); break;
                                case 3: region.setX4(x); region.setY4(y); break;
                            }
                            redraw();
                            if (spritesetEditedListener != null) {
                                spritesetEditedListener.actionPerformed(new ActionEvent(this, selectedSpritesetEntity, "AiRegion"));
                            }
                        } else {
                            closestRegionPoint = findClosestRegionPoint(region, x, y);
                            this.repaint();
                        }
                        break;
                    case AiPoint:
                        AIPoint point = battle.getSpriteset().getAiPoints()[selectedSpritesetEntity];
                        point.setX(x);
                        point.setY(y);
                        redraw();
                        if (spritesetEditedListener != null) {
                            spritesetEditedListener.actionPerformed(new ActionEvent(this, selectedSpritesetEntity, "AiPoint"));
                        }
                        break;
                }
        }
    }
    
    private int findClosestRegionPoint(AIRegion region, int x, int y) {
        int closest = -1;
        double distance = Integer.MAX_VALUE;
        Point mouse = new Point(x, y);
        Point[] points = new Point[] { new Point(region.getX1(), region.getY1()), new Point(region.getX2(), region.getY2()), new Point(region.getX3(), region.getY3()), new Point(region.getX4(), region.getY4()) };
        for (int i = 0; i < points.length; i++) {
            double dist = mouse.distance(points[i]);
            if (dist < distance) {
                closest = i;
                distance = dist;
            }
        }
        return closest;
    }
}
