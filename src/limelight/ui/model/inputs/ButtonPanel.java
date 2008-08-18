package limelight.ui.model.inputs;

import limelight.ui.*;
import limelight.ui.Panel;
import limelight.ui.model.PropPanel;
import limelight.ui.model.TextAccessor;

import java.awt.*;


public class ButtonPanel extends InputPanel
{
  private Button button;

  public ButtonPanel()
  {
    super();
    setSize(100, button.getPreferredSize().height);
  }

  protected Component createComponent()
  {
    return button = new Button(this);
  }

  protected TextAccessor createTextAccessor()
  {
    return new ButtonTextAccessor(button);
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
