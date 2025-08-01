/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.application.settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
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
        loadSettings(null);
    }
    
    public static void loadSpecificSettings(String id) {
        loadSettings(id);
    }
    
    private static void loadSettings(String specificId) {
        String line = null;
        try {
            File file = new File(SETTINGS_FILE_PATH);
            if (file.exists()) {
                Scanner scan = new Scanner(file);
                line = scan.nextLine();
                String storeId = null;
                HashMap<String, String> data = null;
                while (scan.hasNext()) {
                    if (line.startsWith("Store_")) {
                        storeId = line.substring(line.indexOf("_")+1).trim();
                        if (settingsStores.containsKey(storeId) && (specificId == null || storeId.equals(specificId))) {
                            data = new HashMap<>();
                            while (scan.hasNext()) {
                                line = scan.nextLine();
                                if (line.startsWith("Store_")) {
                                    break;
                                } else {
                                    int marker = line.indexOf(':');
                                    if (marker >= 0) {
                                        String id = line.substring(0, marker).trim();
                                        String dataItem = line.substring(marker+1).trim();
                                        data.put(id, dataItem);
                                    } else {
                                        throw new Exception("Settings file corrupted. Line : " + line);
                                    }
                                }
                            }
                            settingsStores.get(storeId).decodeSettings(data);
                            if (specificId != null) {
                                break;
                            }
                        }
                    }
                }
                scan.close();
            } else {
                if (specificId == null) {
                    for (Map.Entry<String, AbstractSettings> entry : settingsStores.entrySet()) {
                        entry.getValue().initialiseNewUser();
                    }
                    saveSettingsFile();
                } else if (settingsStores.containsKey(specificId)) {
                    settingsStores.get(specificId).initialiseNewUser();
                }
            }
        } catch (IOException ex) {
            System.getLogger(SettingsManager.class.getName()).log(System.Logger.Level.ERROR, "Could not load settings file from : " + SETTINGS_FILE_PATH, ex);
        } catch (Exception e) {
            System.getLogger(SettingsManager.class.getName()).log(System.Logger.Level.ERROR, "Error reading settings file. Line : " + line, e);
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
                    sb.append("\t");
                    sb.append(dataItem.getKey() + ": " + dataItem.getValue() + "\n");
                }
                sb.append("\n");
            }
            Path filepath = Paths.get(SETTINGS_FILE_PATH);                
            Files.write(filepath, sb.toString().getBytes());
            Files.setAttribute(filepath, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        } catch (Exception ex) {
            System.getLogger(SettingsManager.class.getName()).log(System.Logger.Level.ERROR, "Could not save settings file to : " + SETTINGS_FILE_PATH, ex);
        }
    }
}
