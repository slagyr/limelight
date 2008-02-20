package limelight.ui.painting;

import limelight.ui.*;
import limelight.LimelightException;

import javax.swing.*;
import java.awt.*;

public class ComboBoxPainter extends Painter
{
  private JComboBox comboBox;

  public ComboBoxPainter(limelight.ui.Panel panel)
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
    BlockEventListener listener = new BlockEventListener(panel.getBlock());
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
