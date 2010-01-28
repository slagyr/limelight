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
    doubleClickOn = false;
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
    new WordSelector(tempIndex).makeProperWordSelection();
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

    public void makeProperWordSelection()
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
          reduceSelectionFromTheLeft();
          break;
        case 3:
          swapSelectionDirectionAndExtendToRight();
          break;
        case 4:
          swapSelectionDirectionAndExtendToLeft();
          break;
        case 5:
          reduceSelectionFromTheRight();
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
      boxInfo.setSelectionIndex(boxInfo.getCursorIndex());
      boxInfo.setCursorIndex(boxInfo.findWordsLeftEdge(mouseIndex));
    }

    private void reduceSelectionFromTheRight()
    {
      boxInfo.setCursorIndex(boxInfo.findWordsRightEdge(mouseIndex));
    }

    private void extendSelectionToLeft()
    {
      boxInfo.setCursorIndex(boxInfo.findWordsLeftEdge(mouseIndex));
    }

    private void reduceSelectionFromTheLeft()
    {
      boxInfo.setCursorIndex(boxInfo.findWordsLeftEdge(mouseIndex));
    }

    private void swapSelectionDirectionAndExtendToRight()
    {
      boxInfo.setSelectionIndex(boxInfo.getCursorIndex());
      boxInfo.setCursorIndex(boxInfo.findWordsRightEdge(mouseIndex));
    }

    private void extendSelectionToRight()
    {
      boxInfo.setCursorIndex(boxInfo.findWordsRightEdge(mouseIndex));
    }


//    public void invoke()
//    {
//      if (mouseIndex > boxInfo.findWordsRightEdge(boxInfo.cursorIndex))
//      {
//        if (mouseIndex > boxInfo.selectionIndex)
//        {
//          boxInfo.selectionIndex = boxInfo.cursorIndex;
//          boxInfo.cursorIndex = boxInfo.findWordsRightEdge(mouseIndex);
//        }
//        else if (boxInfo.cursorIndex < boxInfo.selectionIndex)
//          boxInfo.cursorIndex = boxInfo.findWordsLeftEdge(mouseIndex);
//        else
//          boxInfo.cursorIndex = boxInfo.findWordsRightEdge(mouseIndex);
//      }
//      else if (mouseIndex < boxInfo.findWordsLeftEdge(boxInfo.cursorIndex) && mouseIndex > boxInfo.selectionIndex)
//      {
//        boxInfo.cursorIndex = boxInfo.findWordsRightEdge(mouseIndex);
//      }
//      else if (mouseIndex < boxInfo.selectionIndex && mouseIndex < boxInfo.findWordsLeftEdge(boxInfo.cursorIndex))
//      {
//        if (boxInfo.cursorIndex > boxInfo.selectionIndex)
//          boxInfo.selectionIndex = boxInfo.cursorIndex;
//        boxInfo.cursorIndex = boxInfo.findWordsLeftEdge(mouseIndex);
//      }
//    }
  }
}
