//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockPanel;
import limelight.ui.MockTypedLayoutFactory;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;

public class TextBoxModelTest
{
  TextModel model;
  TextInputPanel panel;

  @Before
  public void setUp()
  {
    panel = new TextBox2Panel();
    MockPanel parent = new MockPanel();
    parent.add(panel);
    parent.setSize(100, 28);
    panel.doLayout();
    model = panel.getModel();
    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
  }

  @Test
  public void canCalcTheXPosForCursorFromString()
  {
    assertEquals(30, model.getXPosFromText("ABC"));
  }
//
//  @Test
//  public void canTellIfTheCursorIsAtACriticalEdge()
//  {
//    assertEquals(true, model.isCursorAtCriticalEdge(0));
//    assertEquals(true, model.isCursorAtCriticalEdge(model.getXPosFromIndex(0)));
//    assertEquals(true, model.isCursorAtCriticalEdge(model.getXPosFromIndex(model.getText().length() - 5)));
//  }
//
//  @Test
//  public void canShiftTheCursorAndTextLeftIfCriticallyRight()
//  {
//    model.setCaretIndex(0);
//    model.setOffset(0, 0);
//
//    model.setCaretIndex(30);
//
//    assertEquals(true, model.isCursorAtCriticalEdge(model.getXPosFromIndex(model.getCaretIndex())));
//
//    model.calculateXOffset();
//
//    assertEquals(true, model.getOffset().x > 0);
//  }
//
//  @Test
//  public void canCutTheXOffsetInHalfWhenTheCursorIsOnTheLeftEdge()
//  {
//    model.calculateXOffset();
//    int offset = model.getOffset().x;
//    model.setCaretIndex(0);
//
//    model.calculateXOffset();
//
//    assertEquals(true, model.getOffset().x <= offset / 2 + 2);
//
//    model.setText("hi");
//    model.calculateXOffset();
//    assertEquals(true, model.getOffset().x == 0);
//
//  }
//
//  @Test
//  public void canCalculateTheTextModelsDimensions()
//  {
//    model.setText("");
//    Dimension dim = model.getTextDimensions();
//    assertEquals(null, dim);
//
//    model.setText("X");
//    dim = model.getTextDimensions();
//    assertEquals(8, dim.width);
//    assertEquals(14, dim.height);
//  }
//
//  @Test
//  public void canGetTheSelectedRegion()
//  {
//    model.setSelectionIndex(0);
//    model.setSelectionOn(true);
//
//    Rectangle region = model.getSelectionRegions().get(0);
//
//    assertEquals(0, region.x);
//    assertEquals(0, region.y);
//    assertEquals(true, region.width > 0);
//    assertEquals(true, region.height > 0);
//  }

  @Test
  public void willAlwaysReturnZeroForTheLineNumber()
  {
    assertEquals(0, model.getLineNumberOfIndex(model.getCaretIndex()));
  }

  @Test
  public void shouldPositionCaretWithNoText() throws Exception
  {
    assertEquals(0, model.getCaretIndex());
    assertEquals(0, model.getXOffset());
  }

  @Test
  public void shouldPositionCaretAfterAbitOfText() throws Exception
  {
    model.setText("one two three");
    model.setOffset(0, 0);
    model.setCaretIndex(3);

    assertEquals(3, model.getCaretIndex());
    assertEquals(0, model.getXOffset());
  }

  @Test
  public void shouldPositionCaretAfterFallingOffTheRight() throws Exception
  {
    model.setText("one two three four");
    model.setOffset(0, 0);
    model.setCaretIndex(11);

    assertEquals(11, model.getCaretIndex());
    assertEquals(-60, model.getXOffset());
  }

  @Test
  public void shouldPositionCaretAfterFallingOffTheRightButNotLeaveSpaceOnTheRight() throws Exception
  {
    model.setText("one two three");
    model.setOffset(0, 0);
    model.setCaretIndex(11);

    assertEquals(11, model.getCaretIndex());
    assertEquals(-31, model.getXOffset());
  }

  @Test
  public void shouldPositionCaretAfterFallingOffTheLeft() throws Exception
  {
    model.setText("one two three four");
    model.setCaretIndex(7);

    assertEquals(7, model.getCaretIndex());
    assertEquals(-20, model.getXOffset());
  }

  @Test
  public void shouldPositionCaretAfterFallingOffTheLeftButNotLeaveSpaceOnLeft() throws Exception
  {
    model.setText("one two three");
    model.setCaretIndex(2);

    assertEquals(2, model.getCaretIndex());
    assertEquals(0, model.getXOffset());
  }

  @Test
  public void shouldLeaveRoomOnTheRightSideForTheCaret() throws Exception
  {
    model.setText("one two th");
    
    assertEquals(10, model.getCaretIndex());
    assertEquals(TextModel.CARET_WIDTH * -1, model.getXOffset());
  }
  
  @Test
  public void shouldAccountForRightHorizontalAllignment() throws Exception
  {
    panel.getStyle().setHorizontalAlignment("right");
    model.setText("123");

    assertEquals(3, model.getCaretIndex());
    assertEquals(-70, model.getXOffset());
  }

//
//  @Test
//  public void shouldCalculateOffsetToTheRightWhenCursorMovesPassedRightEdge() throws Exception
//  {
//    model.setCaretIndex(15); //On the right edge
//
//    assertEquals(-75, model.calculateRightShiftingOffset()); // half the width of the box
//  }
//
//  @Test
//  public void shouldCalulateOffsetToTheRightWhenCaretMovesPassedRightEdgeAndIsAlreadyOffset() throws Exception
//  {
//    model.setOffset(-75, 0);
//    model.setCaretIndex(23);
//
//    int xOffset = model.calculateRightShiftingOffset();
//    assertEquals(-150, xOffset);
//  }
//
//  @Test
//  public void shouldCalculateOffsetToLeftWhenCaretMovesLeftBounds() throws Exception
//  {
//    model.setOffset(-75, 0);
//    model.setCaretIndex(5);
//
//    int xOffset = model.calculateLeftShiftingOffset();
//    assertEquals(0, xOffset);
//
//  }

}
