//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.events.KeyPressedEvent;
import limelight.ui.model.inputs.*;
import limelight.util.Box;

import static org.junit.Assert.assertEquals;

public class AbstractKeyProcessorTest
{
  public final int SELECTION_START_INDEX = 4;

  public TextModel model;
  public MockTextContainer container;
  public KeyProcessor processor;
  public int modifiers;

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

  protected KeyPressedEvent press(int code)
  {
    return new KeyPressedEvent(container, modifiers, code, 0);
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
