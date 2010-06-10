//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.model.PropablePanel;
import limelight.ui.model.TextAccessor;
import limelight.styles.Style;

import java.awt.*;

public class RadioButtonPanel extends AwtInputPanel
{
  private RadioButton radioButton;

  protected Component createComponent()
  {
    return radioButton = new RadioButton(this);
  }

  protected TextAccessor createTextAccessor()
  {
    return new RadioButtonTextAccessor(radioButton);
  }

  protected void setDefaultStyles(Style style)
  {
    style.setDefault(Style.WIDTH, "" + radioButton.getPreferredSize().width);
    style.setDefault(Style.HEIGHT, "" + radioButton.getPreferredSize().height);
  }

  public RadioButton getRadioButton()
  {
    return radioButton;
  }

  private static class RadioButtonTextAccessor implements TextAccessor
  {
    private final RadioButton button;

    public RadioButtonTextAccessor(RadioButton button)
    {
      this.button = button;
    }

    public void setText(PropablePanel panel, String text)
    {
      button.setText(text);
    }

    public String getText()
    {
      return button.getText();
    }

    public void markAsDirty()
    {
      button.getRadioButtonPanel().markAsDirty();
    }

    public void markAsNeedingLayout()
    {
      button.getRadioButtonPanel().markAsNeedingLayout();
    }

    public boolean hasFocus()
    {
      return button.hasFocus();
    }
  }
}
