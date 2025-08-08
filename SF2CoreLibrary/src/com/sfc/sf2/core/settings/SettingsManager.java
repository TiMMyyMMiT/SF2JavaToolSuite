/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.settings;

import com.sfc.sf2.core.Manifest;
import com.sfc.sf2.core.gui.controls.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;

/**
 *
 * @author TiMMy
 */
public class SettingsManager {    
    private static final HashMap<String, AbstractSettings> settingsStores = new HashMap<>();
    private static Path settingsFilePath = null;
    
    private static GlobalSettings globalSettings;
    private static Path globalSettingsPath = null;
    
    private static boolean isRunningInEditor = true;

    static {
        globalSettings = new GlobalSettings();
        CoreSettings core = new CoreSettings();
        settingsStores.put("core", core);
    }
    
    public static boolean isRunningInEditor() {
        return isRunningInEditor;
    }
    
    public static void setRunningInEditor(boolean inEditor) {
        isRunningInEditor = inEditor;
    }
    
    private static Path getGlobalSettingsFilePath() {
        if (globalSettingsPath == null) {
            globalSettingsPath = Path.of(System.getenv("APPDATA")).resolve("SF2").resolve("global.settings");
        }
        return globalSettingsPath;
    }
    
    private static Path getSettingsFilePath() {
        if (settingsFilePath == null) {
            settingsFilePath = Path.of(System.getenv("APPDATA")).resolve("SF2");
            String projectName = Manifest.getProjectName();
            settingsFilePath = settingsFilePath.resolve(projectName + ".settings");
        }
        return settingsFilePath;
    }
    
    public static void registerSettingsStore(String id, AbstractSettings settings) {
        if (settings.getClass().toString().equals("GlobalSettings")) {
            Console.logger().severe("Error: Cannot add another instance of \"Core\" settings.");
            return;
        } else if (settings.getClass().toString().equals("CoreSettings")) {
            Console.logger().severe("Error: Cannot add another instance of \"Core\" settings.");
            return;
        } else if (settingsStores.containsKey(id)) {
            Console.logger().severe("Error: Cannot add duplicate instance of " + id + " settings.");
            return;
        }
        settingsStores.put(id, settings);
    }
    
    public static GlobalSettings getGlobalSettings() {
        return globalSettings;
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends AbstractSettings> T getSettingsStore(String id) {
        if (settingsStores.containsKey(id)) {
            return (T)settingsStores.get(id);
        }
        return null;
    }
    
    public static void loadGlobalSettings() {
        Console.logger().finest("ENTERING loadGlobalSettings");
        String line = null;
        try {
            File file = getGlobalSettingsFilePath().toFile();
            if (file.exists()) {
                Scanner scan = new Scanner(file);
                readStoreData(scan, globalSettings);
                scan.close();
            } else {
                Console.logger().info("Initialising new global settings...");
                globalSettings.initialiseNewUser();
                saveGlobalSettingsFile();
            }
        } catch (IOException ex) {
            Console.logger().log(Level.SEVERE, "Could not load settings file from : " + getSettingsFilePath(), ex);
        } catch (Exception e) {
            Console.logger().log(Level.SEVERE, "Error reading settings file. Line : " + line, e);
        }
        Console.logger().finest("EXITING loadGlobalSettings");
    }
    
    public static void loadSettingsFile() {
        loadSettings(null);
    }
    
    public static void loadSpecificSettings(String id) {
        loadSettings(id);
    }
    
    private static void loadSettings(String specificId) {
        Console.logger().finest("ENTERING loadSettings");
        String line = null;
        try {
            File file = getSettingsFilePath().toFile();
            if (file.exists()) {
                Scanner scan = new Scanner(file);
                line = scan.nextLine();
                String storeId = null;
                HashMap<String, String> data = null;
                while (scan.hasNext()) {
                    if (line.startsWith("Store_")) {
                        storeId = line.substring(line.indexOf("_")+1).trim();
                        if (settingsStores.containsKey(storeId) && (specificId == null || storeId.equals(specificId))) {
                            readStoreData(scan, settingsStores.get(storeId));
                        }
                        if (specificId != null) {
                            break;
                        }
                    }
                }
                scan.close();
            } else {
                Console.logger().info("Initialising new user settings...");
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
            Console.logger().log(Level.SEVERE, "Could not load settings file from : " + getSettingsFilePath(), ex);
        } catch (Exception e) {
            Console.logger().log(Level.SEVERE, "Error reading settings file. Line : " + line, e);
        }
        Console.logger().finest("EXITING loadSettings");
    }
    
    private static void readStoreData(Scanner scan, AbstractSettings store) throws Exception {
        HashMap<String, String> data = new HashMap<>();
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.startsWith("Store_")) {
                break;
            } else {
                int marker = line.indexOf(':');
                if (marker >= 0) {
                    String id = line.substring(0, marker).trim();
                    String dataItem = line.substring(marker+1).trim();
                    data.put(id, dataItem);
                    Console.logger().finest("Settings : " + line);
                } else {
                    throw new Exception("Settings file corrupted. Line : " + line);
                }
            }
        }
        store.decodeSettings(data);
    }
    
    public static void saveGlobalSettingsFile() {
        Console.logger().finest("ENTERING saveGlobalSettingsFile");
        try {
            StringBuilder sb = new StringBuilder();
            writeStoreData(sb, globalSettings);
            Path filepath = getGlobalSettingsFilePath();
            checkSettingsFolderExists(filepath);
            Files.write(filepath, sb.toString().getBytes());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, "Could not save settings file to : " + getSettingsFilePath(), ex);
        }
        Console.logger().finest("EXITING saveGlobalSettingsFile");
    }
    
    public static void saveSettingsFile() {
        Console.logger().finest("ENTERING saveSettingsFile");
        try {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, AbstractSettings> entry : settingsStores.entrySet()) {
                sb.append("Store_" + entry.getKey() + "\n");
                writeStoreData(sb, entry.getValue());
            }
            Path filepath = getSettingsFilePath();
            checkSettingsFolderExists(filepath);
            Files.write(filepath, sb.toString().getBytes());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, "Could not save settings file to : " + getSettingsFilePath(), ex);
        }
        Console.logger().finest("EXITING saveSettingsFile");
    }
    
    private static void writeStoreData(StringBuilder sb, AbstractSettings store) {
        HashMap<String, String> data = new HashMap<>();
        store.encodeSettings(data);
        for (Map.Entry<String, String> dataItem : data.entrySet()) {
            String line = dataItem.getKey() + ": " + dataItem.getValue();
            sb.append("\t");
            sb.append(line);
            sb.append("\n");
            Console.logger().finest("Settings : " + line);
        }
        sb.append("\n");
    }
    
    private static void checkSettingsFolderExists(Path settingsPath) throws IOException {
        Path folder = settingsPath.getParent();
        if (!Files.exists(folder))
            Files.createDirectory(folder);
    }
}
