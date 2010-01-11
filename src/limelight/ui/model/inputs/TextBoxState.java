package limelight.ui.model.inputs;

import limelight.ui.model.TextPanel;

import java.awt.*;
import java.awt.font.TextLayout;


public class TextBoxState
{
  public static final int LEFT_TEXT_MARGIN = 3;

  StringBuffer text = new StringBuffer();
  TextLayout textLayout;
  int cursorX;
  boolean selectionOn;
  int selectionStartX;
  int cursorIndex;
  int selectionIndex;
  int textX;
  int xOffset;
  TextBox2Panel myBox;

  public TextBoxState(TextBox2Panel myBox)
  {
    this.myBox = myBox;
    cursorX = LEFT_TEXT_MARGIN;
    selectionOn = false;
    selectionStartX = 0;
    cursorIndex = 0;
    selectionIndex = 0;
    textX = LEFT_TEXT_MARGIN;
    xOffset = 0;
  }

  public void shiftCursorAndTextRight()
  {
    String rightShiftingText = text.substring(cursorIndex / 2, cursorIndex);
    if (rightShiftingText.length() == 0)
    {
      cursorX = LEFT_TEXT_MARGIN;
      xOffset = 0;
    }
    else
    {
      TextLayout layout = new TextLayout(rightShiftingText, myBox.font, TextPanel.staticFontRenderingContext);
      xOffset -= getWidthDimension(layout);
      if (xOffset < 0)
        xOffset = 0;
    }
  }

  public void setCursorAndSelectionStartX()
  {
    cursorX = getXPosFromIndex(cursorIndex);
    selectionStartX = getXPosFromIndex(selectionIndex);
  }

  public int getXPosFromIndex(int index)
  {
    String toIndexString = text.substring(0, index);
    if (index <= 0)
      return LEFT_TEXT_MARGIN;
    else
      return getXPosFromTextLayout(toIndexString) + getTerminatingSpaceWidth(toIndexString);
  }

  ;

  public int getXPosFromTextLayout(String toIndexString)
  {
    TextLayout layout = new TextLayout(toIndexString, myBox.font, TextPanel.staticFontRenderingContext);
    return getWidthDimension(layout) + LEFT_TEXT_MARGIN - xOffset;
  }

  public int getTerminatingSpaceWidth(String toIndexString)
  {
    int totalSpaceWidth = 0;
    if (toIndexString.charAt(toIndexString.length() - 1) == ' ')
    {
      int i = toIndexString.length() - 1;

      while (toIndexString.charAt(i) == ' ' && i > 0)
      {
        totalSpaceWidth += 3;
        i--;
      }
    }
    return totalSpaceWidth;
  }

  public Dimension calculateTextDimensions()
  {
    if (text != null && text.length() > 0)
    {
      textLayout = new TextLayout(text.toString(), myBox.font, TextPanel.staticFontRenderingContext);
      int height = getHeightDimension(textLayout);
      int width = getWidthDimension(textLayout);
      return new Dimension(width, height);
    }
    return null;
  }

  public int getHeightDimension(TextLayout layout)
  {
    return (int) ((layout.getAscent() + layout.getDescent() + layout.getLeading()) + 0.5);

  }

  public int getWidthDimension(TextLayout layout)
  {
    return (int) (layout.getBounds().getWidth() + layout.getBounds().getX() + 0.5);

  }

  public void setText(String text)
  {
    this.text = new StringBuffer(text);
    cursorIndex = text.length();
  }

  public String getText()
  {
    if (text == null)
      return null;
    return text.toString();
  }

  public void markAsDirty()
  {
    myBox.markAsDirty();
  }


}
