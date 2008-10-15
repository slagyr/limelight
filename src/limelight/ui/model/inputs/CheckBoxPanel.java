//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.model.TextAccessor;
import limelight.styles.Style;

import java.awt.*;

public class CheckBoxPanel extends InputPanel
{
  private CheckBox checkBox;

  protected Component createComponent()
  {
    return checkBox = new CheckBox(this);
  }

  protected TextAccessor createTextAccessor()
  {
    return new CheckBoxTextAccessor(checkBox);
  }

  protected void setDefaultStyles(Style style)
  {
    style.setDefault(Style.WIDTH, "" + checkBox.getPreferredSize().width);
    style.setDefault(Style.HEIGHT, "" + checkBox.getPreferredSize().height);
  }

  public CheckBox getCheckBox()
  {
    return checkBox;
  }

  private static class CheckBoxTextAccessor implements TextAccessor
  {
    private final CheckBox checkBox;

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
