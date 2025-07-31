/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette.gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author wiz
 */
public class ColorEditor extends JPanel {
  DrawingCanvas canvas = new DrawingCanvas();
  JLabel rgbValue = new JLabel("000000");

  JSlider sliderR, sliderG, sliderB;
  
  ColorPane colorPane;

  public ColorEditor() {
    sliderR = getSlider(0, 14, 0, 2, 2);
    sliderG = getSlider(0, 14, 0, 2, 2);
    sliderB = getSlider(0, 14, 0, 2, 2);

    add(canvas);
    
    JPanel sliderPanel = new JPanel();
    sliderPanel.setLayout(new GridLayout(3, 1));

    sliderPanel.add(sliderR);
    sliderPanel.add(sliderG);
    sliderPanel.add(sliderB);

    add(sliderPanel);


    
  }
  
  public void setColorPane(ColorPane cp){
      colorPane = cp;
      canvas.redValue = colorPane.getCurrentColor().getRed();
      canvas.greenValue = colorPane.getCurrentColor().getGreen();
      canvas.blueValue = colorPane.getCurrentColor().getBlue();
      sliderR.setValue(canvas.redValue/16);
      sliderG.setValue(canvas.greenValue/16);
      sliderB.setValue(canvas.blueValue/16);  
      canvas.setBackground(colorPane.getCurrentColor());
  }

  public JSlider getSlider(int min, int max, int init, int mjrTkSp, int mnrTkSp) {
    JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, init);
    slider.setPaintTicks(true);
    slider.setMajorTickSpacing(mjrTkSp);
    slider.setMinorTickSpacing(mnrTkSp);
    slider.setPaintLabels(true);
    slider.setSnapToTicks(true);
    slider.addChangeListener(new SliderListener());
    return slider;
  }

  class DrawingCanvas extends Canvas {
    Color color;
    int redValue, greenValue, blueValue;
    int alphaValue = 255;
    float[] hsbValues = new float[3];

    public DrawingCanvas() {
      setSize(100, 100);
      color = new Color(0, 0, 0);
      setBackgroundColor();
    }

    public void setBackgroundColor() {
      color = new Color(redValue, greenValue, blueValue, alphaValue);
      setBackground(color);
    }
  }

  class SliderListener implements ChangeListener {
    public void stateChanged(ChangeEvent e) {
      JSlider slider = (JSlider) e.getSource();

      if (slider == sliderR) {
        canvas.redValue = slider.getValue()*16;
        colorPane.updateColor(new Color(canvas.redValue,canvas.greenValue,canvas.blueValue));
        displayRGBColor();
      } else if (slider == sliderG) {
        canvas.greenValue = slider.getValue()*16;
        colorPane.updateColor(new Color(canvas.redValue,canvas.greenValue,canvas.blueValue));
        displayRGBColor();
      } else if (slider == sliderB) {
        canvas.blueValue = slider.getValue()*16;
        colorPane.updateColor(new Color(canvas.redValue,canvas.greenValue,canvas.blueValue));
        displayRGBColor();
      }
      canvas.repaint();
    }

    public void displayRGBColor() {
      canvas.setBackgroundColor();

      rgbValue.setText(Integer.toString(canvas.color.getRGB() & 0xffffff, 16));
    }

    public void displayHSBColor() {
      canvas.redValue = canvas.color.getRed();
      canvas.greenValue = canvas.color.getGreen();
      canvas.blueValue = canvas.color.getBlue();

      sliderR.setValue(canvas.redValue);
      sliderG.setValue(canvas.greenValue);
      sliderB.setValue(canvas.blueValue);

      canvas.color = new Color(canvas.redValue, canvas.greenValue,
          canvas.blueValue);
      canvas.setBackground(canvas.color);
    }
  }
}

