/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.dialog.properties;

/**
 *
 * @author wiz
 */
public class DialogProperty {
    
    private String spriteName;
    private String portraitName;
    private String sfxName;

    public DialogProperty(String spriteName, String portraitName, String sfxName) {
        this.spriteName = spriteName;
        this.portraitName = portraitName;
        this.sfxName = sfxName;
    }

    public String getSpriteName() {
        return spriteName;
    }

    public void setSpriteName(String spriteName) {
        this.spriteName = spriteName;
    }

    public String getPortraitName() {
        return portraitName;
    }

    public void setPortraitName(String portraitName) {
        this.portraitName = portraitName;
    }

    public String getSfxName() {
        return sfxName;
    }

    public void setSfxName(String sfxName) {
        this.sfxName = sfxName;
    }
    
    @Override
    public DialogProperty clone() {
        return new DialogProperty(spriteName, portraitName, sfxName);
    }
    
    public static DialogProperty emptyDialogPropertiesEntry() {
        return new DialogProperty("EMPTY", "EMPTY", "EMPTY");
    }
}
