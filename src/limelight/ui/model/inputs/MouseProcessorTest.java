package limelight.ui.model.inputs;

import limelight.ui.api.MockProp;
import limelight.ui.model.PropPanel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    modelInfo = panel.getModelInfo();
    modelInfo.setText("Some Text");
    modelInfo.setCursorIndex(0);
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
    modelInfo = panel.getModelInfo();
    modelInfo.setText("Some Text that will prove to be longer than can fit in this box");
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

    assertTrue(inside);
  }

  @Test
  public void canCheckIfMouseEventWasOutsideThePanel()
  {
    mockMouseEvent = new MockMouseEvent(300, 500);

    boolean inside = processor.isMouseEventInBox(mockMouseEvent);

    assertFalse(inside);
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
  public void willSetCursorAndSelectionIndexOnMouseClickInTheBox()
  {
    processor.processMousePressed(mockMouseEvent);

    assertEquals(1, modelInfo.getCursorIndex());
    assertEquals(1, modelInfo.getSelectionIndex());
  }

  @Test
  public void willNotSetCursorAndSelectionIndexOnMouseClickOutSideTheBox()
  {
    mockMouseEvent = new MockMouseEvent(42, 42);

    processor.processMousePressed(mockMouseEvent);

    assertEquals(0, modelInfo.getCursorIndex());
    assertEquals(0, modelInfo.getSelectionIndex());
  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineText()
  {
    modelInfo.setText("This is\nMulti lined.");
    mockMouseEvent = new MockMouseEvent(110,120);

    processor.processMousePressed(mockMouseEvent);

    assertEquals(9, modelInfo.cursorIndex);
  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineTextAndAYOffset()
  {
    modelInfo.setText("This is\nMulti lined.\nSuper\nMulti\nLined\nTo\nThe Max");
    modelInfo.calculateYOffset();
    mockMouseEvent = new MockMouseEvent(105,165);

    processor.processMousePressed(mockMouseEvent);

    assertEquals(42, modelInfo.cursorIndex);
  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineTextAndClickIsFarRightOfText()
  {
    modelInfo.setText("This is\nMulti lined.");
    mockMouseEvent = new MockMouseEvent(190,120);

    processor.processMousePressed(mockMouseEvent);

    assertEquals(20, modelInfo.cursorIndex);
  }

  @Test
  public void willSetSelectionOnToTrueIfMouseClickInTheBox()
  {
    processor.processMousePressed(mockMouseEvent);

    assertTrue(modelInfo.selectionOn);
  }

  @Test
  public void willChangeTheCursorIndexForMouseDragged()
  {
    processor.processMouseDragged(mockMouseEvent);

    assertEquals(1, modelInfo.getCursorIndex());
  }

  @Test
  public void willChangeTheYOffsetWhileDraggingPastCriticalEdge()
  {
    modelInfo.setText("This is\nMulti lined.\nSuper\nMulti\nLined\nTo\nThe Max");
    int oldYOffset = modelInfo.calculateYOffset();
    mockMouseEvent = new MockMouseEvent(105,101);

    processor.processMouseDragged(mockMouseEvent);

    assertTrue(oldYOffset > modelInfo.yOffset);
  }

  @Test
  public void willChangeTheXOffestWhileDraggingIfPastCriticalEdge()
  {
    setUpTextBox();
    modelInfo.calculateTextXOffset(panel.getWidth(), modelInfo.calculateTextDimensions().width);
    modelInfo.selectionOn = true;
    modelInfo.selectionIndex = modelInfo.cursorIndex;
    int oldXOffset = modelInfo.xOffset;
    System.out.println("oldOffset = " + oldXOffset);
    System.out.println("modelInfo.cursorIndex = " + modelInfo.cursorIndex);

    processor.processMouseDragged(mockMouseEvent);

    assertTrue(oldXOffset > modelInfo.xOffset);
  }

  @Test
  public void willSetSelectionOnToFalseIfMouseReleaseIsAtSameIndexAsTheClick()
  {
    modelInfo.setSelectionIndex(1);
    modelInfo.selectionOn = true;

    processor.processMouseReleased(mockMouseEvent);


    assertFalse(modelInfo.selectionOn);
  }

  @Test
  public void willSelectWordForDoubleClick()
  {
    processor.lastClickTime = (new Date()).getTime();

    processor.processMousePressed(mockMouseEvent);

    assertEquals(4, modelInfo.getCursorIndex());
    assertEquals(0, modelInfo.getSelectionIndex());
    assertTrue(modelInfo.selectionOn);
  }

  @Test
  public void willSetStateForDoubleClickSelection()
  {
    processor.lastClickTime = (new Date()).getTime();

    processor.makeExtraSelectionOnMultiClick();

    assertTrue(processor.doubleClickOn);
  }

  @Test
  public void willNotChangeTheCursorPositionOnMouseReleaseIfDoubleClickIsOn()
  {
    processor.doubleClickOn = true;
    modelInfo.setCursorIndex(5);

    processor.processMouseReleased(mockMouseEvent);

    assertEquals(5, modelInfo.getCursorIndex());
  }

  @Test
  public void willBeginWordSelectionIfDoubleClickIsOn()
  {
    processor.doubleClickOn = true;
    modelInfo.setSelectionIndex(0);
    modelInfo.setCursorIndex(4);
    mockMouseEvent = new MockMouseEvent(modelInfo.getXPosFromIndex(8) + modelInfo.getPanelAbsoluteLocation().x, 115);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(modelInfo.getText().length(), modelInfo.getCursorIndex());
  }

  @Test
  public void canWordSelectGoingToTheLeft()
  {
    processor.doubleClickOn = true;
    modelInfo.setSelectionIndex(5);
    modelInfo.setCursorIndex(9);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(9, modelInfo.getSelectionIndex());
    assertEquals(0, modelInfo.getCursorIndex());
  }

  @Test
  public void canWordSelectManyWordsToTheLeft()
  {
    modelInfo.setText("Some Text Here");
    processor.doubleClickOn = true;
    modelInfo.setSelectionIndex(14);
    modelInfo.setCursorIndex(5);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(14, modelInfo.getSelectionIndex());
    assertEquals(0, modelInfo.getCursorIndex());
  }

  @Test
  public void willDeselectOneWordAtATimeWhenDraggingRightOverSelection()
  {
    modelInfo.setText("Some Text Here");
    processor.doubleClickOn = true;
    modelInfo.setSelectionIndex(14);
    modelInfo.setCursorIndex(5);
    mockMouseEvent = new MockMouseEvent(modelInfo.getXPosFromIndex(10) + modelInfo.getPanelAbsoluteLocation().x, 115);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(14, modelInfo.getSelectionIndex());
    assertEquals(10, modelInfo.getCursorIndex());
  }

  @Test
  public void willDeselectOneWordAtATimeWhenDraggingLeftOverSelection()
  {
    modelInfo.setText("Some Text Here");
    processor.doubleClickOn = true;
    modelInfo.setSelectionIndex(5);
    modelInfo.setCursorIndex(14);
    mockMouseEvent = new MockMouseEvent(modelInfo.getXPosFromIndex(7) + modelInfo.getPanelAbsoluteLocation().x, 115);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(5, modelInfo.getSelectionIndex());
    assertEquals(9, modelInfo.getCursorIndex());
  }

  @Test
  public void willKeepTheOriginalWordSelectedIfDraggedPastRightEdgeOfWordAfterSelectingToTheLeft()
  {
    modelInfo.setText("Some Text Here");
    processor.doubleClickOn = true;
    modelInfo.setSelectionIndex(9);
    modelInfo.setCursorIndex(5);
    mockMouseEvent = new MockMouseEvent(modelInfo.getXPosFromIndex(10) + modelInfo.getPanelAbsoluteLocation().x, 115);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(5, modelInfo.getSelectionIndex());
    assertEquals(14, modelInfo.getCursorIndex());
  }

  @Test
  public void willSelectAllForTripleClick()
  {
    processor.doubleClickOn = true;
    processor.lastClickTime = (new Date()).getTime();

    processor.makeExtraSelectionOnMultiClick();

    assertEquals(0, modelInfo.getSelectionIndex());
    assertEquals(9, modelInfo.getCursorIndex());
  }
}


