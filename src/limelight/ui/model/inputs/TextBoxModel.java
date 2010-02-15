package limelight.ui.model.inputs;

import limelight.ui.TextLayoutImpl;
import limelight.ui.TypedLayout;
import limelight.ui.model.TextPanel;
import limelight.util.Box;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;


public class TextBoxModel extends TextModel
{

  public TextBoxModel(TextInputPanel myBox)
  {
    this.myPanel = myBox;
    cursorX = SIDE_TEXT_MARGIN;
    selectionOn = false;
    font = new Font("Arial", Font.PLAIN, 12);
    selectionStartX = 0;
    cursorIndex = 0;
    selectionIndex = 0;
    xOffset = 0;
  }

  protected int getXPosFromText(String toIndexString)
  {
    TypedLayout layout = new TextLayoutImpl(toIndexString, font, TextPanel.getRenderContext());
    return getWidthDimension(layout) + SIDE_TEXT_MARGIN - xOffset;

  }

  public void shiftOffset(int index)
  {
    int xPos = getXPosFromIndex(index);
    System.out.println("cursorX = " + xPos);
    System.out.println("cursorIndex = " + index);
    System.out.println("xOffset = " + xOffset);
    System.out.println("isCriticallyLeft() = " + isCriticallyLeft(xPos));
    System.out.println("isCriticallyRight() = " + isCriticallyRight(xPos));
    if (index == 0)
    {
      this.cursorX = SIDE_DETECTION_MARGIN;
      xOffset = 0;
    }
    else if (isCriticallyLeft(xPos))
    {
      calculateRightShiftingOffset();
    }
    else if (isCriticallyRight(xPos))
    {
      calculateLeftShiftingOffset();
    }
    this.cursorX = getXPosFromIndex(index);
  }

  @Override
  public boolean isCursorAtCriticalEdge(int xPos)
  {
    if (myPanel.getWidth() > calculateTextDimensions().width)
      return false;
    if (!isCriticallyRight(xPos) && !isCriticallyLeft(xPos))
      return false;
    return true;
  }

  private boolean isCriticallyLeft(int xPos)
  {
    return (xPos <= SIDE_DETECTION_MARGIN && xOffset != 0);
  }

  private boolean isCriticallyRight(int xPos)
  {
    return (xPos >= myPanel.getWidth() - SIDE_DETECTION_MARGIN);// && (xOffset + cursorX <= calculateTextDimensions().width));
  }

  private void calculateRightShiftingOffset()
  {
    String rightShiftingText = getText().substring(0, cursorIndex);
    TypedLayout layout = new TextLayoutImpl(rightShiftingText, font, TextPanel.getRenderContext());
    int textWidth = getWidthDimension(layout) + getTerminatingSpaceWidth(rightShiftingText);
    if (textWidth > getPanelWidth() / 2)
      xOffset -= getPanelWidth() / 2;
    else
      xOffset -= textWidth;
    if (xOffset < 0)
      xOffset = 0;
  }

  public void calculateLeftShiftingOffset()
  {
    int defaultOffset = SIDE_TEXT_MARGIN + SIDE_DETECTION_MARGIN;
    if (cursorIndex == text.length())
    {
      int textWidth = calculateTextDimensions().width;
      if (textWidth > getPanelWidth())
      {
        xOffset = textWidth - getPanelWidth() + defaultOffset;
      }
    }
    else
    {
      String leftShiftingText;
      if (cursorIndex == text.length() - 1)
        leftShiftingText = Character.toString(text.charAt(cursorIndex));
      else
        leftShiftingText = getText().substring(cursorIndex, text.length() - 1);
      TypedLayout layout = new TextLayoutImpl(leftShiftingText, font, TextPanel.getRenderContext());
      int textWidth = getWidthDimension(layout) + getTerminatingSpaceWidth(leftShiftingText);
      if (textWidth > getPanelWidth() / 2)
        xOffset += getPanelWidth() / 2;
      else
        xOffset += textWidth;
      System.out.println("textWidth = " + textWidth);
    }
  }

  public Dimension calculateTextDimensions()
  {
    if (getText() != null && getText().length() > 0)
    {
      int height = 0;
      int width = 0;
      for (TypedLayout layout : getTextLayouts())
      {
        height += (int) (getHeightDimension(layout) + layout.getLeading() + .5);
        width += getWidthDimension(layout);
      }
      return new Dimension(width, height);
    }
    return null;
  }

  public ArrayList<TypedLayout> getTextLayouts()
  {
    if (getText().length() == 0)
      return null;
    else
    {

      if (textLayouts == null || isThereSomeDifferentText())
      {
        setLastLayedOutText(getText());
        textLayouts = new ArrayList<TypedLayout>();
        textLayouts.add(new TextLayoutImpl(getText(), font, TextPanel.getRenderContext()));
      }
      return textLayouts;
    }
  }

  public ArrayList<Rectangle> getSelectionRegions()
  {
    if (getText().length() > 0)
      shiftOffset(cursorIndex);
    int x1 = getXPosFromIndex(cursorIndex);
    int x2 = getXPosFromIndex(selectionIndex);
    int edgeSelectionExtension = 0;

    if (x1 <= SIDE_TEXT_MARGIN || x2 <= SIDE_TEXT_MARGIN)
      edgeSelectionExtension = SIDE_TEXT_MARGIN;
    ArrayList<Rectangle> regions = new ArrayList<Rectangle>();
    if (x1 > x2)
      regions.add(new Box(x2 - edgeSelectionExtension, TOP_MARGIN, x1 - x2 + edgeSelectionExtension, getPanelHeight() - TOP_MARGIN * 2));
    else
      regions.add(new Box(x1 - edgeSelectionExtension, TOP_MARGIN, x2 - x1 + edgeSelectionExtension, getPanelHeight() - TOP_MARGIN * 2));
    return regions;
  }

  @Override
  public int calculateYOffset()
  {
    return 0;
  }

  public boolean isBoxFull()
  {
    if (getText().length() > 0)
      return (myPanel.getWidth() - TextModel.SIDE_DETECTION_MARGIN * 2 <= calculateTextDimensions().width);
    return false;
  }

  @Override
  public boolean isMoveUpEvent(int keyCode)
  {
    return false;
  }

  @Override
  public boolean isMoveDownEvent(int keyCode)
  {
    return false;
  }

  @Override
  public int getTopOfStartPositionForCursor()
  {
    return TOP_MARGIN;
  }

  @Override
  public int getBottomPositionForCursor()
  {
    return myPanel.getHeight() - TOP_MARGIN * 2;
  }

  @Override
  public int getIndexOfLastCharInLine(int line)
  {
    return text.length();
  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
    //this doesn't have to do anything...
  }
}
