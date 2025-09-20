/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

import com.sfc.sf2.core.gui.layout.LayoutAnimator.AnimationListener.AnimationFrameEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;

/**
 *
 * @author TiMMy
 */
public class LayoutAnimator extends BaseLayoutComponent implements ActionListener {
    
    private final AnimationController animationController;
    private final ArrayList<AnimationListener> animationListeners;
    
    private Timer idleTimer = null;
    private int currentAnimFrame = 0;
    private int frameMax;
    private boolean variableAnimationSpeed;
    private boolean loop;
    
    public LayoutAnimator(AnimationController controller) {
        super();
        this.animationController = controller;
        animationListeners = new ArrayList<>();
        animationListeners.add(controller);
        setEnabled(false);
    }
    
    public void addAnimationListener(AnimationListener listener) {
        animationListeners.add(listener);
    }
    
    public void removeAnimationListener(AnimationListener listener) {
        animationListeners.add(listener);
        if (animationListeners.size() == 0) {
            setEnabled(false);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            stopAnimation();
        }
        super.setEnabled(enabled);
    }
    
    public boolean isAnimating() {
        return isEnabled();
    }
    
    public void startAnimation(int speed, int frameMax, boolean loop, boolean variableAnimationSpeed) {
        this.loop = loop;
        this.currentAnimFrame = 0;
        this.frameMax = frameMax;
        this.variableAnimationSpeed = variableAnimationSpeed;
        if (!isEnabled()) {
            currentAnimFrame = 0;
            idleTimer = new Timer(speed*1000/60, this);
            if (!variableAnimationSpeed) idleTimer.setRepeats(loop);
            idleTimer.start();
        }
        super.setEnabled(true);
        if (animationListeners != null) {
            AnimationFrameEvent evt = new AnimationFrameEvent(0);
            for (int i = 0; i < animationListeners.size(); i++) {   
                animationListeners.get(i).animationFrameUpdated(evt);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() != idleTimer) return;
        currentAnimFrame++;
        if (currentAnimFrame > frameMax) {
            if (loop) {
                currentAnimFrame=0;
            } else {
                stopAnimation();
                return;
            }
        }
        AnimationFrameEvent evt = new AnimationFrameEvent(currentAnimFrame);
        for (int i = 0; i < animationListeners.size(); i++) {
            animationListeners.get(i).animationFrameUpdated(evt);
        }
        if (animationController != null && variableAnimationSpeed) {
            int delay = animationController.getAnimationFrameSpeed(currentAnimFrame)*1000/60;
            idleTimer.setInitialDelay(delay);
            idleTimer.setDelay(delay);
            idleTimer.restart();
        }
    }
    
    public void stopAnimation() {
        if (idleTimer != null) {
            idleTimer.stop();
            idleTimer = null;
            setEnabled(false);
        }
    }
    
    public interface AnimationController extends AnimationListener {
        
        public void addAnimationListener(AnimationListener listener);
        public void removeAnimationListener(AnimationListener listener);
        
        public void startAnimation(int speed, int frameMax, boolean loop, boolean variableAnimationSpeed);
        public void stopAnimation();
        public int getAnimationFrameSpeed(int currentAnimFrame);
    }
    
    public interface AnimationListener extends java.util.EventListener {
        
        public void animationFrameUpdated(AnimationFrameEvent e);
    
        public class AnimationFrameEvent {
            private int currentFrame;

            public AnimationFrameEvent(int currentFrame) {
                this.currentFrame = currentFrame;
            }

            public int getCurrentFrame() {
                return currentFrame;
            }
        }
    }
}
