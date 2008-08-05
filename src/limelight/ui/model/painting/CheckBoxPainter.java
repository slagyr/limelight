package limelight.ui.model.painting;

import limelight.ui.*;
import limelight.ui.model.PropPanel;
import limelight.ui.model.inputs.CheckBoxPanel;

import javax.swing.*;
import java.awt.*;

public class CheckBoxPainter extends Painter
{
  private CheckBoxPanel checkBoxPanel;

  public CheckBoxPainter(PropPanel panel)
  {
    super(panel);
    checkBoxPanel = new CheckBoxPanel();
    panel.add(checkBoxPanel);
    panel.sterilize();
//    panel.setLayout(new InputLayout());
//    panel.setTextAccessor(new TextAccessor() {
//
//      public void setText(String text)
//      {
//        textField.setText(text);
//      }
//
//      public String getText()
//      {
//        return textField.getText();
//      }
//    });
  }
  
  public void paint(Graphics2D graphics)
  {
  }

  public JCheckBox getCheckBox()
  {
    return checkBoxPanel.getCheckBox();
  }
}
