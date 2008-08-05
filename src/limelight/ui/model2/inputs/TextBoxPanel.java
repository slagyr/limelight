package limelight.ui.model2.inputs;

import java.awt.*;
import java.util.Set;

public class TextBoxPanel extends InputPanel
{
  private TextBox textBox;

  public TextBoxPanel()
  {
    super();
    textBox.setSize(100, textBox.getPreferredSize().height);
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
}
