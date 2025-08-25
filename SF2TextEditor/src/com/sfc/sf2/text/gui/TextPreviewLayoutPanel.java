/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.text.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.CRAMColor;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.vwfont.FontSymbol;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class TextPreviewLayoutPanel extends AbstractLayoutPanel {
    
    private static final int PREVIEW_WIDTH = 290;
    private static final int PREVIEW_HEIGHT = 64;
    
    private static final int PANEL_TILE_CORNER = 6*16;
    private static final int PANEL_TILE_HORIZONTAL = 6*16+1;
    private static final int PANEL_TILE_VERTICAL = 7*16;
    private static final int PANEL_TILE_FILL = 2*16;
    
    private static final int FONT_START_HEIGHT = PIXEL_HEIGHT;
    private static final int FONT_LINE_HEIGHT = PIXEL_HEIGHT*2;
    private static final int FONT_START_X = 12;
    private static final int FONT_END_X = PREVIEW_WIDTH-12;
    private static final FontSymbol EMPTY_SYMBOL = FontSymbol.EmptySymbol();
    private static final Palette PREVIEW_PALETTE = new Palette(new CRAMColor[] { CRAMColor.BLACK, CRAMColor.WHITE, CRAMColor.LIGHT_GRAY }, true);
    
    private String text;
    Tileset baseTiles;
    FontSymbol[] fontSymbols;
    
    public TextPreviewLayoutPanel() {
        super();
        setBackground(Color.BLACK);
        bgCheckerPattern = false;
        this.setDisplayScale(2);
    }
    
    @Override
    protected boolean hasData() {
        return baseTiles != null && fontSymbols != null;    //Always draw background
    }

    @Override
    protected Dimension getImageDimensions() {
        return new Dimension(PREVIEW_WIDTH, PREVIEW_HEIGHT);
    }

    @Override
    protected void buildImage(Graphics graphics) {
        buildFrame(graphics);
        writeText(graphics);
    }
    
    void buildFrame(Graphics graphics) {
        Tile[] tiles = baseTiles.getTiles();
        graphics.drawImage(tiles[PANEL_TILE_CORNER].getIndexedColorImage(), 0, 0, null);
        graphics.drawImage(tiles[PANEL_TILE_CORNER].getIndexedColorImage(), PREVIEW_WIDTH-PIXEL_WIDTH, 0, null);
        graphics.drawImage(tiles[PANEL_TILE_CORNER].getIndexedColorImage(), 0, PREVIEW_HEIGHT-PIXEL_HEIGHT, null);
        graphics.drawImage(tiles[PANEL_TILE_CORNER].getIndexedColorImage(), PREVIEW_WIDTH-PIXEL_WIDTH, PREVIEW_HEIGHT-PIXEL_HEIGHT, null);
        graphics.drawImage(tiles[PANEL_TILE_HORIZONTAL].getIndexedColorImage(), PIXEL_WIDTH, 0, PREVIEW_WIDTH-PIXEL_WIDTH*2, PIXEL_HEIGHT, null);
        graphics.drawImage(tiles[PANEL_TILE_HORIZONTAL].getIndexedColorImage(), PIXEL_WIDTH, PREVIEW_HEIGHT-PIXEL_HEIGHT, PREVIEW_WIDTH-PIXEL_WIDTH*2, PIXEL_HEIGHT, null);
        graphics.drawImage(tiles[PANEL_TILE_VERTICAL].getIndexedColorImage(), 0, PIXEL_HEIGHT, PIXEL_WIDTH, PREVIEW_HEIGHT-PIXEL_HEIGHT*2, null);
        graphics.drawImage(tiles[PANEL_TILE_VERTICAL].getIndexedColorImage(), PREVIEW_WIDTH-PIXEL_WIDTH, PIXEL_HEIGHT, PIXEL_WIDTH, PREVIEW_HEIGHT-PIXEL_HEIGHT*2, null);
        graphics.drawImage(tiles[PANEL_TILE_FILL].getIndexedColorImage(), PIXEL_WIDTH, PIXEL_HEIGHT, PREVIEW_WIDTH-PIXEL_WIDTH*2, PREVIEW_HEIGHT-PIXEL_HEIGHT*2, null);
    }
    
    void writeText(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        int x = FONT_START_X;
        int y = FONT_START_HEIGHT;
        int next;
        text = "Testing symbols and such. String needs to be longer for testing.";
        for (int i = 0; i < text.length(); i++) {
            FontSymbol symbol = fontSymbols[i%fontSymbols.length];  //TODO Get proper symbols
            if (symbol == null) { symbol = EMPTY_SYMBOL; }
            next = x+symbol.getWidth()+1;
            if (next >= FONT_END_X) {   //Symbol will overrun
                y += FONT_LINE_HEIGHT;
                x = FONT_START_X;
                next = x+symbol.getWidth()+1;
            }
            graphics.drawImage(symbol.getIndexColoredImage(), x, y, null);
            x = next;
        }
    }
    
    public void setText(String text) {
        this.text = text;
        redraw();
    }
    
    public void setBaseTiles(Tileset baseTiles) {
        this.baseTiles = baseTiles;
    }
    
    public void setFontSymbols(FontSymbol[] fontSymbols) {
        this.fontSymbols = fontSymbols;
        for (int i = 0; i < fontSymbols.length; i++) {
            fontSymbols[i].setpalette(PREVIEW_PALETTE);
        }
    }
}
