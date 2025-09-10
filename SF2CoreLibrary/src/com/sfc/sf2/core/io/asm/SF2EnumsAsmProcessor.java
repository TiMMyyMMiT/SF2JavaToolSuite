/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io.asm;

import com.sfc.sf2.helpers.StringHelpers;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;


/**
 *
 * @author TiMMy
 */
public class SF2EnumsAsmProcessor extends AbstractAsmProcessor<SF2EnumsAsmData, SF2EnumsAsmData> {

    @Override
    protected SF2EnumsAsmData parseAsmData(BufferedReader reader, SF2EnumsAsmData pckg) throws IOException, AsmException {
        LinkedHashMap<String, Integer> temp = new LinkedHashMap();
        String[] categories = pckg.getCategories();
        String line;
        int categoriesRead = 0;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("; enum")) {
                line = line.substring(7).trim();
                for (int i = 0; i < categories.length; i++) {
                    if (line.equals(categories[i])) {
                        //Found a category
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("; ---")) {
                                categoriesRead++;
                                break;  //Category end
                            } else if (line.length() > 0 && line.charAt(0) != ';' && line.charAt(0) != ' ') {
                                line = StringHelpers.trimAndRemoveComments(line);
                                int colonIndex = line.indexOf(':');
                                if (colonIndex > -1) {
                                    //Is a valid item
                                    String item = line.substring(0, colonIndex);
                                    int value = 0x7F;
                                    String stringVal = StringHelpers.trimAndRemoveComments(line.substring(colonIndex+1).replace("equ", ""));
                                    if (stringVal.charAt(0) == '$' || (stringVal.charAt(0) >= '0' && stringVal.charAt(0) <= '9')) {
                                        value = StringHelpers.getValueInt(stringVal);
                                    } else if (temp.containsKey(stringVal)) {
                                        //Value is not in expected format. Possibly because value is assigned via variable
                                        //TODO How to handle difference between "vanilla" build and "standard" build
                                        value = temp.get(stringVal);
                                    }
                                    pckg.addEnum(categories[i], item, value);
                                } else {
                                    //Special case to handle things like Items placeholders
                                    int equalsIndex = line.indexOf('=');
                                    if (equalsIndex == -1) continue;
                                    String id = StringHelpers.trimAndRemoveComments(line.substring(0, equalsIndex));
                                    int val = StringHelpers.getValueInt(StringHelpers.trimAndRemoveComments(line.substring(equalsIndex+1)));
                                    if (temp.containsKey(id)) {
                                        temp.replace(id, val);
                                    } else {
                                        temp.put(id, val);
                                    }
                                }
                            }
                        }
                    }
                }
                if (categoriesRead >= categories.length) {
                    break;  //No need to read rest of the file
                }
            }
        }
        return pckg;
    }

    @Override
    protected String getHeaderName(SF2EnumsAsmData item) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void packageAsmData(FileWriter writer, SF2EnumsAsmData item, SF2EnumsAsmData pckg) throws IOException, AsmException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
