//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

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
  TextModel modelInfo;
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
    panel = new TextArea2Panel();
    modelInfo = panel.getModel();
    modelInfo.setText("Some Text");
    modelInfo.setCaretIndex(0);
    processor = new MouseProcessor(modelInfo);
    parent = new PropPanel(new MockProp());
    parent.add(panel);
    parent.setParent(null);
    parent.setLocation(0, 0);
    panel.setParent(parent);
    panel.setLocation(100, 100);
    panel.setSize(150, 75);
    mockMouseEvent = new MockMouseEvent(110, 115);
  }

  public void setUpTextBox()
  {
    panel = new TextBox2Panel();
    modelInfo = panel.getModel();
    modelInfo.setText("Some Text that will prove. to be longer than can fit in this box");
    processor = new MouseProcessor(modelInfo);
    parent = new PropPanel(new MockProp());
    parent.add(panel);
    parent.setParent(null);
    parent.setLocation(0, 0);
    panel.setParent(parent);
    panel.setLocation(100, 100);
    panel.setSize(150, 25);
    mockMouseEvent = new MockMouseEvent(101, 115);
  }

  @Test
  public void canCheckIfMouseEventWasInsideThePanel()
  {
    boolean inside = processor.isMouseEventInBox(mockMouseEvent);

    assertEquals(true, inside);
  }

  @Test
  public void canCheckIfMouseEventWasOutsideThePanel()
  {
    mockMouseEvent = new MockMouseEvent(300, 500);

    boolean inside = processor.isMouseEventInBox(mockMouseEvent);

    assertEquals(false, inside);
  }

  @Test
  public void canCalculateTheMouseClickIndexForTheCursors()
  {
    int index = processor.calculateMouseClickIndex(10, 5);

    assertEquals(1, index);
  }

  @Test
  public void willReturnRightEdgeIndexIfClickIsOnRightEdge()
  {
    int index = processor.calculateMouseClickIndex(140, 5);

    assertEquals(9, index);
  }

  @Test
  public void shouldNotCrashWhenCalculatingMouseClinkIndexWithNoText() throws Exception
  {
    modelInfo.setText("");
    assertEquals(0, processor.calculateMouseClickIndex(10, 5));

    modelInfo.setText(null);
    assertEquals(0, processor.calculateMouseClickIndex(10, 5));
  }

  @Test
  public void willSetCursorAndSelectionIndexOnMouseClickInTheBox()
  {
    processor.processMousePressed(mockMouseEvent);

    assertEquals(1, modelInfo.getCaretIndex());
    assertEquals(1, modelInfo.getSelectionIndex());
  }

  @Test
  public void willNotSetCursorAndSelectionIndexOnMouseClickOutSideTheBox()
  {
    mockMouseEvent = new MockMouseEvent(42, 42);

    processor.processMousePressed(mockMouseEvent);

    assertEquals(0, modelInfo.getCaretIndex());
    assertEquals(0, modelInfo.getSelectionIndex());
  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineText()
  {
    modelInfo.setText("This is\nMulti lined.");
    mockMouseEvent = new MockMouseEvent(110,130);

    processor.processMousePressed(mockMouseEvent);

    assertEquals(9, modelInfo.getCaretIndex());
  }

  // MDM - Failed sporadically
//  @Test
//  public void willMarkCursorProperlyWithAMultiLineTextAndAYOffset()
//  {
//    modelInfo.setText("This is\nMulti lined.\nSuper\nMulti\nLined\nTo\nThe Max");
//    modelInfo.calculateYOffset();
//    mockMouseEvent = new MockMouseEvent(105,165);
//
//    processor.processMousePressed(mockMouseEvent);
//
//    assertEquals(42, modelInfo.getCursorIndex());
//  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineTextAndClickIsFarRightOfText()
  {
    modelInfo.setText("This is\nMulti lined.");
    mockMouseEvent = new MockMouseEvent(190,130);

    processor.processMousePressed(mockMouseEvent);

    assertEquals(20, modelInfo.getCaretIndex());
  }

  @Test
  public void willPutCursorOnTheSameLineAsTheClickWhenFollowedByNewLine()
  {
    modelInfo.setText("This is\nMulti lined.\n");
    mockMouseEvent = new MockMouseEvent(190,130);

    processor.processMousePressed(mockMouseEvent);

    assertEquals(21, modelInfo.getCaretIndex());
  }

  @Test
  public void willSetSelectionOnToTrueIfMouseClickInTheBox()
  {
    processor.processMousePressed(mockMouseEvent);

    assertEquals(true, modelInfo.isSelectionOn());
  }

  @Test
  public void willChangeTheCursorIndexForMouseDragged()
  {
    processor.processMouseDragged(mockMouseEvent);

    assertEquals(1, modelInfo.getCaretIndex());
  }

  @Test
  public void willChangeTheYOffsetWhileDraggingPastCriticalEdge()
  {
    modelInfo.setText("This is\nMulti lined.\nSuper\nMulti\nLined\nTo\nThe Max\nAndMore?");
    int oldYOffset = modelInfo.getOffset().y;
    assertEquals(true, oldYOffset > 0);
    mockMouseEvent = new MockMouseEvent(110,100);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(true, oldYOffset > modelInfo.getOffset().y);
  }

  @Test
  public void willChangeTheXOffestWhileDraggingIfPastCriticalEdge()
  {
    setUpTextBox();
    modelInfo.setSelectionOn(true);
    modelInfo.setSelectionIndex(0);
    int oldXOffset = modelInfo.getOffset().x;
    mockMouseEvent = new MockMouseEvent(100, 115);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(true, oldXOffset > modelInfo.getOffset().x);
  }

  @Test
  public void wontGetCaughtOnAnEdgeUnableToFurtherDrag()
  {
    setUpTextBox();
    mockMouseEvent = new MockMouseEvent(249,115);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(true, modelInfo.getOffset().x > 0);
  }

  @Test
  public void willSetSelectionOnToFalseIfMouseReleaseIsAtSameIndexAsTheClick()
  {
    modelInfo.setSelectionIndex(1);
    modelInfo.setSelectionOn(true);

    processor.processMouseReleased(mockMouseEvent);


    assertEquals(false, modelInfo.isSelectionOn());
  }

  @Test
  public void willSelectWordForDoubleClick()
  {
    processor.lastClickTime = (new Date()).getTime();

    processor.processMousePressed(mockMouseEvent);

    assertEquals(4, modelInfo.getCaretIndex());
    assertEquals(0, modelInfo.getSelectionIndex());
    assertEquals(true, modelInfo.isSelectionOn());
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
    modelInfo.setCaretIndex(5);

    processor.processMouseReleased(mockMouseEvent);

    assertEquals(5, modelInfo.getCaretIndex());
  }

  @Test
  public void willBeginWordSelectionIfDoubleClickIsOn()
  {
    processor.doubleClickOn = true;
    modelInfo.setSelectionIndex(0);
    modelInfo.setCaretIndex(4);
    mockMouseEvent = new MockMouseEvent(modelInfo.getXPosFromIndex(8) + modelInfo.getPanelAbsoluteLocation().x, 115);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(modelInfo.getText().length(), modelInfo.getCaretIndex());
  }

  @Test
  public void canWordSelectGoingToTheLeft()
  {
    processor.doubleClickOn = true;
    modelInfo.setSelectionIndex(5);
    modelInfo.setCaretIndex(9);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(9, modelInfo.getSelectionIndex());
    assertEquals(0, modelInfo.getCaretIndex());
  }

  @Test
  public void canWordSelectManyWordsToTheLeft()
  {
    modelInfo.setText("Some Text Here");
    processor.doubleClickOn = true;
    modelInfo.setSelectionIndex(14);
    modelInfo.setCaretIndex(5);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(14, modelInfo.getSelectionIndex());
    assertEquals(0, modelInfo.getCaretIndex());
  }

  @Test
  public void willDeselectOneWordAtATimeWhenDraggingRightOverSelection()
  {
    modelInfo.setText("Some Text Here");
    processor.doubleClickOn = true;
    modelInfo.setSelectionIndex(14);
    modelInfo.setCaretIndex(5);
    mockMouseEvent = new MockMouseEvent(modelInfo.getXPosFromIndex(10) + modelInfo.getPanelAbsoluteLocation().x + 1, 115);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(14, modelInfo.getSelectionIndex());
    assertEquals(10, modelInfo.getCaretIndex());
  }

  @Test
  public void willDeselectOneWordAtATimeWhenDraggingLeftOverSelection()
  {
    modelInfo.setText("Some Text Here");
    processor.doubleClickOn = true;
    modelInfo.setSelectionIndex(5);
    modelInfo.setCaretIndex(14);
    mockMouseEvent = new MockMouseEvent(modelInfo.getXPosFromIndex(7) + modelInfo.getPanelAbsoluteLocation().x, 115);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(5, modelInfo.getSelectionIndex());
    assertEquals(9, modelInfo.getCaretIndex());
  }

  @Test
  public void willKeepTheOriginalWordSelectedIfDraggedPastRightEdgeOfWordAfterSelectingToTheLeft()
  {
    modelInfo.setText("Some Text Here");
    processor.doubleClickOn = true;
    modelInfo.setSelectionIndex(9);
    modelInfo.setCaretIndex(5);
    mockMouseEvent = new MockMouseEvent(modelInfo.getXPosFromIndex(10) + modelInfo.getPanelAbsoluteLocation().x + 1, 115);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(5, modelInfo.getSelectionIndex());
    assertEquals(14, modelInfo.getCaretIndex());
  }

  @Test
  public void willSelectAllForTripleClick()
  {
    processor.doubleClickOn = true;
    processor.lastClickTime = (new Date()).getTime();

    processor.makeExtraSelectionOnMultiClick();

    assertEquals(0, modelInfo.getSelectionIndex());
    assertEquals(9, modelInfo.getCaretIndex());
  }
}


