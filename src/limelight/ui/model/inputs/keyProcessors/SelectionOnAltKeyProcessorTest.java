package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class SelectionOnAltKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionBoxSetUp();
    processor = SelectionOnAltKeyProcessor.instance;
    modifier = 8;
  }

  @Test
  public void canProcessSpecialKeys()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'a');

    processor.processKey(mockEvent, model);

    assertTextState(2, "Ha are four words");
  }

  @Test
  public void canProcessRightArrowAndMoveToStartOfNextWord()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, model);

    assertSelection(5, SELECTION_START_INDEX, false);
  }

  @Test
  public void canProcessLeftArrowAndMoveToEndOfPreviousWord()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);
    model.setCaretIndex(9);

    processor.processKey(mockEvent, model);

    assertSelection(5, 4, false);
  }

}
