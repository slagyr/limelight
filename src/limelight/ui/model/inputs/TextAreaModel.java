//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.TypedLayout;
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

public class TextAreaModel extends TextModel
{
  private ArrayList<Integer> newLineCharIndices;

  public TextAreaModel(TextInputPanel myAreaPanel)
  {
    super(myAreaPanel);
    setOffset(0, 0);
  }

  protected int getXPosFromText(String toIndexString)
  {
    TypedLayout layout = createLayout(toIndexString);
    int x = getWidthDimension(layout);
    if(x < 0)
      x = 0;
    return x;
  }

  @Override
  public Dimension getTextDimensions()
  {
    if(getText() != null && getText().length() > 0)
    {
      int height = 0;
      int width = 0;
      for(TypedLayout layout : getTypedLayouts())
      {
        height += (int) (getHeightDimension(layout) + layout.getLeading() + .5);
        int dimWidth = getWidthDimension(layout);
        if(dimWidth > width)
          width = dimWidth;
      }
      return new Dimension(width, height);
    }
    return null;
  }

  @Override
  public ArrayList<TypedLayout> getTypedLayouts()
  {
    if(getText() == null || getText().length() == 0)
    {
      textLayouts = new ArrayList<TypedLayout>();
      textLayouts.add(createLayout(""));
    }
    else
    {
      if(textLayouts == null || isThereSomeDifferentText())
      {
        setLastLayedOutText(getText());
        parseTextForMultipleLayouts();
      }
    }
    return textLayouts;
  }

  @Override
  protected void recalculateOffset()
  {
  }

  @Override
  public TypedLayout getActiveLayout()
  {
    int chars = getCaretIndex();
    for(TypedLayout textLayout : textLayouts)
    {
      int lineChars = textLayout.getText().length();
      if(lineChars >= chars)
        return textLayout;
      chars -= lineChars;
    }
    throw new RuntimeException("Could not find active layout for index: " + getCaretIndex());
  }

  @Override
  public Box getCaretShape()
  {
    int index = getCaretIndex();
    for(TypedLayout textLayout : textLayouts)
    {
      int lineChars = textLayout.getText().length();
      if(lineChars >= index)
        return textLayout.getCaretShape(index);
      index -= lineChars;
    }
    throw new RuntimeException("Could not find layout containing caret");
  }

  @Override
  public ArrayList<Rectangle> getSelectionRegions()
  {
    int cursorLine = getLineNumberOfIndex(getCaretIndex());
    int selectionLine = getLineNumberOfIndex(getSelectionIndex());
    if(cursorLine == selectionLine)
      return selectionRegionsForSingleLine();
    else if(selectionLine > cursorLine)
      return selectionRegionsForMultipleLines(cursorLine, getXPosFromIndex(getCaretIndex()), selectionLine, getXPosFromIndex(getSelectionIndex()));
    else
      return selectionRegionsForMultipleLines(selectionLine, getXPosFromIndex(getSelectionIndex()), cursorLine, getXPosFromIndex(getCaretIndex()));
  }

  private ArrayList<Rectangle> selectionRegionsForSingleLine()
  {
    int cursorX = getXPosFromIndex(getCaretIndex());
    int selectionX = getXPosFromIndex(getSelectionIndex());
    int lineHeight = getTotalHeightOfLineWithLeadingMargin(0);
    int yPos = getYPosFromIndex(getCaretIndex()) - calculateYOffset();
    ArrayList<Rectangle> regions = new ArrayList<Rectangle>();
    if(getCaretIndex() > getSelectionIndex())
      regions.add(new Box(selectionX, yPos, cursorX - selectionX, lineHeight));
    else
      regions.add(new Box(cursorX, yPos, selectionX - cursorX, lineHeight));
    return regions;
  }

  private ArrayList<Rectangle> selectionRegionsForMultipleLines(int startingLine, int startingX, int endingLine, int endingX)
  {
    ArrayList<Rectangle> regions = new ArrayList<Rectangle>();
    int lineHeight = getTotalHeightOfLineWithLeadingMargin(0);
    int yPos = lineHeight * startingLine - calculateYOffset();
    regions.add(new Box(startingX, yPos, myPanel.getWidth() - startingX, lineHeight));
    yPos += lineHeight;
    for(int i = startingLine + 1; i < endingLine; i++)
    {
      regions.add(new Box(0, yPos, myPanel.getWidth(), lineHeight));
      yPos += lineHeight;
    }
    regions.add(new Box(0, yPos, endingX, lineHeight));
    return regions;
  }

  @Override
  public boolean isBoxFull()
  {
    if(getText().length() > 0)
      return (myPanel.getHeight() <= getTextDimensions().height);
    return false;
  }

  @Override
  public boolean isMoveUpEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_UP && getLineNumberOfIndex(getCaretIndex()) > 0;
  }

  @Override
  public boolean isMoveDownEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_DOWN && getLineNumberOfIndex(getCaretIndex()) < textLayouts.size() - 1;
  }

  @Override
  public int getIndexOfLastCharInLine(int line)
  {
    getTypedLayouts();
    int numberOfCharacters = 0;
    for(int i = 0; i <= line; i++)
      numberOfCharacters += textLayouts.get(i).getText().length();
    if(line != textLayouts.size() - 1)
      numberOfCharacters--;
    return numberOfCharacters;
  }

  public int calculateYOffset()
  {
    int yOffset = 0;
    int yPos = getYPosFromIndex(getCaretIndex());
    int lineHeight = getTotalHeightOfLineWithLeadingMargin(0);
    int panelHeight = myPanel.getHeight();

    while(yPos <= yOffset)
      yOffset -= lineHeight;

    if(yOffset < 0)
      yOffset = 0;

    while((yPos + lineHeight) > yOffset + panelHeight)
      yOffset += lineHeight;

    return yOffset;
  }

  @Override
  public int getXOffset()
  {
    return 0;
  }

  @Override
  public int getYOffset()
  {
    return offset == null ? 0 : offset.y;
  }

  @Override
  public boolean isCursorAtCriticalEdge(int cursorX)
  {
    return false;
  }

  public ArrayList<TypedLayout> parseTextForMultipleLayouts()
  {
    AttributedCharacterIterator iterator = getIterator();

    newLineCharIndices = findNewLineCharIndices(getText());
    textLayouts = new ArrayList<TypedLayout>();

    LineBreakMeasurer breaker = new LineBreakMeasurer(iterator, TextPanel.getRenderContext());
    int lastCharIndex = 0, newLineCharIndex = 0;
    while(breaker.getPosition() < iterator.getEndIndex())
    {
      lastCharIndex = addANewLayoutForTheNextLine(breaker, lastCharIndex, newLineCharIndex);
      if(layoutEndedOnNewLineChar(lastCharIndex, newLineCharIndex))
        newLineCharIndex++;
    }
    addBlankLayoutIfLastLineIsEmpty();

    return textLayouts;
  }

  private void addBlankLayoutIfLastLineIsEmpty()
  {
    if(getText().length() > 0 && isTheVeryLastCharANewLineChar())
      textLayouts.add(createLayout(""));
  }

  private int addANewLayoutForTheNextLine(LineBreakMeasurer breaker, int lastCharIndex, int newLineCharIndex)
  {
    int firstCharIndex = lastCharIndex;
    lastCharIndex = firstCharIndex + getNextLayout(breaker, newLineCharIndex).getCharacterCount();
    String layoutText = getText().substring(firstCharIndex, lastCharIndex);
    textLayouts.add(createLayout(layoutText));
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
      layout = breaker.nextLayout(myPanel.getWidth(), newLineCharIndices.get(returnCharIndex) + 1, false);
    else
      layout = breaker.nextLayout(myPanel.getWidth());
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
}
