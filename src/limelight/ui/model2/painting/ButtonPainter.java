package limelight.ui.model2.painting;

import limelight.ui.model2.PropPanel;
import limelight.ui.model2.TextAccessor;
import limelight.ui.model2.inputs.ButtonPanel;
import limelight.ui.model2.inputs.Button;
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
