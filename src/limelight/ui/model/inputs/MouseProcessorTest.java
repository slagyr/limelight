//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.api.MockProp;
import limelight.ui.model.PropPanel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class MouseProcessorTest
{
  MouseProcessor processor;
  TextInputPanel panel;
  TextModel model;
  PropPanel parent;
  MouseEvent mockMouseEvent;

  public class MockMouseEvent extends MouseEvent
  {

    public MockMouseEvent(int x, int y)
    {
      super(new Panel(), 0, 12098310293l, 0, x, y, 1, false);
    }
  }

  @Before
  public void setUp()
  {
    setUpWithText("Some Text");
  }

  public void setUpTextBox()
  {
    setUpWithText("Some Text that will prove. to be longer than can fit in this box");
  }

  private void setUpWithText(String text)
  {
    panel = new TextArea2Panel();

    model = panel.getModel();
    model.setText(text);
    model.setCaretIndex(0);
    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);

    processor = new MouseProcessor(model);

    parent = new PropPanel(new MockProp());
    parent.add(panel);
    parent.setParent(null);
    parent.setLocation(0, 0);
    panel.setParent(parent);
    panel.setLocation(100, 100);
    panel.setSize(150, 75);

    mockMouseEvent = new MockMouseEvent(110, 115);
  }
  
  @Test
  public void willSetCursorAndSelectionIndexOnMouseClickInTheBox()
  {
    processor.processMousePressed(mockMouseEvent);

    assertEquals(1, model.getCaretIndex());
    assertEquals(1, model.getSelectionIndex());
  }

  @Test
  public void willNotSetCursorAndSelectionIndexOnMouseClickOutSideTheBox()
  {
    mockMouseEvent = new MockMouseEvent(42, 42);

    processor.processMousePressed(mockMouseEvent);

    assertEquals(0, model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineText()
  {
    model.setText("This is\nMulti lined.");
    mockMouseEvent = new MockMouseEvent(110,130);

    processor.processMousePressed(mockMouseEvent);

    assertEquals(9, model.getCaretIndex());
  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineTextAndAYOffset()
  {
    model.setText("This is\nMulti lined.\nSuper\nMulti\nLined\nTo\nThe Max");
    model.calculateYOffset();
    mockMouseEvent = new MockMouseEvent(105,165);

    processor.processMousePressed(mockMouseEvent);

    assertEquals(42, model.getCaretIndex());
  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineTextAndClickIsFarRightOfText()
  {
    model.setText("This is\nMulti lined.");
    mockMouseEvent = new MockMouseEvent(190,130);

    processor.processMousePressed(mockMouseEvent);

    assertEquals(20, model.getCaretIndex());
  }

  @Test
  public void willPutCursorOnTheSameLineAsTheClickWhenFollowedByNewLine()
  {
    model.setText("This is\nMulti lined.\n");
    mockMouseEvent = new MockMouseEvent(190,130);

    processor.processMousePressed(mockMouseEvent);

    assertEquals(21, model.getCaretIndex());
  }

  @Test
  public void willSetSelectionOnToTrueIfMouseClickInTheBox()
  {
    processor.processMousePressed(mockMouseEvent);

    assertEquals(true, model.isSelectionOn());
  }

  @Test
  public void willChangeTheCursorIndexForMouseDragged()
  {
    processor.processMouseDragged(mockMouseEvent);

    assertEquals(1, model.getCaretIndex());
  }

  @Test
  public void willChangeTheYOffsetWhileDraggingPastCriticalEdge()
  {
    model.setText("This is\nMulti lined.\nSuper\nMulti\nLined\nTo\nThe Max\nAndMore?");
    int oldYOffset = model.getOffset().y;
    assertEquals(true, oldYOffset > 0);
    mockMouseEvent = new MockMouseEvent(110,100);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(true, oldYOffset > model.getOffset().y);
  }

  @Test
  public void willChangeTheXOffestWhileDraggingIfPastCriticalEdge()
  {
    setUpTextBox();
    model.setSelectionOn(true);
    model.setSelectionIndex(0);
    int oldXOffset = model.getOffset().x;
    mockMouseEvent = new MockMouseEvent(100, 115);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(true, oldXOffset > model.getOffset().x);
  }

  @Test
  public void wontGetCaughtOnAnEdgeUnableToFurtherDrag()
  {
    setUpTextBox();
    mockMouseEvent = new MockMouseEvent(249,115);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(true, model.getOffset().x > 0);
  }

  @Test
  public void willSetSelectionOnToFalseIfMouseReleaseIsAtSameIndexAsTheClick()
  {
    model.setSelectionIndex(1);
    model.setSelectionOn(true);

    processor.processMouseReleased(mockMouseEvent);


    assertEquals(false, model.isSelectionOn());
  }

  @Test
  public void willSelectWordForDoubleClick()
  {
    processor.lastClickTime = (new Date()).getTime();

    processor.processMousePressed(mockMouseEvent);

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

    processor.processMouseReleased(mockMouseEvent);

    assertEquals(5, model.getCaretIndex());
  }

  @Test
  public void willBeginWordSelectionIfDoubleClickIsOn()
  {
    processor.doubleClickOn = true;
    model.setSelectionIndex(0);
    model.setCaretIndex(4);
    mockMouseEvent = new MockMouseEvent(model.getXPosFromIndex(8) + model.getPanelAbsoluteLocation().x, 115);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(model.getText().length(), model.getCaretIndex());
  }

  @Test
  public void canWordSelectGoingToTheLeft()
  {
    processor.doubleClickOn = true;
    model.setSelectionIndex(5);
    model.setCaretIndex(9);

    processor.processMouseDragged(mockMouseEvent);

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

    processor.processMouseDragged(mockMouseEvent);

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
    mockMouseEvent = new MockMouseEvent(model.getXPosFromIndex(10) + model.getPanelAbsoluteLocation().x + 1, 115);

    processor.processMouseDragged(mockMouseEvent);

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
    mockMouseEvent = new MockMouseEvent(model.getXPosFromIndex(7) + model.getPanelAbsoluteLocation().x, 115);

    processor.processMouseDragged(mockMouseEvent);

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
    mockMouseEvent = new MockMouseEvent(model.getXPosFromIndex(10) + model.getPanelAbsoluteLocation().x + 1, 115);

    processor.processMouseDragged(mockMouseEvent);

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


