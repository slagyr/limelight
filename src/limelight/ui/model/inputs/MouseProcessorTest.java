//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.model.PropPanel;
import limelight.util.Box;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.MouseEvent;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class MouseProcessorTest
{
  MouseProcessor processor;
  MockTextContainer container;
  TextModel model;
  PropPanel parent;

  private MouseEvent event(int x, int y)
  {
    return new MouseEvent(new java.awt.Panel(), 0, 12098310293l, 0, x, y, 1, false);
  }

  @Before
  public void setUp()
  {
    setUpWithText("Some Text", true);
  }

  public void setUpSingleLine()
  {
    setUpWithText("Some Text that will prove. to be longer than can fit in this box", false);
  }

  private void setUpWithText(String text, boolean multiline)
  {
    container = new MockTextContainer();
    container.bounds = new Box(0, 0, 150, 75);

    if(multiline)
      model = new MultiLineTextModel(container);
    else
      model = new SingleLineTextModel(container);

    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
    model.setText(text);
    model.setCaretIndex(0);

    processor = new MouseProcessor(model);
  }
  
  @Test
  public void willSetCursorAndSelectionIndexOnMouseClickInTheBox()
  {
    processor.processMousePressed(event(10, 5));

    assertEquals(1, model.getCaretIndex());
    assertEquals(1, model.getSelectionIndex());
  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineText()
  {
    model.setText("This is\nMulti lined.");

    processor.processMousePressed(event(10,15));

    assertEquals(9, model.getCaretIndex());
  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineTextAndAYOffset()
  {
    model.setText("This is\nMulti lined.\nSuper\nMulti\nLined\nTo\nThe Max");

    processor.processMousePressed(event(5,65));

    assertEquals(42, model.getCaretIndex());
  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineTextAndClickIsFarRightOfText()
  {
    model.setText("This is\nMulti lined.");

    processor.processMousePressed(event(90,30));

    assertEquals(20, model.getCaretIndex());
  }

  @Test
  public void willPutCursorOnTheSameLineAsTheClickWhenFollowedByNewLine()
  {
    model.setText("This is\nMulti lined.\n");

    processor.processMousePressed(event(90,30));

    assertEquals(21, model.getCaretIndex());
  }

  @Test
  public void willSetSelectionOnToTrueIfMouseClickInTheBox()
  {
    processor.processMousePressed(event(10, 15));

    assertEquals(true, model.isSelectionOn());
  }

  @Test
  public void willChangeTheCursorIndexForMouseDragged()
  {
    processor.processMouseDragged(event(10, 05));

    assertEquals(1, model.getCaretIndex());
  }

  @Test
  public void willChangeTheYOffsetWhileDraggingPastCriticalEdge()
  {
    model.setText("This is\nMulti lined.\nSuper\nMulti\nLined\nTo\nThe Max\nAndMore?");
    assertEquals(-12, model.getYOffset());

    processor.processMouseDragged(event(0,-10));

    assertEquals(0, model.getYOffset());
  }

  @Test
  public void willChangeTheXOffestWhileDraggingIfPastCriticalEdge()
  {
    setUpSingleLine();
    model.setSelectionOn(true);
    model.setSelectionIndex(0);

    processor.processMouseDragged(event(200, 5));

    assertEquals(-51, model.getXOffset());
  }

  @Test
  public void wontGetCaughtOnAnEdgeUnableToFurtherDrag()
  {
    setUpSingleLine();

    processor.processMouseDragged(event(149, 5));

    assertEquals(0, model.getXOffset());
  }

  @Test
  public void willSetSelectionOnToFalseIfMouseReleaseIsAtSameIndexAsTheClick()
  {
    model.setSelectionIndex(1);
    model.setSelectionOn(true);

    processor.processMouseReleased(event(10, 5));

    assertEquals(false, model.isSelectionOn());
  }

  @Test
  public void willSelectWordForDoubleClick()
  {
    processor.lastClickTime = System.currentTimeMillis();

    processor.processMousePressed(event(10, 5));

    assertEquals(4, model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(true, model.isSelectionOn());
  }

  @Test
  public void willSetStateForDoubleClickSelection()
  {
    processor.lastClickTime = (new Date()).getTime();

    processor.makeExtraSelectionOnMultiClick();

    assertEquals(true, processor.doubleClickOn);
  }

  @Test
  public void willNotChangeTheCursorPositionOnMouseReleaseIfDoubleClickIsOn()
  {
    processor.doubleClickOn = true;
    model.setCaretIndex(5);

    processor.processMouseReleased(event(10, 15));

    assertEquals(5, model.getCaretIndex());
  }

  @Test
  public void willBeginWordSelectionIfDoubleClickIsOn()
  {
    processor.doubleClickOn = true;
    model.setSelectionIndex(0);
    model.setCaretIndex(4);

    processor.processMouseDragged(event(model.getX(8) + model.getContainer().getAbsoluteLocation().x, 115));

    assertEquals(model.getText().length(), model.getCaretIndex());
  }

  @Test
  public void canWordSelectGoingToTheLeft()
  {
    processor.doubleClickOn = true;
    model.setSelectionIndex(5);
    model.setCaretIndex(9);

    processor.processMouseDragged(event(10, 5));

    assertEquals(9, model.getSelectionIndex());
    assertEquals(0, model.getCaretIndex());
  }

  @Test
  public void canWordSelectManyWordsToTheLeft()
  {
    model.setText("Some Text Here");
    processor.doubleClickOn = true;
    model.setSelectionIndex(14);
    model.setCaretIndex(5);

    processor.processMouseDragged(event(10, 5));

    assertEquals(14, model.getSelectionIndex());
    assertEquals(0, model.getCaretIndex());
  }

  @Test
  public void willDeselectOneWordAtATimeWhenDraggingRightOverSelection()
  {
    model.setText("Some Text Here");
    processor.doubleClickOn = true;
    model.setSelectionIndex(14);
    model.setCaretIndex(5);

    processor.processMouseDragged(event(101, 15));

    assertEquals(14, model.getSelectionIndex());
    assertEquals(10, model.getCaretIndex());
  }

  @Test
  public void willDeselectOneWordAtATimeWhenDraggingLeftOverSelection()
  {
    model.setText("Some Text Here");
    processor.doubleClickOn = true;
    model.setSelectionIndex(5);
    model.setCaretIndex(14);

    processor.processMouseDragged(event(70, 5));

    assertEquals(5, model.getSelectionIndex());
    assertEquals(9, model.getCaretIndex());
  }

  @Test
  public void willKeepTheOriginalWordSelectedIfDraggedPastRightEdgeOfWordAfterSelectingToTheLeft()
  {
    model.setText("Some Text Here");
    processor.doubleClickOn = true;
    model.setSelectionIndex(9);
    model.setCaretIndex(5);

    processor.processMouseDragged(event(101, 115));

    assertEquals(5, model.getSelectionIndex());
    assertEquals(14, model.getCaretIndex());
  }

  @Test
  public void willSelectAllForTripleClick()
  {
    processor.doubleClickOn = true;
    processor.lastClickTime = (new Date()).getTime();

    processor.makeExtraSelectionOnMultiClick();

    assertEquals(0, model.getSelectionIndex());
    assertEquals(9, model.getCaretIndex());
  }
}


