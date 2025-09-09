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
    public static final Color TERRAIN_BG_ADDITIONAL = Color.WHITE;
    
    private static final ImageIcon[] terrainIcons = new ImageIcon[17];
    private static final Color[] terrainTextColors = new Color[] {
        //Base
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
        //Additional
        new Color(0xBB0000),    //Additional 1
        new Color(0x008800),    //Additional 2
        new Color(0x0000BB),    //Additional 3
        new Color(0xBBBB00),    //Additional 4
        new Color(0x00BBBB),    //Additional 5
        new Color(0xBB00BB),    //Additional 6
        new Color(0x000000),    //Additional 7
    };
    
    public static Color getBGColor(int terrainType) {
        return terrainType < 9 ? TERRAIN_BG : TERRAIN_BG_ADDITIONAL;
    }
    
    public static ImageIcon getTerrainIcon(int terrainType) {
        if (terrainIcons[0] == null) {
            ClassLoader loader = BattleTerrainIcons.class.getClassLoader();
            //Base
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
            //Additional
            terrainIcons[10] = new ImageIcon(loader.getResource("terrain/icons/09_Add_1.png"));
            terrainIcons[11] = new ImageIcon(loader.getResource("terrain/icons/10_Add_2.png"));
            terrainIcons[12] = new ImageIcon(loader.getResource("terrain/icons/11_Add_3.png"));
            terrainIcons[13] = new ImageIcon(loader.getResource("terrain/icons/12_Add_4.png"));
            terrainIcons[14] = new ImageIcon(loader.getResource("terrain/icons/13_Add_5.png"));
            terrainIcons[15] = new ImageIcon(loader.getResource("terrain/icons/14_Add_6.png"));
            terrainIcons[16] = new ImageIcon(loader.getResource("terrain/icons/15_Add_7.png"));
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
