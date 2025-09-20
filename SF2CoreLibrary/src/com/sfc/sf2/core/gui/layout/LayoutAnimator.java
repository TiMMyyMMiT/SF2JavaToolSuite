/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

import com.sfc.sf2.core.gui.layout.LayoutAnimator.AnimationListener.FrameEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author TiMMy
 */
public class LayoutAnimator extends BaseLayoutComponent implements ActionListener {
    
    private final AnimationListener animationListener;
    
    private Timer idleTimer = null;
    private int currentAnimFrame = 0;
    private int frameMax;
    private boolean variableAnimationSpeed;
    private int speed;
    private boolean loop;
    
    public LayoutAnimator(AnimationListener listener) {
        super();
        animationListener = listener;
        setEnabled(false);
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
        this.speed = speed;
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
        if (animationListener != null) {
            animationListener.frameUpdated(new FrameEvent(0));
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
        if (animationListener == null) return;
        animationListener.frameUpdated(new FrameEvent(currentAnimFrame));
        if (variableAnimationSpeed) {
            int delay = animationListener.getFrameSpeed(currentAnimFrame)*1000/60;
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
    
    public interface AnimationListener extends java.util.EventListener
    {
        public int getFrameSpeed(int currentAnimFrame);
        public void frameUpdated(FrameEvent e);
    
        public class FrameEvent {
            private int currentFrame;

            public FrameEvent(int currentFrame) {
                this.currentFrame = currentFrame;
            }

            public int getCurrentFrame() {
                return currentFrame;
            }
        }
    }
}
