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
  TextInputPanel boxPanel;
  TextModel boxInfo;
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
    boxPanel = new TextBox2Panel();
    boxInfo = new PlainTextModel(boxPanel);
    boxInfo.setText("Some Text");
    boxInfo.cursorIndex = 0;
    processor = new MouseProcessor(boxInfo);
    parent = new PropPanel(new MockProp());
    parent.add(boxPanel);
    parent.setParent(null);
    parent.setLocation(0, 0);
    boxPanel.setParent(parent);
    boxPanel.setLocation(100, 100);
    boxPanel.setSize(150, 25);
    mockMouseEvent = new MockMouseEvent(110, 115);
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

    assertEquals(1, boxInfo.cursorIndex);
    assertEquals(1, boxInfo.selectionIndex);
  }

  @Test
  public void willNotSetCursorAndSelectionIndexOnMouseClickOutSideTheBox()
  {
    mockMouseEvent = new MockMouseEvent(42, 42);

    processor.processMousePressed(mockMouseEvent);

    assertEquals(0, boxInfo.cursorIndex);
    assertEquals(0, boxInfo.selectionIndex);
  }

  @Test
  public void willSetSelectionOnToTrueIfMouseClickInTheBox()
  {
    processor.processMousePressed(mockMouseEvent);

    assertTrue(boxInfo.selectionOn);
  }

  @Test
  public void willChangeTheCursorIndexForMouseDragged()
  {
    processor.processMouseDragged(mockMouseEvent);

    assertEquals(1, boxInfo.cursorIndex);
  }

  @Test
  public void willSetSelectionOnToFalseIfMouseReleaseIsAtSameIndexAsTheClick()
  {
    boxInfo.selectionIndex = 1;
    boxInfo.selectionOn = true;

    processor.processMouseReleased(mockMouseEvent);


    assertFalse(boxInfo.selectionOn);
  }

  @Test
  public void willSelectWordForDoubleClick()
  {
    processor.lastClickTime = (new Date()).getTime();

    processor.processMousePressed(mockMouseEvent);

    assertEquals(4,boxInfo.cursorIndex);
    assertEquals(0,boxInfo.selectionIndex);
    assertTrue(boxInfo.selectionOn);
  }

  @Test
  public void willSetStateForDoubleClickSelection()
  {
    processor.lastClickTime = (new Date()).getTime();

    processor.selectWordOnDoubleClick();

    assertTrue(processor.doubleClickOn);
  }

  @Test
  public void willNotChangeTheCursorPositionOnMouseReleaseIfDoubleClickIsOn()
  {
    processor.doubleClickOn = true;
    boxInfo.cursorIndex = 5;

    processor.processMouseReleased(mockMouseEvent);

    assertEquals(5, boxInfo.cursorIndex);
  }

  @Test
  public void willBeginWordSelectionIfDoubleClickIsOn()
  {
    processor.doubleClickOn = true;
    boxInfo.selectionIndex = 0;
    boxInfo.cursorIndex = 4;
    mockMouseEvent = new MockMouseEvent(boxInfo.getXPosFromIndex(8) + boxInfo.getPanelAbsoluteLocation().x,115);

    processor.processMouseDragged(mockMouseEvent);

    assertEquals(boxInfo.text.length(), boxInfo.cursorIndex);
  }
}


