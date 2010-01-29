package limelight.ui.model.inputs;

import java.awt.event.MouseEvent;
import java.awt.font.TextHitInfo;
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
    TextHitInfo hitInfo = boxInfo.getTextLayout().hitTestChar(x + boxInfo.getXOffset(), y);
    int index = hitInfo.getCharIndex();
    if (isHitOnTheRightEdge(hitInfo, index))
      index += 1;
    return index;
  }

  private boolean isHitOnTheRightEdge(TextHitInfo hitInfo, int index)
  {
    return index < hitInfo.getInsertionIndex() && index == boxInfo.text.length() - 1;
  }

  public void processMousePressed(MouseEvent e)
  {
    if (isMouseEventInBox(e))
    {
      int myX = e.getX() - boxInfo.getPanelAbsoluteLocation().x;
      int myY = e.getY() - boxInfo.getPanelAbsoluteLocation().y;
      boxInfo.selectionOn = true;
      boxInfo.setSelectionIndex(calculateMouseClickIndex(myX, myY));
      boxInfo.setCursorIndex(boxInfo.getSelectionIndex());
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
    boxInfo.setCursorIndex(boxInfo.text.length());
  }

  private void selectWordOnDoubleClick()
  {
    boxInfo.setSelectionIndex(boxInfo.findWordsLeftEdge(boxInfo.getCursorIndex()));
    boxInfo.setCursorIndex(boxInfo.findWordsRightEdge(boxInfo.getCursorIndex()));
    doubleClickOn = true;
  }

  public void processMouseDragged(MouseEvent e)
  {
    int myX = e.getX() - boxInfo.getPanelAbsoluteLocation().x;
    int myY = e.getY() - boxInfo.getPanelAbsoluteLocation().y;
    int tempIndex = calculateMouseClickIndex(myX, myY);
    if (doubleClickOn)
      selectWord(tempIndex);
    else
      boxInfo.setCursorIndex(tempIndex);
  }

  private void selectWord(int tempIndex)
  {
    new WordSelector(tempIndex).invoke();
  }

  public void processMouseReleased(MouseEvent e)
  {
    int myX = e.getX() - boxInfo.getPanelAbsoluteLocation().x;
    int myY = e.getY() - boxInfo.getPanelAbsoluteLocation().y;
    if (!doubleClickOn)
    {
      boxInfo.setCursorIndex(calculateMouseClickIndex(myX, myY));
      if (boxInfo.getCursorIndex() == boxInfo.getSelectionIndex())
        boxInfo.selectionOn = false;
    }
  }

  private class WordSelector
  {
    private int mouseIndex;

    public WordSelector(int mouseIndex)
    {
      this.mouseIndex = mouseIndex;
    }

    public void makeWordSelection()
    {

      switch (calculateSelectionDispatchIndex())
      {
        case 0:
          extendSelectionToRight();
          break;
        case 1:
          extendSelectionToRight();
          break;
        case 2:
          moveLeftFacingHead();
          break;
        case 3:
          swapSelectionDirectionAndExtendToRight();
          break;
        case 4:
          swapSelectionDirectionAndExtendToLeft();
          break;
        case 5:
          moveRightFacingHead();
          break;
        case 6:
          extendSelectionToLeft();
          break;
      }
    }

    private int calculateSelectionDispatchIndex()
    {
      int dispatchIndex = 0;
      if (mouseIndex > boxInfo.getSelectionIndex())
        dispatchIndex += 1;
      if (boxInfo.getCursorIndex() < boxInfo.getSelectionIndex())
        dispatchIndex += 2;
      if (mouseIndex < boxInfo.findWordsLeftEdge(boxInfo.getCursorIndex()))
        dispatchIndex += 4;
      return dispatchIndex;
    }

    private void swapSelectionDirectionAndExtendToLeft()
    {
      turnAround();
      boxInfo.setCursorIndex(boxInfo.findWordsLeftEdge(mouseIndex));
    }

    private void turnAround()
    {
      boxInfo.setSelectionIndex(boxInfo.getCursorIndex());
    }

    private void moveRightFacingHead()
    {
      boxInfo.setCursorIndex(boxInfo.findWordsRightEdge(mouseIndex));
    }

    private void extendSelectionToRight()
    {
      int newHead = boxInfo.findWordsRightEdge(mouseIndex);
      repositionHead(newHead);
    }

    private void repositionHead(int newHead)
    {
      boxInfo.setCursorIndex(newHead);
    }

    private void extendSelectionToLeft()
    {
      boxInfo.setCursorIndex(boxInfo.findWordsLeftEdge(mouseIndex));
    }

    private void moveLeftFacingHead()
    {
      boxInfo.setCursorIndex(boxInfo.findWordsLeftEdge(mouseIndex));
    }

    private void swapSelectionDirectionAndExtendToRight()
    {
      turnAround();
      boxInfo.setCursorIndex(boxInfo.findWordsRightEdge(mouseIndex));
    }


    public void invoke()
    {
      boolean rightOfTail = isRightOfTail();

      boolean selectionFacingRight = isSelectionFacingRight();
      boolean turningAround = false;

      boolean isMouseTrailingTheTail = selectionFacingRight && !rightOfTail || !selectionFacingRight && rightOfTail;
      if(isMouseTrailingTheTail)
      {
        turnAround();
        turningAround = true;
      }

      boolean isHeadingToTheRight = selectionFacingRight && !turningAround || !selectionFacingRight && turningAround;
      if(isHeadingToTheRight)
        repositionHead(boxInfo.findWordsRightEdge(mouseIndex));
      else
        repositionHead(boxInfo.findWordsLeftEdge(mouseIndex));

//      if(selectionFacingRight && !turningAround)
//          newHead = boxInfo.findWordsRightEdge(mouseIndex);
//
//      if (isSelectionFacingRight())
//      {
//        if (!rightOfTail)
//        {
//          turnAround();
//          newHead = boxInfo.findWordsLeftEdge(mouseIndex);
//        }
//        else
//          newHead = boxInfo.findWordsRightEdge(mouseIndex);
//      }
//      else
//      {
//        if (rightOfTail)
//        {
//          turnAround();
//          newHead = boxInfo.findWordsRightEdge(mouseIndex);
//        }
//        else
//          newHead = boxInfo.findWordsLeftEdge(mouseIndex);
//      }
//      repositionHead(newHead);

//      if (rightOfHead)
//      {
//        if (!rightOfTail)
//          repositionHead(boxInfo.findWordsLeftEdge(mouseIndex));
//        else
//        {
//          if (!selectionFacingRight)
//            turnAround();
//          repositionHead(boxInfo.findWordsRightEdge(mouseIndex));
//        }
//      }
//      else
//      {
//        if (rightOfTail)
//          repositionHead(boxInfo.findWordsRightEdge(mouseIndex));
//        else
//        {
//          if (selectionFacingRight)
//            turnAround();
//          repositionHead(boxInfo.findWordsLeftEdge(mouseIndex));
//        }
//      }
    }

    private boolean isRightOfHead()
    {
      return mouseIndex > boxInfo.findWordsRightEdge(boxInfo.cursorIndex);
    }

    private boolean isSelectionFacingRight()
    {
      return boxInfo.cursorIndex > boxInfo.selectionIndex;
    }

    private boolean isRightOfTail()
    {
      return mouseIndex > boxInfo.selectionIndex;
    }
  }
}