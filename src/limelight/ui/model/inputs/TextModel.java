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
import java.io.IOException;
import java.util.ArrayList;

public abstract class TextModel implements ClipboardOwner
{
  protected TextContainer container;
  private ArrayList<TypedLayout> lines;
  private TypedLayoutFactory typedLayoutFactory = TextTypedLayoutFactory.instance;

  protected final StringBuffer text = new StringBuffer();
  protected Point offset = new Point(0, 0);
  private TextLocation caretLocation = TextLocation.at(0, 0);
//  private int caretIndex;
  private boolean selectionOn;
  private TextLocation selectionLocation = TextLocation.at(0, 0);
//  private int selectionIndex;
  private TextLocation verticalOrigin;

  protected abstract int getLineNumber(int index);

  protected abstract void buildLines(ArrayList<TypedLayout> lines);

  public abstract boolean isMoveUpEvent(int keyCode);

  public abstract boolean isMoveDownEvent(int keyCode);

  public abstract TypedLayout getActiveLayout();

  public abstract Box getCaretShape();

  public abstract Dimension getTextDimensions();

  public abstract TextLocation getLocationAt(Point point);

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
    return getX(caretLocation);
  }

  public int getX(int index)
  {
    TextLocation location = TextLocation.fromIndex(getLines(), index);
    return getX(location);
  }

  private int getX(TextLocation location)
  {
    TypedLayout line = getLines().get(location.line);
    return line.getX(location.index);
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
    return getX(selectionLocation);
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
    return (int) (line.getHeight() + line.getLeading() + 0.5);
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
    ArrayList<TypedLayout> lines = getLines();
    if(selectionLocation.before(caretLocation))
      clipboard = text.substring(selectionLocation.toIndex(lines), caretLocation.toIndex(lines));
    else
      clipboard = text.substring(caretLocation.toIndex(lines), selectionLocation.toIndex(lines));
    copyText(clipboard);
  }

  public void pasteClipboard()
  {
    String clipboard = getClipboardContents();
    if(clipboard != null && clipboard.length() > 0)
    {
      int caretIndex = caretLocation.toIndex(getLines());
      synchronized(this)
      {
        text.insert(caretIndex, clipboard);
      }
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
    if(selectionLocation.before(caretLocation))
      deleteEnclosedText(selectionLocation, caretLocation);
    else
      deleteEnclosedText(caretLocation, selectionLocation);
  }

  public synchronized void deleteEnclosedText(int first, int second)
  {
    text.delete(first, second);
    lines = null;
    setCaretIndex(first);
    setSelectionIndex(0);
  }


  public void deleteEnclosedText(TextLocation first, TextLocation second)
  {
    ArrayList<TypedLayout> lines = getLines();
    int startIndex = first.toIndex(lines);
    int endIndex = second.toIndex(lines);
    synchronized(this)
    {
      text.delete(startIndex, endIndex);
      this.lines = null;
    }
    setCaretLocation(first);
    setCaretIndex(startIndex);
    setSelectionIndex(startIndex);
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
    return caretLocation.toIndex(getLines());
  }

  public void setCaretIndex(int index)
  {
    setCaretLocation(TextLocation.fromIndex(getLines(), index));
  }

  public void setCaretLocation(TextLocation location)
  {
    setCaretLocation(location, XOffsetStrategy.CENTERED, YOffsetStrategy.FITTING);
  }

  public void setCaretLocation(TextLocation location, XOffsetStrategy xOffsetStrategy, YOffsetStrategy yOffsetStrategy)
  {
    caretLocation = location;
    verticalOrigin = null;
    recalculateOffset(xOffsetStrategy, yOffsetStrategy);
  }

  public int getSelectionIndex()
  {
    return selectionLocation.toIndex(getLines());
  }

  public void setSelectionIndex(int selectionIndex)
  {
    setSelectionLocation(TextLocation.fromIndex(getLines(), selectionIndex));
  }

  public void setSelectionLocation(TextLocation location)
  {
    selectionLocation = location;
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
    return findNextWordSkippingSpacesOrNewLines(findWordsRightEdge(caretLocation.toIndex(getLines())));
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
    moveCaretToNewLine(-1);
  }

  public void moveCursorDownALine()
  {
    moveCaretToNewLine(1);
  }

  public void moveCaretToNewLine(int lineDelta)
  {
    ArrayList<TypedLayout> lines = getLines();
    TextLocation location = TextLocation.fromIndex(lines, getCaretIndex());
    int newLineNumber = location.line + lineDelta;
    if(newLineNumber >= 0 && newLineNumber < lines.size())
    {
      TextLocation origin = verticalOrigin != null ? verticalOrigin : location;
      int desiredX = lines.get(origin.line).getX(origin.index);

      TypedLayout newLine = lines.get(newLineNumber);
      int newIndex = newLine.getIndexAt(desiredX);

      TextLocation newLocation = TextLocation.at(newLineNumber, newIndex);
      setCaretIndex(newLocation.toIndex(lines));

      verticalOrigin = origin;
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
