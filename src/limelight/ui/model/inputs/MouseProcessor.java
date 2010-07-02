//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.model.inputs.offsetting.XOffsetStrategy;
import limelight.ui.model.inputs.offsetting.YOffsetStrategy;
import limelight.ui.text.TextLocation;
import limelight.ui.text.TypedLayout;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MouseProcessor
{
  TextModel model;
  public long lastClickTime;
  public boolean doubleClickOn;
  private final int MULTI_CLICK_MILLIS = 300;

  public MouseProcessor(TextModel model)
  {
    this.model = model;
  }

  public void processMousePressed(MouseEvent e)
  {
    Point pressPoint = getRelativeMouseLocation(e);

    TextLocation location = model.getLocationAt(pressPoint);
    model.setSelectionOn(true);
    model.setSelectionLocation(location);
    model.setCaretLocation(location, XOffsetStrategy.FITTING, YOffsetStrategy.FITTING);

    makeExtraSelectionOnMultiClick();

    lastClickTime = System.currentTimeMillis();
  }

  private Point getRelativeMouseLocation(MouseEvent e)
  {
    Point location = model.getContainer().getAbsoluteLocation();
    return new Point(e.getPoint().x - location.x, e.getPoint().y - location.y);
  }

  public void makeExtraSelectionOnMultiClick()
  {
    if(lastClickTime >= (System.currentTimeMillis() - MULTI_CLICK_MILLIS))
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
    Point mousePoint = getRelativeMouseLocation(e);

    ArrayList<TypedLayout> lines = model.getLines();
    int tempIndex = model.getLocationAt(mousePoint).toIndex(lines);
    // TODO MDM - This needs work.  Ideally, the text will scroll smoothly, a pixel at a time, without the mouse moving.  The scoll speed increased as the mouse moves away.  
    if(mousePoint.x < 3 && tempIndex > 0)
      tempIndex--;
    else if(mousePoint.x > (model.getContainer().getWidth() - 3) && tempIndex < model.getText().length())
      tempIndex++;

    if(doubleClickOn)
      selectWord(tempIndex);
    else
      model.setCaretLocation(TextLocation.fromIndex(lines, tempIndex), XOffsetStrategy.FITTING, YOffsetStrategy.FITTING);
  }

  private void selectWord(int tempIndex)
  {
    new WordSelector(tempIndex).processWordSelection();
  }

  public void processMouseReleased(MouseEvent e)
  {
    Point mousePoint = getRelativeMouseLocation(e);
    if(!doubleClickOn)
    {
      model.setCaretLocation(model.getLocationAt(mousePoint));
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
      model.setCaretLocation(TextLocation.fromIndex(model.getLines(), newHead), XOffsetStrategy.FITTING, YOffsetStrategy.FITTING);
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
