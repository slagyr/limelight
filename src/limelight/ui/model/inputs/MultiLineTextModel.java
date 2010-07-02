//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.text.TextLocation;
import limelight.ui.text.TypedLayout;
import limelight.ui.model.TextPanel;
import limelight.util.Box;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;

public class MultiLineTextModel extends TextModel
{
  private ArrayList<Integer> newLineCharIndices;

  public MultiLineTextModel(TextContainer myAreaPanel)
  {
    super(myAreaPanel);
    setOffset(0, 0);
  }

  @Override
  public Dimension getTextDimensions()
  {
    if(getText() == null && getText().length() == 0)
      return new Dimension(0, 0);

    int height = 0;
    int width = 0;
    for(TypedLayout layout : getLines())
    {
      height += layout.getHeight();
      int lineWidth = layout.getWidth();
      if(lineWidth > width)
        width = lineWidth;
    }
    return new Dimension(width, height);
  }

  @Override
  public TextLocation getLocationAt(Point point)
  {
    int remainingY = point.y - getYOffset();
    ArrayList<TypedLayout> lines = getLines();
    for(int lineNumber = 0; lineNumber < lines.size(); lineNumber++)
    {
      TypedLayout line = lines.get(lineNumber);
      int lineHeight = line.getHeight();
      if(lineHeight > remainingY)
      {
        int lineIndex = line.getIndexAt(point.x - getXOffset());
        return TextLocation.at(lineNumber, lineIndex);
      }
      else
      {
        remainingY -= lineHeight;
      }
    }
    return TextLocation.fromIndex(lines, getText().length());
  }

  public TypedLayout getLineAt(int y)
  {
    int remainingY = y - getYOffset();
    ArrayList<TypedLayout> lines = getLines();
    for(TypedLayout line : lines)
    {
      int lineHeight = line.getHeight();
      if(lineHeight > remainingY)
        return line;
      else
        remainingY -= lineHeight;
    }
    return lines.get(lines.size() - 1);
  }

  protected void buildLines(ArrayList<TypedLayout> lines)
  {
    if(getText() == null || getText().length() == 0)
      lines.add(createLayout(""));
    else
      parseTextForMultipleLayouts(lines);
  }

  @Override
  public TypedLayout getActiveLayout()
  {
    TextLocation caretLocation = TextLocation.fromIndex(getLines(), getCaretIndex());
    return getLines().get(caretLocation.line);
  }

  @Override
  public Box getCaretShape()
  {
    TextLocation caretLocation = TextLocation.fromIndex(getLines(), getCaretIndex());
    TypedLayout line = getLines().get(caretLocation.line);
    Box caretShape = line.getCaretShape(caretLocation.index);
    caretShape.translate(getXOffset(), getCaretY() + getYOffset());
    return caretShape;
  }

  @Override
  public ArrayList<Box> getSelectionRegions()
  {
    int cursorLine = getLineNumber(getCaretIndex());
    int selectionLine = getLineNumber(getSelectionIndex());
    if(cursorLine == selectionLine)
      return selectionRegionsForSingleLine();
    else if(selectionLine > cursorLine)
      return selectionRegionsForMultipleLines(cursorLine, getCaretX(), selectionLine, getX(getSelectionIndex()));
    else
      return selectionRegionsForMultipleLines(selectionLine, getX(getSelectionIndex()), cursorLine, getX(getCaretIndex()));
  }

  private ArrayList<Box> selectionRegionsForSingleLine()
  {
    int cursorX = getX(getCaretIndex());
    int selectionX = getX(getSelectionIndex());
    int lineHeight = getTotalHeightOfLineWithLeadingMargin(0);

    int yPos = getCaretY();

    ArrayList<Box> regions = new ArrayList<Box>();
    if(getCaretIndex() > getSelectionIndex())
      regions.add(new Box(selectionX, yPos, cursorX - selectionX, lineHeight));
    else
      regions.add(new Box(cursorX, yPos, selectionX - cursorX, lineHeight));
    return regions;
  }

  private ArrayList<Box> selectionRegionsForMultipleLines(int startingLine, int startingX, int endingLine, int endingX)
  {
    ArrayList<Box> regions = new ArrayList<Box>();
    int lineHeight = getTotalHeightOfLineWithLeadingMargin(0);
    int yPos = lineHeight * startingLine - getYOffset();
    regions.add(new Box(startingX, yPos, container.getWidth() - startingX, lineHeight));
    yPos += lineHeight;
    for(int i = startingLine + 1; i < endingLine; i++)
    {
      regions.add(new Box(0, yPos, container.getWidth(), lineHeight));
      yPos += lineHeight;
    }
    regions.add(new Box(0, yPos, endingX, lineHeight));
    return regions;
  }

  @Override
  public boolean isMoveUpEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_UP && getLineNumber(getCaretIndex()) > 0;
  }

  @Override
  public boolean isMoveDownEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_DOWN && getLineNumber(getCaretIndex()) < getLines().size() - 1;
  }

  public void parseTextForMultipleLayouts(ArrayList<TypedLayout> lines)
  {
    AttributedCharacterIterator iterator = getIterator();

    newLineCharIndices = findNewLineCharIndices(getText());

    LineBreakMeasurer breaker = new LineBreakMeasurer(iterator, TextPanel.getRenderContext());
    int lastCharIndex = 0, newLineCharIndex = 0;
    while(breaker.getPosition() < iterator.getEndIndex())
    {
      lastCharIndex = addNewLayoutForTheNextLine(lines, breaker, lastCharIndex, newLineCharIndex);
      if(layoutEndedOnNewLineChar(lastCharIndex, newLineCharIndex))
        newLineCharIndex++;
    }
    addBlankLayoutIfLastLineIsEmpty(lines);
  }

  private void addBlankLayoutIfLastLineIsEmpty(ArrayList<TypedLayout> lines)
  {
    if(getText().length() > 0 && isTheVeryLastCharANewLineChar())
      lines.add(createLayout(""));
  }

  private int addNewLayoutForTheNextLine(ArrayList<TypedLayout> lines, LineBreakMeasurer breaker, int lastCharIndex, int newLineCharIndex)
  {
    int firstCharIndex = lastCharIndex;
    lastCharIndex = firstCharIndex + getNextLayout(breaker, newLineCharIndex).getCharacterCount();
    String layoutText = getText().substring(firstCharIndex, lastCharIndex);
    lines.add(createLayout(layoutText));
    return lastCharIndex;
  }

  private boolean layoutEndedOnNewLineChar(int lastCharIndex, int returnCharIndex)
  {
    return thereAreMoreReturnCharacters(returnCharIndex) && lastCharIndex == newLineCharIndices.get(returnCharIndex) + 1;
  }

  private TextLayout getNextLayout(LineBreakMeasurer breaker, int returnCharIndex)
  {
    TextLayout layout;
    if(thereAreMoreReturnCharacters(returnCharIndex))
      layout = breaker.nextLayout(container.getWidth(), newLineCharIndices.get(returnCharIndex) + 1, false);
    else
      layout = breaker.nextLayout(container.getWidth());
    return layout;
  }

  private AttributedCharacterIterator getIterator()
  {
    AttributedString attrString = new AttributedString(getText());
    attrString.addAttribute(TextAttribute.FONT, getFont());
    return attrString.getIterator();
  }

  private boolean isTheVeryLastCharANewLineChar()
  {
    return getText().charAt(getText().length() - 1) == '\n';
  }

  private boolean thereAreMoreReturnCharacters(int returnCharIndex)
  {
    return newLineCharIndices != null && returnCharIndex < newLineCharIndices.size();
  }

  public ArrayList<Integer> findNewLineCharIndices(String text)
  {
    ArrayList<Integer> indices = new ArrayList<Integer>();
    for(int i = 0; i < text.length(); i++)
    {
      if(text.charAt(i) == '\n' || text.charAt(i) == '\r')
        indices.add(i);
    }
    return indices;
  }

  protected int getLineNumber(int index)
  {
    return TextLocation.fromIndex(getLines(), index).line;
  }
}
