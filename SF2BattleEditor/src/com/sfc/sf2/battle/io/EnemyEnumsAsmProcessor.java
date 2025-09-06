/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.io;

import com.sfc.sf2.battle.EnemyEnums;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.StringHelpers;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 *
 * @author TiMMy
 */
public class EnemyEnumsAsmProcessor extends AbstractAsmProcessor<EnemyEnums, EmptyPackage> {

    @Override
    protected EnemyEnums parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        
        LinkedHashMap<String, Integer> enemies = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> items = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> aiCommandSets = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> aiOrders = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> spawnParams = new LinkedHashMap<>();

        int itemEquippedValue = 0;
        int itemNothingValue = 0;
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.trim().startsWith("; enum Enemies")) {
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("ENEMY_")) {
                        line = line.substring(line.indexOf("_") + 1);
                        String name = line.substring(0, line.indexOf(":"));
                        int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                        enemies.put(name, value);
                    } else if (line.startsWith("; --")) {
                        break;
                    }
                }

                Console.logger().finest("Enemies imported: " + enemies.size() + " entries.");
            } else if (line.trim().startsWith("; enum AiCommandsets")) {
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("AICOMMANDSET_")) {
                        line = line.substring(line.indexOf("_") + 1);
                        String command = line.substring(0, line.indexOf(":"));
                        int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                        aiCommandSets.put(command, value);
                    } else if (line.startsWith("; --")) {
                        break;
                    }
                }

                Console.logger().finest("AI Commands imported: " + aiCommandSets.size() + " entries.");
            } else if (line.trim().startsWith("; enum AiOrders")) {
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("AIORDER_")) {
                        line = line.substring(line.indexOf("_") + 1);
                        String order = line.substring(0, line.indexOf(":"));
                        int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                        aiOrders.put(order, value);
                    } else if (line.startsWith("; --")) {
                        break;
                    }
                }

                Console.logger().finest("AI Orders imported: " + aiOrders.size() + " entries.");
            } else if (line.trim().startsWith("; enum SpawnSettings")) {
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("SPAWN_")) {
                        line = line.substring(line.indexOf("_") + 1);
                        String spawn = line.substring(0, line.indexOf(":"));
                        int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                        spawnParams.put(spawn, value);
                    } else if (line.startsWith("; --")) {
                        break;
                    }
                }

                Console.logger().finest("Spawn Params imported: " + spawnParams.size() + " entries.");
            } else if (line.trim().startsWith("; enum Items")) {
                while ((line = reader.readLine()) != null) {
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
                        items.put(item, value);
                    } else if (line.startsWith("item")) {
                        //Get the ITEM_NOTHING & ITEM
                        //TODO .asm will store as string so the value doesn't matter. .bin will store value which will only support expanded item values
                        if (line.startsWith("itemNothing")) {
                            itemNothingValue = StringHelpers.getValueInt(line.substring(line.indexOf("=") + 1));
                        } else if (line.startsWith("itemEquipped")) {
                            itemEquippedValue = StringHelpers.getValueInt(line.substring(line.indexOf("=") + 1));
                        }
                    } else if (line.startsWith("; --")) {
                        break;
                    }
                }

                Console.logger().finest("Items imported: " + items.size() + " entries.");
            }
        }
        return new EnemyEnums(enemies, items, aiCommandSets, aiOrders, spawnParams);
    }

    @Override
    protected String getHeaderName(EnemyEnums item) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void packageAsmData(FileWriter writer, EnemyEnums item, EmptyPackage pckg) throws IOException, AsmException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
