/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.io;

import com.sfc.sf2.battle.EnemyEnums;
import com.sfc.sf2.core.io.asm.SF2EnumsAsmProcessor;
import com.sfc.sf2.helpers.StringHelpers;
import java.util.LinkedHashMap;

/**
 *
 * @author TiMMy
 */
public class EnemyEnumsAsmProcessor extends SF2EnumsAsmProcessor<EnemyEnums> {

    int itemEquippedValue = 0;
    int itemNothingValue = 0;

    public EnemyEnumsAsmProcessor() {
        super(new String[] { "Enemies", "Mapsprites", "AiCommandsets", "AiOrders", "SpawnSettings", "Items (bitfield)" });
    }
    
    @Override
    protected void parseLine(int categoryIndex, String line, LinkedHashMap<String, Integer> asmData) {
        switch (categoryIndex) {
            case 0:
                if (line.startsWith("ENEMY_")) {
                    line = line.substring(line.indexOf("_") + 1);
                    String name = line.substring(0, line.indexOf(":"));
                    int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    asmData.put(name, value);
                }
                break;
            case 1:
                if (line.startsWith("MAPSPRITE_")) {
                    line = line.substring(line.indexOf("_") + 1);
                    String name = line.substring(0, line.indexOf(":"));
                    int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    asmData.put(name, value);
                }
                break;
            case 2:
                if (line.startsWith("AICOMMANDSET_")) {
                    line = line.substring(line.indexOf("_") + 1);
                    String command = line.substring(0, line.indexOf(":"));
                    int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    asmData.put(command, value);
                }
                break;
            case 3:
                if (line.startsWith("AIORDER_")) {
                    line = line.substring(line.indexOf("_") + 1);
                    String order = line.substring(0, line.indexOf(":"));
                    int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    asmData.put(order, value);
                }
                break;
            case 4:
                if (line.startsWith("SPAWN_")) {
                    line = line.substring(line.indexOf("_") + 1);
                    String spawn = line.substring(0, line.indexOf(":"));
                    int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    asmData.put(spawn, value);
                }
                break;
            case 5:
                if (line.startsWith("ITEM_")) {
                    line = line.substring(line.indexOf("_") + 1);
                    String item = line.substring(0, line.indexOf(":"));
                    int value = 0;
                    if (item.equals("NOTHING")) {
                        value = itemNothingValue;
                    } else if (item.equals("EQUIPPED")) {
                        value = itemEquippedValue;
                    } else {
                        int commentIndex = line.indexOf(";");
                        if (commentIndex > -1) {
                            line = line.substring(0, commentIndex);
                        }
                        value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    }
                    asmData.put(item, value);
                } else if (line.startsWith("item")) {
                    //Get the ITEM_NOTHING & ITEM
                    //TODO .asm will store as string so the value doesn't matter. .bin will store value which will only support expanded item values
                    if (line.startsWith("itemNothing")) {
                        itemNothingValue = StringHelpers.getValueInt(line.substring(line.indexOf("=") + 1));
                    } else if (line.startsWith("itemEquipped")) {
                        itemEquippedValue = StringHelpers.getValueInt(line.substring(line.indexOf("=") + 1));
                    }
                }
                break;
        }
    }

    @Override
    protected EnemyEnums createEnumsData(LinkedHashMap<String, Integer>[] dataSets) {
        return new EnemyEnums(dataSets[0], dataSets[1], dataSets[2], dataSets[3], dataSets[4], dataSets[5]);
    }
}
