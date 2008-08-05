package limelight.ui.model2.painting;

import limelight.ui.*;
import limelight.ui.model2.PropPanel;
import limelight.ui.model2.TextAccessor;
import limelight.ui.model2.inputs.TextAreaPanel;
import limelight.ui.model2.inputs.CheckBoxPanel;
import limelight.ui.model2.inputs.RadioButtonPanel;
import limelight.ui.model2.inputs.RadioButton;

import javax.swing.*;
import java.awt.*;

public class RadioButtonPainter extends Painter
{
  private RadioButtonPanel radioButtonPanel;

  public RadioButtonPainter(PropPanel panel)
  {
    super(panel);
    radioButtonPanel = new RadioButtonPanel();
    panel.add(radioButtonPanel);
    panel.sterilize();
    panel.setTextAccessor(new TextAccessor() {
      public void setText(String text)
      {
        radioButtonPanel.getRadioButton().setText(text);
      }

      public String getText()
      {
        return radioButtonPanel.getRadioButton().getText();
      }
    });
  }

  public void paint(Graphics2D graphics)
  {
  }

  public RadioButton getRadioButton()
  {
    return radioButtonPanel.getRadioButton();
  }
}
