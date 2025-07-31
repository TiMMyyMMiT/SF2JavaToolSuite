/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation.layout;

import com.sfc.sf2.background.Background;
import com.sfc.sf2.background.layout.BackgroundLayout;
import com.sfc.sf2.battlesprite.BattleSprite;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimation;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.ground.Ground;
import com.sfc.sf2.ground.layout.GroundLayout;
import com.sfc.sf2.weaponsprite.WeaponSprite;
import com.sfc.sf2.weaponsprite.layout.WeaponSpriteLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimationLayout extends JPanel {
        
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
    
    private BufferedImage backgroundImage = null;
    private BufferedImage groundImage = null;
    private BufferedImage[][] weaponspriteImages = null;
    
    private int currentDisplaySize = 1;
    private int currentAnimationFrame = 0;
    private int currentBattlespriteFrame = 0;
    private int currentFrameX = 0;
    private int currentFrameY = 0;
    private int currentWeaponspriteFrame = 0;
    private int currentWeaponZ = 1;
    private int currentWeaponX = 0;
    private int currentWeaponY = 0;
    private boolean weaponHFlip = false;
    private boolean weaponVFlip = false;
    private boolean hideWeapon = false;
    
    private javax.swing.JPanel panel;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public BufferedImage buildImage(){
        BufferedImage image = buildImage(false, false);
        setSize(image.getWidth(), image.getHeight());
        return image;
    }
    
    public BufferedImage buildImage(boolean pngExport, boolean battlespriteOnly){
        
        Tile[] battlespriteImages = battlesprite.getFrames()[currentBattlespriteFrame];
        int tilesPerRow = battlesprite.getTilesPerRow();
        BufferedImage image = new BufferedImage(256, 224, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        
        g.drawImage(backgroundImage, BACKGROUND_BASE_X, BACKGROUND_BASE_Y, null);
        g.drawImage(groundImage, GROUND_BASE_X, GROUND_BASE_Y, null);
        if(battlesprite.getType()==BattleSprite.TYPE_ENEMY){
            drawBattleSpriteFrame(g, battlespriteImages, BATTLESPRITE_ENEMY_BASE_X+currentFrameX, BATTLESPRITE_ENEMY_BASE_Y+currentFrameY, tilesPerRow, battlespriteImages[0].getPalette());
        }else{
            int weaponFlip = 0 + (weaponHFlip?1:0) + (weaponVFlip?2:0);
            if(currentWeaponZ==2){
                drawBattleSpriteFrame(g, battlespriteImages, BATTLESPRITE_ALLY_BASE_X+currentFrameX, BATTLESPRITE_ALLY_BASE_Y+currentFrameY, tilesPerRow, battlespriteImages[0].getPalette());
                if(!hideWeapon && weaponsprite!=null){
                    g.drawImage(weaponspriteImages[weaponFlip][currentWeaponspriteFrame], WEAPONSPRITE_BASE_X+currentFrameX+currentWeaponX, WEAPONSPRITE_BASE_Y+currentFrameY+currentWeaponY, null);
                }
            }else{
                if(!hideWeapon && weaponsprite!=null){
                    g.drawImage(weaponspriteImages[weaponFlip][currentWeaponspriteFrame], WEAPONSPRITE_BASE_X+currentFrameX+currentWeaponX, WEAPONSPRITE_BASE_Y+currentFrameY+currentWeaponY, null);
                }
                drawBattleSpriteFrame(g, battlespriteImages, BATTLESPRITE_ALLY_BASE_X+currentFrameX, BATTLESPRITE_ALLY_BASE_Y+currentFrameY, tilesPerRow, battlespriteImages[0].getPalette());
            }
        }
        
        return resize(image);
    }
    
    private void drawBattleSpriteFrame(Graphics graphics, Tile[] frameTiles, int xOffset, int yOffset, int tilesPerRow, Color[] palette) {
        
        for(int t = 0; t < frameTiles.length; t++) {
            int x = (t%tilesPerRow)*8 + xOffset;
            int y = (t/tilesPerRow)*8 + yOffset;
            frameTiles[t].setPalette(palette);
            graphics.drawImage(frameTiles[t].getIndexedColorImage(), x, y, null);
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }

    public void setBackground(Background background) {
        this.background = background;
        BackgroundLayout backgroundLayout = new BackgroundLayout();
        backgroundLayout.setTiles(background.getTiles());
        backgroundImage = backgroundLayout.buildImage();        
    }

    public void setGround(Ground ground) {
        this.ground = ground;
        GroundLayout groundLayout = new GroundLayout();
        groundLayout.setTiles(ground.getTiles());        
        groundImage = groundLayout.buildImage();
    }

    public void setBattlesprite(BattleSprite battlesprite) {
        this.battlesprite = battlesprite;
    }

    public void setWeaponsprite(WeaponSprite weaponsprite) {
        this.weaponsprite = weaponsprite;
        if(weaponsprite!=null){
            generateWeaponspriteImages();
        }
    }
    
    public void generateWeaponspriteImages(){
        WeaponSpriteLayout weaponspriteLayout = new WeaponSpriteLayout();
        weaponspriteImages = new BufferedImage[4][4];
        weaponspriteLayout.setTiles(weaponsprite.getTiles());
        BufferedImage image = weaponspriteLayout.buildImage();
        for(int i=0;i<4;i++){
            weaponspriteImages[0][i] = image.getSubimage(0, i*64, 64, 64);
        }
        for(int i=0;i<4;i++){
            weaponspriteImages[1][i] = flipH(weaponspriteImages[0][i]);
            weaponspriteImages[2][i] = flipV(weaponspriteImages[0][i]);
            weaponspriteImages[3][i] = flipH(flipV(weaponspriteImages[0][i]));
        }
    }
    
    private static BufferedImage flipH(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(image, image.getWidth(), 0, -image.getWidth(), image.getHeight(), null);
        g.dispose();
        return newImage;
    }

    private static BufferedImage flipV(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(image, 0, image.getHeight(), image.getWidth(), -image.getHeight(), null);
        g.dispose();
        return newImage;
    }
    
    private BufferedImage resize(BufferedImage image){
        BufferedImage newImage = new BufferedImage(image.getWidth()*currentDisplaySize, image.getHeight()*currentDisplaySize, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(image, 0, 0, image.getWidth()*currentDisplaySize, image.getHeight()*currentDisplaySize, null);
        g.dispose();
        return newImage;
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

    public void setCurrentWeaponZ(int currentWeaponZ) {
        this.currentWeaponZ = currentWeaponZ;
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
        return weaponHFlip;
    }

    public void setWeaponHFlip(boolean weaponHFlip) {
        this.weaponHFlip = weaponHFlip;
    }

    public boolean isWeaponVFlip() {
        return weaponVFlip;
    }

    public void setWeaponVFlip(boolean weaponVFlip) {
        this.weaponVFlip = weaponVFlip;
    }

    public void setAnimation(BattleSpriteAnimation animation) {
        this.animation = animation;
    }
    
    public void updateDisplayProperties(){
        if(this.currentAnimationFrame==0){
            this.currentBattlespriteFrame = 0;
            this.currentFrameX = 0;
            this.currentFrameY = 0;
            this.currentWeaponspriteFrame = animation.getIdle1WeaponFrame()&0xF;
            this.currentWeaponZ = animation.getIdle1WeaponZ();
            this.currentWeaponX = animation.getIdle1WeaponX();
            this.currentWeaponY = animation.getIdle1WeaponY();
            this.weaponHFlip = ((animation.getIdle1WeaponFrame()&0x10)!=0);
            this.weaponVFlip = ((animation.getIdle1WeaponFrame()&0x20)!=0);
        }else{
            int bsFrame = animation.getFrames()[this.currentAnimationFrame-1].getIndex();
            if(bsFrame == 0xF){
                this.currentBattlespriteFrame = getPreviousBattlespriteFrame(this.currentAnimationFrame-1);
            }else{
                this.currentBattlespriteFrame = bsFrame;
            }
            this.currentFrameX = animation.getFrames()[this.currentAnimationFrame-1].getX();
            this.currentFrameY = animation.getFrames()[this.currentAnimationFrame-1].getY();
            this.currentWeaponspriteFrame = animation.getFrames()[this.currentAnimationFrame-1].getWeaponFrame()&0xF;
            this.currentWeaponZ = animation.getFrames()[this.currentAnimationFrame-1].getWeaponZ();
            this.currentWeaponX = animation.getFrames()[this.currentAnimationFrame-1].getWeaponX();
            this.currentWeaponY = animation.getFrames()[this.currentAnimationFrame-1].getWeaponY();
            this.weaponHFlip = ((animation.getFrames()[this.currentAnimationFrame-1].getWeaponFrame()&0x10)!=0);
            this.weaponVFlip = ((animation.getFrames()[this.currentAnimationFrame-1].getWeaponFrame()&0x20)!=0);
        }
    }
    
    private int getPreviousBattlespriteFrame(int initAnimFrame){
        while(animation.getFrames()[initAnimFrame].getIndex()==0xF){
            initAnimFrame--;
        }
        return animation.getFrames()[initAnimFrame].getIndex();
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public int getCurrentDisplaySize() {
        return currentDisplaySize;
    }

    public void setCurrentDisplaySize(int currentDisplaySize) {
        this.currentDisplaySize = currentDisplaySize;
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
