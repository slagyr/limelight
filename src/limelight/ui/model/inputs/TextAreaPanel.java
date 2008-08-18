package limelight.ui.model.inputs;

import javax.swing.*;
import java.awt.*;

public class TextAreaPanel extends InputPanel
{
  private TextArea textArea;

  public TextAreaPanel()
  {
    super();
    setSize(200, 88);
  }

  protected Component createComponent()
  {
    textArea = new TextArea(this);
    return textArea;
  }

  public TextArea getTextArea()
  {
    return textArea;
  }

  public String getText()
  {
    return textArea.getText();
  }

  public void setText(String value)
  {
    textArea.setText(value);
  }

  public boolean canBeBuffered()
  {
    return false;
  }
}
