package limelight;

import javax.swing.*;
import java.awt.*;

public class CheckBoxPainter extends Painter
{
  private JCheckBox checkBox;

  public CheckBoxPainter(Panel panel)
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
