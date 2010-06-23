package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class SelectionOnAltShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionBoxSetUp();
    processor = SelectionOnAltShiftKeyProcessor.instance;
    modifier = 9;
  }

  @Test
  public void canProcessSpecialKeys()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'A');

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(2, "HA are four words");
  }

  @Test
  public void canProcessRightArrowAndSelectToStartOfNextWord()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(5, SELECTION_START_INDEX, true);
  }

  @Test
  public void canProcessLeftArrowAndSelectToEndOfPreviousWord()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);
    modelInfo.setCaretIndex(9);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(5, 4, true);
  }

}
