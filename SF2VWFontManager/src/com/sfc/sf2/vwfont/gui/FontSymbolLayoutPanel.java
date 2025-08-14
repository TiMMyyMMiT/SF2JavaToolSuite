/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.vwfont.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.vwfont.FontSymbol;
import static com.sfc.sf2.vwfont.FontSymbol.PIXEL_HEIGHT;
import static com.sfc.sf2.vwfont.FontSymbol.PIXEL_WIDTH;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author wiz
 */
public class FontSymbolLayoutPanel extends AbstractLayoutPanel {
    
    private static final int DEFAULT_TILES_PER_ROW = 8;
        
    private FontSymbol[] symbols;
    private boolean drawWidthMarker;
    
    public FontSymbolLayoutPanel() {
        super();
        tilesPerRow = DEFAULT_TILES_PER_ROW;    //Symbols per row
        setGridDimensions(16, 16);
    }

    @Override
    protected boolean hasData() {
        return symbols != null && symbols.length > 0;
    }
    
    @Override
    protected Dimension getImageDimensions() {
        int w = tilesPerRow * PIXEL_WIDTH;
        int h = (symbols.length/tilesPerRow) * PIXEL_HEIGHT;
        if (symbols.length%tilesPerRow != 0) {
            h += PIXEL_HEIGHT;
        }
        return new Dimension(w, h);
    }

    @Override
    protected void buildImage(Graphics graphics) {
        if (drawWidthMarker) {
            graphics.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i < symbols.length; i++) {
                int x = symbols[i].getWidth() + (i%tilesPerRow)*PIXEL_WIDTH;
                int y = (i/tilesPerRow)*PIXEL_HEIGHT;
                graphics.drawLine(x, y, x, y+PIXEL_HEIGHT);
            }
        }
        
        for (int i = 0; i < symbols.length; i++) {
            graphics.drawImage(symbols[i].getIndexColoredImage(), (i%tilesPerRow)*PIXEL_WIDTH, (i/tilesPerRow)*PIXEL_HEIGHT, null);
        }
    }

    public FontSymbol[] getFontSymbols() {
        return symbols;
    }

    public void setFontSymbols(FontSymbol[] symbols) {
        this.symbols = symbols;
    }
}
