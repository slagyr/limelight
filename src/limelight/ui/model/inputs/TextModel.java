//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.Style;
import limelight.styles.abstrstyling.HorizontalAlignmentValue;
import limelight.styles.abstrstyling.VerticalAlignmentValue;
import limelight.ui.model.inputs.offsetting.XOffsetStrategy;
import limelight.ui.model.inputs.offsetting.YOffsetStrategy;
import limelight.ui.text.*;
import limelight.ui.model.TextPanel;
import limelight.util.Box;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.awt.font.TextHitInfo;
import java.io.IOException;
import java.util.ArrayList;

public abstract class TextModel implements ClipboardOwner
{
  protected TextContainer container;
  private ArrayList<TypedLayout> lines;
  private TypedLayoutFactory typedLayoutFactory = TextTypedLayoutFactory.instance;

  protected final StringBuffer text = new StringBuffer();
  private int spaceWidth;
  protected Point offset = new Point(0, 0);
  private int caretIndex;
  private int lastCaretIndex;
  private boolean selectionOn;
  private int selectionIndex;

  protected abstract int getLineNumber(int index);

  protected abstract void buildLines(ArrayList<TypedLayout> lines);

  public abstract boolean isMoveUpEvent(int keyCode);

  public abstract boolean isMoveDownEvent(int keyCode);

  public abstract TypedLayout getActiveLayout();

  public abstract Box getCaretShape();

  public abstract Dimension getTextDimensions();

  public abstract int getIndexAt(int x, int y);

  public abstract ArrayList<Box> getSelectionRegions();

  public TextModel(TextContainer container)
  {
    this.container = container;
  }

  public Font getFont()
  {
    // TODO MDM This is inefficient... should cache value
    Style style = container.getStyle();
    String fontFace = style.getCompiledFontFace().getValue();
    int fontStyle = style.getCompiledFontStyle().toInt();
    int fontSize = style.getCompiledFontSize().getValue();
    return new Font(fontFace, fontStyle, fontSize);
  }

  public synchronized final ArrayList<TypedLayout> getLines()
  {
    if(lines == null)
    {
      lines = new ArrayList<TypedLayout>();
      buildLines(lines);
    }
    return lines;
  }

  public int getXOffset()
  {
    return offset.x;
  }

  public int getYOffset()
  {
    return offset.y;
  }

  public int getCaretX()
  {
    return getX(caretIndex);
  }

  public int getX(int index)
  {
    TextLocation location = TextLocation.fromIndex(getLines(), index);
    TypedLayout line = getLines().get(location.line);
    String textBeforeCaret = line.getText().substring(0, location.index);
    return line.getWidthOf(textBeforeCaret);
  }

  public int getCaretY()
  {
    int line = getLineNumber(getCaretIndex());
    int height = 0;
    for(int i = 0; i < line; i++)
    {
      TypedLayout layout = getLines().get(i);
      height += layout.getHeight() + layout.getLeading();
    }
    return height;
  }

  public int getSelectionX()
  {
    return getX(selectionIndex);
  }

  protected void recalculateOffset(XOffsetStrategy xOffsetStrategy, YOffsetStrategy yOffsetStrategy)
  {
    int xOffset = getXOffset();
    int yOffset = getYOffset();
    Box boundingBox = getContainer().getBoundingBox();
    Dimension textDimensions = getTextDimensions();

    if(textDimensions.width < boundingBox.width)
      xOffset = getHorizontalAlignment().getX(textDimensions.width, boundingBox);
    else
      xOffset = xOffsetStrategy.calculateXOffset(this);

    if(textDimensions.height < boundingBox.height)
      yOffset = getVerticalAlignment().getY(textDimensions.height, boundingBox);
    else
      yOffset = yOffsetStrategy.calculateYOffset(this);

    setOffset(xOffset, yOffset);
  }

  public synchronized void clearCache()
  {
    lines = null;
  }

  public int getTotalHeightOfLineWithLeadingMargin(int layoutIndex)
  {
    TypedLayout line = getLines().get(layoutIndex);
    return (int)(line.getHeight() + line.getLeading() + 0.5);
  }

  public VerticalAlignmentValue getVerticalAlignment()
  {
    return container.getStyle().getCompiledVerticalAlignment();
  }

  public HorizontalAlignmentValue getHorizontalAlignment()
  {
    return container.getStyle().getCompiledHorizontalAlignment();
  }

  public boolean isCursorOn()
  {
    return container.isCursorOn();
  }

  public boolean isFocused()
  {
    return container.isFocused();
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

  public synchronized void pasteClipboard()
  {
    String clipboard = getClipboardContents();
    if(clipboard != null && clipboard.length() > 0)
    {
      text.insert(caretIndex, clipboard);
      clearCache();
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

  public synchronized void deleteEnclosedText(int first, int second)
  {
    text.delete(first, second);
    lines = null;
    setCaretIndex(first);
    setSelectionIndex(0);
  }

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

  public void setCaretIndex(int index)
  {
    setCaretIndex(index, XOffsetStrategy.CENTERED, YOffsetStrategy.FITTING);
  }

  public void setCaretIndex(int index, XOffsetStrategy xOffsetStrategy, YOffsetStrategy yOffsetStrategy)
  {
    lastCaretIndex = caretIndex;
    caretIndex = index;
    recalculateOffset(xOffsetStrategy, yOffsetStrategy);
  }

  public int getSelectionIndex()
  {
    return selectionIndex;
  }

  public void setSelectionIndex(int selectionIndex)
  {
    this.selectionIndex = selectionIndex;
  }

  public synchronized void setText(String newText)
  {
    if(newText == null)
      newText = "";

    text.replace(0, text.length(), newText);
    clearCache();
    setCaretIndex(newText.length());
  }

  public String getText()
  {
    if(text == null)
      return null;
    return text.toString();
  }

  public int getLastKeyPressed()
  {
    return container.getLastKeyPressed();
  }

  public void setLastKeyPressed(int keyCode)
  {
    container.setLastKeyPressed(keyCode);
  }

  public int getLastCaretIndex()
  {
    return lastCaretIndex;
  }

  public void setLastCaretIndex(int index)
  {
    lastCaretIndex = index;
  }

  public synchronized void insertChar(char c)
  {
    if(c == KeyEvent.CHAR_UNDEFINED)
      return;
    text.insert(getCaretIndex(), c);
    clearCache();
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
    int previousLine = getLineNumber(getCaretIndex()) - 1;
    setCaretIndex(getNewCursorPositionAfterMovingByALine(previousLine));
  }

  public void moveCursorDownALine()
  {
    if(getLastKeyPressed() == KeyEvent.VK_UP)
    {
      setCaretIndex(getLastCaretIndex());
      return;
    }
    int nextLine = getLineNumber(getCaretIndex()) + 1;
    int newCursorIndex = getNewCursorPositionAfterMovingByALine(nextLine);
    setCaretIndex(newCursorIndex);
  }

  private int getNewCursorPositionAfterMovingByALine(int nextLine)
  {
    int charCount = 0;
    for(int i = 0; i < nextLine; i++)
      charCount += lines.get(i).getText().length();
    int xPos = getX(caretIndex);
    String lineText = lines.get(nextLine).getText();
    int lineLength = lineText.length();
    int newCursorIndex = charCount + lineLength - 1;
    if(nextLine == lines.size() - 1)
      newCursorIndex++;
    if(getX(newCursorIndex) < xPos)
    {
      return newCursorIndex;
    }
    else
    {
      TextHitInfo hitInfo = lines.get(nextLine).hitTestChar(xPos, 5);
      newCursorIndex = hitInfo.getInsertionIndex();
      if(newCursorIndex == lineLength && lineText.endsWith("\n"))
        newCursorIndex--;
      return newCursorIndex + charCount;
    }
  }

  public void sendCursorToStartOfLine()
  {
    int lineNumber = getLineNumber(getCaretIndex());
    int newIndex = TextLocation.at(lineNumber, 0).toIndex(getLines());
    setCaretIndex(newIndex);
  }

  public void sendCaretToEndOfLine()
  {
    int lineNumber = getLineNumber(getCaretIndex());
    int endIndex = getLines().get(lineNumber).getText().length();
    int newIndex = TextLocation.at(lineNumber, endIndex).toIndex(getLines());
    setCaretIndex(newIndex);
  }

  public void setSelectionOn(boolean value)
  {
    selectionOn = value;
  }

  public boolean isSelectionOn()
  {
    return selectionOn;
  }

  public TextContainer getContainer()
  {
    return container;
  }

  public Point getOffset()
  {
    return offset;
  }

  public void setOffset(int x, int y)
  {
    offset.setLocation(x, y);
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

  public int getCaretWidth()
  {
    return 1;
  }

}
