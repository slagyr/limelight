package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SelectionOnAltShiftKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    model.startSelection(TextLocation.at(0, 4));
    processor = SelectionOnAltShiftKeyProcessor.instance;
    modifiers = 9;
  }

  @Test
  public void canProcessRightArrowAndSelectToStartOfNextWord()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(TextLocation.at(0, 5), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrowAndSelectToEndOfPreviousWord()
  {
    model.setCaretLocation(TextLocation.at(0, 9));

    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(TextLocation.at(0, 5), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

}
