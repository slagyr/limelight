package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class SelectionOnKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionBoxSetUp();
    processor = SelectionOnKeyProcessor.instance;
    modifier = 0;
  }

  @Test
  public void canProcessCharactersAndReplaceSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'a');

    processor.processKey(mockEvent, model);

    assertTextState(2, 1, "Ha are four words");
  }

  @Test
  public void canProcessBackSpaceAndDeleteSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_BACK_SPACE);

    processor.processKey(mockEvent, model);

    assertTextState(1, 1, "H are four words");
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

}
