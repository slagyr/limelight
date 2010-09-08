package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

    assertEquals(1, model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessCharacterAndNothingHappens()
  {
    processor.processKey(press(KeyEvent.KEY_A), model);

    assertEquals(1, model.getCaretIndex());
    assertEquals("Here are four words", model.getText());
    if(!model.isSelectionActivated())
      assertEquals(0, model.getSelectionIndex());
  }

}
