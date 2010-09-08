package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AltShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    processor = AltShiftKeyProcessor.instance;
    modifiers = 9;
  }

  @Test
  public void canProcessRightArrowAndSelectToTheNextWord()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(TextLocation.at(0, 5), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrowAndSelectToThePreviousWord()
  {
    model.setCaretLocation(TextLocation.at(0, 9));

    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(TextLocation.at(0, 5), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 9), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

}
