package limelight.ui.model.inputs;

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

  public Button getButton()
  {
    return button;
  }

  public boolean canBeBuffered()
  {
    return false;
  }
}
