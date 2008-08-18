package limelight.ui.model.inputs;

import limelight.ui.model.PropPanel;
import limelight.ui.model.TextAccessor;

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

  protected TextAccessor createTextAccessor()
  {
    return new TextAreaTextAccessor(textArea);
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

  private static class TextAreaTextAccessor implements TextAccessor
  {
    private TextArea textArea;

    public TextAreaTextAccessor(TextArea textArea)
    {
      this.textArea = textArea;
    }

    public void setText(String text)
    {
      textArea.setText(text);
    }

    public String getText()
    {
      return textArea.getText();
    }
  }
}
