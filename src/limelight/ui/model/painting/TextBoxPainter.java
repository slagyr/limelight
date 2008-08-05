package limelight.ui.model.painting;

import limelight.ui.*;
import limelight.ui.model.PropPanel;
import limelight.ui.model.TextAccessor;
import limelight.ui.model.inputs.TextBoxPanel;
import limelight.ui.model.inputs.TextBox;

import java.awt.*;

public class TextBoxPainter extends Painter
{
  private TextBoxPanel textPanel;

  public TextBoxPainter(PropPanel panel)
  {
    super(panel);
    textPanel = new TextBoxPanel();
    panel.add(textPanel);
    panel.sterilize();
    panel.setTextAccessor(new TextAccessor() {

      public void setText(String text)
      {
        textPanel.getTextBox().setText(text);
      }

      public String getText()
      {
        return textPanel.getTextBox().getText();
      }
    });
  }

  public void paint(Graphics2D graphics)
  {
  }

  public TextBox getTextField()
  {
    return textPanel.getTextBox();
  }
}
