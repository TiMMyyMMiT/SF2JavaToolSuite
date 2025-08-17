/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.specialSprites.gui;

import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.gui.TilesetLayoutPanel;

/**
 *
 * @author TiMMy
 */
public class SpecialSpriteLayoutPanel extends TilesetLayoutPanel {
    
    @Override
    public void setTileset(Tileset tileset) {
        super.setTileset(tileset);
        setGridDimensions(8, 8, tileset.getTilesPerRow()/2*8, -1);
    }
    
}
