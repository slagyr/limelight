//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.MockPanel;
import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.model.inputs.*;
import limelight.util.Box;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.Assert.assertEquals;

public class AbstractKeyProcessorTest
{
  public final int SELECTION_START_INDEX = 4;

  public TextModel model;
  public MockTextContainer container;
  public KeyProcessor processor;
  public MockKeyEvent mockEvent;
  public MockPanel parent;
  public int modifier;

  public void setUpSingleLine()
  {
    container = new MockTextContainer();
    container.bounds = new Box(0, 0, 150, 20);
    model = new SingleLineTextModel(container);
    setUp();
  }

  public void setUpMultiLine()
  {
    container = new MockTextContainer();
    container.bounds = new Box(0, 0, 150, 75);
    model = new MultiLineTextModel(container);
    setUp();
  }

  private void setUp()
  {
    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
    model.setText("Here are four words");
    model.setCaretIndex(1);

    model.setSelectionIndex(0);
    model.setSelectionOn(false);
  }

  protected void selectionBoxSetUp()
  {
    setUpSingleLine();
    model.setSelectionIndex(SELECTION_START_INDEX);
    model.setSelectionOn(true);
  }

  protected void selectionAreaSetUp()
  {
    setUpMultiLine();
    model.setSelectionIndex(SELECTION_START_INDEX);
    model.setSelectionOn(true);
  }

  public class MockKeyEvent extends KeyEvent
  {

    public MockKeyEvent(int modifiers, int keyCode)
    {
      super(new Panel(), 1, 123456789l, modifiers, keyCode, ' ');
    }

    public MockKeyEvent(int modifiers, int keyCode, char c)
    {
      super(new Panel(), 1, 123456789l, modifiers, keyCode, c);
    }
  }

  public void assertSelection(int cursorIndex, int selectionIndex, boolean selectionOn)
  {
    assertEquals(cursorIndex, model.getCaretIndex());
    assertEquals(selectionIndex, model.getSelectionIndex());
    assertEquals(selectionOn, model.isSelectionOn());
  }

  public void assertTextState(int caretIndex, int selectionIndex, String text)
  {
    assertEquals(caretIndex, model.getCaretIndex());
    assertEquals(text, model.getText());
    if(!model.isSelectionOn())
      assertEquals(selectionIndex, model.getSelectionIndex());
  }

}
