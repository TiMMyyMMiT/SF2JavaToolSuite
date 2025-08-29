/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.models.spinner;

import com.formdev.flatlaf.ui.FlatArrowButton;
import com.formdev.flatlaf.ui.FlatSpinnerUI;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;

/**
 *
 * @author TiMMy
 */
public class LeftRightSpinnerUI extends FlatSpinnerUI {

  public static ComponentUI createUI(JComponent c) {
    return new LeftRightSpinnerUI();
  }

  @Override
  protected Component createNextButton() {
    Component c = createArrowButton(SwingConstants.NORTH);
    c.setName("Spinner.nextButton");
    installNextButtonListeners(c);
    return c;
  }

  @Override
  protected Component createPreviousButton() {
    Component c = createArrowButton(SwingConstants.SOUTH);
    c.setName("Spinner.previousButton");
    installPreviousButtonListeners(c);
    return c;
  }

  // copied from BasicSpinnerUI
  private Component createArrowButton(int direction) {
    JButton b = new FlatArrowButton(direction, "Spinner", getForeground(true), getForeground(false), buttonHoverArrowColor, focusedBackground, buttonPressedArrowColor, focusedBackground);
    return b;
  }

  @Override
  public void installUI(JComponent c) {
    super.installUI(c);
    c.removeAll();
    GridBagLayout layout = new GridBagLayout();
    c.setLayout(layout);
    c.add(createEditor(), new GridBagConstraints(1, 0, 1, 1, 0.5, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    c.add(createNextButton(), new GridBagConstraints(2, 0, 1, 1, 0.1, 1, GridBagConstraints.LINE_END, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 5), 0, 0));
    c.add(createPreviousButton(), new GridBagConstraints(0, 0, 1, 1, 0.1, 1, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, new Insets(0, 5, 0, 0), 0, 0));
  }
}