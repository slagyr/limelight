package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class ExpandedShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpMultiLine();
    processor = ExpandedShiftKeyProcessor.instance;
    modifier = 1;
  }

  @Test
  public void canProcessCharacters()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'A');

    processor.processKey(mockEvent, model);

    assertTextState(2, "HAere are four words");
  }

  @Test
  public void canProcessRightArrowAndBeingSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, model);

    assertSelection(2, 1, true);
  }

  @Test
  public void canProcessLeftArrowAndBeginSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);

    processor.processKey(mockEvent, model);

    assertSelection(0, 1, true);
  }

  @Test
  public void canProcessUpArrowAndMoveUpALineWithSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(11);

    processor.processKey(mockEvent, model);

    assertSelection(3, 11, true);
  }

  @Test
  public void canProcessDownArrowAndMoveDownALineWithSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(3);

    processor.processKey(mockEvent, model);

    assertSelection(11, 3, true);
  }
}
