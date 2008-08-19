package limelight.ui.model.inputs;

import limelight.ui.*;
import limelight.ui.Panel;
import limelight.ui.model.PropPanel;
import limelight.ui.model.TextAccessor;
import limelight.styles.Style;

import java.awt.*;


public class ButtonPanel extends InputPanel
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
    style.setDefault(Style.HEIGHT, "" + button.getPreferredSize().height);
  }

  public Button getButton()
  {
    return button;
  }

  private static class ButtonTextAccessor implements TextAccessor
  {
    private Button button;

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
