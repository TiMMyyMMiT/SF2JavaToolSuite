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
import com.sfc.sf2.text.compression.Symbols;
import com.sfc.sf2.vwfont.FontSymbol;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author TiMMy
 */
public class TextPreviewLayoutPanel extends AbstractLayoutPanel {
    
    private static final int PREVIEW_WIDTH = 232;
    private static final int PREVIEW_HEIGHT = 64;
    
    private static final int PANEL_TILE_CORNER = 6*16;
    private static final int PANEL_TILE_HORIZONTAL = 6*16+1;
    private static final int PANEL_TILE_VERTICAL = 7*16;
    private static final int PANEL_TILE_FILL = 2*16;
    
    private static final int FONT_START_HEIGHT = PIXEL_HEIGHT;
    private static final int FONT_LINE_HEIGHT = PIXEL_HEIGHT*2;
    private static final int FONT_START_X = 10;
    private static final int FONT_END_X = PREVIEW_WIDTH-10;
    private static final FontSymbol EMPTY_SYMBOL = FontSymbol.EmptySymbol();
    private static final Palette PREVIEW_PALETTE = new Palette(new CRAMColor[] { CRAMColor.BLACK, CRAMColor.WHITE, CRAMColor.LIGHT_GRAY }, true);
    
    private String text;
    private int linesOffset = 0;
    private boolean linesAbove = false;
    private boolean linesBelow = false;
    
    private Tileset baseTiles;
    private FontSymbol[] fontSymbols;
    private String[] allyNames;
    
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
        if (text != null) {
            writeText(graphics);
            graphics.setColor(Color.LIGHT_GRAY);
            if (linesAbove) {
                graphics.fillArc(PREVIEW_WIDTH-20, -5, 20, 20, -90-25, 50);
            }
            if (linesBelow) {
                graphics.fillArc(PREVIEW_WIDTH-20, PREVIEW_HEIGHT-15, 20, 20, 90-25, 50);
            }
        }
    }
    
    void buildFrame(Graphics graphics) {
        Tile[] tiles = baseTiles.getTiles();
        drawTile(graphics, tiles[PANEL_TILE_CORNER], 0, 0, PIXEL_WIDTH, PIXEL_HEIGHT, false, false);
        drawTile(graphics, tiles[PANEL_TILE_CORNER], PREVIEW_WIDTH-PIXEL_WIDTH, 0, PIXEL_WIDTH, PIXEL_HEIGHT, true, false);
        drawTile(graphics, tiles[PANEL_TILE_CORNER], 0, PREVIEW_HEIGHT-PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT, false, true);
        drawTile(graphics, tiles[PANEL_TILE_CORNER], PREVIEW_WIDTH-PIXEL_WIDTH, PREVIEW_HEIGHT-PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT, true, true);
        drawTile(graphics, tiles[PANEL_TILE_HORIZONTAL], PIXEL_WIDTH, 0, PREVIEW_WIDTH-PIXEL_WIDTH*2, PIXEL_HEIGHT, false, false);
        drawTile(graphics, tiles[PANEL_TILE_HORIZONTAL], PIXEL_WIDTH, PREVIEW_HEIGHT-PIXEL_HEIGHT, PREVIEW_WIDTH-PIXEL_WIDTH*2, PIXEL_HEIGHT, false, true);
        drawTile(graphics, tiles[PANEL_TILE_VERTICAL], 0, PIXEL_HEIGHT, PIXEL_WIDTH, PREVIEW_HEIGHT-PIXEL_HEIGHT*2, false, false);
        drawTile(graphics, tiles[PANEL_TILE_VERTICAL], PREVIEW_WIDTH-PIXEL_WIDTH, PIXEL_HEIGHT, PIXEL_WIDTH, PREVIEW_HEIGHT-PIXEL_HEIGHT*2, true, false);
        drawTile(graphics, tiles[PANEL_TILE_FILL], PIXEL_WIDTH, PIXEL_HEIGHT, PREVIEW_WIDTH-PIXEL_WIDTH*2, PREVIEW_HEIGHT-PIXEL_HEIGHT*2, false, false);
    }
    
    void drawTile(Graphics graphics, Tile tile, int x, int y, int width, int height, boolean flipH, boolean flipV) {
        if (flipH) {
            width *= -1;
            x -= width;
        }
        if (flipV) {
            height *= -1;
            y -= height;
        }
        graphics.drawImage(tile.getIndexedColorImage(), x, y, width, height, null);
    }
    
    void writeText(Graphics graphics) {
        linesAbove = false;
        linesBelow = false;
        int width = FONT_END_X-FONT_START_X;
        int height = FONT_LINE_HEIGHT*3;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        char c;
        int next;
        boolean newline;
        int x = 0;
        int y = FONT_LINE_HEIGHT*(-linesOffset);
        if (y < 0) {
            linesAbove = true;
        }
        for (int i = 0; i < text.length(); i++) {
            newline = false;
            c = text.charAt(i);
            if (c == '\n') {
                newline = true;
                y += FONT_LINE_HEIGHT;
                x = 0;
            } else {
                FontSymbol symbol = findSymbol(c);
                if (symbol == null) { symbol = EMPTY_SYMBOL; }
                next = x+symbol.getWidth()+1;
                if (next >= width) {   //Symbol will overrun
                    newline = true;
                    y += FONT_LINE_HEIGHT;
                    x = 0;
                    next = x+symbol.getWidth()+1;
                }
                g.drawImage(symbol.getIndexColoredImage(), x, y, null);
                x = next;
            }
            if (newline && y >= height) {
                linesBelow = true;
            }
        }
        g.dispose();
        graphics.drawImage(image, FONT_START_X, FONT_START_HEIGHT, null);
    }
    
    private FontSymbol findSymbol(char symbolChar) {
        String c = Character.toString(symbolChar);
        String[] table = Symbols.TABLE();
        for (int i = 0; i < table.length; i++) {
            if (table[i].equals(c)) {
                return fontSymbols[i-1];
            }
        }
        return EMPTY_SYMBOL;
    }
    
    private String parseTags(String text) {
        char c;
        int i = 0;
        int tagStart, tagEnd;
        while (i < text.length()) {
            c = text.charAt(i);
            if (c == '{') {
                tagStart = i;
                do {
                    i++;
                    c = text.charAt(i);
                } while (c != '}' && i < text.length());
                tagEnd = i+1;
                String replace = parseTag(text.substring(tagStart+1, tagEnd-1));
                text = text.substring(0, tagStart) + replace + text.substring(tagEnd);
                i = tagStart+replace.length();
            } else {
                i++;
            }
        }
        if (text.charAt(text.length()-1) == '\n') {
            text = text.substring(0, text.length()-1);
        }
        return text;
    }
    
    String parseTag(String tag) {
        if (tag.startsWith("NAME;")) {  //Special case for name input
            String numVal = tag.substring(5);
            int val = -1;
            try {
                val = Integer.parseInt(numVal);
            } catch (NumberFormatException e) {}
            if (allyNames == null || val < 0 || val >= allyNames.length) {
                return "ALLY_" + val;
            } else {
                return allyNames[val];
            }
        }
        
        switch (tag) {
            case "LEADER":
                if (allyNames == null || allyNames.length == 0) {
                    return "LEADER";
                } else {
                    return allyNames[0];
                }
            case "NAME":
                return "ALLY_X";
            case "ITEM":
                return "ITEM_X";
            case "SPELL":
                return "SPELL_X";
            case "CLASS":
                return "CLASS_X";
            case "#":
                return "00";
            case "N":
            case "W1":
                return "\n";
            case "W2":
                return "";
            case "DICT":
                return "item 1, item 2, item 3...";
            case "D1":
            case "D2":
            case "D3":
            case "COLOR":
            case "CLEAR":
            case "START/EOL)":
            default:
                return "";
        }
    }
    
    public void setText(String text) {
        if (text == null) {
            this.text = text;
            redraw();
            return;
        }
        text = parseTags(text);
        this.text = text;
        linesOffset = 0;
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
    
    public void setAllyNames(String[] allyNames) {
        this.allyNames = allyNames;
    }
    
    public int getLinesOffset() {
        return linesOffset;
    }
    
    public void setLinesOffset(int linesOffset) {
        this.linesOffset = linesOffset;
        redraw();
    }
    
    public boolean hasLinesBelow() {
        return linesBelow;
    }
    
    public boolean hasLinesAbove() {
        return linesAbove;
    }
}
