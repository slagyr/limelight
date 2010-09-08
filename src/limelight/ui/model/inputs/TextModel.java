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
  private boolean selectionActivated;
  protected boolean caretOn;
  private TextLocation selectionLocation = TextLocation.at(0, 0);
  private TextLocation verticalOrigin;

  public abstract Box getCaretShape();

  public abstract Dimension getTextDimensions();

  public abstract TextLocation getLocationAt(Point point);

  public abstract ArrayList<Box> getSelectionRegions();

  protected abstract void buildLines(ArrayList<TypedLayout> lines);

  protected abstract XOffsetStrategy getDefaultXOffsetStrategy();

  protected abstract YOffsetStrategy getDefaultYOffsetStrategy();

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


  public boolean isCaretOn()
  {
    return caretOn;
  }

  public void setCaretOn(boolean value)
  {
    caretOn = value;
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

  public int getXOffset(TypedLayout line)
  {
    HorizontalAlignmentValue horizontalAlignment = getContainer().getStyle().getCompiledHorizontalAlignment();
    return horizontalAlignment.getX(line.getWidth(), getContainer().getBoundingBox());
  }

  public int getYOffset()
  {
    return offset.y;
  }

  public int getX(int index)
  {
    TextLocation location = TextLocation.fromIndex(getLines(), index);
    return getX(location);
  }

  public int getX(TextLocation location)
  {
    return getAbsoluteX(location) + getXOffset();
  }

  public int getAbsoluteX(TextLocation location)
  {
    return getLines().get(location.line).getX(location.index);
  }

  public int getY(TextLocation location)
  {
    return getAbsoluteY(location) + getYOffset();
  }

  public int getAbsoluteY(TextLocation location)
  {
    int height = 0;
    for(int i = 0; i < location.line; i++)
    {
      TypedLayout layout = getLines().get(i);
      height += layout.getHeightWithLeading();
    }
    return height;
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

  public VerticalAlignmentValue getVerticalAlignment()
  {
    return container.getStyle().getCompiledVerticalAlignment();
  }

  public HorizontalAlignmentValue getHorizontalAlignment()
  {
    return container.getStyle().getCompiledHorizontalAlignment();
  }

  public boolean hasFocus()
  {
    return container.hasFocus();
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
    deleteEnclosedText(getSelectionStart(), getSelectionEnd());
    deactivateSelection();
  }

  public void deleteEnclosedText(TextLocation start, TextLocation end)
  {
    ArrayList<TypedLayout> lines = getLines();
    int startIndex = start.toIndex(lines);
    int endIndex = end.toIndex(lines);
    synchronized(this)
    {
      text.delete(startIndex, endIndex);
      this.lines = null;
    }
    setCaretLocation(start);
    setSelectionLocation(start);
  }

  public TextLocation findWordsRightEdge(TextLocation location)
  {

    for(int i = location.toIndex(getLines()); i <= text.length() - 1; i++)
    {
      if(i == 0)
        i = 1;
      if(isAtEndOfWord(i))
        return TextLocation.fromIndex(getLines(), i);
    }
    return getEndLocation();
  }

  public TextLocation findWordsLeftEdge(TextLocation location)
  {
    for(int i = location.toIndex(getLines()); i > 1; i--)
    {
      if(isAtStartOfWord(i))
        return TextLocation.fromIndex(getLines(), i);
    }
    return TextLocation.origin;
  }

  public boolean isAtEndOfWord(int i)
  {
    return text.charAt(i - 1) != ' ' && text.charAt(i - 1) != '\n' && (text.charAt(i) == ' ' || text.charAt(i) == '\n');
  }

  public boolean isAtStartOfWord(int i)
  {
    if(i < 0 || i > getText().length())
      return true;
    return (text.charAt(i - 1) == ' ' || text.charAt(i - 1) == '\n') && (text.charAt(i) != ' ' && text.charAt(i) != '\n');
  }

  public TextLocation getCaretLocation()
  {
    return caretLocation;
  }

  //TODO MDM - Remove me
  public int getCaretIndex()
  {
    return caretLocation.toIndex(getLines());
  }

  //TODO MDM - Remove me
  public void setCaretIndex(int index)
  {
    setCaretLocation(TextLocation.fromIndex(getLines(), index));
  }

  public void setCaretLocation(TextLocation location)
  {
    setCaretLocation(location, getDefaultXOffsetStrategy(), getDefaultYOffsetStrategy());
  }

  public void setCaretLocation(TextLocation location, XOffsetStrategy xOffsetStrategy, YOffsetStrategy yOffsetStrategy)
  {
    caretLocation = location;
    verticalOrigin = null;
    recalculateOffset(xOffsetStrategy, yOffsetStrategy);
  }

  //TODO MDM - Remove me
  public int getSelectionIndex()
  {
    return selectionLocation.toIndex(getLines());
  }

  public TextLocation getSelectionLocation()
  {
    return selectionLocation;
  }

  //TODO MDM - Remove me
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
    if(selectionActivated)
      deleteSelection();
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

  public TextLocation findNearestWordToTheLeft()
  {
    return findWordsLeftEdge(getCaretLocation().moved(getLines(), -1));
  }

  public TextLocation findNearestWordToTheRight()
  {
    return findNextWordSkippingSpacesOrNewLines(findWordsRightEdge(caretLocation));
  }

  protected TextLocation findNextWordSkippingSpacesOrNewLines(TextLocation startLocation)
  {
    for(int i = startLocation.toIndex(getLines()); i <= text.length() - 1; i++)
    {
      if(isAtStartOfWord(i))
        return TextLocation.fromIndex(getLines(), i);
    }
    return getEndLocation();
  }

  public TextLocation getEndLocation()
  {
    return TextLocation.fromIndex(getLines(), text.length());
  }

  public void selectAll()
  {
    startSelection(TextLocation.origin);
    setCaretLocation(getEndLocation());
  }

  public void moveCaret(int places)
  {
    setCaretLocation(getCaretLocation().moved(getLines(), places));
  }

  public void moveCaretUpALine()
  {
    moveCaretToNewLine(-1);
  }

  public void moveCaretDownALine()
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
      setCaretLocation(newLocation);

      verticalOrigin = origin;
    }
  }

  public void sendCursorToStartOfLine()
  {
    setCaretLocation(TextLocation.at(getCaretLocation().line, 0));
  }

  public void sendCaretToEndOfLine()
  {
    TypedLayout caretLine = getLines().get(getCaretLocation().line);
    setCaretLocation(TextLocation.at(getCaretLocation().line, caretLine.getText().length()));
  }

  public void startSelection(TextLocation location)
  {
    selectionLocation = location;
    selectionActivated = true;
  }

  public void deactivateSelection()
  {
    selectionActivated = false;
  }

  public boolean hasSelection()
  {
    return selectionActivated && !selectionLocation.equals(caretLocation);
  }

  public boolean isSelectionActivated()
  {
    return selectionActivated;
  }

  public String getSelectedText()
  {
    if(!hasSelection())
      return "";

    int startIndex = getSelectionStart().toIndex(getLines());
    int endIndex = getSelectionEnd().toIndex(getLines());

    return text.substring(startIndex, endIndex);
  }

  public TextLocation getSelectionStart()
  {
    return caretLocation.before(selectionLocation) ? caretLocation : selectionLocation;
  }

  private TextLocation getSelectionEnd()
  {
    return caretLocation.before(selectionLocation) ? selectionLocation : caretLocation;
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
