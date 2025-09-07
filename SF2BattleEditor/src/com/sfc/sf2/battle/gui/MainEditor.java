/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.gui;

import com.sfc.sf2.battle.BattleManager;
import com.sfc.sf2.battle.BattleSpriteset;
import com.sfc.sf2.battle.Enemy;
import com.sfc.sf2.battle.EnemyEnums;
import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import com.sfc.sf2.core.gui.AbstractMainEditor;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.helpers.PathHelpers;
import java.nio.file.Path;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author wiz
 */
public class MainEditor extends AbstractMainEditor {
    
    BattleManager battleManager = new BattleManager();
    
    public MainEditor() {
        super();
        //SettingsManager.registerSettingsStore("terrain", terrainSettings);
        initComponents();
        initCore(console1);
    }
    
    @Override
    protected void initEditor() {
        super.initEditor();
        
        accordionPanel1.setExpanded(false);
        accordionPanel2.setExpanded(false);
        
        tableAllies.addTableModelListener(this::onTableAlliesDataChanged);
        tableAllies.addListSelectionListener(this::onTableAlliesSelectionChanged);
        tableEnemies.addTableModelListener(this::onTableEnemiesDataChanged);
        tableEnemies.addListSelectionListener(this::onTableEnemiesSelectionChanged);
        tableAIRegions.addTableModelListener(this::onTableAIRegionsDataChanged);
        tableAIRegions.addListSelectionListener(this::onTableAIRegionsSelectionChanged);
        tableAIPoints.addTableModelListener(this::onTableAIPointsDataChanged);
        tableAIPoints.addListSelectionListener(this::onTableAIPointsSelectionChanged);
        TableColumnModel columns = tableAllies.jTable.getColumnModel();
        columns.getColumn(0).setMaxWidth(30);
        columns = tableEnemies.jTable.getColumnModel();
        columns.getColumn(0).setMaxWidth(30);
        columns = tableAIRegions.jTable.getColumnModel();
        columns.getColumn(0).setMaxWidth(30);
        columns = tableAIPoints.jTable.getColumnModel();
        columns.getColumn(0).setMaxWidth(30);
        
        /*jComboBox2.setSelectedIndex(terrainSettings.getTerrainDrawMode());
        colorPicker1.setColor(SettingsManager.getGlobalSettings().getTransparentBGColor());
        
        battleMapTerrainLayoutPanel.setDisplayScale(jComboBox1.getSelectedIndex()+1);
        battleMapTerrainLayoutPanel.setShowGrid(jCheckBox2.isSelected());
        battleMapTerrainLayoutPanel.setBGColor(colorPicker1.getColor());
        battleMapTerrainLayoutPanel.setShowExplorationFlags(jCheckBox1.isSelected());
        battleMapTerrainLayoutPanel.setDrawTerrain(jCheckBox3.isSelected());
        battleMapTerrainLayoutPanel.setDrawTerrainText(jComboBox2.getSelectedIndex() != 0);
        battleMapTerrainLayoutPanel.setShowBattleCoords(true);*/
    }
    
    @Override
    protected void onDataLoaded() {
        super.onDataLoaded();
        
        battleLayoutPanel.setBattle(battleManager.getBattle());
        battleLayoutPanel.setTerrain(battleManager.getBattle().getTerrain());
        battleLayoutPanel.setBattleCoords(battleManager.getBattleCoords());
        battleLayoutPanel.setMapLayout(battleManager.getMapLayout());
        
        battleLayoutPanel.setDisplayScale(jComboBox1.getSelectedIndex()+1);
        battleLayoutPanel.setShowGrid(jCheckBox2.isSelected());
        battleLayoutPanel.setBGColor(colorPicker1.getColor());
        battleLayoutPanel.setDrawTerrain(jCheckBox3.isSelected());
        battleLayoutPanel.setShowExplorationFlags(jCheckBox1.isSelected());
        battleLayoutPanel.setDrawTerrain(jCheckBox3.isSelected());
        battleLayoutPanel.setDrawSprites(jCheckBox4.isSelected());        
        
        BattleMapCoords coords = battleManager.getBattleCoords();
        jSpinner1.setValue(coords.getMap());
        jSpinner2.setValue(coords.getX());
        jSpinner3.setValue(coords.getY());
        jSpinner5.setValue(coords.getWidth());
        jSpinner6.setValue(coords.getHeight());
        jSpinner7.setValue(coords.getTrigX());
        jSpinner8.setValue(coords.getTrigY());
        
        BattleSpriteset spriteset = battleManager.getBattle().getSpriteset();
        if (spriteset == null) {
            allyPropertiesTableModel.setTableData(null);
            enemyPropertiesTableModel.setTableData(null);
            aIRegionPropertiesTableModel.setTableData(null);
            aIPointPropertiesTableModel.setTableData(null);
        } else {
            allyPropertiesTableModel.setTableData(spriteset.getAllies());
            enemyPropertiesTableModel.setTableData(spriteset.getEnemies());
            aIRegionPropertiesTableModel.setTableData(spriteset.getAiRegions());
            aIPointPropertiesTableModel.setTableData(spriteset.getAiPoints());
        }
        
        EnemyEnums enemyEnums = battleManager.getEnemyEnums();
        jComboBox_Name.setModel(new DefaultComboBoxModel<>(enemyEnums.getEnemies().keySet().toArray(new String[enemyEnums.getEnemies().size()])));
        jComboBox_AI.setModel(new DefaultComboBoxModel<>(enemyEnums.getCommandSets().keySet().toArray(new String[enemyEnums.getCommandSets().size()])));
        jComboBox_Spawn.setModel(new DefaultComboBoxModel<>(enemyEnums.getSpawnParams().keySet().toArray(new String[enemyEnums.getSpawnParams().size()])));
        jComboBox_Order1.setModel(new DefaultComboBoxModel<>(enemyEnums.getOrders().keySet().toArray(new String[enemyEnums.getOrders().size()])));
        jComboBox_Order2.setModel(new DefaultComboBoxModel<>(enemyEnums.getOrders().keySet().toArray(new String[enemyEnums.getOrders().size()])));
        jComboBox_Items.setModel(new DefaultComboBoxModel<>(enemyEnums.getItems().keySet().toArray(new String[enemyEnums.getItems().size()])));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        aIPointPropertiesTableModel = new com.sfc.sf2.battle.models.AIPointPropertiesTableModel();
        aIRegionPropertiesTableModel = new com.sfc.sf2.battle.models.AIRegionPropertiesTableModel();
        allyPropertiesTableModel = new com.sfc.sf2.battle.models.AllyPropertiesTableModel();
        enemyPropertiesTableModel = new com.sfc.sf2.battle.models.EnemyPropertiesTableModel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel15 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel8 = new javax.swing.JPanel();
        jSplitPane4 = new javax.swing.JSplitPane();
        jPanel9 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        accordionPanel1 = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButton1 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton3 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton4 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton5 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton9 = new com.sfc.sf2.core.gui.controls.FileButton();
        accordionPanel2 = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButton6 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton7 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton8 = new com.sfc.sf2.core.gui.controls.FileButton();
        jPanel16 = new javax.swing.JPanel();
        jSpinner4 = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        jButton18 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        fileButton10 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton11 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton12 = new com.sfc.sf2.core.gui.controls.FileButton();
        jPanel11 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jSpinner5 = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jSpinner6 = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jSpinner7 = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        jSpinner8 = new javax.swing.JSpinner();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jSpinner9 = new javax.swing.JSpinner();
        jLabel13 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel24 = new javax.swing.JPanel();
        tableAllies = new com.sfc.sf2.core.gui.controls.Table();
        jPanel25 = new javax.swing.JPanel();
        tableEnemies = new com.sfc.sf2.core.gui.controls.Table();
        jPanel17 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jSpinner_X = new javax.swing.JSpinner();
        jSpinner_Y = new javax.swing.JSpinner();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jComboBox_Name = new javax.swing.JComboBox<>();
        jComboBox_AI = new javax.swing.JComboBox<>();
        jComboBox_Spawn = new javax.swing.JComboBox<>();
        jLabel40 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jComboBox_Items = new javax.swing.JComboBox<>();
        jTextField_ItemFlags = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jComboBox_Order1 = new javax.swing.JComboBox<>();
        jLabel41 = new javax.swing.JLabel();
        jSpinner_OrderTarget1 = new javax.swing.JSpinner();
        jLabel37 = new javax.swing.JLabel();
        jSpinner_Trigger1 = new javax.swing.JSpinner();
        jPanel14 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jComboBox_Order2 = new javax.swing.JComboBox<>();
        jLabel42 = new javax.swing.JLabel();
        jSpinner_OrderTarget2 = new javax.swing.JSpinner();
        jSpinner_Trigger2 = new javax.swing.JSpinner();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jSpinner_Trigger3 = new javax.swing.JSpinner();
        jPanel26 = new javax.swing.JPanel();
        tableAIRegions = new com.sfc.sf2.core.gui.controls.Table();
        jPanel31 = new javax.swing.JPanel();
        tableAIPoints = new com.sfc.sf2.core.gui.controls.Table();
        jPanel10 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        battleLayoutPanel = new com.sfc.sf2.battle.gui.BattleLayoutPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        colorPicker1 = new com.sfc.sf2.core.gui.controls.ColorPicker();
        console1 = new com.sfc.sf2.core.gui.controls.Console();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SF2BattleEditor");

        jSplitPane1.setDividerLocation(720);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);

        jSplitPane2.setDividerLocation(750);
        jSplitPane2.setOneTouchExpandable(true);

        jSplitPane4.setDividerLocation(350);
        jSplitPane4.setOneTouchExpandable(true);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Import from :"));
        jPanel3.setPreferredSize(new java.awt.Dimension(590, 135));

        accordionPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Map & terrain"));

        fileButton1.setFilePath("..\\graphics\\maps\\mappalettes\\entries.asm");
        fileButton1.setLabelText("Palette entries :");

        fileButton2.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
        fileButton2.setLabelText("Tileset entries :");

        fileButton3.setFilePath("..\\maps\\entries.asm");
        fileButton3.setLabelText("Map entries :");

        fileButton4.setFilePath(".\\terrainentries.asm");
        fileButton4.setLabelText("Terrain entries :");

        fileButton5.setFilePath(".\\global\\battlemapcoords.asm");
        fileButton5.setLabelText("Battle map coords :");

        fileButton9.setFilePath(".\\spritesets\\entries.asm");
        fileButton9.setLabelText("Spriteset entries :");

        javax.swing.GroupLayout accordionPanel1Layout = new javax.swing.GroupLayout(accordionPanel1);
        accordionPanel1.setLayout(accordionPanel1Layout);
        accordionPanel1Layout.setHorizontalGroup(
            accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        accordionPanel1Layout.setVerticalGroup(
            accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        accordionPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Battle sprites :"));

        fileButton6.setFilePath("..\\graphics\\tech\\basepalette.bin");
        fileButton6.setLabelText("Base palette :");

        fileButton7.setFilePath("..\\graphics\\mapsprites\\entries.asm");
        fileButton7.setLabelText("Mapsprite entries :");

        fileButton8.setFilePath("..\\..\\sf2enums.asm");
        fileButton8.setLabelText("Mapsprite enums :");

        javax.swing.GroupLayout accordionPanel2Layout = new javax.swing.GroupLayout(accordionPanel2);
        accordionPanel2.setLayout(accordionPanel2Layout);
        accordionPanel2Layout.setHorizontalGroup(
            accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addComponent(fileButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        accordionPanel2Layout.setVerticalGroup(
            accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Battle :"));

        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(1, 0, 255, 1));

        jLabel10.setText("Battle index :");

        jButton18.setText("Import");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jLabel2.setText("<html>Select disassembly files and battle index.</html>");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addContainerGap(88, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton18))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton18)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accordionPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accordionPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(accordionPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accordionPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Export to :"));
        jPanel5.setPreferredSize(new java.awt.Dimension(32, 135));

        jLabel1.setText("<html>Select new target files.</html>");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jButton2.setText("Export");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        fileButton10.setFilePath(".\\global\\battlemapcoords.asm");
        fileButton10.setLabelText("Battle map coords :");

        fileButton11.setFilePath(".\\entries\\battle01\\terrain.bin");
        fileButton11.setLabelText("Battle terrain :");

        fileButton12.setFilePath(".\\spritesets\\spriteset01.asm");
        fileButton12.setLabelText("Battle spriteset :");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addComponent(fileButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jSplitPane4.setLeftComponent(jPanel9);

        jPanel11.setPreferredSize(new java.awt.Dimension(395, 600));

        jTabbedPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTabbedPane2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane2StateChanged(evt);
            }
        });
        jTabbedPane2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTabbedPane2FocusGained(evt);
            }
        });

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(0, 0, 256, 1));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        jLabel3.setText("Map :");

        jLabel5.setText("Start X :");

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner2StateChanged(evt);
            }
        });

        jLabel6.setText("Start Y :");

        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner3StateChanged(evt);
            }
        });

        jLabel7.setText("Width :");

        jSpinner5.setModel(new javax.swing.SpinnerNumberModel(0, 0, 48, 1));
        jSpinner5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner5StateChanged(evt);
            }
        });

        jLabel8.setText("Height :");

        jSpinner6.setModel(new javax.swing.SpinnerNumberModel(0, 0, 48, 1));
        jSpinner6.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner6StateChanged(evt);
            }
        });

        jLabel9.setText("Trigger X :");

        jSpinner7.setModel(new javax.swing.SpinnerNumberModel(0, -1, 63, 1));
        jSpinner7.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner7StateChanged(evt);
            }
        });

        jLabel11.setText("Trigger Y :");

        jSpinner8.setModel(new javax.swing.SpinnerNumberModel(0, -1, 63, 1));
        jSpinner8.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner8StateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner8, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(73, 73, 73))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jSpinner8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addContainerGap(549, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Map Coords", jPanel4);

        jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane6.setViewportView(jTable1);

        jLabel12.setText("<html>Table above to ignore at the moment.<br>Will display the Land Effect / Move Cost table :<br>disasm\\data\\battles\\global\\movetypeterraincosts.txt<br><br>Map controls when this tab is active :<br>Left click : decrement block's terrain type index.<br>Right click : increment<br>Middle-click drag'n'drop : apply value on zone.<br>(see value to apply in spinner below)<br/><br/>Quick value defs :<br/>-1 = Obstructed<br>0 = Low Sky<br/>1 = Plains<br/>2 = Path<br/>3 = Grass<br/>4 = Forest<br/>5 = Hills<br/>6 = Desert<br/>7 = High Sky<br/>8 = Water</html>");

        jSpinner9.setModel(new javax.swing.SpinnerNumberModel(-1, -1, 8, 1));
        jSpinner9.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner9StateChanged(evt);
            }
        });

        jLabel13.setText("<html>Applied value on selected zone :<br/>(Middle-click drag'n'drop)</html>");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jLabel12)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinner9, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(81, 81, 81))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Terrain", jPanel21);

        jTabbedPane3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane3StateChanged(evt);
            }
        });

        tableAllies.setBorder(null);
        tableAllies.setModel(allyPropertiesTableModel);
        tableAllies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableAllies.setSingleClickText(true);
        tableAllies.setSpinnerNumberEditor(true);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableAllies, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableAllies, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Allies", jPanel24);

        tableEnemies.setBorder(null);
        tableEnemies.setModel(enemyPropertiesTableModel);
        tableEnemies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableEnemies.setSingleClickText(true);
        tableEnemies.setSpinnerNumberEditor(true);

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected Enemy :"));

        jLabel16.setText("Name :");

        jLabel17.setText("X :");

        jSpinner_X.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner_X.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner_XStateChanged(evt);
            }
        });

        jSpinner_Y.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner_Y.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner_YStateChanged(evt);
            }
        });

        jLabel18.setText("Y :");

        jLabel19.setText("AI :");

        jComboBox_Name.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Name.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_NameItemStateChanged(evt);
            }
        });

        jComboBox_AI.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_AI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_AIItemStateChanged(evt);
            }
        });

        jComboBox_Spawn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Spawn.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_SpawnItemStateChanged(evt);
            }
        });

        jLabel40.setText("Spawn :");

        jLabel34.setText("Item :");

        jComboBox_Items.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Items.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_ItemsItemStateChanged(evt);
            }
        });

        jTextField_ItemFlags.setText("flags");
        jTextField_ItemFlags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_ItemFlagsActionPerformed(evt);
            }
        });

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel36.setText("Move Order 1 :");

        jComboBox_Order1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Order1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_Order1ItemStateChanged(evt);
            }
        });

        jLabel41.setText("Target 1 :");

        jSpinner_OrderTarget1.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner_OrderTarget1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner_OrderTarget1StateChanged(evt);
            }
        });

        jLabel37.setText("Trigger Region 1 :");

        jSpinner_Trigger1.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner_Trigger1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner_Trigger1StateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner_Trigger1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_Order1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner_OrderTarget1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jSpinner_OrderTarget1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41)
                    .addComponent(jComboBox_Order1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(jSpinner_Trigger1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel35.setText("Move Order 2 :");

        jComboBox_Order2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Order2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_Order2ItemStateChanged(evt);
            }
        });

        jLabel42.setText("Target 2 :");

        jSpinner_OrderTarget2.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner_OrderTarget2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner_OrderTarget2StateChanged(evt);
            }
        });

        jSpinner_Trigger2.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner_Trigger2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner_Trigger2StateChanged(evt);
            }
        });

        jLabel38.setText("Trigger Region 2 :");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner_Trigger2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_Order2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner_OrderTarget2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(jSpinner_OrderTarget2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35)
                    .addComponent(jComboBox_Order2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(jSpinner_Trigger2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel39.setText("Byte10 :");

        jSpinner_Trigger3.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner_Trigger3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner_Trigger3StateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner_X, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner_Y, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel34))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBox_Items, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox_AI, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(4, 4, 4)
                                        .addComponent(jComboBox_Spawn, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addComponent(jTextField_ItemFlags, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner_Trigger3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jComboBox_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner_X, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jSpinner_Y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jComboBox_AI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40)
                    .addComponent(jComboBox_Spawn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jComboBox_Items, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_ItemFlags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(jSpinner_Trigger3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableEnemies, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableEnemies, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane3.addTab("Enemies", jPanel25);

        tableAIRegions.setBorder(null);
        tableAIRegions.setModel(aIRegionPropertiesTableModel);
        tableAIRegions.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableAIRegions.setSingleClickText(true);
        tableAIRegions.setSpinnerNumberEditor(true);

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableAIRegions, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableAIRegions, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("AI Regions", jPanel26);

        tableAIPoints.setBorder(null);
        tableAIPoints.setModel(aIPointPropertiesTableModel);
        tableAIPoints.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableAIPoints.setSingleClickText(true);
        tableAIPoints.setSpinnerNumberEditor(true);

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableAIPoints, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableAIPoints, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("AI Points", jPanel31);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
        );

        jTabbedPane2.addTab("Spriteset", jPanel22);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        jSplitPane4.setRightComponent(jPanel11);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane4)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane4)
        );

        jSplitPane2.setLeftComponent(jPanel8);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Map"));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        javax.swing.GroupLayout battleLayoutPanelLayout = new javax.swing.GroupLayout(battleLayoutPanel);
        battleLayoutPanel.setLayout(battleLayoutPanelLayout);
        battleLayoutPanelLayout.setHorizontalGroup(
            battleLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1536, Short.MAX_VALUE)
        );
        battleLayoutPanelLayout.setVerticalGroup(
            battleLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1536, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(battleLayoutPanel);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Display"));

        jLabel4.setText("Scale :");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Exploration");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("Show grid");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox3.setText("Terrain");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jCheckBox4.setSelected(true);
        jCheckBox4.setText("Sprites");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jLabel14.setText("BG :");

        javax.swing.GroupLayout colorPicker1Layout = new javax.swing.GroupLayout(colorPicker1);
        colorPicker1.setLayout(colorPicker1Layout);
        colorPicker1Layout.setHorizontalGroup(
            colorPicker1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        colorPicker1Layout.setVerticalGroup(
            colorPicker1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(colorPicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jCheckBox2)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(colorPicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBox1)
                        .addComponent(jCheckBox3)
                        .addComponent(jCheckBox4)))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jSplitPane2.setRightComponent(jPanel10);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1292, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2)
        );

        jSplitPane1.setTopComponent(jPanel15);
        jSplitPane1.setBottomComponent(console1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1308, 865));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Path coordsPath = PathHelpers.getBasePath().resolve(fileButton10.getFilePath());                           
        Path terrainPath = PathHelpers.getBasePath().resolve(fileButton11.getFilePath());                           
        Path spritesetPath = PathHelpers.getBasePath().resolve(fileButton12.getFilePath());
        if (!PathHelpers.createPathIfRequred(terrainPath)) return;
        try {
            battleManager.exportDisassembly(coordsPath, terrainPath, spritesetPath, battleLayoutPanel.getBattle());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Battle disasm could not be exported to : " + terrainPath);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        if(jComboBox1.getSelectedIndex()>=0){
            battleLayoutPanel.setDisplayScale(jComboBox1.getSelectedIndex()+1);
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        battleLayoutPanel.setShowExplorationFlags(jCheckBox1.isSelected());
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        battleLayoutPanel.setShowGrid(jCheckBox2.isSelected());
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        battleLayoutPanel.setDrawTerrain(jCheckBox3.isSelected());
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        Path paletteEntriesPath = PathHelpers.getBasePath().resolve(fileButton1.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButton2.getFilePath());
        Path mapEntriesPath = PathHelpers.getBasePath().resolve(fileButton3.getFilePath());
        Path terrainEntriesPath = PathHelpers.getBasePath().resolve(fileButton4.getFilePath());
        Path battleMapCoordsPath = PathHelpers.getBasePath().resolve(fileButton5.getFilePath());
        Path spritesetEntriesPath = PathHelpers.getBasePath().resolve(fileButton9.getFilePath());
        int battleIndex = (int)jSpinner4.getModel().getValue();
        
        Path basePalettePath = PathHelpers.getBasePath().resolve(fileButton6.getFilePath());
        Path mapspriteEntriesPath = PathHelpers.getBasePath().resolve(fileButton7.getFilePath());
        Path mapspriteEnumsPath = PathHelpers.getBasePath().resolve(fileButton8.getFilePath());
        
        try {
            battleManager.importMapspriteData(basePalettePath, mapspriteEntriesPath, mapspriteEnumsPath);
        } catch (Exception ex) {
            battleManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Mapsprite data could not be imported from : " + mapspriteEntriesPath + " and " + mapspriteEnumsPath);
            return;
        }
        try {
            battleManager.importDisassembly(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, terrainEntriesPath, battleMapCoordsPath, spritesetEntriesPath, battleIndex);
        } catch (Exception ex) {
            battleManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Battle data could not be imported for battle : " + battleIndex);
            return;
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void UpdateEnemyControls(int selectedRow) {
        boolean enabled = selectedRow >= 0;
        jComboBox_Name.setEnabled(enabled);
        jSpinner_X.setEnabled(enabled);
        jSpinner_Y.setEnabled(enabled);
        jComboBox_AI.setEnabled(enabled);
        jComboBox_Spawn.setEnabled(enabled);
        jSpinner_Trigger1.setEnabled(enabled);
        jSpinner_Trigger2.setEnabled(enabled);
        jComboBox_Order1.setEnabled(enabled);
        jComboBox_Order2.setEnabled(enabled);
        jSpinner_OrderTarget1.setEnabled(enabled);
        jSpinner_OrderTarget2.setEnabled(enabled);
        jComboBox_Items.setEnabled(enabled);
        jTextField_ItemFlags.setEnabled(enabled);
              
        if (selectedRow != -1){
            Enemy enemy = (selectedRow == -1) ? null : battleManager.getBattle().getSpriteset().getEnemies()[selectedRow];
            if (enemy != null) {
                jComboBox_Name.setSelectedItem(enemy.getEnemyData().getName());
                jSpinner_X.setValue(enemy.getX());
                jSpinner_Y.setValue(enemy.getY());
                jComboBox_AI.setSelectedItem(enemy.getAi());
                jComboBox_Spawn.setSelectedItem(enemy.getSpawnParams());
                jSpinner_Trigger1.setValue(enemy.getTriggerRegion1());
                jSpinner_Trigger2.setValue(enemy.getTriggerRegion2());
                
                String[] order = enemy.getMoveOrder1().split("\\|");
                jComboBox_Order1.setSelectedItem(order[0]);
                jSpinner_OrderTarget1.setValue(order.length > 1 ? Integer.parseInt(order[1]) : 0);
                order = enemy.getMoveOrder2().split("\\|");
                jComboBox_Order2.setSelectedItem(order[0]);
                jSpinner_OrderTarget2.setValue(order.length > 1 ? Integer.parseInt(order[1]) : 0);
                
                String item = enemy.getItem();
                String flags = "";
                if (item.contains("|")) {
                    flags = item.substring(item.indexOf("|")+1);
                    item = item.substring(0, item.indexOf("|"));
                }
                jComboBox_Items.setSelectedItem(item);
                jTextField_ItemFlags.setText(flags);
            }
        }
    }
    
    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        if(battleLayoutPanel!=null){
            battleLayoutPanel.setDrawSprites(jCheckBox4.isSelected());
        }
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        /*int newMapIndex = (int)jSpinner1.getModel().getValue();
        battleManager.getBattleCoords().setMap(newMapIndex);
        String[][] mapEntries = battleManager.getMapEntries();
        final MapLayoutManager mapLayoutManager = new MapLayoutManager();
        try {     
            mapLayoutManager.importDisassembly(PATH_TEST_PREFIX+jTextField21.getText(), PATH_TEST_PREFIX+jTextField22.getText(), mapEntries[newMapIndex][0], mapEntries[newMapIndex][1], mapEntries[newMapIndex][2]);
        } catch (DisassemblyException ex) {
            Logger.getLogger(MainEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        battleLayoutPanel.setMapLayout(mapLayoutManager.getLayout());
        battleLayoutPanel.setBlockset(mapLayoutManager.getBlockset());
        battleLayoutPanel.updateBattleDisplay();*/
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jSpinner2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner2StateChanged
        /*battleManager.getBattleCoords().setX((int)jSpinner2.getModel().getValue());
        battleLayoutPanel.updateCoordsDisplay();*/  
    }//GEN-LAST:event_jSpinner2StateChanged

    private void jSpinner3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner3StateChanged
        battleManager.getBattleCoords().setY((int)jSpinner3.getModel().getValue());
    }//GEN-LAST:event_jSpinner3StateChanged

    private void jSpinner5StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner5StateChanged
        battleManager.getBattleCoords().setWidth((int)jSpinner5.getModel().getValue());
    }//GEN-LAST:event_jSpinner5StateChanged

    private void jSpinner6StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner6StateChanged
        battleManager.getBattleCoords().setHeight((int)jSpinner6.getModel().getValue());
    }//GEN-LAST:event_jSpinner6StateChanged

    private void jSpinner7StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner7StateChanged
        battleManager.getBattleCoords().setTrigX((int)jSpinner7.getModel().getValue());
    }//GEN-LAST:event_jSpinner7StateChanged

    private void jSpinner8StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner8StateChanged
        battleManager.getBattleCoords().setTrigY((int)jSpinner8.getModel().getValue());
    }//GEN-LAST:event_jSpinner8StateChanged

    private void jTabbedPane2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabbedPane2FocusGained
        
    }//GEN-LAST:event_jTabbedPane2FocusGained

    private void jTabbedPane2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane2StateChanged
        int index = jTabbedPane2.getSelectedIndex();
        if(battleLayoutPanel!=null){
            switch(index){
                case 0:
                    battleLayoutPanel.setCurrentMode(BattleLayoutPanel.MODE_NONE);
                    break;
                case 1:
                    battleLayoutPanel.setCurrentMode(BattleLayoutPanel.MODE_TERRAIN);
                    break;
                case 2:
                    battleLayoutPanel.setCurrentMode(BattleLayoutPanel.MODE_SPRITE);
                    break;
            }
        }
    }//GEN-LAST:event_jTabbedPane2StateChanged

    private void jTabbedPane3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane3StateChanged
        int index = jTabbedPane3.getSelectedIndex();
        if(battleLayoutPanel!=null){
            switch(index){
                case 0:
                    battleLayoutPanel.setCurrentSpritesetMode(BattleLayoutPanel.SPRITESETMODE_ALLY);
                    break;
                case 1:
                    battleLayoutPanel.setCurrentSpritesetMode(BattleLayoutPanel.SPRITESETMODE_ENEMY);
                    break;
                case 2:
                    battleLayoutPanel.setCurrentSpritesetMode(BattleLayoutPanel.SPRITESETMODE_AIREGION);
                    break;
                case 3:
                    battleLayoutPanel.setCurrentSpritesetMode(BattleLayoutPanel.SPRITESETMODE_AIPOINT);
                    break;
            }
        }
    }//GEN-LAST:event_jTabbedPane3StateChanged

    private void jSpinner_Trigger1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner_Trigger1StateChanged
        OnEnemyDataChanged((int)jSpinner_Trigger1.getModel().getValue(), 6);
    }//GEN-LAST:event_jSpinner_Trigger1StateChanged

    private void jSpinner_YStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner_YStateChanged
        OnEnemyDataChanged((int)jSpinner_Y.getModel().getValue(), 2);
    }//GEN-LAST:event_jSpinner_YStateChanged

    private void jSpinner_Trigger2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner_Trigger2StateChanged
        OnEnemyDataChanged((int)jSpinner_Trigger2.getModel().getValue(), 8);
    }//GEN-LAST:event_jSpinner_Trigger2StateChanged

    private void jSpinner9StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner9StateChanged
        int newApplicableTerrainValue = (int)jSpinner9.getModel().getValue();
        battleLayoutPanel.setApplicableTerrainValue(newApplicableTerrainValue);
    }//GEN-LAST:event_jSpinner9StateChanged

    private void jSpinner_OrderTarget1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner_OrderTarget1StateChanged
        OnEnemyOrderChanged((String)jComboBox_Order1.getSelectedItem(), (int)jSpinner_OrderTarget1.getModel().getValue(), true);
    }//GEN-LAST:event_jSpinner_OrderTarget1StateChanged

    private void jSpinner_OrderTarget2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner_OrderTarget2StateChanged
        OnEnemyOrderChanged((String)jComboBox_Order2.getSelectedItem(), (int)jSpinner_OrderTarget2.getModel().getValue(), false);
    }//GEN-LAST:event_jSpinner_OrderTarget2StateChanged

    private void jTextField_ItemFlagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_ItemFlagsActionPerformed
        OnEnemyItemChanged((String)jComboBox_Items.getSelectedItem(), (String)jTextField_ItemFlags.getText());
    }//GEN-LAST:event_jTextField_ItemFlagsActionPerformed

    private void jComboBox_NameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_NameItemStateChanged
        OnEnemyDataChanged((String)evt.getItem(), 0);
    }//GEN-LAST:event_jComboBox_NameItemStateChanged

    private void jSpinner_XStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner_XStateChanged
        OnEnemyDataChanged((int)jSpinner_X.getModel().getValue(), 1);
    }//GEN-LAST:event_jSpinner_XStateChanged

    private void jComboBox_AIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_AIItemStateChanged
        OnEnemyDataChanged((String)evt.getItem(), 3);
    }//GEN-LAST:event_jComboBox_AIItemStateChanged

    private void jComboBox_SpawnItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_SpawnItemStateChanged
        OnEnemyDataChanged((String)evt.getItem(), 10);
    }//GEN-LAST:event_jComboBox_SpawnItemStateChanged

    private void jComboBox_ItemsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_ItemsItemStateChanged
        OnEnemyItemChanged((String)evt.getItem(), jTextField_ItemFlags.getSelectedText());
    }//GEN-LAST:event_jComboBox_ItemsItemStateChanged

    private void jComboBox_Order1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_Order1ItemStateChanged
        OnEnemyOrderChanged((String)evt.getItem(), (int)jSpinner_OrderTarget1.getModel().getValue(), true);
    }//GEN-LAST:event_jComboBox_Order1ItemStateChanged

    private void jComboBox_Order2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_Order2ItemStateChanged
        OnEnemyOrderChanged((String)evt.getItem(), (int)jSpinner_OrderTarget2.getModel().getValue(), false);
    }//GEN-LAST:event_jComboBox_Order2ItemStateChanged

    private void jSpinner_Trigger3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner_Trigger3StateChanged
        OnEnemyDataChanged((int)jSpinner_Trigger3.getModel().getValue(), 9);
    }//GEN-LAST:event_jSpinner_Trigger3StateChanged

    private void OnEnemyDataChanged(Object data, int column){
        /*int selectRow = jTable3.getSelectedRow();
        if (selectRow == -1)
            return;
        
        jTable3.setValueAt(data, selectRow, column);*/
    }

    private void OnEnemyItemChanged(String item, String flags){
        if (flags == null || flags.length() == 0)
            OnEnemyDataChanged(item, 4);
        else
            OnEnemyDataChanged(item+"|"+flags, 4);
    }

    private void OnEnemyOrderChanged(String order, int target, boolean order1){
        OnEnemyDataChanged(order+"|"+target, order1 ? 5 : 7);
    }
    
    private void onTableAlliesDataChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        if (row == battleLayoutPanel.getSelectedAlly()) {
            battleLayoutPanel.redraw();
        }
    }

    private void onTableAlliesSelectionChanged(ListSelectionEvent e) {
        int row = e.getFirstIndex();
        if (row != battleLayoutPanel.getSelectedAlly()) {
            battleLayoutPanel.setSelectedAlly(row);
            battleLayoutPanel.redraw();
        }
        if (battleLayoutPanel.currentMode != BattleLayoutPanel.MODE_SPRITE || battleLayoutPanel.currentSpritesetMode != BattleLayoutPanel.SPRITESETMODE_ALLY) {
            battleLayoutPanel.setCurrentMode(BattleLayoutPanel.MODE_SPRITE);
            battleLayoutPanel.setCurrentSpritesetMode(BattleLayoutPanel.SPRITESETMODE_ALLY);
            battleLayoutPanel.redraw();
        }
    }

    private void onTableEnemiesDataChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        if (row == battleLayoutPanel.getSelectedEnemy()) {
            battleLayoutPanel.redraw();
        }
    }

    private void onTableEnemiesSelectionChanged(ListSelectionEvent e) {
        int row = e.getFirstIndex();
        if (row != battleLayoutPanel.getSelectedEnemy()) {
            battleLayoutPanel.setSelectedEnemy(row);
            UpdateEnemyControls(row);
            battleLayoutPanel.redraw();
        }
        if (battleLayoutPanel.currentMode != BattleLayoutPanel.MODE_SPRITE || battleLayoutPanel.currentSpritesetMode != BattleLayoutPanel.SPRITESETMODE_ENEMY) {
            battleLayoutPanel.setCurrentMode(BattleLayoutPanel.MODE_SPRITE);
            battleLayoutPanel.setCurrentSpritesetMode(BattleLayoutPanel.SPRITESETMODE_ENEMY);
        }
    }

    private void onTableAIRegionsDataChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        if (row == battleLayoutPanel.getSelectedAIRegion()) {
            battleLayoutPanel.redraw();
        }
    }

    private void onTableAIRegionsSelectionChanged(ListSelectionEvent e) {
        int row = e.getFirstIndex();
        if (row != battleLayoutPanel.getSelectedAIRegion()) {
            battleLayoutPanel.setSelectedAIRegion(row);
            battleLayoutPanel.redraw();
        }
        if (battleLayoutPanel.currentMode != BattleLayoutPanel.MODE_SPRITE || battleLayoutPanel.currentSpritesetMode != BattleLayoutPanel.SPRITESETMODE_AIREGION) {
            battleLayoutPanel.setCurrentMode(BattleLayoutPanel.MODE_SPRITE);
            battleLayoutPanel.setCurrentSpritesetMode(BattleLayoutPanel.SPRITESETMODE_AIREGION);
        }
    }

    private void onTableAIPointsDataChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        if (row == battleLayoutPanel.getSelectedAIPoint()) {
            battleLayoutPanel.redraw();
        }
    }

    private void onTableAIPointsSelectionChanged(ListSelectionEvent e) {
        int row = e.getFirstIndex();
        if (row != battleLayoutPanel.getSelectedAIPoint()) {
            battleLayoutPanel.setSelectedAIPoint(row);
            UpdateEnemyControls(row);
            battleLayoutPanel.redraw();
        }
        if (battleLayoutPanel.currentMode != BattleLayoutPanel.MODE_SPRITE || battleLayoutPanel.currentSpritesetMode != BattleLayoutPanel.SPRITESETMODE_AIPOINT) {
            battleLayoutPanel.setCurrentMode(BattleLayoutPanel.MODE_SPRITE);
            battleLayoutPanel.setCurrentSpritesetMode(BattleLayoutPanel.SPRITESETMODE_AIPOINT);
        }
    }
    
    /**
     * To create a new Main Editor, copy the below code
     * Don't forget to change the new main class (below)
     */
    public static void main(String args[]) {
        AbstractMainEditor.programSetup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainEditor().setVisible(true);  // <------ Change this class to new Main Editor class
            }
        });
    }
    /**
     * To create a new Main Editor, copy the above code
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.sfc.sf2.battle.models.AIPointPropertiesTableModel aIPointPropertiesTableModel;
    private com.sfc.sf2.battle.models.AIRegionPropertiesTableModel aIRegionPropertiesTableModel;
    private com.sfc.sf2.core.gui.controls.AccordionPanel accordionPanel1;
    private com.sfc.sf2.core.gui.controls.AccordionPanel accordionPanel2;
    private com.sfc.sf2.battle.models.AllyPropertiesTableModel allyPropertiesTableModel;
    private com.sfc.sf2.battle.gui.BattleLayoutPanel battleLayoutPanel;
    private com.sfc.sf2.core.gui.controls.ColorPicker colorPicker1;
    private com.sfc.sf2.core.gui.controls.Console console1;
    private com.sfc.sf2.battle.models.EnemyPropertiesTableModel enemyPropertiesTableModel;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton1;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton10;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton11;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton12;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton3;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton4;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton5;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton6;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton7;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton8;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton9;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox_AI;
    private javax.swing.JComboBox<String> jComboBox_Items;
    private javax.swing.JComboBox<String> jComboBox_Name;
    private javax.swing.JComboBox<String> jComboBox_Order1;
    private javax.swing.JComboBox<String> jComboBox_Order2;
    private javax.swing.JComboBox<String> jComboBox_Spawn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JSpinner jSpinner6;
    private javax.swing.JSpinner jSpinner7;
    private javax.swing.JSpinner jSpinner8;
    private javax.swing.JSpinner jSpinner9;
    private javax.swing.JSpinner jSpinner_OrderTarget1;
    private javax.swing.JSpinner jSpinner_OrderTarget2;
    private javax.swing.JSpinner jSpinner_Trigger1;
    private javax.swing.JSpinner jSpinner_Trigger2;
    private javax.swing.JSpinner jSpinner_Trigger3;
    private javax.swing.JSpinner jSpinner_X;
    private javax.swing.JSpinner jSpinner_Y;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField_ItemFlags;
    private com.sfc.sf2.core.gui.controls.Table tableAIPoints;
    private com.sfc.sf2.core.gui.controls.Table tableAIRegions;
    private com.sfc.sf2.core.gui.controls.Table tableAllies;
    private com.sfc.sf2.core.gui.controls.Table tableEnemies;
    // End of variables declaration//GEN-END:variables
}
