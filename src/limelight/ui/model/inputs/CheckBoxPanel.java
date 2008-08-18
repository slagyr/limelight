package limelight.ui.model.inputs;

import limelight.ui.model.TextAccessor;

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

  protected TextAccessor createTextAccessor()
  {
    return new CheckBoxTextAccessor(checkBox);
  }

  public CheckBox getCheckBox()
  {
    return checkBox;
  }

  private static class CheckBoxTextAccessor implements TextAccessor
  {
    private CheckBox checkBox;

    public CheckBoxTextAccessor(CheckBox checkBox)
    {
      this.checkBox = checkBox;
    }

    public void setText(String text)
    {
      checkBox.setText(text);
    }

    public String getText()
    {
      return checkBox.getText();
    }
  }
}
