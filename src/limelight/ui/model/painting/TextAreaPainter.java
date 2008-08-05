package limelight.ui.model.painting;

import limelight.ui.*;
import limelight.ui.model.PropPanel;
import limelight.ui.model.TextAccessor;
import limelight.ui.model.inputs.TextAreaPanel;
import limelight.ui.model.inputs.TextArea;

import java.awt.*;

public class TextAreaPainter extends Painter
{
  private TextAreaPanel textPanel;

  public TextAreaPainter(PropPanel panel)
  {
    super(panel);
    textPanel = new TextAreaPanel();
    panel.add(textPanel);
    panel.sterilize();
    panel.setTextAccessor(new TextAccessor() {

      public void setText(String text)
      {
        textPanel.getTextArea().setText(text);
      }

      public String getText()
      {
        return textPanel.getTextArea().getText();
      }
    });
  }

  public void paint(Graphics2D graphics)
  {
  }

  public TextArea getTextField()
  {
    return textPanel.getTextArea();
  }
}
