package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class ShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    textBoxSetUp();
    processor = ShiftKeyProcessor.instance;
    modifier = 1;
  }

  @Test
  public void canProcessCharacters()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'A');

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(2, "HAere are four words");
  }

  @Test
  public void canProcessRightArrowAndBeingSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(2, 1, true);
  }

  @Test
  public void canProcessLeftArrowAndBeginSelection()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(0, 1, true);
  }

}
