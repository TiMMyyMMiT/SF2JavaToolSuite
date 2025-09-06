/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.gui.resources;

import java.awt.Color;
import javax.swing.ImageIcon;

/**
 *
 * @author TiMMy
 */
public class BattleTerrainIcons {
    public static final Color TERRAIN_DARKEN = new Color(0, 0, 0, 50);
    public static final Color TERRAIN_BG = Color.BLACK;
    public static final Color TERRAIN_TEXT_BG = Color.BLACK;
    
    private static final ImageIcon[] terrainIcons = new ImageIcon[10];
    private static final Color[] terrainTextColors = new Color[] {
        new Color(0xFF3333),    //Obstructed
        Color.DARK_GRAY,        //Wall
        Color.WHITE,            //Plains
        new Color(0xFFBB66),    //Path
        new Color(0x008800),    //Grass
        new Color(0x22DD22),    //Forest
        new Color(0xFF7744),    //Hills
        new Color(0xFFFF66),    //Desert
        Color.GRAY,             //Mountain
        new Color(0x6666FF),    //Water
    };
    
    public static ImageIcon getTerrainIcon(int terrainType) {
        if (terrainIcons[0] == null) {
            ClassLoader loader = BattleTerrainIcons.class.getClassLoader();
            terrainIcons[0] = new ImageIcon(loader.getResource("terrain/icons/XX_Obstructed.png"));
            terrainIcons[1] = new ImageIcon(loader.getResource("terrain/icons/00_Wall.png"));
            terrainIcons[2] = new ImageIcon(loader.getResource("terrain/icons/01_Plains.png"));
            terrainIcons[3] = new ImageIcon(loader.getResource("terrain/icons/02_Path.png"));
            terrainIcons[4] = new ImageIcon(loader.getResource("terrain/icons/03_Grass.png"));
            terrainIcons[5] = new ImageIcon(loader.getResource("terrain/icons/04_Forest.png"));
            terrainIcons[6] = new ImageIcon(loader.getResource("terrain/icons/05_Hills.png"));
            terrainIcons[7] = new ImageIcon(loader.getResource("terrain/icons/06_Desert.png"));
            terrainIcons[8] = new ImageIcon(loader.getResource("terrain/icons/07_Mountain.png"));
            terrainIcons[9] = new ImageIcon(loader.getResource("terrain/icons/08_Water.png"));
        }
        terrainType++;
        if (terrainType < 0 || terrainType >= terrainTextColors.length) {
            return terrainIcons[0];
        } else {
            return terrainIcons[terrainType];
        }
    }
    
    public static Color getTerrainTextColor(int terrainType) {
        terrainType++;
        if (terrainType < 0 || terrainType >= terrainTextColors.length) {
            return Color.WHITE;
        } else {
            return terrainTextColors[terrainType];
        }
    }
}
