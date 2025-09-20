/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui;

import com.sfc.sf2.core.gui.layout.*;
import com.sfc.sf2.core.gui.layout.LayoutAnimator.AnimationController;
import com.sfc.sf2.core.gui.layout.LayoutAnimator.AnimationListener.AnimationFrameEvent;

/**
 *
 * @author TiMMy
 */
public abstract class AnimatedLayoutPanel extends AbstractLayoutPanel implements AnimationController {
    
    protected LayoutAnimator animator;
    
    private int currentAnimFrame;
    
    public AnimatedLayoutPanel() {
        super();
        animator = new LayoutAnimator(this);
    }
    
    public boolean isAnimating() {
        return BaseLayoutComponent.IsEnabled(animator);
    }
    
    public int getCurrentAnimationFrame() {
        return currentAnimFrame;
    }

    public void setCurrentAnimationFrame(int currentAnimationFrame) {
        if (this.currentAnimFrame != currentAnimationFrame) {
            this.currentAnimFrame = currentAnimationFrame;
            if (hasData()) {
                redraw();
            }
        }
    }
    
    public void startAnimation(int speed) {
        startAnimation(speed, 100000, true, false);
    }
    
    public void startAnimation(int speed, int frameMax, boolean loop) {
        startAnimation(speed, frameMax, loop, false);
    }
    
    public void startAnimation(int speed, int frameMax, boolean loop, boolean variableAnimationSpeed) {
        animator.startAnimation(speed, frameMax, loop, variableAnimationSpeed);
    }
    
    public void stopAnimation() {
        animator.stopAnimation();
    }
    @Override
    public void addAnimationListener(LayoutAnimator.AnimationListener listener) {
        animator.addAnimationListener(listener);
    }
    
    @Override
    public void removeAnimationListener(LayoutAnimator.AnimationListener listener) {
        animator.removeAnimationListener(listener);
    }
    
    @Override
    public void animationFrameUpdated(AnimationFrameEvent e) {
        setCurrentAnimationFrame(e.getCurrentFrame());
    }
}
