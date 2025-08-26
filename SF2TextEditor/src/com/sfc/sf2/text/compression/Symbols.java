/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.text.compression;

import java.util.HashMap;


/**
 *
 * @author TiMMy
 */
public class Symbols {
    // <editor-fold defaultstate="collapsed" desc="Generated Code">  
    private static final String[] DEFAULT_TABLE = {
        "\\UNUSED",
        " ",
        "0",
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z",
        "a",
        "b",
        "c",
        "d",
        "e",
        "f",
        "g",
        "h",
        "i",
        "j",
        "k",
        "l",
        "m",
        "n",
        "o",
        "p",
        "q",
        "r",
        "s",
        "t",
        "u",
        "v",
        "w",
        "x",
        "y",
        "z",
        "_",
        "-",
        ".",
        ",",
        "!",
        "?",
        "<", /* Represents character '“' (ALT 0147) for easier writing in game script */
        ">", /* Represents character '”' (ALT 0148) for easier writing in game script*/
        "'",
        "(",
        ")",
        "#",
        "%",
        "&",
        "+",
        "/",
        ":",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "\\UNUSED",
        "{DICT}",
        "{N}",
        "{D2}",
        "{#}",
        "{NAME}",
        "{LEADER}",
        "{ITEM}",
        "{SPELL}",
        "{CLASS}",
        "{W2}",
        "{D1}",
        "{D3}",
        "{W1}",
        "{CLEAR}",
        "{NAME",
        "{COLOR",
        "(START/EOL)",
        "\\UNUSED"
    };
    // </editor-fold>
    
    private static String[] symbolTable;
    private static final HashMap<String, Integer> stringMap = new HashMap<>();
    
    static {
        symbolTable = DEFAULT_TABLE;
        for (int i = 0; i < DEFAULT_TABLE.length; i++) {
            if (!stringMap.containsKey(DEFAULT_TABLE[i])) {
                stringMap.put(DEFAULT_TABLE[i], i);
            }
        }
    }
    
    public static int fromChar(char val) {
        return fromString(Character.toString(val));
    }
    
    public static int fromString(String val) {
        if (stringMap.containsKey(val)) {
            return stringMap.get(val);
        }
        return 1;
    }
    
    public static String fromInt(int val) {
        return symbolTable[val];
    }
    
    public static int count() {
        return symbolTable.length;
    }
    
    public static void setImportedTable(int[] importedTable) {
        char c;
        String s;
        stringMap.clear();
        for (int i = 0; i < importedTable.length; i++) {
            if (i >= 238) {
                stringMap.put(DEFAULT_TABLE[i], i);   //For tags, use default table
            } else if (importedTable[i] != 1) {
                c = (char)i;
                     if (c == '“') c = '<';
                else if (c == '”') c = '>';
                s = Character.toString(c);
                symbolTable[i] = s;
                if (!stringMap.containsKey(s)) {
                    stringMap.put(s, importedTable[i]);
                }
            }
        }
    }
}
