/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

import com.sfc.sf2.core.settings.SettingsManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 *
 * @author TiMMy
 */
public class LayoutCoordsGridDisplay extends BaseLayoutComponent {
    private static final int COORDS_PADDING_X = 6;
    private static final int COORDS_PADDING_Y = 6;
    private static final int COORDS_PADDING_SCALE = 2;
    
    private BufferedImage coordsImageX;
    private BufferedImage coordsImageY;
    private int coordsX = -1;
    private int coordsY = -1;
    private int verticalPadding = 0;
    
    public LayoutCoordsGridDisplay(int rowSize, int columnSize, int verticalPadding) {
        this.coordsX = rowSize;
        this.coordsY = columnSize;
        this.verticalPadding = verticalPadding;
    }
    
    public Dimension getOffset(int displayScale) {
        return new Dimension(verticalPadding+COORDS_PADDING_X+displayScale*COORDS_PADDING_SCALE, COORDS_PADDING_Y+displayScale*COORDS_PADDING_SCALE);
    }
    
    public void paintCoordsImage(Graphics graphics, Dimension displayArea, int displayScale) {
        coordsImageX = paintCoordsAxis(true, displayArea.width, coordsX, displayScale);
        coordsImageY = paintCoordsAxis(false, displayArea.height, coordsY, displayScale);
        Dimension offset = getOffset(displayScale);
        graphics.drawImage(coordsImageX, offset.width, 0, null);
        graphics.drawImage(coordsImageY, 0, offset.height, null);
    }
    
    private BufferedImage paintCoordsAxis(boolean xAxis, int imageSize, int coordsSize, int displayScale) {
        imageSize *= displayScale;
        coordsSize *= displayScale;
        int padding = (xAxis ? COORDS_PADDING_Y : COORDS_PADDING_X+verticalPadding) + COORDS_PADDING_SCALE*displayScale;
        BufferedImage image = new BufferedImage(xAxis ? imageSize : padding, xAxis ? padding : imageSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D)image.getGraphics();
        int fontSize = 4+2*displayScale;
        g2.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        FontMetrics fontMetrics = g2.getFontMetrics();
        g2.setColor(SettingsManager.getGlobalSettings().getIsDarkTheme() ? Color.WHITE : Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int count = imageSize/coordsSize;
        float halfPadding = padding*0.5f;
        float offset = coordsSize*0.5f + 1 + displayScale;
        for (int i = 0; i <= count; i++) {
            String item = Integer.toString(i);
            float textWidth = (float)fontMetrics.getStringBounds(item, g2).getWidth();
            float x = xAxis ? i*coordsSize + offset - textWidth : halfPadding-1 - textWidth*0.5f;
            float y = xAxis ? halfPadding+1 + displayScale*0.5f : i*coordsSize+offset;
            g2.drawString (Integer.toString(i), x, y);
        }
        g2.setColor(SettingsManager.getGlobalSettings().getIsDarkTheme() ? Color.BLACK : Color.WHITE);
        for (int i = 0; i <= count; i++) {
            if (xAxis) {
                g2.drawLine(i*coordsSize, 0, i*coordsSize, padding);
            } else {
                g2.drawLine(0, i*coordsSize, padding, i*coordsSize);
            }
        }
        if (xAxis) {
            g2.drawLine(imageSize-1, 0, imageSize-1, padding);
        } else {
            g2.drawLine(0, imageSize-1, padding, imageSize-1);
        }
        g2.dispose();
        return image;
    }
}
