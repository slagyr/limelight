//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.painting;

import limelight.ui.*;
import limelight.ui.model.*;
import limelight.LimelightException;

import javax.swing.*;
import java.awt.*;

public class ButtonPainter extends Painter
{
  private JButton button;

  public ButtonPainter(limelight.ui.model.Panel panel)
  {
    super(panel);
    panel.add(buildButton());
    panel.sterilize();
    panel.setLayout(new InputLayout());
    panel.setTextAccessor(new TextAccessor() {

      public void setText(String text) throws LimelightException
      {
        button.setText(text);
      }

      public String getText()
      {
        return button.getText();
      }
    });
  }

  private JButton buildButton()
  {
    button = new JButton();
    PropEventListener listener = new PropEventListener(panel.getProp());
    button.addKeyListener(listener);
    button.addMouseListener(listener);
    button.addActionListener(listener);
    button.addFocusListener(listener);
    button.addChangeListener(listener);
    return button;
  }

  public void paint(Graphics2D graphics)
  {
  }

  public JButton getButton()
  {
    return button;
  }
}
