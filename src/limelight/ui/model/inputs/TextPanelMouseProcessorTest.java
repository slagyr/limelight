//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.events.MouseDraggedEvent;
import limelight.ui.events.MousePressedEvent;
import limelight.ui.events.MouseReleasedEvent;
import limelight.ui.model.MockRootPanel;
import limelight.ui.text.TextLocation;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class TextPanelMouseProcessorTest
{
  private TextModel model;
  private TextInputPanel panel;
  private MockRootPanel root;

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
    if(multiline)
      panel = new TextAreaPanel();
    else
      panel = new TextBoxPanel();

    panel.setSize(150, 75);
    root = new MockRootPanel();
    root.add(panel);

    model = panel.getModel();

    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
    model.setText(text);
    model.setCaretIndex(0);
  }

  private void pressAt(int x, int y)
  {
    multiplePressAt(x, y, 0);
  }

  private void releaseAt(int x, int y)
  {
    panel.getEventHandler().dispatch(new MouseReleasedEvent(panel, 0, new Point(x, y), 0));
  }

  private void multiplePressAt(int x, int y, int count)
  {
    panel.getEventHandler().dispatch(new MousePressedEvent(panel, 0, new Point(x, y), count));
  }

  private void dragAt(int x, int y)
  {
    panel.getEventHandler().dispatch(new MouseDraggedEvent(panel, 0, new Point(x, y), 0));
  }

  @Test
  public void willSetCursorAndSelectionIndexOnMouseClickInTheBox()
  {
    pressAt(10, 5);

    assertEquals(1, model.getCaretIndex());
    assertEquals(1, model.getSelectionIndex());
  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineText()
  {
    model.setText("This is\nMulti lined.");

    pressAt(10, 15);

    assertEquals(9, model.getCaretIndex());
  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineTextAndAYOffset()
  {
    model.setText("This is\nMulti lined.\nSuper\nMulti\nLined\nTo\nThe Max");
    pressAt(5, 65);

    assertEquals(42, model.getCaretIndex());
  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineTextAndClickIsFarRightOfText()
  {
    model.setText("This is\nMulti lined.");

    pressAt(90, 30);

    assertEquals(20, model.getCaretIndex());
  }

  @Test
  public void willPutCursorOnTheSameLineAsTheClickWhenFollowedByNewLine()
  {
    model.setText("This is\nMulti lined.\n");

    pressAt(90, 30);

    assertEquals(21, model.getCaretIndex());
  }

  @Test
  public void willSetSelectionOnToTrueIfMouseClickInTheBox()
  {
    pressAt(10, 15);

    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void willChangeTheCursorIndexForMouseDragged()
  {
    dragAt(10, 5);

    assertEquals(1, model.getCaretIndex());
  }

  @Test
  public void willChangeTheYOffsetWhileDraggingPastCriticalEdge()
  {
    model.setText("This is\nMulti lined.\nSuper\nMulti\nLined\nTo\nThe Max\nAndMore?");
    assertEquals(-12, model.getYOffset());

    dragAt(0, -10);

    assertEquals(0, model.getYOffset());
  }

  @Test
  public void willChangeTheXOffestWhileDraggingIfPastCriticalEdge()
  {
    setUpSingleLine();
    model.startSelection(TextLocation.origin);

    dragAt(200, 5);

    assertEquals(-51, model.getXOffset());
  }

  @Test
  public void wontGetCaughtOnAnEdgeUnableToFurtherDrag()
  {
    setUpSingleLine();

    dragAt(149, 5);

    assertEquals(0, model.getXOffset());
  }

  @Test
  public void willSelectWordForDoubleClick()
  {
    multiplePressAt(10, 5, 2);

    assertEquals(4, model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void willNotChangeTheCursorPositionOnMouseReleaseIfDoubleClickIsOn()
  {
    multiplePressAt(10, 5, 2);
    model.setCaretIndex(5);

    releaseAt(10, 15);

    assertEquals(5, model.getCaretIndex());
  }

  @Test
  public void willBeginWordSelectionIfDoubleClickIsOn()
  {
    multiplePressAt(10, 5, 2);
    model.setSelectionIndex(0);
    model.setCaretIndex(4);

    dragAt(model.getX(8) + model.getContainer().getAbsoluteLocation().x, 115);

    assertEquals(model.getText().length(), model.getCaretIndex());
  }

  @Test
  public void canWordSelectGoingToTheLeft()
  {
    multiplePressAt(10, 5, 2);
    model.setSelectionIndex(5);
    model.setCaretIndex(9);

    dragAt(10, 5);

    assertEquals(9, model.getSelectionIndex());
    assertEquals(0, model.getCaretIndex());
  }

  @Test
  public void canWordSelectManyWordsToTheLeft()
  {
    model.setText("Some Text Here");
    multiplePressAt(10, 5, 2);
    model.setSelectionIndex(14);
    model.setCaretIndex(5);

    dragAt(10, 5);

    assertEquals(14, model.getSelectionIndex());
    assertEquals(0, model.getCaretIndex());
  }

  @Test
  public void willDeselectOneWordAtATimeWhenDraggingRightOverSelection()
  {
    model.setText("Some Text Here");
    multiplePressAt(10, 5, 2);
    model.setSelectionIndex(14);
    model.setCaretIndex(5);

    dragAt(101, 15);

    assertEquals(14, model.getSelectionIndex());
    assertEquals(10, model.getCaretIndex());
  }

  @Test
  public void willDeselectOneWordAtATimeWhenDraggingLeftOverSelection()
  {
    model.setText("Some Text Here");
    multiplePressAt(10, 5, 2);
    model.setSelectionIndex(5);
    model.setCaretIndex(14);

    dragAt(70, 5);

    assertEquals(5, model.getSelectionIndex());
    assertEquals(9, model.getCaretIndex());
  }

  @Test
  public void willKeepTheOriginalWordSelectedIfDraggedPastRightEdgeOfWordAfterSelectingToTheLeft()
  {
    model.setText("Some Text Here");
    multiplePressAt(10, 5, 2);
    model.setSelectionIndex(9);
    model.setCaretIndex(5);

    dragAt(101, 115);

    assertEquals(5, model.getSelectionIndex());
    assertEquals(14, model.getCaretIndex());
  }

  @Test
  public void willSelectAllForTripleClick()
  {
    multiplePressAt(10, 5, 3);
    releaseAt(10, 5);

    assertEquals(0, model.getSelectionIndex());
    assertEquals(9, model.getCaretIndex());
    assertEquals(true, model.isSelectionActivated());
  }
  
  @Test
  public void pressingTurnsCaretOn() throws Exception
  {
    assertEquals(false, model.isCaretOn());

    pressAt(0, 0);

    assertEquals(true, model.isCaretOn());
  }
  
  @Test
  public void pressingMakesPanelDirty() throws Exception
  {
    assertEquals(0, root.dirtyRegions.size());

    pressAt(0, 0);

    assertEquals(panel.getBoundingBox(), root.dirtyRegions.get(0));
  }
  
  @Test
  public void pressingFocusesOnPanel() throws Exception
  {
    pressAt(0, 0);

    assertEquals(panel, root.getKeyListener().getFocusedPanel());
  }
  
  @Test
  public void draggingMakePanelDirty() throws Exception
  {
    dragAt(0, 0);

    assertEquals(panel.getBoundingBox(), root.dirtyRegions.get(0));
  }

}


