//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockPanel;
import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.text.TextLocation;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;

public class SingleLineTextModelTest
{
  TextModel model;
  TextInputPanel panel;

  @Before
  public void setUp()
  {
    panel = new TextBox2Panel();
    MockPanel parent = new MockPanel();
    parent.add(panel);
    parent.setSize(100, 20);
    panel.doLayout();
    model = panel.getModel();
    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
  }

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
    assertEquals(0, model.getLineNumber(model.getCaretIndex()));
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
    assertEquals(-1, model.getXOffset());
  }
  
  @Test
  public void shouldAccountForRightHorizontalAllignment() throws Exception
  {
    panel.getStyle().setHorizontalAlignment("right");
    model.setText("123");

    assertEquals(3, model.getCaretIndex());
    assertEquals(70, model.getXOffset());
  }

  @Test
  public void shouldAccountForVerticalAlignment() throws Exception
  {
    panel.getStyle().setVerticalAlignment("bottom");
    model.setText("123");
    
    assertEquals(10, model.getYOffset());
  }

  @Test
  public void shouldGetIndex() throws Exception
  {
    model.setText("some text");

    assertEquals(TextLocation.at(0, 0), model.getLocationAt(new Point(0, 0)));
    assertEquals(TextLocation.at(0, 1), model.getLocationAt(new Point(10, 0)));
    assertEquals(TextLocation.at(0, 5), model.getLocationAt(new Point(50, 0)));
    assertEquals(TextLocation.at(0, 9), model.getLocationAt(new Point(1000, 0)));
  }

}
