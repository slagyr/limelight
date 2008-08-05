package limelight.ui.model2.inputs;

import java.awt.*;

public class RadioButtonPanel extends InputPanel
{
  private RadioButton radioButton;

  public RadioButtonPanel()
  {
    super();
    radioButton.setSize(radioButton.getPreferredSize().width, radioButton.getPreferredSize().height);
  }

  protected Component createComponent()
  {
    return radioButton = new RadioButton(this);
  }

  public RadioButton getRadioButton()
  {
    return radioButton;
  }
}
