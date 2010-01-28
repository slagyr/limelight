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
      boxInfo.selectionIndex = calculateMouseClickIndex(myX, myY);
      boxInfo.cursorIndex = boxInfo.selectionIndex;
      selectWordOnDoubleClick();
      lastClickTime = (new Date()).getTime();
    }
  }

  public void selectWordOnDoubleClick()
  {
    if (lastClickTime >= (new Date()).getTime() - 300)
    {
      boxInfo.selectionIndex = boxInfo.findWordsLeftEdge(boxInfo.cursorIndex);
      boxInfo.cursorIndex = boxInfo.findWordsRightEdge(boxInfo.cursorIndex);
      doubleClickOn = true;
    }
    else
      doubleClickOn = false;
  }

  public void processMouseDragged(MouseEvent e)
  {
    int myX = e.getX() - boxInfo.getPanelAbsoluteLocation().x;
    int myY = e.getY() - boxInfo.getPanelAbsoluteLocation().y;
    int tempIndex = calculateMouseClickIndex(myX, myY);
    if (doubleClickOn)
      selectWord(tempIndex);
    else
      boxInfo.cursorIndex = tempIndex;
  }

  private void selectWord(int tempIndex)
  {
    if (tempIndex > boxInfo.findWordsRightEdge(boxInfo.cursorIndex))
    {
      boxInfo.cursorIndex = boxInfo.findWordsRightEdge(tempIndex);
    }
    else if(tempIndex < boxInfo.findWordsLeftEdge(boxInfo.cursorIndex))
    {
      if ( tempIndex < boxInfo.selectionIndex)
      boxInfo.cursorIndex = boxInfo.findWordsLeftEdge(tempIndex);
    }
  }

  public void processMouseReleased(MouseEvent e)
  {
    int myX = e.getX() - boxInfo.getPanelAbsoluteLocation().x;
    int myY = e.getY() - boxInfo.getPanelAbsoluteLocation().y;
    if (!doubleClickOn)
    {
      boxInfo.cursorIndex = calculateMouseClickIndex(myX, myY);
      if (boxInfo.cursorIndex == boxInfo.selectionIndex)
        boxInfo.selectionOn = false;
    }
  }
}
