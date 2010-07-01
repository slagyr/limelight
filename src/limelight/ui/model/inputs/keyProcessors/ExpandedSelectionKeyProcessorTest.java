package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class ExpandedSelectionKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionAreaSetUp();
    processor = ExpandedSelectionOnKeyProcessor.instance;
    modifier = 0;
  }

  @Test
  public void canProcessCharactersAndReplaceSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'a');

    processor.processKey(mockEvent, model);

    assertTextState(2, "Ha are four words");
  }

  @Test
  public void canProcessBackSpaceAndDeleteSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_BACK_SPACE);

    processor.processKey(mockEvent, model);

    assertTextState(1, "H are four words");
  }

  @Test
  public void canProcessRightArrowAndEndSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, model);

    assertSelection(2, SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessLeftArrowAndEndSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);

    processor.processKey(mockEvent, model);

    assertSelection(0, SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessUpArrowAndEndSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(11);

    processor.processKey(mockEvent, model);

    assertSelection(3, SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessDownArrowAndEndSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(3);

    processor.processKey(mockEvent, model);

    assertSelection(11, SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessTheReturnKeyAndRemoveAllSelectedTextWhenStartingNewLine()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_ENTER, '\r');

    processor.processKey(mockEvent, model);

    assertTextState(2, "H\r are four words");
  }
}
