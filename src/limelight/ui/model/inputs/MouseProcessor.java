//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.model.inputs.offsetting.XOffsetStrategy;
import limelight.ui.model.inputs.offsetting.YOffsetStrategy;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Date;

public class MouseProcessor
{
  TextModel model;
  public long lastClickTime;
  public boolean doubleClickOn;

  public MouseProcessor(TextModel model)
  {
    this.model = model;
  }

  public void processMousePressed(MouseEvent e)
  {
    Point location = getRelativeMouseLocation(e);

    int index = model.getIndexAt(location.x, location.y);
    model.setSelectionOn(true);
    model.setSelectionIndex(index);
    model.setCaretIndex(index, XOffsetStrategy.CENTERED, YOffsetStrategy.FITTING);  // TODO MDM - need a new strategy here...

    makeExtraSelectionOnMultiClick();

    lastClickTime = (new Date()).getTime();
  }

  private Point getRelativeMouseLocation(MouseEvent e)
  {
    Point location = model.getContainer().getAbsoluteLocation();
    return new Point(e.getPoint().x - location.x, e.getPoint().y - location.y);
  }

  public void makeExtraSelectionOnMultiClick()
  {
    if(lastClickTime >= (new Date()).getTime() - 300)
    {
      if(doubleClickOn)
        selectAllOnTripleClick();
      else
        selectWordOnDoubleClick();
    }
    else
      doubleClickOn = false;
  }

  private void selectAllOnTripleClick()
  {
    model.setSelectionIndex(0);
    model.setCaretIndex(model.getText().length());
  }

  private void selectWordOnDoubleClick()
  {
    model.setSelectionIndex(model.findWordsLeftEdge(model.getCaretIndex()));
    model.setCaretIndex(model.findWordsRightEdge(model.getCaretIndex()));
    doubleClickOn = true;
  }

  public void processMouseDragged(MouseEvent e)
  {
    Point location = getRelativeMouseLocation(e);

    int tempIndex = model.getIndexAt(location.x, location.y);
    if(model.isCursorAtCriticalEdge(location.x))
    {
      if(model.getXPosFromIndex(tempIndex) < location.x && tempIndex < model.getText().length())
        tempIndex++;
      tempIndex--;
    }
    if(doubleClickOn)
      selectWord(tempIndex);

    else
      model.setCaretIndex(tempIndex);
  }

  private void selectWord(int tempIndex)
  {
    new WordSelector(tempIndex).processWordSelection();
  }

  public void processMouseReleased(MouseEvent e)
  {
    int myX = e.getX() - model.getPanelAbsoluteLocation().x;
    int myY = e.getY() - model.getPanelAbsoluteLocation().y;
    if(!doubleClickOn)
    {
      model.setCaretIndex(model.getIndexAt(myX, myY));
      if(model.getCaretIndex() == model.getSelectionIndex())
        model.setSelectionOn(false);
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
      if(isMouseTrailingTheTail)
        turnAround();

      if(rightOfTail)
        repositionHead(model.findWordsRightEdge(mouseIndex));
      else
        repositionHead(model.findWordsLeftEdge(mouseIndex));
    }

    private void turnAround()
    {
      model.setSelectionIndex(model.getCaretIndex());
    }

    private void repositionHead(int newHead)
    {
      model.setCaretIndex(newHead);
    }

    private boolean isSelectionFacingRight()
    {
      return model.getCaretIndex() > model.getSelectionIndex();
    }

    private boolean isRightOfTail()
    {
      return mouseIndex > model.getSelectionIndex();
    }
  }
}
