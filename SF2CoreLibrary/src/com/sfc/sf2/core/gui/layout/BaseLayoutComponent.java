/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

/**
 *
 * @author TiMMy
 */
public abstract class BaseLayoutComponent {
    
    boolean isEnabled = true;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }
    
    public static boolean IsEnabled(BaseLayoutComponent component) {
        return component != null && component.isEnabled;
    }
}
