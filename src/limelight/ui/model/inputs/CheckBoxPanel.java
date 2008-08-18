package limelight.ui.model.inputs;

import java.awt.*;

public class CheckBoxPanel extends InputPanel
{
  private CheckBox checkBox;

  public CheckBoxPanel()
  {
    super();
    setSize(checkBox.getPreferredSize().width, checkBox.getPreferredSize().height);
  }

  protected Component createComponent()
  {
    return checkBox = new CheckBox(this);
  }

  public CheckBox getCheckBox()
  {
    return checkBox;
  }

  public boolean canBeBuffered()
  {
    return false;
  }
}
