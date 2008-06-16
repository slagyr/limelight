//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.painting;

import limelight.ui.*;
import limelight.ui.model.*;
import limelight.LimelightException;

import javax.swing.*;
import java.awt.*;

public class RadioButtonPainter extends Painter
{
  private JRadioButton radioButton;

  public RadioButtonPainter(limelight.ui.model.Panel panel)
  {
    super(panel);
    panel.add(buildTextBox());
    panel.sterilize();
    panel.setLayout(new InputLayout());
    panel.setTextAccessor(new TextAccessor() {
      public void setText(String text) throws LimelightException
      {
        radioButton.setText(text);
      }

      public String getText()
      {
        return radioButton.getText();
      }
    });
  }

  private JRadioButton buildTextBox()
  {
    radioButton = new JRadioButton();
    PropEventListener listener = new PropEventListener(panel.getProp());
    radioButton.addKeyListener(listener);
    radioButton.addMouseListener(listener);
    radioButton.addActionListener(listener);
    radioButton.addFocusListener(listener);
    radioButton.addChangeListener(listener);
    return radioButton;
  }

  public void paint(Graphics2D graphics)
  {
  }

  public JRadioButton getRadioButton()
  {
    return radioButton;
  }
}
