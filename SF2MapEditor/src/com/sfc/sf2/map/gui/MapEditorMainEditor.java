/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.gui;

import com.sfc.sf2.core.gui.AbstractMainEditor;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.gui.layout.LayoutAnimator;
import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.map.Map;
import com.sfc.sf2.map.MapManager;
import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.animation.MapAnimationFrame;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.settings.MapBlockSettings;
import java.nio.file.Path;
import java.util.logging.Level;
import javax.swing.JCheckBox;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;

/**
 *
 * @author wiz
 */
public class MapEditorMainEditor extends AbstractMainEditor {
    
    private final MapBlockSettings mapLayoutSettings = new MapBlockSettings();
    MapManager mapManager = new MapManager();
    
    JCheckBox tabRelativeCheckbox;
    boolean tabRelativeCheckboxState;
    JCheckBox actionRelativeCheckbox;
    boolean actionRelativeCheckboxState;
    
    public MapEditorMainEditor() {
        super();
        SettingsManager.registerSettingsStore("mapLayout", mapLayoutSettings);
        initComponents();
        initCore(console1);
    }
    
    @Override
    protected void initEditor() {
        super.initEditor();
        
        accordionPanel1.setExpanded(false);
        accordionPanel2.setExpanded(false);
        
        colorPicker1.setColor(SettingsManager.getGlobalSettings().getTransparentBGColor());
        colorPickerTileset.setColor(mapLayoutSettings.getTilesetBGColor());
        jComboBox4.setSelectedIndex(mapLayoutSettings.getTilesetScale()-1);
        
        mapLayoutPanel.setShowGrid(jCheckBox10.isSelected());
        mapLayoutPanel.setDisplayScale(jComboBox9.getSelectedIndex()+1);
        mapLayoutPanel.setBGColor(colorPicker1.getColor());
        mapLayoutPanel.setShowPriority(jCheckBox13.isSelected());
        mapLayoutPanel.setShowExplorationFlags(jCheckBox11.isSelected());
        mapLayoutPanel.setShowInteractionFlags(false);
        mapLayoutPanel.setDrawMode_Tabs(MapLayoutPanel.DRAW_MODE_NONE);
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_ALL, false);
        
        mapLayoutPanel.setShowAreasOverlay(jCheckBox1.isSelected());
        mapLayoutPanel.setShowAreasUnderlay(jCheckBox4.isSelected());
        mapLayoutPanel.setShowFlagCopyResult(jCheckBox6.isSelected());
        mapLayoutPanel.setShowStepCopyResult(jCheckBox12.isSelected());
        
        mapBlocksetLayoutPanel.setShowGrid(jCheckBox3.isSelected());
        mapBlocksetLayoutPanel.setDisplayScale(jComboBox3.getSelectedIndex()+1);
        mapBlocksetLayoutPanel.setBGColor(colorPickerBlockset.getColor());
        mapBlocksetLayoutPanel.setShowPriority(jCheckBox5.isSelected());
        mapBlocksetLayoutPanel.setItemsPerRow((int)jSpinner7.getValue());
        
        //mapLayoutPanel.setLeftSlot(leftSlotBlockPanel);
        //mapBlocksetLayoutPanel.setLeftSlotBlockPanel(leftSlotBlockPanel);
        //mapBlocksetLayoutPanel.setRightSlotBlockPanel(rightSlotBlockPanel);
        
        tilesetLayoutPanelAnim.setBGColor(colorPickerTileset.getColor());
        tilesetLayoutPanelAnim.setShowGrid(jCheckBox2.isSelected());
        tilesetLayoutPanelAnim.setDisplayScale(jComboBox4.getSelectedIndex()+1);
        tilesetLayoutPanelAnim.setItemsPerRow((int)jSpinner6.getValue());
        tilesetLayoutPanelAnim.setShowAnimationFrames(jCheckBox7.isSelected());
        tilesetLayoutPanelModified.setBGColor(colorPickerTileset.getColor());
        tilesetLayoutPanelModified.setShowGrid(jCheckBox2.isSelected());
        tilesetLayoutPanelModified.setDisplayScale(jComboBox4.getSelectedIndex()+1);
        tilesetLayoutPanelModified.setItemsPerRow((int)jSpinner6.getValue());
        tilesetLayoutPanelModified.setShowAnimationFrames(jCheckBox7.isSelected());
        
        tableAreas.addListSelectionListener(this::OnTableSelectionChanged);
        tableFlagCopies.addListSelectionListener(this::OnTableSelectionChanged);
        tableStepCopies.addListSelectionListener(this::OnTableSelectionChanged);
        tableRoofCopies.addListSelectionListener(this::OnTableSelectionChanged);
        tableWarps.addListSelectionListener(this::OnTableSelectionChanged);
        tableChestItems.addListSelectionListener(this::OnTableSelectionChanged);
        tableOtherItems.addListSelectionListener(this::OnTableSelectionChanged);
        
        tilesetLayoutPanelModified.getAnimator().addAnimationListener(this::onAnimationUpdated);
        
        tableAnimFrames.addListSelectionListener(this::onAnimationFramesSelectionChanged);
        tableAnimFrames.addTableModelListener(this::onAnimationFramesDataChanged);
        tableAnimFrames.jTable.getColumnModel().getColumn(0).setMaxWidth(30);
        
        infoButtonSharedBlocks.setVisible(false);
        infoButtonSharedAnimation.setVisible(false);
    }
    
    @Override
    protected void onDataLoaded() {
        super.onDataLoaded();
        
        Map map = mapManager.getMap();        
        mapLayoutPanel.setMap(map);
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_EXPLORATION_FLAGS, jCheckBox11.isSelected());
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_GRID, jCheckBox10.isSelected());
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_AREAS, jCheckBox15.isSelected());
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_FLAG_COPIES, jCheckBox16.isSelected());
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_STEP_COPIES, jCheckBox17.isSelected());
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_ROOF_COPIES, jCheckBox18.isSelected());
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_WARPS, jCheckBox19.isSelected());
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_ITEMS, jCheckBox20.isSelected());
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_TRIGGERS, jCheckBox21.isSelected());
                
        if (map != null) {
            mapBlocksetLayoutPanel.setBlockset(map.getBlockset());
            mapBlocksetLayoutPanel.setTilesets(map.getLayout().getTilesets());
            
            mapAreaTableModel.setTableData(map.getAreas());
            mapFlagCopyTableModel.setTableData(map.getFlagCopies());
            mapStepCopyTableModel.setTableData(map.getStepCopies());
            MapCopyEventTableModel.setTableData(map.getRoofCopies());
            mapWarpTableModel.setTableData(map.getWarps());
            mapChestItemTableModel.setTableData(map.getChestItems());
            mapOtherItemTableModel.setTableData(map.getOtherItems());
            
            MapAnimation animation = map.getAnimation();
            if (animation != null) {
                tilesetLayoutPanelAnim.setMapAnimation(animation);
                tilesetLayoutPanelModified.setMapAnimation(animation);
                if (animation.getFrames().length == 0) {
                    tilesetLayoutPanelModified.setSelectedTileset(-1);
                } else {
                    tilesetLayoutPanelModified.setSelectedTileset(animation.getFrames()[0].getDestTileset());
                }
                tilesetLayoutPanelModified.getAnimator().stopAnimation();
                jCheckBox8.setSelected(false);
                jCheckBox9.setSelected(false);
                jSpinner2.setValue(animation.getTilesetId());
                jSpinner3.setValue(animation.getLength());

                tableAnimFrames.jTable.clearSelection();
                mapAnimationFrameTableModel.setTableData(animation.getFrames());
            }

            String sharedBlockInfo = mapManager.getSharedBlockInfo();
            infoButtonSharedBlocks.setVisible(sharedBlockInfo != null);
            if (sharedBlockInfo != null) {
                infoButtonSharedBlocks.setMessageText("This block and layout data are also used by the following maps:\n" + sharedBlockInfo + "\nAny changes will affect these other maps.\n\nTo unlink the maps, you can export this layout for a specific map and then update \\maps\\entries.asm");
            }
            String sharedAnimationInfo = mapManager.getSharedAnimationInfo();
            infoButtonSharedAnimation.setVisible(sharedAnimationInfo != null);
            if (sharedAnimationInfo != null) {
                infoButtonSharedAnimation.setMessageText("This animation data is also used by the following maps:\n" + sharedAnimationInfo + "\nAny changes will affect these other maps.\n\nTo unlink the maps, you can export this animation for a specific map and then update \\maps\\entries.asm");
            }
        } else {
            mapBlocksetLayoutPanel.setBlockset(null);
            
            mapAreaTableModel.setTableData(null);
            mapFlagCopyTableModel.setTableData(null);
            mapStepCopyTableModel.setTableData(null);
            MapCopyEventTableModel.setTableData(null);
            mapWarpTableModel.setTableData(null);
            mapChestItemTableModel.setTableData(null);
            mapOtherItemTableModel.setTableData(null);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupMapActions = new javax.swing.ButtonGroup();
        mapAreaTableModel = new com.sfc.sf2.map.models.MapAreaTableModel();
        mapFlagCopyTableModel = new com.sfc.sf2.map.models.MapFlagCopyEventTableModel();
        mapChestItemTableModel = new com.sfc.sf2.map.models.MapItemTableModel();
        mapOtherItemTableModel = new com.sfc.sf2.map.models.MapItemTableModel();
        MapCopyEventTableModel = new com.sfc.sf2.map.models.MapCopyEventTableModel();
        mapStepCopyTableModel = new com.sfc.sf2.map.models.MapCopyEventTableModel();
        mapAnimationFrameTableModel = new com.sfc.sf2.map.animation.models.MapAnimationFrameTableModel();
        mapWarpTableModel = new com.sfc.sf2.map.models.MapWarpTableModel();
        flatOptionPaneWarningIcon1 = new com.formdev.flatlaf.icons.FlatOptionPaneWarningIcon();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel15 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel8 = new javax.swing.JPanel();
        jSplitPane4 = new javax.swing.JSplitPane();
        jPanel9 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        accordionPanel1 = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButton1 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton14 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton20 = new com.sfc.sf2.core.gui.controls.FileButton();
        accordionPanel2 = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButton3 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton4 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton5 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton6 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton7 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton8 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton9 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton10 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton11 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton12 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton13 = new com.sfc.sf2.core.gui.controls.FileButton();
        jPanel6 = new javax.swing.JPanel();
        jButton18 = new javax.swing.JButton();
        jSpinner4 = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton19 = new javax.swing.JButton();
        jSpinner5 = new javax.swing.JSpinner();
        jLabel16 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        fileButton15 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton16 = new com.sfc.sf2.core.gui.controls.FileButton();
        jLabel11 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        infoButton1 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel35 = new javax.swing.JPanel();
        fileButton17 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton18 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton19 = new com.sfc.sf2.core.gui.controls.FileButton();
        jLabel12 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        infoButton2 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel11 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        mapBlocksetLayoutPanel = new com.sfc.sf2.map.block.gui.MapBlocksetLayoutPanel();
        jPanel14 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        infoButton3 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton4 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel17 = new javax.swing.JPanel();
        jRadioButton2 = new javax.swing.JRadioButton();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        infoButton10 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel31 = new javax.swing.JPanel();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        infoButton5 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton6 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton7 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton8 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton9 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        infoButton11 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton10 = new javax.swing.JRadioButton();
        infoButton12 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton13 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton11 = new javax.swing.JRadioButton();
        infoButton14 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton12 = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        jRadioButton13 = new javax.swing.JRadioButton();
        infoButton15 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton14 = new javax.swing.JRadioButton();
        infoButton16 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton15 = new javax.swing.JRadioButton();
        infoButton17 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel13 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jSpinner7 = new javax.swing.JSpinner();
        jLabel20 = new javax.swing.JLabel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jLabel21 = new javax.swing.JLabel();
        colorPickerBlockset = new com.sfc.sf2.core.gui.controls.ColorPicker();
        infoButtonSharedBlocks = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel21 = new javax.swing.JPanel();
        tableAreas = new com.sfc.sf2.core.gui.controls.Table();
        jPanel12 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jPanel22 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel24 = new javax.swing.JPanel();
        tableFlagCopies = new com.sfc.sf2.core.gui.controls.Table();
        jPanel20 = new javax.swing.JPanel();
        jCheckBox6 = new javax.swing.JCheckBox();
        jPanel25 = new javax.swing.JPanel();
        tableStepCopies = new com.sfc.sf2.core.gui.controls.Table();
        jPanel32 = new javax.swing.JPanel();
        jCheckBox12 = new javax.swing.JCheckBox();
        jPanel26 = new javax.swing.JPanel();
        tableRoofCopies = new com.sfc.sf2.core.gui.controls.Table();
        jPanel27 = new javax.swing.JPanel();
        tableWarps = new com.sfc.sf2.core.gui.controls.Table();
        jPanel28 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel29 = new javax.swing.JPanel();
        tableChestItems = new com.sfc.sf2.core.gui.controls.Table();
        jPanel30 = new javax.swing.JPanel();
        tableOtherItems = new com.sfc.sf2.core.gui.controls.Table();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tilesetLayoutPanelAnim = new com.sfc.sf2.map.animation.gui.MapAnimationTilesetLayoutPanel();
        tableAnimFrames = new com.sfc.sf2.core.gui.controls.Table();
        jScrollPane11 = new javax.swing.JScrollPane();
        tilesetLayoutPanelModified = new com.sfc.sf2.map.animation.gui.MapModifiedTilesetLayoutPanel();
        jPanel38 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jCheckBox2 = new javax.swing.JCheckBox();
        colorPickerTileset = new com.sfc.sf2.core.gui.controls.ColorPicker();
        jLabel17 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jSpinner6 = new javax.swing.JSpinner();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        infoButtonSharedAnimation = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        mapLayoutPanel = new com.sfc.sf2.map.gui.MapLayoutPanel();
        jPanel33 = new javax.swing.JPanel();
        jComboBox9 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jCheckBox10 = new javax.swing.JCheckBox();
        colorPicker1 = new com.sfc.sf2.core.gui.controls.ColorPicker();
        jLabel59 = new javax.swing.JLabel();
        jCheckBox11 = new javax.swing.JCheckBox();
        jCheckBox13 = new javax.swing.JCheckBox();
        jCheckBox15 = new javax.swing.JCheckBox();
        jCheckBox16 = new javax.swing.JCheckBox();
        jCheckBox17 = new javax.swing.JCheckBox();
        jCheckBox18 = new javax.swing.JCheckBox();
        jCheckBox21 = new javax.swing.JCheckBox();
        jCheckBox20 = new javax.swing.JCheckBox();
        jCheckBox19 = new javax.swing.JCheckBox();
        jCheckBox9 = new javax.swing.JCheckBox();
        jCheckBox22 = new javax.swing.JCheckBox();
        console1 = new com.sfc.sf2.core.gui.controls.Console();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SF2MapEditor");

        jSplitPane1.setDividerLocation(900);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);

        jSplitPane2.setDividerLocation(700);
        jSplitPane2.setOneTouchExpandable(true);

        jSplitPane4.setDividerLocation(300);
        jSplitPane4.setOneTouchExpandable(true);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel3.setPreferredSize(new java.awt.Dimension(590, 135));

        accordionPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Paths"));

        fileButton1.setFilePath("..\\graphics\\maps\\mappalettes\\entries.asm");
        fileButton1.setLabelText("Palette entries :");

        fileButton2.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
        fileButton2.setLabelText("Tileset entries :");

        fileButton14.setFilePath(".\\entries.asm");
        fileButton14.setLabelText("Map entries :");

        fileButton20.setFilePath("..\\..\\sf2enums.asm");
        fileButton20.setLabelText("Sf2enums :");

        javax.swing.GroupLayout accordionPanel1Layout = new javax.swing.GroupLayout(accordionPanel1);
        accordionPanel1.setLayout(accordionPanel1Layout);
        accordionPanel1Layout.setHorizontalGroup(
            accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
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
                .addComponent(fileButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        accordionPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Map files"));

        fileButton3.setFilePath("00-tilesets.asm");
        fileButton3.setLabelText("Tilesets :");

        fileButton4.setFilePath("0-blocks.bin");
        fileButton4.setLabelText("Blocks :");

        fileButton5.setFilePath("1-layout.bin");
        fileButton5.setLabelText("Layout :");

        fileButton6.setFilePath("2-areas.asm");
        fileButton6.setLabelText("Areas :");

        fileButton7.setFilePath("3-flag-events.asm");
        fileButton7.setLabelText("Flag events :");

        fileButton8.setFilePath("4-step-events.asm");
        fileButton8.setLabelText("Step events :");

        fileButton9.setFilePath("5-roof-events.asm");
        fileButton9.setLabelText("Roof events :");

        fileButton10.setFilePath("6-warp-events.asm");
        fileButton10.setLabelText("Warps :");

        fileButton11.setFilePath("7-chest-items.asm");
        fileButton11.setLabelText("Chest items :");

        fileButton12.setFilePath("8-other-items.asm");
        fileButton12.setLabelText("Other items :");

        fileButton13.setFilePath("9-animations.asm");
        fileButton13.setLabelText("Animations :");

        javax.swing.GroupLayout accordionPanel2Layout = new javax.swing.GroupLayout(accordionPanel2);
        accordionPanel2.setLayout(accordionPanel2Layout);
        accordionPanel2Layout.setHorizontalGroup(
            accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        accordionPanel2Layout.setVerticalGroup(
            accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Import"));

        jButton18.setText("Import");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(3, 0, 100, 1));

        jLabel4.setText("Map :");

        jLabel18.setText("Import map disassembly files");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton18))
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jButton18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Export"));

        jLabel2.setText("Export map disassembly files");

        jButton19.setText("Export");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jSpinner5.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 1));

        jLabel16.setText("Map :");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton19))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jButton19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap())
        );

        jTabbedPane5.addTab("Map", jPanel5);

        jPanel34.setBorder(javax.swing.BorderFactory.createTitledBorder("Blockset image export"));

        fileButton15.setFilePath(".\\export\\blockset.png");
        fileButton15.setLabelText("Blockset image :");

        fileButton16.setFilePath(".\\export\\blockset_hptiles.txt");
        fileButton16.setLabelText("Blockset priority tiles :");

        jLabel11.setText("Export blockset data");
        jLabel11.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jButton4.setText("Export");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        infoButton1.setMessageText("<html><b>Image Format:</b> Select an image File (e.g. PNG or GIF).<br>Color format should be 4BPP / 16 indexed colors.<br>(Images of 8BPP / 256 indexed colors will be converted to 4 BPP / 16). <br><br><b>Priority tiles:</b> Outputs a a representation of the map's blockset with 'H' = a high-priority tile, and 'L' = a regular tile (low priority).<br>High priority tells the engine to draw tiles over the sprites (i.e. player character).</html>");
        infoButton1.setText("");

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addComponent(jButton4))
                    .addComponent(fileButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jButton4)
                    .addComponent(infoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane5.addTab("Blockset Image", jPanel34);

        jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder("Map layout image export"));

        fileButton17.setFilePath("\\.export\\maplayout.png");
        fileButton17.setLabelText("Layout image :");

        fileButton18.setFilePath("\\.export\\maplayout_flags.txt");
        fileButton18.setLabelText("Layout flags :");

        fileButton19.setFilePath("\\.export\\layout_hptiles.txt");
        fileButton19.setLabelText("Layout priority tiles :");

        jLabel12.setText("Export map layout data");

        jButton5.setText("Export");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        infoButton2.setMessageText("<html><b>Image Format:</b> Select an image File (e.g. PNG or GIF).<br>Color format should be 4BPP / 16 indexed colors.<br>(Images of 8BPP / 256 indexed colors will be converted to 4 BPP / 16).<br><br><b>Flags:</b> Outputs a representation of the map containing flag values (i.e. if the tile has an item, warp, etc flag on it).<br><br><b>Priority tiles:</b> Outputs a a representation of the map with 'H' = a high-priority tile, and 'L' = a regular tile (low priority).<br>High priority tells the engine to draw tiles over the sprites (i.e. player character).</html>");
        infoButton2.setText("");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5))
                    .addComponent(fileButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileButton17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5)
                    .addComponent(infoButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane5.addTab("Layout Image", jPanel35);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane5)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accordionPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accordionPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(accordionPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accordionPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Import/Export", jPanel3);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        jSplitPane4.setLeftComponent(jPanel9);

        jTabbedPane2.setMinimumSize(new java.awt.Dimension(390, 185));
        jTabbedPane2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane2StateChanged(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Blockset"));

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        javax.swing.GroupLayout mapBlocksetLayoutPanelLayout = new javax.swing.GroupLayout(mapBlocksetLayoutPanel);
        mapBlocksetLayoutPanel.setLayout(mapBlocksetLayoutPanelLayout);
        mapBlocksetLayoutPanelLayout.setHorizontalGroup(
            mapBlocksetLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 398, Short.MAX_VALUE)
        );
        mapBlocksetLayoutPanelLayout.setVerticalGroup(
            mapBlocksetLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 859, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(mapBlocksetLayoutPanel);

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Actions"));

        jButton1.setText("Undo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Exploration Flags"));

        buttonGroupMapActions.add(jRadioButton1);
        jRadioButton1.setText("Obstructed");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroupMapActions.add(jRadioButton3);
        jRadioButton3.setText("Stairs");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        infoButton3.setMessageText("<html><b>Stairs flag:</b> Marks a block so that character will walk diagonally up/down when moving left/right on the tile.<br>- Left click to flag a block as stairs. Left click again to toggle stairs direction<br>- Middle click to clear all flags from the block<br>- Right click to remove the stairs flag</html>");
        infoButton3.setText("");

        infoButton4.setMessageText("<html><b>Obstructed flag:</b> Marks a block so that it cannot be walked on.<br>- Left click to flag a block as obstructed<br>- Middle click to clear all flags from the block<br>- Right click to remove the obstructed flag</html>");
        infoButton4.setText("");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jRadioButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jRadioButton1)
                    .addComponent(infoButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(infoButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected Blocks"));

        buttonGroupMapActions.add(jRadioButton2);
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("Apply Blocks");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jPanel18.setPreferredSize(new java.awt.Dimension(24, 24));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );

        jPanel19.setPreferredSize(new java.awt.Dimension(24, 24));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );

        jLabel3.setText("Left click :");

        jLabel7.setText("Right click :");

        infoButton10.setMessageText("<html><b>Block editing:</b> Place blocks on the map.<br>- Left click to place the 'left click' block<br>- Middle click to copy the hovered map block into the 'left click' slot. Hold and drag to copy a range of blocks<br>- Right click to place the 'right click' block</html>");
        infoButton10.setText("");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19))))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jRadioButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(infoButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder("Action Flags"));

        buttonGroupMapActions.add(jRadioButton4);
        jRadioButton4.setText("Warp");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        buttonGroupMapActions.add(jRadioButton5);
        jRadioButton5.setText("Barrel");
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });

        buttonGroupMapActions.add(jRadioButton6);
        jRadioButton6.setText("Vase");
        jRadioButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton6ActionPerformed(evt);
            }
        });

        buttonGroupMapActions.add(jRadioButton7);
        jRadioButton7.setText("Table");
        jRadioButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton7ActionPerformed(evt);
            }
        });

        buttonGroupMapActions.add(jRadioButton8);
        jRadioButton8.setText("Trigger");
        jRadioButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton8ActionPerformed(evt);
            }
        });

        infoButton5.setMessageText("<html><b>Warp flag:</b> Marks a block with a warp trigger. Must be used in conjunction with a Warp Event..<br>- Left click to flag a block with a warp<br>- Middle click to clear all flags from the block<br>- Right click to remove the warp flag</html>");
        infoButton5.setText("");

        infoButton6.setMessageText("<html><b>Trigger flag:</b> Marks a block so that it will trigger a search for Copy Events.<br>- Left click to flag a block with a trigger<br>- Middle click to clear all flags from the block<br>- Right click to remove the trigger  flag</html>");
        infoButton6.setText("");

        infoButton7.setMessageText("<html><b>Barrel flag:</b> Marks a block to be searchable with the barrel message. Combine with \"Other Items\" to allow player to obtain items.<br>- Left click to flag a block as a barrel<br>- Middle click to clear all flags from the block<br>- Right click to remove the barrel flag</html>");
        infoButton7.setText("");

        infoButton8.setMessageText("<html><b>Vase flag:</b> Marks a block to be searchable with the vase message. Combine with \"Other Items\" to allow player to obtain items.<br>- Left click to flag a block as a vase<br>- Middle click to clear all flags from the block<br>- Right click to remove the vase flag</html>");
        infoButton8.setText("");

        infoButton9.setMessageText("<html><b>Table flag:</b> Marks the block so that interacting with it will interact with the block on the other side of it.<br>- Left click to flag a block as a table<br>- Middle click to clear all flags from the block<br>- Right click to remove the table flag</html>");
        infoButton9.setText("");

        buttonGroupMapActions.add(jRadioButton9);
        jRadioButton9.setText("Shelf");
        jRadioButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton9ActionPerformed(evt);
            }
        });

        infoButton11.setMessageText("<html><b>Shelf flag:</b> Marks a block to be searchable with the bookshelf message. Combine with \"Other Items\" to allow player to obtain items.<br>- Left click to flag a block as a bookshelf<br>- Middle click to clear all flags from the block<br>- Right click to remove the shelf flag<br><br>NOTE: This is separate from the map's s4_descriptions.asm</html>");
        infoButton11.setText("");

        buttonGroupMapActions.add(jRadioButton10);
        jRadioButton10.setText("Search");
        jRadioButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton10ActionPerformed(evt);
            }
        });

        infoButton12.setMessageText("<html><b>Search flag:</b> Marks a block to be searchable with the \"searched the area\" message. Combine with \"Other Items\" to allow player to obtain items.<br>- Left click to flag a block as a searchable<br>- Middle click to clear all flags from the block<br>- Right click to remove the search flag</html>");
        infoButton12.setText("");

        infoButton13.setMessageText("<html><b>Caravan flag:</b> Marks the block so the player cannot walk on it but the caravan can.<br>- Left click to flag a block as a caravan block<br>- Middle click to clear all flags from the block<br>- Right click to remove the caravan flag</html>");
        infoButton13.setText("");

        buttonGroupMapActions.add(jRadioButton11);
        jRadioButton11.setText("Caravan");
        jRadioButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton11ActionPerformed(evt);
            }
        });

        infoButton14.setMessageText("<html><b>Raft flag:</b> Marks the block so the player cannot walk on it but the raft can.<br>- Left click to flag a block as a raft block<br>- Middle click to clear all flags from the block<br>- Right click to remove the raft flag</html>");
        infoButton14.setText("");

        buttonGroupMapActions.add(jRadioButton12);
        jRadioButton12.setText("Raft");
        jRadioButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton12ActionPerformed(evt);
            }
        });

        buttonGroupMapActions.add(jRadioButton13);
        jRadioButton13.setText("Chest");
        jRadioButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton13ActionPerformed(evt);
            }
        });

        infoButton15.setMessageText("<html><b>Chest flag:</b> Marks a block to be searchable with the chest message & animation. Combine with \"Chest Items\" to allow player to obtain items.<br>- Left click to flag a block as a chest<br>- Middle click to clear all flags from the block<br>- Right click to remove the chest flag</html>");
        infoButton15.setText("");

        buttonGroupMapActions.add(jRadioButton14);
        jRadioButton14.setText("L. Up");
        jRadioButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton14ActionPerformed(evt);
            }
        });

        infoButton16.setMessageText("<html><b>Layer Up flag:</b> Marks the block to shift the player (and followers) to the upper layer (the priority layer). Affected characters will be drawn above all map tiles.<br>- Left click to flag a block as a layer up block<br>- Middle click to clear all flags from the block<br>- Right click to remove the layer up flag<br><br>NOTE: When the player character is in the upper layer, flag checks will occur as if the character was in the upper region of the area (see map_06 as reference).</html>");
        infoButton16.setText("");

        buttonGroupMapActions.add(jRadioButton15);
        jRadioButton15.setText("L. Down");
        jRadioButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton15ActionPerformed(evt);
            }
        });

        infoButton17.setMessageText("<html><b>Layer Down flag:</b> Marks the block to shift the player (and followers) out of the upper layer (the priority layer). Restores characters to be drawn above map tiles but below 'priority' map tiles.<br>- Left click to flag a block as a layer down block<br>- Middle click to clear all flags from the block<br>- Right click to remove the layer down flag</html>");
        infoButton17.setText("");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator3)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addComponent(jRadioButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(jRadioButton8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel31Layout.createSequentialGroup()
                                        .addComponent(jRadioButton11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(infoButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel31Layout.createSequentialGroup()
                                        .addComponent(jRadioButton9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(infoButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel31Layout.createSequentialGroup()
                                        .addComponent(jRadioButton10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(infoButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel31Layout.createSequentialGroup()
                                        .addComponent(jRadioButton5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(infoButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel31Layout.createSequentialGroup()
                                        .addComponent(jRadioButton14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(infoButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel31Layout.createSequentialGroup()
                                        .addComponent(jRadioButton12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(infoButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel31Layout.createSequentialGroup()
                                        .addComponent(jRadioButton15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(infoButton17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel31Layout.createSequentialGroup()
                                        .addComponent(jRadioButton7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(infoButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel31Layout.createSequentialGroup()
                                        .addComponent(jRadioButton6)
                                        .addGap(10, 10, 10)
                                        .addComponent(infoButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel31Layout.createSequentialGroup()
                                        .addComponent(jRadioButton13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(infoButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jRadioButton4)
                    .addComponent(infoButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton8)
                    .addComponent(infoButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jRadioButton14)
                    .addComponent(infoButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton15)
                    .addComponent(infoButton17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jRadioButton10)
                    .addComponent(infoButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton13)
                    .addComponent(infoButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jRadioButton5)
                    .addComponent(infoButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton6)
                    .addComponent(infoButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jRadioButton9)
                    .addComponent(infoButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton7)
                    .addComponent(infoButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jRadioButton11)
                    .addComponent(infoButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton12)
                    .addComponent(infoButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Blocks display"));

        jLabel19.setText("Scale :");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jSpinner7.setModel(new javax.swing.SpinnerNumberModel(8, 4, 64, 2));
        jSpinner7.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner7StateChanged(evt);
            }
        });

        jLabel20.setText("Blocks per row :");

        jCheckBox3.setSelected(true);
        jCheckBox3.setText("Show grid");
        jCheckBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox3ItemStateChanged(evt);
            }
        });

        jCheckBox5.setText("Show priority");
        jCheckBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox5ItemStateChanged(evt);
            }
        });

        jLabel21.setText("BG :");

        colorPickerBlockset.addColorChangedListener(new com.sfc.sf2.core.gui.controls.ColorPicker.ColorChangedListener() {
            public void colorChanged(java.awt.event.ActionEvent evt) {
                colorPickerBlocksetColorChanged(evt);
            }
        });

        javax.swing.GroupLayout colorPickerBlocksetLayout = new javax.swing.GroupLayout(colorPickerBlockset);
        colorPickerBlockset.setLayout(colorPickerBlocksetLayout);
        colorPickerBlocksetLayout.setHorizontalGroup(
            colorPickerBlocksetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        colorPickerBlocksetLayout.setVerticalGroup(
            colorPickerBlocksetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        infoButtonSharedBlocks.setIcon(flatOptionPaneWarningIcon1);
        infoButtonSharedBlocks.setText("");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addComponent(infoButtonSharedBlocks, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPickerBlockset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox5)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel20)
                    .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox3)
                    .addComponent(infoButtonSharedBlocks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel19)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox5)
                    .addComponent(jLabel21)
                    .addComponent(colorPickerBlockset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane2.addTab("Layout", jPanel4);

        tableAreas.setBorder(null);
        tableAreas.setModel(mapAreaTableModel);
        tableAreas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableAreas.setSingleClickText(true);
        tableAreas.setSpinnerNumberEditor(true);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Areas display"));

        jCheckBox1.setText("Show upper layer overlay");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox4.setText("Show BG underlay");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox4))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tableAreas, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableAreas, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Areas", jPanel21);

        jTabbedPane3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane3StateChanged(evt);
            }
        });

        tableFlagCopies.setBorder(null);
        tableFlagCopies.setModel(mapFlagCopyTableModel);
        tableFlagCopies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableFlagCopies.setSingleClickText(true);
        tableFlagCopies.setSpinnerNumberEditor(true);

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Flag copies display"));

        jCheckBox6.setText("Show copy");
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox6)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableFlagCopies, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableFlagCopies, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Flag Copies", jPanel24);

        tableStepCopies.setBorder(null);
        tableStepCopies.setModel(mapStepCopyTableModel);
        tableStepCopies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableStepCopies.setSingleClickText(true);
        tableStepCopies.setSpinnerNumberEditor(true);

        jPanel32.setBorder(javax.swing.BorderFactory.createTitledBorder("Step copies display"));

        jCheckBox12.setText("Show copy");
        jCheckBox12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox12)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableStepCopies, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                    .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableStepCopies, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Step Copies", jPanel25);

        tableRoofCopies.setBorder(null);
        tableRoofCopies.setModel(MapCopyEventTableModel);
        tableRoofCopies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableRoofCopies.setSingleClickText(true);
        tableRoofCopies.setSpinnerNumberEditor(true);

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableRoofCopies, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableRoofCopies, javax.swing.GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Roof Copies", jPanel26);

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

        jTabbedPane2.addTab("Block Copies", jPanel22);

        tableWarps.setBorder(null);
        tableWarps.setModel(mapWarpTableModel);
        tableWarps.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableWarps.setSingleClickText(true);
        tableWarps.setSpinnerNumberEditor(true);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableWarps, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableWarps, javax.swing.GroupLayout.DEFAULT_SIZE, 852, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Warps", jPanel27);

        tableChestItems.setBorder(null);
        tableChestItems.setModel(mapChestItemTableModel);
        tableChestItems.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableChestItems.setSingleClickText(true);
        tableChestItems.setSpinnerNumberEditor(true);

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableChestItems, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableChestItems, javax.swing.GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane4.addTab("Chest Items", jPanel29);

        tableOtherItems.setBorder(null);
        tableOtherItems.setModel(mapOtherItemTableModel);
        tableOtherItems.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableOtherItems.setSingleClickText(true);
        tableOtherItems.setSpinnerNumberEditor(true);

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableOtherItems, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableOtherItems, javax.swing.GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane4.addTab("Other Items", jPanel30);

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(jTabbedPane4)
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane4)
        );

        jTabbedPane2.addTab("Items", jPanel28);

        jScrollPane10.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Animation Tileset"));
        jScrollPane10.setPreferredSize(new java.awt.Dimension(335, 154));

        javax.swing.GroupLayout tilesetLayoutPanelAnimLayout = new javax.swing.GroupLayout(tilesetLayoutPanelAnim);
        tilesetLayoutPanelAnim.setLayout(tilesetLayoutPanelAnimLayout);
        tilesetLayoutPanelAnimLayout.setHorizontalGroup(
            tilesetLayoutPanelAnimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 367, Short.MAX_VALUE)
        );
        tilesetLayoutPanelAnimLayout.setVerticalGroup(
            tilesetLayoutPanelAnimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );

        jScrollPane10.setViewportView(tilesetLayoutPanelAnim);

        tableAnimFrames.setBorder(null);
        tableAnimFrames.setModel(mapAnimationFrameTableModel);
        tableAnimFrames.setSpinnerNumberEditor(true);

        jScrollPane11.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Modified Tileset"));
        jScrollPane11.setPreferredSize(new java.awt.Dimension(335, 154));

        javax.swing.GroupLayout tilesetLayoutPanelModifiedLayout = new javax.swing.GroupLayout(tilesetLayoutPanelModified);
        tilesetLayoutPanelModified.setLayout(tilesetLayoutPanelModifiedLayout);
        tilesetLayoutPanelModifiedLayout.setHorizontalGroup(
            tilesetLayoutPanelModifiedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 367, Short.MAX_VALUE)
        );
        tilesetLayoutPanelModifiedLayout.setVerticalGroup(
            tilesetLayoutPanelModifiedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );

        jScrollPane11.setViewportView(tilesetLayoutPanelModified);

        jPanel38.setBorder(javax.swing.BorderFactory.createTitledBorder("Tileset display"));
        jPanel38.setMinimumSize(new java.awt.Dimension(340, 100));

        jLabel15.setText("Scale :");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
        jComboBox4.setSelectedIndex(1);
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        jCheckBox2.setText("Show grid");
        jCheckBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox2ItemStateChanged(evt);
            }
        });

        colorPickerTileset.addColorChangedListener(new com.sfc.sf2.core.gui.controls.ColorPicker.ColorChangedListener() {
            public void colorChanged(java.awt.event.ActionEvent evt) {
                colorPickerTilesetColorChanged(evt);
            }
        });

        javax.swing.GroupLayout colorPickerTilesetLayout = new javax.swing.GroupLayout(colorPickerTileset);
        colorPickerTileset.setLayout(colorPickerTilesetLayout);
        colorPickerTilesetLayout.setHorizontalGroup(
            colorPickerTilesetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        colorPickerTilesetLayout.setVerticalGroup(
            colorPickerTilesetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        jLabel17.setText("BG :");

        jLabel43.setText("Tiles per row :");

        jSpinner6.setModel(new javax.swing.SpinnerNumberModel(16, 4, 32, 4));
        jSpinner6.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner6StateChanged(evt);
            }
        });

        jCheckBox7.setSelected(true);
        jCheckBox7.setText("Show anim frames");
        jCheckBox7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox7ItemStateChanged(evt);
            }
        });

        jCheckBox8.setText("Preview anim");
        jCheckBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox8animationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addComponent(jCheckBox2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel43)
                        .addGap(0, 0, 0)
                        .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addComponent(jCheckBox7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPickerTileset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43)
                    .addComponent(jCheckBox2)
                    .addComponent(jLabel15)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jCheckBox7)
                    .addComponent(jCheckBox8)
                    .addComponent(colorPickerTileset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addContainerGap())
        );

        jLabel8.setText("Tileset :");

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(0, 0, 256, 1));
        jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner2StateChanged(evt);
            }
        });

        jLabel9.setText("Tileset length :");

        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(0, 0, 128, 1));
        jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner3StateChanged(evt);
            }
        });

        infoButtonSharedAnimation.setIcon(flatOptionPaneWarningIcon1);
        infoButtonSharedAnimation.setText("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(infoButtonSharedAnimation, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(infoButtonSharedAnimation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tableAnimFrames, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tableAnimFrames, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane2.addTab("Anims", jPanel23);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        javax.swing.GroupLayout mapLayoutPanelLayout = new javax.swing.GroupLayout(mapLayoutPanel);
        mapLayoutPanel.setLayout(mapLayoutPanelLayout);
        mapLayoutPanelLayout.setHorizontalGroup(
            mapLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1536, Short.MAX_VALUE)
        );
        mapLayoutPanelLayout.setVerticalGroup(
            mapLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1536, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(mapLayoutPanel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );

        jPanel33.setBorder(javax.swing.BorderFactory.createTitledBorder("Map view"));

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
        jComboBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox9ActionPerformed(evt);
            }
        });

        jLabel10.setText("Scale :");

        jCheckBox10.setText("Show grid");
        jCheckBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox10ActionPerformed(evt);
            }
        });

        colorPicker1.addColorChangedListener(new com.sfc.sf2.core.gui.controls.ColorPicker.ColorChangedListener() {
            public void colorChanged(java.awt.event.ActionEvent evt) {
                colorPicker1ColorChanged(evt);
            }
        });

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

        jLabel59.setText("BG :");

        jCheckBox11.setText("Exploration flags");
        jCheckBox11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox11ActionPerformed(evt);
            }
        });

        jCheckBox13.setText("Show priority");
        jCheckBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox13ActionPerformed(evt);
            }
        });

        jCheckBox15.setText("Areas");
        jCheckBox15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox15ActionPerformed(evt);
            }
        });

        jCheckBox16.setText("Flag Copies");
        jCheckBox16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox16ActionPerformed(evt);
            }
        });

        jCheckBox17.setText("Step Copies");
        jCheckBox17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox17ActionPerformed(evt);
            }
        });

        jCheckBox18.setText("Roof Copies");
        jCheckBox18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox18ActionPerformed(evt);
            }
        });

        jCheckBox21.setText("Triggers");
        jCheckBox21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox21ActionPerformed(evt);
            }
        });

        jCheckBox20.setText("Items");
        jCheckBox20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox20ActionPerformed(evt);
            }
        });

        jCheckBox19.setText("Warps");
        jCheckBox19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox19ActionPerformed(evt);
            }
        });

        jCheckBox9.setText("Preview anim");
        jCheckBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox9animationActionPerformed(evt);
            }
        });

        jCheckBox22.setText("Vehicles");
        jCheckBox22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox22ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addComponent(jCheckBox11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel59)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addComponent(jCheckBox16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox18)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addComponent(jCheckBox19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 175, Short.MAX_VALUE)
                        .addComponent(jCheckBox13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox9)))
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(colorPicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel59)
                    .addComponent(jCheckBox11)
                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jCheckBox10)
                    .addComponent(jCheckBox15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jCheckBox19)
                    .addComponent(jCheckBox20)
                    .addComponent(jCheckBox21)
                    .addComponent(jCheckBox22)
                    .addComponent(jCheckBox9)
                    .addComponent(jCheckBox13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jCheckBox16)
                    .addComponent(jCheckBox17)
                    .addComponent(jCheckBox18))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jSplitPane2.setRightComponent(jPanel10);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2)
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
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1416, 1008));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        Path paletteEntriesPath = PathHelpers.getBasePath().resolve(fileButton1.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButton2.getFilePath());
        Path mapEntriesPath = PathHelpers.getBasePath().resolve(fileButton14.getFilePath());
        Path sf2enumsPath = PathHelpers.getBasePath().resolve(fileButton20.getFilePath());
        int mapId = (int)jSpinner4.getValue();
        try {
            mapManager.importDisassemblyFromEntries(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, sf2enumsPath, mapId);
        } catch (Exception ex) {
            mapManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map animation be imported for map : " + mapId);
        }
        onDataLoaded();
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        mapLayoutPanel.revertLastAction();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        onMapActionCheckboxSet(null, MapLayoutPanel.MAP_FLAG_EDIT_BLOCK);
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        onMapActionCheckboxSet(jCheckBox11, MapBlock.MAP_FLAG_OBSTRUCTED);
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        onMapActionCheckboxSet(jCheckBox11, MapBlock.MAP_FLAG_STAIRS_RIGHT);
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        onMapActionCheckboxSet(jCheckBox19, MapBlock.MAP_FLAG_WARP);
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        onMapActionCheckboxSet(jCheckBox20, MapBlock.MAP_FLAG_BARREL);
    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void jRadioButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton6ActionPerformed
        onMapActionCheckboxSet(jCheckBox20, MapBlock.MAP_FLAG_VASE);
    }//GEN-LAST:event_jRadioButton6ActionPerformed

    private void jRadioButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton7ActionPerformed
        onMapActionCheckboxSet(jCheckBox20, MapBlock.MAP_FLAG_TABLE);
    }//GEN-LAST:event_jRadioButton7ActionPerformed

    private void jRadioButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton8ActionPerformed
        onMapActionCheckboxSet(jCheckBox21, MapBlock.MAP_FLAG_TRIGGER);
    }//GEN-LAST:event_jRadioButton8ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        /*String toolDir = System.getProperty("user.dir");
        Path toolPath = Paths.get(toolDir);
        
        String mapPath = jTextField24.getText();
        if(!mapPath.endsWith(File.separator)){
            mapPath = mapPath+""+File.separator;
        }
        //Path basePath = Paths.get(mapPath).toAbsolutePath();
        System.out.println(toolPath.toString());
        Path basePath = toolPath.resolve(Paths.get(mapPath)).normalize();
        System.out.println(basePath.toString());
        Path pPath = Paths.get(jTextField42.getText());
        Path pngPath;
        if(!pPath.isAbsolute()){
           pngPath = basePath.resolve(pPath).normalize();
        }else{
            pngPath = pPath;
        }
        System.out.println(pngPath.toString());
        Path hpPath = Paths.get(jTextField50.getText());
        Path hpFilepath;
        if(!hpPath.isAbsolute()){
           hpFilepath = basePath.resolve(hpPath).normalize();
        }else{
            hpFilepath = hpPath;
        }
        System.out.println(hpFilepath.toString());
        
        //mapManager.exportBlocksetImage(mapblockLayout, pngPath.toString(), hpFilepath.toString(), (int)jSpinner1.getValue());
        */
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
         /*                              
        String toolDir = System.getProperty("user.dir");
        Path toolPath = Paths.get(toolDir);
        
        String mapPath = jTextField24.getText();
        if(!mapPath.endsWith(File.separator)){
            mapPath = mapPath+""+File.separator;
        }
        //Path basePath = Paths.get(mapPath).toAbsolutePath();
        System.out.println(toolPath.toString());
        Path basePath = toolPath.resolve(Paths.get(mapPath)).normalize();
        System.out.println(basePath.toString());
        Path pPath = Paths.get(jTextField43.getText());
        Path pngPath;
        if(!pPath.isAbsolute()){
           pngPath = basePath.resolve(pPath).normalize();
        }else{
            pngPath = pPath;
        }
        Path fPath = Paths.get(jTextField47.getText());
        Path flagsPath;
        if(!fPath.isAbsolute()){
           flagsPath = basePath.resolve(fPath).normalize();
        }else{
            flagsPath = fPath;
        }
        Path tPath = Paths.get(jTextField44.getText());
        Path phtilesPath;
        if(!tPath.isAbsolute()){
           phtilesPath = basePath.resolve(tPath).normalize();
        }else{
            phtilesPath = tPath;
        }
        System.out.println(phtilesPath.toString());
        
        mapManager.exportMapLayoutImage(////mapLayoutPanel, pngPath.toString(), flagsPath.toString(), phtilesPath.toString());*/
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTabbedPane2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane2StateChanged
        SetTabRelativeCheckbox(null, MapLayoutPanel.DRAW_MODE_NONE);
        int index = jTabbedPane2.getSelectedIndex();
        mapLayoutPanel.setIsOnActionsTab(index == 0);
        switch (index) {
            case 0:     //Actions & Anims
                JCheckBox actionCheckbox = actionRelativeCheckbox;
                int mode = mapLayoutPanel.getCurrentMode();
                onMapActionCheckboxSet(null, -1);
                onMapActionCheckboxSet(actionCheckbox, mode);
                mapLayoutPanel.setDrawMode_Tabs(MapLayoutPanel.DRAW_MODE_NONE);
                break;
            case 1:     //Areas panel
                SetTabRelativeCheckbox(jCheckBox15, MapLayoutPanel.DRAW_MODE_AREAS);
                break;
            case 2:     //Block Copies panels
                jTabbedPane3StateChanged(new ChangeEvent(jTabbedPane3));
                break;
            case 3:     //Warps panel
                SetTabRelativeCheckbox(jCheckBox19, MapLayoutPanel.DRAW_MODE_WARPS);
                break;
            case 4:     //Items panel
                SetTabRelativeCheckbox(jCheckBox20, MapLayoutPanel.DRAW_MODE_ITEMS);
                break;
        }
        mapLayoutPanel.redraw();
    }//GEN-LAST:event_jTabbedPane2StateChanged

    private void jTabbedPane3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane3StateChanged
        int index = jTabbedPane2.getSelectedIndex();
        if (index != 2) return; //Is not on Block copies panel
        SetTabRelativeCheckbox(null, MapLayoutPanel.DRAW_MODE_NONE);
        index = jTabbedPane3.getSelectedIndex();
        switch (index) {
            default:     //Layout and Anims & Block Copies panels
                return;
            case 0:     //Flag copies
                SetTabRelativeCheckbox(jCheckBox16, MapLayoutPanel.DRAW_MODE_FLAG_COPIES);
                break;
            case 1:     //Step copies
                SetTabRelativeCheckbox(jCheckBox17, MapLayoutPanel.DRAW_MODE_STEP_COPIES);
                break;
            case 2:     //Roof copies
                SetTabRelativeCheckbox(jCheckBox18, MapLayoutPanel.DRAW_MODE_ROOF_COPIES);
                break;
        }
        mapLayoutPanel.redraw();
    }//GEN-LAST:event_jTabbedPane3StateChanged

    private void jComboBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox9ActionPerformed
        mapLayoutPanel.setDisplayScale(jComboBox9.getSelectedIndex()+1);
        mapLayoutSettings.setTilesetScale(jComboBox9.getSelectedIndex()+1);
        SettingsManager.saveSettingsFile();
    }//GEN-LAST:event_jComboBox9ActionPerformed

    private void jCheckBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox10ActionPerformed
        mapLayoutPanel.setShowGrid(jCheckBox10.isSelected());
    }//GEN-LAST:event_jCheckBox10ActionPerformed

    private void colorPicker1ColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorPicker1ColorChanged
        mapLayoutPanel.setBGColor(colorPicker1.getColor());
        mapLayoutSettings.setTilesetBGColor(colorPicker1.getColor());
        SettingsManager.saveSettingsFile();
    }//GEN-LAST:event_colorPicker1ColorChanged

    private void jCheckBox11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox11ActionPerformed
        mapLayoutPanel.setShowExplorationFlags(jCheckBox11.isSelected());
    }//GEN-LAST:event_jCheckBox11ActionPerformed

    private void jCheckBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox13ActionPerformed
        mapLayoutPanel.setShowPriority(jCheckBox13.isSelected());
    }//GEN-LAST:event_jCheckBox13ActionPerformed

    private void jCheckBox15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox15ActionPerformed
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_AREAS, jCheckBox15.isSelected());
    }//GEN-LAST:event_jCheckBox15ActionPerformed

    private void jCheckBox16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox16ActionPerformed
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_FLAG_COPIES, jCheckBox16.isSelected());
    }//GEN-LAST:event_jCheckBox16ActionPerformed

    private void jCheckBox17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox17ActionPerformed
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_STEP_COPIES, jCheckBox17.isSelected());
    }//GEN-LAST:event_jCheckBox17ActionPerformed

    private void jCheckBox18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox18ActionPerformed
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_ROOF_COPIES, jCheckBox18.isSelected());
    }//GEN-LAST:event_jCheckBox18ActionPerformed

    private void jCheckBox19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox19ActionPerformed
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_WARPS, jCheckBox19.isSelected());
    }//GEN-LAST:event_jCheckBox19ActionPerformed

    private void jCheckBox20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox20ActionPerformed
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_ITEMS, jCheckBox20.isSelected());
    }//GEN-LAST:event_jCheckBox20ActionPerformed

    private void jCheckBox21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox21ActionPerformed
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_TRIGGERS, jCheckBox21.isSelected());
    }//GEN-LAST:event_jCheckBox21ActionPerformed

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        if (jComboBox4.getSelectedIndex() >= 0 && tilesetLayoutPanelModified != null) {
            int scale = (int)jComboBox4.getSelectedIndex()+1;
            if (scale != mapLayoutSettings.getTilesetScale()) {
                tilesetLayoutPanelAnim.setDisplayScale(scale);
                tilesetLayoutPanelModified.setDisplayScale(scale);
                mapLayoutSettings.setTilesetScale(scale);
                SettingsManager.saveSettingsFile();
            }
        }
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jCheckBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox2ItemStateChanged
        tilesetLayoutPanelAnim.setShowGrid(jCheckBox2.isSelected());
        tilesetLayoutPanelModified.setShowGrid(jCheckBox2.isSelected());
    }//GEN-LAST:event_jCheckBox2ItemStateChanged

    private void colorPickerTilesetColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorPickerTilesetColorChanged
        tilesetLayoutPanelAnim.setBGColor(colorPickerTileset.getColor());
        tilesetLayoutPanelModified.setBGColor(colorPickerTileset.getColor());
        mapLayoutSettings.setTilesetBGColor(colorPickerTileset.getColor());
        SettingsManager.saveSettingsFile();
    }//GEN-LAST:event_colorPickerTilesetColorChanged

    private void jSpinner6StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner6StateChanged
        int tilesPerRow = (int)jSpinner6.getValue();
        tilesetLayoutPanelAnim.setItemsPerRow(tilesPerRow);
        tilesetLayoutPanelModified.setItemsPerRow(tilesPerRow);
    }//GEN-LAST:event_jSpinner6StateChanged

    private void jCheckBox7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox7ItemStateChanged
        tilesetLayoutPanelAnim.setShowAnimationFrames(jCheckBox7.isSelected());
        tilesetLayoutPanelModified.setShowAnimationFrames(jCheckBox7.isSelected());
    }//GEN-LAST:event_jCheckBox7ItemStateChanged

    private void jCheckBox8animationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox8animationActionPerformed
        boolean isSelected = ((JCheckBox)evt.getSource()).isSelected();
        jCheckBox8.setSelected(isSelected);
        jCheckBox9.setSelected(isSelected);
        tilesetLayoutPanelModified.setPreviewAnim(isSelected);
    }//GEN-LAST:event_jCheckBox8animationActionPerformed

    private void jSpinner2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner2StateChanged
        int value = (int)jSpinner2.getValue();
        MapAnimation anim = tilesetLayoutPanelModified.getMapAnimation();
        if (anim != null && anim.getTilesetId()!= value) {
            try {
                anim.setTilesetId(value);
                Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButton14.getFilePath());
                Tileset tileset = mapManager.getMapAnimationManager().importTileset(mapLayoutPanel.getMap().getLayout().getPalette(), tilesetEntriesPath, value);
                tilesetLayoutPanelAnim.setTileset(tileset);
            } catch (Exception ex) {
                Console.logger().log(Level.SEVERE, null, ex);
                Console.logger().severe("ERROR Tileset could not be imported for tileset : " + value);
            }
        }
    }//GEN-LAST:event_jSpinner2StateChanged

    private void jSpinner3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner3StateChanged
        int value = (int)jSpinner3.getValue();
        MapAnimation anim = tilesetLayoutPanelModified.getMapAnimation();
        if (anim != null && anim.getLength()!= value) {
            anim.setLength(value);
        }
    }//GEN-LAST:event_jSpinner3StateChanged

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        //TODO Export mapdata
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jCheckBox9animationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox9animationActionPerformed
        boolean isSelected = ((JCheckBox)evt.getSource()).isSelected();
        jCheckBox8.setSelected(isSelected);
        jCheckBox9.setSelected(isSelected);
        tilesetLayoutPanelModified.setPreviewAnim(isSelected);
    }//GEN-LAST:event_jCheckBox9animationActionPerformed

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        if (jComboBox3.getSelectedIndex() >= 0 && mapBlocksetLayoutPanel != null) {
            int scale = (int)jComboBox3.getSelectedIndex()+1;
            if (scale != mapLayoutSettings.getBlocksetScale()) {
                mapBlocksetLayoutPanel.setDisplayScale(scale);
                mapLayoutSettings.setBlocksetScale(scale);
                SettingsManager.saveSettingsFile();
            }
        }
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jSpinner7StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner7StateChanged
        if (mapBlocksetLayoutPanel != null) {
            int blocksPerRow = (int)jSpinner7.getValue();
            if (blocksPerRow != mapLayoutSettings.getBlocksetBlocksPerRow()) {
                mapBlocksetLayoutPanel.setItemsPerRow(blocksPerRow);
                mapLayoutSettings.setBlocksetBlocksPerRow(blocksPerRow);
                SettingsManager.saveSettingsFile();
            }
        }
    }//GEN-LAST:event_jSpinner7StateChanged

    private void jCheckBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox3ItemStateChanged
        if (mapBlocksetLayoutPanel != null) {
            mapBlocksetLayoutPanel.setShowGrid(jCheckBox3.isSelected());
        }
    }//GEN-LAST:event_jCheckBox3ItemStateChanged

    private void jCheckBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox5ItemStateChanged
        mapBlocksetLayoutPanel.setShowPriority(jCheckBox5.isSelected());
    }//GEN-LAST:event_jCheckBox5ItemStateChanged

    private void colorPickerBlocksetColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorPickerBlocksetColorChanged
        mapBlocksetLayoutPanel.setBGColor(colorPickerBlockset.getColor());
        mapLayoutSettings.setBlocksetBGColor(colorPickerBlockset.getColor());
        SettingsManager.saveSettingsFile();
    }//GEN-LAST:event_colorPickerBlocksetColorChanged

    private void jRadioButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton9ActionPerformed
        onMapActionCheckboxSet(jCheckBox20, MapBlock.MAP_FLAG_SHELF);
    }//GEN-LAST:event_jRadioButton9ActionPerformed

    private void jRadioButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton10ActionPerformed
        onMapActionCheckboxSet(jCheckBox20, MapBlock.MAP_FLAG_SEARCH);
    }//GEN-LAST:event_jRadioButton10ActionPerformed

    private void jRadioButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton11ActionPerformed
        onMapActionCheckboxSet(jCheckBox22, MapBlock.MAP_FLAG_CARAVAN);
    }//GEN-LAST:event_jRadioButton11ActionPerformed

    private void jRadioButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton12ActionPerformed
        onMapActionCheckboxSet(jCheckBox22, MapBlock.MAP_FLAG_RAFT);
    }//GEN-LAST:event_jRadioButton12ActionPerformed

    private void jCheckBox22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox22ActionPerformed
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_VEHICLES, jCheckBox22.isSelected());
    }//GEN-LAST:event_jCheckBox22ActionPerformed

    private void jRadioButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton13ActionPerformed
        onMapActionCheckboxSet(jCheckBox20, MapBlock.MAP_FLAG_CHEST);
    }//GEN-LAST:event_jRadioButton13ActionPerformed

    private void jRadioButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton14ActionPerformed
        onMapActionCheckboxSet(jCheckBox21, MapBlock.MAP_FLAG_LAYER_UP);
    }//GEN-LAST:event_jRadioButton14ActionPerformed

    private void jRadioButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton15ActionPerformed
        onMapActionCheckboxSet(jCheckBox21, MapBlock.MAP_FLAG_LAYER_DOWN);
    }//GEN-LAST:event_jRadioButton15ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        mapLayoutPanel.setShowAreasOverlay(jCheckBox1.isSelected());
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        mapLayoutPanel.setShowAreasUnderlay(jCheckBox4.isSelected());
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed
        mapLayoutPanel.setShowFlagCopyResult(jCheckBox6.isSelected());
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jCheckBox12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox12ActionPerformed
        mapLayoutPanel.setShowStepCopyResult(jCheckBox12.isSelected());
    }//GEN-LAST:event_jCheckBox12ActionPerformed

    private void SetTabRelativeCheckbox(JCheckBox checkbox, int mode) {
        if (checkbox == null) {
            // Restore checkboxes
            if (tabRelativeCheckbox != null) {
                tabRelativeCheckbox.setSelected(tabRelativeCheckboxState);
                tabRelativeCheckbox.setEnabled(true);
            }
            tabRelativeCheckbox = null;
        }
        else {
            if (tabRelativeCheckbox != null) {
                SetTabRelativeCheckbox(null, 0);
            }
            //If tabs change then disable the action tab affecting the checkboxes
            if (!mapLayoutPanel.isDrawMode_Tabs(MapLayoutPanel.DRAW_MODE_ACTION_FLAGS)) {
                JCheckBox actionCheckbox = actionRelativeCheckbox;
                int actionMode = mapLayoutPanel.getCurrentMode();
                onMapActionCheckboxSet(null, -1);
                actionRelativeCheckbox = actionCheckbox;
                mapLayoutPanel.setCurrentMode(actionMode);
            }
            // Lock active checkbox
            mapLayoutPanel.setDrawMode_Tabs(mode);
            tabRelativeCheckbox = checkbox;
            tabRelativeCheckboxState = checkbox.isSelected();
            tabRelativeCheckbox.setSelected(true);
            tabRelativeCheckbox.setEnabled(false);
        }
    }
    
    private void onMapActionCheckboxSet(JCheckBox checkbox, int mode) {
        if (mapLayoutPanel.getCurrentMode() == mode) return;
        mapLayoutPanel.setCurrentMode(mode);
        if (actionRelativeCheckbox != null) {
            // Restore checkboxes
            if (actionRelativeCheckbox != null) {
                actionRelativeCheckbox.setSelected(actionRelativeCheckboxState);
                actionRelativeCheckbox.setEnabled(true);
            }
            actionRelativeCheckbox = null;
        }
        if (checkbox != null) {
            // Lock active checkbox
            actionRelativeCheckbox = checkbox;
            actionRelativeCheckboxState = checkbox.isSelected();
            actionRelativeCheckbox.setSelected(true);
            actionRelativeCheckbox.setEnabled(false);
        }
        mapLayoutPanel.redraw();
    }
    
    private void onAnimationFramesSelectionChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() || tilesetLayoutPanelModified.getAnimator().isAnimating()) return;
        int selected = tableAnimFrames.jTable.getSelectedRow();
        tilesetLayoutPanelAnim.setSelectedFrame(selected);
        tilesetLayoutPanelModified.setSelectedFrame(selected);
        if (selected == -1) selected = 0;
        MapAnimation animation = tilesetLayoutPanelModified.getMapAnimation();
        int tileset = -1;
        if (animation != null && animation.getFrames() != null && selected < animation.getFrames().length) {
            tileset = animation.getFrames()[selected].getDestTileset();
        }
        tilesetLayoutPanelModified.setSelectedTileset(tileset);
    }
    
    private void onAnimationFramesDataChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.INSERT || e.getType() == TableModelEvent.DELETE) {
            //Number of animation frames changed
            MapAnimationFrame[] frames = mapAnimationFrameTableModel.getTableData(MapAnimationFrame[].class);
            tilesetLayoutPanelModified.getMapAnimation().setFrames(frames);
        } else if (e.getColumn() == 3) {
            //Editing destination tileset
            MapAnimation animation = tilesetLayoutPanelModified.getMapAnimation();
            animation.generateModifiedTilesets();
            tilesetLayoutPanelModified.setMapAnimation(animation);
            tilesetLayoutPanelModified.setSelectedTileset(animation.getFrames()[e.getFirstRow()].getDestTileset());
        } else if (e.getColumn() == 4) {
            //Editing destination index
            MapAnimation animation = tilesetLayoutPanelModified.getMapAnimation();
            int frame = e.getFirstRow();
            animation.generateModifiedTileset(frame);
            tilesetLayoutPanelModified.setMapAnimation(animation);
            tilesetLayoutPanelModified.setSelectedTileset(animation.getFrames()[frame].getDestTileset());
        } else if (e.getColumn() < 3) {
            //Editing the start or length of the frame (affects both panels)
            tilesetLayoutPanelAnim.redraw();
        }
        tilesetLayoutPanelModified.redraw();
    }
    
    private void onAnimationUpdated(LayoutAnimator.AnimationListener.AnimationFrameEvent e) {
        mapLayoutPanel.getMapLayout().getBlockset().clearIndexedColorImage(true);
        mapLayoutPanel.redraw();
        tableAnimFrames.jTable.setRowSelectionInterval(e.getCurrentFrame(), e.getCurrentFrame());
    }
    
    private void OnTableSelectionChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        mapLayoutPanel.setSelectedItemIndex(((ListSelectionModel)e.getSource()).getLeadSelectionIndex());
    }
    
    /**
     * To create a new Main Editor, copy the below code
     * Don't forget to change the new main class (below)
     */
    public static void main(String args[]) {
        AbstractMainEditor.programSetup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MapEditorMainEditor().setVisible(true);  // <------ Change this class to new Main Editor class
            }
        });
    }
    /**
     * To create a new Main Editor, copy the above code
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.sfc.sf2.map.models.MapCopyEventTableModel MapCopyEventTableModel;
    private com.sfc.sf2.core.gui.controls.AccordionPanel accordionPanel1;
    private com.sfc.sf2.core.gui.controls.AccordionPanel accordionPanel2;
    private javax.swing.ButtonGroup buttonGroupMapActions;
    private com.sfc.sf2.core.gui.controls.ColorPicker colorPicker1;
    private com.sfc.sf2.core.gui.controls.ColorPicker colorPickerBlockset;
    private com.sfc.sf2.core.gui.controls.ColorPicker colorPickerTileset;
    private com.sfc.sf2.core.gui.controls.Console console1;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton1;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton10;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton11;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton12;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton13;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton14;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton15;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton16;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton17;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton18;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton19;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton20;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton3;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton4;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton5;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton6;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton7;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton8;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton9;
    private com.formdev.flatlaf.icons.FlatOptionPaneWarningIcon flatOptionPaneWarningIcon1;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton1;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton10;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton11;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton12;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton13;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton14;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton15;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton16;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton17;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton2;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton3;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton4;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton5;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton6;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton7;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton8;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton9;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButtonSharedAnimation;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButtonSharedBlocks;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox15;
    private javax.swing.JCheckBox jCheckBox16;
    private javax.swing.JCheckBox jCheckBox17;
    private javax.swing.JCheckBox jCheckBox18;
    private javax.swing.JCheckBox jCheckBox19;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox20;
    private javax.swing.JCheckBox jCheckBox21;
    private javax.swing.JCheckBox jCheckBox22;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox9;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton11;
    private javax.swing.JRadioButton jRadioButton12;
    private javax.swing.JRadioButton jRadioButton13;
    private javax.swing.JRadioButton jRadioButton14;
    private javax.swing.JRadioButton jRadioButton15;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JSpinner jSpinner6;
    private javax.swing.JSpinner jSpinner7;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTabbedPane jTabbedPane5;
    private com.sfc.sf2.map.animation.models.MapAnimationFrameTableModel mapAnimationFrameTableModel;
    private com.sfc.sf2.map.models.MapAreaTableModel mapAreaTableModel;
    private com.sfc.sf2.map.block.gui.MapBlocksetLayoutPanel mapBlocksetLayoutPanel;
    private com.sfc.sf2.map.models.MapItemTableModel mapChestItemTableModel;
    private com.sfc.sf2.map.models.MapFlagCopyEventTableModel mapFlagCopyTableModel;
    private com.sfc.sf2.map.gui.MapLayoutPanel mapLayoutPanel;
    private com.sfc.sf2.map.models.MapItemTableModel mapOtherItemTableModel;
    private com.sfc.sf2.map.models.MapCopyEventTableModel mapStepCopyTableModel;
    private com.sfc.sf2.map.models.MapWarpTableModel mapWarpTableModel;
    private com.sfc.sf2.core.gui.controls.Table tableAnimFrames;
    private com.sfc.sf2.core.gui.controls.Table tableAreas;
    private com.sfc.sf2.core.gui.controls.Table tableChestItems;
    private com.sfc.sf2.core.gui.controls.Table tableFlagCopies;
    private com.sfc.sf2.core.gui.controls.Table tableOtherItems;
    private com.sfc.sf2.core.gui.controls.Table tableRoofCopies;
    private com.sfc.sf2.core.gui.controls.Table tableStepCopies;
    private com.sfc.sf2.core.gui.controls.Table tableWarps;
    private com.sfc.sf2.map.animation.gui.MapAnimationTilesetLayoutPanel tilesetLayoutPanelAnim;
    private com.sfc.sf2.map.animation.gui.MapModifiedTilesetLayoutPanel tilesetLayoutPanelModified;
    // End of variables declaration//GEN-END:variables
}
