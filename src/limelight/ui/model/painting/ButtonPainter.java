package limelight.ui.model.painting;

import limelight.ui.model.PropPanel;
import limelight.ui.model.TextAccessor;
import limelight.ui.model.inputs.ButtonPanel;
import limelight.ui.model.inputs.Button;
import limelight.ui.Painter;

import java.awt.*;

public class ButtonPainter extends Painter
{
  private ButtonPanel buttonPanel;

  public ButtonPainter(PropPanel panel)
  {
    super(panel);
    buttonPanel = new ButtonPanel();
    panel.add(buttonPanel);
    panel.sterilize();

    panel.setTextAccessor(new TextAccessor() {

      public void setText(String text)
      {
        buttonPanel.getButton().setText(text);
      }

      public String getText()
      {
        return buttonPanel.getButton().getText();
      }
    });
  }

  public void paint(Graphics2D graphics)
  {
  }

  public Button getButton()
  {
    return buttonPanel.getButton();
  }
}
