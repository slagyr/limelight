package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

public class AltShiftCmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    processor = AltShiftCmdKeyProcessor.instance;
    modifiers = 13;
  }

  @Test
  public void canProcessRightArrowAndNothingHappens()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertSelection(1, 0, false);
  }

  @Test
  public void canProcessCharacterAndNothingHappens()
  {
    processor.processKey(press(KeyEvent.KEY_A), model);

    assertTextState(1, 0, "Here are four words");
  }

}
