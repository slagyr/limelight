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
    model.setCaretLocation(TextLocation.origin);
  }

  private void pressAt(int x, int y)
  {
    multiplePressAt(x, y, 0);
  }

  private void releaseAt(int x, int y)
  {
    new MouseReleasedEvent(panel, 0, new Point(x, y), 0).dispatch(panel);
  }

  private void multiplePressAt(int x, int y, int count)
  {
    new MousePressedEvent(panel, 0, new Point(x, y), count).dispatch(panel);
  }

  private void dragAt(int x, int y)
  {
    new MouseDraggedEvent(panel, 0, new Point(x, y), 0).dispatch(panel);
  }

  @Test
  public void willSetCursorAndSelectionIndexOnMouseClickInTheBox()
  {
    pressAt(10, 5);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineText()
  {
    model.setText("This is\nMulti lined.");

    pressAt(10, 15);

    assertEquals(TextLocation.at(1, 1), model.getCaretLocation());
  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineTextAndAYOffset()
  {
    model.setText("This is\nMulti lined.\nSuper\nMulti\nLined\nTo\nThe Max");
    pressAt(5, 65);

    assertEquals(TextLocation.at(6, 0), model.getCaretLocation());
  }

  @Test
  public void willMarkCursorProperlyWithAMultiLineTextAndClickIsFarRightOfText()
  {
    model.setText("This is\nMulti lined.");

    pressAt(90, 30);

    assertEquals(TextLocation.at(1, 12), model.getCaretLocation());
  }

  @Test
  public void willPutCursorOnTheSameLineAsTheClickWhenFollowedByNewLine()
  {
    model.setText("This is\nMulti lined.\n");

    pressAt(90, 30);

    assertEquals(TextLocation.at(2, 0), model.getCaretLocation());
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

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
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

    assertEquals(TextLocation.at(0, 4), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void willNotChangeTheCursorPositionOnMouseReleaseIfDoubleClickIsOn()
  {
    multiplePressAt(10, 5, 2);
    model.setCaretLocation(TextLocation.at(0, 5));

    releaseAt(10, 15);

    assertEquals(TextLocation.at(0, 5), model.getCaretLocation());
  }

  @Test
  public void willBeginWordSelectionIfDoubleClickIsOn()
  {
    multiplePressAt(10, 5, 2);
    model.startSelection(TextLocation.origin);
    model.setCaretLocation(TextLocation.at(0, 4));

    dragAt(model.getX(8) + model.getContainer().getAbsoluteLocation().x, 115);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
  }

  @Test
  public void canWordSelectGoingToTheLeft()
  {
    multiplePressAt(10, 5, 2);
    model.startSelection(TextLocation.at(0, 5));
    model.setCaretLocation(TextLocation.at(0, 9));

    dragAt(10, 5);

    assertEquals(TextLocation.at(0, 9), model.getSelectionLocation());
    assertEquals(TextLocation.origin, model.getCaretLocation());
  }

  @Test
  public void canWordSelectManyWordsToTheLeft()
  {
    model.setText("Some Text Here");
    multiplePressAt(10, 5, 2);
    model.startSelection(TextLocation.at(0, 14));
    model.setCaretLocation(TextLocation.at(0, 5));

    dragAt(10, 5);

    assertEquals(TextLocation.at(0, 14), model.getSelectionLocation());
    assertEquals(TextLocation.origin, model.getCaretLocation());
  }

  @Test
  public void willDeselectOneWordAtATimeWhenDraggingRightOverSelection()
  {
    model.setText("Some Text Here");
    multiplePressAt(10, 5, 2);
    model.startSelection(TextLocation.at(0, 14));
    model.setCaretLocation(TextLocation.at(0, 5));

    dragAt(101, 15);

    assertEquals(TextLocation.at(0, 14), model.getSelectionLocation());
    assertEquals(TextLocation.at(0, 10), model.getCaretLocation());
  }

  @Test
  public void willDeselectOneWordAtATimeWhenDraggingLeftOverSelection()
  {
    model.setText("Some Text Here");
    multiplePressAt(10, 5, 2);
    model.startSelection(TextLocation.at(0, 5));
    model.setCaretLocation(TextLocation.at(0, 14));

    dragAt(70, 5);

    assertEquals(TextLocation.at(0, 5), model.getSelectionLocation());
    assertEquals(TextLocation.at(0, 9), model.getCaretLocation());
  }

  @Test
  public void willKeepTheOriginalWordSelectedIfDraggedPastRightEdgeOfWordAfterSelectingToTheLeft()
  {
    model.setText("Some Text Here");
    multiplePressAt(10, 5, 2);
    model.startSelection(TextLocation.at(0, 9));
    model.setCaretLocation(TextLocation.at(0, 5));

    dragAt(101, 115);

    assertEquals(TextLocation.at(0, 5), model.getSelectionLocation());
    assertEquals(TextLocation.at(0, 14), model.getCaretLocation());
  }

  @Test
  public void willSelectAllForTripleClick()
  {
    multiplePressAt(10, 5, 3);
    releaseAt(10, 5);

    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(TextLocation.at(0, 9), model.getCaretLocation());
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

    assertEquals(panel.getBounds(), root.dirtyRegions.get(0));
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

    assertEquals(panel.getBounds(), root.dirtyRegions.get(0));
  }

}


