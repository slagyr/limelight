package limelight.ui.model.inputs;

import java.awt.*;

public class TextAreaPanel extends InputPanel
{
  private TextArea textArea;

  public TextAreaPanel()
  {
    super();
    textArea.setSize(200, 88);
  }

  protected Component createComponent()
  {
    return textArea = new TextArea(this);
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

}
