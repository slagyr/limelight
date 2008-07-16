//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.painting;

import limelight.ui.model.PropEventListener;
import limelight.ui.model.InputLayout;
import limelight.ui.Painter;

import javax.swing.*;
import java.awt.*;

public class CheckBoxPainter extends Painter
{
  private JCheckBox checkBox;

  public CheckBoxPainter(limelight.ui.model.Panel panel)
  {
    super(panel);
    panel.add(buildTextBox());
    panel.sterilize();
    panel.setLayout(new InputLayout());
  }

  private JCheckBox buildTextBox()
  {
    checkBox = new JCheckBox();
    PropEventListener listener = new PropEventListener(panel.getProp());
    checkBox.addKeyListener(listener);
    checkBox.addMouseListener(listener);
    checkBox.addActionListener(listener);
    checkBox.addFocusListener(listener);
    checkBox.addChangeListener(listener);
    return checkBox;
  }

  public void paint(Graphics2D graphics)
  {
  }

  public JCheckBox getCheckBox()
  {
    return checkBox;
  }
}
