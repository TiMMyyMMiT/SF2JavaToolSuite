/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 *
 * @author wiz
 */
public class ColorPane extends JPanel {

    private static final Border DEFAULT_BORDER = new MatteBorder(1, 1, 1, 1, Color.GRAY);
    private static final Border HOVER_BORDER = new MatteBorder(2, 2, 2, 2, Color.DARK_GRAY);
    
    private Color currentColor;
    private ColorEditor colorEditor;
    private ColorPane self = this;

    public ColorPane(Color color, ColorEditor ce) {
        
        colorEditor = ce;
        updateColor(color);
        setBackground(currentColor);
        setBorder(DEFAULT_BORDER);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(HOVER_BORDER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(DEFAULT_BORDER);
            }
            
            @Override
            public void mouseClicked(MouseEvent e){
                colorEditor.setColorPane(self);
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(30, 30);
    }

    public Color getCurrentColor() {
        return currentColor;
    }
    
    public void updateColor(Color c){
        currentColor = c;
        if (c.getAlpha() == 0)
            currentColor = new Color(c.getRed(),c.getGreen(),c.getBlue(),255);
        setBackground(this.currentColor);
    }
}    
