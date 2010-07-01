package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class AltShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    processor = AltShiftKeyProcessor.instance;
    modifier = 9;
  }

  @Test
  public void canProcessSpecialKeys()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'A');

    processor.processKey(mockEvent, model);

    assertTextState(2, "HAere are four words");
  }

  @Test
  public void canProcessRightArrowAndSelectToTheNextWord()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, model);

    assertSelection(5, 1, true);
  }

  @Test
  public void canProcessLeftArrowAndSelectToThePreviousWord()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);
    model.setCaretIndex(9);

    processor.processKey(mockEvent, model);

    assertSelection(5, 9, true);
  }

}
