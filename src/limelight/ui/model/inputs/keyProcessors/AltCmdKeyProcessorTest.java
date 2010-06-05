package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class AltCmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    textBoxSetUp();
    processor = AltCmdKeyProcessor.instance;
    modifier = 12;
  }

  @Test
  public void canProcessRightArrowAndNothingHappens()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(1, 0, false);
  }

  @Test
  public void canProcessCharacterAndNothingHappens()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(1, "Here are four words");
  }

}
