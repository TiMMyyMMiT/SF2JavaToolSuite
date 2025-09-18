/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.dialog.properties;

import com.sfc.sf2.core.AbstractEnums;
import com.sfc.sf2.mapsprite.MapSprite;
import com.sfc.sf2.portrait.Portrait;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

/**
 *
 * @author TiMMy
 */
public class DialogPropertiesEnums extends AbstractEnums {
    
    private final LinkedHashMap<String, Integer> mapSprites;
    private final LinkedHashMap<String, Integer> portraits;
    private final LinkedHashMap<String, Integer> sfx;
    
    private MapSprite[] mapSpriteImages;
    private Portrait[] portraitImages;

    public DialogPropertiesEnums(LinkedHashMap<String, Integer> mapSprites, LinkedHashMap<String, Integer> portraits, LinkedHashMap<String, Integer> sfx) {
        this.mapSprites = mapSprites;
        this.portraits = portraits;
        this.sfx = sfx;
    }

    public void setImages(MapSprite[] mapSpriteImages, Portrait[] portraitImages) {
        this.mapSpriteImages = mapSpriteImages;
        this.portraitImages = portraitImages;
    }
    
    public LinkedHashMap<String, Integer> getMapSprites() {
        return mapSprites;
    }

    public LinkedHashMap<String, Integer> getPortraits() {
        return portraits;
    }

    public LinkedHashMap<String, Integer> getSfx() {
        return sfx;
    }
    
    public BufferedImage getMapSpriteFor(String name) {
        int index = -1;
        if (mapSprites.containsKey(name)) index = mapSprites.get(name);
        if (index >= 0 && index < mapSpriteImages.length) {
            return mapSpriteImages[index].getFrame(2, 0).getIndexedColorImage(2);
        } else {
            return null;
        }
    }
    
    public BufferedImage getPortraitFor(String name) {
        int index = -1;
        if (portraits.containsKey(name)) index = portraits.get(name);
        if (index >= 0 && index < portraitImages.length) {
            return portraitImages[index].getIndexedColorImage(false, false, false);
        } else {
            return null;
        }
    }
}
