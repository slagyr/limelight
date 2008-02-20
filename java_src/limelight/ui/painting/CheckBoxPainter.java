package limelight.ui.painting;

import limelight.ui.BlockEventListener;
import limelight.ui.InputLayout;
import limelight.ui.Painter;

import javax.swing.*;
import java.awt.*;

public class CheckBoxPainter extends Painter
{
  private JCheckBox checkBox;

  public CheckBoxPainter(limelight.ui.Panel panel)
  {
    super(panel);
    panel.add(buildTextBox());
    panel.sterilize();
    panel.setLayout(new InputLayout());
  }

  private JCheckBox buildTextBox()
  {
    checkBox = new JCheckBox();
    BlockEventListener listener = new BlockEventListener(panel.getBlock());
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
