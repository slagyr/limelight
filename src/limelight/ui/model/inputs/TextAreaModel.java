package limelight.ui.model.inputs;

import limelight.ui.TextLayoutImpl;
import limelight.ui.TypedLayout;
import limelight.ui.model.TextPanel;
import limelight.util.Box;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
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
    this.myPanel = myAreaPanel;
    cursorX = SIDE_TEXT_MARGIN;
    selectionOn = false;
    font = new Font("Arial", Font.PLAIN, 12);
    selectionStartX = 0;
    cursorIndex = 0;
    selectionIndex = 0;
    xOffset = 0;
    yOffset = 0;
  }

  @Override
  public void shiftOffset(int index)
  {
  }

  protected int getXPosFromText(String toIndexString)
  {
    TypedLayout layout = new TextLayoutImpl(toIndexString, font, TextPanel.getRenderContext());
    int x = getWidthDimension(layout) + SIDE_TEXT_MARGIN;
    if (x < SIDE_TEXT_MARGIN)
      x = SIDE_TEXT_MARGIN;
    return x;
  }

  @Override
  public Dimension calculateTextDimensions()
  {
    if (getText() != null && getText().length() > 0)
    {
      int height = 0;
      int width = 0;
      for (TypedLayout layout : getTextLayouts())
      {
        height += (int) (getHeightDimension(layout) + layout.getLeading() + .5);
        int dimWidth = getWidthDimension(layout);
        if (dimWidth > width)
          width = dimWidth;
      }
      return new Dimension(width, height);
    }
    return null;
  }

  @Override
  public ArrayList<TypedLayout> getTextLayouts()
  {
    if (getText().length() == 0)
      return null;
    else
    {
      if (textLayouts == null || isThereSomeDifferentText())
      {
        setLastLayedOutText(getText());
        parseTextForMultipleLayouts();
      }
      return textLayouts;
    }
  }

  @Override
  public ArrayList<Rectangle> getSelectionRegions()
  {
    int cursorLine = getLineNumberOfIndex(cursorIndex);
    int selectionLine = getLineNumberOfIndex(selectionIndex);
    if (cursorLine == selectionLine)
      return selectionRegionsForSingleLine();
    else if (selectionLine > cursorLine)
      return selectionRegionsForMultipleLines(cursorLine, getXPosFromIndex(cursorIndex), selectionLine, getXPosFromIndex(selectionIndex));
    else
      return selectionRegionsForMultipleLines(selectionLine, getXPosFromIndex(selectionIndex), cursorLine, getXPosFromIndex(cursorIndex));
  }

  private ArrayList<Rectangle> selectionRegionsForSingleLine()
  {
    int cursorX = getXPosFromIndex(cursorIndex);
    int selectionX = getXPosFromIndex(selectionIndex);
    int lineHeight = getTotalHeightOfLineWithLeadingMargin(0);    
    int yPos = getYPosFromIndex(cursorIndex) - TOP_MARGIN - calculateYOffset();
    ArrayList<Rectangle> regions = new ArrayList<Rectangle>() ;
    if (cursorIndex > selectionIndex)
      regions.add(new Box(selectionX , yPos, cursorX - selectionX, lineHeight));
    else
      regions.add(new Box(cursorX , yPos, selectionX - cursorX, lineHeight));
    return regions;
  }

  private ArrayList<Rectangle> selectionRegionsForMultipleLines(int startingLine, int startingX, int endingLine, int endingX)
  {
    ArrayList<Rectangle> regions = new ArrayList<Rectangle>();
    int lineHeight = getTotalHeightOfLineWithLeadingMargin(0);
    int yPos = lineHeight * startingLine - calculateYOffset();
    regions.add(new Box(startingX, yPos, myPanel.getWidth() - startingX, lineHeight));
    yPos += lineHeight;
    for (int i = startingLine + 1; i < endingLine; i++)
    {
      regions.add(new Box(SIDE_TEXT_MARGIN, yPos, myPanel.getWidth()-SIDE_TEXT_MARGIN, lineHeight));
      yPos += lineHeight;
    }
    regions.add(new Box(SIDE_TEXT_MARGIN, yPos, endingX -SIDE_TEXT_MARGIN, lineHeight));
    return regions;
  }

  @Override
  public boolean isBoxFull()
  {
    if (getText().length() > 0)
      return (myPanel.getHeight() - TextModel.TOP_MARGIN * 2 <= calculateTextDimensions().height);
    return false;
  }

  @Override
  public boolean isMoveUpEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_UP && getLineNumberOfIndex(cursorIndex) > 0;
  }

  @Override
  public boolean isMoveDownEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_DOWN && getLineNumberOfIndex(cursorIndex) < textLayouts.size() - 1;
  }

  @Override
  public int getTopOfStartPositionForCursor()
  {
    return getYPosFromIndex(cursorIndex) - calculateYOffset() - TOP_MARGIN;
  }

  @Override
  public int getBottomPositionForCursor()
  {
    return getTopOfStartPositionForCursor() + getHeightOfCurrentLine() - 1;
  }

  @Override
  public int getIndexOfLastCharInLine(int line)
  {
    getTextLayouts();
    int numberOfCharacters = 0;
    for (int i = 0; i <= line; i++)
      numberOfCharacters += textLayouts.get(i).getText().length();
    if (line != textLayouts.size() - 1)
      numberOfCharacters--;
    return numberOfCharacters;
  }

  @Override
  public void calculateLeftShiftingOffset()
  {
  }

  @Override
  public int calculateYOffset()
  {
    int yPos = getYPosFromIndex(cursorIndex);
    int lineHeight = getTotalHeightOfLineWithLeadingMargin(0);
    int panelHeight = myPanel.getHeight();
    while(yPos <= yOffset)
    {
      yOffset -= lineHeight;
    if(yOffset < 0)
       yOffset = 0;
    }

    while((yPos + lineHeight) > yOffset + panelHeight)
    {
      yOffset += lineHeight;
    }
    return yOffset;
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
    int lastCharIndex =0, newLineCharIndex = 0;
    while (breaker.getPosition() < iterator.getEndIndex())
    {
      lastCharIndex = addANewLayoutForTheNextLine(breaker, lastCharIndex, newLineCharIndex);
      if (layoutEndedOnNewLineChar(lastCharIndex, newLineCharIndex))
        newLineCharIndex++;
    }
    addBlankLayoutIfLastLineIsEmpty();

    return textLayouts;
  }

  private void addBlankLayoutIfLastLineIsEmpty()
  {
    if(getText().length() > 0 && isTheVeryLastCharANewLineChar())
      textLayouts.add(new TextLayoutImpl("", font, TextPanel.getRenderContext()));
  }

  private int addANewLayoutForTheNextLine(LineBreakMeasurer breaker, int lastCharIndex, int newLineCharIndex)
  {
    int firstCharIndex = lastCharIndex;
    lastCharIndex = firstCharIndex + getNextLayout(breaker, newLineCharIndex).getCharacterCount();
    String layoutText = getText().substring(firstCharIndex, lastCharIndex);
    textLayouts.add(new TextLayoutImpl(layoutText, font, TextPanel.getRenderContext()));
    return lastCharIndex;
  }

  private boolean layoutEndedOnNewLineChar(int lastCharIndex, int returnCharIndex)
  {
    return thereAreMoreReturnCharacters(returnCharIndex) && lastCharIndex == newLineCharIndices.get(returnCharIndex) + 1;
  }

  private TextLayout getNextLayout(LineBreakMeasurer breaker, int returnCharIndex)
  {
    TextLayout layout;
    if (thereAreMoreReturnCharacters(returnCharIndex))
      layout = breaker.nextLayout(myPanel.getWidth() - SIDE_DETECTION_MARGIN, newLineCharIndices.get(returnCharIndex) + 1, false);
    else
      layout = breaker.nextLayout(myPanel.getWidth() - SIDE_DETECTION_MARGIN);
    return layout;
  }

  private AttributedCharacterIterator getIterator()
  {
    AttributedString attrString = new AttributedString(getText());
    attrString.addAttribute(TextAttribute.FONT, font);
    return attrString.getIterator();
  }

  private boolean isTheVeryLastCharANewLineChar()
  {
    return getText().charAt(getText().length()-1) == '\n';
  }

  private boolean thereAreMoreReturnCharacters(int returnCharIndex)
  {
    return newLineCharIndices != null && returnCharIndex < newLineCharIndices.size();
  }

  public ArrayList<Integer> findNewLineCharIndices(String text)
  {
    ArrayList<Integer> indices = new ArrayList<Integer>();
    for (int i = 0; i < text.length(); i++)
    {
      if (text.charAt(i) == '\n' || text.charAt(i) == '\r')
        indices.add(i);
    }
    return indices;
  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
  }
}
