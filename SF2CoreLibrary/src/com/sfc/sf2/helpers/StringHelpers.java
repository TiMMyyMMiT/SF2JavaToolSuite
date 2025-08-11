/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

/**
 *
 * @author TiMMy
 */
public class StringHelpers {
    public static String trimAndRemoveComments(String line) {
        int commentIndex = line.lastIndexOf(';');
        if (commentIndex < 0) {
            return line.trim();
        } else {
            return line.substring(0, commentIndex).trim();
        }
    }
}
