/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.application.settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author TiMMy
 */
public class SettingsManager {
    private static final String SETTINGS_FILE_PATH = ".\\.sf2settings.txt";
    
    static final HashMap<String, AbstractSettings> settingsStores = new HashMap<>();

    static {
        CoreSettings core = new CoreSettings();
        settingsStores.put("core", core);
    }
    
    public static void registerSettingsStore(String id, AbstractSettings settings) {
        if (settings.getClass().toString().equals("CoreSettings")) {
            System.out.println("Error: Cannot add another instance of \"Core\" settings.");
            return;
        } else if (settingsStores.containsKey(id)) {
            System.out.println("Error: Cannot add duplicate instance of " + id + " settings.");
            return;
        }
        settingsStores.put(id, settings);
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends AbstractSettings> T getSettingsStore(String id) {
        if (settingsStores.containsKey(id)) {
            return (T)settingsStores.get(id);
        }
        return null;
    }
    
    public static void loadSettingsFile() {
        try {
            File file = new File(SETTINGS_FILE_PATH);
            if (file.exists()) {
                Scanner scan = new Scanner(file);
                String line = scan.nextLine();
                String storeId = null;
                HashMap<String, String> data = null;
                while (scan.hasNext()) {
                    if (line.startsWith("Store_")) {
                        storeId = line.substring(line.indexOf("_")+1).trim();
                        if (settingsStores.containsKey(storeId)) {
                            data = new HashMap<>();
                            while (scan.hasNext()) {
                                line = scan.nextLine();
                                if (line.startsWith("Store_")) {
                                    break;
                                } else {
                                    String[] split = line.split(":");
                                    if (split.length > 1) {
                                        data.put(split[0].trim(), split[1].trim());
                                    }
                                }
                            }
                            settingsStores.get(storeId).decodeSettings(data);
                        }
                    }
                }
                scan.close();
            } else {
                for (Map.Entry<String, AbstractSettings> entry : settingsStores.entrySet()) {
                    entry.getValue().initialiseNewUser();
                }
                saveSettingsFile();
            }
        } catch (IOException ex) {
            System.getLogger(SettingsManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (Exception e) {
            System.out.println("Error loading settings file. " + e);
        }
    }
    
    public static void saveSettingsFile() {
        try {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, AbstractSettings> entry : settingsStores.entrySet()) {
                    sb.append("Store_" + entry.getKey() + "\n");
                    HashMap<String, String> data = new HashMap<>();
                    entry.getValue().encodeSettings(data);
                    for (Map.Entry<String, String> dataItem : data.entrySet()) {
                        sb.append(dataItem.getKey() + ": " + dataItem.getValue() + "\n");
                    }
                    sb.append("\n");
                }
                Path filepath = Paths.get(SETTINGS_FILE_PATH);                
                Files.write(filepath, sb.toString().getBytes());
        } catch (IOException ex) {
            System.getLogger(SettingsManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (Exception e) {
            System.out.println("Error saving settings file. " + e);
        }
    }
}
