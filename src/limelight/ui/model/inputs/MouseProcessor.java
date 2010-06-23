//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.TypedLayout;

import java.awt.event.MouseEvent;
import java.awt.font.TextHitInfo;
import java.util.ArrayList;
import java.util.Date;

public class MouseProcessor
{
  TextModel boxInfo;
  public long lastClickTime;
  public boolean doubleClickOn;

  public MouseProcessor(TextModel boxInfo)
  {
    this.boxInfo = boxInfo;
  }

  public boolean isMouseEventInBox(MouseEvent e)
  {
    int myX = e.getX() - boxInfo.getPanelAbsoluteLocation().x;
    int myY = e.getY() - boxInfo.getPanelAbsoluteLocation().y;
    return isWithinMyXRange(myX) && isWithinMyYRange(myY);
  }

  private boolean isWithinMyYRange(int myY)
  {
    return myY > 0 && myY < boxInfo.getPanelHeight();
  }

  private boolean isWithinMyXRange(int myX)
  {
    return myX > 0 && myX < boxInfo.getPanelWidth();
  }

  public int calculateMouseClickIndex(int x, int y)
  {
    int index = getIndexByLoopingThroughLayouts(x, y);
    if (isMouseXPastLastCharacterAndNotOnNewLine(x, index))
      index += 1;
    return index;
  }

  private int getIndexByLoopingThroughLayouts(int x, int y)
  {
    ArrayList<TypedLayout> layouts = boxInfo.getTypedLayouts();
    if (layouts == null)
      return 0;
    int layoutIndex = 0;
    int charCount = 0;
    int layoutYPosition = boxInfo.getTotalHeightOfLineWithLeadingMargin(layoutIndex);
    while (notPastTheLastLayout(y,layoutIndex, layoutYPosition))
    {
      charCount += layouts.get(layoutIndex).getText().length();
      layoutIndex++;
      layoutYPosition += boxInfo.getTotalHeightOfLineWithLeadingMargin(layoutIndex);
    }
    TextHitInfo hitInfo = layouts.get(layoutIndex).hitTestChar(x + boxInfo.getOffset().x, y);
    return hitInfo.getCharIndex() + charCount;
  }

  private boolean notPastTheLastLayout(int y, int i, int layoutPosition)
  {
    return i < boxInfo.getTypedLayouts().size() - 1 && y + boxInfo.getOffset().y > layoutPosition;
  }

  private boolean isMouseXPastLastCharacterAndNotOnNewLine(int x, int index)
  {
    if(boxInfo.getText() == null || boxInfo.getText().length() == 0)
      return false;
    return x > boxInfo.getXPosFromIndex(index) && index == boxInfo.getText().length() - 1 && boxInfo.getText().charAt(index) != '\n';
  }


  public void processMousePressed(MouseEvent e)
  {
    if (isMouseEventInBox(e))
    {
      int myX = e.getX() - boxInfo.getPanelAbsoluteLocation().x;
      int myY = e.getY() - boxInfo.getPanelAbsoluteLocation().y;
      boxInfo.setSelectionOn(true);
      boxInfo.setSelectionIndex(calculateMouseClickIndex(myX, myY));
      boxInfo.setCaretIndex(boxInfo.getSelectionIndex());
      makeExtraSelectionOnMultiClick();
      lastClickTime = (new Date()).getTime();
    }
  }

  public void makeExtraSelectionOnMultiClick()
  {
    if (lastClickTime >= (new Date()).getTime() - 300)
    {
      if (doubleClickOn)
        selectAllOnTripleClick();
      else
        selectWordOnDoubleClick();
    }
    else
      doubleClickOn = false;
  }

  private void selectAllOnTripleClick()
  {
    boxInfo.setSelectionIndex(0);
    boxInfo.setCaretIndex(boxInfo.getText().length());
  }

  private void selectWordOnDoubleClick()
  {
    boxInfo.setSelectionIndex(boxInfo.findWordsLeftEdge(boxInfo.getCaretIndex()));
    boxInfo.setCaretIndex(boxInfo.findWordsRightEdge(boxInfo.getCaretIndex()));
    doubleClickOn = true;
  }

  public void processMouseDragged(MouseEvent e)
  {
    int myX = e.getX() - boxInfo.getPanelAbsoluteLocation().x;
    int myY = e.getY() - boxInfo.getPanelAbsoluteLocation().y;

    int tempIndex = calculateMouseClickIndex(myX, myY);
    if (boxInfo.isCursorAtCriticalEdge(myX))
    {
      if (boxInfo.getXPosFromIndex(tempIndex) < myX && tempIndex < boxInfo.getText().length())
        tempIndex++;
      tempIndex--;
    }
    if (doubleClickOn)
      selectWord(tempIndex);

    else
      boxInfo.setCaretIndex(tempIndex);
  }

  private void selectWord(int tempIndex)
  {
    new WordSelector(tempIndex).processWordSelection();
  }

  public void processMouseReleased(MouseEvent e)
  {
    int myX = e.getX() - boxInfo.getPanelAbsoluteLocation().x;
    int myY = e.getY() - boxInfo.getPanelAbsoluteLocation().y;
    if (!doubleClickOn)
    {
      boxInfo.setCaretIndex(calculateMouseClickIndex(myX, myY));
      if (boxInfo.getCaretIndex() == boxInfo.getSelectionIndex())
        boxInfo.setSelectionOn(false);
    }
  }

  private class WordSelector
  {
    private int mouseIndex;

    public WordSelector(int mouseIndex)
    {
      this.mouseIndex = mouseIndex;
    }

    public void processWordSelection()
    {
      boolean rightOfTail = isRightOfTail();

      boolean selectionFacingRight = isSelectionFacingRight();

      boolean isMouseTrailingTheTail = selectionFacingRight && !rightOfTail || !selectionFacingRight && rightOfTail;
      if (isMouseTrailingTheTail)
        turnAround();

      if (rightOfTail)
        repositionHead(boxInfo.findWordsRightEdge(mouseIndex));
      else
        repositionHead(boxInfo.findWordsLeftEdge(mouseIndex));
    }

    private void turnAround()
    {
      boxInfo.setSelectionIndex(boxInfo.getCaretIndex());
    }

    private void repositionHead(int newHead)
    {
      boxInfo.setCaretIndex(newHead);
    }

    private boolean isSelectionFacingRight()
    {
      return boxInfo.getCaretIndex() > boxInfo.getSelectionIndex();
    }

    private boolean isRightOfTail()
    {
      return mouseIndex > boxInfo.getSelectionIndex();
    }
  }
}
