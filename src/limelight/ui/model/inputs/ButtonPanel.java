//- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.model.TextAccessor;
import limelight.styles.Style;

import java.awt.*;


public class ButtonPanel extends AwtInputPanel
{
  private Button button;

  protected Component createComponent()
  {
    return button = new Button(this);
  }

  protected TextAccessor createTextAccessor()
  {
    return new ButtonTextAccessor(button);
  }

  protected void setDefaultStyles(Style style)
  {
    style.setDefault(Style.WIDTH, "100");
    int defaultHeight = button.getPreferredSize().height;
    if(defaultHeight < 16)    // Windows default prefered height is rediculously small
      defaultHeight = 24;
    style.setDefault(Style.HEIGHT, "" + defaultHeight);
  }

  public Button getButton()
  {
    return button;
  }

  private static class ButtonTextAccessor implements TextAccessor
  {
    private final Button button;

    public ButtonTextAccessor(Button button)
    {
      this.button = button;
    }

    public void setText(String text)
    {
      button.setText(text);
    }

    public String getText()
    {
      return button.getText();
    }
  }
}
