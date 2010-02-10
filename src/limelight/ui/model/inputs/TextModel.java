package limelight.ui.model.inputs;

import limelight.styles.styling.SimpleHorizontalAlignmentAttribute;
import limelight.styles.styling.SimpleVerticalAlignmentAttribute;
import limelight.ui.TextLayoutImpl;
import limelight.ui.TypedLayout;
import limelight.ui.model.TextPanel;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.ArrayList;

public abstract class TextModel implements ClipboardOwner
{

  public static final int SIDE_TEXT_MARGIN = 3;
  public static final int SIDE_DETECTION_MARGIN = 4;
  public static final int TOP_MARGIN = 4;

  public StringBuffer text = new StringBuffer();
  ArrayList<TypedLayout> textLayouts;
  int cursorX;
  public boolean selectionOn;
  int selectionStartX;
  protected int cursorIndex;
  protected int selectionIndex;
  public Font font;
  int xOffset;
  TextInputPanel myPanel;
  private String lastLayedOutText;
  private int lastCursorIndex;
  private int spaceWidth;

  public TextModel()
  {

  }

  public void calculateTextXOffset(int panelWidth, int textWidth)
  {
    if (textWidth >= panelWidth)
    {
      cursorX = getXPosFromIndex(cursorIndex);
      int newXOffset = textWidth - panelWidth + SIDE_DETECTION_MARGIN + SIDE_TEXT_MARGIN;
      if (!typingInCenterOfBox(panelWidth, newXOffset))
        xOffset = newXOffset;
      while (cursorX < SIDE_DETECTION_MARGIN)
        shiftOffset();
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


  public abstract void shiftOffset();

  public void setCursorAndSelectionStartX()
  {
    cursorX = getXPosFromIndex(cursorIndex);
    selectionStartX = getXPosFromIndex(selectionIndex);
  }

  public int getXPosFromIndex(int index)
  {
    if (text == null || text.length() == 0)
      return SIDE_TEXT_MARGIN;
    int lineNumber = getLineNumberOfIndex(index);
    int startOfLineIndex = 0;
    for (int i = 0; i < lineNumber; i++)
      startOfLineIndex += textLayouts.get(i).getText().length();
    if (index <= 0 || isLastCharacterAReturn(index) || startOfLineIndex == index)
      return SIDE_TEXT_MARGIN;
    String toIndexString = text.substring(startOfLineIndex, index);
    return getXPosFromText(toIndexString) + getTerminatingSpaceWidth(toIndexString);
  }


  protected abstract int getXPosFromText(String toIndexString);

  public int getYPosFromIndex(int index)
  {
    int yPos = TOP_MARGIN;
    if (text == null || text.length() == 0)
      return yPos;
    int lineNumber = getLineNumberOfIndex(index);
    int layoutIndex;
    for (layoutIndex = 0; layoutIndex < lineNumber; layoutIndex++)
      yPos += getTotalHeightOfLineWithLeadingMargin(layoutIndex);
    if (isLastCharacterAReturn(index) && index == text.length())
    {
      yPos += getTotalHeightOfLineWithLeadingMargin(layoutIndex);
    }
    return yPos;
  }

  private boolean isLastCharacterAReturn(int index)
  {
    if (cursorIndex == 0)
      return false;
    return text.charAt(index - 1) == '\r' || text.charAt(index - 1) == '\n';
  }

  public int getTerminatingSpaceWidth(String string)
  {
    int trailingSpaces = 0;
    for (int i = string.length() - 1; i > 0 && string.charAt(i) == ' '; i--)
      trailingSpaces++;

    return trailingSpaces * spaceWidth();
  }

  public int spaceWidth()
  {
    if (spaceWidth == 0)
    {
      TypedLayout layout1 = new TextLayoutImpl("a a", font, TextPanel.getRenderContext());
      TypedLayout layout2 = new TextLayoutImpl("aa", font, TextPanel.getRenderContext());
      spaceWidth = getWidthDimension(layout1) - getWidthDimension(layout2);
    }
    return spaceWidth;
  }

  public abstract Dimension calculateTextDimensions();

  public float getHeightDimension(TypedLayout layout)
  {
    return (layout.getAscent() + layout.getDescent());

  }

  public int getTotalHeightOfLineWithLeadingMargin(int layoutIndex)
  {
    return (int) (getHeightDimension(textLayouts.get(layoutIndex)) + textLayouts.get(layoutIndex).getLeading() + .5);
  }

  public int getHeightOfCurrentLine()
  {
    int lineNumber = getLineNumberOfIndex(cursorIndex);
    return (int) getHeightDimension(textLayouts.get(lineNumber));
  }

  public int getWidthDimension(TypedLayout layout)
  {
    return (int) (layout.getBounds().getWidth() + layout.getBounds().getX() + 0.5);
  }

  public int getXOffset()
  {
    return xOffset;
  }


  public abstract ArrayList<TypedLayout> getTextLayouts();

  public Point getPanelAbsoluteLocation()
  {
    return myPanel.getAbsoluteLocation();
  }

  public int getPanelWidth()
  {
    return myPanel.getWidth();
  }

  public int getPanelHeight()
  {
    return myPanel.getHeight();
  }

  public Shape getPaintableRegion()
  {
    return myPanel.getPaintableRegion();
  }

  public SimpleHorizontalAlignmentAttribute getHorizontalAlignment()
  {
    return myPanel.horizontalTextAlignment;
  }

  public SimpleVerticalAlignmentAttribute getVerticalAlignment()
  {
    return myPanel.verticalTextAlignment;
  }

  public boolean isCursorOn()
  {
    return myPanel.isCursorOn();
  }

  public boolean isFocused()
  {
    return myPanel.isFocused();
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

  public abstract ArrayList<Rectangle> getSelectionRegions();

  public int findWordsRightEdge(int index)
  {
    for (int i = index; i <= text.length() - 1; i++)
    {
      if (i == 0)
        i = 1;
      if (isAtEndOfWord(i))
        return i;
    }
    return text.length();
  }

  public boolean isAtEndOfWord(int i)
  {
    return text.charAt(i - 1) != ' ' && text.charAt(i - 1) != '\n' && (text.charAt(i) == ' ' || text.charAt(i) == '\n');
  }

  public int findWordsLeftEdge(int index)
  {
    for (int i = index; i > 1; i--)
    {
      if (isAtStartOfWord(i))
        return i;
    }
    return 0;
  }

  public boolean isAtStartOfWord(int i)
  {
    return (text.charAt(i - 1) == ' ' || text.charAt(i - 1) == '\n') && (text.charAt(i) != ' ' && text.charAt(i) != '\n');
  }

  public int getCursorIndex()
  {
    return cursorIndex;
  }

  public void setCursorIndex(int cursorIndex)
  {
    lastCursorIndex = this.cursorIndex;
    this.cursorIndex = cursorIndex;
    myPanel.setPaintableRegion(cursorIndex);
  }

  public int getSelectionIndex()
  {
    return selectionIndex;
  }

  public void setSelectionIndex(int selectionIndex)
  {
    this.selectionIndex = selectionIndex;
    myPanel.setPaintableRegion(selectionIndex);
  }

  public abstract boolean isBoxFull();

  public void setText(String text)
  {
    if (text == null)
      this.text = null;
    else
    {
      this.text = new StringBuffer(text);
      setCursorIndex(text.length());
    }
  }

  public String getText()
  {
    if (text == null)
      return null;
    return text.toString();
  }

  public void setLastLayedOutText(String lastLayedOutText)
  {
    this.lastLayedOutText = lastLayedOutText;
  }

  public String getLastLayedOutText()
  {
    return lastLayedOutText;
  }

  public int getLastKeyPressed()
  {
    return myPanel.getLastKeyPressed();          
  }

  public void setLastKeyPressed(int keyCode)
  {
    myPanel.setLastKeyPressed(keyCode);
  }

  public int getLastCursorIndex()
  {
    return lastCursorIndex;
  }

  public void setLastCursorIndex(int index)
  {
    lastCursorIndex = index;
  }

  public boolean isThereSomeDifferentText()
  {
    return !text.toString().equals(lastLayedOutText);
  }

  public int getLineNumberOfIndex(int index)
  {
    getTextLayouts();
    int lineNumber = 0;
    for (; lineNumber < textLayouts.size()-1; lineNumber++)
    {
      index -= textLayouts.get(lineNumber).getText().length();
      if (index < 0)
        break;
    }
    return lineNumber;
  }

  public abstract int getTopOfStartPositionForCursor();

  public abstract int getBottomPositionForCursor();

  public abstract int getIndexOfLastCharInLine(int line);
}
