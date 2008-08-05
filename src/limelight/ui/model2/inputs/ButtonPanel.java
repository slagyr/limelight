package limelight.ui.model2.inputs;

import java.awt.*;


public class ButtonPanel extends InputPanel
{
  private Button button;

  public ButtonPanel()
  {
    super();
    button.setSize(100, button.getPreferredSize().height);
  }

  protected Component createComponent()
  {
    return button = new Button(this);
  }

  public Button getButton()
  {
    return button;
  }
}
