package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

public class SelectionOnAltShiftCmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    selectionBoxSetUp();
    processor = SelectionOnAltShiftCmdKeyProcessor.instance;
    modifiers = 13;
  }

  @Test
  public void canProcessCharacterAndDoNothing()
  {
    processor.processKey(press(KeyEvent.KEY_A), model);

    assertSelection(1, SELECTION_START_INDEX, true);
  }

}
