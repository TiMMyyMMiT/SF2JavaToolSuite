/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 *
 * @author TiMMy
 */
public class GraphicsHelpers {
    
    public static void drawBackgroundTransparencyPattern(BufferedImage image, Color bgColor) {
        Color bgDarkerColor = bgColor.darker();
        int bgInt = bgColor.getRGB();
        int darkInt = bgDarkerColor.getRGB();
        int[] data = ((DataBufferInt)(image.getRaster().getDataBuffer())).getData();
        int width = image.getWidth();
        int height = image.getHeight();
        int offset;
        for (int j = 0; j < height; j++) {
            offset = ((j/12)%2) == 0 ? 12 : 0;
            for (int i = 0; i < width; i++) {
                data[i+j*width] = ((i+j*width+offset)%24) < 12 ? bgInt : darkInt;
            }
        }
    }
    
    public static void drawGrid(BufferedImage image, int gridW, int gridH, int gridThickness) {
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(gridThickness));
        int x = 0;
        int y = 0;
        if (gridW >= 0) {
            while (x < image.getWidth()) {
                graphics.drawLine(x, 0, x, image.getHeight());
                x += gridW;
            }
            graphics.drawLine(x-1, 0, x-1, image.getHeight());
        }
        if (gridH >= 0) {
            while (y < image.getHeight()) {
                graphics.drawLine(0, y, image.getWidth(), y);
                y += gridH;
            }
            graphics.drawLine(0, y-1, image.getWidth(), y-1);
        }
        graphics.dispose();
    }
}
