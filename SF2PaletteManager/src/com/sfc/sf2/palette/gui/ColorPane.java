/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette.gui;

import com.sfc.sf2.palette.CRAMColor;
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
    
    private CRAMColor currentColor;
    private ColorEditor colorEditor;
    private ColorPane self = this;

    public ColorPane(CRAMColor color, ColorEditor ce) {
        
        colorEditor = ce;
        updateColor(color);
        setBackground(currentColor.CRAMColor());
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
    
    public void setEditor(ColorEditor ce) {
        colorEditor = ce;
    }

    public CRAMColor getCurrentColor() {
        return currentColor;
    }
    
    public void updateColor(CRAMColor c) {
        currentColor = c;
        if (c.CRAMColor().getAlpha() == 0) {
            int rgb = c.CRAMColor().getRGB() & 0xFF;
            currentColor = CRAMColor.fromPremadeCramColor(new Color(rgb));
        }
        setBackground(this.currentColor.CRAMColor());
    }
}    
