package limelight.ui.model.inputs;

import limelight.ui.model.TextAccessor;

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

  protected TextAccessor createTextAccessor()
  {
    return new TextBoxTextAccessor(textBox);
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

  private static class TextBoxTextAccessor implements TextAccessor
  {
    private TextBox textBox;

    public TextBoxTextAccessor(TextBox textBox)
    {
      this.textBox = textBox;
    }

    public void setText(String text)
    {
      textBox.setText(text);
    }

    public String getText()
    {
      return textBox.getText();
    }
  }
}
