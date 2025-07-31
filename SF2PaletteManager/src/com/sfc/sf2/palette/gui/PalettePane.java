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
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 *
 * @author wiz
 */
public class PalettePane extends JPanel {
    
    private ColorEditor colorEditor;
    private Palette palette;
    private ColorPane[] currentColorPanes;
    
    public PalettePane(Palette palette) {
        setLayout(new GridBagLayout());
        setPalette(palette);
   }
    
    public PalettePane(){
        Color[] colors = {};
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        for (int col = 0; col < colors.length; col++) {
            gbc.gridx = col;

            ColorPane colorPane = new ColorPane(colors[col], colorEditor);
            Border border = null;
            if (col < colors.length-1) {
                border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
            } else {
                border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
            }
            colorPane.setBorder(border);
            add(colorPane, gbc);
        }
    }

    public ColorPane[] getCurrentColorPanes() {
        return currentColorPanes;
    }

    public void setCurrentColorPanes(ColorPane[] currentColorPanes) {
        this.currentColorPanes = currentColorPanes;
    }

    public ColorEditor getColorEditor() {
        return colorEditor;
    }

    public void setColorEditor(ColorEditor colorEditor) {
        this.colorEditor = colorEditor;
    }

    public Palette getPalette() {
        return palette;
    }
    
   public void setPalette(Palette palette) {
        this.removeAll();
        this.palette = palette;
        Color[] colors = palette.getColors();
        this.currentColorPanes = new ColorPane[colors.length];
        GridBagConstraints gbc = new GridBagConstraints();
        for (int col = 0; col < colors.length; col++) {
            gbc.gridx = col;
            ColorPane colorPane = new ColorPane(colors[col],colorEditor);
            currentColorPanes[col] = colorPane;
            add(colorPane, gbc);
        }
   }
   
   public Palette getUpdatedColors() {
       Color[] colors = palette.getColors();
       for(int i=0;i<currentColorPanes.length;i++){
           colors[i] = currentColorPanes[i].getCurrentColor();
       }
       return palette;
   }
}
