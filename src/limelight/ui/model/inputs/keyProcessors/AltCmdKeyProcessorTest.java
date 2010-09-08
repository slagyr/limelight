package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AltCmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    processor = AltCmdKeyProcessor.instance;
    modifiers = 12;
  }

  @Test
  public void canProcessRightArrowAndNothingHappens()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessCharacterAndNothingHappens()
  {
    processor.processKey(press(KeyEvent.KEY_A), model);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals("Here are four words", model.getText());
    assertEquals(false, model.isSelectionActivated());
  }

}
