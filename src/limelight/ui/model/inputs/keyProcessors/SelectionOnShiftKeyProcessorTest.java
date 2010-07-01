package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class SelectionOnShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionBoxSetUp();
    processor = SelectionOnShiftKeyProcessor.instance;
    modifier = 1;
  }

  @Test
  public void canProcessRightArrowAndContinueSelectionToTheRight()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, model);

    assertSelection(2, SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessLeftArrowAndContinueSelectionToTheLeft()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);

    processor.processKey(mockEvent, model);

    assertSelection(0, SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessCharacterAndPlaceUppercaseCharAtCursorIndex()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'A');

    processor.processKey(mockEvent, model);

    assertTextState(2, "HA are four words");
  }

}
