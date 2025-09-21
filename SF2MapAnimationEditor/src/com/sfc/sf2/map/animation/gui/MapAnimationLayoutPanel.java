/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.animation.gui;

import com.sfc.sf2.core.gui.layout.LayoutAnimator;
import com.sfc.sf2.core.gui.layout.LayoutAnimator.AnimationController;
import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.map.layout.gui.StaticMapLayoutPanel;
import java.awt.Graphics;

/**
 *
 * @author wiz
 */
public class MapAnimationLayoutPanel extends StaticMapLayoutPanel implements AnimationController {
    
    private MapAnimation animation;

    public MapAnimationLayoutPanel() {
        animator = new LayoutAnimator(this);
    }

    @Override
    protected void drawImage(Graphics graphics) {
        super.drawImage(graphics);
        
        /*MapBlock[] blocks = this.map.getAnimation().getFrames()[this.selectedAnimFrame].getBlocks();
        int imageHeight = 64 * 3 * 8;
        for (int y = 0; y < 64; y++) {
            for (int x = 0; x < 64; x++) {
                MapBlock block = blocks[y * 64 + x];
                graphics.drawImage(block.getIndexedColorImage(), x * 3 * 8, y * 3 * 8, null);
            }
        }*/
    }

    public MapAnimation getAnimation() {
        return animation;
    }
    
    public void setAnimation(MapAnimation animation) {
        this.animation = animation;
        this.redraw();
    }

    @Override
    public int getAnimationFrameSpeed(int currentAnimFrame) {
        return 0;
    }
}
