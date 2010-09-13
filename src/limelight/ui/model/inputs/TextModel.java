//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.abstrstyling.HorizontalAlignmentValue;
import limelight.styles.abstrstyling.VerticalAlignmentValue;
import limelight.ui.Fonts;
import limelight.ui.model.inputs.offsetting.XOffsetStrategy;
import limelight.ui.model.inputs.offsetting.YOffsetStrategy;
import limelight.ui.text.*;
import limelight.ui.model.TextPanel;
import limelight.util.Box;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public abstract class TextModel implements ClipboardOwner
{
  private TextContainer container;
  private StringBuffer text = new StringBuffer(); // TODO MDM Storing the text in both StringBuffer and List of TextLayouts leads to some major inefficiencies.  Should make an attempt to get rid of StringBuffer.
  private ArrayList<TypedLayout> lines;
  private Point offset = new Point(0, 0);
  private boolean caretOn;
  private TypedLayoutFactory typedLayoutFactory = TextTypedLayoutFactory.instance;
  private TextLocation caretLocation = TextLocation.at(0, 0);
  private boolean selectionActivated;
  private TextLocation selectionLocation = TextLocation.at(0, 0);
  private TextLocation verticalOrigin;
  private boolean changeFlag;

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
    return Fonts.fromStyle(container.getStyle());
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
    return horizontalAlignment.getX(line.getWidth(), getContainer().getBounds());
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
    Box boundingBox = getContainer().getBounds();
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
    changeFlag = true;
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

  public String getClipboardContents()
  {
    Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
    if(contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor))
    {
      try
      {
        return (String) contents.getTransferData(DataFlavor.stringFlavor);
      }
      catch(Exception e)
      {
        return "";
      }
    }
    return "";
  }

  public void copySelection()
  {
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(getSelectedText()), this);
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
        clearCache();
      }
      setCaretLocation(TextLocation.fromIndex(getLines(), caretIndex + clipboard.length()));
    }
  }

  public void cutSelection()
  {
    if(!selectionActivated)
      return;

    copySelection();
    deleteSelection();
  }

  public void deleteSelection()
  {
    if(!selectionActivated)
      return;

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
      clearCache();
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

  public TextLocation getSelectionLocation()
  {
    return selectionLocation;
  }

  public void setSelectionLocation(TextLocation location)
  {
    selectionLocation = location;
  }

  public synchronized void setText(String newText)
  {
    if(newText == null)
      newText = "";

    if(newText.length() == text.length() && newText.equals(getText()))
      return;

    text = new StringBuffer(newText);
    clearCache();
    setCaretLocation(getEndLocation());
  }

  public String getText()
  {
    return text.toString();
  }

  public synchronized void insertChar(char c)
  {
    if(c == KeyEvent.CHAR_UNDEFINED)
      return;
    if(selectionActivated)
      deleteSelection();
    text.insert(getCaretLocation().toIndex(getLines()), c);
    clearCache();
    setCaretLocation(getCaretLocation().moved(getLines(), 1));
  }

  public TextLocation locateNearestWordToTheLeft()
  {
    return findWordsLeftEdge(getCaretLocation().moved(getLines(), -1));
  }

  public TextLocation locateNearestWordToTheRight()
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
    int newLineNumber = caretLocation.line + lineDelta;
    if(newLineNumber >= 0 && newLineNumber < lines.size())
    {
      TextLocation origin = verticalOrigin != null ? verticalOrigin : caretLocation;
      int desiredX = lines.get(origin.line).getX(origin.index);

      TypedLayout newLine = lines.get(newLineNumber);
      int newIndex = newLine.getIndexAt(desiredX);

      TextLocation newLocation = TextLocation.at(newLineNumber, newIndex);
      setCaretLocation(newLocation);

      verticalOrigin = origin;
    }
  }

  public void sendCaretToStartOfLine()
  {
    setCaretLocation(TextLocation.at(getCaretLocation().line, 0));
  }

  public void sendCaretToEndOfLine()
  {
    final TextLocation caret = getCaretLocation();
    TypedLayout caretLine = getLines().get(caret.line);
    setCaretLocation(TextLocation.at(caret.line, caretLine.visibleLength()));
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

  public TextLocation getSelectionEnd()
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

  public void resetChangeFlag()
  {
    changeFlag = false;
  }

  public boolean hasChanged()
  {
    return changeFlag;
  }


  public boolean isSingleLine()
  {
    return false;
  }
}
