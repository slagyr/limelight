//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.*;
import limelight.ui.Panel;
import limelight.ui.events.*;
import limelight.ui.events.Event;
import limelight.ui.model.inputs.offsetting.XOffsetStrategy;
import limelight.ui.model.inputs.offsetting.YOffsetStrategy;
import limelight.ui.text.TextLocation;
import limelight.ui.text.TypedLayout;

import java.awt.*;
import java.util.ArrayList;

public class TextPanelMouseProcessor
{
  TextModel model;
  public long lastClickTime;
  public boolean inWordSelectionMode;

  public TextPanelMouseProcessor(TextModel model)
  {
    this.model = model;
  }

  public void processMousePressed(MousePressedEvent e)
  {
    final Panel panel = e.getRecipient();
    inWordSelectionMode = false;

    TextLocation location = model.getLocationAt(e.getLocation());
    model.startSelection(location);
    model.setCaretLocation(location, XOffsetStrategy.FITTING, YOffsetStrategy.FITTING);
    model.setCaretOn(true);

    handleMultipleClicks(e);

    panel.markAsDirty();
    panel.getRoot().getKeyListener().focusOn(panel);

    lastClickTime = System.currentTimeMillis();
  }

  private void handleMultipleClicks(MousePressedEvent e)
  {
    if(e.getClickCount() == 2)
    {
      inWordSelectionMode = true;
      model.setSelectionLocation(model.findWordsLeftEdge(model.getCaretLocation()));
      model.setCaretLocation(model.findWordsRightEdge(model.getCaretLocation()));
    }
    else if(e.getClickCount() == 3)
    {
      model.selectAll();
    }
  }

  public void processMouseDragged(MouseDraggedEvent e)
  {
    Point mousePoint = e.getLocation();

    ArrayList<TypedLayout> lines = model.getLines();
    TextLocation tempLocation = model.getLocationAt(mousePoint);

    // TODO MDM - This needs work.  Ideally, the text will scroll smoothly, a pixel at a time, without the mouse moving.  The scoll speed increased as the mouse moves away.
    if(mousePoint.x < 3 && tempLocation.index > 0)
      tempLocation = tempLocation.moved(lines, -1);
    else if(mousePoint.x > (model.getContainer().getWidth() - 3) && tempLocation.atEnd(lines))
      tempLocation = tempLocation.moved(lines, +1);

    if(inWordSelectionMode)
      selectWord(tempLocation);
    else
      model.setCaretLocation(tempLocation, XOffsetStrategy.FITTING, YOffsetStrategy.FITTING);

    e.getRecipient().markAsDirty();
  }

  private void selectWord(TextLocation tempLocation)
  {
    new WordSelector(tempLocation).processWordSelection();
  }

  private class WordSelector
  {
    private TextLocation mouseLocation;

    public WordSelector(TextLocation location)
    {
      this.mouseLocation = location;
    }

    public void processWordSelection()
    {
      boolean rightOfTail = isRightOfTail();

      boolean selectionFacingRight = isSelectionFacingRight();

      boolean isMouseTrailingTheTail = selectionFacingRight && !rightOfTail || !selectionFacingRight && rightOfTail;
      if(isMouseTrailingTheTail)
        turnAround();

      if(rightOfTail)
        repositionHead(model.findWordsRightEdge(mouseLocation));
      else
        repositionHead(model.findWordsLeftEdge(mouseLocation));
    }

    private void turnAround()
    {
      model.setSelectionLocation(model.getCaretLocation());
    }

    private void repositionHead(TextLocation newHead)
    {
      model.setCaretLocation(newHead, XOffsetStrategy.FITTING, YOffsetStrategy.FITTING);
    }

    private boolean isSelectionFacingRight()
    {
      return model.getCaretLocation().isAfter(model.getSelectionLocation());
    }

    private boolean isRightOfTail()
    {
      return mouseLocation.isAfter(model.getSelectionLocation());
    }
  }
}
