package limelight.ui.model.inputs;

import limelight.styles.styling.SimpleHorizontalAlignmentAttribute;
import limelight.styles.styling.SimpleVerticalAlignmentAttribute;
import limelight.ui.TextLayoutImpl;
import limelight.ui.TypedLayout;
import limelight.ui.model.TextPanel;
import limelight.util.Box;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.font.TextLayout;
import java.io.IOException;

public abstract class TextModel implements ClipboardOwner
{

  public static final int LEFT_TEXT_MARGIN = 3;
  public static final int SIDE_DETECTION_MARGIN = 4;
  public static final int TOP_MARGIN = 4;

  public StringBuffer text = new StringBuffer();
  TypedLayout textLayout;
  int cursorX;
  public boolean selectionOn;
  int selectionStartX;
  protected int cursorIndex;
  protected int selectionIndex;
  public Font font;
  int xOffset;
  TextInputPanel myBox;

  public TextModel()
  {

  }

  public void calculateTextXOffset(int panelWidth, int textWidth)
  {
    if (textWidth >= panelWidth)
    {
      cursorX = getXPosFromIndex(cursorIndex);
      int newXOffset = textWidth - panelWidth + SIDE_DETECTION_MARGIN + LEFT_TEXT_MARGIN;
      if (!typingInCenterOfBox(panelWidth, newXOffset))
        xOffset = newXOffset;
      if (cursorX < SIDE_DETECTION_MARGIN)
        shiftTextRight();
    }
    else
      xOffset = 0;
  }

  private boolean typingInCenterOfBox(int panelWidth, int newXOffset)
  {
    return (isLeftOfTheRightMargin(panelWidth) && newXOffset > xOffset);
  }

  private boolean isLeftOfTheRightMargin(int panelWidth)
  {
    return cursorX < panelWidth - SIDE_DETECTION_MARGIN;
  }


  public void shiftTextRight()
  {
    String rightShiftingText = text.substring(cursorIndex / 2, cursorIndex);
    if (rightShiftingText.length() == 0 || xOffset == 0)
    {
      cursorX = LEFT_TEXT_MARGIN;
      xOffset = 0;
    }
    else
    {
      TypedLayout layout = new TextLayoutImpl(rightShiftingText, font, TextPanel.getRenderContext());
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


  public int getXPosFromTextLayout(String toIndexString)
  {
    TypedLayout layout = new TextLayoutImpl(toIndexString, font, TextPanel.getRenderContext());
    return getWidthDimension(layout) + LEFT_TEXT_MARGIN - xOffset;
  }

  public int getTerminatingSpaceWidth(String string)
  {
    int totalSpaceWidth = 0;
    if (string.charAt(string.length() - 1) == ' ')
    {
      int i = string.length() - 1;

      while (i > 0 && string.charAt(i) == ' ')
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
      TypedLayout textLayout = getTextLayout();
      int height = getHeightDimension(textLayout);
      int width = getWidthDimension(textLayout);
      return new Dimension(width, height);
    }
    return null;
  }

  public int getHeightDimension(TypedLayout layout)
  {
    return (int) ((layout.getAscent() + layout.getDescent() + layout.getLeading()) + 0.5);

  }

  public int getWidthDimension(TypedLayout layout)
  {
    return (int) (layout.getBounds().getWidth() + layout.getBounds().getX() + 0.5);

  }

  public int getXOffset()
  {
    return xOffset;
  }

  public TypedLayout getTextLayout()
  {
    if (text.length() == 0)
      return null;
    else
    {
      if (textLayout == null || !textLayout.toString().equals(text.toString()))
        textLayout = new TextLayoutImpl(text.toString(), font, TextPanel.getRenderContext());
    }
    return textLayout;
  }

  public void setText(String text)
  {
    this.text = new StringBuffer(text);
    setCursorIndex(text.length());
  }

  public String getText()
  {
    if (text == null)
      return null;
    return text.toString();
  }

  public Point getPanelAbsoluteLocation()
  {
    return myBox.getAbsoluteLocation();
  }

  public int getPanelWidth()
  {
    return myBox.getWidth();
  }

  public int getPanelHeight()
  {
    return myBox.getHeight();
  }

  public SimpleHorizontalAlignmentAttribute getHorizontalAlignment()
  {
    return myBox.horizontalTextAlignment;
  }

  public SimpleVerticalAlignmentAttribute getVerticalAlignment()
  {
    return myBox.verticalTextAlignment;
  }

  public boolean isCursorOn()
  {
    return myBox.isCursorOn();
  }

  public boolean isFocused()
  {
    return myBox.isFocused();
  }

  public void copyText(String clipboard)
  {
    StringSelection stringSelection = new StringSelection(clipboard);
    Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    systemClipboard.setContents(stringSelection, this);
  }

  public String getClipboardContents()
  {
    String clipboardString = "";
    Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable contents = systemClipboard.getContents(null);
    boolean hasText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
    if (hasText)
    {
      try
      {
        clipboardString = (String) contents.getTransferData(DataFlavor.stringFlavor);
      }
      catch (UnsupportedFlavorException e)
      {
        e.printStackTrace();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    return clipboardString;
  }

  public void copySelection()
  {
    String clipboard;
    if (selectionIndex < cursorIndex)
      clipboard = text.substring(selectionIndex, cursorIndex);
    else
      clipboard = text.substring(cursorIndex, selectionIndex);
    copyText(clipboard);
  }

  public void pasteClipboard()
  {
    String clipboard = getClipboardContents();
    if (clipboard != null && clipboard.length() > 0)
    {
      text.insert(cursorIndex, clipboard);
      setCursorIndex(cursorIndex + clipboard.length());
    }
  }

  public void cutSelection()
  {
    copySelection();
    deleteSelection();
  }

  public void deleteSelection()
  {
    if (selectionIndex < cursorIndex)
      deleteEnclosedText(selectionIndex, cursorIndex);
    else
      deleteEnclosedText(cursorIndex, selectionIndex);
  }

  public void deleteEnclosedText(int first, int second)
  {
    text.delete(first, second);
    setCursorIndex(first);
    setSelectionIndex(0);
  }

  public Rectangle getSelectionRegion()
  {
    int x1 = getXPosFromIndex(cursorIndex);
    int x2 = getXPosFromIndex(selectionIndex);

    if (x1 > x2)
      return new Box(x2, TOP_MARGIN, x1 - x2, getPanelHeight() - TOP_MARGIN * 2);
    else
      return new Box(x1, TOP_MARGIN, x2 - x1, getPanelHeight() - TOP_MARGIN * 2);

  }

  public int findWordsRightEdge(int index)
  {
    for (int i = index; i <= text.length() - 1; i++)
    {
      if (i == 0)
        i = 1;
      if (text.charAt(i - 1) != ' ' && text.charAt(i) == ' ')
        return i;
    }
    return text.length();
  }

  public int findWordsLeftEdge(int index)
  {
    for (int i = index; i > 1; i--)
    {
      if (text.charAt(i - 1) == ' ' && text.charAt(i) != ' ')
        return i;
    }
    return 0;
  }

  public int getCursorIndex()
  {
    return cursorIndex;
  }

  public void setCursorIndex(int cursorIndex)
  {
    this.cursorIndex = cursorIndex;
    myBox.setPaintableRegion(cursorIndex);
  }

  public int getSelectionIndex()
  {
    return selectionIndex;
  }

  public void setSelectionIndex(int selectionIndex)
  {
    this.selectionIndex = selectionIndex;
    myBox.setPaintableRegion(selectionIndex);
  }
}
