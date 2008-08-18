package limelight.ui.model.inputs;

import java.awt.*;

public class TextBoxPanel extends InputPanel
{
  private TextBox textBox;

  public TextBoxPanel()
  {
    super();
    setSize(100, textBox.getPreferredSize().height);
  }

  protected Component createComponent()
  {
    return textBox = new TextBox(this);
  }

  public TextBox getTextBox()
  {
    return textBox;
  }

  public String getText()
  {
    return textBox.getText();
  }

  public void setText(String text)
  {
    textBox.setText(text);
  }

  public boolean canBeBuffered()
  {
    return false;
  }
}
