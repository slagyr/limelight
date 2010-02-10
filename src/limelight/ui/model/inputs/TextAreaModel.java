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
  }

  @Override
  public void shiftOffset()
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
        textLayouts = parseTextForMultipleLayouts(getText());
      }
      return textLayouts;
    }
  }

  @Override
  public ArrayList<Rectangle> getSelectionRegions()
  {
    int cursorX = getXPosFromIndex(cursorIndex);
    int selectionX = getXPosFromIndex(selectionIndex);
    int yPos = 0;
    ArrayList<Rectangle> regions = new ArrayList<Rectangle>();
    int cursorLine = getLineNumberOfIndex(cursorIndex);
    int selectionLine = getLineNumberOfIndex(selectionIndex);
    int lineHeight = getTotalHeightOfLineWithLeadingMargin(0);
    if (cursorLine == selectionLine)
    {
      yPos += lineHeight * cursorLine;
      if (cursorIndex > selectionIndex)
        regions.add(new Box(selectionX , yPos, cursorX - selectionX, lineHeight));
      else
        regions.add(new Box(cursorX , yPos, selectionX - cursorX, lineHeight));
      return regions;
    }
    else if (selectionLine > cursorLine)
    {
      yPos += lineHeight * cursorLine;
      regions.add(new Box(cursorX , yPos, myPanel.getWidth() - cursorX, lineHeight));
      yPos += lineHeight;
      for (int i = cursorLine + 1; i < selectionLine; i++)
      {
        regions.add(new Box(3, yPos, myPanel.getWidth()-3, lineHeight));
        yPos += lineHeight;
      }
      regions.add(new Box(3, yPos, selectionX -3, lineHeight));
    }
    else
    {
      yPos += lineHeight * selectionLine;
      regions.add(new Box(selectionX , yPos, myPanel.getWidth() - selectionX, lineHeight));
      yPos += lineHeight;
      for (int i = selectionLine + 1; i < cursorLine; i++)
      {
        regions.add(new Box(3, yPos, myPanel.getWidth()-3, lineHeight));
        yPos += lineHeight;
      }
      regions.add(new Box(3, yPos, cursorX -3, lineHeight));
    }

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
    return getYPosFromIndex(cursorIndex);
  }

  @Override
  public int getBottomPositionForCursor()
  {
    return getTopOfStartPositionForCursor() + getHeightOfCurrentLine() - TOP_MARGIN - 1;
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

  public ArrayList<TypedLayout> parseTextForMultipleLayouts(String text)
  {
    AttributedString attrString = new AttributedString(text);
    attrString.addAttribute(TextAttribute.FONT, font);
    AttributedCharacterIterator iterator = attrString.getIterator();

    ArrayList<Integer> returnCharIndices = findReturnCharIndices(text);
    ArrayList<TypedLayout> textLayouts = new ArrayList<TypedLayout>();

    LineBreakMeasurer breaker = new LineBreakMeasurer(iterator, TextPanel.getRenderContext());
    int firstCharIndex = 0, lastCharIndex, returnCharIndex = 0;
    String layoutText = "";
    while (breaker.getPosition() < iterator.getEndIndex())
    {
      TextLayout layout;
      if (isThereMoreReturnCharacters(returnCharIndices, returnCharIndex))
        layout = breaker.nextLayout(myPanel.getWidth() - SIDE_DETECTION_MARGIN, returnCharIndices.get(returnCharIndex) + 1, false);
      else
        layout = breaker.nextLayout(myPanel.getWidth() - SIDE_DETECTION_MARGIN);
      lastCharIndex = firstCharIndex + layout.getCharacterCount();
      if (isThereMoreReturnCharacters(returnCharIndices, returnCharIndex) && lastCharIndex == returnCharIndices.get(returnCharIndex) + 1)
        returnCharIndex++;
      layoutText = text.substring(firstCharIndex, lastCharIndex);
      textLayouts.add(new TextLayoutImpl(layoutText, font, TextPanel.getRenderContext()));
      firstCharIndex = lastCharIndex;

    }

    return textLayouts;
  }

  private boolean isThereMoreReturnCharacters(ArrayList<Integer> returnCharIndices, int returnCharIndex)
  {
    return returnCharIndices != null && returnCharIndex < returnCharIndices.size();
  }

  public ArrayList<Integer> findReturnCharIndices(String text)
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
