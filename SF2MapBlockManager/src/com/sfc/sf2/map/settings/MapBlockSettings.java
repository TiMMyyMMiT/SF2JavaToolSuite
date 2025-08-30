/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.settings;

import com.sfc.sf2.core.settings.AbstractSettings;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class MapBlockSettings implements AbstractSettings {

    private Color blocksetBGColor;
    private Color tilesetBGColor;
    private Color blockBGColor;
    
    public Color getBlocksetBGColor() {
        return blocksetBGColor;
    }
    
    public void setBlocksetBGColor(Color blocksetBGColor) {
        this.blocksetBGColor = blocksetBGColor;
    }
    
    public Color getTilesetBGColor() {
        return tilesetBGColor;
    }
    
    public void setTilesetBGColor(Color tilesetBGColor) {
        this.tilesetBGColor = tilesetBGColor;
    }
    
    public Color getBlockBGColor() {
        return blockBGColor;
    }
    
    public void setBlockBGColor(Color blockBGColor) {
        this.blockBGColor = blockBGColor;
    }
    
    @Override
    public void initialiseNewUser() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("blocksetBGColor")) {
            String[] colorSplit = data.get("blocksetBGColor").split(",");
            blocksetBGColor = new Color(Integer.parseInt(colorSplit[0].trim()), Integer.parseInt(colorSplit[1].trim()), Integer.parseInt(colorSplit[2].trim()));
        }
        if (data.containsKey("tilesetBGColor")) {
            String[] colorSplit = data.get("tilesetBGColor").split(",");
            tilesetBGColor = new Color(Integer.parseInt(colorSplit[0].trim()), Integer.parseInt(colorSplit[1].trim()), Integer.parseInt(colorSplit[2].trim()));
        }
        if (data.containsKey("blockBGColor")) {
            String[] colorSplit = data.get("blockBGColor").split(",");
            tilesetBGColor = new Color(Integer.parseInt(colorSplit[0].trim()), Integer.parseInt(colorSplit[1].trim()), Integer.parseInt(colorSplit[2].trim()));
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("tilesetBGColor", String.format("%d, %d, %d", tilesetBGColor.getRed(), tilesetBGColor.getGreen(), tilesetBGColor.getBlue()));
        data.put("blockBGColor", String.format("%d, %d, %d", blockBGColor.getRed(), blockBGColor.getGreen(), blockBGColor.getBlue()));
    }
}
