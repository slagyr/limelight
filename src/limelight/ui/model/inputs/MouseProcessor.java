package limelight.ui.model.inputs;

import java.awt.event.MouseEvent;
import java.awt.font.TextHitInfo;

public class MouseProcessor
{

  TextModel boxInfo;

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
    }
  }

  public void processMouseDragged(MouseEvent e)
  {
    int myX = e.getX() - boxInfo.getPanelAbsoluteLocation().x;
    int myY = e.getY() - boxInfo.getPanelAbsoluteLocation().y;
    boxInfo.cursorIndex = calculateMouseClickIndex(myX, myY);
  }

  public void processMouseReleased(MouseEvent e)
  {
    int myX = e.getX() - boxInfo.getPanelAbsoluteLocation().x;
    int myY = e.getY() - boxInfo.getPanelAbsoluteLocation().y;
    boxInfo.cursorIndex = calculateMouseClickIndex(myX, myY);
    if (boxInfo.cursorIndex == boxInfo.selectionIndex)
      boxInfo.selectionOn = false;
  }
}
