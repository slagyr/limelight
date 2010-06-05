package limelight.ui.model.inputs.keyProcessors;

import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class SelectionAltShiftCmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionBoxSetUp();
    processor = SelectionOnAltShiftCmdKeyProcessor.instance;
    modifier = 13;
  }

  @Test
  public void canProcessCharacterAndDoNothing()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(1, SELECTION_START_INDEX, true);
  }

}
