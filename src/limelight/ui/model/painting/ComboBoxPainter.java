//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.painting;

import limelight.ui.*;
import limelight.ui.model.*;
import limelight.LimelightException;

import javax.swing.*;
import java.awt.*;

public class ComboBoxPainter extends Painter
{
  private JComboBox comboBox;

  public ComboBoxPainter(limelight.ui.model.Panel panel)
  {
    super(panel);
    panel.add(buildComboBox());
    panel.sterilize();
    panel.setLayout(new InputLayout());
    panel.setTextAccessor(new TextAccessor() {

      public void setText(String text) throws LimelightException
      {
        comboBox.setSelectedItem(text);
      }

      public String getText()
      {
        return comboBox.getSelectedItem().toString();
      }
    });
  }

  private JComboBox buildComboBox()
  {
    comboBox = new JComboBox();
    PropEventListener listener = new PropEventListener(panel.getProp());
    comboBox.addKeyListener(listener);
    comboBox.addMouseListener(listener);
    comboBox.addFocusListener(listener);
    comboBox.addItemListener(listener);
    return comboBox;
  }

  public void paint(Graphics2D graphics)
  {
  }

  public JComboBox getComboBox()
  {
    return comboBox;
  }
}
