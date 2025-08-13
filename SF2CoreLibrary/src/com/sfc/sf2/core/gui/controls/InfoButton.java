/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.controls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanProperty;
import javax.swing.JOptionPane;

/**
 *
 * @author TiMMy
 */
public class InfoButton extends javax.swing.JButton {
    
    private final com.formdev.flatlaf.icons.FlatHelpButtonIcon flatHelpButtonIcon1;
    private String messageText;
    
    public InfoButton() {
        flatHelpButtonIcon1 = new com.formdev.flatlaf.icons.FlatHelpButtonIcon();
        setMaximumSize(new java.awt.Dimension(26, 26));
        setMinimumSize(new java.awt.Dimension(26, 26));
        setPreferredSize(new java.awt.Dimension(26, 26));
        setText(null);
        setIcon(flatHelpButtonIcon1);
        setMargin(new java.awt.Insets(1, 1, 1, 1));
        setBorderPainted(false);
        setContentAreaFilled(false); 
        setFocusPainted(false); 
        setOpaque(false);
        this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonPressed(e);
            }
        });
    }
    
    public String getMessageText() {
        return messageText;
    }
    
    @BeanProperty(preferred = true, visualUpdate = true, description = "The title to show on the Color Chooser popup.")
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
    
    private void buttonPressed(ActionEvent evt) {
        if (messageText != null) {
            JOptionPane.showMessageDialog(null, messageText, "Help", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
