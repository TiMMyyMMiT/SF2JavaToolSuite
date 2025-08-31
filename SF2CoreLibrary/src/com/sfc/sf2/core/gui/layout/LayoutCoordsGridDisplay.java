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
    
    private BufferedImage coordsImageH;
    private BufferedImage coordsImageV;
    private int coordsH = -1;
    private int coordsV = -1;
    private int topPadding = 0;
    private int leftPadding = 0;
    private int fontIncrease = 0;
    private boolean cumulative = false;
    
    public LayoutCoordsGridDisplay(int rowSize, int columnSize, boolean cumulativeNumbers) {
        this(rowSize, columnSize, cumulativeNumbers, 0, 0, 0);
    }
    
    public LayoutCoordsGridDisplay(int rowSize, int columnSize, boolean cumulativeNumbers, int leftPadding, int topPadding, int fontIncrease) {
        this.coordsH = rowSize;
        this.coordsV = columnSize;
        this.cumulative = cumulativeNumbers;
        this.topPadding = topPadding;
        this.leftPadding = leftPadding;
        this.fontIncrease = fontIncrease;
    }
    
    public Dimension getOffset(int displayScale) {
        int w = coordsV <= 0 ? 0 : leftPadding+COORDS_PADDING_X+displayScale*COORDS_PADDING_SCALE;
        int h = coordsH <= 0 ? 0 : topPadding+COORDS_PADDING_Y+displayScale*COORDS_PADDING_SCALE;
        return new Dimension(w, h);
    }
    
    public void paintCoordsImage(Graphics graphics, int displayScale) {
        if (coordsH > 0) {
            int padding = coordsV <= 0 ? 0 : leftPadding+COORDS_PADDING_X+COORDS_PADDING_SCALE*displayScale;
            graphics.drawImage(coordsImageH, padding, 0, null);
        }
        if (coordsV > 0) {
            int padding = coordsH <= 0 ? 0 : topPadding+COORDS_PADDING_Y+COORDS_PADDING_SCALE*displayScale;
            graphics.drawImage(coordsImageV, 0, padding, null);
        }
    }
    
    public void buildCoordsImage(Dimension displayArea, int displayScale) {
        if (coordsH > 0) {
            coordsImageH = paintCoordsAxis(true, displayArea.width, coordsH, displayScale, fontIncrease, 1);
        }
        if (coordsV > 0) {
            int numberScale = cumulative ? displayArea.width/coordsH : 1;
            coordsImageV = paintCoordsAxis(false, displayArea.height, coordsV, displayScale, fontIncrease, numberScale);
        }
    }
    
    private BufferedImage paintCoordsAxis(boolean hAxis, int imageSize, int coordsSize, int displayScale, int fontIncrease, int numberScale) {
        imageSize *= displayScale;
        coordsSize *= displayScale;
        int padding = (hAxis ? COORDS_PADDING_Y+topPadding : COORDS_PADDING_X+leftPadding) + COORDS_PADDING_SCALE*displayScale;
        BufferedImage image = new BufferedImage(hAxis ? imageSize : padding, hAxis ? padding : imageSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D)image.getGraphics();
        int fontSize = 4+2*displayScale+2*fontIncrease;
        g2.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        FontMetrics fontMetrics = g2.getFontMetrics();
        g2.setColor(SettingsManager.getGlobalSettings().getIsDarkTheme() ? Color.WHITE : Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int count = imageSize/coordsSize;
        float halfPadding = padding*0.5f;
        float offset = coordsSize*0.5f + 1 + displayScale;
        for (int i = 0; i <= count; i++) {
            String item = Integer.toString(i*numberScale);
            float textWidth = (float)fontMetrics.getStringBounds(item, g2).getWidth();
            float x = hAxis ? i*coordsSize + offset - textWidth : halfPadding-1 - textWidth*0.5f;
            float y = hAxis ? halfPadding+1 + displayScale*0.5f : i*coordsSize+offset;
            g2.drawString(item, x, y);
        }
        g2.setColor(SettingsManager.getGlobalSettings().getIsDarkTheme() ? Color.BLACK : Color.WHITE);
        for (int i = 0; i <= count; i++) {
            if (hAxis) {
                g2.drawLine(i*coordsSize, 0, i*coordsSize, padding);
            } else {
                g2.drawLine(0, i*coordsSize, padding, i*coordsSize);
            }
        }
        if (hAxis) {
            g2.drawLine(imageSize-1, 0, imageSize-1, padding);
        } else {
            g2.drawLine(0, imageSize-1, padding, imageSize-1);
        }
        g2.dispose();
        return image;
    }
}
