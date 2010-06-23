//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.Style;
import limelight.styles.abstrstyling.HorizontalAlignmentValue;
import limelight.styles.abstrstyling.VerticalAlignmentValue;
import limelight.ui.TextLayoutImpl;
import limelight.ui.TextTypedLayoutFactory;
import limelight.ui.TypedLayout;
import limelight.ui.model.TextPanel;
import limelight.util.Box;
import limelight.util.Debug;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.io.IOException;
import java.util.ArrayList;

public abstract class TextModel implements ClipboardOwner
{
  public static final int CARET_WIDTH = 1;

  protected TextInputPanel myPanel;
  protected ArrayList<TypedLayout> textLayouts;
  private TypedLayoutFactory typedLayoutFactory = TextTypedLayoutFactory.instance;

  private StringBuffer text = new StringBuffer();
  private int spaceWidth;
  protected Point offset = new Point(0, 0);
  protected int caretX;
  private int caretIndex;
  private int lastCaretIndex;
  private boolean selectionOn;
  private int selectionStartX;
  private int selectionIndex;
  private String lastLayedOutText;

  public abstract int calculateYOffset();

  public abstract int getXOffset();

  public abstract int getYOffset();

  protected abstract int getXPosFromText(String toIndexString);

  public abstract boolean isBoxFull();

  public abstract boolean isMoveUpEvent(int keyCode);

  public abstract boolean isMoveDownEvent(int keyCode);

  public abstract int getIndexOfLastCharInLine(int line);

  public abstract boolean isCursorAtCriticalEdge(int cursorX);

  public abstract ArrayList<TypedLayout> getTypedLayouts();

  protected abstract void recalculateOffset();
  
  public abstract TypedLayout getActiveLayout();

  public abstract Box getCaretShape();

  public TextModel(TextInputPanel panel)
  {
    this.myPanel = panel;
  }

  public void setCursorAndSelectionStartX()
  {
    caretX = getXPosFromIndex(caretIndex);
    selectionStartX = getXPosFromIndex(selectionIndex);
  }

  public Font getFont()
  {
    Style style = myPanel.getStyle();
    String fontFace = style.getCompiledFontFace().getValue();
    int fontStyle = style.getCompiledFontStyle().toInt();
    int fontSize = style.getCompiledFontSize().getValue();
    return new Font(fontFace, fontStyle, fontSize);
  }

  public int getXPosFromIndex(int index)
  {
    if(text == null || text.length() == 0)
      return 0;
    int lineNumber = getLineNumberOfIndex(index);
    int startOfLineIndex = 0;
    for(int i = 0; i < lineNumber; i++)
      startOfLineIndex += textLayouts.get(i).getText().length();
    if(index <= 0 || startOfLineIndex == index)
      return 0;
    String toIndexString = text.substring(startOfLineIndex, index);
    int xPos = getXPosFromText(toIndexString) + getTerminatingSpaceWidth(toIndexString);
    if(xPos < 0)
      return 0;
    return xPos;
  }

  public void clearLayouts()
  {
    textLayouts = null;
  }

  public int getYPosFromIndex(int index)
  {
    int yPos = 0;
    if(text == null || text.length() == 0)
      return yPos;
    int lineNumber = getLineNumberOfIndex(index);
    int lineHeight = getTotalHeightOfLineWithLeadingMargin(0);
    for(int layoutIndex = 0; layoutIndex < lineNumber; layoutIndex++)
      yPos += lineHeight;
    return yPos;
  }

  public int getTerminatingSpaceWidth(String string)
  {
    int trailingSpaces = 0;
    for(int i = string.length() - 1; i > 0 && string.charAt(i) == ' '; i--)
      trailingSpaces++;

    return trailingSpaces * spaceWidth();
  }

  public int spaceWidth()
  {
    Font font = getFont();
    if(spaceWidth == 0)
    {
      TypedLayout layout1 = new TextLayoutImpl("a a", font, TextPanel.getRenderContext());
      TypedLayout layout2 = new TextLayoutImpl("aa", font, TextPanel.getRenderContext());
      spaceWidth = getWidthDimension(layout1) - getWidthDimension(layout2);
    }
    return spaceWidth;
  }

  public abstract Dimension getTextDimensions();

  public float getHeightDimension(TypedLayout layout)
  {
    return (layout.getAscent() + layout.getDescent());

  }

  public int getTotalHeightOfLineWithLeadingMargin(int layoutIndex)
  {
    textLayouts = getTypedLayouts();
    TypedLayout typedLayout = textLayouts.get(layoutIndex);
    return (int) (getHeightDimension(typedLayout) + typedLayout.getLeading() + 0.5);
  }

  public int getHeightOfCurrentLine()
  {
    int lineNumber = getLineNumberOfIndex(caretIndex);
    TypedLayout layoutForLine = textLayouts.get(lineNumber);
    return (int) getHeightDimension(layoutForLine);
  }

  public int getWidthDimension(TypedLayout layout)
  {
    return layout.getWidth();
  }

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

  public int getYAlignmentOffset()
  {
    try
    {
      return getVerticalAlignment().getY(getTextDimensions().height, myPanel.getBoundingBox());
    }
    catch(Exception e)
    {
Debug.log("e = " + e);
      return 0;
    }
  }

  public VerticalAlignmentValue getVerticalAlignment()
  {
    return myPanel.getStyle().getCompiledVerticalAlignment();
  }

  public int getXAlignmentOffset()
  {
    return getHorizontalAlignment().getX(getTextDimensions().width, myPanel.getBoundingBox()) - getOffset().x;
  }

  public HorizontalAlignmentValue getHorizontalAlignment()
  {
    return myPanel.getStyle().getCompiledHorizontalAlignment();
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
    if(hasText)
    {
      try
      {
        clipboardString = (String) contents.getTransferData(DataFlavor.stringFlavor);
      }
      catch(UnsupportedFlavorException e)
      {
        e.printStackTrace();
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }
    }
    return clipboardString;
  }

  public void copySelection()
  {
    String clipboard;
    if(selectionIndex < caretIndex)
      clipboard = text.substring(selectionIndex, caretIndex);
    else
      clipboard = text.substring(caretIndex, selectionIndex);
    copyText(clipboard);
  }

  public void pasteClipboard()
  {
    String clipboard = getClipboardContents();
    if(clipboard != null && clipboard.length() > 0)
    {
      text.insert(caretIndex, clipboard);
      setCaretIndex(caretIndex + clipboard.length());
    }
  }

  public void cutSelection()
  {
    copySelection();
    deleteSelection();
  }

  public void deleteSelection()
  {
    if(selectionIndex < caretIndex)
      deleteEnclosedText(selectionIndex, caretIndex);
    else
      deleteEnclosedText(caretIndex, selectionIndex);
  }

  public void deleteEnclosedText(int first, int second)
  {
    text.delete(first, second);
    setCaretIndex(first);
    setSelectionIndex(0);
  }

  public abstract ArrayList<Rectangle> getSelectionRegions();

  public int findWordsRightEdge(int index)
  {
    for(int i = index; i <= text.length() - 1; i++)
    {
      if(i == 0)
        i = 1;
      if(isAtEndOfWord(i))
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
    for(int i = index; i > 1; i--)
    {
      if(isAtStartOfWord(i))
        return i;
    }
    return 0;
  }

  public boolean isAtStartOfWord(int i)
  {
    return (text.charAt(i - 1) == ' ' || text.charAt(i - 1) == '\n') && (text.charAt(i) != ' ' && text.charAt(i) != '\n');
  }

  public int getCaretIndex()
  {
    return caretIndex;
  }

  public void setCaretIndex(int cursorIndex)
  {
    lastCaretIndex = this.caretIndex;
    this.caretIndex = cursorIndex;
    recalculateOffset();
  }

  public int getSelectionIndex()
  {
    return selectionIndex;
  }

  public void setSelectionIndex(int selectionIndex)
  {
    this.selectionIndex = selectionIndex;
  }

  public void setText(String text)
  {
    if(text == null)
      this.text = null;
    else
    {
      this.text = new StringBuffer(text);
      setCaretIndex(text.length());
    }
  }

  public String getText()
  {
    if(text == null)
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

  public int getLastCaretIndex()
  {
    return lastCaretIndex;
  }

  public void setLastCaretIndex(int index)
  {
    lastCaretIndex = index;
  }

  public boolean isThereSomeDifferentText()
  {
    return !text.toString().equals(lastLayedOutText);
  }


  public int getLineNumberOfIndex(int index)
  {
    getTypedLayouts();
    int lineNumber = 0;
    for(; lineNumber < textLayouts.size() - 1; lineNumber++)
    {
      index -= textLayouts.get(lineNumber).getText().length();
      if(index < 0)
        break;
    }
    return lineNumber;
  }


  public void insertCharIntoTextBox(char c)
  {
    if(c == KeyEvent.CHAR_UNDEFINED)
      return;
    text.insert(getCaretIndex(), c);
    setCaretIndex(getCaretIndex() + 1);
  }

  public boolean isMoveRightEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_RIGHT && getCaretIndex() < getText().length();
  }

  public boolean isMoveLeftEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_LEFT && getCaretIndex() > 0;
  }

  public void initSelection()
  {
    selectionOn = true;
    setSelectionIndex(getCaretIndex());
  }

  public int findNearestWordToTheLeft()
  {
    return findWordsLeftEdge(getCaretIndex() - 1);
  }

  public int findNearestWordToTheRight()
  {
    return findNextWordSkippingSpacesOrNewLines(findWordsRightEdge(caretIndex));
  }

  protected int findNextWordSkippingSpacesOrNewLines(int startIndex)
  {
    for(int i = startIndex; i <= getText().length() - 1; i++)
    {
      if(isAtStartOfWord(i))
        return i;
    }
    return getText().length();
  }


  public void selectAll()
  {
    selectionOn = true;
    setCaretIndex(getText().length());
    setSelectionIndex(0);
  }

  public void moveCursorUpALine()
  {
    if(getLastKeyPressed() == KeyEvent.VK_DOWN)
    {
      setCaretIndex(getLastCaretIndex());
      return;
    }
    int previousLine = getLineNumberOfIndex(caretIndex) - 1;
    setCaretIndex(getNewCursorPositionAfterMovingByALine(previousLine));
  }

  public void moveCursorDownALine()
  {
    if(getLastKeyPressed() == KeyEvent.VK_UP)
    {
      setCaretIndex(getLastCaretIndex());
      return;
    }
    int nextLine = getLineNumberOfIndex(caretIndex) + 1;
    int newCursorIndex = getNewCursorPositionAfterMovingByALine(nextLine);
    setCaretIndex(newCursorIndex);
  }

  private int getNewCursorPositionAfterMovingByALine(int nextLine)
  {
    int charCount = 0;
    for(int i = 0; i < nextLine; i++)
      charCount += textLayouts.get(i).getText().length();
    int xPos = getXPosFromIndex(caretIndex);
    String lineText = textLayouts.get(nextLine).getText();
    int lineLength = lineText.length();
    int newCursorIndex = charCount + lineLength - 1;
    if(nextLine == textLayouts.size() - 1)
      newCursorIndex++;
    if(getXPosFromIndex(newCursorIndex) < xPos)
    {
      return newCursorIndex;
    }
    else
    {
      TextHitInfo hitInfo = textLayouts.get(nextLine).hitTestChar(xPos, 5);
      newCursorIndex = hitInfo.getInsertionIndex();
      if(newCursorIndex == lineLength && lineText.endsWith("\n"))
        newCursorIndex--;
      return newCursorIndex + charCount;
    }
  }

  public void sendCursorToStartOfLine()
  {
    int currentLine = getLineNumberOfIndex(caretIndex);

    if(currentLine == 0)
    {
      setCaretIndex(0);
    }
    else
    {
      setCaretIndex(getIndexOfLastCharInLine(currentLine - 1) + 1);
    }
  }

  public void sendCursorToEndOfLine()
  {
    int currentLine = getLineNumberOfIndex(caretIndex);
    setCaretIndex(getIndexOfLastCharInLine(currentLine));
  }

  public void setSelectionOn(boolean value)
  {
    selectionOn = value;
  }

  public boolean isSelectionOn()
  {
    return selectionOn;
  }

  public void setCaretX(int x)
  {
    caretX = x;
  }

  public int getCaretX()
  {
    return caretX;
  }

  public int getSelectionStartX()
  {
    return selectionStartX;
  }

  public TextInputPanel getPanel()
  {
    return myPanel;
  }
  
  public int getTopOfStartPositionForCursor()
  {
    return getYPosFromIndex(getCaretIndex()) - getOffset().y;
  }

  public int getBottomPositionForCursor()
  {
    return getTopOfStartPositionForCursor() + getHeightOfCurrentLine() - 1;
  }

  public Point getOffset()
  {
    return offset;
  }

  public void setOffset(int x, int y)
  {
    offset.setLocation(x, y);
  }

  public int calculateRightShiftingOffset()
  {
    return 0;
  }

  public int calculateLeftShiftingOffset()
  {
    return 0;
  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
    //this doesn't have to do anything...
  }

  protected TypedLayout createLayout(String text)
  {
    return typedLayoutFactory.createLayout(text, getFont(), TextPanel.getRenderContext());
  }


  public void setTypedLayoutFactory(TypedLayoutFactory factory)
  {
    typedLayoutFactory = factory;
  }
}
