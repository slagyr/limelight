package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class NormalKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    textBoxSetUp();
    processor = NormalKeyProcessor.instance;
    modifier = 0;
  }

  @Test
  public void canProcessCharacters()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'a');

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(2, "Haere are four words");
  }

  @Test
  public void willNotInsertAnUndefinedCharacter()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, KeyEvent.CHAR_UNDEFINED);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(1, "Here are four words");
  }

  @Test
  public void canProcessBackSpace()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_BACK_SPACE);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(0, "ere are four words");
  }

  @Test
  public void canProcessRightArrow()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(2, 0, false);
  }

  @Test
  public void canProcessLeftArrow()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(0, 0, false);
  }

}
