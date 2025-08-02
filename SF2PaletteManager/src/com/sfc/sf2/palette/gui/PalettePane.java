/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette.gui;

import com.sfc.sf2.palette.Palette;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

/**
 *
 * @author wiz
 */
public class PalettePane extends JPanel {
        
    private ColorEditor colorEditor;
    private Palette palette;
    private ColorPane[] colorPanes;
    
    public PalettePane() {
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        colorPanes = new ColorPane[16];
        for (int i = 0; i < 16; i++) {
            gbc.gridx = i;
            ColorPane colorPane = new ColorPane(Color.BLACK, colorEditor);
            colorPanes[i] = colorPane;
            add(colorPane, gbc);
        }
    }

    public ColorEditor getColorEditor() {
        return colorEditor;
    }

    public void setColorEditor(ColorEditor colorEditor) {
        this.colorEditor = colorEditor;
        for (int i = 0; i < colorPanes.length; i++) {
            colorPanes[i].setEditor(colorEditor);
        }
    }

    public Palette getPalette() {
        return palette;
    }
    
   public void setPalette(Palette palette) {
        this.palette = palette;
        Color[] colors = palette.getColors();
        for (int i = 0; i < colorPanes.length; i++) {
            if (i < colors.length) {
                colorPanes[i].updateColor(colors[i]);
                colorPanes[i].setVisible(true);
            } else {
                colorPanes[i].setVisible(false);
            }
        }
   }
   
   public Palette getUpdatedPalette() {
       Color[] colors = new Color[palette.getColors().length];
       for(int i=0; i < colorPanes.length; i++) {
           colors[i] = colorPanes[i].getCurrentColor();
       }
       return new Palette("New Palette", colors);
   }
}
