//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.model.TextAccessor;
import limelight.styles.Style;

import java.awt.*;
import java.awt.event.MouseEvent;

public class TextAreaPanel extends AwtInputPanel
{
  private TextArea textArea;

  protected Component createComponent()
  {
    textArea = new TextArea(this);
    return textArea;
  }

  protected TextAccessor createTextAccessor()
  {
    return new TextAreaTextAccessor(textArea);
  }

  protected void setDefaultStyles(Style style)
  {
    style.setDefault(Style.WIDTH, "200");
    style.setDefault(Style.HEIGHT, "88");
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
    private final TextArea textArea;

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

  public void mouseEntered(MouseEvent e)
  {
    getRoot().setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
  }

  public void mouseExited(MouseEvent e)
  {
    getRoot().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
  }
}
