package limelight.ui.model2.painting;

import limelight.ui.Painter;
import limelight.ui.model2.PropPanel;
import limelight.ui.model2.TextAccessor;
import limelight.ui.model2.inputs.ComboBoxPanel;

import javax.swing.*;
import java.awt.*;

public class ComboBoxPainter extends Painter
{
  ComboBoxPanel comboBoxPanel;

   public ComboBoxPainter(PropPanel panel)
  {
    super(panel);
    comboBoxPanel = new ComboBoxPanel();
    panel.add(comboBoxPanel);
    panel.sterilize();
    panel.setTextAccessor(new TextAccessor(){
      public void setText(String text)
      {
        comboBoxPanel.getComboBox().setSelectedItem(text);
      }

      public String getText()
      {
        return comboBoxPanel.getComboBox().getSelectedItem() + "";
      }
    });
  }

  public void paint(Graphics2D graphics)
  {
  }

  public JComboBox getComboBox()
  {
    return comboBoxPanel.getComboBox();
  }
}
