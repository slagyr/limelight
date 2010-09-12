//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.model.MockParentPanel;
import limelight.ui.text.TextLocation;
import limelight.util.Box;
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
    panel = new TextBoxPanel();
    MockParentPanel parent = new MockParentPanel();
    parent.add(panel);
    parent.setSize(100, 20);
    panel.doLayout();
    model = panel.getModel();
    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
  }

  @Test
  public void canGetTheSelectedRegion()
  {
    model.setText("blah");
    model.startSelection(TextLocation.origin);

    assertEquals(new Box(0, 0, 40, 11), model.getSelectionRegions().get(0));
  }

  @Test
  public void willAlwaysReturnZeroForTheLineNumber()
  {
    assertEquals(0, model.getCaretLocation().line);
  }

  @Test
  public void shouldPositionCaretWithNoText() throws Exception
  {
    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(0, model.getXOffset());
  }

  @Test
  public void shouldPositionCaretAfterAbitOfText() throws Exception
  {
    model.setText("one two three");
    model.setOffset(0, 0);
    model.setCaretLocation(TextLocation.at(0, 3));

    assertEquals(TextLocation.at(0, 3), model.getCaretLocation());
    assertEquals(0, model.getXOffset());
  }

  @Test
  public void shouldPositionCaretAfterFallingOffTheRight() throws Exception
  {
    model.setText("one two three four");
    model.setOffset(0, 0);
    model.setCaretLocation(TextLocation.at(0, 11));

    assertEquals(TextLocation.at(0, 11), model.getCaretLocation());
    assertEquals(-60, model.getXOffset());
  }

  @Test
  public void shouldPositionCaretAfterFallingOffTheRightButNotLeaveSpaceOnTheRight() throws Exception
  {
    model.setText("one two three");
    model.setOffset(0, 0);
    model.setCaretLocation(TextLocation.at(0, 11));

    assertEquals(TextLocation.at(0, 11), model.getCaretLocation());
    assertEquals(-31, model.getXOffset());
  }

  @Test
  public void shouldPositionCaretAfterFallingOffTheLeft() throws Exception
  {
    model.setText("one two three four");
    model.setCaretLocation(TextLocation.at(0, 7));

    assertEquals(TextLocation.at(0, 7), model.getCaretLocation());
    assertEquals(-20, model.getXOffset());
  }

  @Test
  public void shouldPositionCaretAfterFallingOffTheLeftButNotLeaveSpaceOnLeft() throws Exception
  {
    model.setText("one two three");
    model.setCaretLocation(TextLocation.at(0, 2));

    assertEquals(TextLocation.at(0, 2), model.getCaretLocation());
    assertEquals(0, model.getXOffset());
  }

  @Test
  public void shouldLeaveRoomOnTheRightSideForTheCaret() throws Exception
  {
    model.setText("one two th");
    
    assertEquals(TextLocation.at(0, 10), model.getCaretLocation());
    assertEquals(-1, model.getXOffset());
  }
  
  @Test
  public void shouldAccountForRightHorizontalAllignment() throws Exception
  {
    panel.getStyle().setHorizontalAlignment("right");
    model.setText("123");

    assertEquals(TextLocation.at(0, 3), model.getCaretLocation());
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

    @Test
  public void gettingOffsetForLineWhenTextIsTooBigToFit() throws Exception
  {
    model.setText("one two three four");
    model.setOffset(0, 0);
    model.setCaretLocation(TextLocation.at(0, 11));

    assertEquals(-60, model.getXOffset(model.getLines().get(0)));
  }

}
