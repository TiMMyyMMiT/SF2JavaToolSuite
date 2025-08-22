/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation.gui;

import com.sfc.sf2.background.Background;
import com.sfc.sf2.battlesprite.BattleSprite;
import com.sfc.sf2.battlesprite.BattleSprite.BattleSpriteType;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimation;
import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.ground.Ground;
import com.sfc.sf2.weaponsprite.WeaponSprite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimationLayoutPanel extends AbstractLayoutPanel {
    
    private static final Dimension IMAGE_SIZE = new Dimension(256, 224);
    
    private static final int BACKGROUND_BASE_X = 0;
    private static final int BACKGROUND_BASE_Y = 56;
    private static final int GROUND_BASE_X = 136;
    private static final int GROUND_BASE_Y = 140;
    private static final int BATTLESPRITE_ALLY_BASE_X = 136;
    private static final int BATTLESPRITE_ALLY_BASE_Y = 64;
    private static final int BATTLESPRITE_ENEMY_BASE_X = 0;
    private static final int BATTLESPRITE_ENEMY_BASE_Y = 56;
    private static final int WEAPONSPRITE_BASE_X = 136;
    private static final int WEAPONSPRITE_BASE_Y = 64;
        
    private Background background;
    private Ground ground;
    private BattleSprite battlesprite;
    private WeaponSprite weaponsprite;
    private BattleSpriteAnimation animation;
    
    private int currentAnimationFrame = 0;
    private int currentBattlespriteFrame = 0;
    private int currentFrameX = 0;
    private int currentFrameY = 0;
    private int currentWeaponspriteFrame = 0;
    private int weaponHFlip = 1;
    private int weaponVFlip = 1;
    private boolean currentWeaponBehind = false;
    private int currentWeaponX = 0;
    private int currentWeaponY = 0;
    private boolean hideWeapon = false;

    @Override
    protected boolean hasData() {
        return battlesprite != null && animation != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        return IMAGE_SIZE;
    }

    @Override
    protected void buildImage(Graphics graphics) {
        Tileset frame = battlesprite.getFrames()[currentBattlespriteFrame];
        int tilesPerRow = battlesprite.getTilesPerRow();
        BufferedImage image = new BufferedImage(256, 224, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        
        g.drawImage(background.getTileset().getIndexedColorImage(), BACKGROUND_BASE_X, BACKGROUND_BASE_Y, null);
        g.drawImage(ground.getTileset().getIndexedColorImage(), GROUND_BASE_X, GROUND_BASE_Y, null);
        if (battlesprite.getType() == BattleSpriteType.ENEMY) {
            drawBattleSpriteFrame(g, frame, BATTLESPRITE_ENEMY_BASE_X+currentFrameX, BATTLESPRITE_ENEMY_BASE_Y+currentFrameY, tilesPerRow);
        } else {
            if (currentWeaponBehind) {
                drawBattleSpriteFrame(g, frame, BATTLESPRITE_ALLY_BASE_X+currentFrameX, BATTLESPRITE_ALLY_BASE_Y+currentFrameY, tilesPerRow);
                if (!hideWeapon && weaponsprite!=null) {
                    g.drawImage(weaponsprite.getFrames()[currentWeaponspriteFrame].getIndexedColorImage(), WEAPONSPRITE_BASE_X+currentFrameX+currentWeaponX, WEAPONSPRITE_BASE_Y+currentFrameY+currentWeaponY,
                            WeaponSprite.FRAME_TILE_WIDTH*PIXEL_WIDTH*weaponHFlip, WeaponSprite.FRAME_TILE_HEIGHT*Tile.PIXEL_HEIGHT*weaponVFlip, null);
                }
            } else {
                if (!hideWeapon && weaponsprite != null) {
                    g.drawImage(weaponsprite.getFrames()[currentWeaponspriteFrame].getIndexedColorImage(), WEAPONSPRITE_BASE_X+currentFrameX+currentWeaponX, WEAPONSPRITE_BASE_Y+currentFrameY+currentWeaponY,
                            WeaponSprite.FRAME_TILE_WIDTH*PIXEL_WIDTH*weaponHFlip, WeaponSprite.FRAME_TILE_HEIGHT*Tile.PIXEL_HEIGHT*weaponVFlip, null);
                }
                drawBattleSpriteFrame(g, frame, BATTLESPRITE_ALLY_BASE_X+currentFrameX, BATTLESPRITE_ALLY_BASE_Y+currentFrameY, tilesPerRow);
            }
        }
    }
    
    private void drawBattleSpriteFrame(Graphics graphics, Tileset frame, int xOffset, int yOffset, int tilesPerRow) {
        Tile[] tiles = frame.getTiles();
        for (int t = 0; t < tiles.length; t++) {
            int x = (t%tilesPerRow)*8 + xOffset;
            int y = (t/tilesPerRow)*8 + yOffset;
            graphics.drawImage(tiles[t].getIndexedColorImage(), x, y, null);
        }
    }

    public void setBackground(Background background) {
        this.background = background;
        redraw();
    }

    public void setGround(Ground ground) {
        this.ground = ground;
        redraw();
    }

    public void setBattlesprite(BattleSprite battlesprite) {
        this.battlesprite = battlesprite;
    }

    public void setWeaponsprite(WeaponSprite weaponsprite) {
        this.weaponsprite = weaponsprite;
    }

    public void setCurrentBattlespriteFrame(int currentBattlespriteFrame) {
        this.currentBattlespriteFrame = currentBattlespriteFrame;
    }

    public int getCurrentFrameX() {
        return currentFrameX;
    }

    public void setCurrentFrameX(int currentFrameX) {
        this.currentFrameX = currentFrameX;
    }

    public int getCurrentFrameY() {
        return currentFrameY;
    }

    public void setCurrentFrameY(int currentFrameY) {
        this.currentFrameY = currentFrameY;
    }

    public void setCurrentWeaponFrame(int currentWeaponFrame) {
        this.currentWeaponspriteFrame = currentWeaponFrame;
    }

    public void setCurrentWeaponBehind(boolean currentWeaponBehind) {
        this.currentWeaponBehind = currentWeaponBehind;
    }

    public void setCurrentWeaponX(int currentWeaponX) {
        this.currentWeaponX = currentWeaponX;
    }

    public int getCurrentWeaponY() {
        return currentWeaponY;
    }

    public void setCurrentWeaponY(int currentWeaponY) {
        this.currentWeaponY = currentWeaponY;
    }

    public boolean isWeaponHFlip() {
        return weaponHFlip < 0;
    }

    public void setWeaponHFlip(boolean weaponHFlip) {
        this.weaponHFlip = weaponHFlip ? -1 : 1;
    }

    public boolean isWeaponVFlip() {
        return weaponVFlip < 0;
    }

    public void setWeaponVFlip(boolean weaponVFlip) {
        this.weaponVFlip = weaponVFlip ? -1 : 1;
    }
    
    public BattleSpriteAnimation getAnimation() {
        return animation;
    }

    public void setAnimation(BattleSpriteAnimation animation) {
        this.animation = animation;
    }
    
    public void updateDisplayProperties() {
        if (this.currentAnimationFrame == 0) {
            this.currentBattlespriteFrame = 0;
            this.currentFrameX = 0;
            this.currentFrameY = 0;
            /*this.currentWeaponspriteFrame = animation.getIdle1WeaponFrame()&0xF;
            this.currentWeaponBehind = animation.getIdle1WeaponZ();
            this.currentWeaponX = animation.getIdle1WeaponX();
            this.currentWeaponY = animation.getIdle1WeaponY();
            this.weaponHFlip = ((animation.getIdle1WeaponFrame()&0x10)!=0) ? -1 : 1;
            this.weaponVFlip = ((animation.getIdle1WeaponFrame()&0x20)!=0) ? -1 : 1;*/
        } else {
            int bsFrame = animation.getFrames()[this.currentAnimationFrame-1].getIndex();
            if (bsFrame == 0xF) {
                this.currentBattlespriteFrame = getPreviousBattlespriteFrame(this.currentAnimationFrame-1);
            } else {
                this.currentBattlespriteFrame = bsFrame;
            }
            this.currentFrameX = animation.getFrames()[this.currentAnimationFrame-1].getX();
            this.currentFrameY = animation.getFrames()[this.currentAnimationFrame-1].getY();
            this.currentWeaponspriteFrame = animation.getFrames()[this.currentAnimationFrame-1].getWeaponFrame()&0xF;
            this.currentWeaponBehind = animation.getFrames()[this.currentAnimationFrame-1].getWeaponBehind();
            this.currentWeaponX = animation.getFrames()[this.currentAnimationFrame-1].getWeaponX();
            this.currentWeaponY = animation.getFrames()[this.currentAnimationFrame-1].getWeaponY();
            this.weaponHFlip = ((animation.getFrames()[this.currentAnimationFrame-1].getWeaponFrame()&0x10)!=0) ? -1 : 1;
            this.weaponVFlip = ((animation.getFrames()[this.currentAnimationFrame-1].getWeaponFrame()&0x20)!=0) ? -1 : 1;
        }
    }
    
    private int getPreviousBattlespriteFrame(int initAnimFrame) {
        while (animation.getFrames()[initAnimFrame].getIndex()==0xF) {
            initAnimFrame--;
        }
        return animation.getFrames()[initAnimFrame].getIndex();
    }

    public int getCurrentAnimationFrame() {
        return currentAnimationFrame;
    }

    public void setCurrentAnimationFrame(int currentAnimationFrame) {
        this.currentAnimationFrame = currentAnimationFrame;
    }

    public boolean isHideWeapon() {
        return hideWeapon;
    }

    public void setHideWeapon(boolean hideWeapon) {
        this.hideWeapon = hideWeapon;
    }
}
